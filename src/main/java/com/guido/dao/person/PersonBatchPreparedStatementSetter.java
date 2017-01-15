package com.guido.dao.person;

import com.guido.model.Person;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

class PersonBatchPreparedStatementSetter implements BatchPreparedStatementSetter {

    List<Person> entities;

    public PersonBatchPreparedStatementSetter(List<Person> entities) {
        this.entities = entities;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setInt(1, entities.get(i).getId());
        ps.setString(2, entities.get(i).getLastName());
        ps.setString(3, entities.get(i).getFirstName());
        ps.setString(4, entities.get(i).getStreet());
        ps.setString(5, entities.get(i).getCity());
    }

    @Override
    public int getBatchSize() {
        return entities.size();
    }
}