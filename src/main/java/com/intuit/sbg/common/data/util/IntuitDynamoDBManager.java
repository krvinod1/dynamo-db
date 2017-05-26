/*
 * CONFIDENTIAL -- Copyright 2017 Intuit Inc. This material contains certain trade secrets and confidential and
 * proprietary information of Intuit Inc. Use, reproduction, disclosure and distribution by any means are prohibited,
 * except pursuant to a written license from Intuit Inc. Use of copyright notice is precautionary and does not imply
 * publication or disclosure.
 *
 */

package com.intuit.sbg.common.data.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

/**
 * Created by vkumar21 on 3/27/17.
 */
public class IntuitDynamoDBManager {
    private DynamoDBMapper dynamoDBMapper;
    private DynamoDBMapperConfig config;
    private ClientConfiguration clientConfiguration;

    public IntuitDynamoDBManager(String url) {
        DynamoDBMapperConfig.TableNameOverride tableNameOverride = DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix
                ("dev.");
        config = new DynamoDBMapperConfig.Builder().withTableNameOverride(tableNameOverride).build();
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient();
        amazonDynamoDB.setEndpoint(url);
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB, config);
    }

    public IntuitDynamoDBManager(AWSCredentials awsCredentials, ClientConfiguration clientConfiguration,
                                 Region region, RuntimeEnvironment runtimeEnvironment) {

        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(awsCredentials, clientConfiguration);
        amazonDynamoDB.setRegion(region);
        DynamoDBMapperConfig.TableNameOverride tableNameOverride = DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix
                (runtimeEnvironment.toString().toLowerCase()+ ".");
        config = new DynamoDBMapperConfig.Builder().withTableNameOverride(tableNameOverride).build();
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB, config);
        this.clientConfiguration = clientConfiguration;
    }

    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    public DynamoDBMapperConfig getDynamoDBConfig() {
        return config;
    }

    public ClientConfiguration  getClientConfiguration() {
        return clientConfiguration;
    }
}
