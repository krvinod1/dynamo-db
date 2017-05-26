/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.repository.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.intuit.sbg.common.data.DynamoDBBaseTest;
import com.intuit.sbg.common.data.TableInfo;
import com.intuit.sbg.common.data.repository.RuleIdMapRepository;
import com.intuit.sbg.common.data.IndexInfo;
import com.intuit.sbg.common.data.domain.RuleIdMap;
import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 4/11/17.
 */
public class RuleIdMapRespositoryImplTest extends DynamoDBBaseTest {

    RuleIdMapRepository repository = null;

    @Override
    public void initializeTableInfoAndRespository() {
        tableInfo = new TableInfo("dev.RuleIdMap", "Owner", "S", "MapperApp1ToApp2", "S");
        IndexInfo indexInfo = new IndexInfo("RuleIdMapMapperApp2ToApp1LIndex", true, "Owner", "S", "MapperApp2ToApp1", "S");
        List<IndexInfo> indexes = new ArrayList<>();
        indexes.add(indexInfo);
        tableInfo.setIndexInfos(indexes);
        repository = new RuleIdMapRespositoryImpl(intuitDynamoDBManager);
    }

    @Test()
    public void findOne() throws Exception {
        int count = 10;
        System.out.println("date" + Instant.now().toString()  );
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo" + count);
        ruleIdMap.setApp1Mapper(mappers[0]);
        ruleIdMap.setApp2Mapper(mappers[1]);
        repository.save(ruleIdMap);
        RuleIdMap savedValue = repository.findOne(ruleIdMap);
        Assert.assertNotNull(savedValue);
        Assert.assertEquals(ruleIdMap, savedValue);
        Assert.assertEquals(new Integer(count).toString(), savedValue.getApp1Value());
        Assert.assertEquals(new Integer(count*5).toString(), savedValue.getApp2Value());
    }

    @Test
    public void findByFirstAppTest() throws Exception {
        int count = 1;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("FirstAppTest" + count);
        RuleIdMapMapper app1Mapper = mappers[0];
        RuleIdMapMapper app2Mapper = mappers[1];
        ruleIdMap.setApp1Mapper(app1Mapper);
        ruleIdMap.setApp2Mapper(app2Mapper);
        repository.save(ruleIdMap);

        RuleIdMap findRuleidMap = new RuleIdMap();
        findRuleidMap.setOwner("FirstAppTest" + count);
        findRuleidMap.setApp1Mapper(app1Mapper);

        RuleIdMap result = repository.findByFirstApp(findRuleidMap);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getCreated());
        Assert.assertEquals(ruleIdMap, result);
    }

    @Test
    public void findBySecondAppTest() throws Exception {
        int count = 2;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap1 = new RuleIdMap();
        ruleIdMap1.setOwner("SecondAppTest");
        RuleIdMapMapper app1Mapper = mappers[0];
        RuleIdMapMapper app2Mapper = mappers[1];
        ruleIdMap1.setApp1Mapper(app1Mapper);
        ruleIdMap1.setApp2Mapper(app2Mapper);
        repository.save(ruleIdMap1);

        RuleIdMap ruleIdMap2 = new RuleIdMap();
        ruleIdMap2.setOwner("SecondAppTest");
        mappers = createAppMappers(++count);
        ruleIdMap2.setApp1Mapper(mappers[0]);
        ruleIdMap2.setApp2Mapper(app2Mapper);
        repository.save(ruleIdMap2);

        RuleIdMap findRuleIdMap = new RuleIdMap();
        findRuleIdMap.setOwner("SecondAppTest" );
        findRuleIdMap.setApp2Mapper(app2Mapper);
        RuleIdMap result = repository.findBySecondApp(findRuleIdMap);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getCreated());
        Assert.assertEquals(result, ruleIdMap2);
        Assert.assertNotEquals(result, ruleIdMap1);
    }

    @Test
    public void findBySecondAppNotExistTest() throws Exception {
        int count = 3;
        RuleIdMapMapper[] mappers = createAppMappers(count);
        RuleIdMap ruleIdMap1 = new RuleIdMap();
        ruleIdMap1.setOwner("SecondAppNotExistTest");
        RuleIdMapMapper app1Mapper = mappers[0];
        RuleIdMapMapper app2Mapper = mappers[1];
        ruleIdMap1.setApp1Mapper(app1Mapper);
        ruleIdMap1.setApp2Mapper(app2Mapper);
        repository.save(ruleIdMap1);

        RuleIdMap findRuleIdMap = new RuleIdMap();
        findRuleIdMap.setOwner("SecondAppNotExistTest" );
        findRuleIdMap.setApp2Mapper(app1Mapper);
        RuleIdMap result = repository.findBySecondApp(findRuleIdMap);

        Assert.assertNull(result);
    }

    private RuleIdMapMapper[] createAppMappers(int suffix) {
        RuleIdMapMapper[] mappers = new RuleIdMapMapper[2];
        mappers[0] = new RuleIdMapMapper("qbo-salesforce", "qbo", new Integer(suffix).toString(), "salesforce");
        mappers[1] = new RuleIdMapMapper("qbo-salesforce", "salesforce", new Integer(5*suffix).toString(), "qbo");
        return mappers;
    }
}