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
    private ArrayList <ClientHandler> serverPlayers;
    private ArrayList <String> clientHistory = new ArrayList<>();
    private int attempts;
    private final int name;
    private  boolean closeConnection = false;
    private boolean isStartGamePrompt = false;
    private boolean isGuessPrompt = false;
    private boolean solved = false;


    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> otherPlayers, int playerNum) throws IOException {
        this.client = clientSocket;
        this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.toClient = new PrintWriter(client.getOutputStream(),true);
        this.name = playerNum;
        this.serverPlayers = otherPlayers;
        this.attempts = GameConfiguration.guessNumber;
    }



    @Override
    public void run() {
        UserText.intro(toClient, name, this);
        String response;
        try {
            while (true) {
                response = fromClient.readLine();
                if (isStartGamePrompt){
                    if (!response.equals("Y")) {
                        clientDisconnect();
                        break;
                    } else {
                        ServerMain.resetGame();
                        UserText.userPrompt(toClient, this);
                    }
                }
                if (isGuessPrompt){

                }
                            }

        } catch (IOException e) {
            System.out.println("Could not read from user#" + name);
        }


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

    public void clientDisconnect() throws IOException {
        toClient.println("Disconnecting Goodbye ....");
        fromClient.close();
        toClient.close();
        if(closeConnection){
            client.close();
        }

    }


}
