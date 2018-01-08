package com.example.android.guardianapi;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GuardianActivity extends AppCompatActivity implements LoaderCallbacks<List<Guardian>> {
    private static final String LOG_TAG = GuardianActivity.class.getName();
    private static final int LOADER_ID = 1;
    /**
     * URL
     */
    private static final String USGS_REQUEST_URL =
            "http://content.guardianapis.com/search?q=debates&api-key=test&show-fields=byline";
    /**
     * Adapter for the list of earthquakes
     */
    private GuardianAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        ListView listView = findViewById(R.id.list);

        mAdapter = new GuardianAdapter(this, new ArrayList<Guardian>());

        mEmptyStateTextView = findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Guardian currentGuardian = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri uri = Uri.parse(currentGuardian.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<Guardian>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value...
        uriBuilder.appendQueryParameter("order-by", "newest");


        uriBuilder.appendQueryParameter("orderby", orderBy);
        // Return the completed uri
        return new GuardianLoader(this, uriBuilder.toString());

    }


    @Override
    public void onLoadFinished(Loader<List<Guardian>> loader, List<Guardian> guardians) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_guardian);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (guardians != null && !guardians.isEmpty()) {
            mAdapter.addAll(guardians);
        }
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.

    }

    @Override
    public void onLoaderReset(Loader<List<Guardian>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
