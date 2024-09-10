#!/bin/bash

# -v "`pwd`/elasticsearch_data:/usr/share/elasticsearch/data"

docker build --tag luncher_core_elasticsearch --file ./Dockerfile-elasticsearch .
docker run --name luncher_local_elasticsearch --rm -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" luncher_core_elasticsearch
