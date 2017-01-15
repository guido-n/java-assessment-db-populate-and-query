package com.guido.dao.order;

import com.guido.model.Order;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class OrderBatchPreparedStatementSetter implements BatchPreparedStatementSetter {

    List<Order> entities;

    public OrderBatchPreparedStatementSetter(List<Order> entities) {
        this.entities = entities;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setInt(1, entities.get(i).getId());
        ps.setString(2, entities.get(i).getOrderNumber());
        ps.setInt(3, entities.get(i).getPersonId());
    }

    @Override
    public int getBatchSize() {
        return entities.size();
    }
}