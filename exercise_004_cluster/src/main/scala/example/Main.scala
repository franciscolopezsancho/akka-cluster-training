package example

import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent.MemberEvent
import org.slf4j.LoggerFactory

object Main {

  val logger = LoggerFactory.getLogger(Main.getClass())

  def main(args: Array[String]): Unit = {
    val ports = 
      if (args.isEmpty)
        Seq(0)
      else args.toSeq.map(_.toInt)
       
    ports.foreach(setup)
  }

      
  def setup(port: Int) = {
    val config = ConfigFactory
      .parseString(s"akka.remote.artery.canonical.port=$port")
      .withFallback(ConfigFactory.load())


    ActorSystem[Nothing](UserGuardian(), "ClusterListener", config)

  }


  object UserGuardian {

    def apply(): Behavior[Nothing] = {
      Behaviors.setup[Nothing]{ context => 
        context.log.debug("Starting guardian")
        context.spawn(ClusterListener(),"Just-Listening")
        Behaviors.same
      }
    }
  }

}
