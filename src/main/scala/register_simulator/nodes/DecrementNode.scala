package register_simulator.nodes

import register_simulator.{Label, Register, RegisterMachine}

class DecrementNode(val register: Register, val possible: Label, val impossible: Label) extends Instruction {

  println(s"$register- -> $possible, $impossible")
  override def execute(): Unit = {
    var stateOp = RegisterMachine.regMachine.get(register)

    if (stateOp.isEmpty){
      RegisterMachine.regMachine.put(register, 0)
      stateOp = Some[Int](0)
    }

    val state: Int = stateOp.get
    if(state > 0) {
      RegisterMachine.regMachine.update(register, state - 1)
      executeNextLabel(possible)
    } else {
      executeNextLabel(impossible)
    }
  }
}

