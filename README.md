# afford-iq-dev-tools
Dev And Analysis Tools For Afford IQ

## AsyncTransactionAnalyzerApp

- Build project
- Go to 'async-transaction-refresh-analyzer/src/main/java/io/blackarrow/sandbox/async/transactions'
- Right click on class 'AsyncTransactionAnalyzerApp' and select "Run 'AsyncTransactionAnalyzerApp...main()'"

NOTE: If you run the app with no arguments, it will fetch transactions for the current day.

      If you want to see the results for a different date, add the appropriate parameter (in ISO Date Formar), e.g.
        -Ddate=2022-10-09

When the run has complete, you will be able to view the async transactions summary for the current day in :

-  ${user.home}/BlackArrow/TransactionsRefreshSummaries/demo
-  ${user.home}/BlackArrow/TransactionsRefreshSummaries/test

