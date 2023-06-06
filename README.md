# Echo-Server-Client



## basic function

In this exercise, you will learn how to communicate over a network connection. A TCP/IP-based echo server and the associated echo client are to be developed. The client sends text messages, which the server sends back as an "echo" to the same client.
Create separate projects for both the server and the client.

## features of the client:
  -The client is intended to allow multiple text messages to be sent to the server over one connection.
  -The server's responses should be displayed on the console.
  -When starting the client, it should be possible to specify an IP and a port number as command line parameters.
  -If the client receives a response string ending in \exit, then it should exit (not before).
Possible call of the client program:
  java -jar echo-client.jar localhost 5678
  
## Basic functions of the server:

    -The server should send back all messages received via TCP/IP, but with the prefix (IP = IP   of the client, port = port of the client).
    -After starting the server, it should output the IP(s) and port number(s) (hardcoded         allowed) on which it is receiving messages.
    -If the server receives the \exit message, it should still send the appropriate response     and close the connection to the client (the server itself should not be terminated).
    -The port of the server should be able to be set via a command line parameter. If this is     not specified, a standard port should be used.
    -Settings on the server should be made via a config file. This should be loaded at startup. 
 Possible call of the server program:
  java -jar echo-server.jar
  
## Multi-Client:
  
    Server (main class in the server project):
    Management class for connection parent data and creation of new connection (waiting for         connections)
     - Waiting for incoming connections
     - Create a new connection object
     - Manage active connections
     - (processing the configuration)
     - singleton
    Now the server has to be programmed in such a way that it is multi-client capable. To do      this, you must ensure that all blocking methods run in their own thread. The following          functions are blocking:
     - Waiting for connections: ServerSocket.accept()
     - Reading streams: DataInputStream.readUTF()
     - Reading user input: Scanner.next....()
   To allow multiple clients to connect, you must call the blocking methods on their own          thread. To achieve this separation, we recommend the advanced class design shown above.


## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
