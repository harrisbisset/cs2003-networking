#!bin/bash

# Usage: bash create-jar.sh <server/client>
if [ "$#" -ne 1 ]; then
    echo "bad number of arguements"
    echo "Usage: bash create-jar.sh <server/client>"
    exit 1
fi

if [ "$1" == "server" ]; then
    result=$(javac -d ./bin $(find ./src/com/server/* ./src/com/util/* | grep .java) && \
            echo Main-Class: com.server.SimpleServer > MANIFEST.MF && \
            jar -cfm SimpleServer.jar MANIFEST.MF -C ./bin . && \
            rm MANIFEST.MF)
    exit $result

elif [ "$1" == "client" ]; then
    result=$(javac -d ./bin $(find ./src/com/client/* ./src/com/util/* | grep .java) && \
            echo Main-Class: com.server.SimpleClient > MANIFEST.MF && \
            jar -cfm SimpleClient.jar MANIFEST.MF -C ./bin . && \
            rm MANIFEST.MF)
    exit $result
fi

echo "invalid paramter passed in"
exit 2