package example

import akka.actor._


object Manager {


  case object Delegate 

  def props = Props(new Manager())
}

class Manager extends Actor with ActorLogging {

  override def receive = {
    case Manager.Delegate =>
      //Address
      //RootActorPath
      val selection = context.actorSelection(
            s"akka://${MainManager.systemName}@127.0.0.1:25520/user/jeffrey"
      )
      selection ! Worker.Dig
  }
}
