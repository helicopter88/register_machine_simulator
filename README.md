# Register Machine Simulator
A simulator for certain register machines written in Scala
It uses Swing for the (very simple) UI

# Instructions
* with an encoded program:
  Just input the number inside the input field and press simulate
  You will get back a dialog with the decoded instructions and the execution output

* with a list of instructions:
 The syntax for an instruction is:
```L#: INSTRUCTION;```
The supported instructions are:
*```R#+ -> L#``` that will incremement the register # and then go to the specified label
*```R#- -> L#, L#``` that will try to decrement the register #, if successful jump to the first label specified, else to the second one
*```HALT``` that will halt the execution of the program

