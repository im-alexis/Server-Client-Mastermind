/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment_5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    private Socket client;
    private final BufferedReader fromClient; // Read from the Client
    private final PrintWriter toClient; // Write to the Client
    private  InputStreamReader x;
    private ArrayList <ClientManager> otherPlayers;
    private ArrayList <String> clientHistory = new ArrayList<>();
    private int attempts;
    private final int clientID;
    private  boolean closeConnection = false;
    private boolean isStartGamePrompt = false;
    private boolean isGuessPrompt = false;
    private boolean solved = false;
    private boolean playerAcceptedGame = false;


    public ClientManager(Socket clientSocket, ArrayList<ClientManager> otherPlayers, int playerNum) throws IOException {
        this.client = clientSocket;
        this.x =new InputStreamReader(client.getInputStream());
        this.fromClient = new BufferedReader(x);
        this.toClient = new PrintWriter(client.getOutputStream(),true);
        this.clientID = playerNum;
        this.otherPlayers = otherPlayers;
        this.attempts = GameConfiguration.guessNumber;
    }



    @Override
    public void run() {
        UserText.intro(toClient, clientID, this);
        String response = "";
        try {
            while (true) {
                if (fromClient.ready()) {
                    response = fromClient.readLine();
                    if(response.contains("SAY")) {
                        int firstSpace = response.indexOf(" ");
                        if(firstSpace != -1){
                            String msg = response.substring(firstSpace +1);
                            System.out.println("[Server]" + " user#" + clientID + ": " + msg);
                            sayToLobby(msg);
                        }
                    }
                    if(response.equals("QUIT")) {
                        clientDisconnect();
                        break;
                    }
                    if(attempts > 1) {
                        if (isStartGamePrompt) {
                            if (!response.equals("Y")) {
                                UserText.newGamePrompt(toClient, this);

                            } else {
                                if (!ServerFunctionality.isGameStarted()) {
                                    System.out.println("[Server]" + " user#" + clientID + " has started a new game");
                                    playerAcceptedGame = true;
                                    ServerFunctionality.resetGame();
                                }
                                playerAcceptedGame = true;
                                System.out.println("[Server] Game for user#" + clientID + " is getting set");
                                toClient.println("\nGenerating secret code ...");

                            }
                        } else if (isGuessPrompt && !response.contains("SAY")) {
                            System.out.println("[Server] Analysing user#" + clientID + " response: " + response);
                            Pegs.analyseUserInput(response, this, toClient);
                            if (solved) {
                                ServerFunctionality.declareWinner(clientID, (GameConfiguration.guessNumber - attempts));
                                System.out.println("[Server] user#" + clientID + " has won");
                            }
                        }
                        if (ServerFunctionality.isGameStarted() && playerAcceptedGame) {
                            UserText.userPrompt(toClient, this);
                        }
                    } else if (ServerFunctionality.thereIsSomeone()) {
                        ServerFunctionality.everyoneLost();
                    } else {
                        toClient.println("You're out of guesses");
                    }
                }
            }

        } catch (IOException e) {}

    }

    private void sayToLobby (String msg){
        for(ClientManager e : otherPlayers){
            if(e.getClientID() != clientID){
                e.printToClient("[user#" + clientID + "]" + " " + msg);
            }
        }
    }
    public void printToClient (String s){
        toClient.println(s);
    }
    public void resetAttempts(){
        this.attempts = GameConfiguration.guessNumber;
    }

    public int getClientID() {
        return clientID;
    }

    public boolean isPlayerAcceptedGame() {
        return playerAcceptedGame;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isCloseConnection() {
        return closeConnection;
    }

    public void setGuessPrompt(boolean guessPrompt) {
        isGuessPrompt = guessPrompt;
    }
    public void setStartGamePrompt(boolean sGP){
        isStartGamePrompt = sGP;
    }

    public ArrayList<String> getClientHistory() {
        return clientHistory;
    }
    public void addToHistory(String guess){
        clientHistory.add(guess);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
    public void usedAttempt (){
        attempts--;
    }

    public void setPlayerAcceptedGame(boolean playerAcceptedGame) {
        this.playerAcceptedGame = playerAcceptedGame;
    }

    public void clientDisconnect()  {

        try {
            closeConnection = true;
            fromClient.close();
            toClient.println("SHUTDOWN");
            toClient.close();
            x.close();
            } catch (IOException ex) {

            }
    }


}
