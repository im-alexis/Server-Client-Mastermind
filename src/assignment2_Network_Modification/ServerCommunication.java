/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment2_Network_Modification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerCommunication implements Runnable{

    private Socket server;
    private volatile BufferedReader fromServer; // Read from the Server

    private boolean readyForInput = false;

    public ServerCommunication(Socket s) throws IOException {
        this.server = s;
        this.fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

    }

    public void endSession () throws IOException {
        System.out.println("In endSession function");
        fromServer.close();
    }

    @Override
    public void run() {
        String serverResponse;
            try {
                while (true) {
                    if(fromServer.ready()){
                        serverResponse = fromServer.readLine();
                        if(serverResponse == null) break;
                        if(!serverResponse.equals("")) {
                            System.out.println("[Server] " + serverResponse);
                            if(serverResponse.contains("Are you ready") || serverResponse.contains("Enter guess:") ){
                                System.out.print("> ");
                                readyForInput = true;
                            }
                        }
                        else {
                            System.out.println(serverResponse);
                            readyForInput = false;
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Could not read from server or Disconnected");
            }finally {
                System.out.println("Jumped out of the loop in ServerCommunication, thread");
                try {
                    fromServer.close();
                } catch (IOException e) {
                    System.out.println("assignment2_Network_Modification.ServerCommunication reader already closed");
                }
            }

        }


}


