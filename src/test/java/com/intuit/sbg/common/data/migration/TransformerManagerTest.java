/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.migration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.intuit.sbg.common.data.DynamoDBBaseTest;
import com.intuit.sbg.common.data.IndexInfo;
import com.intuit.sbg.common.data.TableInfo;
import com.intuit.sbg.common.data.domain.RuleIdMap;
import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapper;
import com.intuit.sbg.common.data.migration.transformer.RuleIdMapTransformer;
import com.intuit.sbg.common.data.repository.Repository;
import com.intuit.sbg.common.data.repository.impl.RuleIdMapRespositoryImpl;
import com.intuit.sbg.common.data.util.IntuitDynamoDBManager;
import com.intuit.sbg.common.data.util.RuntimeEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vkumar21 on 4/14/17.
 */
public class TransformerManagerTest extends DynamoDBBaseTest {
    private Repository<RuleIdMap> repository = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerManagerTest.class);

    @Override
    public void initializeTableInfoAndRespository() {
        tableInfo = new TableInfo("dev.RuleIdMap", "Owner", "S", "MapperApp1ToApp2", "S");
        IndexInfo indexInfo = new IndexInfo("RuleIdMapMapperApp2ToApp1LIndex", true, "Owner", "S", "MapperApp2ToApp1", "S");
        List<IndexInfo> indexes = new ArrayList<>();
        indexes.add(indexInfo);
        tableInfo.setIndexInfos(indexes);
        repository = new RuleIdMapRespositoryImpl(intuitDynamoDBManager);
    }

    @Test
    public void transformTest() throws Exception {
        String fileName = "migration/ruleidmap.json";
        InputStream stream = null;
        long count = 0l;
        try {
            stream = TransformerManagerTest.class.getClassLoader().getResourceAsStream(fileName);
            TransformerManager transformerManager = new TransformerManager(stream, repository);
            count = transformerManager.transform(new RuleIdMapTransformer(), RuleIdMapTransformer.RuleIdMapJson.class);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
        Assert.assertEquals(94, count);
    }

    @Test(expectedExceptions = IOException.class )
    public void transforrNotValidFileTest() throws Exception {
        String fileName = "migration/invalid_file.txt";
        InputStream stream = TransformerManagerTest.class.getClassLoader().getResourceAsStream(fileName);
        TransformerManager transformerManager = new TransformerManager(stream, repository);
        transformerManager.transform(new RuleIdMapTransformer(), RuleIdMapTransformer.RuleIdMapJson.class);
    }

    /** below are the examples how to use transform to transform data
     *
     * @throws IOException
     */

    public static void main(String[] args) throws Exception {
        //change file file name
      /*  int count = 3;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        RuntimeEnvironment runtimeEnvironment = RuntimeEnvironment.QA;
        IntuitDynamoDBManager dynamoDBManager = createAppConnectDynamoDBManager(runtimeEnvironment);
        Repository<RuleIdMap> repository = new RuleIdMapRespositoryImpl(dynamoDBManager);
        repository.save(ruleIdMap);
        RuleIdMap savedValue = repository.findOne(ruleIdMap);
        Assert.assertNotNull(savedValue);
        Assert.assertEquals(ruleIdMap, savedValue);
        Assert.assertEquals(new Integer(count).toString(), savedValue.getApp1Value());
        Assert.assertEquals(new Integer(count*5).toString(), savedValue.getApp2Value());*/
        String fileName = "migration/ruleidmap-stage.json";
        LOGGER.debug("reading input file " + fileName);
        InputStream stream = null;
        try {
            stream = TransformerManagerTest.class.getClassLoader().getResourceAsStream(fileName);
            if ( stream == null) {
                throw new IOException("unable to read input jsnn file " + fileName);
            }
            RuntimeEnvironment runtimeEnvironment = RuntimeEnvironment.STAGE;
            IntuitDynamoDBManager dynamoDBManager = createAppConnectDynamoDBManager(runtimeEnvironment);
            //IntuitDynamoDBManager dynamoDBManager = createLocalAppConnectDynamoDBManager();

            Repository<RuleIdMap> ruleIdMapRepository = new RuleIdMapRespositoryImpl(dynamoDBManager);
            TransformerManager transformerManager = new TransformerManager(stream, ruleIdMapRepository);
            transformerManager.transform(new RuleIdMapTransformer(), RuleIdMapTransformer.RuleIdMapJson.class);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {}
            }
        }
    }

    private static RuleIdMapMapper[] createAppMappers(int suffix) {
        RuleIdMapMapper[] mappers = new RuleIdMapMapper[2];
        mappers[0] = new RuleIdMapMapper("qbo-salesforce", "qbo", new Integer(suffix).toString(), "salesforce");
        mappers[1] = new RuleIdMapMapper("qbo-salesforce", "salesforce", new Integer(5*suffix).toString(), "qbo");
        return mappers;
    }
    private static IntuitDynamoDBManager createLocalAppConnectDynamoDBManager() {
        return new IntuitDynamoDBManager("http://localhost:8000");
    }

    private static IntuitDynamoDBManager createAppConnectDynamoDBManager(RuntimeEnvironment runtimeEnvironment) {
        AWSCredentials awsCredentials = new DefaultAWSCredentialsProviderChain().getCredentials();
        ClientConfiguration clientConfiguration = new ClientConfiguration().withConnectionTimeout(5000).withGzip(true);
        Region region = Region.getRegion(Regions.US_WEST_2);
        return new IntuitDynamoDBManager(awsCredentials, clientConfiguration, region, runtimeEnvironment);
    }
}