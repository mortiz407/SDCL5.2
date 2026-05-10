/*Name: Manuel Ortiz
 *Date: 05/08/2026
 *Assignment: 5.2 Project: Software Design and Control Statements
 *Description: Commands.java
 *This is where all the actual math happens.
 *Addition, subtraction, multiplication, and division all live here.
 *AVG, MAX, and MIN are also handled here.
 *Memory functions live in Memory.java
 */

import java.text.DecimalFormat;
import java.util.Scanner;

public class Commands {

    // Create a DecimalFormat object to format results to 4 decimal places and trim
    // trailing zeros
    static DecimalFormat df = new DecimalFormat("0.####");

    // This method reads what the user typed and figures out which math operation to
    // run
    // It first tries to calculate directly from an expression like 0 / 10
    // If that fails it falls back to the prompt-based calculator flow
    public static void handleExpression(String input, Scanner scanner) {

        String operatorInput = input.trim();

        if (tryInlineExpression(operatorInput, scanner)) {
            return;
        }

        // Check which operator the user typed and run the matching operation
        if (operatorInput.contains("+")) {
            addition(scanner);

        } else if (operatorInput.contains("*")) {
            multiplication(scanner);

        } else if (operatorInput.contains("/")) {
            division(scanner);

        } else if (operatorInput.contains("-")) {
            // Subtraction is checked last because negative numbers also have a dash
            subtraction(scanner);

        } else {
            // If no valid operator was found print an error
            System.out.println();
            System.out.println("=============================================================================");
            System.out.println("                   [ERROR] I could not understand that formula.              ");
            System.out.println("                   Try something like  1 + 2  or  1.5 / 2.5                  ");
            System.out.println("=============================================================================");
            System.out.println();
        }
    }

    // Tries to solve a two-number expression that was typed directly by the user
    // Examples: 0 / 10 or 5 + 2
    // This version keeps the logic simple so it is easier to learn
    public static boolean tryInlineExpression(String input, Scanner scanner) {
        String[] parts = input.split("\\s+");

        if (parts.length != 3) {
            return false;
        }

        String operator = parts[1];
        if (!operator.equals("+") && !operator.equals("-") && !operator.equals("*") && !operator.equals("/")) {
            return false;
        }

        double num1;
        double num2;

        try {
            num1 = Double.parseDouble(parts[0]);
        } catch (NumberFormatException e) {
            num1 = getValidNumber(" Enter the first number: ", scanner);
        }

        if (operator.equals("/")) {
            num2 = getValidDivisor(parts[2], scanner);
        } else {
            try {
                num2 = Double.parseDouble(parts[2]);
            } catch (NumberFormatException e) {
                num2 = getValidNumber(" Enter the second number: ", scanner);
            }
        }

        double result;

        if (operator.equals("+")) {
            result = num1 + num2;
        } else if (operator.equals("-")) {
            result = num1 - num2;
        } else if (operator.equals("*")) {
            result = num1 * num2;
        } else {
            result = num1 / num2;
        }

        displayResult(num1, operator, num2, result);
        return true;
    }

    // This helper method asks the user to enter a number
    // It keeps asking until the user enters a valid decimal number
    // try catch handles the case where the user types something that is not a
    // number
    public static double getValidNumber(String prompt, Scanner scanner) {

        // Keep looping until the user enters a valid number
        while (true) {
            System.out.print(prompt);

            try {
                // Try to read the input as a double
                double value = Double.parseDouble(scanner.nextLine().trim());

                // If we get here it worked so return the value
                return value;

            } catch (NumberFormatException e) {
                // This catch block runs if the user typed letters or something that is not a
                // number
                // NumberFormatException is the error Java throws when parseDouble fails
                System.out.println();
                System.out.println("=============================================================================");
                System.out.println("         [ERROR] That is not a valid number. Please enter a decimal         ");
                System.out.println("                 value like  5  or  10.25  and try again.                   ");
                System.out.println("=============================================================================");
                System.out.println();
            }
        }
    }

    // Asks the user for two numbers, adds them together, and displays the result
    public static void addition(Scanner scanner) {

        // Ask for two valid numbers using the helper method
        double num1 = getValidNumber(" Enter the first number: ", scanner);
        double num2 = getValidNumber(" Enter the second number: ", scanner);

        // Do the math and display the result
        double result = num1 + num2;
        displayResult(num1, "+", num2, result);
    }

    // Asks the user for two numbers, subtracts them, and displays the result
    public static void subtraction(Scanner scanner) {

        // Ask for two valid numbers using the helper method
        double num1 = getValidNumber(" Enter the first number: ", scanner);
        double num2 = getValidNumber(" Enter the second number: ", scanner);

        // Do the math and display the result
        double result = num1 - num2;
        displayResult(num1, "-", num2, result);
    }

