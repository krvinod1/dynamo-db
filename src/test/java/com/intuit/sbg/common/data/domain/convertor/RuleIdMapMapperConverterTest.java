/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.domain.convertor;


import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 4/12/17.
 */
public class RuleIdMapMapperConverterTest {

    @Test
    public void convertTest() {
        RuleIdMapMapper mapper = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        RuleIdMapMapperConverter ruleIdMapMapperConverter = new RuleIdMapMapperConverter();
        String value = ruleIdMapMapperConverter.convert(mapper);
        Assert.assertEquals(value, mapper.toString());
    }

    @Test
    public void unconvertTest() {
        RuleIdMapMapper mapper = new RuleIdMapMapper("mapper", "app1name", "app1value", "app2name");
        RuleIdMapMapperConverter ruleIdMapMapperConverter = new RuleIdMapMapperConverter();
        RuleIdMapMapper value = ruleIdMapMapperConverter.unconvert(mapper.toString());
        Assert.assertEquals(value.getMapperName(), "mapper");
        Assert.assertEquals(value.getApp1Name(), "app1name");
        Assert.assertEquals(value.getApp1Value(), "app1value");
        Assert.assertEquals(value.getApp2Name(), "app2name");
    }
}