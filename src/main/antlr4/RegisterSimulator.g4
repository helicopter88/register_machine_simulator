grammar RegisterSimulator;

@header {
    package register_simulator;
}

program: (instruction)* EOF;
instruction: LABEL':' REGISTER '+' LABEL          #Increment
             | LABEL':' REGISTER '-' LABEL','LABEL  #Decrement
             | LABEL':' HALT                         #Halt
             ;

fragment DIGIT: [0-9];
fragment NUMBER: DIGIT+;
REGISTER: 'R'NUMBER;
LABEL:   'L'NUMBER;
HALT: 'HALT';