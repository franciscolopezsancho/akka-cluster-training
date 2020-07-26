package example

import org.slf4j.LoggerFactory
import akka.actor._
import akka.cluster.sharding._

object Worker {

  case class Increment(workerId: String) extends CborSerializer
  case class Envelope(workerId: String, payload: Any) extends CborSerializer

  def props: Props = Props(new Worker())

  def typeName = "Worker"

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case Envelope(workerId, payload) => (workerId, payload) //TODO is anything wrong in here?
    case msg @ Increment(workerId) => (workerId, msg)
  }

  def extractShardId(shardCount: Int): ShardRegion.ExtractShardId = {
    case Envelope(workerId, payload) =>
      (workerId.hashCode % shardCount).toString
    case msg @ Increment(workerId) =>
      (workerId.hashCode % shardCount).toString
  }


  def startSharding(numShards: Int, system: ActorSystem): ActorRef = {
    ClusterSharding(system).start(
      typeName = typeName,
      entityProps = Worker.props,
      settings = ClusterShardingSettings(system),
      extractEntityId = Worker.extractEntityId,
      extractShardId = Worker.extractShardId(numShards)
    )
  }

  def startProxy(numShards: Int, system: ActorSystem): ActorRef = {
    ClusterSharding(system).startProxy(
      typeName = typeName,
      Some("proxy"), // role
      extractEntityId = Worker.extractEntityId,
      extractShardId = Worker.extractShardId(numShards)
    )
  }

}

class Worker extends Actor with ActorLogging {

  import Worker._

  var count = 0

  def receive: Receive = {
    case Worker.Increment(workerId) =>
      log.info(s"############################# I'm worker ${context.self.path} with count: $count")
      count += 1

  }

  

}
