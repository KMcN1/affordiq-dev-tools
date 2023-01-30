package io.blackarrow.sandbox.async.transactions.model.ddb;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTransactionErrorDetails {

    String subTaskId;
    Integer statusCode;
    String error;
    String errorDescription;
    String subTaskType;
}
