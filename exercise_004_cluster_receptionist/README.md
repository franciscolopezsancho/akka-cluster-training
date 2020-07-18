In this exercise we'll just create another sample of what can be done with the rececptionist. While in the previous exercise we'll subscribed to events of the system we'll now subscribe to events of our own domain. In this case every time an actor "Worker" get's added to the cluster it will register itself in the receptionist. On the other side the manager is subscribed to the same key the Worker get registered in the receptionist. This way when/if a new Worker gets registered, the Manager will get the list of all the actors currently registered to that key. In the case that the Manager subscribes after some worker have already registered it will get initially the list of all those already subscribed workers. 

We are using here also adapter because two use cases.
One; the receptionist is sending a message to the Manager that doesn't belong to the manager's API. Two; the Worker is also responding with a `Pong` that belongs to the Worker not the Manager.

To run this example, is required to form a cluster on the seed-nodes 25520, 25521 and have at least one Manager and one Worker.

 sbt "runMain example.Main 25520 worker"
 sbt "runMain example.Main 25521 worker"
 sbt "runMain example.Main 0 manager"
 