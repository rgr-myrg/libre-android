package net.usrlib.libre.rest;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rgr-myrg on 1/6/17.
 */

public class HtmlLoader extends AsyncTask<Void, Void, String> {
	private OnRequestComplete mCallback = null;
	private URL mUrl = null;

	public interface OnRequestComplete {
		void run(String html);
	}

	public HtmlLoader fetchWithUrl(final String url) {
		try {
			this.mUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return this;
	}

	public HtmlLoader onRequestComplete(final OnRequestComplete callback) {
		this.mCallback = callback;
		return this;
	}

	public void start() {
		this.execute();
	}

	@Override
	protected String doInBackground(Void... params) {
		StringBuilder body = new StringBuilder();
		HttpURLConnection urlConnection = null;
		BufferedReader bufferedReader = null;

		try {
			// HttpURLConnection uses GET request by default.
			urlConnection = (HttpURLConnection) mUrl.openConnection();
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();

			if (inputStream == null) {
				return null;
			}

			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";

			while ((line = bufferedReader.readLine()) != null) {
				publishProgress();
				body.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return String.valueOf(body);
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);

		if (mCallback != null) {
			mCallback.run(response);
		}
	}
}
