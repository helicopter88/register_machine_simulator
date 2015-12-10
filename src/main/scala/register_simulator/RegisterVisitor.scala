package register_simulator

import org.antlr.v4.runtime.tree._
import register_simulator.nodes.{DecrementNode, HaltNode, IncrementNode, Instruction}

import scala.collection.mutable.ArrayBuffer


/**
  * Created by domenico on 10/12/15.
  */
class RegisterVisitor extends RegisterSimulatorVisitor[Unit] {
  private def generateInt(terminalNode: TerminalNode): Int = {
    Integer.parseInt(terminalNode.getText.substring(1))
  }

  val labels = new ArrayBuffer[Label]

  override def visitProgram(ctx: RegisterSimulatorParser.ProgramContext): Unit = ???

  override def visitInstruction(ctx: RegisterSimulatorParser.InstructionContext): Unit = {
    val labelNumber = generateInt(ctx.LABEL(0))
    var instruction: Instruction = null
    if (ctx.HALT() != null) {
      instruction = new HaltNode
    } else {
      if (ctx.getText.contains("-")) instruction =
        new DecrementNode(new Register(generateInt(ctx.REGISTER())), new Label(generateInt(ctx.LABEL(1))),
          new Label(generateInt(ctx.LABEL(2))))
      if (ctx.getText.contains("+")) instruction =
        new IncrementNode(new Register(generateInt(ctx.REGISTER())), new Label(generateInt(ctx.LABEL(1))))

    }
  }

  override def visitTerminal(terminalNode: TerminalNode): Unit = ???

  override def visitChildren(ruleNode: RuleNode): Unit = ???

  override def visitErrorNode(errorNode: ErrorNode): Unit = ???

  override def visit(parseTree: ParseTree): Unit = ???
}
