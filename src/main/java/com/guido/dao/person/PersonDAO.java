package com.guido.dao.person;

import com.guido.dao.DAO;
import com.guido.model.Person;

import com.guido.model.builder.PersonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

import static com.guido.dao.person.Constants.*;

/**
 * PersonDAO
 */
public class PersonDAO implements DAO<Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonDAO.class);

    private static final RowMapper<Person> rowMapper =
            (resultSet, rowNum) ->
                    new PersonBuilder()
                            .withId(resultSet.getInt("PERSON_ID"))
                            .withLastName(resultSet.getString("LAST_NAME"))
                            .withFirstName(resultSet.getString("FIRST_NAME"))
                            .withStreet(resultSet.getString("STREET"))
                            .withCity(resultSet.getString("CITY"))
                            .build();

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Person findById(int id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, new Object[]{id}, rowMapper);
    }

    @Override
    public List<Person> findAll() {
        return this.jdbcTemplate.query(SELECT_ALL_SQL, rowMapper);
    }

    public List<Person> findPersonsWithAtLeastOneOrder() {
        return jdbcTemplate.query(PERSONS_WITH_AT_LEAST_ONE_ORDER_SQL, rowMapper);
    }

    public List<Person> findPersonsWithAtLeastOneOrderInnerStyle() {
        return jdbcTemplate.query(PERSONS_WITH_AT_LEAST_ONE_ORDER_SQL_INNER_STYLE, rowMapper);
    }

    @Override
    public void persist(Person p) {
        jdbcTemplate.update(INSERT_SQL, p.getId(), p.getLastName(), p.getFirstName(), p.getStreet(), p.getCity());
    }

    @Override
    public void persistBatch(List<Person> persons) {
        log.info("PersonDAO.persistBatch() - batch size: {}", persons.size());
        jdbcTemplate.batchUpdate(INSERT_SQL, new PersonBatchPreparedStatementSetter(persons));
    }

}