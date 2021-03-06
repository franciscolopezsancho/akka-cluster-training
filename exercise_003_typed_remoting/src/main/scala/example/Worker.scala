package example

import org.slf4j.LoggerFactory
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey

object Worker {

  val WorkerKey = ServiceKey[Command]("worker-key")

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      context.system.receptionist ! Receptionist.Register(
        WorkerKey,
        context.self
      )
      Behaviors.receiveMessage[Command] {
        case Dig => {
          context.log.info(s"I'm ${context.self.path}, and I'm digging")
          Behaviors.same
        }
      }
    }

  sealed trait Command
  case object Dig extends Command

}
