package net.pandazooka.maplab6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    EditText studentID;
    EditText studentName;
    EditText studentGrade;
    Button btn_StudentAdd;
    Button btn_StudentFind;
    Button btn_StudentAll;
    Button btn_StudentDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = this.openOrCreateDatabase("Students",MODE_PRIVATE, null);

        //Create table
        db.execSQL("CREATE TABLE IF NOT EXISTS Students (ID VARCHAR, Name VARCHAR, Grade VARCHAR);");


        studentID       = (EditText) findViewById(R.id.studentID);
        studentName     = (EditText) findViewById(R.id.studentName);
        studentGrade    = (EditText) findViewById(R.id.studentGrade);

        btn_StudentAdd = (Button) findViewById(R.id.btn_StudentAdd);
        btn_StudentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String i = studentID.getText().toString();
                    String g = studentGrade.getText().toString();
                    String n = studentName.getText().toString();
                    if(addStudent(i, n, g)) {
                        studentID.setText("");
                        studentName.setText("");
                        studentGrade.setText("");
                    }else {
                    }
            }
        });

        btn_StudentFind = (Button) findViewById(R.id.btn_StudentFind);
        btn_StudentFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i = studentID.getText().toString();
                findDeleteStudent(i, false);
                //Toast.makeText(MainActivity.this, "NumberFormatException", Toast.LENGTH_SHORT).show();
                //alertBuilder("This student does not exist", null);

                studentID.setText("");
                studentName.setText("");
                studentGrade.setText("");
            }
        });

        btn_StudentAll = (Button) findViewById(R.id.btn_StudentAll);
        btn_StudentAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getAllStudents();
            }
        });

        btn_StudentDelete = (Button) findViewById(R.id.btn_StudentDelete);
        btn_StudentDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                    String i = studentID.getText().toString();
                    findDeleteStudent(i, true);
                        //String alertMsg = "ID:" + i + " Name: " + " Marks:"; //This needs to be fixed
                        //alertBuilder("The following student has been deleted", alertMsg);
                    //} else {
                    //    alertBuilder("This student does not exist", null);
                    //}
                    //alertBuilder("This student does not exist", null);

                studentID.setText("");
                studentName.setText("");
                studentGrade.setText("");
            }
        });

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

    private boolean addStudent(String id, String name, String grade){
        if(id.matches("") || name.matches("") || grade.matches("")){
            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            db.execSQL("INSERT INTO Students (ID, Name, Grade) VALUES('"
                    + id + "', '"
                    + name + "', '"
                    + grade + "');");
            String alertMsg = "ID:" + id + " Name:" + name + " Marks:" + grade;
            alertBuilder("The following student was added", alertMsg);
            return true;
        }
    }

    private void findDeleteStudent(String id, boolean delete){
        Cursor c = db.rawQuery("SELECT ID, Name, Grade FROM Students WHERE ID = '" + id + "'", null);

        int i = c.getColumnIndex("ID");
        int n = c.getColumnIndex("Name");
        int g = c.getColumnIndex("Grade");

        //Check if result is valid
        c.moveToFirst();
        if(c != null && c.getCount() > 0){
            String sID      = c.getString(i);
            String sName    = c.getString(n);
            String sGrade   = c.getString(g);

            Log.d("ID", sID);
            Log.d("Name", sName);
            Log.d("Grade", sGrade);

            if(delete) {
                db.execSQL("DELETE FROM Students WHERE ID = '" + id + "';");
                String alertMsg = "ID:" + sID + " Name: " + sName + " Marks:" + sGrade;
                alertBuilder("The following student has been deleted", alertMsg);
            }else{
                String alertMsg = "ID:" + sID + " Name: " + sName + " Marks:" + sGrade;
                alertBuilder("Student details are as follows", alertMsg);
            }
            //return true;
        }else {
                alertBuilder("This student does not exist", null);
            //return false;
        }
    }

    private void getAllStudents(){
        Cursor c = db.rawQuery("SELECT * FROM Students", null);

        int i = c.getColumnIndex("ID");
        int n = c.getColumnIndex("Name");
        int g = c.getColumnIndex("Grade");
        StringBuffer alertMsg = new StringBuffer();

        //Check if result is valid
        c.moveToFirst();
        if(c != null && c.getCount() > 0) {
            //Loop through results
            do {
                String sID = c.getString(i);
                String sName = c.getString(n);
                String sGrade = c.getString(g);

                Log.d("ID", sID);
                Log.d("Name", sName);
                Log.d("Grade", sGrade);

                alertMsg.append("ID:" + sID + " Name: " + sName + " Marks:" + sGrade + "\n");
            } while (c.moveToNext());

            if(c.getCount() == 1) {
                alertBuilder("The following student has been added", alertMsg.toString());
            }else{
                alertBuilder("The following students have been added", alertMsg.toString());
            }
        }else {
            alertBuilder("No students were found", null);
        }
    }

    private void alertBuilder(String title, String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(title)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        if(msg != null){
            alert.setMessage(msg);
        }
        alert.show();
    }
}
