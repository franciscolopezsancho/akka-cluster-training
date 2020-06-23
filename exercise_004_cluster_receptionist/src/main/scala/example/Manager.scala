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

      val counterMessageAdapter = context.messageAdapter[Worker.Counter](WorkerCounter)

      Behaviors.receive[Command] { (context, message) =>
        message match {
          case Subscribe =>
            context.system.receptionist ! Receptionist.Subscribe(
              UserGuardian.counterRegisterKey,
              listingResponseAdapter
            )
            Behaviors.same

          case ListingResponse(UserGuardian.counterRegisterKey.Listing(listings)) =>
            context.log.debug(s"found $listings")
            listings.foreach { worker => 
              //initializing
              if(!currentWorkers.contains(worker.path.toString())) currentWorkers.updated(worker.path.toString(),0)
            }
            listings.foreach(actor => actor ! Worker.GetCounter(counterMessageAdapter)) //here's the tricky bit
            Behaviors.same

          case WorkerCounter(Worker.Counter(worker, amount)) =>
            if (currentWorkers.forall{ case (actorPath, counter) => counter > 0 })
               context.log.info(s"total counter for all workers is ${currentWorkers.values.sum}")
            else
              context.log.debug(s"no counter for worker $worker. Initializing to 0")
              currentWorkers = currentWorkers.updated(worker,amount)
            Behaviors.same
        }

      }
    }

  sealed trait Command
  case object Subscribe extends Command
  private case class ListingResponse(listing: Receptionist.Listing)
      extends Command
  case class WorkerCounter(workerCounter: Worker.Counter) extends Command
}
