package example

import akka.actor._

object Manager {

  case class Delegate(workersName: String)

  def props = Props(new Manager())
}

class Manager extends Actor with ActorLogging {

  val config = context.system.settings.config

  val port = config.getInt("akka.remote.artery.canonical.port")
  val hostname = config.getInt("akka.remote.artery.canonical.hostname")

  override def receive = {
    case Manager.Delegate(workersName) =>
      val selection = context.actorSelection(
        //TODO use Address
            s"akka://${context.system.name}@$hostname:$port/user/$workersName"
      )
      selection ! Worker.Dig
  }
}
