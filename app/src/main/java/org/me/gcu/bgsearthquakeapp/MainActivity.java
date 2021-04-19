package org.me.gcu.bgsearthquakeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.AdapterView;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.ArrayList;

import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private ArrayList<String> items;
    private TextView rawDataDisplay;
    private String result = "";
    private String earthquakeSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private LinkedList <EarthquakeClass> earthquakeList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        items = new ArrayList<String>();
        rawDataDisplay = (TextView)findViewById(R.id.text_view_id);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");

        startProgress();

        /*alist = parseData(earthquakeSource);

        if (alist != null)
        {
            Log.e("MyTag","List not null");
            int count = 0;
            for (Object o : alist)
            {
                Log.e("MyTag",o.toString());
                items.add(o.toString());
                count = count + 1;
            }
            Log.e("My Tag", "Array is " + items.toString());
        }
        else
        {
            Log.e("MyTag","List is null");
        }

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview,items);

        if (adapter == null)
        {
            Log.e("MyTag","Adapter error");
        }

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        */

    }

    private LinkedList<EarthquakeClass> parseData(String dataToParse)
    {
        EarthquakeClass widget = null;
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                Log.e("MyTag","In while loop");
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        earthquakeList  = new LinkedList<EarthquakeClass>();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","Item Start Tag found");
                        widget = new EarthquakeClass();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag","Title is " + temp);
                        widget.setTitle(temp);
                        Resources res = getResources();
                        String[] magnitudes = res.getStringArray(R.array.magnitudes_array);
                        /*if (string1.equals(String target))
                        {
                            findViewById(R.id.textView1).setBackgroundColor(getResources().getColor(R.color.Green));
                        }*/
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("link"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag","Link is " + temp);
                            widget.setLink(temp);
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("pubDate"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag","Publish Date is " + temp);
                                widget.setPubDate(temp);
                            }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","item is " + widget.toString());
                        earthquakeList.add(widget);
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = earthquakeList.size();
                        Log.e("MyTag","channel size is " + size);
                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        return earthquakeList;

    }

    public void onClick(View aview)
    {
        /*Log.e("MyTag","in onClick");
        startProgress();
        Log.e("MyTag","after startProgress");*/

        //////////////
        // Add call to parsing here
    }

    public void startProgress()
    {
        // Runs the network access on a separate thread;
        new Thread(new Task(earthquakeSource)).start();
    } //

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //Separate thread to access the internet resource over a network
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
                }
            });
        }

    }

}
