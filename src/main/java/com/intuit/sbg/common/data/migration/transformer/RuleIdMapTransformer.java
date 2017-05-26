/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.migration.transformer;


import com.google.gson.annotations.SerializedName;
import com.intuit.sbg.common.data.migration.Transformer;
import com.intuit.sbg.common.data.domain.RuleIdMap;

/**
 * Created by vkumar21 on 4/6/17.
 */
public class RuleIdMapTransformer implements Transformer<RuleIdMapTransformer.RuleIdMapJson, RuleIdMap> {

    @Override
    public RuleIdMap transform(RuleIdMapTransformer.RuleIdMapJson input) {
        RuleIdMap ruleIdMap = new RuleIdMap.Builder(input.getCreatedBy(), input.getMappingName())
                .withFirstApp(input.getApp1ObjectName(), input.getApp1ObjectId())
                .withSecondApp(input.getApp2ObjectName(), input.getApp2ObjectId())
                .build();
        return ruleIdMap;
    }

    public static class RuleIdMapJson {
        @SerializedName("created_by")
        private String createdBy;
        @SerializedName("mapping_name")
        private String mappingName;
        @SerializedName("app1_object_name")
        private String app1ObjectName;
        @SerializedName("app1_object_id")
        private String app1ObjectId;
        @SerializedName("app2_object_name")
        private String app2ObjectName;
        @SerializedName("app2_object_id")
        private String app2ObjectId;

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String craetedBy) {
            this.createdBy = craetedBy;
        }

        public String getMappingName() {
            return mappingName;
        }

        public void setMappingName(String mappingName) {
            this.mappingName = mappingName;
        }

        public String getApp1ObjectName() {
            return app1ObjectName;
        }

        public void setApp1ObjectName(String app1ObjectName) {
            this.app1ObjectName = app1ObjectName;
        }

        public String getApp1ObjectId() {
            return app1ObjectId;
        }

        public void setApp1ObjectId(String app1ObjectId) {
            this.app1ObjectId = app1ObjectId;
        }

        public String getApp2ObjectName() {
            return app2ObjectName;
        }

        public void setApp2ObjectName(String app2ObjectName) {
            this.app2ObjectName = app2ObjectName;
        }

        public String getApp2ObjectId() {
            return app2ObjectId;
        }

        public void setApp2ObjectId(String app2ObjectId) {
            this.app2ObjectId = app2ObjectId;
        }

        @Override
        public String toString() {
            return "RuleIdMapJson{" +
                    ", createdBy='" + createdBy + '\'' +
                    ", mappingName='" + mappingName + '\'' +
                    ", app1ObjectName='" + app1ObjectName + '\'' +
                    ", app1ObjectId='" + app1ObjectId + '\'' +
                    ", app2ObjectName='" + app2ObjectName + '\'' +
                    ", app2ObjectId='" + app2ObjectId + '\'' +
                    '}';
        }
    }
}
