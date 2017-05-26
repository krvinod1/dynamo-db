/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.domain.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;


/** This class is used to convert RuleIdMapper to dynamo db schema string format. For example, if a RuleIdMapMapper is defined with
 * mapperName = salesforce-qbo, app1Name = salesforce, app1Value = 10 and app2Name = qbo. The mapper will be converted by converter class to
 * app1Mapper field of RuleIdMap to salesforce-qbo#salesforce#10#qbo.
 * Convert and convert is called by dynamoDB when it is defined as part of domain class using DynamoDBTypeConverted annotation
 *
 *  Created by vkumar21 on 4/6/17.
 */
public class RuleIdMapMapperConverter implements DynamoDBTypeConverter<String, RuleIdMapMapper> {
    public String convert(RuleIdMapMapper mapper) {
        RuleIdMapMapper ruleIdMapMapper = mapper;
        StringBuilder mapperValue = new StringBuilder();

        if (ruleIdMapMapper != null) {
            mapperValue.append(ruleIdMapMapper.getMapperName());
            mapperValue.append(RuleIdMapMapper.DELEMETER).append(ruleIdMapMapper.getApp1Name());
            mapperValue.append(RuleIdMapMapper.DELEMETER).append(ruleIdMapMapper.getApp1Value());
            mapperValue.append(RuleIdMapMapper.DELEMETER).append(ruleIdMapMapper.getApp2Name());
        }
        return mapperValue.toString();
    }

    public RuleIdMapMapper unconvert(String value) {
        RuleIdMapMapper ruleIdMapMapper = new RuleIdMapMapper();
        if (value != null && value.length() != 0) {
            String[] data = value.split(RuleIdMapMapper.DELEMETER);
            ruleIdMapMapper.setMapperName(data[0].trim());
            ruleIdMapMapper.setApp1Name(data[1].trim());
            ruleIdMapMapper.setApp1Value(data[2].trim());
            ruleIdMapMapper.setApp2Name(data[3].trim());
        }
        return ruleIdMapMapper;
    }
}
