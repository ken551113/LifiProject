package gia.kenlee.lifiproject;

import com.physicaloid.lib.Physicaloid;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String PREF_SSID="NUM1";
    private String PREF_PW="NUM2";

    Button btSend;
    EditText etSSID, etPassword;
    Switch swOpen;

    Physicaloid mPhysicaloid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swOpen = (Switch) findViewById(R.id.swOpen);
        btSend = (Button) findViewById(R.id.btSend);
        etSSID = (EditText) findViewById(R.id.etSSID);
        etPassword = (EditText) findViewById(R.id.etPassword);

        setEnabledUi(false);

        mPhysicaloid = new Physicaloid(this);
        swOpen.setChecked(false);
        swOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOpened) {
                if (isOpened) {
                    openDevice();
                } else {
                    closeDevice();
                }
            }
        });
    }


    private void openDevice() {
        if (!mPhysicaloid.isOpened()) {
            if (mPhysicaloid.open()) {
                mPhysicaloid.setBaudrate(115200);
                setEnabledUi(true);
            } else {
                Toast.makeText(this, "Cannot open", Toast.LENGTH_LONG).show();
                setEnabledUi(true);
            }
        }
    }

    private void closeDevice() {
            if(mPhysicaloid.close()) {
                setEnabledUi(false);
            }
        }


    private void setEnabledUi(boolean on) {
        if(on) {
            btSend.setEnabled(true);
            etSSID.setEnabled(true);
            etPassword.setEnabled(true);
        } else {
            btSend.setEnabled(false);
            etSSID.setEnabled(false);
            etPassword.setEnabled(false);
        }
    }

    public void onClickSend(View v){
        String ssid = "I"+etSSID.getText().toString()+"\n";
        String password = "P"+etPassword.getText().toString()+"\n";
        if(ssid.length()>0) {
            byte[] buf＿id= ssid.getBytes();
            byte[] buf_password = password.getBytes();
            mPhysicaloid.write(buf＿id, buf＿id.length);
            mPhysicaloid.write(buf_password, buf_password.length);
        }
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        SharedPreferences pref = getSharedPreferences ("pref_apn" , MODE_PRIVATE);
        Editor ed = pref.edit();
        ed.putString(PREF_SSID, etSSID.getText().toString());
        ed.putString(PREF_PW, etPassword.getText().toString());
        ed.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        SharedPreferences pref = getSharedPreferences("pref_apn", MODE_PRIVATE);
        etSSID.setText(pref.getString(PREF_SSID, ""));
        etPassword.setText(pref.getString(PREF_PW, ""));
        super.onResume();
    }
}

