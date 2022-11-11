import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import assignment2_Network_Modification.*;

public class ClientHandler implements Runnable {
    private Socket client;
    private volatile BufferedReader in; // Read from the Client
    private volatile PrintWriter out; // Write to the Client
    private Game clientGame;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
    }
    @Override
    public void run() {
        try {
            UserText.intro(out);
            UserText.gameStartResponse(in, out, this.client);
            while (true) {
                Game clientGame = new Game(true, out);
                clientGame.runGame(in, out);
                UserText.newGamePrompt(out);
                UserText.gameStartResponse(in, out, client);
            }
        }catch (IOException e){
            System.err.println(e);
        }



    }


}
