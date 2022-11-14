/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment5;

import java.io.*;
import java.net.*;

public class ClientMain {


    public static void main(String[] args) {

        try{
            Socket connectedToServer = new Socket("localhost",6666);
            ServerCommunication communication = new ServerCommunication(connectedToServer);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter toServer = new PrintWriter(connectedToServer.getOutputStream(),true);
            new Thread(communication).start();

            while (true){

                String clientInput = keyboard.readLine();
                toServer.println(clientInput);
                if (clientInput.equals("QUIT")) break;
            }

            keyboard.close();
            toServer.close();
            communication.endSession();
            connectedToServer.close();
            System.exit(0);

        }catch(Exception e){System.out.println(e);}
    }
}
