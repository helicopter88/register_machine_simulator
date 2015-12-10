package register_simulator

import org.antlr.v4.runtime.tree._


/**
  * Created by domenico on 10/12/15.
  */
class RegisterVisitor[T] extends RegisterSimulatorVisitor[T] {
  override def visitProgram(ctx: RegisterSimulatorParser.ProgramContext): T = ???

  override def visitInstruction(ctx: RegisterSimulatorParser.InstructionContext): T = ???

  override def visitTerminal(terminalNode: TerminalNode): T = ???

  override def visitChildren(ruleNode: RuleNode): T = ???

  override def visitErrorNode(errorNode: ErrorNode): T = ???

  override def visit(parseTree: ParseTree): T = ???
}
