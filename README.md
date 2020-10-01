# rabbit-mq-java-examples

Project with examples of rabbit mq features in java spring boot app. What will be covered:
* local rabbitmq setup (using docker)
* rabbitmq infrastructure setup - queues, exchanges, etc.
* spring boot app

# Usage

## Local RabbitMQ broker
```
docker-compose up &>stdout.log &
```

Check logs: ```tail -F stdout.log

Stop local infrastructure:
```
docker-compose stop
```

Decompose local infrastructure:
```
docker-compose down
```

## RabbitMQ HTTP admin console
Call ```curl -i -u rabbitmq:rabbitmq http://localhost:15672/api/whoami``` should return HTTP 200 status with info in response.
Access [rabbit-mq-http-admin-console](http://localhost:15672/) using rabbitmq/rabbitmq authentication



