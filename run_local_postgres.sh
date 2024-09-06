#!/bin/bash

# -v "`pwd`/elasticsearch_data:/usr/share/elasticsearch/data" 

docker build --tag luncher_core-postgres-test --file ./Dockerfile-postgres .
docker run --name luncher_local_postgres --rm -p 5432:5432 -e "POSTGRES_DB=luncher_core" -e "POSTGRES_USER=testuser" -e "POSTGRES_PASSWORD=testpass" luncher_core-postgres-test
