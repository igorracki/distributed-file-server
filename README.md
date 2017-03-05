# distributed-file-server
Distributed File Server - Java RMI

# Compile and Run Server
```
javac FileInfo.java RemoteFileInt.java RemoteFileIntImpl.java RemoteFileServer.java
rmic RemoteFileIntImpl
start rmiregistry
java RemoteFileServer
```

# Compile and Run Client
```
javac Client.java ClientGUI.java
java Client
```
