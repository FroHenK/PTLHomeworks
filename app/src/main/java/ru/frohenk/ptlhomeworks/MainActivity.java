package ru.frohenk.ptlhomeworks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Gson gson;
    private Button buttonUpdate;
    private File file;
    private TextView textViewLastUpd;
    private DatePicker datePicker;
    private Button buttonGetHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        file = getFileStreamPath("homeworks.dat");
        textViewLastUpd = (TextView) findViewById(R.id.textViewLastUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdate();
            }
        });
        updateTimeChanged();
        startUpdate();
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        buttonGetHomework = (Button) findViewById(R.id.buttonGetHomework);
        buttonGetHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Implement
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH,datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                calendar.set(Calendar.YEAR, datePicker.getYear());
                Intent intent = new Intent(MainActivity.this,HomeworkByDateActivity.class);
                intent.putExtra(HomeworkByDateActivity.CALENDAR,calendar);
                startActivity(intent);
            }
        });
    }


    public void updateTimeChanged(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date = new Date(file.lastModified());
        textViewLastUpd.setText(String.format(getString(R.string.updatedDate),dateFormat.format(date)));

    }
    public void startUpdate(){
        //I know that this is horrible, but I was 14 yrs old...
        buttonUpdate.setEnabled(false);
        buttonUpdate.setText(R.string.updatingPleaseWait);
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {


                URL url = new URL(getString(R.string.urlForJson));
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();


                    StringWriter writer = new StringWriter();
                    IOUtils.copy(inputStream, writer, "UTF-8");
                    String jsonString = writer.toString();
                    System.out.println(jsonString);
                    HashMap hashMap = gson.fromJson(jsonString, HashMap.class);
                    if (hashMap.containsKey("errors"))
                        throw new RuntimeException("Server gave some error");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.write(jsonString.getBytes());
                    objectOutputStream.close();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (Exception e){
                    Log.e("GetHomework", "Error", e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    });
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonUpdate.setEnabled(true);
                            buttonUpdate.setText(R.string.action_update);
                            updateTimeChanged();
                        }
                    });

                }
                }
        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // nope i don't want it
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
