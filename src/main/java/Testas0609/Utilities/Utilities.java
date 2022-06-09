package Testas0609.Utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utilities {

    public static String stringNotEmpty(String tekstas, Scanner sc){
        String input;
        do{
            System.out.println(tekstas);
            input = sc.nextLine().trim();
        } while (input.equals(""));
        return input;
    }
    public static double notEmptyDouble(String tekstas, Scanner sc) {
        double input = 0;
        for(;;) {
            try {
                System.out.println(tekstas);
                return input = Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Bloga įvestis");
            }
        }
    }
}
