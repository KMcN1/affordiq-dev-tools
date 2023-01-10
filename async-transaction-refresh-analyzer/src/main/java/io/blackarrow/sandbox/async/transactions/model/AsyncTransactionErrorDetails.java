package io.blackarrow.sandbox.async.transactions.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;

@Builder
@Value
@DynamoDbImmutable(builder = AsyncTransactionErrorDetails.AsyncTransactionErrorDetailsBuilder.class)
public class AsyncTransactionErrorDetails {

    @Getter
    String subTaskId;

    @Getter
    Integer statusCode;

    @Getter
    String error;

    @Getter
    String errorDescription;

    @Getter
    String subTaskType;
}
