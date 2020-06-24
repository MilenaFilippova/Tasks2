package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    EditText noteData;
    TextView noteTitle;
    SQLiteDatabase db;
    long itemData;
    Cursor userCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
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
        switch (v.getId()){
            case R.id.btn_back:
                String text = noteData.getText().toString();
                db.execSQL("UPDATE " + DatabaseHelper.TABLE + " SET " + DatabaseHelper.COLUMN_TEXT + " = '" + text + "', " + DatabaseHelper.COLUMN_DATE + " = " + "datetime()" + " WHERE _id = " + itemData);
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
                onBackPressed();
                break;
        }
    }
}
