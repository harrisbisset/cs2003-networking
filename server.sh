#!bin/bash

# Usage: bash server.sh <IPAddress> <port>
javac -d ./bin $(find ./src/com/server/* ./src/com/util/* | grep .java) && \
    cd ./bin && \
    java com/server/SimpleServer $@