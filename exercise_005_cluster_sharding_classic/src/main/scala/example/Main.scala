package example

import example.Worker
import com.typesafe.config.ConfigFactory
import scala.util.Random
import org.slf4j.LoggerFactory
import scala.concurrent.blocking
import akka.cluster.sharding.ClusterSharding
import akka.cluster.Cluster
import akka.actor.ActorSystem
import akka.actor.AbstractActor.Receive
import akka.actor._
import akka.cluster.sharding.ClusterShardingSettings
import akka.cluster.sharding.ShardCoordinator
import akka.cluster.ClusterSettings.DataCenter


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
        akka.cluster.roles.0=creator
        """)
      .withFallback(ConfigFactory.load())

    val userGuardian =
      ActorSystem("CiscoSystem", config)


    val numShards = userGuardian.settings.config.getInt("akka.cluster.sharding.num-shards")


    if (input.role == "proxy"){

      //proxy can also be obtained if role when starting the ClusterSharding 
      // is diferent from actual one. Question couldn't they form a new ClusterSharding
      // e.g. A A B = > B is proxy but A A B and then B. Would B be ?

      val proxyRef = Worker.startProxy(numShards, userGuardian) // TODO find what's wrong with this

      // ClusterSharding(userGuardian).shardRegion(Worker.typeName) ! Worker.IncreaseOne(input.workerId) // TODO find what's wrong with this


      // alternative to check I can send messages below
      // val shard = Worker.startSharding(input.workerId, numShards, userGuardian)
      // shard ! Worker.IncreaseOne(input.workerId)

      // shard ! Worker.Envelope(input.workerId, Worker.IncreaseOne(input.workerId))//  TODO find what's wrong with this

    }

    if (input.role == "creator"){

      val shard = Worker.startSharding(input.workerId, numShards, userGuardian)

    }



  }
  
}
 


