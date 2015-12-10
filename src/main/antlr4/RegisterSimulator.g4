grammar RegisterSimulator;

@header {
    package register_simulator;
}

program: (instruction)* EOF;
instruction: LABEL':' REGISTER '+' LABEL |         # Increment => IncrementNode
             LABEL':' REGISTER '-' LABEL','LABEL | # Decrement => DecrementNode
             LABEL':' HALT                         # Halt      => HaltNode
             ;

fragment DIGIT: [0-9];
fragment NUMBER: DIGIT+;
REGISTER: 'R'NUMBER;
LABEL:   'L'NUMBER;
HALT: 'HALT';