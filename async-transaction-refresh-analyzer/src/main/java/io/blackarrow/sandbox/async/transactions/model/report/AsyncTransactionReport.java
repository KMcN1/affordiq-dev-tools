package io.blackarrow.sandbox.async.transactions.model.report;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AsyncTransactionReport {

    private String userId;
    private String institutionId;
    private String accountId;
    private Map<LocalDate, Integer> numberOfTimesProcessedOnDate;

}
