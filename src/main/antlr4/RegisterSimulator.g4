grammar RegisterSimulator;

@header {
    package register_simulator;
}

program: (instruction)* EOF;
instruction: LABEL':' REGISTER '+' LABEL |
             LABEL':' REGISTER '-' LABEL','LABEL |
             LABEL':' HALT
             ;

fragment DIGIT: [0-9];
fragment NUMBER: DIGIT+;
REGISTER: 'R'NUMBER;
LABEL:   'L'NUMBER;
HALT: 'HALT';