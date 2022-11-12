
import java.io.*;
import java.net.*;

public class ClientMain {

    public static void main(String[] args) {

        try{
            Socket connectedToServer = new Socket("localhost",6666);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(connectedToServer.getInputStream()));
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter toServer = new PrintWriter(connectedToServer.getOutputStream(),true);
            while (true){


            }


            //connectedToServer.close();

        }catch(Exception e){System.out.println(e);}
    }
}
