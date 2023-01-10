package io.blackarrow.sandbox.async.transactions.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AsyncTransactionSummary {

    String environment;
    String date;

    int numberFailed;
    int numberProcessing;
    int numberProcessed;

    List<AsyncTransaction> failed;
    List<AsyncTransaction> processing;
    List<AsyncTransaction> processed;
}
