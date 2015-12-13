package register_simulator.nodes

import register_simulator.{Instructions, Label}

trait Instruction {

  def execute()

  protected def executeNextLabel(next: Label) = {
    if (Instructions.instructions.size < next.index) {
      val nextInstruction = Instructions.instructions.apply(next.index)
      nextInstruction.execute()
    } else
      println("Attempted to access undefined label")
  }
}
