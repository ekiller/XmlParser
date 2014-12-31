package com.natiki.xmlparser;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {


    static final String KEY_ID = "id";
    static final String KEY_NAME = "name";
    static final String KEY_LAT = "lat";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);

        // getting intent data
        Intent in = getIntent();

        // Get XML values from previous intent
        String name = in.getStringExtra(KEY_NAME);
        String idi = in.getStringExtra(KEY_ID);
        String lat = in.getStringExtra(KEY_LAT);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name_label);
        TextView lblCost = (TextView) findViewById(R.id.id_label);
        TextView lblDesc = (TextView) findViewById(R.id.lat_label);

        lblName.setText(name);
        lblCost.setText(idi);
        lblDesc.setText(lat);
    }
}
