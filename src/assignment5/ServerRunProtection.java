package assignment5;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServerRunProtection implements Runnable {
    private static BufferedReader keyboard;
    private static InputStreamReader x;

    public ServerRunProtection() {
        x = new InputStreamReader(System.in);
        keyboard = new BufferedReader(x);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String serverGuardian = keyboard.readLine();
                if (serverGuardian.equals("QUIT")) {
                    System.out.println("[Server] Are you sure (Y/N)?");
                    serverGuardian = keyboard.readLine();
                    if (serverGuardian.equals("Y")) {
                        ServerMain.closeTheServer();
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


