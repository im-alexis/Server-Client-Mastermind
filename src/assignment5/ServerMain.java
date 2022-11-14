/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment5;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static ArrayList<ClientManager> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(15);
    private static int idNum = 0;

    private static String secretCode;
    private static boolean gameStarted = false;

    public static void main(String[] args) throws IOException{
       runItUp();
    }
    public static void runItUp() throws IOException {
        ServerSocket server = new ServerSocket(6666);
        try{
            System.out.println("[Server] Server is live......");
            while (true)  {
                System.out.println("[Server] Waiting on new connection...");
                Socket connectionClient = server.accept();//establishes connection
                ClientManager connection = new ClientManager(connectionClient, clients, idNum);
                System.out.println("[Server] user#" + idNum + " Connected");
                idNum++;
                clients.add(connection);
                pool.execute(connection);
                cleanClientList();

            }

        }catch(Exception e){
            System.out.println("[Server] AHHHHH SHIT. I GOTTA RESTART .....");
            server.close();
            cleanClientList();
            clients.clear();
        }
        finally {
            System.out.println("[Server] Fok it we ball!");
            runItUp();

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
        for (ClientManager e : clients) {
            e.printToClient("Game Over");
            e.printToClient( "user#" + userNum + " has guessed the secret code in " + attemptsUsed);
            e.printToClient("Are you ready for another game? (Y/N): ");
            e.resetAttempts();
            e.setPlayerAcceptedGame(false);
            e.setStartGamePrompt(true);
            e.setGuessPrompt(false);

        }

    }
    public static boolean isGameStarted (){
        return gameStarted;
    }

    private static void cleanClientList () {
        try {
            for (ClientManager e : clients) {
                if (e.isCloseConnection()) {
                    System.out.println("[Server]" + " user#" + e.getClientID() + " has disconnected");
                    e.clientDisconnect();
                    clients.remove(e);

                }
            }

        }catch (IOException e){

        }
    }

}


