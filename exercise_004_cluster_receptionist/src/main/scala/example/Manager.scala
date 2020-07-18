package example

import akka.actor.typed._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorRef
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import akka.actor.typed.ActorRef
import akka.actor.ActorPath
import example.Main.UserGuardian

object Manager {

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      //context.spawnAnonymous(Worker())// TODO ?? why juust for testing§

      var currentWorkers: Map[String, Int] = Map.empty

      val listingResponseAdapter =
        context.messageAdapter[Receptionist.Listing](ListingResponse)

      val counterMessageAdapter =
        context.messageAdapter[Worker.Pong](WorkerPong)

      Behaviors.receive[Command] { (context, message) =>
        message match {
          case Subscribe =>
            context.system.receptionist ! Receptionist.Subscribe(
              Worker.CounterRegisterKey,
              listingResponseAdapter
            )
            Behaviors.same

          case ListingResponse(
              Worker.CounterRegisterKey.Listing(listings)
              ) =>
            listings.foreach(actor =>
              actor ! Worker.Ping(counterMessageAdapter)
            ) //here's the tricky bit
            Behaviors.same

          case WorkerPong(Worker.Pong(worker, number)) =>
            context.log.info(s"pong from $worker with random number $number")
            Behaviors.same
        }

  }
    }

  sealed trait Command
  case object Subscribe extends Command
  private case class ListingResponse(listing: Receptionist.Listing)
      extends Command
  case class WorkerPong(workerCounter: Worker.Pong) extends Command
}
