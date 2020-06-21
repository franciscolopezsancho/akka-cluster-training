package example

import akka.actor._

object Manager {

  case object Delegate

  def props = Props(new Manager())
}

class Manager extends Actor with ActorLogging {

  override def receive = {
    case Manager.Delegate =>
      val selection = context.actorSelection(
        //TODO use Address
            s"akka://${Main.systemName}@127.0.0.1:25520/user/${Main.workersName}"
      )
      selection ! Worker.Dig
  }
}
