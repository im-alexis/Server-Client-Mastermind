
import java.io.*;
import java.net.*;

public class MyClient {

    public static void main(String[] args) {

        try{
            Socket connectedToServer = new Socket("localhost",6666);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(connectedToServer.getInputStream()));
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter toServer = new PrintWriter(connectedToServer.getOutputStream(),true);
            while (true){
                boolean didItPrint = false;
                boolean isAResult = false;
                boolean isAPrompt = false;
                while (fromServer.ready()) {
                    String responseFromServer = fromServer.readLine();
                    System.out.println(responseFromServer);
                    didItPrint = true;
                    if(responseFromServer.contains("-> Result:")){
                       isAResult = true;
                    }
                    if(responseFromServer.contains("Are you ready for another game? (Y/N):")){
                        isAPrompt = true;
                    }
                }
                if (didItPrint && !isAResult && !isAPrompt) {
                    System.out.print("\r");
                    String reply = keyboard.readLine();
                    toServer.println(reply);
                }
                if (isAPrompt) {
                    System.out.print("\r");
                    String reply = keyboard.readLine();
                    toServer.println(reply);
                }

            }


            //connectedToServer.close();

        }catch(Exception e){System.out.println(e);}
    }
}
