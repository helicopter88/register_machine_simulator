grammar RegisterSimulator;

@header {
    package register_simulator;
}

program: (instruction';')* EOF;

// Examples:
// L0: R0+ -> L1;
// L1: R0- -> L1, L2;
// L2: HALT;

instruction:   LABEL':' REGISTER '+' '->' LABEL          #Increment
             | LABEL':' REGISTER '-' '->' LABEL','LABEL  #Decrement
             | LABEL':' HALT                             #Halt
             ;

fragment DIGIT: [0-9];
fragment NUMBER: DIGIT+;
REGISTER: 'R'NUMBER;
LABEL:   'L'NUMBER;
HALT: 'HALT';
WS: [ \n\t\r]+ -> skip;