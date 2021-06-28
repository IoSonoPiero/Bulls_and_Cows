import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean hasErrors = false;

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please, enter the secret code's length:");

        String inputString = scanner.next();

        int secretLength = 0;
        if (onlyDigits(inputString)) {
            secretLength = Integer.parseInt(inputString);
        } else {
            System.out.format("Error: \"%s\" isn't a valid number.", inputString);
            hasErrors = true;
        }

        if (!hasErrors && secretLength <= 0) {
            System.out.format("Error: can't generate a secret number with a length of %d.", secretLength);
            hasErrors = true;
        }

        if (!hasErrors && secretLength > 36) {
            System.out.format("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.", secretLength);
            hasErrors = true;
        }

        int maxRange = 0;
        if (!hasErrors) {
            System.out.println("Input the number of possible symbols in the code:");

            inputString = scanner.next();

            if (onlyDigits(inputString)) {
                maxRange = Integer.parseInt(inputString);
            } else {
                System.out.format("Error: \"%s\" isn't a valid number.", inputString);
                hasErrors = true;
            }
        }

        if (!hasErrors && maxRange > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            hasErrors = true;
        }

        if (!hasErrors && secretLength > maxRange) {
            System.out.format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", secretLength, maxRange);
            hasErrors = true;
        }

        if (!hasErrors) {
            String secretNumber = calculateSecretNumber(secretLength, maxRange);
            //System.out.println(secretNumber);
            startTheGame(secretNumber);
        }
    }

    public static boolean onlyDigits(String str) {
        for (int i = 0; i < str.length(); i++) {
            return Character.isDigit(str.charAt(i));
        }
        return false;
    }

    private static String calculateSecretNumber(int secretLength, int maxRange) {
        // initialize the seed
        Random random = new Random(System.nanoTime());

        StringBuilder theSecret = new StringBuilder(secretLength);

        int value;

        System.out.print("The secret is prepared: ");

        // loop until secret length and fill the secrete with unique digits
        while (theSecret.length() < secretLength) {

            random = new Random(System.nanoTime());
            value = random.nextInt(maxRange);
            value = value < 10 ? value + 48 : value + 87;
            //value = random.nextInt(secretLength + 1);
            String theCharacter = Character.toString((char) value);

            // if the value is "in" theSecret
            if (theSecret.indexOf(theCharacter) != -1) {
                // skip
                continue;
            }
            // append the value to the secret
            theSecret.append(theCharacter);
            System.out.print("*");
        }

        //determine what to print before starting the game
        switch (maxRange) {
            case 10:
                System.out.println(" (0-9).");
                break;
            case 11:
                System.out.println(" (0-9, a).");
                break;
            default:
                System.out.println(" (0-9, a-" + (char) (maxRange + 86) + ").");
                break;
        }

        return theSecret.toString();
    }

    public static void startTheGame(String secretNumber) {
        boolean isGuessed = false;
        int cows = 0;
        int bulls = 0;
        int iterator = 1;

        System.out.println("Okay, let's start a game!");

        // loop until secret guessed
        while (!isGuessed) {
            System.out.println("Turn " + iterator);

            Scanner scanner = new Scanner(System.in);
            String number = scanner.next();
            for (int i = 0; i < secretNumber.length(); i++) {
                for (int j = 0; j < number.length(); j++) {
                    if (number.charAt(j) == secretNumber.charAt(i)) {
                        if (i == j) {
                            bulls++;
                        } else {
                            cows++;
                        }
                    }
                }

            }

            if (cows == bulls && bulls == 0) {
                System.out.println("Grade: None. ");
            } else {
                System.out.println("Grade: " + bulls + " bull(s) and " + cows + " cow(s). ");
            }

            if (bulls == secretNumber.length()) {
                System.out.println("Congratulations! You guessed the secret code.");
                isGuessed = true;
            }
            iterator++;
            bulls = 0;
            cows = 0;
        }
    }
}