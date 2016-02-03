package com.example.raja.testurlconnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//import android.R;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private ImageView imgView1;
    EditText editt1;

    RecyclerView recyclerView;
    LinearLayoutManager llm;

    private Bitmap bitmap = null;
    URL url1;
    String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgView1 = (ImageView) findViewById(R.id.imgView1);

        editt1 = (EditText) findViewById(R.id.editt1);
        //RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Floating button", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlString = editt1.getText().toString();
                try {
                    url1 = new URL(urlString);
                } catch (MalformedURLException e){
                    Log.e("URL", "Something went wrong while" + " retrieving bitmap from ");
                }
                    new ImageDownloader().execute(url1);
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                checkInternetConnection();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings chosen", Toast.LENGTH_SHORT)
                        .show();
            case R.id.action_others: {
                Toast.makeText(getApplicationContext(), "Others chosen", Toast.LENGTH_SHORT)
                        .show();
            }
            default:
                return true;
        }

        //return super.onOptionsItemSelected(item);
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        // Check for network connections

        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(getApplicationContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            // not connected to the internet
            Toast.makeText(getApplicationContext(), "Not connected to network!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private InputStream openHttpConnection(String urlStr) throws IOException{
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not a Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    private class ImageDownloader extends AsyncTask<URL, Void, Bitmap>{

        Bitmap bm = null;
        protected Bitmap doInBackground(URL... urls) {
            String urlStr = urls[0].toString();
//            int count = url.length;
            /*long totalSize = 0;
            for (int i = 0; i < count; i++) {
                totalSize += Downloader.downloadFile(urls[i]);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;*/
            try {
                bm = downloadBitmap(urlStr);
                return bm;
            } catch (IOException e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
            /*progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", "Downloading Image");*/
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
            result = bm;
            imgView1.setImageBitmap(result);
        }

        /*protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }*/

        private Bitmap downloadBitmap(String urlStr) throws IOException {
            InputStream in = null;
            final String url = urlStr;
            try{
                in = openHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(in);
            }catch(IOException e){
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }finally{
                if (in != null) {
                    in.close();
                }
            }
            return bitmap;
        }
    }
}