    // Asks the user for two numbers, multiplies them, and displays the result
    public static void multiplication(Scanner scanner) {

        // Ask for two valid numbers using the helper method
        double num1 = getValidNumber(" Enter the first number: ", scanner);
        double num2 = getValidNumber(" Enter the second number: ", scanner);

        // Do the math and display the result
        double result = num1 * num2;
        displayResult(num1, "*", num2, result);
    }

    // Asks the user for two numbers and divides them
    // Has extra handling for division by zero on top of the normal number
    // validation
    public static void division(Scanner scanner) {

        // Ask for the first number using the helper method
        double num1 = getValidNumber(" Enter the first number: ", scanner);
        double num2 = getValidDivisor(null, scanner);

        // Do the math and display the result
        double result = num1 / num2;
        displayResult(num1, "/", num2, result);
    }

    // This helper is just for division because the second number cannot be zero
    public static double getValidDivisor(String firstAttempt, Scanner scanner) {
        String currentInput = firstAttempt;

        while (true) {
            try {
                if (currentInput == null) {
                    System.out.print(" Enter the second number: ");
                    currentInput = scanner.nextLine().trim();
                }

                double value = Double.parseDouble(currentInput);

                if (value == 0) {
                    System.out.println();
                    System.out.println("=============================================================================");
                    System.out.println("                   [ERROR] Division by zero is undefined.                    ");
                    System.out.println("            Even this calculator has limits. Try a different number.         ");
                    System.out.println("=============================================================================");
                    System.out.println();
                    currentInput = null;
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("=============================================================================");
                System.out.println("         [ERROR] That is not a valid number. Please enter a decimal         ");
                System.out.println("                 value like  5  or  10.25  and try again.                   ");
                System.out.println("=============================================================================");
                System.out.println();
                currentInput = null;
            }
        }
    }

    // This method handles AVG MAX and MIN commands
    // Example: user types AVG 10 20 30
    // This is called from Menu.java separately from handleExpression
    public static void handleMultiValue(String input) {

        // Convert to uppercase so avg Avg AVG all work the same
        String formatInput = input.trim().toUpperCase();

        // Split the input by spaces
        // "AVG 10 20 30" becomes ["AVG", "10", "20", "30"]
        String[] parts = formatInput.split(" ");

        // Make sure the user typed at least one number after the keyword
        if (parts.length < 2) {
            System.out.println();
            System.out.println(" [ERROR] You need to enter at least one number.");
            System.out.println(" Example:  AVG 10 20 30");
            System.out.println();
            return;
        }

        // Convert the numbers from strings into doubles and store them in an array
        // We start at index 1 to skip the keyword
        double[] arr = new double[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
            arr[i - 1] = Double.parseDouble(parts[i]);
        }

        // Call the right method based on what the user typed
        if (formatInput.startsWith("AVG")) {
            System.out.println();
            System.out.println(" Average: " + df.format(avgVal(arr)));
            System.out.println();

        } else if (formatInput.startsWith("MAX")) {
            System.out.println();
            System.out.println(" Maximum: " + df.format(maxVal(arr)));
            System.out.println();

        } else if (formatInput.startsWith("MIN")) {
            System.out.println();
            System.out.println(" Minimum: " + df.format(minVal(arr)));
            System.out.println();
        }
    }

    // Calculates the average of all numbers in the array
    public static double avgVal(double[] arr) {
        double total = 0;
        for (double num : arr) {
            total += num;
        }
        return total / arr.length;
    }

    // Finds the largest number in the array
    public static double maxVal(double[] arr) {
        double max = arr[0];
        for (double num : arr) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    // Finds the smallest number in the array
    public static double minVal(double[] arr) {
        double min = arr[0];
        for (double num : arr) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }

    // Handles all result formatting for the entire program
    // Always displays in the format: num1 operator num2 = result
    // Trims trailing zeros so 7.2500 shows as 7.25
    public static void displayResult(double num1, String operator, double num2, double result) {

        // Format the result to 4 decimal places and trim trailing zeros
        String formattedResult = df.format(result);

        // Format the input numbers as well so they look nice in the output
        String formattedNum1 = df.format(num1);

        // Format num2 as well so it looks nice in the output
        String formattedNum2 = df.format(num2);

        // Print the result in the format: num1 operator num2 = result
        System.out.println();
        System.out.println(" " + formattedNum1 + " " + operator + " " + formattedNum2 + " = " + formattedResult);
        System.out.println();
    }
}