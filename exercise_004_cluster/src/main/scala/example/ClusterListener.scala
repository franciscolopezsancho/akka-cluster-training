package example

import akka.cluster.ClusterEvent._
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors


object ClusterListener {

  def apply(): Behavior[MemberEvent] = {
    Behaviors.receive[MemberEvent] { (context, message) =>
      message match {
        case MemberUp(member) =>
            context.log.info(s"### $member is UP")

        case MemberRemoved(member, previousStatus) => 
            context.log.info(s"### $member it was $previousStatus but now is DOWN")
    
        case x => context.log.info(s"### event $x")

      }
      Behaviors.same

    }

  }

}
