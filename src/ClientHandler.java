import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import assignment2_Network_Modification.*;

public class ClientHandler implements Runnable {
    private Socket client;
    private volatile BufferedReader in; // Read from the Client
    private volatile PrintWriter out; // Write to the Client
    private int attempts;
    private int name;
    private ArrayList <ClientHandler> serverPlayers;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> otherPlayers, int playerNum) throws IOException {
        this.client = clientSocket;
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = new PrintWriter(client.getOutputStream(),true);
        this.name = playerNum;
        this.serverPlayers = otherPlayers;
        this.attempts = GameConfiguration.guessNumber;
    }
    @Override
    public void run() {




    }


}
