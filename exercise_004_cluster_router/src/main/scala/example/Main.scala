package example

import akka.actor.typed.ActorSystem
import example.Worker
import com.typesafe.config.ConfigFactory
import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.cluster.typed._
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.receptionist.Receptionist
import scala.util.Random
import org.slf4j.LoggerFactory

object Main {

  val logger = LoggerFactory.getLogger(Main.getClass())

  def main(args: Array[String]): Unit = {
    val port = args(0).toInt
    val role = args(1)

    val config = ConfigFactory
      .parseString(s"""
        akka.remote.artery.canonical.port=$port
        akka.cluster.roles.0=$role
        """)
      .withFallback(ConfigFactory.load())


    val userGuardian = ActorSystem[Nothing](UserGuardian(), "CiscoSystem", config)

  }

  object UserGuardian {

    val counterRegisterKey = ServiceKey[Worker.GetCounter]("counterRegistry") //TODO why needs a class? matter which class I pick?

    def apply(): Behavior[Nothing] = {
      Behaviors.setup[Nothing] { context =>
        if (Cluster(context.system).selfMember.hasRole("manager")) {
          val manager =
            context.spawn(Manager(), s"manager-${Random.nextInt(4)}")
          manager ! Manager.Subscribe

        }

        if (Cluster(context.system).selfMember.hasRole("worker")) {
          val worker: ActorRef[Worker.Command]  = context.spawn(Worker(0), s"worker-${Random.nextInt(4)}")
          context.system.receptionist ! Receptionist.Register(
            counterRegisterKey,
            worker
          )
          worker ! Worker.IncreaseOne

        }
        Behaviors.same

      }

    }

  }

}
