package common.service;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Map;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Http.Options;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.Validator;

public class RestClientUtil {

	private static Log _log=LogFactoryUtil.getLog(RestClientUtil.class);
	public static Object getForObject(String uri, Class<?> responseType){
		Object responseObject=null;
		try {
			Options options=new Options();
			options.setLocation(uri);
			String responseObjectString = HttpUtil.URLtoString(options);
			responseObject=JSONFactoryUtil.looseDeserialize(responseObjectString,responseType);
		} catch (IOException e) {
			e.printStackTrace();
			_log.error("Error in RestClientUtil getForObject",e);
		}
		return responseObject;
	}	
	
	public static Object postForObject(String uri, Object requestObject, Class<?> responseType){
		Object responseObject=null;
		try {
			String requestObjectString=JSONFactoryUtil.looseSerialize(requestObject);
			Options options=new Options();
			options.setLocation(uri);
			options.setPost(Boolean.TRUE);
			options.addHeader(HttpHeaders.CONTENT_TYPE, ContentTypes.APPLICATION_JSON);
			options.setBody(requestObjectString, ContentTypes.APPLICATION_JSON, "UTF-8");
			String responseObjectString = HttpUtil.URLtoString(options);
			responseObject=JSONFactoryUtil.looseDeserialize(responseObjectString,responseType);
		} catch (IOException e) {
			e.printStackTrace();
			_log.error("Error in RestClientUtil postForObject",e);
		}
		return responseObject;
	}	
	
	public static Object postForObjectWithMultiPartRequest(String uri, Map<String,String> formDataMap,File uploadFile, Class<?> responseType){
		Object responseObject=null;
		try {
			Options options=new Options();
			options.setLocation(uri);
			options.setPost(Boolean.TRUE);
			//options.addHeader(HttpHeaders.CONTENT_TYPE, ContentTypes.MULTIPART_FORM_DATA+"; boundary=---" + System.currentTimeMillis() + "---");
			if(Validator.isNotNull(formDataMap)&&formDataMap.size()>0) {
				formDataMap.forEach((key,value)->{
					options.addPart(key, value);
				});
			}
			options.addFilePart("uploadFile", uploadFile.getName(), Files.readAllBytes(uploadFile.toPath()), ContentTypes.MULTIPART_FORM_DATA+"; boundary=---" + System.currentTimeMillis() + "---", "UTF-8");
			String responseObjectString = HttpUtil.URLtoString(options);
			responseObject=JSONFactoryUtil.looseDeserialize(responseObjectString,responseType);
		} catch (IOException e) {
			e.printStackTrace();
			_log.error("Error in RestClientUtil postForObjectWithMultiPartRequest",e);
		}
		return responseObject;
	}	
}
