package example

import org.slf4j.LoggerFactory
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.ActorPath



object Worker {

  val counterRegisterKey = ServiceKey[Worker.getClass()]("counterRegistry") //TODO why needs a class? matter which class I pick?


  def apply(count: Int): Behavior[Command]=
    Behaviors.setup { context => 
        context.system.receptionist ! Receptionist.Register(
            counterRegisterKey,
            context.self
          )
      Behaviors.receiveMessage[Command] { 
          case IncreaseOne => 
            context.log.info(s"I'm ${context.self.path} digging by now $count cubits")
            apply(count + 1)
      }
    }

  sealed trait Command extends CborSerializer
  case object IncreaseOne extends Command
  

}


