package com.guido.parser;

/**
 * Parse a CSV row into an entity (i.e. a pojo)
 */
@FunctionalInterface
public interface CSVRowParser<E> {

    public E parse(String row, String separator);

}
