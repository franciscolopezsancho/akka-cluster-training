package example

import org.slf4j.LoggerFactory
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.actor.ActorPath



object Worker {

  val CounterEntityTypeKey = EntityTypeKey[Worker.Command]("counterRegistry")//TODO review the name


  def apply(count: Int, entityId: String): Behavior[Command]=
    Behaviors.setup { context => 
      Behaviors.receiveMessage[Command] { 
          case IncreaseOne => 
            context.log.info(s"I'm ${context.self.path} digging by now $count cubits")
            apply(count + 1, entityId)
      }
    }

  sealed trait Command extends CborSerializer
  case object IncreaseOne extends Command
  

}


