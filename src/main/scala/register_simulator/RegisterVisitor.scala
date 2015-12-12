package register_simulator

import org.antlr.v4.runtime.tree._
import register_simulator.RegisterSimulatorParser.{ProgramContext, IncrementContext, DecrementContext, HaltContext}
import register_simulator.nodes.{DecrementNode, HaltNode, IncrementNode, Instruction}
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class RegisterVisitor extends RegisterSimulatorVisitor[mutable.Buffer[Instruction]] {
  private def generateInt(terminalNode: TerminalNode): Int = {
    Integer.parseInt(terminalNode.getText.substring(1))
  }

  override def visitProgram(ctx: RegisterSimulatorParser.ProgramContext): mutable.Buffer[Instruction] = {
    ctx.instruction().flatMap(elem => visit(elem))
  }

  override def visitHalt(ctx: RegisterSimulatorParser.HaltContext):  mutable.Buffer[Instruction] = {
    mutable.Buffer[Instruction](new HaltNode)
  }

  override def visitIncrement(ctx: RegisterSimulatorParser.IncrementContext): mutable.Buffer[Instruction] = {
    mutable.Buffer[Instruction](new IncrementNode(new Register(generateInt(ctx.REGISTER())),
      new Label(generateInt(ctx.LABEL(1)))))
  }

  override def visitDecrement(ctx: RegisterSimulatorParser.DecrementContext): mutable.Buffer[Instruction] = {
    mutable.Buffer[Instruction](new DecrementNode(new Register(generateInt(ctx.REGISTER())),
      new Label(generateInt(ctx.LABEL(1))), new Label(generateInt(ctx.LABEL(2)))))
  }

  override def visitTerminal(terminalNode: TerminalNode) = null

  override def visitChildren(ruleNode: RuleNode):  mutable.Buffer[Instruction] = null

  override def visitErrorNode(errorNode: ErrorNode):  mutable.Buffer[Instruction] = null

  override def visit(parseTree: ParseTree):  mutable.Buffer[Instruction] = {
    val buffer = new ArrayBuffer[Instruction]()
    parseTree match {
      case node: ProgramContext => buffer ++= visitProgram(node)
      case node: HaltContext => buffer ++= visitHalt(node)
      case node: DecrementContext => buffer ++= visitDecrement(node)
      case node: IncrementContext => buffer ++= visitIncrement(node)
    }
  }
}
