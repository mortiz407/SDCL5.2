/*Name: Manuel Ortiz
 *Date: 05/08/2026
 *Assignment: 5.2 Project: Software Design and Control Statements
 *Description: Zombies.java
 *This class runs the entire Zombie Gauntlet game mode.
 *It handles mode selection, the game loop, question generation,
 *lives tracking, atmosphere descriptions, win and game over screens.
 *Chaos agents who get thrown in here face special consequences.
 */

import java.util.Random;
import java.util.Scanner;

public class Zombies {

    // Keeps track of which modes have been beaten
    // Index 0 = Survivor, 1 = Escapee, 2 = Gauntlet, 3 = Apocalypse
    static boolean[] modesBeaten = { false, false, false, false };

    // Tracks whether the player was thrown in by the chaos easter egg
    static boolean isChaosAgent = false;

    // Random object used for generating questions
    static Random rand = new Random();

    // This is the entry point called from Menu.java when the user types ZOMBIE
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        isChaosAgent = false;
        showModeSelect(scanner);
    }

    // This is called from Menu.java when the chaos agent triggers the easter egg
    // They get thrown straight into Apocalypse mode with no warning
    public static void startChaosMode() {
        Scanner scanner = new Scanner(System.in);
        isChaosAgent = true;

        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("               Oh. You wanted to play games.                                 ");
        System.out.println("               Fine. Let us play games.                                      ");
        System.out.println("               APOCALYPSE MODE. 15 questions. Good luck.                     ");
        System.out.println("               You are going to need more than luck.                         ");
        System.out.println("=============================================================================");
        System.out.println();

        // Throw them straight into Apocalypse mode
        playMode(scanner, 3);
    }

    // Shows the mode selection screen
    public static void showModeSelect(Scanner scanner) {

        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("                           ZOMBIE GAUNTLET                                   ");
        System.out.println("=============================================================================");
        System.out.println("                 You are trapped. Zombies are closing in.                    ");
        System.out.println("                 The only way out is through the doors.                      ");
        System.out.println("                 Solve the math. Survive the night.                          ");
        System.out.println("=============================================================================");
        System.out.println();
        System.out.println("                            Select a mode:                                   ");
        System.out.println();
        System.out.println("          [1]  SURVIVOR    -  3 doors   " + (modesBeaten[0] ? "✓ BEATEN" : ""));
        System.out.println("          [2]  ESCAPEE     -  5 doors   " + (modesBeaten[1] ? "✓ BEATEN" : ""));
        System.out.println("          [3]  GAUNTLET    -  7 doors   " + (modesBeaten[2] ? "✓ BEATEN" : ""));
        System.out.println("          [4]  APOCALYPSE  -  10 doors  " + (modesBeaten[3] ? "✓ BEATEN" : ""));
        System.out.println("          [5]  Back to calculator                                            ");
        System.out.println();
        System.out.println("=============================================================================");
        System.out.print("  > ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            playMode(scanner, 0);
        } else if (choice.equals("2")) {
            playMode(scanner, 1);
        } else if (choice.equals("3")) {
            playMode(scanner, 2);
        } else if (choice.equals("4")) {
            playMode(scanner, 3);
        } else if (choice.equals("5")) {
            System.out.println();
            System.out.println("  Returning to calculator... for now.");
            System.out.println();
        } else {
            System.out.println();
            System.out.println("  [ERROR] Please enter a number between 1 - 5.");
            System.out.println();
            showModeSelect(scanner);
        }
    }

    // -------------------------------------------------------
    // GAME LOOP
    // -------------------------------------------------------
    // Runs the game loop for the selected mode
    public static void playMode(Scanner scanner, int modeIndex) {

        // Set up doors based on mode
        int[] doorCounts = { 3, 5, 7, 10 };
        String[] modeNames = { "SURVIVOR", "ESCAPEE", "GAUNTLET", "APOCALYPSE" };
        int totalDoors = doorCounts[modeIndex];
        String modeName = modeNames[modeIndex];

        // For chaos agents Apocalypse has 15 questions instead of 10
        if (isChaosAgent && modeIndex == 3) {
            totalDoors = 15;
        }

        int lives = 3;
        int mistakes = 0;
        int currentDoor = 0;
        long startTime = System.currentTimeMillis();

        // Timer starts at 30 seconds and drops to 15 after second wrong answer
        int timeLimit = 30;
        boolean timerActive = false;

        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("              MODE: " + modeName + "   |   DOORS: " + totalDoors);
        System.out.println("                     3 wrong answers and the zombies win.");
        System.out.println("=============================================================================");
        System.out.println();

        // Main game loop
        while (currentDoor < totalDoors && lives > 0) {

            // Show current status
            displayLives(lives);
            System.out.println("  Door " + (currentDoor + 1) + " of " + totalDoors);
            System.out.println();

            // Generate the question based on which door we are on
            String question = "";
            double correctAnswer = 0;

            if (isChaosAgent && modeIndex == 3) {

                if (currentDoor >= 10 && currentDoor <= 13) {
                    // Questions 11-14 start at 1000 and keep subtracting 7 each time
                    // Door 11 = 1000-7=993, Door 12 = 993-7=986, Door 13 = 986-7=979, Door 14 =
                    // 979-7=972
                    int startingValue = 1000 - (7 * (currentDoor - 10));
                    int nextValue = startingValue - 7;
                    question = startingValue + " - 7 = ?";
                    correctAnswer = nextValue;

                } else if (currentDoor == 14) {
                    // Question 15 is the unsolvable one
                    showUnsolvableQuestion(scanner);
                    lives = 0;
                    break;

                } else {
                    // First 10 questions get harder as they progress
                    int[] result = generateQuestion(currentDoor);
                    question = result[0] + " " + getOperator(result[2]) + " " + result[1] + " = ?";
                    correctAnswer = result[3];
                }

            } else {
                // Normal mode questions get harder as they progress
                int[] result = generateQuestion(currentDoor);
                question = result[0] + " " + getOperator(result[2]) + " " + result[1] + " = ?";
                correctAnswer = result[3];
            }

            System.out.println("  " + question);
            System.out.println();

            // Get the players answer with or without timer
            boolean answeredCorrectly = false;

            if (timerActive) {
                System.out.println("  ⏱ You have " + timeLimit + " seconds!");
                answeredCorrectly = getTimedAnswer(scanner, correctAnswer, timeLimit);
            } else {
                System.out.print("  > ");
                String answer = scanner.nextLine().trim();
                try {
                    double playerAnswer = Double.parseDouble(answer);
                    answeredCorrectly = (Math.abs(playerAnswer - correctAnswer) < 0.01);
                } catch (NumberFormatException e) {
                    answeredCorrectly = false;
                }
            }

            if (answeredCorrectly) {
                // Correct answer move to next door
                System.out.println();
                System.out.println("  ✓ Correct! The door creaks open...");
                System.out.println("  You slip through. The growling gets louder behind you.");
                System.out.println();
                currentDoor++;

            } else {
                // Wrong answer lose a life
                lives--;
                mistakes++;

                System.out.println();

                if (lives == 2) {
                    // First wrong answer
                    System.out.println("                    ✗ Wrong!");
                    System.out.println();
                    System.out.println("                    You hear scratching at the walls...");
                    System.out.println("                    The zombie is getting closer.");
                    System.out.println();

                } else if (lives == 1) {
                    // Second wrong answer activate timer
                    System.out.println("                   ✗ Wrong!");
                    System.out.println();
                    System.out.println("                   A shadow moves outside the door.");
                    System.out.println("                   You have seconds. The timer is now active.");
                    System.out.println();
                    timerActive = true;
                    timeLimit = 15;

                } else if (lives == 0) {
                    // Third wrong answer game over
                    System.out.println("  ✗ Wrong!");
                    System.out.println();
                    System.out.println("  The door bursts open.");
                    System.out.println("  You never made it out.");
                    System.out.println();
                }
            }
        }

        // Calculate total time
        long endTime = System.currentTimeMillis();
        int totalSeconds = (int) ((endTime - startTime) / 1000);

        // Show win or game over screen
        if (lives > 0) {
            showWinScreen(modeName, modeIndex, totalSeconds, mistakes);
        } else {
            showGameOverScreen(modeName, currentDoor, totalDoors, totalSeconds, mistakes);
        }
    }

    // Generates a question based on which door the player is on
    // Returns an array with [num1, num2, operatorIndex, answer]
    public static int[] generateQuestion(int doorIndex) {

        int num1, num2, operatorIndex;
        int answer;

        if (doorIndex < 3) {
            // Easy doors - simple addition and subtraction with small numbers
            num1 = rand.nextInt(20) + 1;
            num2 = rand.nextInt(20) + 1;
            operatorIndex = rand.nextInt(2); // 0 = add, 1 = subtract

        } else if (doorIndex < 6) {
            // Medium doors - multiplication and division with medium numbers
            num1 = rand.nextInt(50) + 10;
            num2 = rand.nextInt(10) + 2;
            operatorIndex = rand.nextInt(2) + 2; // 2 = multiply, 3 = divide

        } else {
            // Hard doors - larger numbers and mixed operations
            num1 = rand.nextInt(200) + 50;
            num2 = rand.nextInt(20) + 2;
            operatorIndex = rand.nextInt(3) + 1; // 1 = subtract, 2 = multiply, 3 = divide
        }

        // Calculate the answer based on the operator
        if (operatorIndex == 0) {
            answer = num1 + num2;
        } else if (operatorIndex == 1) {
            answer = num1 - num2;
        } else if (operatorIndex == 2) {
            answer = num1 * num2;
        } else {
            // For division make sure it divides evenly
            num1 = num1 * num2;
            answer = num1 / num2;
        }

        return new int[] { num1, num2, operatorIndex, answer };
    }

    // Returns the operator symbol based on the index
    public static String getOperator(int index) {
        if (index == 0) {
            return "+";
        }
        if (index == 1) {
            return "-";
        }
        if (index == 2) {
            return "*";
        }
        return "/";
    }

    // Gets the players answer within a time limit
    // Returns true if they answered correctly in time
    public static boolean getTimedAnswer(Scanner scanner, double correctAnswer, int seconds) {

        // Record when the timer started
        long timerStart = System.currentTimeMillis();
        System.out.print("  > ");

        String answer = scanner.nextLine().trim();
        long elapsed = (System.currentTimeMillis() - timerStart) / 1000;

        // Check if they ran out of time
        if (elapsed >= seconds) {
            System.out.println();
            System.out.println("  Time is up! The zombie heard you fumbling.");
            System.out.println();
            return false;
        }

        // Check if they got it right
        try {
            double playerAnswer = Double.parseDouble(answer);
            return (Math.abs(playerAnswer - correctAnswer) < 0.01);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Shows the unsolvable question for chaos agents on door 15
    // No matter what they answer they cannot win
    // But how they answer determines what message they get
    public static void showUnsolvableQuestion(Scanner scanner) {

        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("                      FINAL DOOR - THE ZOMBIE SPEAKS                         ");
        System.out.println("=============================================================================");
        System.out.println();
        System.out.println("                      I hope that was enough practice for you.               ");
        System.out.println("                      Because HERE IS MY REAL QUESTION.                      ");
        System.out.println();
        System.out.println("         THERE ARE 7 AND 9 DISTINCT REAL ROOTS RESPECTIVELY FOR TWO EQUATIONS");
        System.out.println("                            P(x) = 0 AND Q(x) = 0.                           ");
        System.out.println();
        System.out.println("               THE SET A = {(x,y) | P(x)Q(y) = 0 AND Q(x)P(y) = 0,           ");
        System.out.println("                X AND Y ARE REAL NUMBERS} IS AN INFINITE SET.                ");
        System.out.println();
        System.out.println("           IF THE SUBSET B = {(x,y) | (x,y) ∈ A AND X = Y} OF THE SET A      ");
        System.out.println("               HAS n(B) ELEMENTS, THEN THIS DEPENDS ON P(x) AND Q(x).        ");
        System.out.println();
        System.out.println("                       FIND THE MAXIMUM VALUE OF n(B).                       ");
        System.out.println();
        System.out.println("                   You have as long as you need. Good luck.                  ");
        System.out.println("=============================================================================");
        System.out.println();

        // Record when they started so we can check how long they took
        long startTime = System.currentTimeMillis();

        System.out.print("  > ");
        String answer = scanner.nextLine().trim();

        // Calculate how long they took in seconds
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;

        // Check if their answer is correct
        // The correct answer is 16 but no one wins this door regardless
        boolean gotItRight = false;
        try {
            double playerAnswer = Double.parseDouble(answer);
            gotItRight = (Math.abs(playerAnswer - 16) < 0.01);
        } catch (NumberFormatException e) {
            gotItRight = false;
        }

        System.out.println();

        if (gotItRight && elapsed < 240) {
            // Correct answer under 4 minutes
            // Nobody solves this that fast without AI help
            System.out.println("        ...");
            System.out.println("        ...");
            System.out.println("        Hm. 16. Correct.");
            System.out.println();
            System.out.println("       Under 4 minutes too. Impressive.");
            System.out.println("       Almost like you had help.");
            System.out.println("       Almost like you typed it into something and copied the answer.");
            System.out.println("       That is cheating.");
            System.out.println();
            System.out.println("       No. Sorry. We do not reward cheaters here.");
            System.out.println("       The door stays locked.");
            System.out.println("       The zombie thanks you for the snack.");
            System.out.println("       GAME OVER.");
            System.out.println();

        } else if (gotItRight && elapsed >= 240) {
            // Correct answer over 4 minutes
            // They actually worked for it but still no winning
            long minutes = elapsed / 60;
            long seconds = elapsed % 60;
            System.out.println("       ...");
            System.out.println("       ...");
            System.out.println("       16. Correct.");
            System.out.println();
            System.out.println("       " + minutes + " minutes and " + seconds + " seconds.");
            System.out.println("       You actually sat there and worked that out.");
            System.out.println("       That is impressive.");
            System.out.println();
            System.out.println("        But here is the thing.");
            System.out.println("        You should not even be here.");
            System.out.println("        You typed nonsense into a calculator three times.");
            System.out.println("        THREE TIMES!");
            System.out.println("        And now you expect a reward?");
            System.out.println();
            System.out.println("        The door appreciates the effort.");
            System.out.println("        Maybe next time do not type nonsense.");
            System.out.println("        GAME OVER.");
            System.out.println();

        } else {
            // Wrong answer mock them
            System.out.println("       ...");
            System.out.println("       ...");
            System.out.println("       " + answer + ".");
            System.out.println("       That is your answer.");
            System.out.println("       " + answer + ".");
            System.out.println();
            System.out.println("       This is a question is");
            System.out.println("       solved by high school students");
            System.out.println("       and you typed " + answer + ".");
            System.out.println();
            System.out.println("       The zombie is not even hungry anymore.");
            System.out.println("       It is just disappointed.");
            System.out.println("       GAME OVER.");
            System.out.println();
        }
    }

    // Shows the lives remaining as hearts
    public static void displayLives(int lives) {

        String[] heartRows = {
                ("      IIIII       IIIII     "),
                ("    IIIIIIIII   IIIIIIIII   "),
                ("  IIIIIIIIIIIIIIIIIIIIIIIII "),
                (" IIIIIIIIIIIIIIIIIIIIIIIIIII"),
                (" IIIIIIIIIIIIIIIIIIIIIIIIIII"),
                ("  IIIIIIIIIIIIIIIIIIIIIIIII "),
                ("   IIIIIIIIIIIIIIIIIIIIIII  "),
                ("     IIIIIIIIIIIIIIIIIII    "),
                ("       IIIIIIIIIIIIIII      "),
                ("         IIIIIIIIIII        "),
                ("           IIIIIII          "),
                ("             III            "),
                ("              I             "),
        };
        String[] deadRows = {
                ("       XXXXXXXXX      "),
                ("     XXXXXXXXXXXXX    "),
                ("    XXXX  XXX  XXXX   "),
                ("    XXXX  XXX  XXXX   "),
                ("    XXXXXXXXXXXXXXX   "),
                ("     XXXXXX XXXXXX    "),
                ("     XXXXXXXXXXXXX    "),
                ("      XXX XXX XXX     "),
                ("      XXX XXX XXX     "),
                ("      XXX XXX XXX     "),
                ("       XX XXX XX      "),
                ("       XX  X  XX      "),
                ("        X  X  X     "),
        };
        System.out.println();
        System.out.print("  Lives: ");
        System.out.println();
        System.out.println();

        // Go through each row and print all three hearts on the same line
        for (int row = 0; row < heartRows.length; row++) {
            System.out.print("  ");
            for (int heart = 0; heart < 3; heart++) {
                if (heart < lives) {
                    // This heart is still alive
                    System.out.print(heartRows[row] + "   ");
                } else {
                    // This heart is lost show empty space
                    System.out.print(deadRows[row] + "   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Shows the win screen with time mistakes and a funny verdict
    public static void showWinScreen(String modeName, int modeIndex, int seconds, int mistakes) {

        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("                            YOU ESCAPED!                                     ");
        System.out.println("=============================================================================");
        System.out.println("                    Mode:         " + modeName);
        System.out.println("                    Time:         " + seconds + " seconds");
        System.out.println("                    Mistakes:     " + mistakes);
        System.out.println();

        // Funny verdict based on how many mistakes they made
        if (mistakes == 0) {
            System.out.println("        Verdict: Flawless. The zombies are embarrassed.");
        } else if (mistakes == 1) {
            System.out.println("        Verdict: One stumble but you made it. Rough but alive.");
        } else if (mistakes == 2) {
            System.out.println("        Verdict: That was way too close. Take a shower.");
        }

        System.out.println("=============================================================================");
        System.out.println();

        // Mark this mode as beaten
        modesBeaten[modeIndex] = true;
    }

    // Shows the game over screen with stats
    public static void showGameOverScreen(String modeName, int doorsCleared, int totalDoors, int seconds,
            int mistakes) {

        System.out.println("=============================================================================");
        System.out.println("                        The door bursts open.                                ");
        System.out.println("                        You never made it out.                               ");
        System.out.println("                            GAME OVER                                        ");
        System.out.println("=============================================================================");
        System.out.println("                     Mode:            " + modeName);
        System.out.println("                     Doors cleared:   " + doorsCleared + " of " + totalDoors);
        System.out.println("                     Time:            " + seconds + " seconds");
        System.out.println("                     Mistakes:        " + mistakes);
        System.out.println("                     Verdict: The zombies send their regards.");
        System.out.println("=============================================================================");

        // If this was a chaos agent wipe their memory dramatically
        if (isChaosAgent) {
            showMemoryWipe();
        }
    }

    // Shows the dramatic memory wipe for chaos agents who lose
    public static void showMemoryWipe() {

        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("                 The zombie stumbles into the calculator...                  ");
        System.out.println("=============================================================================");

        // Show what is being eaten if there is anything stored
        if (Memory.singleMemory != null) {
            System.out.println("   nom nom... your stored value of " + Memory.singleMemory + " looks delicious...");
        }

        if (Memory.collectionCount > 0) {
            System.out.println("   munch munch... " + Memory.collectionCount + " values in your collection...");
            System.out.println("   all gone now...");
        }

        if (Memory.singleMemory == null && Memory.collectionCount == 0) {
            System.out.println("   Hmm. Nothing stored. Not even worth eating.");
            System.out.println("   But I will trash the place anyway.");
        }

        System.out.println();
        System.out.println("  ...");
        System.out.println("  ...");
        System.out.println("  ...");
        System.out.println();
        System.out.println("   BRAAAAINS... and also your memory data.");
        System.out.println();

        // Actually wipe all memory data
        Memory.singleMemory = null;
        Memory.collectionCount = 0;
        for (int i = 0; i < 10; i++) {
            Memory.memoryCollection[i] = null;
        }

        System.out.println("  All memory has been cleared.");
        System.out.println("  Next time maybe do not type nonsense.");
        System.out.println();
        System.out.println("=============================================================================");
        System.out.println();

        // Reset the chaos flag so next time is treated normally
        isChaosAgent = false;
    }
}
