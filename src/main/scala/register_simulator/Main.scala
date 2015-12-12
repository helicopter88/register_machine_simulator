package register_simulator

import java.io._

import org.antlr.v4.runtime.{CommonTokenStream, ANTLRFileStream}

/**
  * Created by domenico on 10/12/15.
  */
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
  val instructions = registerVisitor.visit(fileParser.program())
  println(instructions)
  // and then delete it
  new File(file).delete
}
