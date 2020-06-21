package example

import org.slf4j.LoggerFactory
import akka.actor._


trait CborSerializable

object Worker {

  case object Dig extends CborSerializable 

  def props = Props(new Worker())
}

class Worker extends Actor with ActorLogging {

  override def receive = {
    case Worker.Dig => {
      log.info(s"I'm ${context.self.path} digging")
      context.system.terminate
    }
  }
}


