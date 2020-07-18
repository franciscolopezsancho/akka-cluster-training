package example

import org.slf4j.LoggerFactory
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.ActorPath
import scala.util.Random
object Worker {

  val CounterRegisterKey = ServiceKey[Worker.Command]("counterRegistry") //TODO why needs a class? matter which class I pick?

  def apply(count: Int): Behavior[Command] =
    Behaviors.setup { context =>
      context.system.receptionist ! Receptionist.Register(
        CounterRegisterKey,
        context.self
      )

      Behaviors.receiveMessage[Command] { 
          case Ping(replyTo: ActorRef[Worker.Pong]) =>
            replyTo ! Pong(context.self.path.toString(), Random.nextInt(14))
            Behaviors.same
      }
    }

  sealed trait Command extends CborSerializer
  case class Ping(replyTo: ActorRef[Nothing]) extends Command
  case class Pong(actorPath: String, count: Int) extends Command

}
