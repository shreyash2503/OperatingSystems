# Multiprogramming Operating Systems

### This is a repository which implements Multiprogramming Operating Systems in Java

### There are two types of instructions in this project :-

- #### CPU Instructions
  - These instructions are executed by the CPU in both the phases
  - _Block Instructions_ (Executed on a block of memory, here block size is 10)
    - GD :- Get Data (Eg. GD10 :- Get Data from **input.txt** and put it in 10th Memory Location)
    - PD :- Put Data (Eg. PD10 :- Get Data from 10th Memory Location and put it in **output.txt**)
  - _Non-Block Instructions_ (Executed on a single memory location)
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
    - This file will produce the output of all these instructions along with the error that occur during their execution
  - #### Below is overall flow of the project :-
    [Link to PDF](./pdfs/phase1.pdf)

- #### Phase 2:

  - ##### This phase implements the following :-

    - ##### Memory Management
      - The memory allocation is dynamic in this part of the project
      - This phase makes use of a **2D** charcter array of 300x4 to load data from input file
      - Memory is allocated randomly in this phase and the page table is used to keep track of the memory location
    - #### Mode Switching
      - Mode switching is mimicked using MasterMode function and called when READ, WRITE, TERMINATE operations are performed
      - SI is used to raise interrupt which in turns causes the mode switching
    - #### Error Handling
      - Six different errors are handled in this phase
      - Out of Data Error :- Occurs when GD is called and there is no data left to be read from input.txt
      - Line Limit Error :- Occurs when the number of lines in output.txt exceeds the limit specified in the control card
      - Time Limit Error :- Occurs when the number of instructions specified in the control card is less than the number of instructions executed by the CPU
      - Operation Code Error :- Occurs when the operation code is not valid
      - Operand Error :- Occurs when the operand is not valid
      - Page Fault Error :- Occurs when the page is not present in the memory
    - #### Paging
      - Paging is implemented in this part of the program
      - As soon as $AMJ is encountered page table is created at random memoory location
      - The page table contains the Virtual address to a particular page in the memory
      - A single row of page table is 4 bit long and first bit indicate if a virtual address is allocated to that memory location or not (0 or 1) and last 2 bit indicate the page number or Virtual address
    - #### Interrupts
      - There are 3 types of interrupts in this phase :-
      - SI :- Divided into three parts :-
        - SI=1 :- Read Interrupt
        - SI=2 :- Write Interrupt
        - SI=3 :- Terminate Interrupt
        - This interrupt is used to indicate mode switching
      - TI :- Timer Interrupt
        - TI=2 :- Occurs when Time limit is exceeded
      - PI :- Program Interrupt and is divided into three parts :-
        - PI=1 :- Operation Code Error
        - PI=2 :- Operand Error
        - PI=3 :- Page Fault
        - **Page Fault** is divided is further divided into 2 parts :-
          - **Valid Page Fault** :- Occurs when page is not present in the memory due to instructions like GD and SR
          - **Invalid Page Fault** :- Occurs when page is not present in the memory due to instructions like LR, CR, BT
      - Also a combinations of these interrupts can occur, SI and TI can occur together and TI and PI can occur together, but SI and PI cannot occur together (Refer to PDF for more details)
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
  - #### Below is overall flow of the project :-
    [Link to PDF](./pdfs/phase2.pdf)

## How to run the project

    - Phase 1
        javac Main.java
        java Main
    - Phase 2
        Navigate to src
        javac App.java
        java App
    __After running the project you will see the output in output.txt file__
