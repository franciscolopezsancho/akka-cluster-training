

One of the main abstractiono of Akka is the referencial transparency that ActorRef provides.
No matter where that actor lives, it's real url or path is hidden under its reference.

In here we'll see how with just the name of the actor we can acquire a reference to it to afterwards us it for sending messages.

after that we can connect to local, remote or cluster ()


The difference between loocal and remote is just a matter of configurationo.
In this sample we'll configure it to work just in local (see applicaation.conf) and see how  
when trying to connect two equal but not the same ActorSystems wouldn't work.  

To check it works spinning Worker and Manager in the same JVM

        sbt runMain example.Main 

To check it doesn't when running in different ones

        sbt "runMain example.Main worker"
        sbt "runMain example.Main manager"


        