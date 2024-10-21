# CS2003 Networking

## Usage:
### Simple
Example:
- bash server.sh 127.0.0.0 1026
- bash client.sh 127.0.0.0 1026

### Docker
For running the server as a containerized application. 

Requirements:
- Docker installed
- Docker Engine running if on windows

Example:
- bash docker-server.sh 1026

Will need to change defaults manually in the Dockerfile, if you want to change which ip and port the server runs on, or what resources are included.

### Jar File
For if you wanted to distribute the Client, or Server to multiple people, without them having to compile or download the repo.

Example:
- bash create-jar.sh server
- java -jar SimpleServer.jar 127.0.0.0 1026

schedule.txt must be in the directory if using AdvancedProtocol

### Note:
You may have problems using ports < 1000 on linux machines (the os doesn't let you).
