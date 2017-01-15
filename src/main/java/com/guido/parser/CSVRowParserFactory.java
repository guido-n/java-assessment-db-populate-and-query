package com.guido.parser;

import com.guido.model.Order;
import com.guido.model.builder.OrderBuilder;
import com.guido.model.Person;
import com.guido.model.builder.PersonBuilder;

public class CSVRowParserFactory {

    /**
     *
     * @param row
     * @param separator
     * @param length
     * @return
     */
    private static String[] splitRow(String row, String separator, int length) {

        String[] parts = row.split(separator);

        if (parts.length != length) {
            throw new IllegalArgumentException("Wrong CSV columns: " + row);
        }

        return parts;
    }

    /**
     *
     * @return
     */
    public static CSVRowParser<Order> newOrderCSVRowParser() {
        return (String row, String separator) -> {

            String[] parts = splitRow(row, separator, 3);

            int id, orderNo;

            try {
                id = Integer.parseInt(parts[0].trim());
                orderNo = Integer.parseInt(parts[2].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Bad CSV row: " + row, e);
            }

            return new OrderBuilder()
                    .withId(id)
                    .withOrderNumber(parts[1].trim())
                    .withPersonId(orderNo)
                    .build();

        };
    }

    /**
     *
     * @return
     */
    public static CSVRowParser<Person> newPersonCSVRowParser() {
        return (String row, String separator) -> {

            String[] parts = splitRow(row, separator, 5);

            int id;

            try {
                id = Integer.parseInt(parts[0].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Bad CSV row: " + row, e);
            }

            return new PersonBuilder()
                    .withId(id)
                    .withFirstName(parts[1].trim())
                    .withLastName(parts[2].trim())
                    .withStreet(parts[3].trim())
                    .withCity(parts[4].trim())
                    .build();

        };
    }

}
