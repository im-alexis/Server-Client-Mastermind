import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerCommunication implements Runnable{

    private Socket client;
    private volatile BufferedReader fromServer; // Read from the Server
    private volatile PrintWriter toServer; // Write to the Server

    public ServerCommunication(){


    }

    @Override
    public void run() {

    }
}
