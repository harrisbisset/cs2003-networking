# CS2003 Networking

## Usage:

### Simple
Example:
- bash sh/server.sh 127.0.0.0 1026
- bash sh/client.sh 127.0.0.0 1026

### Docker
For running the server as a containerized application. 

Requirements:
- Docker installed
- Docker Engine running if on windows

Example:
- bash sh/docker-server.sh 1026

Will need to change defaults manually in the Dockerfile, if you want to change which ip and port the server runs on.


### Jar File
For if you wanted to distribute the Client, or Server to multiple people, without them having to compile or download the repo.

Example:
- bash sh/create-jar.sh server
- java -jar SimpleServer.jar 127.0.0.0 1026

### Note:
You may have problems using ports < 1000 on linux machines (the os doesn't let you).
