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
    private static ExecutorService pool = Executors.newFixedThreadPool(26);
    private static int idNum = 0;

    private static String secretCode;
    private static boolean gameStarted = false;
    private static ServerSocket server ;
    private static Thread safety;

    public static void main(String[] args) throws IOException{
       runItUp();
    }
    public static void runItUp() throws IOException {
            server = new ServerSocket(6666);
            ServerRunProtection theProtector = new ServerRunProtection();
            safety = new Thread(theProtector);
            safety.start();
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
            try {
                safety.interrupt();
            }catch (Exception ex){
                System.out.println("Protector Thread stopped");
            }
            idNum = 0;
            cleanClientList();
        }
        finally {
            System.out.println("[Server] Fok it we ball!");
            runItUp();

        }
    }

    public static void closeTheServer () {
        System.out.println("[Server] Server is shutting down...");
        System.out.println("[Server] Disconnecting clients...");
            for (ClientManager e : clients) {
                e.printToClient("Server is shutting down...");
                e.printToClient("SHUTDOWN");
                e.clientDisconnect();

            }
            System.exit(0);


    }
    public static boolean thereIsSomeone (){
        for (ClientManager e : clients) {
           if(!e.isSolved() && (e.getAttempts() > 1) && e.isPlayerAcceptedGame()){
               return true;
           }
        }
        return false;
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
        
        for (ClientManager e : clients) {
            e.printToClient("Game Over");
            e.printToClient( "user#" + userNum + " has guessed the secret code in " + attemptsUsed + " attempt(s)");
            e.printToClient("Are you ready for another game? (Y/N): ");
            e.resetAttempts();
            e.setPlayerAcceptedGame(false);
            e.setStartGamePrompt(true);
            e.setSolved(false);
            e.setGuessPrompt(false);
        }
        gameStarted = false;

    }

    public static void everyoneLost (){
        for (ClientManager e : clients) {
            e.printToClient("Game Over");
            e.printToClient("All users out of guesses");
            e.printToClient("Are you ready for another game? (Y/N): ");
            e.resetAttempts();
            e.setPlayerAcceptedGame(false);
            e.setStartGamePrompt(true);
            e.setSolved(false);
            e.setGuessPrompt(false);
        }
        gameStarted = false;
    }
    public static boolean isGameStarted (){
        return gameStarted;
    }

    public static void cleanClientList () {
        for (ClientManager e : clients) {
            if (e.isCloseConnection()) {
                System.out.println("[Server]" + " user#" + e.getClientID() + " has disconnected");
                e.clientDisconnect();
                clients.remove(e);

            }
        }

    }

}


