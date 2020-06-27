package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText noteData;
    TextView noteTitle;
    SQLiteDatabase db;
    long itemData;
    Cursor userCursor;

    MediaPlayer mediaPlayer;
    Button play, pause, stop;
    Spinner spinner;
    String prev_song = "";
    ArrayList<MediaPlayer> players = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        setContentView(R.layout.activity_main);
        players.add(MediaPlayer.create(this, R.raw.comp1));
        players.add(MediaPlayer.create(this, R.raw.comp2));
        players.add(MediaPlayer.create(this, R.raw.comp3));

        play = (Button) findViewById(R.id.start);
        pause = (Button) findViewById(R.id.pause);
        stop = (Button) findViewById(R.id.stop);
        spinner = findViewById(R.id.compos);

        Intent intent = getIntent();
        itemData = intent.getLongExtra("id", -1);
        noteData = findViewById(R.id.noteData);
        noteTitle = findViewById(R.id.noteTitle);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        if(itemData != -1){
            setNoteData();
        }

    }


    private void setNoteData(){
        db = databaseHelper.getReadableDatabase();
        userCursor =  db.rawQuery("select _id, name, create_date, text from "+ DatabaseHelper.TABLE + " WHERE _id = " + itemData + ";", null);
        userCursor.moveToFirst();
        noteTitle.setText(userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
        noteData.setText(userCursor.getString(userCursor.getColumnIndex(DatabaseHelper.COLUMN_TEXT)));
    }

    public void onClick2(View v){

        if (!spinner.getSelectedItem().toString().equals("NO")) { // проверяем, что есть выбранная запись
            int index = Integer.valueOf(spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().length() - 1)); // так как запси в массиве, нужен индекс её вытащить
            index -= 1;
            if (!prev_song.equals(spinner.getSelectedItem().toString()) && (!prev_song.equals(""))) { // вот этот весь if да и само prev_song, воспроизведение записей не накладывалось
                int temp = Integer.valueOf(prev_song.substring(prev_song.length() - 1)) - 1;
                players.get(temp).pause();
            }
            prev_song = spinner.getSelectedItem().toString();

            switch (v.getId()) {
                case R.id.btn_back:
                    String text = noteData.getText().toString();
                    db.execSQL("UPDATE " + DatabaseHelper.TABLE + " SET " + DatabaseHelper.COLUMN_TEXT + " = '" + text + "', " + DatabaseHelper.COLUMN_DATE + " = " + "datetime()" + " WHERE _id = " + itemData);
                    Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    break;
                case R.id.start:
                    players.get(index).start();
                    break;
                case R.id.pause:
                    players.get(index).pause();
                    break;
                case R.id.stop:
                    players.get(index).stop();
                    break;

            }
        }
        else {
            Toast toast = Toast.makeText(this, "Choose file", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
