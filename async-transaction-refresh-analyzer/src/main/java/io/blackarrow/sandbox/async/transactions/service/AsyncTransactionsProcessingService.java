package io.blackarrow.sandbox.async.transactions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.blackarrow.sandbox.async.transactions.model.AsyncTransaction;
import io.blackarrow.sandbox.async.transactions.model.AsyncTransactionSummary;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class AsyncTransactionsProcessingService {
    public static final File BASE_DIR;
    static {
        URL resource = AsyncTransactionsProcessingService.class.getResource("/");
        BASE_DIR = new File(resource.getFile(), "../../results");
    }

    public static final String PROCESSING = "Processing";
    public static final String PROCESSED = "Processed";
    public static final String FAILED = "Failed";

    private final AsyncTransactionFetcherService asyncTransactionFetcherService;

    private final ObjectMapper objectMapper;

    public AsyncTransactionsProcessingService() {
        objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .registerModule(new JavaTimeModule());
        asyncTransactionFetcherService = new AsyncTransactionFetcherService(objectMapper);
    }


    public File fetchAndProcessAsyncTransactionDetails(LocalDate date, Region region, String env) throws IOException {
        List<AsyncTransaction> asyncTransactions = asyncTransactionFetcherService.getAsyncTransactionsForRegionAndDate(date, region);
        return process(asyncTransactions, date, env);
    }

    private File process(List<AsyncTransaction> asyncTransactions, LocalDate date, String env) throws IOException {
        Map<String, List<AsyncTransaction>> statusGroupedAsyncTransactions =
                asyncTransactions.stream().collect(groupingBy(AsyncTransaction::getStatus));

        AsyncTransactionSummary summary = getAsyncTransactionSummary(date, env, statusGroupedAsyncTransactions);

        File outputFile = new File(BASE_DIR, "/".concat(env)
                .concat("/AsyncTransactionSummary_").concat(date.toString()).concat(".json")
        );

        objectMapper.writeValue(new FileOutputStream(outputFile), summary);
        log.info("Wrote results for env: {}, to file: {}", env, outputFile);
        return outputFile;
    }

    private AsyncTransactionSummary getAsyncTransactionSummary(LocalDate date, String env, Map<String, List<AsyncTransaction>> statusGroupedAsyncTransactions) {

        List<AsyncTransaction> failedAsyncTransactions = getAsyncTransactionsForType(statusGroupedAsyncTransactions, FAILED);
        List<AsyncTransaction> processingAsyncTransactions = getAsyncTransactionsForType(statusGroupedAsyncTransactions, PROCESSING);
        List<AsyncTransaction> processedAsyncTransactions = getAsyncTransactionsForType(statusGroupedAsyncTransactions, PROCESSED);

        return AsyncTransactionSummary.builder()
                .date(date.toString())
                .environment(env)
                .numberFailed(failedAsyncTransactions.size())
                .numberProcessing(processingAsyncTransactions.size())
                .numberProcessed(processedAsyncTransactions.size())
                .failed(failedAsyncTransactions)
                .processing(processingAsyncTransactions)
                .processed(processedAsyncTransactions)
                .build();
    }

    private List<AsyncTransaction> getAsyncTransactionsForType(Map<String, List<AsyncTransaction>> statusGroupedAsyncTransactions, String failed) {
        return Optional.ofNullable(statusGroupedAsyncTransactions.get(failed)).orElse(List.of())
                .stream()
                .sorted(Comparator.comparing(AsyncTransaction::getRequestTimestamp).reversed())
                .collect(Collectors.toList());
    }

}
