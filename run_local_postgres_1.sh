#!/bin/sh

docker build -t luncher_local_postgres -f Dockerfile-local-postgres .

docker run --name luncher_local_postgres --rm -p 5432:5432 luncher_local_postgres
