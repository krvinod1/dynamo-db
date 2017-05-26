/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.domain.convertor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/** Class to represent app1Mapper and app2Mapper of RuleIdMap class. A dynamoDB converter (RuleIdMapMapperConverter) is used to convert
 * this class to String or String to class during write and read.
 *
 * Created by vkumar21 on 4/6/17.
 */
public class RuleIdMapMapper {

    static final String DELEMETER = "#";

    private String mapperName;
    private String app1Name;
    private String app1Value;
    private String app2Name;

    public RuleIdMapMapper() {
    }

    public RuleIdMapMapper(String mapperName, String app1Name, String app1value, String app2Name) {
        this.mapperName = mapperName;
        this.app1Name = app1Name;
        this.app1Value = app1value;
        this.app2Name = app2Name;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getApp1Name() {
        return app1Name;
    }

    public void setApp1Name(String app1Name) {
        this.app1Name = app1Name;
    }

    public String getApp1Value() {
        return app1Value;
    }

    public void setApp1Value(String app1Value) {
        this.app1Value = app1Value;
    }

    public String getApp2Name() {
        return app2Name;
    }

    public void setApp2Name(String app2Name) {
        this.app2Name = app2Name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RuleIdMapMapper ruleIdMapMapper = (RuleIdMapMapper) o;

        return new EqualsBuilder()
                .append(mapperName, ruleIdMapMapper.mapperName)
                .append(app1Name, ruleIdMapMapper.app1Name)
                .append(app1Value, ruleIdMapMapper.app1Value)
                .append(app2Name, ruleIdMapMapper.app2Name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(mapperName)
                .append(app1Name)
                .append(app1Value)
                .append(app2Name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return mapperName + DELEMETER + app1Name + DELEMETER + app1Value + DELEMETER + app2Name;
    }
}
