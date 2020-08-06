

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import mymessage.LoginReq;
import mymessage.LoginRes;
import mymessage.MMessage;
import mymessage.VerifyReq;
import mymessage.VerifyRes;




/**
 *
 * @author bala
 */
public class HandleClient extends Thread {

    Socket clientSocket = null;
    
    String nodeid;
    
    Home guiinst;

    ObjectInputStream in;
    ObjectOutputStream out;

    String drivetostore;
    
    HandleClient(Socket sc,Home g)
    {
       clientSocket = sc;

       guiinst = g;

  
       
    }

    

     void sendMessage(MMessage m)
    {


         try
         {
            out.writeObject(m);
            out.flush();

            //guiinst.writetolog("Sending the  message");

         }
         catch(Exception e)
         {
             e.printStackTrace();

         }

    }


       public static String readFileAsString(String filePath)
   {

        try
        {
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(
            new FileReader(filePath));
            char[] buf = new char[1024];
            int numRead=0;
            while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
            }
            reader.close();
            return fileData.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
       
    public void handleLogin(LoginReq lre)
    {
        try
        {
            System.out.println("Recived login req from " + lre.username);
           Database db = new Database();
           String q = "select * from dealer where did='" + lre.username + "' and dpass='" + lre.pwd + "'";
           ResultSet rs = db.executeQuery(q);
           LoginRes lres = new LoginRes();
           
           if (rs.next())
           {
                lres.status=0;
               
               
           }
           else
           {
               lres.status=1;
               
           }
           sendMessage(lres);
            
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        
    }

    public void handleVerify(VerifyReq vr,String ipaddr)
    {
        long st = Calendar.getInstance().getTimeInMillis();
           
          String [] parts = vr.scancode.split(":");
          
          VerifyRes vres = new VerifyRes();
          vres.status = "INVALID MEDICINE";
          
           String uid = parts[0];
           String mediname = parts[1];
           
           String fromplace = ipaddr;
           Date today = new Date(); 
                   GregorianCalendar  cal = new GregorianCalendar();
                   cal.setTime(today);
                   
                   int month = cal.get(Calendar.MONTH);
                   int year = cal.get(Calendar.YEAR);
                   
                   int curmy = year*12+month;
                   
                   
                   int dayOfWeek = cal.get(Calendar.DATE);
                   int dayOfMonth = cal.get(Calendar.MONTH)+1;
                   int dayOfYear = cal.get(Calendar.YEAR); 
                   int hh=cal.get(Calendar.HOUR_OF_DAY);
                   int mm=cal.get(Calendar.MINUTE);
                   
                   String daystr = dayOfWeek +"/" +dayOfMonth + "/" +dayOfYear;
                   
                   System.out.println("Day is "+ daystr);
                   String timestr = hh + ":" + mm;
           try
           {
               Database db = new Database();
               
               String qm = "select * from meddetail where medicinename='" + mediname + "'";
               ResultSet rs2 = db.executeQuery(qm);
               
               if (rs2.next())
               {
                    String key = rs2.getString("keytouse");
                    try
                    {
                    uid=AESUtil.decrypt(uid, key);
                    }
                    catch(Exception ex)
                    {
                          
                    }
                    String q = "select * from lotinfo where lotid='" + uid + "'";
                    ResultSet rs = db.executeQuery(q);
                    if (rs.next())
                    {
                        String med = rs.getString("medicinename");
                        String my=rs.getString("expirydate");
                        parts = my.split("-");
                        int m = Integer.parseInt(parts[0]);
                        int y = Integer.parseInt(parts[1]);
                        int exmy = y*12+m;
                        if (curmy<=exmy)
                        {
                            vres.status = "VALID MEDICINE";
                            
                        }
                        else
                        {
                            // have to add to statistics
                            String iq = "insert into statistics values('" + med + "','" + fromplace + "','" + daystr + "','" + timestr + "','EXPIRED')";
                            db.executeUpdate(iq);                                                   

                            vres.status = "EXPIRED MEDICINE";
                           
                        }                                                                                                   

                    }

                    else
                    {
                        String iq = "insert into statistics values('" + mediname + "','" + fromplace + "','" + daystr + "','" + timestr + "','INVALID')";
                        db.executeUpdate(iq);  
                         // have to add to statistics
                         vres.status = "INVALID MEDICINE";
                        
                    }
                   
               }
               else
               {
                        vres.status = "INVALID MEDICINE";
                    
                   
               }
               
               db.close();
        
           }
           catch(Exception ex)
           {
               ex.printStackTrace();
               
           }
           sendMessage(vres);
           long et = Calendar.getInstance().getTimeInMillis();   
           long diff = et-st;
           String ct = "P#" + diff;
           FileAppender.AppendtoFile("Perfg1.txt",ct);
           
        
    }
    public void run()
    {
        try
        {


            //guiinst.writetolog("Client handler started");

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            System.out.println(" Got input stream");

            

            String ip=(((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");

            while (true)
            {

               System.out.println(" Waiting for messages");

               MMessage m = (MMessage)in.readObject();
               
               switch(m.type)
               {
                   case 1:
                   {
                       LoginReq lre = (LoginReq)m;
                       handleLogin(lre);
                       
                       
                       break;
                   }
                   case 3:
                   {
                       VerifyReq vr = (VerifyReq)m;
                       handleVerify(vr,ip);
                       
                   }
                   
                   
                   
               }



            }

        }
        catch(Exception e)
        {
           guiinst.writetolog("!!! Detected error ");

           e.printStackTrace();





        }
            


    }

}
