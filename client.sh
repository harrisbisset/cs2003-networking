#!bin/bash

# Usage: bash client.sh [args]
javac -d ./bin $(find ./src/com/* | grep .java) && \
    cd ./bin && \
    java com/SimpleClient $@