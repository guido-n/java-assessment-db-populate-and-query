package com.guido.dao.order;

import static com.guido.utils.Utils.loadStringFromResource;

class Constants {

    public static final String INSERT_SQL = "insert into CLIENT_ORDER (ORDER_ID, ORDER_NO, PERSON_ID) values (?, ?, ?)";
    public static final String SELECT_BY_ID_SQL = "select * from CLIENT_ORDER where ORDER_ID = ?";
    public static final String SELECT_ALL_SQL = "select * from CLIENT_ORDER";

    public static final String ORDERS_WITH_PERSON_NAME_SQL = loadStringFromResource("/sql/orders-with-person-name.sql", Constants.class);

}
