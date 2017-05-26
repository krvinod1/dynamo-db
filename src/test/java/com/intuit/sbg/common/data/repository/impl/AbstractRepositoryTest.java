/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.repository.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.intuit.sbg.common.data.DynamoDBBaseTest;
import com.intuit.sbg.common.data.IndexInfo;
import com.intuit.sbg.common.data.TableInfo;
import com.intuit.sbg.common.data.domain.RuleIdMap;
import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 3/28/17.
 */

public class AbstractRepositoryTest extends DynamoDBBaseTest {

    private AbstractRepository<RuleIdMap> ruleIdMapAbstractRepository;
    private static int count=1;

    @Override
    public void initializeTableInfoAndRespository() {
        tableInfo = new TableInfo("dev.RuleIdMap", "Owner", "S", "MapperApp1ToApp2", "S");
        IndexInfo indexInfo = new IndexInfo("RuleIdMapMapperApp2ToApp1LIndex", true, "Owner", "S", "MapperApp2ToApp1", "S");
        List<IndexInfo> indexes = new ArrayList<>();
        indexes.add(indexInfo);
        tableInfo.setIndexInfos(indexes);

        ruleIdMapAbstractRepository = new AbstractRepository<RuleIdMap>(intuitDynamoDBManager) {
            @Override
            public Class<RuleIdMap> getDomainClass() {
                return RuleIdMap.class;
            }
        };
    }

    @Test()
    public void save() throws Exception {
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
        RuleIdMap savedValue = ruleIdMapAbstractRepository.findOne(ruleIdMap);
        Assert.assertNotNull(savedValue);
        Assert.assertEquals(ruleIdMap, savedValue);
        Assert.assertEquals(new Integer(count).toString(), savedValue.getApp1Value());
        Assert.assertEquals(new Integer(count*5).toString(), savedValue.getApp2Value());
    }

    @Test()
    public void saveTwice() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("bar" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
        ruleIdMapAbstractRepository.save(ruleIdMap);
    }

    @Test(expectedExceptions = ConditionalCheckFailedException.class )
    public void saveTwiceWithoutFetch() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
        ruleIdMap.setApp1Mapper(mappers[1]);
        ruleIdMap.setApp2Mapper(mappers[0]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
    }

    @Test
    public void saveTrueWithoutFetch() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("bar" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
        ruleIdMap.setApp1Mapper(mappers[1]);
        ruleIdMap.setApp2Mapper(mappers[0]);
        ruleIdMapAbstractRepository.save(ruleIdMap, true);
    }

    @Test()
    public void saveBatch() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        count++;
        RuleIdMapMapper[] mappers1 = createAppMappers(count);
        RuleIdMap ruleIdMap1 = new RuleIdMap();
        ruleIdMap1.setOwner("foo" + count);
        ruleIdMap1.setApp1Mapper(mappers1[0]);
        ruleIdMap1.setApp2Mapper(mappers1[1]);

        ruleIdMapAbstractRepository.save(Arrays.asList(ruleIdMap, ruleIdMap1));
    }

    @Test()
    public void query() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo-query");
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        count++;
        RuleIdMapMapper[] mappers1 = createAppMappers(count);
        RuleIdMap ruleIdMap1 = new RuleIdMap();
        ruleIdMap1.setOwner("foo-query" );
        ruleIdMap1.setApp1Mapper(mappers1[0]);
        ruleIdMap1.setApp2Mapper(mappers1[1]);

        ruleIdMapAbstractRepository.save(Arrays.asList(ruleIdMap, ruleIdMap1));
        ruleIdMap1 = new RuleIdMap();
        ruleIdMap1.setOwner("foo-query" );
        List<RuleIdMap> results = ruleIdMapAbstractRepository.query(ruleIdMap1);
        Assert.assertEquals(results.size(), 2);
    }

    @Test()
    public void delete() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("bar" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
        ruleIdMapAbstractRepository.delete(ruleIdMap);
        RuleIdMap savedValue = ruleIdMapAbstractRepository.findOne(ruleIdMap);
        Assert.assertNull(savedValue);
    }

    @Test()
    public void deleteAll() throws Exception {
        count++;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("bar" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMapAbstractRepository.save(ruleIdMap);
        ruleIdMapAbstractRepository.deleteAll();
        RuleIdMap savedValue = ruleIdMapAbstractRepository.findOne(ruleIdMap);
        long total = ruleIdMapAbstractRepository.count();
        Assert.assertNull(savedValue);
        Assert.assertEquals(0l,total);
    }

    private RuleIdMapMapper[] createAppMappers(int suffix) {
        RuleIdMapMapper[] mappers = new RuleIdMapMapper[2];
        mappers[0] = new RuleIdMapMapper("qbo-salesforce", "qbo", new Integer(suffix).toString(), "salesforce");
        mappers[1] = new RuleIdMapMapper("qbo-salesforce", "salesforce", new Integer(5*suffix).toString(), "qbo");
       return mappers;
    }

}