ChainCommander
==============

# Download
http://download.pgon.ca/chaincommander-0.0.1-SNAPSHOT.jar

# Description
Upload some files with clients in chain. When the client connects to the server, the server will tell him the last client connected so that he can download from that one. This design makes it quick to upload a file to multiple computer on a local network since each client is a server and each server only keeps one client connected. At each point in time each node is connected to one server and to one client. No more.

Master node -> Client node 1 -> Client node 2 -> Client node 3 -> Client node 4 -> Client node 5