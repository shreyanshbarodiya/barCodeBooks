package shreyansh.com.barcodebooks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class listActivity extends AppCompatActivity {

    public static ArrayList<String> isbnList = new ArrayList<>();
    public Button scanMore;
    public TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        scanMore = (Button) findViewById(R.id.btnScanMore);
        tv = (TextView) findViewById(R.id.textView);

        scanMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(listActivity.this,scanActivity.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Export data on clicking this button
                String isbnData = "";
                for(int i =0; i < isbnList.size(); i++){
                    isbnData = isbnData + isbnList.get(i) + "\n" ;
                }
                writeToFile(isbnData);
                Log.i("myTag","fab was clicked");


            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);



        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,isbnList);
        listView.setAdapter(adapter);

    }

    public static void addToList(String newIsbn){
        isbnList.add(newIsbn);
    }

    private void writeToFile(String data) {
        try {
            File path = listActivity.this.getExternalFilesDir(null);
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            File file = new File(path, ts + "_isbn_list.txt");

            FileOutputStream stream = new FileOutputStream(file);
            try {
                stream.write(data.getBytes());
                Toast.makeText(listActivity.this, "successfully written", Toast.LENGTH_LONG).show();
            }catch(Exception e){
                Toast.makeText(listActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                tv.setText("error: " + e.getMessage());
            }
            finally {
                stream.close();
            }
            Log.i("myTag","successfully written");


        }
        catch (IOException e) {
            Log.e("myTag", "File write failed: " + e.toString());
            Toast.makeText(listActivity.this, "Error... Try again", Toast.LENGTH_LONG).show();
            tv.setText("error" + e.getMessage());
        }
    }

}
