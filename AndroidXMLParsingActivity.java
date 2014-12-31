package com.natiki.xmlparser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidXMLParsingActivity extends ListActivity {

    // All static variables
    static final String URL = "http://opendap.co-ops.nos.noaa.gov/stations/stationsXML.jsp";
    // XML node keys
    static final String KEY_STATION = "station"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_LAT = "lat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

// get xml data
        new GetXMLAsyncTask().execute();

// selecting single ListView item
        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
// getting values from selected ListItem
                String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
                String idi = ((TextView)view.findViewById(R.id.ID)).getText().toString();
                String lat = ((TextView)view.findViewById(R.id.lat)).getText().toString();

// Starting new Intent
                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
                in.putExtra(KEY_NAME, name);
                in.putExtra(KEY_ID, idi);
                in.putExtra(KEY_LAT, lat);
                startActivity(in);
            }
        });
    }

    private class GetXMLAsyncTask extends AsyncTask<Void, Void, String> {
        private ArrayList<HashMap<String, String>> menuItems = null;
        private ArrayList<HashMap<String, String>> menuItemsSort = null;
        private XMLParser parser = null;

        public GetXMLAsyncTask() {
            super();
            menuItems = new ArrayList<HashMap<String, String>>();
            parser = new XMLParser();
        }

        @Override
        protected void onPostExecute(String xml) {
            super.onPostExecute(xml);
String tst = null;
            String tct = null;
            if (null != xml) {
                Document doc = parser.getDomElement(xml);

                NodeList nl = doc.getElementsByTagName(KEY_STATION);

// looping through all item nodes <station>
                for (int i = 0; i < nl.getLength(); i++) {
// creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    Element e = (Element) nl.item(i);

                    NamedNodeMap attrs = e.getAttributes();

                    Node stName = attrs.item(0);
                    Node stID = attrs.item(1);
                    tst = stName.getNodeValue();
                    tct = stID.getNodeValue();



                        map.put(KEY_NAME, tst);
                        map.put(KEY_ID, tct);

                        map.put(KEY_LAT, parser.getValue(e, KEY_LAT));
                        menuItems.add(map);



// adding each child node to HashMap key => value

                }

                Collections.sort(menuItems, new MapComparator(KEY_NAME));


// Adding menuItems to ListView
                ListAdapter adapter = new SimpleAdapter(getApplicationContext(), menuItems
                        , R.layout.list_item, new String[] { KEY_NAME, KEY_ID, KEY_LAT }
                        , new int[]{R.id.name, R.id.ID, R.id.lat});

                setListAdapter(adapter);
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            return parser.getXmlFromUrl(URL); // getting XML
        }
    }
}