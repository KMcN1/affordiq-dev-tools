package io.blackarrow.sandbox.async.transactions;

import io.blackarrow.sandbox.async.transactions.service.AsyncTransactionsProcessingService;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class AsyncTransactionAnalyzerApp {

    private static final Map<String, Region> ENV_MAP = Map.of("demo", Region.EU_WEST_2, "test", Region.US_EAST_2);

    public static void main(String[] args) {
        LocalDate date = LocalDate.now().minusDays(0);
        AsyncTransactionsProcessingService asyncTransactionsProcessingService = new AsyncTransactionsProcessingService();

        ENV_MAP.forEach((env, region) -> {
            try {
                File outputFile = asyncTransactionsProcessingService.fetchAndProcessAsyncTransactionDetails(date, region, env);
            } catch (IOException e) {
                log.error("Error while processing results:", e);
            }
        });
    }

}
