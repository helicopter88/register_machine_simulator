package register_simulator

import java.io._

import org.antlr.v4.runtime.{CommonTokenStream, ANTLRFileStream}

import scala.collection.mutable

object Main extends App {
  val bri = new BufferedReader(new InputStreamReader(System.in))
  val bro = new BufferedWriter(new FileWriter("in.txt"))
  var line = bri.readLine
  while (line != null) {
    bro.write(line + "\n")
    line = bri.readLine
  }
  bro.close()

  val file = "in.txt"
  val tokens = new CommonTokenStream(new RegisterSimulatorLexer(new ANTLRFileStream(file)))
  val fileParser = new RegisterSimulatorParser(tokens)
  val registerVisitor = new RegisterVisitor
  Instructions.instructions = registerVisitor.visit(fileParser.program()).toList

  Instructions.instructions.head.execute()
  println(RegisterMachine.regMachine)

  new File(file).delete
}
