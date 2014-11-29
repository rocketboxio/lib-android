package io.rocketbox;

import org.json.JSONObject;

public abstract class RocketboxListenerUpload {

	public abstract void response(JSONObject json);
	public abstract void progress(int progress);
}
