package com.example.android.guardianapi;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebtesam on 1/2/2018 AD.
 */

public class QueryUtils  {
    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Guardian} objects.
     */
    public static List<Guardian> fetchEarthquakeData(String requestUrl) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Guardian> guardians = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return guardians;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the gaurdian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Guardian} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Guardian> extractFeatureFromJson(String guardianJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(guardianJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Guardian> guardians = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(guardianJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray guardianArray = response.getJSONArray("results");

            // For each earthquake in the earthquakeArray, create an  object
            for (int i = 0; i < guardianArray.length(); i++) {

                JSONObject current = guardianArray.getJSONObject(i);

                // Extract the value for the key called "mag"
                String webTitle = current.getString("webTitle");

                String sectionName  = current.getString("sectionName");

                // Extract the value for the key called "url"
                String url = current.getString("webUrl");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Guardian guardian = new Guardian(sectionName,webTitle,url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                guardians.add(guardian);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the guardian JSON results", e);
        }

        // Return the list
        return guardians;
    }


}
