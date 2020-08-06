package com.example.medicinecounterfeit;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import mymessage.LoginReq;
import mymessage.LoginRes;
import mymessage.MMessage;
import mymessage.VerifyReq;
import mymessage.VerifyRes;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ScanActivity extends Activity {

	Button scan;
	TextView sresult;
	Button logoff;
	
	static ScanActivity inst;
	String sip;
	String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
	 
		username=getIntent().getExtras().getString("username");
		sip = getIntent().getExtras().getString("serverip");
		
		inst=this;
		scan = (Button)findViewById(R.id.scanQR);
		sresult = (TextView)findViewById(R.id.scanresult);
		logoff = (Button)findViewById(R.id.logoff);
		
		scan.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
	        	 
	        	 Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        	 intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	        	 startActivityForResult(intent, 0);
	         }
	      });
		
		logoff.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
	        	 
	        	Intent i = new Intent(getApplicationContext(), MainActivity.class);
	            startActivity(i);
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
		        VerifyReq lreq = new VerifyReq();
		        lreq.username = arg0[1];
		        lreq.scancode = arg0[2];
		        
		        
		        //Log.i("Hotel","Sent LoginMessage with username:" + lreq.username + ":" + lreq.pwd); 
		        sendMessage(lreq,out);
		        
		        
			    MMessage m = (MMessage)in.readObject();
			    
			    //Toast toast=Toast.makeText(getApplicationContext(),"response recieved" + m.type,Toast.LENGTH_SHORT); 
			    
			    if (m.type==4)
			    {
			    	VerifyRes lres = (VerifyRes)m;
			    	
			    	//toast=Toast.makeText(getApplicationContext(),"Login response recieved with status=" + lres.status,Toast.LENGTH_SHORT); 
			    	
			    		socket.close();
			    					    
					    return lres.status;
			    		
			    	
			    	
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
	            
	        	
	        	sresult.setText(result);
	            //handleSuccess();
	        	
	        	
	     }
	}
	
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 0) {
          
            if (resultCode == RESULT_OK) {
            	
            	String sc = data.getStringExtra("SCAN_RESULT");
            	ResponseHandler rs = new ResponseHandler();
            	Toast.makeText(this, sc, Toast.LENGTH_LONG).show();
     	        rs.execute(sip,username,sc);
                //tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                //tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
            } else if (resultCode == RESULT_CANCELED) {
                //tvStatus.setText("Press a button to start a scan.");
                //tvResult.setText("Scan cancelled.");
            }
        }
		
		/*IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Log.d("MainActivity", "Scanned");
            	String scncon = result.getContents();
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                
            	try
        		{
        			
        	       
        	        ResponseHandler rs = new ResponseHandler();
        	        
        	        rs.execute(sip,username,scncon);
        	        
        	        
        	        
        		}		
        		catch(Exception ex)
        		{
        			Log.i("BlindShopping","Error:" + ex); 
        			Toast toast=Toast.makeText(getApplicationContext(),"Failing to connect to server",Toast.LENGTH_SHORT);  
        		    toast.show();
        		}
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }*/
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
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
