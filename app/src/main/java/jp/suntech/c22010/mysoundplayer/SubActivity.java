package jp.suntech.c22010.mysoundplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            int _id = bundle.getInt("_id");
            TextView tv_music_test = findViewById(R.id.tv_music_test);//デバッグ用
            tv_music_test.setText(Integer.toString(_id));
        }
        else {
            Toast.makeText(SubActivity.this, "曲情報がありません", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}