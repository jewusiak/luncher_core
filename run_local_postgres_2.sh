#!/bin/bash
schema () {
sleep 5
docker exec -it luncher_local_postgres /bin/bash -c "psql -h localhost -p 5432 -U postgres -c 'create database luncher_core_local;'" 
docker exec -it luncher_local_postgres /bin/bash -c "psql -h localhost -p 5432 -d luncher_core_local -U postgres -c 'create schema luncher_core;'"
}

(schema)&