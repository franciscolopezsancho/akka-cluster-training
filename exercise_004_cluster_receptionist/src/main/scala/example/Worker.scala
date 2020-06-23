package example

import org.slf4j.LoggerFactory
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.ActorPath



object Worker {


  def apply(count: Int): Behavior[Command]=
    Behaviors.setup { context => 
      Behaviors.receiveMessage[Command] { 
          case IncreaseOne => 
            context.log.info(s"I'm ${context.self.path} digging by now $count cubits")
            apply(count + 1)
          case GetCounter(replyTo: ActorRef[Worker.Command])  => 
            replyTo ! Counter(context.self.path.toString(), count)
            Behaviors.same
      }
    }

  sealed trait Command extends CborSerializer
  case object IncreaseOne extends Command
  case class GetCounter(replyTo: ActorRef[Nothing]) extends Command
  case class Counter(actorPath: String, count: Int) extends Command

}


