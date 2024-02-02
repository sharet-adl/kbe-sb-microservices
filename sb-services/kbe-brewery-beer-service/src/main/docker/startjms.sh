# !bash

docker run -it --rm \
  -p 8161:8161 \
  -p 61616:61616 \
  apache/activemq-artemis \
  -e ARTEMIS_USER=artemis \
  -e ARTEMIS_PASSWORD=simetraehcapa