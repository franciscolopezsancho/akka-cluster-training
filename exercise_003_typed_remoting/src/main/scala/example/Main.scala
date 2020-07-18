package example

import akka.actor.typed.ActorSystem
import example.Worker
import com.typesafe.config.ConfigFactory
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Main extends App {

  val factoryUserGuardian: ActorSystem[Factory.Command] =
    ActorSystem(Factory(), "testing-remote")

  factoryUserGuardian ! Factory.ContractWorker
  factoryUserGuardian ! Factory.ContractWorker

  factoryUserGuardian ! Factory.Organize

}

object Factory {

  sealed trait Command
  case object ContractWorker extends Command
  case object Organize extends Command

  def apply(): Behavior[Command] = {
    Behaviors.setup[Command] { context => 
      Behaviors.receive[Command] { (context, message) =>
        message match {
          case ContractWorker =>
            context.spawn(
              Worker(),
              s"worker-${scala.util.Random.nextInt(Int.MaxValue)}"
            )
            Behaviors.same

          case Organize =>
            val manager = context.spawn(
              Manager(),
              s"manager-${scala.util.Random.nextInt(Int.MaxValue)}"
            )
            manager ! Manager.Delegate
            Behaviors.same
        }
      }

    }
  }
}
