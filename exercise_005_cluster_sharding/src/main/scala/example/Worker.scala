package example

import org.slf4j.LoggerFactory
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.actor.ActorPath

object Worker {

  case object IncreaseOne extends Command

  def props = Props(new Worker())
}

class Worker {

  var counter = 0

  def receive: Receive = {
    case IncreaseOne =>
      context.log.info(s"I'm ${context.self.path} digging by now $count cubits")
      counter += 1
  }

}
