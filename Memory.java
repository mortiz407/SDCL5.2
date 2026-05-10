
/*Name: Manuel Ortiz
 *Date: 05/08/2026
 *Assignment: 5.2 Project: Software Design and Control Statements
 *Description: Memory.java
 *This class handles everything related to memory.
 *Single value memory and collection memory both live here.
 *Commands.java handles math, Memory.java handles storage.
 *Think of it as the calculator's brain.
 */
import java.text.DecimalFormat;
import java.util.Scanner;

public class Memory {

    // DecimalFormat pattern that shows up to 4 decimal places
    // but trims unnecessary zeros so 7.2500 shows as 7.25
    static DecimalFormat df = new DecimalFormat("0.####");

    // -------------------------------------------------------
    // SINGLE VALUE MEMORY
    // Stores one number the user can recall, replace, or clear
    // We use Double with a capital D so we can set it to null when empty
    // null means nothing is stored yet
    // -------------------------------------------------------

    static Double singleMemory = null;

    // Stores a value into the single memory slot
    // Example: user types MS 42
    public static void memoryStore(String input) {

        // Split the input to get the number after MS
        String[] parts = input.trim().split(" ");

        // Make sure the user actually typed a number after MS
        if (parts.length < 2) {
            System.out.println();
            System.out.println(" [ERROR] You need to enter a value to store.");
            System.out.println(" Example:  MS 42");
            System.out.println();
            return;
        }

        // Convert the number and store it
        double value = Double.parseDouble(parts[1].trim());
        singleMemory = value;
        System.out.println();
        System.out.println(" Memory stored: " + df.format(singleMemory));
        System.out.println();
    }

    // Recalls the value stored in single memory
    // Example: user types MR
    public static void memoryRecall() {

        // Check if anything is stored first
        // If singleMemory is null nothing has been stored yet
        if (singleMemory == null) {
            System.out.println();
            System.out.println(" [ERROR] Memory is empty. Nothing stored yet.");
            System.out.println(" Use  MS [value]  to store a number first.");
            System.out.println();
        } else {
            System.out.println();
            System.out.println(" Memory value: " + df.format(singleMemory));
            System.out.println();
        }
    }

    // Clears the single memory slot
    // Example: user types MC
    public static void memoryClear() {

        // Check if there is even anything to clear
        if (singleMemory == null) {
            System.out.println();
            System.out.println(" Memory is already empty. Nothing to clear.");
            System.out.println();
        } else {
            singleMemory = null;
            System.out.println();
            System.out.println(" Memory cleared.");
            System.out.println();
        }
    }

    // Peeks at the single memory slot without changing anything
    // Example: user types M?
    public static void memoryPeek() {

        // Check if there is anything stored
        if (singleMemory == null) {
            System.out.println();
            System.out.println(" Memory is currently empty.");
            System.out.println();
        } else {
            System.out.println();
            System.out.println(" Currently stored: " + df.format(singleMemory));
            System.out.println();
        }
    }

    // -------------------------------------------------------
    // COLLECTION MEMORY
    // Stores up to 10 integers the user can manage
    // We use Integer with a capital I so we can tell which slots are empty
    // -------------------------------------------------------

    // This array holds up to 10 integers
    static Integer[] memoryCollection = new Integer[10];

    // Keeps track of how many values are currently stored
    static int collectionCount = 0;

    // Adds a value to the collection from the main prompt
    // Example: user types M+ 25
    public static void collectionAdd(String input) {

        // Split the input to get the number after M+
        String[] parts = input.trim().split(" ");

        // Make sure the user typed a number after M+
        if (parts.length < 2) {
            System.out.println();
            System.out.println(" [ERROR] You need to enter a value to add.");
            System.out.println(" Example:  M+ 25");
            System.out.println();
            return;
        }

        // Check if the collection is already full
        if (collectionCount >= 10) {
            System.out.println();
            System.out.println(" [ERROR] Memory collection is full. Maximum 10 values.");
            System.out.println(" Use MLIST to manage your stored values.");
            System.out.println();
            return;
        }

        // Convert and store the value
        int value = Integer.parseInt(parts[1].trim());
        memoryCollection[collectionCount] = value;
        collectionCount++;
        System.out.println();
        System.out.println(" Added " + value + " to memory collection.");
        System.out.println(" Collection now has " + collectionCount + " of 10 slots used.");
        System.out.println();
    }

