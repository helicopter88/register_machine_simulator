package register_simulator

import org.antlr.v4.runtime.tree._
import register_simulator.RegisterSimulatorParser.{ProgramContext, IncrementContext, DecrementContext, HaltContext}
import register_simulator.nodes.{DecrementNode, HaltNode, IncrementNode, Instruction}
import scala.collection.JavaConversions._
import scala.collection.mutable


/**
  * Created by domenico on 10/12/15.
  */
class RegisterVisitor extends RegisterSimulatorVisitor[Unit] {
  private def generateInt(terminalNode: TerminalNode): Int = {
    Integer.parseInt(terminalNode.getText.substring(1))
  }

  val labels = new mutable.HashMap[Label, Instruction]

  override def visitProgram(ctx: RegisterSimulatorParser.ProgramContext): Unit = {
    ctx.instruction().foreach(elem => visit(elem))
  }

  override def visitHalt(ctx: RegisterSimulatorParser.HaltContext): Unit = {
    labels.put(new Label(generateInt(ctx.LABEL())), new HaltNode)
  }

  override def visitIncrement(ctx: RegisterSimulatorParser.IncrementContext): Unit = {
    labels.put(new Label(generateInt(ctx.LABEL(0))), new IncrementNode(new Register(generateInt(ctx.REGISTER())),
      new Label(generateInt(ctx.LABEL(1)))))
  }

  override def visitDecrement(ctx: RegisterSimulatorParser.DecrementContext): Unit = {
    labels.put(new Label(generateInt(ctx.LABEL(0))), new DecrementNode(new Register(generateInt(ctx.REGISTER())),
      new Label(generateInt(ctx.LABEL(1))), new Label(generateInt(ctx.LABEL(2)))))
  }

  override def visitTerminal(terminalNode: TerminalNode): Unit = ???

  override def visitChildren(ruleNode: RuleNode): Unit = ???

  override def visitErrorNode(errorNode: ErrorNode): Unit = ???

  override def visit(parseTree: ParseTree): Unit = {
    parseTree match {
      case node: ProgramContext => visitProgram(node)
      case node: HaltContext => visitHalt(node)
      case node: DecrementContext => visitDecrement(node)
      case node: IncrementContext => visitIncrement(node)
    }
  }
}
