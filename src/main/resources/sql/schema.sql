CREATE TABLE PERSON (
   PERSON_ID  INTEGER PRIMARY KEY,
   LAST_NAME  VARCHAR(255),
   FIRST_NAME VARCHAR(255),
   STREET     VARCHAR(255),
   CITY       VARCHAR(255)
);

CREATE TABLE CLIENT_ORDER (
   ORDER_ID  INTEGER PRIMARY KEY,
   ORDER_NO  INTEGER,
   PERSON_ID INTEGER
);