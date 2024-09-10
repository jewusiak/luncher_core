#!/bin/bash

# -v "`pwd`/elasticsearch_data:/usr/share/elasticsearch/data"

docker build --tag luncher_core_elasticsearch --file ./Dockerfile-elasticsearch .
docker build --tag luncher_core_postgres --file ./Dockerfile-postgres .