    // Opens the memory collection sub menu
    // Example: user types MLIST
    public static void collectionMenu(Scanner input) {

        // Keep the sub menu running until the user picks Back
        boolean inMenu = true;

        while (inMenu) {

            // Show the sub menu
            System.out.println();
            System.out.println("===========================================================");
            System.out.println("                    Memory Collection                      ");
            System.out.println("===========================================================");
            System.out.println("  [1]  Show all values                                     ");
            System.out.println("  [2]  Show count                                          ");
            System.out.println("  [3]  Remove a value                                      ");
            System.out.println("  [4]  Add a value                                         ");
            System.out.println("  [5]  Get sum                                             ");
            System.out.println("  [6]  Get average                                         ");
            System.out.println("  [7]  Get difference of first and last value              ");
            System.out.println("  [8]  Back to calculator                                  ");
            System.out.println("===========================================================");
            System.out.print("  > ");

            // Read the users choice
            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                // Show all values stored in the collection
                collectionShowAll();

            } else if (choice.equals("2")) {
                // Show how many values are stored
                collectionShowCount();

            } else if (choice.equals("3")) {
                // Remove a specific value from the collection
                collectionRemove(input);

            } else if (choice.equals("4")) {
                // Add a new value to the collection
                collectionAddFromMenu(input);

            } else if (choice.equals("5")) {
                // Get the sum of all values
                collectionSum();

            } else if (choice.equals("6")) {
                // Get the average of all values
                collectionAverage();

            } else if (choice.equals("7")) {
                // Get the difference between the first and last value
                collectionDifference();

            } else if (choice.equals("8")) {
                // Go back to the main calculator
                System.out.println();
                System.out.println(" Returning to calculator...");
                System.out.println();
                inMenu = false;

            } else {
                // The user typed something that is not on the menu
                System.out.println();
                System.out.println(" [ERROR] Please enter a number between 1 and 8.");
                System.out.println();
            }
        }
    }

    // Shows all values currently stored in the collection
    public static void collectionShowAll() {

        // Check if the collection is empty
        if (collectionCount == 0) {
            System.out.println();
            System.out.println(" Memory collection is empty.");
            System.out.println(" Use  M+ [value]  to add numbers.");
            System.out.println();
            return;
        }

        // Print all stored values with their position number
        System.out.println();
        System.out.println(" Stored values:");
        for (int i = 0; i < collectionCount; i++) {
            System.out.println("   [" + (i + 1) + "]  " + memoryCollection[i]);
        }
        System.out.println();
    }

    // Shows how many values are in the collection
    public static void collectionShowCount() {
        System.out.println();
        System.out.println(" Collection count: " + collectionCount + " of 10 slots used.");
        System.out.println();
    }

    // Removes a specific value from the collection by position
    public static void collectionRemove(Scanner input) {

        // Check if the collection is empty first
        if (collectionCount == 0) {
            System.out.println();
            System.out.println(" Memory collection is empty. Nothing to remove.");
            System.out.println();
            return;
        }

        // Show all values so the user knows what to pick
        collectionShowAll();

        // Ask the user which position to remove
        System.out.print(" Enter the number of the value you want to remove: ");
        String choice = input.nextLine().trim();
        int position = Integer.parseInt(choice) - 1;

        // Make sure the position is valid
        if (position < 0 || position >= collectionCount) {
            System.out.println();
            System.out.println(" [ERROR] Invalid position. Please pick a number from the list.");
            System.out.println();
            return;
        }

        // Save the value being removed so we can confirm it
        int removed = memoryCollection[position];

        // Shift all values after the removed one down by one position
        // This keeps the array clean with no gaps in the middle
        for (int i = position; i < collectionCount - 1; i++) {
            memoryCollection[i] = memoryCollection[i + 1];
        }

        // Clear the last slot and reduce the count
        memoryCollection[collectionCount - 1] = null;
        collectionCount--;

        System.out.println();
        System.out.println(" Removed " + removed + " from the collection.");
        System.out.println(" Collection now has " + collectionCount + " of 10 slots used.");
        System.out.println();
    }

    // Adds a value to the collection from inside the sub menu
    public static void collectionAddFromMenu(Scanner input) {

        // Check if the collection is already full
        if (collectionCount >= 10) {
            System.out.println();
            System.out.println(" [ERROR] Memory collection is full. Maximum 10 values.");
            System.out.println(" Remove a value first before adding a new one.");
            System.out.println();
            return;
        }

        // Ask the user for the value to add
        System.out.print(" Enter a whole number to add: ");
        String choice = input.nextLine().trim();
        int value = Integer.parseInt(choice);

        // Store the value and increase the count
        memoryCollection[collectionCount] = value;
        collectionCount++;

        System.out.println();
        System.out.println(" Added " + value + " to memory collection.");
        System.out.println(" Collection now has " + collectionCount + " of 10 slots used.");
        System.out.println();
    }

    // Gets the sum of all values in the collection
    public static void collectionSum() {

        // Check if the collection is empty
        if (collectionCount == 0) {
            System.out.println();
            System.out.println(" [ERROR] Memory collection is empty. Nothing to add up.");
            System.out.println();
            return;
        }

        // Add up all the values
        int total = 0;
        for (int i = 0; i < collectionCount; i++) {
            total += memoryCollection[i];
        }

        System.out.println();
        System.out.println(" Sum of all values: " + total);
        System.out.println();
    }

    // Gets the average of all values in the collection
    public static void collectionAverage() {

        // Check if the collection is empty
        if (collectionCount == 0) {
            System.out.println();
            System.out.println(" [ERROR] Memory collection is empty. Nothing to average.");
            System.out.println();
            return;
        }

        // Add up all the values
        int total = 0;
        for (int i = 0; i < collectionCount; i++) {
            total += memoryCollection[i];
        }

        // Divide by the count to get the average
        double average = (double) total / collectionCount;

        System.out.println();
        System.out.println(" Average of all values: " + df.format(average));
        System.out.println();
    }

    // Gets the difference between the first and last value in the collection
    public static void collectionDifference() {

        // Check if the collection is empty
        if (collectionCount == 0) {
            System.out.println();
            System.out.println(" [ERROR] Memory collection is empty. Nothing to compare.");
            System.out.println();
            return;
        }

        // Check if there is at least two values to compare
        if (collectionCount < 2) {
            System.out.println();
            System.out.println(" [ERROR] You need at least 2 values to get a difference.");
            System.out.println(" Add more values using  M+ [value]  or option 4 in this menu.");
            System.out.println();
            return;
        }

        // Grab the first and last values from the collection
        int firstValue = memoryCollection[0];
        int lastValue = memoryCollection[collectionCount - 1];

        // Subtract the last from the first
        int difference = firstValue - lastValue;

        System.out.println();
        System.out.println(" First value: " + firstValue);
        System.out.println(" Last value:  " + lastValue);
        System.out.println(" Difference:  " + firstValue + " - " + lastValue + " = " + difference);
        System.out.println();
    }
}