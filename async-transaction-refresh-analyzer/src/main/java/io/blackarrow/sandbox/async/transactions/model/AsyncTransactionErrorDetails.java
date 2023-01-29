package io.blackarrow.sandbox.async.transactions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
