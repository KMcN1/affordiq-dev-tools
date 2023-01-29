package io.blackarrow.sandbox.async.transactions.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.blackarrow.sandbox.async.transactions.model.annot.DynamoDbTable;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTransaction {

    String taskId;
    String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime requestTimestamp;
    String status;
    String accountId;
    String institutionId;
    String resultsUri;
    List<AsyncTransactionErrorDetails> errorDetails;
}
