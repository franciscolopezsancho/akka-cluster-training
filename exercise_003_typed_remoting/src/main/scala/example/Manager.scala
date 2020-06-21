package example

import akka.actor.typed._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed._
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey

object Manager {


  def apply(): Behavior[Command] =
    Behaviors.setup { context => 
      //context.spawnAnonymous(Worker())// TODO ?? why juust for testing§
      val listingResponseAdapter = context.messageAdapter[Receptionist.Listing](ListingResponse)

      Behaviors.receive[Command] { (context, message) =>
        message match {
          case Delegate =>
            //Address
            //RootActorPath
            context.system.receptionist ! Receptionist.Find(Worker.WorkerKey,listingResponseAdapter)
            Behaviors.same
          
          case ListingResponse(Worker.WorkerKey.Listing(listings)) => 
            context.log.debug(s"found $listings")
            listings.foreach(actor => actor ! Worker.Dig)
            Behaviors.same
        }

      }
    }

  sealed trait Command
  case object Delegate extends Command
  private case class ListingResponse(listing: Receptionist.Listing) extends Command
}
