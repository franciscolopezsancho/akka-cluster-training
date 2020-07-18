package example

import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.ActorRef
import akka.actor.ActorPath

object Manager {

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>

      val group  = Routers.group(Worker.CounterRegisterKey)
      val router = context.spawn(group, "router")
      Behaviors.receive[Command] { (context, message) =>
        message match {
          case PollWorkers(n: Int) => 
            (0 to n).foreach(router ! IncreaseOne)

            Behaviors.same
      }
    }
  }

  sealed trait Command
  case class PollWorkers(amountWorkers: Int) extends Command
}
