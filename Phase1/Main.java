import java.io.IOException;

public class Main{
    public static void main(String [] args){
        Load load = new Load();
        try {
            load.LOAD("input.txt", "output.txt");
            load.printMemory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }
}