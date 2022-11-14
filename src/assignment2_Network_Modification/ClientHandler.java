/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment2_Network_Modification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket client;
    private final BufferedReader fromClient; // Read from the Client
    private final PrintWriter toClient; // Write to the Client
    private ArrayList <ClientHandler> otherPlayers;
    private ArrayList <String> clientHistory = new ArrayList<>();
    private int attempts;
    private final int name;
    private  boolean closeConnection = false;
    private boolean isStartGamePrompt = false;
    private boolean isGuessPrompt = false;
    private boolean solved = false;
    private boolean playerAcceptedGame = false;


    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> otherPlayers, int playerNum) throws IOException {
        this.client = clientSocket;
        this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.toClient = new PrintWriter(client.getOutputStream(),true);
        this.name = playerNum;
        this.otherPlayers = otherPlayers;
        this.attempts = GameConfiguration.guessNumber;
    }



    @Override
    public void run() {
        UserText.intro(toClient, name, this);
        String response = "";
        try {
            while (true) {

                if (fromClient.ready()) {
                    response = fromClient.readLine();
                    if(response.contains("SAY")) {
                        int firstSpace = response.indexOf(" ");
                        if(firstSpace != -1){
                            String msg = response.substring(firstSpace +1);
                            System.out.println("[Server]" + " user#" + name + ": " + msg);
                            sayToLobby(msg);
                        }

                    }
                    if(response.equals("QUIT")) {
                        closeConnection = true;
                    }
                    if (isStartGamePrompt) {
                        if (!response.equals("Y")) {
                            UserText.newGamePrompt(toClient, this);

                        } else {
                            if(!ServerMain.isGameStarted()) {
                                System.out.println("[Server]" + " user#" + name + " has started a new game");
                                playerAcceptedGame = true;
                                ServerMain.resetGame();
                            }
                            playerAcceptedGame = true;
                            System.out.println("[Server] Game for user#" + name + " is getting set");
                            toClient.println("\nGenerating secret code ...");

                        }
                    } else if (isGuessPrompt && !response.contains("SAY")) {
                        System.out.println("[Server] Analysing user#"+ name +" response: " + response);
                        Pegs.analyseUserInput(response, this, toClient);
                        if (solved) {
                          ServerMain.declareWinner(name,(GameConfiguration.guessNumber - attempts));
                          System.out.println("[Server] user#" + name + " has won");
                        }
                    }
                    if (ServerMain.isGameStarted() && playerAcceptedGame) {
                        UserText.userPrompt(toClient, this);
                    }
                }

            }

        } catch (IOException e) {}




    }

    private void sayToLobby (String msg){
        for(ClientHandler e : otherPlayers){
            if(e.getName() != name){
                e.printToClient("[user#" + name + "]" + " " + msg);
            }
        }
    }
    public void printToClient (String s){
        toClient.println(s);
    }
    public void resetAttempts(){
        this.attempts = GameConfiguration.guessNumber;
    }

    public int getName() {
        return name;
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

    public void clientDisconnect() throws IOException {

        try {

            fromClient.close();
            toClient.close();
            client.close();
            } catch (IOException ex) {

            }

    }


}
