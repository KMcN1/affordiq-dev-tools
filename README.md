# afford-iq-dev-tools
Dev And Analysis Tools For Afford IQ

## AsyncTransactionAnalyzerApp

### Prerequisites:

To be able to successfully run this app, you first need to set up AWS on your PC/Laptop (see api-affordiq-core/README.md)

...and also fetch a valid token using script 'mfa.sh'. 

### Run the App

First build the project with mvn clean package.

#### Then either run from within IntelliJ
- Go to 'async-transaction-refresh-analyzer/src/main/java/io/blackarrow/sandbox/async/transactions'
- Right click on class 'AsyncTransactionAnalyzerApp' and select 
      
      Run 'AsyncTransactionAnalyzerApp...main()'


#### ...or run the Spring Boot Jar
- Get the Spring Boot Jar from the 'target' directory of the project. 
- Run With:   

      java -jar async-transaction-refresh-analyzer-${maven_version}.jar

#### Specify Date

NOTE: If you run the app with no arguments, it will fetch transactions for the current day.

If you want to see the results for a different date, add the appropriate parameter (in ISO Date Formar), e.g.

      java -jar async-transaction-refresh-analyzer-${maven_version}.jar-Ddate=2022-10-09

### Results

When the run has complete, you will be able to view the async transactions summary for the given date day in:

-  ${user.home}/BlackArrow/TransactionsRefreshSummaries/demo
-  ${user.home}/BlackArrow/TransactionsRefreshSummaries/test

