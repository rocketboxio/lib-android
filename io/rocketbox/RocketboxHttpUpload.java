package io.rocketbox;

import io.rocketbox.RocketboxMultipartEntity.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class RocketboxHttpUpload extends AsyncTask<String, Integer, JSONObject> {

	private String filePath;
	private String fileName;
	private HttpClient client;
	private long totalSize;
	private RocketboxListenerUpload listener;

	public RocketboxHttpUpload(String filePath, String fileName, RocketboxListenerUpload listener) {
		super();
		this.filePath = filePath;
		this.listener = listener;
		if (fileName == null){
			this.fileName = "";
		}
		else{
			this.fileName = fileName;		
		}
		
	}

	@Override
	protected void onPreExecute() {
		int timeout = 60000;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);

		client = new DefaultHttpClient(httpParameters);
	}

	public static String getMimeType(String url) {
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(url);
		return type;
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject result = null;
		try {
			File file = new File(filePath);
			HttpPost post = new HttpPost(Rocketbox.api_path + params[0]);

			MultipartEntity entity = new RocketboxMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}

					});

			ContentBody cbFile = new FileBody(file, getMimeType(filePath));
			entity.addPart("file", cbFile);
			entity.addPart("token", new StringBody(Rocketbox.token));
			entity.addPart("fileName", new StringBody(this.fileName));
			totalSize = entity.getContentLength();
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				String json = EntityUtils.toString(response.getEntity());
				result = new JSONObject(json);
			} else {
				Log.d("DEBUG", "HTTP Fail, Response Code: " + statusCode);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		listener.progress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		listener.response(result);
	}
}