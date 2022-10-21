import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         try {
                Indexing.IndexDocs();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        while (true) {
            try {
                Searching.Search();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.print("q se vuoi uscire se no qualsiasi lettera: ");
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            if(input.equals("q")){
                break;
            }
        }

    }
}