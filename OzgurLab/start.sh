#!/bin/bash

docker-compose up -d

docker start HospitalSystem-db

docker start HospitalSystem-pgadmin

java -jar Hospital-System-0.0.1-SNAPSHOT.jar
