package com.guido.dao.person;

import static com.guido.utils.Utils.loadStringFromResource;

class Constants {

    public static final String INSERT_SQL = "insert into PERSON (PERSON_ID, LAST_NAME, FIRST_NAME, STREET, CITY) values (?, ?, ?, ?, ?)";
    public static final String SELECT_BY_ID_SQL = "select * from PERSON where PERSON_ID = ?";
    public static final String SELECT_ALL_SQL = "select * from PERSON";

    public static final String PERSONS_WITH_AT_LEAST_ONE_ORDER_SQL = loadStringFromResource("/sql/persons-with-at-least-one-order.sql", Constants.class);
    public static final String PERSONS_WITH_AT_LEAST_ONE_ORDER_SQL_INNER_STYLE = loadStringFromResource("/sql/persons-with-at-least-one-order-inner.sql", Constants.class);

}
