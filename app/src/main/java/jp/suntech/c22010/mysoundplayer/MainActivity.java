package jp.suntech.c22010.mysoundplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private DatabaseHelper _helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(_helper == null) {
            _helper = new DatabaseHelper(MainActivity.this);
            ResetData(MainActivity.this);
        }

        ListView lv_music = findViewById(R.id.lv_music);
        lv_music.setOnItemClickListener(new ListItemClickListener());
        UpdateList();
    }

    @Override
    protected void onDestroy(){
        _helper.close();
        super.onDestroy();
    }

    public void ResetData(Context context) {
        DatabaseHelper _helper = new DatabaseHelper(context);
        SQLiteDatabase db = _helper.getWritableDatabase();

        String sql = "DELETE FROM music_list";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.executeUpdateDelete();


        String title_array[] = {"銃声", "爆発音"};
        int frag_array[] = {};
        int sound_array[] = {R.raw.gun, R.raw.bomb};
        for (int i = 0; i < 2; i++) {
            String sqlInsert = "INSERT INTO music_list (_id, title, frag_res, music_res) VALUES (?, ?, ?, ?)";
            stmt = db.compileStatement(sqlInsert);

            stmt.bindLong(1, i + 1);
            stmt.bindString(2, title_array[i]);
            stmt.bindLong(3, frag_array[i]);
            stmt.bindLong(4, sound_array[i]);

            stmt.executeInsert();
        }
    }

    public void UpdateList(){
        SQLiteDatabase db = _helper.getWritableDatabase();
        String sql = "SELECT * FROM music_list;";
        Cursor cursor = db.rawQuery(sql, null);
        List<Map<String, Object>> all_list = new ArrayList<>();

        String[] FROM = {"title"};
        int[] TO = {android.R.id.text1};

        while (cursor.moveToNext()) {
            int idx_title = cursor.getColumnIndex("title");

            String res_title = cursor.getString(idx_title);

            Map<String, Object> res = new HashMap<>();
            res.put("title", res_title);
            all_list.add(res);
        }

        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, all_list, android.R.layout.simple_list_item_1, FROM, TO);
        ListView lv_music = findViewById(R.id.lv_music);
        lv_music.setAdapter(adapter);
        cursor.close();
        db.close();
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){

            SQLiteDatabase db = _helper.getWritableDatabase();
            String sql = "SELECT * FROM music_list WHERE _id = " + position + ";";
            Cursor cursor = db.rawQuery(sql, null);

            String title = "";
            int image = 0;
            int music = 0;

            while (cursor.moveToNext()) {
                int idx_title = cursor.getColumnIndex("title");
                int idx_frag = cursor.getColumnIndex("frag_res");
                int idx_music = cursor.getColumnIndex("music_res");

                title = cursor.getString(idx_title);
                image = cursor.getInt(idx_frag);
                music = cursor.getInt(idx_music);
            }
            cursor.close();
            db.close();

            Bundle bundle = new Bundle();
            bundle.putInt("_id", position);
            bundle.putString("title", title);
            bundle.putInt("frag_res", image);
            bundle.putInt("sound_res", music);

            Intent intent = new Intent(MainActivity.this, SubActivity.class);
            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

}