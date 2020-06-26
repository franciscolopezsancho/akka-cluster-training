`context.actorSelection` isn't supported on Akka Typed, from now on this will be done with 
the recepctionists



worth to mention:

- https://doc.akka.io/docs/akka-enhancements/current/kubernetes-lease.html ? 


prove there's only one, even if we spin multiple managers
show doesn't mantain state

akka.cluster.sharding.state-store-mode should be not persistent


to test:
first start 
sbt "runMain example.Main 25522 shard ..."
sbt "runMain example.Main 25521 shard ..."

sbt "runMain example.Main 0 worker 123" to create in any port a worker with id 123.
sbt "runMain example.Main 0 sender 123" to send a message of IncreaseOne to the worker 123.