tinyapps
========

A couple small applications I made

To compile and run:
* gradle run

To create a zip:
* gradle distZip

To load in Eclipse:
* Simply import the projects since all the Eclipse's project files are present in the repository.

Applications:
* ChainCommander: Upload some files with clients in chain. When the client connects to the server, the server will tell him the last client connected so that he can download from that one.
* UpdateWatcher: This is a program that checks the filesystem for any change and run the command. It supports to have a quiet period.
