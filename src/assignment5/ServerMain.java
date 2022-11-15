/* EE422C Assignment #5 submission by
 * Alexis Torres
 * at39625
 */

package assignment5;

import java.io.*;
import java.util.ArrayList;


public class ServerMain {


    private static BufferedReader keyboard;
    private static InputStreamReader x;


    public static void main(String[] args) {
        x = new InputStreamReader(System.in);
        keyboard = new BufferedReader(x);
        ServerFunctionality server = new ServerFunctionality();
        new Thread(server).start();
        try {
            while (true) {
                String serverGuardian = keyboard.readLine();
                if (serverGuardian.equals("QUIT")) {
                    System.out.println("[Server] Are you sure (Y/N)?");
                    serverGuardian = keyboard.readLine();
                    if (serverGuardian.equals("Y")) {
                        ServerFunctionality.closeTheServer();
                        break;
                    }
                }
                else {
                    System.out.println("[Server] " + serverGuardian + " is not a vaild command");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }



}


