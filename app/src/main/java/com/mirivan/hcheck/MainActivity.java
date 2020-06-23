package com.mirivan.hcheck;
 
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.lang.Class;
import java.io.IOException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.TextView;

public class MainActivity extends Activity { 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("HID Checker");
        
        MaterialButton chmod_btn = findViewById(R.id.chmod_btn);
        MaterialButton duk_btn = findViewById(R.id.duk_btn);
        MaterialButton drd_btn = findViewById(R.id.drd_btn);
        MaterialButton close_btn = findViewById(R.id.close_btn);
        
        OnClickListener init = new OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()) {
                    case R.id.chmod_btn:
                        try {
                            Process sudo = Runtime.getRuntime().exec("su");
                            sudo.getOutputStream().write("chmod 666 /dev/hidg*".getBytes());
                        } catch(IOException e){
                            Toast toast = Toast.makeText(getApplicationContext(), 
                                                         "Failed to initialize chmod.", Toast.LENGTH_SHORT); 
                            toast.show(); 
                        }
                        break;
                    case R.id.duk_btn:
                        String duk_link = "https://f-droid.org/app/remote.hid.keyboard.client";
                        Intent duk_intent = new Intent(Intent.ACTION_VIEW);
                        duk_intent.setData(Uri.parse(duk_link));
                        startActivity(duk_intent);
                        break;
                    case R.id.drd_btn:
                        String drb_link = "https://f-droid.org/app/com.mayank.rucky";
                        Intent drb_intent = new Intent(Intent.ACTION_VIEW);
                        drb_intent.setData(Uri.parse(drb_link));
                        startActivity(drb_intent);
                        break;
                    case R.id.close_btn:
                        System.exit(0);
                }
            }
        };
        duk_btn.setOnClickListener(init);
        drd_btn.setOnClickListener(init);
        chmod_btn.setOnClickListener(init);
        close_btn.setOnClickListener(init);

        try {
            Runtime.getRuntime().exec("su");
        } catch(IOException e){
            Toast toast = Toast.makeText(getApplicationContext(), 
                                         "Failed to initialize su.", Toast.LENGTH_SHORT); 
            toast.show(); 
        }
     checkSupport();
  }
public void checkSupport(){

    TextView hid_supported = findViewById(R.id.hid_supported);
    TextView fs_supported = findViewById(R.id.fs_supported);
    
    if (new File("/dev/hidg0").exists() && new File("/dev/hidg1").exists()){
        hid_supported.setText("Supported");
    }
    else{
        if (new File("/config/usb_gadget/g1").exists()){
            hid_supported.setText("You can start HID with USB Arsenal");
        }
        else{
            hid_supported.setText("Unsupported");
        }
    }
    
    if (new File("/config/usb_gadget/g1").exists()){
        fs_supported.setText("Supported");
    }
    else{
        fs_supported.setText("Unsupported");
    }
  }
}
