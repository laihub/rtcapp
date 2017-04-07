package com.closeli.demo.clbidirectionrenderdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.closeli.natives.CLWebRtcNativeBinder;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnFirst = (Button) findViewById(R.id.btnFirst);
        Button btnSecond = (Button) findViewById(R.id.btnSecond);

       btnFirst.setOnClickListener(this);
        btnSecond.setOnClickListener(this);

        CLWebRtcNativeBinder.initStart(getApplicationContext());


    }



    @Override
    public void onClick(View view) {

        //Camera 权限提前判断！！！
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (result != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

                Toast.makeText(this, "没有相机权限！！！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Intent intent = null;
        if (R.id.btnFirst == view.getId()) {
            intent = new Intent(this, CLBIVideoDemoActivity.class);
        }
        else {
            intent = new Intent(this, CLBIVideoDemoRemoteActivity.class);
        }
        startActivity(intent);
    }

}
