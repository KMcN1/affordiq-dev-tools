package io.blackarrow.sandbox.async.transactions;

import io.blackarrow.sandbox.async.transactions.service.AsyncTransactionsProcessingService;
import org.assertj.core.util.Arrays;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncTransactionsProcessingServiceTest {

    AsyncTransactionsProcessingService asyncTransactionsProcessingService = new AsyncTransactionsProcessingService();

    @Test
    void testCreateOutputDir() {
        String[] baseDirContents = AsyncTransactionsProcessingService.BASE_DIR.list();

        assertThat(Arrays.asList(baseDirContents), Matchers.containsInAnyOrder("demo", "test"));
    }

    @Test
    @Disabled
    void testFetchAndProcessAsyncTransactionDetails() throws IOException {
        LocalDate date = LocalDate.now().minusDays(1);
        Region region = Region.EU_WEST_2;
        File outputFile = asyncTransactionsProcessingService.fetchAndProcessAsyncTransactionDetails(date, region, "demo");

        assertTrue(outputFile.exists());
    }
}