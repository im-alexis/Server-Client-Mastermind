/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment2_Network_Modification;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(15);
    private static int idNum = 0;

    private static String secretCode;
    private static boolean gameStarted = false;

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(6666);
        try{
            System.out.println("[Server] Server is live. Waiting for connection ....");
            while (true)  {
                Socket connectionClient = server.accept();//establishes connection
                ClientHandler connection = new ClientHandler(connectionClient, clients, idNum);
                System.out.println("[Server] user#" + idNum + " Connected");
                idNum++;
                clients.add(connection);
                System.out.println("[Server] Waiting on new connection...");
                pool.execute(connection);
                for (ClientHandler e : clients){
                    if(e.isCloseConnection()){
                        System.out.println("user#"+ e.getName() + " has disconnected");
                        clients.remove(e);

                    }
                }
            }

        }catch(Exception e){
            System.out.println(e);
            System.out.println("There is an error");
        }
        finally {
            System.out.println("Fok it we ball");

        }
    }

    public static void resetGame(){
        if(!gameStarted) {
            secretCode = SecretCodeGenerator.getInstance().getNewSecretCode();
            System.out.println("[Server] The code is: " + secretCode);
            gameStarted = true;
        }

    }
    public static String getSecretCode(){
        return secretCode;
    }

    public static void declareWinner (int userNum, int attemptsUsed){
        gameStarted = false;
        for (ClientHandler e : clients) {
            e.printToClient("Game Over");
            e.printToClient( "user#" + userNum + " has guessed the secret code in " + attemptsUsed);

        }

    }
    public static boolean isGameStarted (){
        return gameStarted;
    }
}
