package register_simulator

import register_simulator.nodes.{DecrementNode, IncrementNode, HaltNode, Instruction}
import register_simulator.utils.LinkedList

object Instructions {

  var instructions: List[Instruction] = Nil

  def decodeDoubleNumToPair(program: BigInt) = {
    decodeNumToPair(program)
  }

  def decodeSingleNumToPair(program: BigInt) = {
    decodeNumToPair(program + 1)
  }

  private def decodeNumToPair(program: BigInt) = {
    var value = program
    var x = BigInt.apply(0)

    while (value % 2 == 0) {
      value >>= 1
      x += 1
    }
    value -= 1
    value /= 2

    (x, value)
  }

  def decodeNumToList(program: BigInt): LinkedList[BigInt] = {
    lazy val result = new LinkedList[BigInt] // careful with that lazy
    if (program.equals(BigInt.apply(0)))
      return result
    val pair = decodeDoubleNumToPair(program)
    result += pair._1
    result ++= decodeNumToList(pair._2)
    result
  }

  def decodeProgram(program: String) = {
    val result = new LinkedList[Instruction]
    lazy val list = decodeNumToList(BigInt.apply(program))

    list.foreach(elem => result += decodeInstruction(elem))
    instructions = result.toList
    result
  }

  private def decodeInstruction(instruction: BigInt): Instruction = {
    if (instruction.equals(BigInt.apply(0)))
      return new HaltNode
    val pair = decodeDoubleNumToPair(instruction)
    if(pair._1 % 2 == 0)
      return new IncrementNode(new Register((pair._1 / 2).toInt), new Label(pair._2.toInt))
    val pairLabels = decodeSingleNumToPair(pair._2)

    new DecrementNode(new Register(((pair._1 - 1)/2).toInt),
                      new Label(pairLabels._1.toInt),
                      new Label(pairLabels._2.toInt))
  }

}
