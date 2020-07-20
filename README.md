# Distributed-Shared-White-Board

In this project, a shared whiteboard which allows multiple users to draw simultaneously on a canvas is designed and implemented using client-server architecture. A single central server manages all client’s states and the communication between each other. The server uses Java RMI framework. Remote interfaces and servants are designed to make the communication between server and clients. Threads are the lowest level of abstraction for network communication and concurrency.

## Running

Server

```
java –jar Server.jar <port>
```

Client

```
java –jar Client.jar <server-address> <server-port>
```

## Built With
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Haichao Song** - *University of Melbourne*
