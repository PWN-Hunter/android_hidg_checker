package com.mirivan.hcheck;

import android.app.Activity;
import android.app.UiModeManager;
import android.app.*;
import android.os.Bundle;
import android.content.Context;
import androidx.appcompat.widget.Toolbar;
import android.view.View.*;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.io.*;
import android.widget.*;

public class DevActivity extends Activity {
	
	public boolean show_su_version = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("(Admin) HID Checker");
		
		MaterialButton chmod_btn = findViewById(R.id.chmod_btn);
		MaterialButton invalidate = findViewById(R.id.invalidate);
		MaterialButton reboot = findViewById(R.id.reboot);
		final MaterialButton su_version = findViewById(R.id.su_version);
		final TextView output = findViewById(R.id.output);
		final TextInputEditText rules = findViewById(R.id.rules);
		final String chmod = "chmod " + rules.getText().toString();
		
		OnClickListener click = new OnClickListener(){
			public void onClick(View v){
				switch (v.getId()){
					case R.id.chmod_btn:
						try {
							Process sudo = Runtime.getRuntime().exec("su");
							sudo.getOutputStream().write(chmod.getBytes());
							Snackbar.make(v, R.string.repermed, Snackbar.LENGTH_LONG).setAction(R.string.ok, null).show();
						} catch(IOException e){
							Snackbar.make(v, R.string.failed_init_chmod, Snackbar.LENGTH_LONG).show();
					    }
					break;
					case R.id.invalidate:
						((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
					break;
					case R.id.reboot:
						try {
						Process sudo = Runtime.getRuntime().exec("su");
						sudo.getOutputStream().write("reboot".getBytes());
						} catch(IOException e) {
						Snackbar.make(v, R.string.failed_reboot, Snackbar.LENGTH_LONG).show();
						}
					break;
					case R.id.su_version:
						if (show_su_version) { 
							output.setText("");
							su_version.setText(R.string.su_version);
							show_su_version = false; 
						} else {
							output.setText(su_version());
							su_version.setText(R.string.hide_su_version);
							show_su_version = true; 
						}
					break;
			    }
		    }
	    };
		chmod_btn.setOnClickListener(click);
		invalidate.setOnClickListener(click);
		reboot.setOnClickListener(click);
		su_version.setOnClickListener(click);
    }
	public String su_version() {
		try {
			Process process = Runtime.getRuntime().exec("su -v");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer out = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
            out.append(buffer, 0, read);
            }
            reader.close();
            return out.toString();
		} catch (IOException e) {
            throw new RuntimeException(e);
	}
   }
	private void setContentView(){
		UiModeManager ui = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

        switch (ui.getNightMode()) {
            case ui.MODE_NIGHT_NO:
                setTheme(R.style.DAY);
				setContentView(R.layout.activity_dev);
                break;
            case ui.MODE_NIGHT_YES:
                setTheme(R.style.NIGHT);
				setContentView(R.layout.activity_dev);
				break;
        }
	}
}
