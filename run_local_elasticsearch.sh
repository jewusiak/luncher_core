#!/bin/bash

# -v "`pwd`/elasticsearch_data:/usr/share/elasticsearch/data" 

docker run --name luncher_local_elasticsearch --rm -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" elasticsearch:8.12.2
