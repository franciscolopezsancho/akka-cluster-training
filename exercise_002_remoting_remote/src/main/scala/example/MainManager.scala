package example

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import example.Worker

object MainManager extends App {

    val systemName = "diggersCO"

    val config = ConfigFactory.load()


    val userGuardian: ActorSystem = ActorSystem(systemName,config.getConfig("managerApp").withFallback(config))

    userGuardian.actorOf(Manager.props,"CEO") ! Manager.Delegate
}