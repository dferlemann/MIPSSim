instr 00400000,2
#2008000a #addi $8, $0, 10
#2009fff1 #addi $9, $0, -15
#200a0001 #addi $10,$0, 1
#010a4022 #sub $8, $8, $10
#012a4820 #add $9, $10, $9
#1500fffd #bne $8, $0, -3
#3c0100fa #lui $1, 0x000000fa
#3421000a #ori $1, $1, 0x0000000a
#00014021 #addu $8, $0, $1
#2528fffb #addiu $8, $9, -5

# lw test:
#3c011001 # first: lui $1, 0x1001
#8c240000 # next:  lw $4, ($1)

# lh test: 
#3c011001 # first: lui $1, 0x1001
#84240000 # next:  lhu $4, ($1)

# lhu test: 
#3c011001 # first: lui $1, 0x1001
#94240000 # next:  lhu $4, ($1)

# lbu test: 
#3c011001 # first: lui $1, 0x1001
#90240000 # next:  lbu $4, ($1)

# lb test: 
3c011001 # first: lui $1, 0x1001
80240000 # next:  lb $4, ($1)

data 10010000, 17
8f,0a,1d,2c,
00,00,00,00,
23,00,00,00,
00,00,00,
00,
37

start 00400000
