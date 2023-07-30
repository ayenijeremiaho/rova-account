# ROVA Account - API (A Bank account simulator).

## This is the first phase on this project and is still being developed.

### Description

The project hopes to create a fully functional application capable of simulating the banking 
experience of account creation, debiting and crediting.

### Instructions to use

- Clone this repository
- Import into your preferred IDE as a maven project
- Now, the project is all yours.


### Project Folder Structure

- account -> Contains the logic behind account creation, debiting and crediting
- config -> Contains  configuration for OpenAPI
- customer -> Contains the user entity and services
- exception -> Contains all exceptions classes including the exception controller that intercepts runtime exceptions
- transaction -> Contains the logic behind transaction entries
- utility -> Contains static methods

### Package terminologies

- controller -> A class file containing REST endpoints that can be accessible via the browser
- dto -> Data Transfer Object, used to communicate data with the client, also helps create abstraction from the database entities
- service -> Contains an interface class and an implementation package
- implementation -> Housed within the service package, this contains the business logic implementation for the interface implemented from the service package
- repository -> Contains specific entity-class interfaces that intermediates with the database via JPA and Hibernate
- model -> Contains classes that maps to database tables, the class name becomes the table name and the class fields maps to the database column
- enums -> Contains a class-like file with a list of constant variables

### Run Test

- Navigate to http://localhost:8080/swagger-ui/index.html#/
- Use sample request from below
- If DB connectivity fails, another credential can easily be created from https://uibakery.io/sql-playground
- SAMPLE REQUEST for New Account customers
    ```
    {
      "firstname": "jeremiah",
      "surname": "jay",
      "accountType": "CURRENT",
      "initialCredit": 1.0
    }
  ```
- SAMPLE REQUEST for adding account to existing customers. The customerId value is dependent on the result of the new account users above
    ```
    {
      "customerId": ${id},
      "accountType": "CURRENT",
      "initialCredit": 1.0
    }
    ```
- The two other APIs just need to append either the customerId or accountNo, both can be retrieved from either of the two requests above

### Run UNIT test

- Navigate to the src/test/java folder
- Right click on the com.ayenijeremiaho.pastebinapi and run the test
- For intelliJ users, right click on project root and click on 'Run Tests ..'
  
### Contact

Your Name - [@twitter_handle](https://twitter.com/ayenijeremiaho) - ayenijeremiah@gmail.com

Project Link: [https://github.com/ayenijeremiaho/rova-account](https://github.com/ayenijeremiaho/rova-account)