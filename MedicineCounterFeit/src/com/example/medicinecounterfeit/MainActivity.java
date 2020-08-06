package com.example.medicinecounterfeit;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import mymessage.LoginReq;
import mymessage.LoginRes;
import mymessage.MMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText loginame,password,serverip;
	Button loginb;
	
	
    public String srvip;
	
	public String usern;
	
	public String pa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		serverip = (EditText)findViewById(R.id.sip);
		loginame = (EditText)findViewById(R.id.un);
		password = (EditText)findViewById(R.id.pw);
		loginb = (Button)findViewById(R.id.login);
		
		loginb.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
	        	 
	        	 handleLoginClick();
	        	
	         }
	      });
		
	}

	public void sendMessage(MMessage m,ObjectOutputStream out)
	{
	        try
	         {
	            out.writeObject(m);
	            out.flush();

	           // guiinst.writetolog("Sending the message:");

	         }
	         catch(Exception e)
	         {
	             e.printStackTrace();

	         }


	}
	
	class ResponseHandler extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
	
			try
			{
				
				Socket socket;
				ObjectOutputStream out;

				ObjectInputStream in;
				
				InetAddress serverAddr = InetAddress.getByName(arg0[0]);
				
				socket = new Socket(serverAddr, 5000);
				
				out = new ObjectOutputStream(socket.getOutputStream());
		        in = new ObjectInputStream(socket.getInputStream());
		        LoginReq lreq = new LoginReq();
		        lreq.username = arg0[1];
		        lreq.pwd = arg0[2];
		        
		        
		        //Log.i("Hotel","Sent LoginMessage with username:" + lreq.username + ":" + lreq.pwd); 
		        sendMessage(lreq,out);
		        
		        
			    MMessage m = (MMessage)in.readObject();
			    
			    //Toast toast=Toast.makeText(getApplicationContext(),"response recieved" + m.type,Toast.LENGTH_SHORT); 
			    
			    if (m.type==2)
			    {
			    	LoginRes lres = (LoginRes)m;
			    	
			    	//toast=Toast.makeText(getApplicationContext(),"Login response recieved with status=" + lres.status,Toast.LENGTH_SHORT); 
			    	if (lres.status==0)
			    	{
			    		socket.close();
			    					    
					    return "1";
			    		
			    	}
			    	else
			    	{
			    		socket.close();
			    		
					    return "0";
			    		
			    	}
			    	
			    }
			    
			
			}
			catch(Exception ex)
			{
				
				//Log.i("Hotel","Error:" + ex);
			}
			
			return "0";
			
			
			
		}
		
		 @Override
	     protected void onPostExecute(String result) {
	            
	        	// moving to next activity
	        	if (result.equals("1"))
	        	{
	        		handleSuccess();
	        	}
	        	else
	        	{
	        		handleFail();
	        	}
	        	
	     }
	}
	
	public void handleSuccess()
	{
		Intent i = new Intent(getApplicationContext(), ScanActivity.class);
    	i.putExtra("username", usern);
    	i.putExtra("serverip", srvip);
    	startActivity(i);
    	
	}
	public void handleFail()
	{
		Toast.makeText(MainActivity.this,
				"Login Failed,relogin", Toast.LENGTH_SHORT).show(); 
		
		
	}
	
	public void handleLoginClick()
	{
		
		srvip = serverip.getText().toString();
		
		usern = loginame.getText().toString();
		
		pa = password.getText().toString();
		
		
		try
		{
			
	       
	        ResponseHandler rs = new ResponseHandler();
	        
	        rs.execute(srvip,usern,pa);
	        
	        
	        
		}		
		catch(Exception ex)
		{
			//Log.i("BlindShopping","Error:" + ex); 
			Toast toast=Toast.makeText(getApplicationContext(),"Failing to connect to server",Toast.LENGTH_SHORT);  
		    toast.show();
		}
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
