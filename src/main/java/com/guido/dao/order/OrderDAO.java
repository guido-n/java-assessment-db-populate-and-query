package com.guido.dao.order;

import com.guido.dao.DAO;
import com.guido.model.Order;

import com.guido.model.builder.OrderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

import static com.guido.dao.order.Constants.*;

/**
 * OrderDAO
 */
public class OrderDAO implements DAO<Order> {

    private static final Logger log = LoggerFactory.getLogger(OrderDAO.class);

    private static final RowMapper<Order> rowMapper =
            (resultSet, rowNum) ->
                    new OrderBuilder()
                            .withId(resultSet.getInt("ORDER_ID"))
                            .withOrderNumber(resultSet.getString("ORDER_NO"))
                            .withPersonId(resultSet.getInt("PERSON_ID"))
                            .build();

    private static final RowMapper<Order> queryRowMapper =
            (resultSet, rowNum) -> {
                Order o = rowMapper.mapRow(resultSet, rowNum);
                o.setPersonName(resultSet.getString("LAST_NAME"));
                return o;
            };

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Order findById(int id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, new Object[]{id}, rowMapper);
    }

    @Override
    public List<Order> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, rowMapper);
    }

    public List<Order> findAllWithPersonName() {
        return jdbcTemplate.query(ORDERS_WITH_PERSON_NAME_SQL, queryRowMapper);
    }

    @Override
    public void persist(Order o) {
        jdbcTemplate.update(INSERT_SQL, o.getId(), o.getOrderNumber(), o.getPersonId());
    }

    @Override
    public void persistBatch(List<Order> orders) {
        log.info("OrderDAO.persistBatch() - batch size: {}", orders.size());
        jdbcTemplate.batchUpdate(INSERT_SQL, new OrderBatchPreparedStatementSetter(orders));
    }
}
