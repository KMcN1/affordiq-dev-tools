package io.blackarrow.sandbox.async.transactions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.blackarrow.sandbox.async.transactions.model.report.AccountDetails;
import io.blackarrow.sandbox.async.transactions.model.ddb.AsyncTransaction;
import io.blackarrow.sandbox.async.transactions.model.report.AsyncTransactionReport;
import io.blackarrow.sandbox.async.transactions.model.AsyncTransactionSummary;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IMPORTANT: This class is still a Work In Progress.
 */
@Slf4j
class ProcessAnalyticsReportService {

    private static final File BASE_DIR = new File(
            "async-transaction-refresh-analyzer/results");

    private static ObjectMapper objectMapper;

    public static void main(String[] args) throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        createReport();
    }

    static void createReport() throws IOException {
        String environment = "demo";

        File summariesDir = new File(BASE_DIR, environment);
        List<AsyncTransactionSummary> asyncTransactionSummaries = new ArrayList<>();
        File[] files = summariesDir.listFiles();
        if (null == files || files.length == 0) {
            log.info("No files found in directory {}", summariesDir);
            System.exit(1);
        }
        for (File file : files) {
            asyncTransactionSummaries.add(objectMapper.readValue(file, AsyncTransactionSummary.class));
        }

        List<LocalDate> dateRange = asyncTransactionSummaries.stream()
                .map(s -> LocalDate.parse(s.getDate()))
                .sorted().collect(Collectors.toList());

        List<AsyncTransaction> processed = asyncTransactionSummaries.stream()
                .map(AsyncTransactionSummary::getProcessed)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Map<AccountDetails, List<AsyncTransaction>> mappedByAccount = processed.stream()
                .collect(Collectors.groupingBy(s ->
                        new AccountDetails(s.getUserId(), s.getInstitutionId(), s.getAccountId())
                ));

        List<AsyncTransactionReport> asyncTransactionReports = new ArrayList<>();
        mappedByAccount.forEach((account, v) -> {

            Map<LocalDate, List<AsyncTransaction>> mappedByDate =
                    v.stream().collect(Collectors.groupingBy(t -> t.getRequestTimestamp().toLocalDate()));

            Map<LocalDate, Integer> timesProcessedOnDate = new HashMap<>();
            dateRange.forEach(date ->
                    timesProcessedOnDate.put(date,
                            Optional.ofNullable(mappedByDate.get(date)).map(List::size)
                                    .orElse(0)
                    )
            );
            Map<LocalDate, Integer> timesProcessedOnDate_Sorted = new TreeMap<>(timesProcessedOnDate);

            asyncTransactionReports.add(
                    AsyncTransactionReport.builder()
                            .userId(account.getUserId())
                            .institutionId(account.getInstitutionId())
                            .accountId(account.getAccountId())
                            .numberOfTimesProcessedOnDate(timesProcessedOnDate_Sorted)
                            .build()
            );
        });

        String dateSuffix = getEnvAndDateSuffix(dateRange, environment);

        objectMapper.writeValue(new File(BASE_DIR, "Report_AccountWasRefreshed" + dateSuffix + ".json"), asyncTransactionReports);
    }

    private static String getEnvAndDateSuffix(List<LocalDate> dateRange, String environment) {
        LocalDate FROM = dateRange.get(0);
        LocalDate TO = dateRange.get(dateRange.size() - 1);
        String dateSuffix = "from_".concat(FROM.toString()).concat("_to_").concat(TO.toString());
        return "_".concat(environment).concat("_").concat(dateSuffix);
    }

}