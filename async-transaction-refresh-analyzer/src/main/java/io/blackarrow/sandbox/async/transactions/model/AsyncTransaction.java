package io.blackarrow.sandbox.async.transactions.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
