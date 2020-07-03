This is simple use case of Akka Cluster Sharding

The only actor is a Counter

to test:

First create a Sharded Cluster, with the port, the role and the id of the Counter

    sbt "runMain example.Main 25522 shard 123"
    sbt "runMain example.Main 25521 shard 124"

Then create a proxy and Increase a counter sending an IncreaseOne to the Counter with id 124. 
Port below will be 0 which means random. 

    sbt "runMain example.Main 0 proxy 124" 








#### TODO
prove there's only one, even if we spin multiple managers
show doesn't mantain state

akka.cluster.sharding.state-store-mode should be not persistent

