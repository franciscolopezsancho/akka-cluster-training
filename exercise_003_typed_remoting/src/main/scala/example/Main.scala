package example

import akka.actor.typed.ActorSystem
import example.Worker

object Main extends App {


    val factoryUserGuardian: ActorSystem[Factory.Command] = ActorSystem(Factory(),"testing-remote-factory")
    val managerUserGuardian: ActorSystem[Manager.Command] = ActorSystem(Manager(),"testing-remote-manager")

    factoryUserGuardian ! Factory.ContractWorker
    factoryUserGuardian ! Factory.ContractWorker

    Thread.sleep(3000)


    managerUserGuardian ! Manager.Delegate
}