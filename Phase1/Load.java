import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class Load {
    char [][] memory;
    int m ;
    public Load(){
        this.m = 0;
        this.memory = new char[100][4];
        for(int i = 0; i < 100; i++){
            for(int j = 0; j < 4; j++){
                memory[i][j] = '0';
            }
        }
    }
    public void LOAD(String filename) throws IOException{
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            String subString = null;
            int pcflag = 0;
            int dcflag = 0;
            while((line = reader.readLine()) != null){
                if(line.length() >= 4){
                    subString = line.substring(0, 4);
                } else {
                    subString = line;
                }

                if(pcflag == 1) {
                    if(subString.equals("$DTA")){
                        pcflag = 0;
                        dcflag = 1;
                        continue;
                    }
                    String code = line;
                    System.out.println(code);


                    if(code.length() < 4){
                        for(int i=0;i<code.length();i++){
                            memory[m][i] = code.charAt(i);
                            continue;
                        }
                    }

                    int size = 0;
                    System.out.println("M = " + m);

                    while(size + 4 <= code.length()){
                       String sub = code.substring(size, size + 4);

                       for(int i=0;i<4;i++){
                            memory[m][i] = sub.charAt(i);
                       }
                        m ++;
                        size += 4;
                    }
                    System.out.println("M(after) = " + m);
                    int n = m % 10;
                    if(n != 0){
                        m = m + (10 - n);
                    }
                    // break;
                    continue;
                }


                if(subString.equals("$AMJ") && pcflag == 0 && dcflag == 0){
                    pcflag = 1;
                    continue;
                }


                if(dcflag == 1){
                    if(subString.equals("$END")){
                        dcflag = 0;
                        pcflag = 0;
                        // continue; //-> Actually continue will be called
                        break;
                    }
                    System.out.println("$DTA Encountered");
                    System.out.println("MOS, Start Execution Function");
                }
            }

        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void printMemory(){
        for(int i=0;i<30;i++){
            for(int j=0;j<4;j++){
                System.out.print(memory[i][j] + " ");
            }
            System.out.println();
        }
       

    }
    
}
