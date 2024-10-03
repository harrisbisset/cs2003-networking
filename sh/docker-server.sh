#!/bin/bash

# Usage: bash docker-server.sh <port>

if [ "$#" -ne 1 ]; then
    echo "bad number of arguements"
    echo "Usage: bash docker-server.sh <port>"
    exit 1
fi

# stop and running containers with "simple-server-build" image name
rm=$(docker stop $(docker ps -a -q --filter "ancestor=simple-server-build") && /
    docker rm $(docker ps -a -q --filter "ancestor=simple-server-build"))
if [ $? -ne 0 ]; then
    echo "clean failed"
    exit 2
fi

build=$(docker --debug build --tag simple-server-build .)
if [ $? -ne 0 ]; then
    echo "build failed"
    exit 3
fi

run=$(docker run -d --name simple-server -p $1:$1 simple-server-build)
if [ $? -ne 0 ]; then
    echo "run failed"
    exit 4
fi

# if fail, use to debug:
# docker build --no-cache --progress plain --tag simple-server-build .

# if fail due to memory issues
# docker system prune -af && docker image prune -af && docker system prune -af --volumes && docker system df