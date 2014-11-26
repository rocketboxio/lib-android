package io.rocketbox;

import java.util.List;

import org.apache.http.NameValuePair;

public class Rocketbox {

	private static Rocketbox instance;
	protected static String token = null;
	protected static String api_path = "https://www.rocketbox.io/api/v1/";

	public void setToken(String token) {
		Rocketbox.token = token;
	}

	public static Rocketbox getInstance() {
		if (instance == null) {
			instance = new Rocketbox();
		}
		return instance;
	}
	
	private Boolean isTokenValid(){
		if (Rocketbox.token != null) {
			return true;
		} else {
			System.err.println("Rocketbox - Please set your Token - Rocketbox.getInstance().setToken(' -- YOUR TOKEN HERE --')");
			return false;
		}
	}

	public void get(String key, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncGetValue asyn = new AsyncGetValue();
		asyn.setListener(listener);
		asyn.execute("get", key);
	}

	public void set(String key, String value, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncSetValue asyn = new AsyncSetValue();
		asyn.setListener(listener);
		asyn.execute("set", key, value);
	}

	public void delete(String key, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncDeleteValue asyn = new AsyncDeleteValue();
		asyn.setListener(listener);
		asyn.execute("delete", key);
	}

	public void getAll(RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncGetAllValue asyn = new AsyncGetAllValue();
		asyn.setListener(listener);
		asyn.execute("getAll");
	}

	public void CollectionAdd(String key, List<NameValuePair> parameters,
			RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionAdd asyn = new AsyncCollectionAdd();
		asyn.setListener(listener);
		asyn.setValues(parameters);
		asyn.execute("collection/add", key);
	}

	public void CollectionEdit(String key, List<NameValuePair> parameters,
			RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionEdit asyn = new AsyncCollectionEdit();
		asyn.setListener(listener);
		asyn.setValues(parameters);
		asyn.execute("collection/edit", key);
	}

	public void CollectionGet(String key, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionGet asyn = new AsyncCollectionGet();
		asyn.setListener(listener);
		asyn.execute("collection/get", key);
	}

	public void CollectionGetAll(RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionGetAll asyn = new AsyncCollectionGetAll();
		asyn.setListener(listener);
		asyn.execute("collection/getAll");
	}

	public void CollectionDelete(String id, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionDelete asyn = new AsyncCollectionDelete();
		asyn.setListener(listener);
		asyn.execute("collection/delete", id);
	}

	public void CollectionSearch(String key, List<NameValuePair> parameters,
			RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionSearch asyn = new AsyncCollectionSearch();
		asyn.setListener(listener);
		asyn.setValues(parameters);
		asyn.execute("collection/search", key);
	}

	public void CollectionSearchById(String id, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionSearchById asyn = new AsyncCollectionSearchById();
		asyn.setListener(listener);
		asyn.execute("collection/searchById", id);
	}

	public void CollectionDrop(String key, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncCollectionDrop asyn = new AsyncCollectionDrop();
		asyn.setListener(listener);
		asyn.execute("collection/drop", key);
	}
	
	public void SendMail(String to, String subject, String body, RocketboxListener listener) {
		if (!isTokenValid()){ return; }
		AsyncSendMail asyn = new AsyncSendMail();
		asyn.setListener(listener);
		asyn.execute("mail/send", to, subject, body);
	}

}
