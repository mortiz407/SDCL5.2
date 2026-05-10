/*Name: Manuel Ortiz
 *Date: 05/08/2026
 *Assignment: 5.2 Project: Software Design and Control Statements
 *Description:Calculator.java
 *This is the main class. Its only job is to start the program
 *and connect everything together.
 */

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {

        // Create a Scanner object to read user input
        Scanner input = new Scanner(System.in);

        // Start the menu and pass the Scanner object to it
        Menu.start(input);

        // Close the Scanner object to prevent resource leaks
        input.close();
    }
}