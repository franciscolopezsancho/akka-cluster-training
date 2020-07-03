package example

import akka.actor.ActorSystem
import example.Worker

object Main2 extends App {

    val systemName = "diggersCO"

    val userGuardian: ActorSystem = ActorSystem(systemName)

    val workersName = "charlie"
    userGuardian.actorOf(Worker.props, workersName)
    userGuardian.actorOf(Manager.props,"CEO") ! Manager.Delegate
}