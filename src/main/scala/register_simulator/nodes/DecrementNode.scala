package register_simulator.nodes

import register_simulator.{Instructions, RegisterMachine, Label, Register}

class DecrementNode(val register: Register, val possible: Label, val impossible: Label) extends Instruction {

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

  private def executeNextLabel(next: Label) = {
    if (Instructions.instructions.isDefinedAt(next.index)) {
      val nextInstruction = Instructions.instructions.apply(next.index)
      nextInstruction.execute()
    } else
      println("Attempted to access undefined label")
  }
}

