# java-assessment-db-populate-and-query

Although trivial, this project deals with a number of Java aspects.

In particular it involves the _DAO_ facade design pattern, CSV file stream & process methods, Spring JDBC templates, in-memory H2 DB for integration testing, Java8 streams, functional-style interface design, code reusabilty through the use of generics, TDD and integration testing using Spring.

Here is the assessment specification:

```
Assumptions and prerequisites for this exercise:

There exist a DB with the following two tables

    PERSON [ PERSON_ID (Primary Column), LAST_NAME, FIRST_NAME, STREET, CITY ]
    ORDER [ ORDER_ID (Primary Column), ORDER_NO, PERSON_ID ]

Input files to be processed

    persons.csv – The file has Person ID, First Name, Last Name, Street, City separated by comma (‘,’)
    orders.csv  – The file Order ID, Order Number and Person ID separated by pipe (‘|’) character

Write a program to

    Read the files and populate the tables

    Fetch data and print
        Persons with at least one Order
        All Orders with First Name of the corresponding person (if available)
```