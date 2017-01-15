package com.guido.populator;

import com.guido.dao.DAO;
import com.guido.parser.CSVRowParser;
import com.guido.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Pupolate DB reading entities from a CSV file
 */
public class DBPopulator {

    private static final Logger log = LoggerFactory.getLogger(DBPopulator.class);

    /**
     *
     * @param dao
     * @param parser
     * @param csvSeparator
     * @param csvFile
     * @param <E>
     * @throws IOException
     */
    public static <E> void populate(DAO<E> dao, CSVRowParser<E> parser, String csvSeparator, Path csvFile) throws IOException {

        try (final BufferedReader bufferedReader = Files.newBufferedReader(csvFile, StandardCharsets.UTF_8)) {

            bufferedReader
                    .lines()
                    .skip(1) // skip CSV header
                    .filter(Utils::notNullAndNotEmpty) // check if CSV row is empty
                    .forEach(csvRow -> {
                        E e = parser.parse(csvRow, csvSeparator);
                        // log.info("CSV ROW: \"{}\" ENTITY: \"{}\"", csvRow, e);
                        dao.persist(e);
                    });
        }

    }

    /**
     *
     * @param dao
     * @param parser
     * @param csvSeparator
     * @param csvFile
     * @param insertBatchSize
     * @param <E>
     * @throws IOException
     */
    public static <E> void populateBatch(DAO<E> dao, CSVRowParser<E> parser, String csvSeparator, Path csvFile, int insertBatchSize) throws IOException {

        // a stream object is closeable and it closes the underlying resources (including the file)
        // Files.lines() populates lazily as the stream is consumed
        // https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html#lines-java.nio.file.Path-java.nio.charset.Charset-

        try (final Stream<String> csvRowsStream = Files.lines(csvFile, StandardCharsets.UTF_8)) {

            final List<E> batch = new ArrayList<>(insertBatchSize);

            csvRowsStream
                    .skip(1) // skip CSV header
                    .filter(Utils::notNullAndNotEmpty) // check if CSV row is empty
                    .forEach(csvRow -> {
                        E e = parser.parse(csvRow, csvSeparator);
                        // log.info("CSV ROW: \"{}\" ENTITY: \"{}\"", csvRow, e);
                        batch.add(e);
                        if (batch.size() == insertBatchSize) {
                            dao.persistBatch(batch);
                            batch.clear();
                        }
                    });

            if (!batch.isEmpty())
                dao.persistBatch(batch);
        }

    }

    /**
     *
     * @param dao
     * @return
     */
    public static ParamsHolder populate (DAO dao) {
        ParamsHolder holder = new ParamsHolder();
        holder.setDao(dao);
        return holder;
    }

    /**
     * ParamsHolder
     */
    public static class ParamsHolder {
        private DAO dao;
        private CSVRowParser lineParser;
        String csvSeparator;
        Path csvFile;
        int insertBatchSize = -1;

        public ParamsHolder setDao(DAO dao) {
            this.dao = dao;
            return this;
        }

        public ParamsHolder usingCSVRowParser(CSVRowParser lineParser) {
            this.lineParser = lineParser;
            return this;
        }

        public ParamsHolder usingCSVSeparator(String csvSeparator) {
            this.csvSeparator = csvSeparator;
            return this;
        }

        public ParamsHolder readingDataFrom(Path csvFile) {
            this.csvFile = csvFile;
            return this;
        }

        public ParamsHolder withBatchSize(int insertBatchSize) {
            this.insertBatchSize = insertBatchSize;
            return this;
        }

        public void thatsAll() throws IOException {
            if (insertBatchSize == -1)
                DBPopulator.populate(dao, lineParser, csvSeparator, csvFile);
            else
                DBPopulator.populateBatch(dao, lineParser, csvSeparator, csvFile, insertBatchSize);
        }
    }
}
