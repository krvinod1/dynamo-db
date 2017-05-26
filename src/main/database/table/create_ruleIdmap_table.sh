#!/usr/bin/env bash
source env_util.sh

aws dynamodb --profile $(prop 'aws.profile') --endpoint-url $(prop 'amazon.dynamodb.endpoint') \
   create-table --table-name $(prop 'environment').RuleIdMap \
  --attribute-definitions \
            AttributeName=Owner,AttributeType=S \
            AttributeName=MapperApp1ToApp2,AttributeType=S \
            AttributeName=MapperApp2ToApp1,AttributeType=S \
    --key-schema AttributeName=Owner,KeyType=HASH \
                 AttributeName=MapperApp1ToApp2,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=100,WriteCapacityUnits=20 \
    --local-secondary-indexes IndexName=RuleIdMapMapperApp2ToApp1LIndex,KeySchema="[{AttributeName=Owner,KeyType=HASH},
    {AttributeName=MapperApp2ToApp1,
    KeyType=RANGE}]",Projection="{ProjectionType=ALL}"