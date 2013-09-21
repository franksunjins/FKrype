package frank.sit.fkrype;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	String inputfile="1input.txt";
	String inputPassword="1password.txt";
	File root = Environment.getExternalStorageDirectory();
	String[] mFileList;
	File mPath = new File(root.getAbsolutePath());
	String mChosenFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//create input and password file
		
		try{
			File dir = new File (root.getAbsolutePath() + "/FKrype");
			dir.mkdirs();
			FileOutputStream initPassword=new FileOutputStream(new File(dir, inputPassword));
			OutputStreamWriter wr=new OutputStreamWriter(initPassword);
			wr.write("input password");
			wr.flush();
			initPassword=new FileOutputStream(new File(dir, inputfile));
			wr=new OutputStreamWriter(initPassword);
			wr.write("franksunjin you are a fucking idiot input password");
			wr.close();
		}catch(Exception e){System.out.println(e.getStackTrace().toString());}
//		File file = new File(getString(R.string.dir), inputfile);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.item1:
	            openQuit();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void openQuit() {
		finish();	
	}
	public void SelectFile(View view) {
		this.onCreateDialog(R.id.textView1);
	}
	public void SelectFile2(View view) {
		this.onCreateDialog(R.id.TextView01);
	}
	public void fEncrype(View view)
	throws Exception{
		File dir = new File (root.getAbsolutePath() + "/FKrype");
		//File input, eoutput,doutput;
		String textPassword="111111";
		//EditText e1=((EditText)findViewById(R.id.editText1));
		TextView t2=((TextView)findViewById(R.id.TextView01));
		TextView t1=((TextView)findViewById(R.id.textView1));
		inputfile=t1.getHint().toString();
		RadioButton r2=(RadioButton)findViewById(R.id.radio21);
		byte[] dataBytes = new byte[1024];
		 //       byte[] cB = new byte[1024];
		int nread = 0; 
		AES a=new AES();
		String data;
		String output;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		if(r2.isChecked()){//using text password
			textPassword=((EditText)findViewById(R.id.editText1)).getText().toString();
			dataBytes=AES.static_stringToByteArray(textPassword);
        	md.update(dataBytes, 0, dataBytes.length);
		}else if(t2.getHint().toString()=="Open a file"){//using password file
			inputPassword=t2.getHint().toString();
        FileInputStream fis = new FileInputStream(inputPassword);//password file
        while ((nread = fis.read(dataBytes)) != -1) {
        		md.update(dataBytes, 0, nread);
        }
        fis.close();}
        byte[] mdbytes = md.digest();
        
        data  = new Scanner(new File(inputfile)).useDelimiter("\\Z").next();
        a.setKey(mdbytes);
        //AES.self_test(mdbytes, "franksunjin111111112321juindsja", "franksunjin111111112321juindsja", 1);
        output=a.Encrypt(data);
        //String de=a.Decrypt(output);
        try{
        	FileOutputStream initPassword=new FileOutputStream(new File(dir+"/", "encrypted.txt"));
			OutputStreamWriter wr=new OutputStreamWriter(initPassword);
			wr.write(output);
			wr.flush();
			/*
			initPassword=new FileOutputStream(new File(dir+"/", "decrypted.txt"));
			wr=new OutputStreamWriter(initPassword);
			wr.write(de);
			wr.flush();
			*/
			wr.close();
    	}catch(Exception e){e.printStackTrace();}
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Encrypte compelet", duration);
        toast.show();
        }
	
	
	
//	private static final String FTYPE = ".txt";    
	private void loadFileList() {
	    try {
	        mPath.mkdirs();
	    }
	    catch(SecurityException e) {
	        Log.e("2", "unable to write on the sd card " + e.toString());
	    }
	    if(mPath.exists()) {
	        mFileList = mPath.list();
	        //test toast!
	        
	    }
	    else {
	        mFileList= new String[0];
	    }
	}

	protected Dialog onCreateDialog(final int textid) {
		mPath = new File(root.getAbsolutePath());
		loadFileList();
	    Dialog dialog = null;
	    dialog=fileDialog(textid);
	    Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "Select your file", duration);
        toast.show();
	    
	       
    	    	
	            
	    //builder.setMessage("hellow");
	    //dialog = builder.show();
	    return dialog;
	}
	public Dialog fileDialog(final int textid) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Choose your file");
         if(mFileList == null) {
             Log.e(ACCESSIBILITY_SERVICE, "Showing file picker before loading the file list");
             dialog = builder.create();
             builder.setMessage("no file");
             return dialog;
         }
         
         	//builder.setMessage("we got file");
         builder.setItems(mFileList, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) {
             	//if we choose a dir, we do it again
             		mChosenFile = mFileList[which];
             		mPath = new File (mPath.getAbsolutePath() + "/"+mChosenFile);
	    	if(!mPath.isDirectory()){
	    		TextView text1 = (TextView) findViewById(textid);
	    		text1.setText(mChosenFile);
	    		text1.setHint(mPath.getAbsolutePath());
	    	}
	    	loadFileList();
	    	dialog=fileDialog(textid);
         	    	}
            	});
         dialog = builder.show();
         
	    	return dialog;
	}
	public void clear(View view) {
		mPath = new File(root.getAbsolutePath());
		TextView text1 = (TextView) findViewById(R.id.textView1);
		text1.setText("");
		EditText e1=(EditText)findViewById(R.id.editText1);
		e1.setText("");
	}
}



