


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bala
 */
public class Server extends Thread {

    

    ServerSocket serverSocket;


    int listenport;

    Home guiinst;

    void init(int lp,Home cp)
    {
        listenport = lp;
        guiinst = cp;
        try {
            serverSocket = new ServerSocket(listenport);
        } catch (IOException e) {
            System.err.println("Could not listen on port:" +listenport);
            System.exit(1);
        }


    }
    
   

    

    public void run()
    {

        try
        {

            guiinst.writetolog("Counter feit Server started...");
            while(true)
            {
                Socket clientSocket = null;

                clientSocket = serverSocket.accept();

                guiinst.writetolog(" Recieved the connection from Client ");


                HandleClient hc = new HandleClient(clientSocket,guiinst);

                hc.start();

                
                
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        
        
    }

}
