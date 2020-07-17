package com.mirivan.hcheck;
 
import androidx.appcompat.app.AppCompatActivity;
import android.app.UiModeManager;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import java.io.*;
import android.net.Uri;
import android.view.View;
import android.view.View.*;
import android.widget.*;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView();
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.app_name);
		
		LinearLayout testing = findViewById(R.id.testing);
		LinearLayout nsuoder = findViewById(R.id.nsudoer);
		TextView hid_supported = findViewById(R.id.hid_supported);
		TextView fs_supported = findViewById(R.id.fs_supported);
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
							Snackbar.make(v, R.string.repermed, Snackbar.LENGTH_LONG).setAction(R.string.ok, null).show();
                        } catch(IOException e){
							Snackbar.make(v, R.string.failed_init_chmod, Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.duk_btn:
                        Intent duk_link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f-droid.org/app/remote.hid.keyboard.client"));
						startActivity(duk_link);
                        break;
                    case R.id.drd_btn:
						Intent drd_link = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f-droid.org/app/com.mayank.rucky"));
						startActivity(drd_link);
                        break;
                    case R.id.close_btn:
                        finish();
                    }
                }
            };
        duk_btn.setOnClickListener(init);
        drd_btn.setOnClickListener(init);
        chmod_btn.setOnClickListener(init);
        close_btn.setOnClickListener(init);
		toolbar.setOnClickListener(multi_init);
		
        try {
            Runtime.getRuntime().exec("su");
        } catch(IOException e){
            testing.setVisibility(View.INVISIBLE);
			nsuoder.setVisibility(View.VISIBLE);
        }
		if (new File("/dev/hidg0").exists() && new File("/dev/hidg1").exists()){
			hid_supported.setText(R.string.supported);
		}
		else{
			if (new File("/config/usb_gadget/g1").exists()){
				hid_supported.setText(R.string.arsenal);
			}
			else{
				hid_supported.setText(R.string.unsupported);
			}
		}

		if (new File("/config/usb_gadget/g1").exists()){
			fs_supported.setText(R.string.supported);
		}
		else{
			fs_supported.setText(R.string.unsupported);
		}
    }
	public int count = 1;
	OnClickListener multi_init = new OnClickListener(){
		@Override
		public void onClick(View v){count=count+1;
			if(count==3){startActivity();count=0;}
			else{
			}
		}
	};
	public void startActivity(){startActivity(new Intent(this, DevActivity.class));}
	private void setContentView(){
		UiModeManager ui = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);

        switch (ui.getNightMode()) {
            case ui.MODE_NIGHT_NO:
                setTheme(R.style.DAY);
				setContentView(R.layout.activity_main);
                break;
            case ui.MODE_NIGHT_YES:
                setTheme(R.style.NIGHT);
				setContentView(R.layout.activity_main);
				break;
        }
	}
}
