package example

import org.slf4j.LoggerFactory
import akka.actor._



object Worker {

  case object Dig 

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


