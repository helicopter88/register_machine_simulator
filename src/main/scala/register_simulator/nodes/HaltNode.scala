package register_simulator.nodes

/**
  * Created by Domenico on 10/12/2015.
  */
class HaltNode extends Instruction {

  println(s"HALT")

  override def execute() = {}
}
