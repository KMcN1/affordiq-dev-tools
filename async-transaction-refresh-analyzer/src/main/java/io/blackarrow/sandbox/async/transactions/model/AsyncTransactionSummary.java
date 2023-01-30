package io.blackarrow.sandbox.async.transactions.model;


import io.blackarrow.sandbox.async.transactions.model.ddb.AsyncTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
