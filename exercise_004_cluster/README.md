`context.actorSelection` isn't supported on Akka Typed, from now on this will be done with 
the recepctionists



worth to mention:

- https://doc.akka.io/docs/akka-enhancements/current/kubernetes-lease.html ? 



use roles to create managers or workers?


bare in mind that is necessary initiate the cluster adding first instances that represent the seed nodes.
This is not you environmet production conf shouldn't.

Once the cluster is formed any port can be used, that's why the zero.

This all is inspired in the akka-samples