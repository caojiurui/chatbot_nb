package pers.cjr.chatbot.nb.util.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pers.cjr.chatbot.nb.biz.utils.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class HttpClientUtil {

	private Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private int timeoutDuration = 1000*60*3;	//5分钟

	public Map<String,Object> getOCR(String url) {
		try {
			String result = this.get(url);
			Map<String,Object> map = JSONObject.parseObject(result);
			if(MapUtil.getInteger(map,"error") == 0){
				return map;
			}else{
				throw new Exception("请求错误返回 >>>>>> respone:"+ JSONUtil.objectToJsonStr(map));
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+",请求失败,url："+url);
			e.printStackTrace();
		}
		return null;
	}

	public Map<String,Object> postOCR(String url, Map<String, Object> args,Boolean existFileFlag) {
		try {
			String result = this.post(url,args,existFileFlag);
			Map<String,Object> map = JSONObject.parseObject(result);
			if(MapUtil.getInteger(map,"error") == 0){
                return map;
            }else{
				throw new Exception("请求错误返回 >>>>>> respone:"+ JSONUtil.objectToJsonStr(map));
            }
		} catch (Exception e) {
			logger.error(e.getMessage()+",请求失败,url："+url+",args:"+ StringUtil.toShortString(JSONUtil.objectToJsonStr(args),1000));
			e.printStackTrace();
		}
		return null;
	}

	public String post(String url, Map<String, Object> args,Boolean existFileFlag) throws Exception {

		HttpPost post = new HttpPost(url);

		post.setConfig(getDefaultRequestConfig());

		if(args != null && args.size() > 0) {
			if(existFileFlag){
				MultipartEntity reqEntity = new MultipartEntity();
				for (String key: args.keySet()) {
					Object value = args.get(key);
					if(value != null){
						if(value instanceof File){	//文件类型
							FileBody fb = new FileBody((File) value);
							reqEntity.addPart(key, fb);
						}else{
							StringBody sb = new StringBody(String.valueOf(value));
							reqEntity.addPart(key, sb);
						}
					}
				}
				post.setEntity(reqEntity);
			}else {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for(Iterator<String> it = args.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					if(args.get(key) != null) {
						formparams.add(new BasicNameValuePair(key, args.get(key).toString()));
					}
				}
				HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");
				post.setEntity(reqEntity);
			}
		}

		post.setConfig(getDefaultRequestConfig());

		HttpClient client = HttpClients.createDefault();

		HttpResponse response = client.execute(post);

		int status = response.getStatusLine().getStatusCode();
		HttpEntity resEntity = response.getEntity();
		String message = EntityUtils.toString(resEntity, "utf-8");
        try {
            logger.debug("post 请求 >>>>> 【url】:{},【param】:{},【response】:{}",url,StringUtil.toShortString(JSONUtil.objectToJsonStr(args),1000),message);
            if (status == 200) { // 正常返回
                return message.toString();
            }else{
                throw new HttpException("post 请求异常 >>>>>> 【url】:{"+url+"},【param】:{"+StringUtil.toShortString(JSONUtil.objectToJsonStr(args),1000)+"},【response】:{"+message+"}");
            }
        } catch (HttpException e) {
            throw e;
        } finally {
            post.releaseConnection();
        }

	}

	public String get(String url) throws Exception {

		HttpGet get = new HttpGet(url);

		get.setConfig(getDefaultRequestConfig());

		HttpClient client = HttpClients.createDefault();

		HttpResponse response = client.execute(get);

		int status = response.getStatusLine().getStatusCode();
		HttpEntity resEntity = response.getEntity();
		String message = EntityUtils.toString(resEntity, "utf-8");

		try {
			logger.debug("get 请求 >>>>>> 【url】:{},【response】:{}",url,message);
			if (status == 200) { // 正常返回
                return message.toString();
            }else{
                throw new HttpException("get 请求异常 >>>>>> 【url】:{"+url+"},【response】:{"+message+"}");
            }
		} catch (HttpException e) {
            throw e;
		} finally {
            get.releaseConnection();
		}

	}

	public void downloadFile(String fileUrl,String filePath,String fileName,String ext) throws Exception {
		ext = ext == null ? StringUtil.getExtension(fileUrl) : ext;
		fileName = fileName == null ? new ShortIdGenUtil().generate() : fileName;
		try {
			if(FileUtil.checkDir(filePath)){
				//设置图片路径
				URL url = new URL(fileUrl);
				URLConnection conn = url.openConnection();
				InputStream inStream = conn.getInputStream();
				//下载图片
				FileOutputStream fs = new FileOutputStream(filePath+"/"+fileName+"."+ext);
				int byteread = 0;
				byte[] buffer = new byte[1204];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.close();

                logger.info("下载成功! >>>>>>>>>>>>>   "+fileUrl);
//				System.out.println("下载成功! >>>>>>>>>>>>>   "+fileUrl);
			}
		} catch (Exception e) {
            logger.error("下载失败! >>>>>>>>>>>>>   "+fileUrl);
//			System.err.println("下载失败! >>>>>>>>>>>>>   "+fileUrl);
			throw e;
		}
	}

	private RequestConfig getDefaultRequestConfig(){
		return RequestConfig.custom().setSocketTimeout(timeoutDuration)
									 .setConnectTimeout(timeoutDuration)
									 .setConnectionRequestTimeout(timeoutDuration)
									 .build();
	}
}