/* EE422C Assignment #2 submission by
 * Modified for Server/Client Application
 * Alexis Torres
 * at39625
 */

package assignment5;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Pegs {
    private static boolean isValid(String userInput){
        if(userInput.length() != GameConfiguration.pegNumber){return false;} // if it is not equal to the chars needed then its automatically not valid
        for(int i = 0; i < userInput.length(); i++){
            if(!Arrays.asList(GameConfiguration.colors).contains(userInput.substring(i,i+1))){ // if it dones not cointain the color then it is not valid
                return false;
            }
        }
        return true;
    }
    public static void analyseUserInput (String userInput, ClientManager client, PrintWriter toClient) {
        ArrayList<Object> bIndex = new ArrayList<>(); // holding the index of the white and black pegs
        ArrayList <Object> wIndex = new ArrayList<>();
        int bPeg = 0;
        int wPeg = 0;
        String pegResult;
        if (userInput.equals("HISTORY")) { // can use different variations of history to access
            //System.out.println("\nGuess\t\tResult");
            toClient.println();
            for (String s : client.getClientHistory()) {
                toClient.println( s.substring(0, 4) + "\t\t" + s.substring(16));
            }
            toClient.println();
            return;
        }
        boolean valid = isValid(userInput);
        if (!valid) {
            toClient.println("\n" + userInput + "-> INVALID GUESS\n");
            return;
        }
        for (int i = 0; i < userInput.length(); i++) { //doing bPegs first makes tracking what is has been found easier
            if (userInput.substring(i, i + 1).equals(ServerMain.getSecretCode().substring(i, i + 1))) {
                bPeg++;
                bIndex.add(i);
                if (bPeg == GameConfiguration.pegNumber) {
                    client.setSolved(true);
                }
            }
        }
        boolean doneWithCol; // since I am checking multiple pegs, this help to know when the white peg has been placed in the right spot
        for(int i = 0; i < userInput.length(); i++){
            doneWithCol = false;
            if(!bIndex.contains(i)){
                for(int j = 0; j < userInput.length(); j++) {
                    if(userInput.substring(i, i+1).equals(ServerMain.getSecretCode().substring(j,j+1))){
                        if(!bIndex.contains(j) && !wIndex.contains(j) && !doneWithCol){ // if the color has not been found and wPeg has not been incremented
                            wPeg++;
                            wIndex.add(j);
                            doneWithCol = true; // this to not go back into the if statements and increment wPeg w/ repeats
                        }
                    }
                }
            }
        }
        client.usedAttempt();
        if(!client.isSolved() && client.getAttempts() > 0) {
            pegResult = userInput + " -> Result: " + bPeg + "B_" + wPeg + "W";
            client.addToHistory(pegResult);
            toClient.print("\n" + pegResult + "\n\n");
        }
        else if (client.isSolved() ){
            pegResult = userInput + " -> Result: " + bPeg + "B_" + wPeg + "W";
            client.addToHistory(pegResult);
            toClient.println("\n" + pegResult);
        } else if (!client.isSolved()  && client.getAttempts() == 0) {
            toClient.println();
        }


    }
}
