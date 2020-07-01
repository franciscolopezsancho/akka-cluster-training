package example

import org.slf4j.LoggerFactory
import akka.actor._
import akka.cluster.sharding._

object Worker {

  case class IncreaseOne(workerId: String) extends CborSerializer
  case object Increment extends CborSerializer
  case class Envelope(workerId: String, payload: Any) extends CborSerializer

  def props(workerId: String): Props = Props(new Worker(workerId))

  def typeName = "Worker"

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case Envelope(workerId, payload) => (workerId, payload)
    case msg @ IncreaseOne(workerId) => (workerId, msg)
  }

  def extractShardId(shardCount: Int): ShardRegion.ExtractShardId = {
    case Envelope(workerId, payload) =>
      (workerId.hashCode % shardCount).toString
    case msg @ IncreaseOne(workerId) =>
      (workerId.hashCode % shardCount).toString
  }


  def startSharding(workerId: String, numShards: Int, system: ActorSystem): ActorRef = {
    ClusterSharding(system).start(
      typeName = typeName,
      entityProps = Worker.props(workerId),
      settings = ClusterShardingSettings(system),
      extractEntityId = Worker.extractEntityId,
      extractShardId = Worker.extractShardId(numShards)
    )
  }

  def startProxy(numShards: Int, system: ActorSystem): ActorRef = {
    ClusterSharding(system).startProxy(
      typeName = typeName,
      Some("player-registry"), // role
      extractEntityId = Worker.extractEntityId,
      extractShardId = Worker.extractShardId(numShards)
    )
  }

}

class Worker(workerId: String) extends Actor with ActorLogging {

  import Worker._

  var count = 0

  def receive: Receive = {
    case Worker.IncreaseOne(workerId) =>
      log.info(s"I'm ${self.path} digging by now $count cubits")
      count += 1
    case Worker.Increment =>
      log.info(s"got till here with envelop with message")
      count += 1

  }

  

}
