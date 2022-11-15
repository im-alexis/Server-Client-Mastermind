/* EE422C Assignment #2 submission by
 * Modified for Server/Client Application
 * Alexis Torres
 * at39625
 */

package assignment5;
import java.io.PrintWriter;



public class UserText {


    public static void intro (PrintWriter toClient, int user, ServerClientManager client){

        toClient.println("Hello user#"+ user +". Welcome to Mastermind.  Here are the rules.\n");
        toClient.println("This is a text version of the classic board game Mastermind.\n");
        toClient.println("The computer will think of a secret code. The code consists of 4\n" +
            "colored pegs. The pegs MUST be one of six colors: blue, green,\n"+
            "orange, purple, red, or yellow. A color may appear more than once in\n" +
            "the code. You try to guess what colored pegs are in the code and\n"+
            "what order they are in. After you make a valid guess the result\n"+
            "(feedback) will be displayed.\n");
        toClient.println(
            "The result consists of a black peg for each peg you have guessed\n"+
            "exactly correct (color and position) in your guess. For each peg in\n"+
            "the guess that is the correct color, but is out of position, you get\n"+
            "a white peg. For each peg, which is fully incorrect, you get no\n"+
            "feedback.\n"
            );
        toClient.println("Only the first letter of the color is displayed. B for Blue, R for\n" +
            "Red, and so forth. When entering guesses you only need to enter the\n"+
            "first character of each color as a capital letter.\n");
        toClient.println("You have " + GameConfiguration.guessNumber + " guesses to figure out the secret code or you lose the\n" +
                "game. Are you ready to play? (Y/N): ");

        client.setStartGamePrompt(true);
        client.setGuessPrompt(false);

    }
    public static void newGamePrompt(PrintWriter toClient, ServerClientManager client){
        toClient.println("Are you ready for another game? (Y/N): ");
        client.setStartGamePrompt(true);
        client.setGuessPrompt(false);


    }
    public static void userPrompt (PrintWriter toClient, ServerClientManager client){
        toClient.println("You have " + client.getAttempts() + " guesses left.\n"
                +"What is your next guess?\n" + "Type in the characters for your guess and press enter.\n" +
                "Enter guess: ");
        client.setStartGamePrompt(false);
        client.setGuessPrompt(true);


    }


}
