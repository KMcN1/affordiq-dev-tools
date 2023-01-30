package io.blackarrow.sandbox.async.transactions.model.report;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AsyncTransactionReportSummary {

    private List<LocalDate> dates;

    private List<AsyncTransactionReport> reports;


}

