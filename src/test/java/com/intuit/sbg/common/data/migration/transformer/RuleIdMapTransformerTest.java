/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.migration.transformer;

import com.intuit.sbg.common.data.domain.RuleIdMap;
import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 4/14/17.
 */
public class RuleIdMapTransformerTest {

    @Test
    public void testTransform() throws Exception {
        RuleIdMapTransformer transformer = new RuleIdMapTransformer();
        RuleIdMapTransformer.RuleIdMapJson ruleIdMapJson = new RuleIdMapTransformer.RuleIdMapJson();
        ruleIdMapJson.setMappingName("salesforce-qbo");
        ruleIdMapJson.setApp1ObjectName("salesforce");
        ruleIdMapJson.setApp1ObjectId("2");
        ruleIdMapJson.setApp2ObjectName("qbo");
        ruleIdMapJson.setApp2ObjectId("10");
        ruleIdMapJson.setCreatedBy("10-10-1972");

        RuleIdMap ruleIdMap = transformer.transform(ruleIdMapJson);

        Assert.assertEquals(ruleIdMap.getApp1Mapper(), new RuleIdMapMapper("salesforce-qbo", "salesforce", "2", "qbo"));
        Assert.assertEquals(ruleIdMap.getApp2Mapper(), new RuleIdMapMapper("salesforce-qbo", "qbo", "10", "salesforce"));
        Assert.assertEquals(ruleIdMap.getApp1Value(), "2");
        Assert.assertEquals(ruleIdMap.getApp2Value(), "10");
    }

    @Test
    public void testRuleIdMapJsonToString() throws Exception {
        RuleIdMapTransformer.RuleIdMapJson ruleIdMapJson = new RuleIdMapTransformer.RuleIdMapJson();
        ruleIdMapJson.setMappingName("salesforce-qbo");
        ruleIdMapJson.setApp1ObjectName("salesforce");
        ruleIdMapJson.setApp1ObjectId("2");
        ruleIdMapJson.setApp2ObjectName("qbo");
        ruleIdMapJson.setApp2ObjectId("10");
        ruleIdMapJson.setCreatedBy("10-10-1972");


        Assert.assertTrue(ruleIdMapJson.toString().contains("mappingName"));
        Assert.assertTrue(ruleIdMapJson.toString().contains("salesforce"));
    }

}