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
import akka.cluster.sharding.typed.scaladsl._
import scala.concurrent.blocking

object Main {

  val logger = LoggerFactory.getLogger(Main.getClass())

  case class ConsoleInput(port: Int, role: String, workerId: String)

  def main(args: Array[String]): Unit = {
    val input = 
      if(args.isEmpty || args.length < 3) {
        logger.info("No input, falling back to default port: 25521, role: shard and empty workerId")
        ConsoleInput(25521,"shard","")
      }
      else ConsoleInput(args(0).toInt, args(1), args(2))

    val config = ConfigFactory
      .parseString(s"""
        akka.remote.artery.canonical.port=${input.port}
        akka.cluster.roles.0=${input.role}
        """)
      .withFallback(ConfigFactory.load())

    val userGuardian =
      ActorSystem[Nothing](UserGuardian(input.workerId), "CiscoSystem", config)


  }

  object UserGuardian {

    def apply(workerId: String): Behavior[Nothing] = {
      Behaviors.setup[Nothing] { context =>


        val sharding = ClusterSharding(context.system)

        if (Cluster(context.system).selfMember.hasRole("sender")) {
          sharding.init(Entity(Worker.CounterEntityTypeKey)(entityContext => Worker(0,entityContext.entityId)))
          val workerRef: EntityRef[Worker.Command] = sharding.entityRefFor(Worker.CounterEntityTypeKey, workerId)
          workerRef ! Worker.IncreaseOne

          blocking{
            Thread.sleep(3000)
          }
          context.system.terminate
        }

        if (Cluster(context.system).selfMember.hasRole("worker")) {
         sharding.init(Entity(Worker.CounterEntityTypeKey)(entityContext => Worker(0,entityContext.entityId)))
          val worker: ActorRef[Worker.Command] =
            context.spawn(Worker(0,workerId), s"${workerId}")
          blocking{
            Thread.sleep(3000)
          }
          context.system.terminate

        }

        if (Cluster(context.system).selfMember.hasRole("shard")) {
          sharding.init(Entity(Worker.CounterEntityTypeKey)(entityContext => Worker(0,entityContext.entityId)))
        }

        Behaviors.same

      }

    }

  }

}
