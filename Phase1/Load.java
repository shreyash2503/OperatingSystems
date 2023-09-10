import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class Load {
    char [][] memory;
    int m ;
    char [] R;
    char [] IR;
    int IC;
    int T;
    int SI;
    BufferedReader reader;
    // BufferedWriter writer;
    FileWriter writer;
    int pcflag;
    int dcflag;
    char[] buffer;


    public Load(){
        this.R = new char[4];
        this.IR = new char[4];
        this.pcflag = 0;
        this.dcflag = 0;
        this.memory = new char[100][4];
        this.buffer = new char[40];
    }

    public void init(){
        this.m = 0;
        for(int i = 0; i < 100; i++){
            for(int j = 0; j < 4; j++){
                memory[i][j] = '0';
            }
        }
    }
    public void LOAD(String filename1, String filename2) throws IOException{
        try{
            reader = new BufferedReader(new FileReader(filename1));
            writer = new FileWriter(filename2);
            // writer = new BufferedWriter(wr);


            String line = null;
            String subString = null;
            while((line = reader.readLine()) != null){
                System.out.println(line);
                if(line.length() >= 4){
                    subString = line.substring(0, 4);
                } else {
                    subString = line;
                }

                if(subString.equals("$END")){
                    continue;
                }

                if(pcflag == 1) {
                    if(subString.equals("$DTA")){
                        pcflag = 0;
                        this.IC = 0;
                        EXECUTE();
                        continue;
                    }

                    String code = line;
                    // System.out.println(code);


                    if(code.length() < 4){
                        for(int i=0;i<code.length();i++){
                            memory[m][i] = code.charAt(i);
                        }
                        int n = m % 10;
                        if(n != 0){
                            m = m + (10 - n);
                        }
                        continue;
                    }

                    int size = 0;

                    while(size + 4 <= code.length()){
                       String sub = code.substring(size, size + 4);

                       for(int i=0;i<4;i++){
                            memory[m][i] = sub.charAt(i);
                       }
                        m ++;
                        size += 4;
                    }
                    int n = m % 10;
                    if(n != 0){
                        m = m + (10 - n);
                    }
                    // break;
                    continue;
                }


                if(subString.equals("$AMJ") && pcflag == 0 && dcflag == 0){
                    pcflag = 1;
                    init();
                    continue;
                }


                
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }


    public void EXECUTE() throws IOException{
        while(1 < 2){
            if(IC == 100){
                break;
            }
            IR[0] = memory[IC][0];
            IR[1] = memory[IC][1];
            IR[2] = memory[IC][2];
            IR[3] = memory[IC][3];
            IC ++;

            if(IR[0] == 'L' && IR[1] == 'R'){
                String line = new String(IR);
                int num = Integer.parseInt(line.substring(2));
                for(int i=0;i<4;i++){
                    R[i] = memory[num][i];
                }
            } else if(IR[0] == 'S' && IR[1] == 'R'){
                String line = new String(IR);
                int num = Integer.parseInt(line.substring(2));
                for(int i=0;i<4;i++){
                    memory[num][i] = R[i];
                }
            } else if(IR[0] == 'C' && IR[1] == 'R'){
                String line = new String(IR);
                int num = Integer.parseInt(line.substring(2));
                if(memory[num][0] == R[0] && memory[num][1] == R[1] && memory[num][2] == R[2] && memory[num][3] == R[3]){
                    T = 1;
                } else {
                    T = 0;
                }
            } else if(IR[0] == 'B' && IR[1] == 'T'){
                if(T == 1){
                    String line = new String(IR);
                    int num = Integer.parseInt(line.substring(2));
                    IC = num;
                    T = 0;
                }
            } else if(IR[0] == 'G' && IR[1] == 'D'){
                SI = 1;
                MASTERMODE();
            } else if(IR[0] == 'P' && IR[1] == 'D'){
                SI = 2;
                MASTERMODE();
            } else if(IR[0] == 'H'){
                SI = 3;
                MASTERMODE();
                break;
            }
        }

    }

    public void MASTERMODE() throws IOException{
        int i = this.SI;
        if(i == 1){
            READ();
        } else if(i == 2){
            WRITE();
        } else if(i == 3){
            TERMINATE();

        }
        SI = 0;

    }

    public void TERMINATE() throws IOException{
        System.out.println("End card detected");
        writer.write("\n\n");
        printMemory();

    }

    public void READ(){
        IR[3] = '0';

        String line = new String(IR);

        int num = Integer.parseInt(line.substring(2));

        try{
            line = reader.readLine();

        } catch(IOException e){
            e.printStackTrace();
            
        }
        buffer = line.toCharArray();
        for(int i=0;i<line.length();){
            memory[num][i % 4] = buffer[i];
            i++;
            if(i % 4 == 0){
                num++;
            }
        }


    }
    public void WRITE(){

        IR[3] = '0';
        String line = new String(IR);
        int num = Integer.parseInt(line.substring(2));
        String s;
        String total = "";

        for(int i=0;i<10;i++){
            s = new String(memory[num + i]);
            String t = "";
            for(int j=0;j<4;j++){
                if(s.charAt(j) != '0'){
                    t = t + s.charAt(j);
                }
                else {
                    break;
                }
            }
            total = total.concat(t);
        }
        System.out.println("----------------------In writing mode----------------");
        try{
            writer.write(total);
            writer.write("\n");
            writer.flush();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public void printMemory(){
        for(int i=0;i<100;i++){
            for(int j=0;j<4;j++){
                System.out.print(memory[i][j] + " ");
            }
            System.out.println();
        }
    }
}
