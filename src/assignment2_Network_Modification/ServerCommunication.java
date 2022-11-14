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

    public void endSession ()  {
        System.out.println("Disconnecting Goodbye ....");
        System.out.println("Come again!");
        try {
            fromServer.close();
            server.close();
            fromServer.close();
        } catch (IOException e) {
            System.out.println("ServerCommunication reader OR writer or ServerConnection already closed");
        }
    }

    @Override
    public void run() {
        String serverResponse = "";
            try {
                while (true) {
                    if(fromServer.ready()) {
                        serverResponse = fromServer.readLine();
                        if (serverResponse == null) break;
                        if (!serverResponse.equals("") && !serverResponse.contains("[user#")) {
                            System.out.println("[Server] " + serverResponse);
                            if (serverResponse.contains("Are you ready") || serverResponse.contains("Enter guess:")) {
                                System.out.print("> ");
                                readyForInput = true;

                            }
                        }else if (serverResponse.contains("[user#")) {
                            System.out.println("\n" + serverResponse);
                            System.out.print("> ");
                        }
                        else {
                            System.out.println(serverResponse);
                            readyForInput = false;
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Couldn't Connect....");
            }

        }


}


