package register_simulator.nodes

import register_simulator.{Instructions, RegisterMachine, Label, Register}

class IncrementNode(val register: Register, val next: Label) extends Instruction {

  println(s"$register- -> $next")

  override def execute(): Unit = {
    lazy val stateOp = RegisterMachine.regMachine.get(register)

    if (stateOp.isEmpty)
      RegisterMachine.regMachine.put(register, 1)
    else
      RegisterMachine.regMachine.update(register, stateOp.get + 1)

    if (Instructions.instructions.isDefinedAt(next.index)) {
      val nextInstruction = Instructions.instructions.apply(next.index)
      nextInstruction.execute()
    } else
      println("Attempted to access undefined label")
  }
}
