@echo off

echo All containers are going to be stopped.

docker stop HospitalSystem-db
echo PostgreSQL is stopped.
docker stop HospitalSystem-pgadmin
echo Pg Admin is stopped.