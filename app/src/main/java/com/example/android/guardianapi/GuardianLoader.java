package com.example.android.guardianapi;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by ebtesam on 1/2/2018 AD.
 */

public class GuardianLoader extends AsyncTaskLoader<List<Guardian>> {

    /** Query URL */
    private String mUrl;

    public GuardianLoader(Context context, String mUrl) {
        super(context);
        this.mUrl=mUrl;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Guardian> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Guardian> guardians = QueryUtils.fetchGuardianData(mUrl);
        return guardians;
    }
}
