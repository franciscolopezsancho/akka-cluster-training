package example

import akka.actor.ActorSystem
import example.Worker

/** 
 * In this sample we'll create a Worker Actor without retainig it's ActorRef
 * Then create a Manager actor that will Delegate some work to the previous created worker 
 * 
 * This shows how an actor can be referenced just by name. Using that name to find an ActorRef.
 * 
 * 
 * */
object Main {

    val userGuardian: ActorSystem = ActorSystem("diggersCO")


    /**
      * Accepted values are 
      * no value => that will spin Worker and Manager
      * "worker" => that will spin a Worker
      * "manager" => that will spin a Manager
      *
      * @param args
      */
    override def main(args: Array[String]): Unit = {

            userGuardian.actorOf(Worker.props, workersName)
            userGuardian.actorOf(Manager.props,"CEO") ! Manager.Delegate(workersName)

    }

   
}