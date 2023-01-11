package io.blackarrow.sandbox.async.transactions;

import io.blackarrow.sandbox.async.transactions.service.AsyncTransactionsProcessingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncTransactionsProcessingServiceTest {

    AsyncTransactionsProcessingService asyncTransactionsProcessingService = new AsyncTransactionsProcessingService(null);

    @Test
    @Disabled
    void testFetchAndProcessAsyncTransactionDetails() throws IOException {
        LocalDate date = LocalDate.now().minusDays(1);
        Region region = Region.EU_WEST_2;
        File outputFile = asyncTransactionsProcessingService.fetchAndProcessAsyncTransactionDetails(date, region, "demo");

        assertTrue(outputFile.exists());
    }
}