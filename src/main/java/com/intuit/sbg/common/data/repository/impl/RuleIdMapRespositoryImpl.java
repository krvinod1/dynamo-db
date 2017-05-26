/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.repository.impl;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.intuit.sbg.common.data.repository.RuleIdMapRepository;
import com.intuit.sbg.common.data.util.IntuitDynamoDBManager;
import com.intuit.sbg.common.data.domain.RuleIdMap;

/**
 * Created by vkumar21 on 4/6/17.
 */
public class RuleIdMapRespositoryImpl extends AbstractRepository<RuleIdMap> implements RuleIdMapRepository {

    public RuleIdMapRespositoryImpl(IntuitDynamoDBManager intuitDynamoDBManager) {
        super(intuitDynamoDBManager);
    }

    @Override
    public Class<RuleIdMap> getDomainClass() {
        return RuleIdMap.class;
    }

    @Override
    public RuleIdMap findOne(RuleIdMap entity) {
         DynamoDBMapperConfig localConfig = new DynamoDBMapperConfig.Builder().withTableNameOverride(config.getTableNameOverride())
                                                .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT).build();

        return dynamoDBMapper.load(entity, localConfig);
    }

    @Override
    public RuleIdMap findByFirstApp(RuleIdMap ruleIdMap) {
        return findOne(ruleIdMap);
    }

    @Override
    public RuleIdMap findBySecondApp(RuleIdMap ruleIdMap) {

        RuleIdMap hashKey = new RuleIdMap();
        hashKey.setOwner(ruleIdMap.getOwner());

        Condition rangeKeyCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ)
                .withAttributeValueList(new AttributeValue().withS(ruleIdMap.getApp2Mapper().toString()));

        DynamoDBQueryExpression<RuleIdMap> queryExpression = new DynamoDBQueryExpression<RuleIdMap>()
                .withHashKeyValues(hashKey)
                .withRangeKeyCondition("MapperApp2ToApp1", rangeKeyCondition);
        List<RuleIdMap> results = dynamoDBMapper.query(getDomainClass(), queryExpression);

        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
}
