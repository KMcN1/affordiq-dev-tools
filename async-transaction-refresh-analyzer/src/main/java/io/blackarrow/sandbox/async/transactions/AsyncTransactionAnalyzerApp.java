package io.blackarrow.sandbox.async.transactions;

import io.blackarrow.sandbox.async.transactions.service.AsyncTransactionsProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;


@Slf4j
@SpringBootApplication
public class AsyncTransactionAnalyzerApp implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(AsyncTransactionAnalyzerApp.class, args);
    }

    public static final File BASE_DIR;

    static {
        BASE_DIR =
                new File(System.getProperty("user.home").concat("/BlackArrow/TransactionsRefreshSummaries"));
    }

    private static final String ARG_DATE = "date";
    private static final String ARG_OUTPUT_DIR = "output_dir";
    private static final Map<String, Region> ENV_MAP = Map.of("demo", Region.EU_WEST_2, "test", Region.US_EAST_2);

    @Override
    public void run(ApplicationArguments args) {
        File outputDir = getOutputDirectory(args);
        LocalDate date = getDateToUse(args);

        AsyncTransactionsProcessingService asyncTransactionsProcessingService = new AsyncTransactionsProcessingService(outputDir);

        log.info("Checking AsyncTransactions for date {}, with output directory {}", date, outputDir);

        ENV_MAP.forEach((env, region) -> {
            try {
                asyncTransactionsProcessingService.fetchAndProcessAsyncTransactionDetails(date, region, env);
            } catch (IOException e) {
                log.error("Error while processing results:", e);
            }
        });
        logSuccess(outputDir);
    }

    private static LocalDate getDateToUse(ApplicationArguments argMap) {
        LocalDate date = LocalDate.now().minusDays(0); // Default
        if (argMap.containsOption(ARG_DATE) && !argMap.getOptionValues(ARG_DATE).isEmpty()) {
            try {
                date = LocalDate.parse(argMap.getOptionValues(ARG_DATE).get(0));
            } catch (DateTimeParseException ex) {
                log.error("There was a problem with the inout 'date' argument: ", ex);
                System.exit(1);
            }

        }
        return date;
    }

    private File getOutputDirectory(ApplicationArguments argMap) {
        File outputDir = BASE_DIR;
        if (argMap.containsOption(ARG_OUTPUT_DIR) && !argMap.getOptionValues(ARG_OUTPUT_DIR).isEmpty()) {
            outputDir = new File(argMap.getOptionValues(ARG_OUTPUT_DIR).get(0));
        }
        return outputDir;
    }


    private static void logSuccess(File outputDir) {
        log.info("\n=================================================================================\n" +
                String.format("Processing finished. View the results in directory: %s", outputDir.getAbsolutePath()) +
                "\n================================================================================= ");
    }

}
