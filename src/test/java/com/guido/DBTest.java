package com.guido;

import com.guido.dao.*;
import com.guido.dao.order.*;
import com.guido.dao.person.*;
import com.guido.model.*;
import com.guido.model.builder.PersonBuilder;
import com.guido.parser.*;
import com.guido.populator.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import org.mockito.Mockito;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * DBTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/test-context.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBTest {

    private static final Logger log = LoggerFactory.getLogger(DBTest.class);

    private static final String VERTICAL_BAR = "\\|";
    private static final String COMMA = ",";

    @Autowired
    @Qualifier("h2DataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("personDAO")
    private DAO<Person> personDAO;

    @Autowired
    @Qualifier("orderDAO")
    private DAO<Order> orderDAO;

    private final Path personDataCsvFile;
    private final Path orderDataCsvFile;

    private final CSVRowParser<Person> personCSVRowParser;
    private final CSVRowParser<Order>  orderCSVRowParser;

    /**
     * @throws URISyntaxException
     */
    public DBTest() throws URISyntaxException {

        personDataCsvFile = Paths.get(getClass().getResource("/data/persons.csv").toURI());
        orderDataCsvFile = Paths.get(getClass().getResource("/data/orders.csv").toURI());

        personCSVRowParser = CSVRowParserFactory.newPersonCSVRowParser();
        orderCSVRowParser = CSVRowParserFactory.newOrderCSVRowParser();

    }

    /**
     * Empty the tables before every test is performed
     *
     * @throws URISyntaxException
     * @throws IOException
     */
    @Before
    public void resetDB() throws URISyntaxException, IOException {
        String resetSQL = new String(Files.readAllBytes(Paths.get(getClass().getResource("/sql/reset.sql").toURI())), StandardCharsets.UTF_8);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(resetSQL);
    }

    @Test
    public void testPersonDBPopulatorMock() throws IOException {

        try (BufferedReader bufferedReader = Files.newBufferedReader(personDataCsvFile, StandardCharsets.UTF_8)) {
            bufferedReader.lines().forEachOrdered(log::info);
        }

        CSVRowParser<Person> personCSVParser = Mockito.mock(CSVRowParser.class);
        personDAO = Mockito.mock(DAO.class);
        DBPopulator.populate(personDAO, personCSVParser, COMMA, personDataCsvFile);

        PersonBuilder personBuilder = new PersonBuilder();
        List<Person> persons = new ArrayList<>();
        persons.add(personBuilder.withId(1).withFirstName("Ola").withLastName("Hansen").withStreet("Timoteivn").withCity("Sandnes").build());
        persons.add(personBuilder.withId(2).withFirstName("Tove").withLastName("Svendson").withStreet("Borgvn").withCity("Stavanger").build());
        persons.add(personBuilder.withId(3).withFirstName("Kari").withLastName("Pettersen").withStreet("Storgt").withCity("Stavanger").build());

        when(personDAO.findAll()).thenReturn(persons);

        assertThat(personDAO.findAll(), equalTo(persons));

    }

    @Test
    public void testPersonDBPopulator() throws IOException {

        DBPopulator.populate(
                personDAO,
                personCSVRowParser,
                COMMA,
                personDataCsvFile
        );

        testPersonPopulation();

    }

    @Test
    public void testPersonDBPopulator_NamedParametersStyle() throws IOException {

        DBPopulator
                .populate(personDAO)
                .readingDataFrom(personDataCsvFile)
                .usingCSVRowParser(personCSVRowParser)
                .usingCSVSeparator(COMMA)
                .thatsAll();

        testPersonPopulation();

    }

    @Test
    public void testPersonDBBatchPopulator() {

        IntStream
                .rangeClosed(1, 7)
                .forEach(this::testPersonDBBatchPopulator);

    }

    @Test
    public void testPersonDBQuery() throws IOException {

        DBPopulator.populate(personDAO, personCSVRowParser, COMMA, personDataCsvFile);
        DBPopulator.populate(orderDAO, orderCSVRowParser, VERTICAL_BAR, orderDataCsvFile);

        testPersonPopulation();
        testOrderPopulation();

        PersonDAO castedPersonDAO = (PersonDAO) personDAO;

        List<Person> personsA = castedPersonDAO.findPersonsWithAtLeastOneOrder();
        List<Person> personsB = castedPersonDAO.findPersonsWithAtLeastOneOrderInnerStyle();

        assertThat(personsA.containsAll(personsB), is(true));
        assertThat(personsB.containsAll(personsA), is(true));

        /*for(Person p : personsA)
            log.info("PERSONS GROUP A {}", p);

        for(Person p : personsB)
            log.info("PERSONS GROUP B {}", p);*/

    }

    @Test
    public void testOrderDBPopulator() throws IOException {

        DBPopulator.populate(
                orderDAO,
                orderCSVRowParser,
                VERTICAL_BAR,
                orderDataCsvFile
        );

        testOrderPopulation();

    }

    @Test
    public void testOrderDBPopulator_NamedParametersStyle() throws IOException {

        DBPopulator
                .populate(orderDAO)
                .readingDataFrom(orderDataCsvFile)
                .usingCSVRowParser(orderCSVRowParser)
                .usingCSVSeparator(VERTICAL_BAR)
                .thatsAll();

        testOrderPopulation();

    }

    @Test
    public void testOrderDBBatchPopulator() {

        IntStream
                .rangeClosed(1, 9)
                .forEach(this::testOrderDBBatchPopulator);

    }

    @Test
    public void testOrderDBQuery() throws IOException {

        DBPopulator.populate(personDAO, personCSVRowParser, COMMA, personDataCsvFile);
        DBPopulator.populate(orderDAO, orderCSVRowParser, VERTICAL_BAR, orderDataCsvFile);

        testPersonPopulation();
        testOrderPopulation();

        OrderDAO castedOrderDAO = (OrderDAO) orderDAO;

        List<Order> orders = castedOrderDAO.findAllWithPersonName();

        assertThat(orders.get(0).getPersonName(), isEmptyOrNullString());
        assertThat(orders.get(2).getPersonName(), equalTo("Alien"));

        /*for(Order o : orders)
            log.info("ORDER WITH NAME {}", o);*/

    }

    private void testPersonDBBatchPopulator(int batchSize) {

        log.info("testPersonDBBatchPopulator() - BATCH SIZE: {}", batchSize);

        try {

            resetDB();

            DBPopulator
                    .populate(personDAO)
                    .readingDataFrom(personDataCsvFile)
                    .usingCSVRowParser(personCSVRowParser)
                    .usingCSVSeparator(COMMA)
                    .withBatchSize(batchSize)
                    .thatsAll();

            testPersonPopulation();

        } catch (Exception e) {
            log.error("testPersonDBBatchPopulator() caught an Exception", e);
        }
    }

    private void testOrderDBBatchPopulator(int batchSize) {

        log.info("testOrderDBBatchPopulator() - BATCH SIZE: {}", batchSize);

        try {

            resetDB();

            DBPopulator.populateBatch(
                    orderDAO,
                    orderCSVRowParser,
                    VERTICAL_BAR,
                    orderDataCsvFile,
                    batchSize
            );

            testOrderPopulation();

        } catch (Exception e) {
            log.error("testOrderDBBatchPopulator() caught an Exception", e);
        }
    }

    private void testPersonPopulation() {

        List<Person> persons = personDAO.findAll();

        assertThat(persons.size(), is(5));

        assertThat(persons.get(2).getFirstName(), not(equalTo("Guido")));
        assertThat(persons.get(2).getFirstName(), equalTo("Kari"));

        assertThat(personDAO.findById(3).getFirstName(), not(equalTo("Guido")));
        assertThat(personDAO.findById(3).getFirstName(), equalTo("Kari"));

    }

    private void testOrderPopulation() {

        List<Order> orders = orderDAO.findAll();

        assertThat(orders.size(), is(7));

        assertThat(orders.get(3).getOrderNumber(), not(equalTo("Guido")));
        assertThat(orders.get(3).getOrderNumber(), equalTo("2003"));

        assertThat(orderDAO.findById(13).getOrderNumber(), not(equalTo("Guido")));
        assertThat(orderDAO.findById(13).getOrderNumber(), equalTo("2003"));

    }
}
