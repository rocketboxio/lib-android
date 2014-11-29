package io.rocketbox;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;

public class AsyncSendMail extends AsyncTask<String, Void, JSONObject> {

	private RocketboxListener listener;
	private List<NameValuePair> values;

	public List<NameValuePair> getValues() {
		return values;
	}

	public void setValues(List<NameValuePair> value) {
		this.values = value;
	}

	public RocketboxListener getListener() {
		return listener;
	}

	public void setListener(RocketboxListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		listener.response(result);
	}

	@Override
	protected JSONObject doInBackground(String... params) {

		try {
			String request = Rocketbox.api_path + params[0];
			URL url = new URL(request);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("token", Rocketbox.token));

			JSONObject json_value = new JSONObject();
			for (NameValuePair nameValuePair : values) {
				json_value.put(nameValuePair.getName(),
						nameValuePair.getValue());
			}
			parameters.add(new BasicNameValuePair("value", json_value
					.toString()));

			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					os, "UTF-8"));
			writer.write(getQuery(parameters));
			writer.flush();
			writer.close();
			os.close();

			conn.connect();

			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			String json = getResponseText(in);
			return new JSONObject(json);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getQuery(List<NameValuePair> params)
			throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private String getResponseText(InputStreamReader inStream) {
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

}
