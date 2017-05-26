/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.domain;

import java.time.Instant;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapper;
import com.intuit.sbg.common.data.domain.convertor.RuleIdMapMapperConverter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by vkumar21 on 3/31/17.
 */
@DynamoDBTable(tableName = "RuleIdMap")
public class RuleIdMap {
    private String owner;
    private RuleIdMapMapper app1Mapper;
    private RuleIdMapMapper app2Mapper;
    private String created;
    private Long version;

    public RuleIdMap() {
        this.created = Instant.now().toString();
    }

    public RuleIdMap(String owner, RuleIdMapMapper app1Mapper, RuleIdMapMapper app2Mapper) {
        this();
        this.owner = owner;
        this.app1Mapper = app1Mapper;
        this.app2Mapper = app2Mapper;
    }

    @DynamoDBHashKey(attributeName = "Owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @DynamoDBRangeKey(attributeName = "MapperApp1ToApp2")
    @DynamoDBTypeConverted(converter = RuleIdMapMapperConverter.class)
    public RuleIdMapMapper getApp1Mapper() {
        return app1Mapper;
    }

    public void setApp1Mapper(RuleIdMapMapper app1Mapper) {
        this.app1Mapper = app1Mapper;
    }

    @DynamoDBIndexRangeKey(localSecondaryIndexName = "RuleIdMapMapperApp2ToApp1LIndex", attributeName = "MapperApp2ToApp1")
    @DynamoDBTypeConverted(converter = RuleIdMapMapperConverter.class)
    public RuleIdMapMapper getApp2Mapper() {
        return app2Mapper;
    }

    public void setApp2Mapper(RuleIdMapMapper app2Mapper) {
        this.app2Mapper = app2Mapper;
    }

    @DynamoDBIgnore()
    public String getApp1Value() {
        return app1Mapper == null ? null : app1Mapper.getApp1Value();
    }

    @DynamoDBIgnore()
    public String getApp2Value() {
        return app2Mapper == null ? null : app2Mapper.getApp1Value();
    }

    @DynamoDBAttribute
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @DynamoDBVersionAttribute(attributeName = "ver")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RuleIdMap ruleIdMap = (RuleIdMap) o;

        return new EqualsBuilder()
                .append(owner, ruleIdMap.owner)
                .append(app1Mapper, ruleIdMap.app1Mapper)
                .append(app2Mapper, ruleIdMap.app2Mapper)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(owner)
                .append(app1Mapper)
                .append(app2Mapper)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RuleIdMap{" +
                "owner='" + owner + '\'' +
                ", app1Mapper=" + app1Mapper +
                ", app2Mapper=" + app2Mapper +
                ", created='" + created + '\'' +
                ", version=" + version +
                '}';
    }

    public static class Builder {
        private String owner;
        private String mapperName;
        private String app1Name;
        private String app1Value;
        private String app2Name;
        private String app2Value;

        public Builder(String owner, String mapper) {
            this.owner = owner;
            this.mapperName = mapper;
        }

        public Builder withFirstApp(String app1Name, String app1Value) {
            this.app1Name = app1Name;
            this.app1Value = app1Value;
            return this;
        }

        public Builder withSecondApp(String app2Name, String app2Value) {
            this.app2Name = app2Name;
            this.app2Value = app2Value;
            return this;
        }

        public RuleIdMap build() {
            RuleIdMap ruleIdMap = new RuleIdMap();
            RuleIdMapMapper ruleIdMapMapper1 = new RuleIdMapMapper(mapperName, app1Name, app1Value, app2Name);
            RuleIdMapMapper ruleIdMapMapper2 = new RuleIdMapMapper(mapperName, app2Name, app2Value, app1Name);
            ruleIdMap.setOwner(owner);
            ruleIdMap.setApp1Mapper(ruleIdMapMapper1);
            ruleIdMap.setApp2Mapper(ruleIdMapMapper2);
            return ruleIdMap;
        }
    }
}
