/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.migration;

import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughputExceededException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.intuit.sbg.common.data.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkumar21 on 4/6/17.
 */
public class TransformerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerManager.class);

    private InputStream inputStream;
    private Repository repository;

    public TransformerManager(InputStream inputStream, Repository repository) {
        this.inputStream = inputStream;
        this.repository = repository;
    }

    public <T, S> long transform(Transformer<T,S> t, Class jsonClass) throws Exception {
        LOGGER.debug("Starting transformation and migration ");
        long count =0l;
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            Gson gson = new GsonBuilder().create();

            reader.beginArray();
            int batchCounter = 0;
            List<S> items = new ArrayList<>();
            while (reader.hasNext()) {
                T obj = gson.fromJson(reader, jsonClass);
                S s = t.transform(obj);
                items.add(s);
                batchCounter++;
                if ( batchCounter == 500 || (!reader.hasNext())) {
                    {
                        process(items);
                        batchCounter = 0;
                        items = new ArrayList<>();
                    }
                }
                count++;
            }
            LOGGER.debug("Total no of records migrated " + count);
        }  catch (IOException ex) {
            LOGGER.error("Got exception while reading json " + ex.getMessage());
            throw ex;
        } finally {
            try {
                if ( reader != null) {
                    reader.close();
                }
                inputStream.close();
            } catch (Exception e){}
        }
        return count;
    }

    private <T> void process(List<T> batches) throws Exception {
        List<T> failedBatch = new ArrayList<>();
        try {
            if ( batches.size() > 1) {
                repository.save(batches);
            } else {
                repository.save(batches.get(0));
            }
            LOGGER.debug("uploaded successfully ");
        } catch (ProvisionedThroughputExceededException proEx ) {
            LOGGER.debug("ProvisionedThroughputExceededException occured. Lets sleep for some time ");
            Thread.currentThread().sleep(1000*5); // sleep for 5 second.
            failedBatch.addAll(batches);
        } catch (Exception e) {
            LOGGER.error("Exception encountered while saving " + e.getMessage());
            failedBatch.addAll(batches);
        } finally {
            // try the failed batch again
            if ( failedBatch.size() > 0 && batches.size() > 1 ) {
                try {
                    repository.save(failedBatch);
                } catch (Exception e) {
                    LOGGER.error(" Failed again " + e.getMessage());
                    for ( T t : batches) {
                        LOGGER.warn(" Records unable to save " + t.toString());
                    }
                }
            }
            failedBatch = new ArrayList<T>();
        }
    }

}
