package io.blackarrow.sandbox.async.transactions.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetails {
    private String userId;
    private String institutionId;
    private String accountId;
}
