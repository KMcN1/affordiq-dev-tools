package io.blackarrow.sandbox.async.transactions.model.ddb;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTransaction {

    String userId;
    String institutionId;
    String accountId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime requestTimestamp;
    String status;
    String taskId;

    String resultsUri;
    List<AsyncTransactionErrorDetails> errorDetails;
}
