package register_simulator

import java.io._

import org.antlr.v4.runtime.{ANTLRFileStream, ANTLRInputStream, CommonTokenStream}
import register_simulator.nodes.Instruction
import register_simulator.utils.{Decoder, Encoder, LinkedList}

object Main {
  var encode = true
  var decode = false
  var fileName: ANTLRInputStream = new ANTLRInputStream(System.in)
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
        // If the line contains a separator, it probably is a path
        case str if str.contains(File.separator) =>
          encode = true
          fileName = new ANTLRFileStream(str)
        // otherwise, it probably is just the number we want to decode
        case str =>
          decode = true
          number = str
      }
    }
  }

  def main(args: Array[String]) {
    parseArgs(args)
    if (encode) {
      val tokens = new CommonTokenStream(new RegisterSimulatorLexer(fileName))
      val fileParser = new RegisterSimulatorParser(tokens)
      val registerVisitor = new RegisterVisitor
      Instructions.instructions = new LinkedList[Instruction](registerVisitor.visit(fileParser.program()))
      println(s"Encoded instructions: ${Encoder.encodeProgram(Instructions.instructions)}")
    }

    if (decode)
      println(s"Decoded instructions: \n${Decoder.decodeProgram(number)}")


    Instructions.instructions.head.execute()
    println(RegisterMachine.regMachine)
  }
}
