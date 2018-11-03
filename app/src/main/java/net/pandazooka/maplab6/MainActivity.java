package net.pandazooka.maplab6;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText studentID;
    EditText studentName;
    EditText studentGrade;
    Button btn_StudentAdd;
    Button btn_StudentFind;
    Button btn_StudentAll;
    Button btn_StudentUnknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = this.openOrCreateDatabase("Students",MODE_PRIVATE, null);

        //Create table
        db.execSQL("CREATE TABLE IF NOT EXISTS Students (ID INT(10), Name VARCHAR, Grade Int(3));");


        studentID       = (EditText) findViewById(R.id.studentID);
        studentName     = (EditText) findViewById(R.id.studentName);
        studentGrade    = (EditText) findViewById(R.id.studentGrade);

        btn_StudentAdd = (Button) findViewById(R.id.btn_StudentAdd);
        btn_StudentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int i = Integer.parseInt(studentID.getText().toString());
                    int g = Integer.parseInt(studentGrade.getText().toString());
                    String n = studentName.getText().toString();

                    addStudent(i, n, g);
                }catch (NumberFormatException e){
                    Toast.makeText(MainActivity.this, "NumberFormatException", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_StudentFind = (Button) findViewById(R.id.btn_StudentFind);
        btn_StudentFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ii = Integer.parseInt(studentID.getText().toString());
                Cursor c = db.rawQuery("SELECT ID, Name, Grade FROM Students WHERE ID = " + ii, null);

                int i = c.getColumnIndex("ID");
                int n = c.getColumnIndex("Name");
                int g = c.getColumnIndex("Grade");

                //Check if result is valid
                c.moveToFirst();
                if(c != null){
                    int sID         = c.getInt(i);
                    String sName    = c.getString(n);
                    int sGrade      = c.getInt(g);

                    Log.d("ID", valueOf(sID));
                    Log.d("Name", sName);
                    Log.d("Grade", valueOf(sGrade));

                    studentID.setText(valueOf(sID));
                    studentName.setText(sName);
                    studentGrade.setText(valueOf(sGrade));
                }
            }
        });

        btn_StudentAll = (Button) findViewById(R.id.btn_StudentAll);
        btn_StudentAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor c = db.rawQuery("SELECT * FROM Students", null);

                int i = c.getColumnIndex("ID");
                int n = c.getColumnIndex("Name");
                int g = c.getColumnIndex("Grade");

                //Check if result is valid
                c.moveToFirst();
                if(c != null) {
                    //Loop through results
                    do {
                        int sID = c.getInt(i);
                        String sName = c.getString(n);
                        int sGrade = c.getInt(g);

                        Log.d("ID", valueOf(sID));
                        Log.d("Name", sName);
                        Log.d("Grade", valueOf(sGrade));

                        studentID.setText(valueOf(sID));
                        studentName.setText(sName);
                        studentGrade.setText(valueOf(sGrade));
                    } while (c.moveToNext());
                }
            }
        });

        btn_StudentUnknown  = (Button) findViewById(R.id.btn_StudentUnknown);

        //Insert data into table
        //db.execSQL("INSERT INTO Students (ID, Name, Grade) VALUES(1, 'Justin', 100);");

        /*
        //Get all data from table
        Cursor c = db.rawQuery("SELECT * FROM Students", null);

        int i = c.getColumnIndex("ID");
        int n = c.getColumnIndex("Name");
        int g = c.getColumnIndex("Grade");

        //Check if result is valid
        c.moveToFirst();
        if(c != null){
            //Loop through results
            do{
                int sID         = c.getInt(i);
                String sName    = c.getString(n);
                int sGrade      = c.getInt(g);

                Log.d("ID", valueOf(sID));
                Log.d("Name", sName);
                Log.d("Grade", valueOf(sGrade));

                studentID.setText(valueOf(sID));
                studentName.setText(sName);
                studentGrade.setText(valueOf(sGrade));
            }while(c.moveToNext());

        }*/
    }

    private void addStudent(int id, String name, int grade){
        db.execSQL("INSERT INTO Students (ID, Name, Grade) VALUES("
                + id + ", "
                + name + ", "
                + grade + ");");
    }

}
