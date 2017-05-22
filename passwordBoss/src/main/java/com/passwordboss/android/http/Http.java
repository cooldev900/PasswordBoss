package com.passwordboss.android.http;

import com.passwordboss.android.BuildConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;

public class Http {

	private HttpClient mHttpClient;

	public Http(){
		mHttpClient=new DefaultHttpClient();
		mHttpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "password-boss/mobile:android-" + BuildConfig.VERSION_NAME);
	}

	/*
	 *  This methods sets URL, headers, StringEntity to HttpPost
	 *  and executes request and and returns data in string format.
	 */
	public String runPostMethod(String url, HashMap<String, String> mHeaders,
			String mRequest) throws ClientProtocolException, IOException {
		
		HttpPost mHttpPost = new HttpPost(url);
		StringEntity mStringEntity;
		if (mHeaders != null){
			for (String key : mHeaders.keySet()) {
				mHttpPost.setHeader(key, mHeaders.get(key));
			}
		}
		if(mRequest!=null){
			mStringEntity=new StringEntity(mRequest);
			mHttpPost.setEntity(mStringEntity);
		}
		HttpResponse response =mHttpClient.execute(mHttpPost);
		HttpEntity httpEntity = response.getEntity();
		String responseString = EntityUtils.toString(httpEntity);
		return responseString;
	}
	
	/*
	 *  This methods sets URL, headers, StringEntity to HttpGet
	 *  and executes request and and returns data in string format.
	 */
	public String runGetMethod(String url, HashMap<String, String> mHeaders)
			throws ClientProtocolException, IOException {
		
		HttpGet mHttpGet = new HttpGet(url);
		if (mHeaders != null){
			for (String key : mHeaders.keySet()) {
				mHttpGet.setHeader(key, mHeaders.get(key));
			}
		}
		
		HttpResponse response = mHttpClient.execute(mHttpGet);
		HttpEntity httpEntity = response.getEntity();
		String responseString = EntityUtils.toString(httpEntity);
		return responseString;
	}


	public String runDeleteMethod(String url, HashMap<String, String> mHeaders, String mRequest) 
			throws ClientProtocolException, IOException {
		
		HttpDeleteWithBody mHttpDelete = new HttpDeleteWithBody(url);
		StringEntity mStringEntity;
		if (mHeaders != null){
			for (String key : mHeaders.keySet()) {
				mHttpDelete.setHeader(key, mHeaders.get(key));
			}
		}
		if(mRequest!=null){
			mStringEntity=new StringEntity(mRequest);
			mHttpDelete.setEntity(mStringEntity);
		}
		
		HttpResponse response = mHttpClient.execute(mHttpDelete);
		HttpEntity httpEntity = response.getEntity();
		String responseString = EntityUtils.toString(httpEntity);
		return responseString;
	}
	
	public String runPatchMethod(String url, HashMap<String, String> mHeaders, String mRequest)
			throws ClientProtocolException, IOException {
		
		HttpPatch mHttpPatch = new HttpPatch(url);
		StringEntity mStringEntity;
		if (mHeaders != null) {
			for (String key : mHeaders.keySet()) {
				mHttpPatch.setHeader(key, mHeaders.get(key));
				//android.util.Log.v("Test", key + "" + mHeaders.get(key));
			}
		}

		if(mRequest!=null) {
			mStringEntity = new StringEntity(mRequest);
			mHttpPatch.setEntity(mStringEntity);
		}
		
		HttpResponse response = mHttpClient.execute(mHttpPatch);
		HttpEntity httpEntity = response.getEntity();
		String responseString = EntityUtils.toString(httpEntity);
		return responseString;
	}
}
