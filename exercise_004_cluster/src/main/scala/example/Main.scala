package example

import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent.MemberEvent


object Main {

  object UserGuardian {

    def apply(): Behavior[MemberEvent] = {
      Behaviors.setup[MemberEvent]{ context => 
        context.log.debug("Starting guardian")
        context.spawn(ClusterListener(),"Just-Listening")
        Behaviors.same
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val ports = 
      if (args.isEmpty)
        Seq(0)
      else args.toSeq.map(_.toInt)
       
    ports.foreach(setup _ )
  }

      
  def setup(port: Int) = {
    val config = ConfigFactory
      .parseString(s"akka.remote.artery.port=$port")
      .withFallback(ConfigFactory.load())

    ActorSystem[MemberEvent](UserGuardian(), "ClusterListener", config)

  }

}
