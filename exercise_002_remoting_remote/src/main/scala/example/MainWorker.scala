package example

import akka.actor.ActorSystem
import example.Worker
import example.MainManager

object MainWorker extends App {


    val userGuardian: ActorSystem = ActorSystem("diggersCO")

    val workersName = "jeffrey"

    userGuardian.actorOf(Worker.props, workersName)
}