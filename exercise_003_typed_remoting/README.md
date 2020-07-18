`context.actorSelection` isn't supported on Akka Typed, from now on this will be done with 
the recepctionists. The reason is discontinued is because the answer wasn't an ActorRef, was a substitute that
not only didn't have types in it. Also could be empty, so had to be checked that the ref represented an actual
actor before could be used (have a look how in here if interested)

The way this works is quite simple to understand. The akka.actor.typed.Recepcionist is an Object (called CRDT) that lives in each of the nodes of the cluster. The implementation of it CRDT properties is achieve through Distribute Data (DD) Akka li. This actor is in charge of registering actors in a map, where the map is Map[...receptionist.ServiceKey, ...ActorRef]. (... = akka.actor.typed). The recepcionist state is replicated as DD

This receptionist will respond to three messages. Find, Listing and Subscribe. First two are will get back one or multiple registered ActorRefs , respectively. And the latter will add and ActorRef to that map. 




We'll see later on how can the receptcioninst be used we a group Router.


Now they have to form cluster! TODO ask akka-applied




do they have to be in the same name "actorSystem"?


