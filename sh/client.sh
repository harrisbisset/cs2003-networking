#!bin/bash

# Usage: bash client.sh <IPAddress> <port>
javac -d ./bin $(find ./src/com/client/* ./src/com/util/* | grep .java) && \
    cd ./bin && \
    java com/client/SimpleClient $@