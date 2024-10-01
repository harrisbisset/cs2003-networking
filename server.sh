#!bin/bash

# Usage: bash server.sh [args]
javac -d ./bin $(find ./src/com/* | grep .java) && \
    cd ./bin && \
    java com/SimpleServer $@