# gRPC Bulletin Board Service Project

- List of group members and NetIDs
- A short paragraph or two describing what you accomplished including details on how to use
the client if you deviated from the original design
- A short paragraph or two describing any issues you may have encountered and how you think
you could have solved them. If you didn’t have any simply state that you didn’t have any issues.
- A few sentences to answer each the following two questions:
  - In this project, we implemented a simple RPC service but we didn’t have to explicitly implement
mechanisms to handle multiple clients. Instead we just used the mechanisms the
gRPC framework provides. How does gRPC handle multiple clients?
> gRPC uses multithreading to handle multiple clients. It manages the threads using a ThreadPool.
  - With the current implementation, if we add the requirement stating that each post title
must be unique, we could end up with more than one post with the same title if two clients
post at times close enough to each other. How can we modify our implementation to ensure
this edge case won’t occur?
> The server would have to a use a thread-safe operation when adding data to the data structure that stores posts. Java includes such datastructures, for example the ```java.util.concurrent.ConcurrentHashMap<K,V>``` which ensure all operations on the object are threadsafe.
