package assignment5;

import java.util.Scanner;

public class ServerRunProtection implements Runnable{
    Scanner keyboard;
    public ServerRunProtection (){
        keyboard = new Scanner(System.in);
    }
    @Override
    public void run() {
        try {
            while (true){
            String serverGuardian = keyboard.nextLine();
                if (serverGuardian.equals("QUIT")) {
                   System.out.println("[Server] Are you sure (Y/N)?");
                   serverGuardian = keyboard.nextLine();
                   if(serverGuardian.equals("Y")){
                    ServerMain.closeTheServer();
                   }
                }
                System.out.println("[Server] " + serverGuardian + " is not a vaild command");
            }
        }catch (Exception e){}

    }
}
