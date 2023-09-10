public class App {
    public static void main(String[] args) throws Exception {
        Load load = new Load();
        try{
            load.LOAD("input.txt", "output.txt");
            load.printMemory();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
