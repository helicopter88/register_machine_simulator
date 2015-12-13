package register_simulator.utils

import register_simulator.nodes.{DecrementNode, IncrementNode, HaltNode, Instruction}

object Encoder {
  private def encodeDoublePair(pair: (BigInt, BigInt)) = (BigInt.apply(2) ^ pair._1) * (pair._2 * 2 + 1)

  private def encodeSinglePair(pair: (BigInt, BigInt)) = encodeDoublePair(pair) - 1

  private def encodeList(list: LinkedList[BigInt]): BigInt = {
    if (list.isEmpty)
      return BigInt.apply(0)
    encodeDoublePair(list.head, encodeList(list.tail()))
  }

  private def encodeInstruction(instruction: Instruction) = {
    instruction match {
      case instruction: HaltNode => BigInt.apply(0)
      case instruction: IncrementNode =>
        encodeDoublePair(BigInt.apply(instruction.register.index * 2), BigInt.apply(instruction.next.index))
      case instruction: DecrementNode =>
        encodeDoublePair(BigInt.apply(instruction.register.index * 2 + 1),
          encodeSinglePair(BigInt.apply(instruction.possible.index),
            BigInt.apply(instruction.impossible.index)))
    }
  }

  def encodeProgram(instructions: LinkedList[Instruction]) = {
    val listNum = new LinkedList[BigInt]
    instructions.foreach(elem => listNum += encodeInstruction(elem))
    encodeList(listNum)

  }
}
