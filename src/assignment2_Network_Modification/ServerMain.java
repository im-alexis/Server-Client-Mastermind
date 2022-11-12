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
    private static boolean gameHasStarted = false;

    public static void main(String[] args) throws IOException{
        ServerSocket server = new ServerSocket(6666);
        try{
            System.out.println("[Server] Server is live. Waiting for connection ....");
            while (true)  {
                Socket connectionClient = server.accept();//establishes connection
                ClientHandler connection = new ClientHandler(connectionClient, clients, idNum);
                System.out.println("[Server] Client [" + idNum + "] Connected");
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

        }catch(Exception e){System.out.println(e);}
    }

    public static void resetGame(){
        if(!gameHasStarted) {
            secretCode = SecretCodeGenerator.getInstance().getNewSecretCode();
            System.out.println("[Server]: The code is:" + secretCode);
            gameHasStarted = true;
        }

    }
    public static String getSecretCode(){
        return secretCode;
    }

    public static String declareWinner (int userNum, int attemptsUsed){
        gameHasStarted = false;
        return "user#" + userNum + " has guessed the secret code in " + attemptsUsed;
    }
}
