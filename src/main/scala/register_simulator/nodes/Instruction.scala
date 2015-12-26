package register_simulator.nodes

import register_simulator.{Instructions, Label}

trait Instruction {

  def execute(): String

  protected def executeNextLabel(next: Label): String = {
    if (Instructions.instructions.size > next.index) {
      val nextInstruction = Instructions.instructions.apply(next.index)
      nextInstruction.execute()
    } else
      "Attempted to access undefined label\n"
  }
}
