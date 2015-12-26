package register_simulator

import org.antlr.v4.runtime.{ANTLRFileStream, ANTLRInputStream, CommonTokenStream}
import register_simulator.nodes.Instruction
import register_simulator.utils.{Decoder, Encoder, LinkedList}

object Main {
  var encode = true
  var decode = false
  var fileName: ANTLRInputStream = null
  var number = "0"

  private def parseArgs(args: Array[String]): Unit = {

    for (arg <- args) {
      arg match {
        case "-e" | "--encode" =>
          decode = false
          encode = true
        case "-d" | "--decode" =>
          decode = true
          encode = false
        // Check for numbers, if we find them, we probably want to decode them
        case str if str.matches("[0-9]+") =>
          decode = true
          number = str
        // otherwise, just interpret it as a file
        case str =>
          encode = true
          fileName = new ANTLRFileStream(str)
      }
    }
  }

  def main(args: Array[String]) {
    parseArgs(args)
    if (encode) {
      if (fileName == null)
        fileName = new ANTLRInputStream(System.in)
      val tokens = new CommonTokenStream(new RegisterSimulatorLexer(fileName))
      val fileParser = new RegisterSimulatorParser(tokens)
      val registerVisitor = new RegisterVisitor
      Instructions.instructions = new LinkedList[Instruction](registerVisitor.visit(fileParser.program()))
      println(s"Encoded instructions: ${Encoder.encodeProgram(Instructions.instructions) }")
    }

    if (decode) {
      lazy val decodedInstructions = Decoder.decodeProgram(number)
      println("Decoded Instructions:")
      // Use zipWithIndex so that we can access both the index and the instruction
      // to print them neatly
      decodedInstructions.zipWithIndex.foreach(pair => println(s"L${pair._2 }: ${pair._1 }"))
    }

    println("Execution output:")
    try {
      Instructions.instructions.head.execute()
    } catch {
      // If we get a stack overflow error, it probably means
      // some label was recursively calling itself directly or indirectly roughly 65k times
      // it means that the program is looping
      case e: StackOverflowError => println("Infinite loop during execution.")
    }
    println("Final register state:")
    println(RegisterMachine.regMachine.mkString(","))
  }
}
