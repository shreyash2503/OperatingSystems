# Multiprogramming Operating Systems

### This is a repository which implements Multiprogramming Operating Systems in Java

### There are two types of instructions in this project :-

- #### CPU Instructions
  - These instructions are executed by the CPU
  - These instructions are :-
  - _Block Instructions_
    - GD :- Get Data (Eg. GD10 :- Get Data from **input.txt** and put it in 10th Memory Location)
    - PD :- Put Data (Eg. PD10 :- Get Data from 10th Memory Location and put it in **output.txt**)
  - _Non-Block Instructions_
    - LR :- Load Register (Eg. LR10 :- Load the data from 10th Memory Location to Register)
    - SR :- Store Register (Eg. SR10 :- Store the data from Register to 10th Memory Location)
    - CR :- Compare Register (Eg. CR10 :- Compare the data from Register to 10th Memory Location)
    - BT :- Branch if True (Eg. BT10 :- Branch to 10th Memory Location if the data in Register is True)
      - Makes use of a bit called "C" if set to one BT will branch to the given location
    - H :- Halt

### The repository is divide into two parts :-

- #### Phase 1:
  - ##### This phase implements the following :-
    - ##### Memory Management
      - The memory allocation is sequential in this part of the project
      - This phase makes use of a **2D** charcter array of 100x4 to load data from input file
    - #### Mode Switching
      - Mode switching is mimicked using MasterMode function and called when READ, WRITE, TERMINATE operations are performed
      - SI is used to raise interrupt which in turns causes the mode switching
    - #### I/O Management
      - The I/O Management is implemented using two files inpput.txt and output.txt
      - The input.txt files contains the jobs to be executed by the CPU
  - #### Input.txt
    - The input.txt file contains various jobs to be executed by the CPU
    - Every job is dived into 3 parts
      - Control Card :- This contains the JOB ID (First 4 characters after _$AMJ_), total number of instructions in the Program Card (Next 4 digits), total number of lines in output (Next 4 digits)
      - Program Card :- This contains the instructions to be executed by the CPU
        - Every Program Card 7 types of instructions :-
          - GD, PD, LR, SR, CR, BT, H
    - Data Card :- This contains the data to be used by the CPU
  - #### Ouput.txt
    - This file will produce the output of all these instructions
    <iframe src="./pdfs/phase1.pdf" width="100%" height="500px"></iframe>
