package plus.ojbk.qqsms;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientSms {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientSms.class);
	private static final String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=%s&random=%s";
	private static final  int appid = 1400123337; 
	private static final String appkey = "de2";
	private static final int templateId = 171000;
	
	public static void main(String[] args) {
		//System.err.println(SmsSemder("王小明","ojbkplus","1800000000"));
	}
	
	public static String SmsSemder(String name,String sign,String phoneNumber){
		long random = SmsSenderUtil.getRandom();
	    long now = SmsSenderUtil.getCurrentTime();
	    String smsurl=String.format(url,appid,random);
	    JSONObject tel = new JSONObject();
	    tel.put("nationcode", "86");
	    tel.put("mobile", phoneNumber);
	    List<String> params = Arrays.asList(name);
	    JSONObject obj = new JSONObject();
	    obj.put("ext", "");
	    obj.put("extend", "");
	    obj.put("params", params);
	    obj.put("sig", SmsSenderUtil.calculateSignature(appkey, random, now, phoneNumber));
	    obj.put("sign", sign);
	    obj.put("tel", tel);
	    obj.put("time", now);
	    obj.put("tpl_id", templateId);
	    return doPostForJson(smsurl, obj.toString());
	}
	
	
	public static String doPostForJson(String url,String jsonParams) {
		 
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60 * 1000).setConnectionRequestTimeout(60 * 1000)
				.setSocketTimeout(60 * 1000).setRedirectsEnabled(true).build();

		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-Type", "application/json"); //
		try {
			httpPost.setEntity(new StringEntity(jsonParams, ContentType.create("application/json", "utf-8")));
			LOGGER.info("request parameters" + EntityUtils.toString(httpPost.getEntity()));
			HttpResponse response = httpClient.execute(httpPost);
			LOGGER.info(" code:" + response.getStatusLine().getStatusCode());
			HttpEntity httpEntity = response.getEntity();
			return EntityUtils.toString(httpEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"result\":500,\"errmsg\":\"exception\",\"ext\":\"\"}";
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
