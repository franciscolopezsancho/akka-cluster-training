In this case here we'll use now the cluster configuration plus the events that already are published (by whom?) when member of a cluster get in state UP or DOWN


Couple of things we should have in mind here. First we'll subscribe to events of the system. Previously we were kind of subscribing (actually finding but the mechanism is the same) to event of the receptionist we were registering ourselves. Now we are listening to event that the internal [system guardian](https://doc.akka.io/docs/akka/current/typed/guide/tutorial_1.html) actor is generating. Specifically `akka.cluster.ClusterEvent.MemberEvent` 

Because this event don't belong to the Actor `ClusterListener` we will create to listen to these changes, we'll have to add an adapter the can hold one message our actor recognizes that in itself holds a message of the type we want to listen to.

Two things we said. Subscribing and adapter. Then once that Listener is created we just need define in what are the mechanism we choose to start the cluster. It will be seed-nodes through the definition of the socket in `seed-nodes` = [a,b]
in application.conf plus state also in there that `akka.actor.provider` is cluster and no `local` as the previous exercise.


to test you could create first the cluster
 
   sbt "runMain example.Main 25520"
   sbt "runMain example.Main 25521"

after this the cluster would be then formed and adding a new node as:

   sbt "runMain example.Main " 

would show in the message we added in the logs as Member UP conversely if this node is stopped would show that Member is DOWN






For the sake of simplicity we can add `akka.cluster.jmx.multi-mbeans-in-same-jvm = on` to allow multiple ActorSystems start in the same JVM. So this would allow 

    sbt "runMain example.Main 25520 25521" // that will start the cluster in one go