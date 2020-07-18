package example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.cluster.ClusterEvent.MemberEvent
import akka.cluster.ClusterEvent.MemberRemoved
import akka.cluster.ClusterEvent.MemberUp
import akka.actor.typed.ActorRef
import akka.cluster.typed.Subscribe
import akka.cluster.typed.Cluster

object ClusterListener {

  sealed trait Event
  private case class CLChanged(event: MemberEvent) extends Event

  def apply(): Behavior[Event] = {
    Behaviors.setup[Event] { context =>
      val memberEvent: ActorRef[MemberEvent] = context.messageAdapter(CLChanged)
      Cluster(context.system).subscriptions ! Subscribe(
        memberEvent,
        classOf[MemberEvent]
      )

      Behaviors.receive[Event] { (context, message) =>
        message match {
          case CLChanged(systemMessage) =>
            systemMessage match {
              case MemberUp(member) =>
                context.log.info(s"### $member is UP")

              case MemberRemoved(member, previousStatus) =>
                context.log.info(
                  s"### $member it was $previousStatus but now is DOWN"
                )
              case x => context.log.info(s"### event $x")
            }

        }
        Behaviors.same
      }

    }

  }

}
