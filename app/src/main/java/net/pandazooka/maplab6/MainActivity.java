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
                    Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_StudentFind = (Button) findViewById(R.id.btn_StudentFind);
        btn_StudentFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int i = Integer.parseInt(studentID.getText().toString());
                    findStudent(i, true);
                }catch(NumberFormatException e){
                    //Toast.makeText(MainActivity.this, "NumberFormatException", Toast.LENGTH_SHORT).show();
                    alertBuilder("This student does not exist", null);
                }
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
                try {
                    int i = Integer.parseInt(studentID.getText().toString());
                    if (findStudent(i, false)) {
                        db.execSQL("DELETE FROM Students WHERE ID = " + i + ";");

                        String alertMsg = "ID:" + i + " Name: " + " Marks:"; //This needs to be fixed
                        alertBuilder("The following student has been deleted", alertMsg);
                        /*
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("The following student has been deleted")
                                .setMessage("ID:" + i + " Name: " + " Marks:") //This needs to be fixed
                                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        */
                    } else {
                        alertBuilder("This student does not exist", null);
                        /*
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("This student does not exist")
                                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        */
                    }
                }catch(NumberFormatException e){
                    alertBuilder("This student does not exist", null);
                }
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

    private void addStudent(int id, String name, int grade){
        db.execSQL("INSERT INTO Students (ID, Name, Grade) VALUES("
                + id + ", '"
                + name + "', "
                + grade + ");");
        String alertMsg = "ID:" + id + " Name:" + name + " Marks:" + grade;
        alertBuilder("The following student was added", alertMsg);
        /*
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("The following student was added")
                .setMessage("ID:" + id + " Name:" + name + " Marks:" + grade)
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
        */
    }

    private boolean findStudent(int id, boolean displayAlert){
        Cursor c = db.rawQuery("SELECT ID, Name, Grade FROM Students WHERE ID = " + id, null);

        int i = c.getColumnIndex("ID");
        int n = c.getColumnIndex("Name");
        int g = c.getColumnIndex("Grade");

        //Check if result is valid
        c.moveToFirst();
        if(c != null && c.getCount() > 0){
            int sID         = c.getInt(i);
            String sName    = c.getString(n);
            int sGrade      = c.getInt(g);

            Log.d("ID", valueOf(sID));
            Log.d("Name", sName);
            Log.d("Grade", valueOf(sGrade));

            if(displayAlert) {
                String alertMsg = "ID:" + sID + " Name: " + sName + " Marks:" + sGrade;
                alertBuilder("Student details are as follows", alertMsg);
                /*
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Student details are as follows")
                        .setMessage("ID:" + sID + " Name: " + sName + " Marks:" + sGrade)
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
               */
            }
            return true;
        }else {
            if (displayAlert) {
                alertBuilder("This student does not exist", null);
                /*
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("This student does not exist")
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                */
            }
            return false;
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
                int sID = c.getInt(i);
                String sName = c.getString(n);
                int sGrade = c.getInt(g);

                Log.d("ID", valueOf(sID));
                Log.d("Name", sName);
                Log.d("Grade", valueOf(sGrade));

                alertMsg.append("ID:" + sID + " Name: " + sName + " Marks:" + sGrade + "\n");
            } while (c.moveToNext());

            if(c.getCount() == 1) {
                alertBuilder("The following student has been added", alertMsg.toString());
            }else{
                alertBuilder("The following students have been added", alertMsg.toString());
            }
            /*
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("The following students have been added") //Needs a singular version
                    .setMessage(dialogMsg)
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            */
        }else {
            alertBuilder("No students were found", null);
            /*
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("No students were found")
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            */
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
