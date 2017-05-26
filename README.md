# common-data
Amazon Dynamo DB framework to develop and test DAO with different table name for different environment and testing
against in memory database

## How to add a new entity, repository to access it and testing

### Create a new entity without writing any code

 - Define the table in database/table folder. import source env_util.sh to change the table name based on environment.properties
 - Define domain entity in domain package
 - Define a repository under repository package and overwrite getDomainClass() method for basic operation defined in
   repository interface

### Test it against real database
 - Extends test from DynamoDBBaseTest
 - override initializeTableInfoAndRespository() method. Developer need to define table name, indexes and repository
 - Framework will start a local dynamo DB server, create table before each test and run the test.

