package app.example.app.fetchjsondata;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list  = (ListView) findViewById(R.id.listView);
        new fetcherService().execute(new String[]{"https://reqres.in/api/users?page=1"});
    }


    public class fetcherService extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader buffer_reader = null;
            String results = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream input_stream = connection.getInputStream();
                if(input_stream == null){
                    return null;
                }


                buffer_reader = new BufferedReader(new InputStreamReader(input_stream));
                StringBuffer buffer =new StringBuffer();
                String lines;


                while((lines = buffer_reader.readLine()) != null){
                    buffer.append(lines+"\n");
                }

                if(buffer.length() == 0){
                    return null;
                }

                results = buffer.toString();
            }catch (Exception e){
                Log.e("Error", e.toString());
            }
            return  results;
        }

        @Override
        protected void onPostExecute(String values) {

            //Displaying data in single row
            try {
                JSONObject jsonObject = new JSONObject(values);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                ArrayList<String> arrayList = new ArrayList<String>();


                for(int i=0; i<jsonArray.length();i++){
                    arrayList.add(jsonArray.getJSONObject(i).getString("first_name"));
                }

                ArrayAdapter adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList.toArray(new String[arrayList.size()]));
                list.setAdapter(adapter);

            }catch (Exception e){
                Log.e("Error", e.toString());
            }




           /* Displaying data in two rows
            try {
                JSONObject jsonObject = new JSONObject(values);
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> items;

                for(int i=0; i<jsonArray.length();i++){
                    items = new HashMap<String, String>();
                    items.put("firstname", jsonArray.getJSONObject(i).getString("first_name"));
                    items.put("lastname", jsonArray.getJSONObject(i).getString("last_name"));
                    arrayList.add(items);
                }

                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, arrayList, R.layout.custom_layout, new String[]{"firstname", "lastname"}, new int[]{R.id.textView1, R.id.textView2});
                list.setAdapter(adapter);

            }catch (Exception e){
                Log.e("Error", e.toString());
            }*/
        }

    }
}
