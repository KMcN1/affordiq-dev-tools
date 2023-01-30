package io.blackarrow.sandbox.async.transactions.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.blackarrow.sandbox.async.transactions.model.ddb.AsyncTransaction;
import io.blackarrow.sandbox.async.transactions.model.ddb.AsyncTransactionErrorDetails;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.dynamodb.model.ExecuteStatementResponse;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AsyncTransactionFetcherService {

    private final ObjectMapper objectMapper;

    public AsyncTransactionFetcherService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected List<AsyncTransaction> getAsyncTransactionsForRegionAndDate(LocalDate date, Region region) {
        List<AsyncTransaction> items = getAllAsyncTransactions(getDynamoDbClient(region));
        return getAsyncTransactionsForGivenDate(items, date);
    }

    private List<AsyncTransaction> getAsyncTransactionsForGivenDate(List<AsyncTransaction> items, LocalDate date) {
        return items.stream().filter(item -> item.getRequestTimestamp().toLocalDate().isEqual(date)).collect(Collectors.toList());
    }

    private List<AsyncTransaction> getAllAsyncTransactions(DynamoDbClient dynamoDbClient) {
        ExecuteStatementResponse response = dynamoDbClient.executeStatement(ExecuteStatementRequest.builder()
                .statement("select * from asyncTransactions")
                .build());
        List<AsyncTransaction> items = response.items().stream().map(item -> mapToAsyncTransaction(item)).collect(Collectors.toList());
        return items;
    }

    private AsyncTransaction mapToAsyncTransaction(Map<String, AttributeValue> item) {
        return AsyncTransaction.builder()
                .taskId(item.get("PK").s())
                .userId(item.get("userId").s())
                .requestTimestamp(OffsetDateTime.parse(item.get("requestTimestamp").s()))
                .status(item.get("status").s())
                .accountId(item.get("accountId").s())
                .institutionId(item.get("institutionId").s())
                .resultsUri(item.get("resultsUri").s())
                .errorDetails(createErrorDetails(item))
                .build();
    }

    private List<AsyncTransactionErrorDetails> createErrorDetails(Map<String, AttributeValue> item) {
        List<AsyncTransactionErrorDetails> errorDetailList = null;
        AttributeValue errorDetails = item.get("errorDetails");
        if (errorDetails != null) {
            TypeReference<List<AsyncTransactionErrorDetails>> valueTypeRef = new TypeReference<>() {
            };
            try {
                errorDetailList = objectMapper.readValue(errorDetails.s(), valueTypeRef);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                // Just leave it null
            }
        }
        return errorDetailList;
    }

    public DynamoDbClient getDynamoDbClient(Region region) {
        return DynamoDbClient.builder()
                .region(region)
                .build();
    }
}
