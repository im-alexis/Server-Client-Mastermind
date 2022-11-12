/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment2_Network_Modification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerCommunication implements Runnable{

    private Socket server;
    private volatile BufferedReader fromServer; // Read from the Server

    public ServerCommunication(Socket s) throws IOException {
        this.server = s;
        this.fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

    }

    public void endSession () throws IOException {
        fromServer.close();
    }

    @Override
    public void run() {
        String serverResponse;
            try {
                while (true) {
                    serverResponse = fromServer.readLine();

                    if(serverResponse == null) break;

                    //System.out.println("[Server]: " + serverResponse);
                    System.out.println("[Server] "+serverResponse);
                }
            } catch (IOException e) {
                System.out.println("Could not read from server or Disconnected");
            }finally {
                try {
                    fromServer.close();
                } catch (IOException e) {
                    System.out.println("assignment2_Network_Modification.ServerCommunication reader already closed");
                }
            }

        }

    }
