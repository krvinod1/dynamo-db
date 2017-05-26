/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.domain;

import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 4/12/17.
 */
public class RuleIdMapTest {

    @Test
    public void defaultConstructionTest() {
        RuleIdMap ruleIdMap = new RuleIdMap();
        Assert.assertNull(ruleIdMap.getOwner());
        Assert.assertNotNull(ruleIdMap.getCreated());
        Assert.assertNull(ruleIdMap.getApp1Mapper());
        Assert.assertNull(ruleIdMap.getApp2Mapper());
        Assert.assertNull(ruleIdMap.getApp1Value());
        Assert.assertNull(ruleIdMap.getApp2Value());
    }

    @Test
    public void constructionTest() {
        RuleIdMapMapper mapper1 = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        RuleIdMapMapper mapper2 = new RuleIdMapMapper("mapper", "app2name", "app2value", "app1name");
        RuleIdMap ruleIdMap = new RuleIdMap("foo", mapper1, mapper2);

        Assert.assertNotNull(ruleIdMap.getOwner());
        Assert.assertNotNull(ruleIdMap.getCreated());
        Assert.assertEquals(ruleIdMap.getApp1Mapper(), mapper1);
        Assert.assertEquals(ruleIdMap.getApp2Mapper(), mapper2);
        Assert.assertEquals(ruleIdMap.getApp1Value(), "app1value");
        Assert.assertEquals(ruleIdMap.getApp2Value(), "app2value");
    }

    @Test
    public void getterSetterTest() {
        RuleIdMapMapper mapper1 = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        RuleIdMapMapper mapper2 = new RuleIdMapMapper("mapper", "app2name", "app2value", "app1name");
        RuleIdMap ruleIdMap = new RuleIdMap();
        ruleIdMap.setOwner("foo");
        ruleIdMap.setApp1Mapper(mapper1);
        ruleIdMap.setApp2Mapper(mapper2);

        Assert.assertEquals(ruleIdMap.getOwner(), "foo");
        Assert.assertEquals(ruleIdMap.getApp1Mapper(), mapper1);
        Assert.assertEquals(ruleIdMap.getApp2Mapper(), mapper2);
        Assert.assertEquals(ruleIdMap.getApp1Value(), "app1value");
        Assert.assertEquals(ruleIdMap.getApp2Value(), "app2value");
    }

    @Test
    public void ruleIdBuilderTest() {
        RuleIdMap ruleIdMap = new RuleIdMap.Builder("foo", "saleforce-qbo")
                .withFirstApp("salesforce", "2")
                .withSecondApp("QBO", "10").build();
        Assert.assertNotNull(ruleIdMap);

        Assert.assertEquals(ruleIdMap.getApp1Mapper(), new RuleIdMapMapper("saleforce-qbo", "salesforce", "2", "QBO"));
        Assert.assertEquals(ruleIdMap.getApp2Mapper(), new RuleIdMapMapper("saleforce-qbo", "QBO", "10", "salesforce"));
        Assert.assertEquals(ruleIdMap.getApp1Value(), "2");
        Assert.assertEquals(ruleIdMap.getApp2Value(), "10");
    }

    @Test
    public void ruleIdBuildeEqualsTest() {
        RuleIdMap ruleIdMap = new RuleIdMap.Builder("foo", "saleforce-qbo")
                .withFirstApp("salesforce", "2")
                .withSecondApp("QBO", "10").build();
        Assert.assertNotNull(ruleIdMap);

        RuleIdMap ruleIdMap1= new RuleIdMap.Builder("foo", "saleforce-qbo")
                .withFirstApp("salesforce", "2")
                .withSecondApp("QBO", "10").build();
        Assert.assertNotNull(ruleIdMap);

        Assert.assertEquals(ruleIdMap1, ruleIdMap);
    }

    @Test
    public void ruleIdBuildeHashCodeTest() {
        RuleIdMap ruleIdMap = new RuleIdMap.Builder("foo", "saleforce-qbo")
                .withFirstApp("salesforce", "2")
                .withSecondApp("QBO", "10").build();
        Assert.assertNotNull(ruleIdMap);

        RuleIdMap ruleIdMap1= new RuleIdMap.Builder("foo", "saleforce-qbo")
                .withFirstApp("salesforce", "2")
                .withSecondApp("QBO", "10").build();
        Assert.assertNotNull(ruleIdMap);

        Assert.assertEquals(ruleIdMap1.hashCode(), ruleIdMap.hashCode());
    }
}