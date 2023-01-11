package io.blackarrow.sandbox.async.transactions;

import io.blackarrow.sandbox.async.transactions.service.AsyncTransactionsProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@SpringBootApplication
public class AsyncTransactionAnalyzerApp implements CommandLineRunner {

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
    public void run(String... args) {
        Map<String, String> argMap = getArguments(args);
        File outputDir = getOutputDirectory(argMap);
        LocalDate date = getDateToUse(argMap);
        AsyncTransactionsProcessingService asyncTransactionsProcessingService = new AsyncTransactionsProcessingService(outputDir);

        ENV_MAP.forEach((env, region) -> {
            try {
                asyncTransactionsProcessingService.fetchAndProcessAsyncTransactionDetails(date, region, env);
            } catch (IOException e) {
                log.error("Error while processing results:", e);
            }
        });
        logSuccess(outputDir);
    }

    private static LocalDate getDateToUse(Map<String, String> argMap) {
        LocalDate date = LocalDate.now().minusDays(0); // Default
        if (argMap.containsKey(ARG_DATE)) {
            try {
                date = LocalDate.parse(argMap.get(ARG_DATE));
            } catch (DateTimeParseException ex) {
                log.error("There was a problem with the inout 'date' argument: ", ex);
                System.exit(1);
            }

        }
        return date;
    }

    private File getOutputDirectory(Map<String, String> argMap) {
        File outputDir = BASE_DIR;
        if (argMap.containsKey(ARG_OUTPUT_DIR)) {
            outputDir = new File(argMap.get(ARG_OUTPUT_DIR));
        }
        return outputDir;
    }

    private static Map<String, String> getArguments(String[] args) {
        Map<String, String> argMap = Arrays.stream(args).map(arg -> arg.split("="))
                .collect(Collectors.toMap(arr -> arr[0],
                        arr -> arr.length > 1 ? arr[1] : ""));
        return argMap;
    }

    private static void logSuccess(File outputDir) {
        log.info("\n=================================================================================\n" +
                String.format("Processing finished. View the results in directory: %s", outputDir.getAbsolutePath()) +
                "\n================================================================================= ");
    }

}
