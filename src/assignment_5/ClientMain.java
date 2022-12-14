/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment_5;

import java.io.*;
import java.net.*;

public class ClientMain {
private static Socket  connectedToServer;
private static CommunicationFromServerProcessing communication;
private static BufferedReader keyboard;
private static PrintWriter toServer;

private static InputStreamReader x;

    public static void main(String[] args) {

        try{

             connectedToServer = new Socket("localhost",6666);
             communication = new CommunicationFromServerProcessing(connectedToServer);
             x = new InputStreamReader(System.in);
             keyboard = new BufferedReader(x);
             toServer = new PrintWriter(connectedToServer.getOutputStream(),true);
            new Thread(communication).start();

            while (true){
                if(keyboard.ready()) {
                    String clientInput = keyboard.readLine();
                    toServer.println(clientInput);
                    if (clientInput.equals("QUIT")) break;
                }
            }
            closeClient();
        }catch(Exception e){System.out.println(e);}
    }

    public static void closeClient(){
       try {
           communication.endSession();
           connectedToServer.close();
           toServer.close();
           keyboard.close();
           x.close();
           System.exit(0);

       }catch (IOException e){
            System.out.println("IN THE CATCH");
       }

    }

}
