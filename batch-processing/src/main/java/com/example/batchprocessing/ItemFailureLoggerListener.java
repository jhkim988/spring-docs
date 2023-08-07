package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemFailureLoggerListener extends ItemListenerSupport<Person, Person> {
    private static Logger logger = LoggerFactory.getLogger(ItemFailureLoggerListener.class);

    @Override
    public void onReadError(Exception ex) {
        logger.error("Encountered Error on Read", ex);
    }

    @Override
    public void onWriteError(Exception ex, Chunk items) {
        logger.error("Encountered Error on Writer", ex, items);
    }

    @Override
    public void onProcessError(Person item, Exception ex) {
        logger.error("Encountered Error on Process", item, ex);
    }
}
