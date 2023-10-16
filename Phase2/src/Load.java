import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Load {

    char [][] memory;
    char [] IR;
    int IC;
    char [] R;
    int C;
    int PTR;
    int SI;
    int PI;
    int TI;
    char [] buffer;
    BufferedReader reader;
    FileWriter writer;
    Set<Integer> checkRandomNumber;
    List<PCB> pcbList;
    PCB pcb;
    boolean pageFault;
    int curr;
    String[] errors = {"NO ERROR", "OUT OF DATA", "LINE LIMIT EXCEEDED", "TIME LIMIT EXCEEDED", "OPERATION CODE ERROR", "OPERAND ERROR", "INVALID PAGE FAULT", "TIME LIMIT EXCEEDED WITH OPERATION ERROR", "TIME LIMIT EXCEEDED WITH OPERAND ERROR"};
    boolean flag;
    boolean reachedH;
    boolean reachedEnd;

    /**
     * 
     */
    public Load(){
        this.memory = new char[300][4];
        this.IR = new char[4];
        this.R = new char [4];
        this.buffer = new char[40];
        this.checkRandomNumber = new HashSet<>();
        this.pcbList = new ArrayList<>();
        this.curr = -1;
        this.flag = false;
        this.reachedH = false;
        this.reachedEnd = false;
    }

    public void init(){
        this.C = 0;
        this.SI = 0;
        this.PI = 0;
        this.TI = 0;
        this.curr = -1;
        this.flag = false;
        this.reachedH = false;
        this.reachedEnd = false;

        for(int i = 0; i < 300; i++){
            for(int j = 0; j < 4; j++){
                memory[i][j] = '_';
            }
        }

        for (int i = 0; i < 4; i++){
            IR[i] = '\u0000';
            R[i] = '\u0000';
        }

        // for(int i = 0; i < 40; i++){
        //     buffer[i] = '_';
        // }
        this.buffer = new char[40];
        this.pcb = new PCB(null, -1, -1, -1, -1);

        this.checkRandomNumber.clear();
        this.pcbList.clear();

    }
    public void initializePT(int ptr){
        for(int i=ptr;i<ptr+10;i++){
            for(int j=0;j<4;j++){
                if(j == 0){
                    memory[i][j] = '0';
                } else {

                    memory[i][j] = '*';
                }
            }
        }
    }



    public void LOAD(String fileName1, String fileName2) throws IOException{
        try{
            reader = new BufferedReader(new FileReader(fileName1));
            writer = new FileWriter(fileName2);
            String line = null;
            String subString = null;
            
            while((line = reader.readLine()) != null){
                if(line.length() >= 4){
                    subString = line.substring(0, 4);
                } else {
                    subString = line;
                }
                if(subString.equals("$AMJ")){
                    init();
                    int TTL;
                    int TLL;
                    String jOBID;
                    PTR = ALLOCATE() * 10;   // ! Generate a random number to store the page table
                    curr = PTR;
                    jOBID = line.substring(4, 8);
                    TTL = Integer.parseInt(line.substring(8, 12));
                    TLL = Integer.parseInt(line.substring(12, 16));

                    System.out.println("jOBID: " + jOBID);
                    System.out.println("TTL: " + TTL);
                    System.out.println("TLL: " + TLL);
                    System.out.println("PTR: " + PTR);
                    System.out.print("\n\n");
                    initializePT(PTR);

                    pcb = new PCB(jOBID, TTL, TLL, 0, 0);
                    continue;
                }
                if(subString.equals("$DTA")){
                    STARTEXECUTION();
                    // break;
                    continue;
                }
                if(subString.equals("$END")){
                    continue;
                }
                else {
                    if(flag){
                        continue;

                    }
                    int m = ALLOCATE();
                    String frameNo = String.valueOf(m);
                    if((m / 10.0) < 1){
                        frameNo = "0" + frameNo;
                    }
                    // System.out.println(frameNo);
                    memory[curr][0] = '1';
                    memory[curr][2] = frameNo.charAt(0);
                    memory[curr][3] = frameNo.charAt(1);
                    curr = curr + 1;

                    buffer = line.toCharArray();
                    int ra = m * 10;
                    for(int i=0;i<line.length();){
                        memory[ra][i % 4] = buffer[i];
                        i++;
                        if(i % 4 == 0){
                            ra++;
                        }

                    }
               }
                

            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int ALLOCATE(){
        Random random = new Random();
        int randomNumber = random.nextInt(30);
        if(checkRandomNumber.size() == 0){
            checkRandomNumber.add(randomNumber);
            return randomNumber;
        } 
        while(checkRandomNumber.contains(randomNumber)){
            randomNumber = random.nextInt(30);
        }
        checkRandomNumber.add(randomNumber);
        return randomNumber;
       

    }

    public void printMemory(){
        for(int i = 0; i < 300; i++){
            System.out.print(i + " ");
            for(int j = 0; j < 4; j++){
                System.out.print(memory[i][j] + " | ");
            }
            System.out.println();
            System.out.println();
        }
    }


    int ADDRESSMAP(int VA){
        if(0 <= VA && VA < 100){
            int pt = PTR + VA / 10;
            if(memory[pt][0] == '0'){
                // System.out.println("This is a call from addressmap");
                PI = 3;
                return -1;
            }
            pt = Integer.parseInt(String.valueOf(memory[pt][2]) + String.valueOf(memory[pt][3])) * 10 + VA % 10;
            return pt;
        }
        PI = 2;
        return -1;
    }

    void STARTEXECUTION(){
        this.IC = 0;
        EXECUTEUSERPROGRAM();

       

    }


    
    void WRITE(int RA){
        if(pcb.LLC + 1 > pcb.TLL){
            TI = 2;
            TERMINATE(2);
            flag = true;
            return;

        } else {
            String total = "";
            String s = "";


            for(int i=0;i<10;i++){
                s = new String(memory[RA + i]);
                String t = "";
                for(int j=0;j<4;j++){
                    if(s.charAt(j) != '_'){

                        t += s.charAt(j);
                    }
                }
                total = total.concat(t);
            }
            // System.out.println("This is t: " + total);
            System.out.println("----------------------In writing mode----------------");
            try{
                writer.write(total);
                writer.write("\n");
                writer.flush();
                pcb.LLC++;
            } catch(IOException e){
                e.printStackTrace();
            }


        }
        SI = 0;

    }
    void TERMINATE(int EM){
        try {
            writer.write("\n");
            if(EM == 0 ){
                // writer.write("Job ID: " + pcb.jOBID + "\n");
                // writer.write("TLL: " + pcb.TLL + "\n");
                // writer.write("TLC" + pcb.LLC + "\n");
                // writer.write("ERROR: " + errors[EM] + "\n");
                
                writer.write("Job ID: " + pcb.jOBID + "\n");
                writer.write("IC:  " + IC + "\n");
                writer.write("IR:  " + String.valueOf(IR) + "\n");
                writer.write("TTL: " + pcb.TTL + "\n");
                writer.write("TTC:  " + pcb.TTC + "\n");
                writer.write("TLL: " + pcb.TLL + "\n");
                writer.write("LLC:  " + pcb.LLC + "\n");
                writer.write("NO ERROR" + "\n");

            } else {
                writer.write("Job ID: " + pcb.jOBID + "\n");
                // writer.write("TLL: " + pcb.TLL + "\n");
                // writer.write("TLC" + pcb.LLC + "\n");
                writer.write("ERROR: " + errors[EM] + "\n");
                writer.write("IC:  " + IC + "\n");
                writer.write("IR:  " + String.valueOf(IR) + "\n");
                writer.write("TTL: " + pcb.TTL + "\n");
                writer.write("TTC:  " + pcb.TTC + "\n");

                writer.write("TLL:  " + pcb.TLL + "\n");
                writer.write("LLC:  " + pcb.LLC + "\n");
                writer.write("\n");
                writer.write("\n");
                SI = 0;
                TI = 0;
                PI = 0;
            }
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }
    void READ(int RA){
        try {
            String line = reader.readLine();
            if(line.contains("$END")){
                reachedEnd = true;
                TERMINATE(1);
                return;
            } else {
                // System.out.println(line);
                buffer = line.toCharArray();
                for(int i=0;i<line.length();){
                    memory[RA][i % 4] = buffer[i];
                    i++;
                    if(i % 4 == 0){
                        RA++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 

        SI = 0;



    }

    void MASTERMODE(int RA){
        if(TI == 0 && PI == 0 && SI != 0){
            // System.out.println("Hello I occuredmox" + 1);
            switch(SI){
                case 1:
                    SI = 0;
                    READ(RA);

                    return;
                case 2:
                    SI = 0;
                    WRITE(RA);
                    return;
                case 3:
                    SI = 0;
                    TERMINATE(0);
                    flag = true;
                    return;
            }
        }else if(TI == 2 && PI == 0 && SI != 0){
            // System.out.println("Hello I occuredmox" + 2);
            switch(SI){
                case 1:
                    SI = 0;
                    TERMINATE(1); /// 3
                    flag = true;
                    return;
                case 2:
                    SI = 0;
                    WRITE(RA);
                    // if(!flag){
                        TERMINATE(3);
                    // }
                    return;
                case 3:
                    SI = 0;
                    TERMINATE(0);
                    flag = true;
                    return;
            }

            // SI = 0;
        } else if(TI == 0 && PI != 0){
            // System.out.println("Hello I occuredmox" + 3 + " " + PI);

            switch(PI){
                case 1:
                    // System.out.println("Shreyash1");
                    TERMINATE(4);
                    flag = true;
                    return;
                case 2:
                    // System.out.println("Shreyash2");
                    TERMINATE(5);
                    flag = true;
                    return;
                case 3:
                    // System.out.println("Shreyash3");
                    // System.out.println("Hello I occuredmox" + 3);

                    // PI = 0;
                    if((IR[0] == 'G' && IR[1] == 'D') || (IR[0] == 'S' && IR[1] == 'R')){
                        System.out.println("Valid Page fault occurred");
                        pageFault = true;
                        int m = ALLOCATE();
                        String t = String.valueOf(m);
                        if(m / 10.0  < 1){
                            t = "0" + t;

                        } 
                        memory[curr][0] = '1';
                        memory[curr][2] = t.charAt(0);
                        memory[curr][3] = t.charAt(1);

                        curr++;

                        // if(pcb.TTC + 1 > pcb.TTL){
                        //     TI = 2;
                        //     PI = 3;
                        //     MASTERMODE(0);
                        //     break;
                        // }
                        PI = 0;
                    } else if((IR[0] == 'P' && IR[1] == 'D') || (IR[0] == 'L' && IR[1] == 'R') || (IR[0] == 'C' && IR[1] == 'R') || (IR[0] == 'B' && IR[1] == 'T')){
                        // PI = 0;
                        TERMINATE(6);
                        flag = true;
                        // if(pcb.TTC + 1 > pcb.TTL){
                        //     TI = 2;
                        //    PI = 3;
                        //     MASTERMODE(0);
                        //     break;
                        // }
                    } 
                    // {
                    //     PI = 1;
                    //     MASTERMODE(0);
                    //     break;
                    // }
                    return;


            }
            // PI = 0;
        } else if(TI == 2 && PI != 0) {
            // System.out.println("Hello I occured" + 4);
            switch(PI){
                case 1:
                    TERMINATE(7);
                    flag = true;
                    return;
                case 2:
                    TERMINATE(8);
                    flag = true;
                    return;
                
            }

        }
        if(TI == 2 && PI == 3){
            TERMINATE(3);
        } else if(TI == 2 && SI == 0){
            TERMINATE(3);
        }

        PI = 0;
        SI = 0;

    }

    void SIMULATION(){
        pcb.TTC ++;
        if(pcb.TTC > pcb.TTL){
            TI = 2;
        }
        
    }

    void EXECUTEUSERPROGRAM(){
        // System.out.println("execute");
         while(true){
            // System.out.println("execute");
            if(flag){
                break;
            }
            if(reachedEnd){
                break;
            }
            int pt = ADDRESSMAP(IC);
            // if(PI != 0){
            //     System.out.println("Hello I occured" + 1);
            //     MASTERMODE(0);
            //     if(pageFault){
            //         System.out.println("Hello I occured" + 1);
            //         continue;
            //     }
            //     break;
            // }
            // System.out.println("This is it" + new String(memory[pt]));
            IR[0] = memory[pt][0];
            IR[1] = memory[pt][1];
            IR[2] = memory[pt][2];
            IR[3] = memory[pt][3];
            IC++;
            // System.out.println("Value of IR is ::" + String.valueOf(IR));
            if(IR[0] != 'H'){
                    for(int i=0;i<2;i++){
                        if(!(IR[i + 2] > 47 && IR[i + 2] < 58)){
                            PI = 2;
                            break;
                        }
                    }
            }
            
            if(pcb.TTC + 1 > pcb.TTL && PI == 2){
                TI = 2;
                MASTERMODE(0);
                return;
            }
           
            // if(PI != 0){
            //     System.out.println("Hello I occured" + 2);
            //     MASTERMODE(0);
            //     break;
            // }
            int ra = -1;
            if(IR[0] != 'H' && PI == 0){
                ra = ADDRESSMAP(Integer.parseInt(String.valueOf(IR[2]) + String.valueOf(IR[3])));

            }
            // System.out.println("The real address is ::" + ra);
            if(PI != 0){
                MASTERMODE(0);
                if(pageFault){
                    // System.out.println("Hello I occured" + 3);
                    IC --;
                    pageFault = false;
                    PI = 0;
                    continue;
                }
                break;
            }
            
            if(IR[0] == 'G' && IR[1] == 'D'){
                SI = 1;
                MASTERMODE(ra); 
                SIMULATION();
            } else if(IR[0] == 'P' && IR[1] == 'D'){
                SI = 2;
                MASTERMODE(ra);
                SIMULATION();
            } else if(IR[0] == 'H'){
                SI = 3;
                MASTERMODE(ra);
                SIMULATION();
                reachedH = true;
                break;
            } else if(IR[0] == 'L' && IR[1] == 'R'){
                for(int i = 0; i < 4; i++){
                    R[i] = memory[ra][i];
                }
                SIMULATION();
            } else if(IR[0] == 'S' && IR[1] == 'R'){
                for(int i = 0; i < 4; i++){
                    memory[ra][i] = R[i];
                }
                SIMULATION();
            } else if(IR[0] == 'C' && IR[1] == 'R'){
                if(memory[ra][0] == R[0] && memory[ra][1] == R[1] && memory[ra][2] == R[2] && memory[ra][3] == R[3]){
                    C = 1;
                } else {
                    C = 0;
                }
                SIMULATION();
            } else if(IR[0] == 'B' && IR[1] == 'T'){
                if(C == 1){
                    IC = Integer.parseInt(String.valueOf(IR[2]) + String.valueOf(IR[3]));
                    C = 0;
                }
                SIMULATION();
            } else {
                PI = 1;
                // System.out.println("Hello I occured ytladf;ksl;as");
                
                SIMULATION();
                MASTERMODE(0);
                return;
            }
            
            if(reachedH){
                reachedH = true;
                return;
            }
           

           

            for(int i=0;i<4;i++){
                IR[i] = '\u0000';
            }
            
        }


    }
    
    
}
