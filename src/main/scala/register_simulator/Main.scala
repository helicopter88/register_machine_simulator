package register_simulator


import java.io.StringReader

import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream, RecognitionException}
import register_simulator.nodes.Instruction
import register_simulator.utils.{Decoder, Encoder, LinkedList}

import scala.swing.Dialog.Message
import scala.swing._

object Main extends SimpleSwingApplication {
  private val input = new TextArea() {
    columns = 10
    rows = 10
  }
  private val button = new Button {
    text = "Simulate"
    action = new Action("Simulate") {
      override def apply(): Unit = {
        simulate(input.text)
      }
    }
  }
  private val flowPanel = new FlowPanel(new swing.Label("Input"), new ScrollPane(input), button)

  def top = new MainFrame() {
    resizable = true
    title = "Register simulator"
    contents = flowPanel
  }

  def simulate(text: String) {
    val stringBuilder = new StringBuilder("")
    text match {
      case empty if text.isEmpty =>
        Dialog.showMessage(messageType = Message.Error, title = "Error", message = "Please input something")
        return
      case number if text.matches("[0-9]+") =>
        lazy val decodedInstructions = Decoder.decodeProgram(number)
        stringBuilder.append("Decoded Instructions:\n")
        // Use zipWithIndex so that we can access both the index and the instruction
        // to print them neatly
        decodedInstructions.zipWithIndex.foreach(pair => stringBuilder.append(s"L${pair._2}: ${pair._1}\n"))

      case program =>
        val fileName = new ANTLRInputStream(new StringReader(input.text))
        try {
          val tokens = new CommonTokenStream(new RegisterSimulatorLexer(fileName))
          val fileParser = new RegisterSimulatorParser(tokens)
          val registerVisitor = new RegisterVisitor
          Instructions.instructions = new LinkedList[Instruction](registerVisitor.visit(fileParser.program()))
          stringBuilder.append(s"Encoded instructions:\n ${Encoder.encodeProgram(Instructions.instructions)}")
        } catch {
          case e: RecognitionException =>
            Dialog.showMessage(messageType = Message.Error, title = "Error", message = "Error while parsing input text")
        }
    }


    stringBuilder.append("Execution output:\n")
    try {
      stringBuilder.append(Instructions.instructions.head.execute())
    } catch {
      // If we get a stack overflow error, it probably means
      // some label was recursively calling itself directly or indirectly roughly 65k times
      // it means that the program is looping
      case e: StackOverflowError =>
        stringBuilder.append("Infinite loop during execution.\n")
        Dialog.showMessage(message = s"Infinite loop in execution", messageType = Dialog.Message.Warning)
    }
    stringBuilder.append("Final register state:\n")
    stringBuilder.append(RegisterMachine.regMachine.mkString(","))
    val textArea = new TextArea() {
      text = stringBuilder.toString()
      editable = false
    }
    val dialog = new Dialog() {
      title = "Simulation Output"
      centerOnScreen()
      contents = new ScrollPane(textArea)
    }
    dialog.open()
  }
}
