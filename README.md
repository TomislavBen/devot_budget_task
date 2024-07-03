# Devot Budget Task REST API

## Requirements

- Java Development Kit 17
- [PostgreSQL] https://www.postgresql.org/download/ for local development and testing

## Quick start

Initialize DB

> DB runs in PostgreSQL on port 5432, so make sure you don't have any other service running on this port.

> run in Terminal
> \psql
> CREATE DATABASE devottask;
> grant all privileges on database "devottask" to <yourname>;
> grant all privileges on database "devottask" to postgres;

run app

## API documentation

http://localhost:8080/docs/swagger-ui/index.html#

On first run we seed Account in base so we could log into swagger and continue testing
username: user@example.com
password: password

Or we can create Account via Postman without athorization and enter Swagger UI

POST http://localhost:8080/api/v1/register

body
{
"name":"Yourname",
"email":"Yourname@email.com",
"password":"password"
}



