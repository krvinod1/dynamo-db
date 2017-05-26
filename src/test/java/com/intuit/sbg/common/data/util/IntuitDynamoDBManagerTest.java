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
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * Created by vkumar21 on 4/12/17.
 */
public class IntuitDynamoDBManagerTest {

    @Test
    public void constructionOneTest() {
        IntuitDynamoDBManager intuitDynamoDBManager = new IntuitDynamoDBManager("http://locahost:9000");
        Assert.assertNull(intuitDynamoDBManager.getClientConfiguration());
        DynamoDBMapperConfig config = intuitDynamoDBManager.getDynamoDBConfig();
        DynamoDBMapper mapper = intuitDynamoDBManager.getDynamoDBMapper();
        Assert.assertNotNull(config);
        Assert.assertNotNull(mapper);
        Assert.assertEquals("dev.", config.getTableNameOverride().getTableNamePrefix());
    }


    @Test
    public void constructionStageTest() {
        // this is done to run test at QA machine - related to AWS AWSCredentials
        String  oldAccessKey = System.getProperty("aws.accessKeyId");
        String  oldSecretKey = System.getProperty("aws.secretKey");
        System.setProperty("aws.accessKeyId", "foo");
        System.setProperty("aws.secretKey", "bar");
        AWSCredentials awsCredentials = new DefaultAWSCredentialsProviderChain().getCredentials();
        ClientConfiguration clientConfiguration = new ClientConfiguration().withConnectionTimeout(5000).withGzip(true);
        Region region = Region.getRegion(Regions.US_WEST_2);
        IntuitDynamoDBManager intuitDynamoDBManager = new IntuitDynamoDBManager(awsCredentials,  clientConfiguration,
                 region,  RuntimeEnvironment.STAGE);

        Assert.assertNotNull(intuitDynamoDBManager.getClientConfiguration());
        Assert.assertNotNull(intuitDynamoDBManager.getDynamoDBConfig());
        Assert.assertNotNull(intuitDynamoDBManager.getDynamoDBMapper());
        Assert.assertEquals("stage.", intuitDynamoDBManager.getDynamoDBConfig().getTableNameOverride().getTableNamePrefix());
        //reset the property
        if ( oldAccessKey != null) {
            System.setProperty("aws.accessKeyId", oldAccessKey);
        }

        if ( oldSecretKey != null) {
            System.setProperty("aws.secretKey", oldSecretKey);
        }
    }

}