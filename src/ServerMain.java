
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(15);
    private static int idNum = 0;

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(6666);
        try{
            System.out.println("[Server] Server is live. Waiting for connection ....");
            while (true)  {
                Socket connectionClient = server.accept();//establishes connection
                ClientHandler connection = new ClientHandler(connectionClient, clients, idNum);
                idNum++;
                clients.add(connection);
                System.out.println("[Server] Client Connected");
                pool.execute(connection);




            }

        }catch(Exception e){System.out.println(e);}
    }

    public static void resetGame(){

    }
}
