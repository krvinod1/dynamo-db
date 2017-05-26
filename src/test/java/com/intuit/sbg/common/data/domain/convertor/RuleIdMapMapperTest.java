/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.domain.convertor;


import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 4/12/17.
 */
public class RuleIdMapMapperTest {

    @Test
    public void defaultConstructorTest() {
        RuleIdMapMapper ruleIdMapMapper = new RuleIdMapMapper();
        Assert.assertNull(ruleIdMapMapper.getApp1Name());
        Assert.assertNull(ruleIdMapMapper.getApp1Value());
        Assert.assertNull(ruleIdMapMapper.getApp2Name());
        Assert.assertNull(ruleIdMapMapper.getMapperName());
    }


    @Test
    public void constructorTest() {
        RuleIdMapMapper ruleIdMapMapper = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        Assert.assertEquals(ruleIdMapMapper.getMapperName(), "mapper");
        Assert.assertEquals(ruleIdMapMapper.getApp1Name(), "app1name");
        Assert.assertEquals(ruleIdMapMapper.getApp1Value(), "app1value");
        Assert.assertEquals(ruleIdMapMapper.getApp2Name(), "app2name");

    }

    @Test
    public void setterAndGetterTest() {
        RuleIdMapMapper ruleIdMapMapper = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        ruleIdMapMapper.setMapperName("mapper-updated");
        ruleIdMapMapper.setApp1Name("app1name-updated");
        ruleIdMapMapper.setApp1Value("app1value-updated");
        ruleIdMapMapper.setApp2Name("app2name-updated");
        Assert.assertEquals(ruleIdMapMapper.getMapperName(), "mapper-updated");
        Assert.assertEquals(ruleIdMapMapper.getApp1Name(), "app1name-updated");
        Assert.assertEquals(ruleIdMapMapper.getApp1Value(), "app1value-updated");
        Assert.assertEquals(ruleIdMapMapper.getApp2Name(), "app2name-updated");
    }

    @Test
    public void toStringTest() {
        RuleIdMapMapper ruleIdMapMapper = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        Assert.assertEquals(ruleIdMapMapper.toString(), "mapper#app1name#app1value#app2name");
    }

    @Test
    public void equalTest() {
        Map<RuleIdMapMapper, String> map = new HashMap<>();
        RuleIdMapMapper ruleIdMapMapper = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        map.put(ruleIdMapMapper,"hello");
        RuleIdMapMapper ruleIdMapMapper1 = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        Assert.assertTrue(map.containsKey(ruleIdMapMapper1));
        Assert.assertTrue(map.get(ruleIdMapMapper1).equals("hello"));
    }

}