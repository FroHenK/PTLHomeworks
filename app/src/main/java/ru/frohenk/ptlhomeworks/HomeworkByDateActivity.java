package ru.frohenk.ptlhomeworks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class HomeworkByDateActivity extends AppCompatActivity {

    public static final String CALENDAR = "calendar";
    private Calendar neededDate;
    private File file;
    public ArrayList<Homework> homeworks;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_by_date);
        //I know that this is horrible, but I was 14 yrs old...
        neededDate = (Calendar) getIntent().getSerializableExtra(CALENDAR);
        file = getFileStreamPath("homeworks.dat");
        homeworks=new ArrayList<>();//to make no null error
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));

            StringWriter writer = new StringWriter();
            IOUtils.copy(objectInputStream, writer, "UTF-8");
            String theString = writer.toString();
            HashMap<String, Object> hashMap =(HashMap<String, Object>) new Gson().fromJson(theString, HashMap.class);
            ArrayList<Homework> homeworks = (ArrayList<Homework>) hashMap.get("homeworks");

            for (LinkedTreeMap homework :  (ArrayList<LinkedTreeMap>)hashMap.get("homeworks"))
            {

                Homework next = new Homework((int) Math.floor((Double) homework.get("id")),(String )homework.get("subject"),Calendar.getInstance(),(String)homework.get("body"),Calendar.getInstance());
                LinkedTreeMap date = (LinkedTreeMap) homework.get("date");
                if((int) Math.floor((Double) date.get("month")) ==neededDate.get(Calendar.MONTH)
                        &&(int) Math.floor((Double) date.get("dayOfMonth"))==neededDate.get(Calendar.DAY_OF_MONTH)&&
                        (int) Math.floor((Double) date.get("year"))==neededDate.get(Calendar.YEAR))
                    this.homeworks.add(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT);
        }
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<Homework>(HomeworkByDateActivity.this, android.R.layout.simple_list_item_1,homeworks));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
