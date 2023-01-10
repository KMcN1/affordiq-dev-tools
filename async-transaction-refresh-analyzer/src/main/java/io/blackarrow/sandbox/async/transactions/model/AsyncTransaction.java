package io.blackarrow.sandbox.async.transactions.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.blackarrow.sandbox.async.transactions.model.annot.DynamoDbTable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Value
@ToString
@DynamoDbTable(tableName = "asyncTransactions")
@DynamoDbImmutable(builder = AsyncTransaction.AsyncTransactionBuilder.class)
public class AsyncTransaction {

    @Getter(onMethod = @__({@DynamoDbPartitionKey, @DynamoDbAttribute("PK")}))
    String taskId;

    @Getter
    String userId;

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime requestTimestamp;

    @Getter
    String status;

    @Getter
    String resultsUri;

    @Getter
    String accountId;

    @Getter
    String institutionId;

    @Getter
    List<AsyncTransactionErrorDetails> errorDetails;
}
