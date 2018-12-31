package common.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.Http.Options;
import com.liferay.portal.kernel.util.HttpUtil;


/**
 * @author Sabyasachi
 */
public class CommonServiceUtil {
	
	 private static String boundary = "===" + System.currentTimeMillis() + "===";
	    private static final String LINE_FEED = "\r\n";
	    private HttpURLConnection httpConn;
	    private String charset;
	    private OutputStream outputStream;
	    private PrintWriter writer;
	  
	   

	public static Object getForObject(String uri, Class<?> responseType) throws Exception {
		URL url = new URL(uri);
		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
		httpConnection.setRequestMethod("GET");
		httpConnection.connect();
		int responsecode = httpConnection.getResponseCode();
		if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : "+ httpConnection.getResponseCode());
        }
		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
        String responseObjectString;
        while ((responseObjectString = responseBuffer.readLine()) != null) {
            ;
        }
        responseBuffer.close();
        httpConnection.disconnect();
        Object responseObject=JSONFactoryUtil.looseDeserialize(responseObjectString, responseType);
        return responseObject;
	}
	
	public static Object postForObject(String uri, Object requestObject, Class<?> responseType) throws Exception {
		String requestObjectString=JSONFactoryUtil.looseSerialize(requestObject);
		
		URL url = new URL(uri);
		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/json");
		httpConnection.connect();
		
		OutputStream outputStream = httpConnection.getOutputStream();
		outputStream.write(requestObjectString.getBytes());
		outputStream.flush();
		
		if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "+ httpConnection.getResponseCode());
        }

		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
				 
        String responseObjectString;
        while ((responseObjectString = responseBuffer.readLine()) != null) {
            ;
        }
        responseBuffer.close();
        httpConnection.disconnect();
		Object responseObject=JSONFactoryUtil.looseDeserialize(responseObjectString, responseType);
        return responseObject;
	}
	
	
	public static Object postForObjectWithMutipartRequest(String uri, Object requestObject, File uploadFile, Class<?> responseType) throws Exception {
		String requestObjectString=JSONFactoryUtil.looseSerialize(requestObject);
		
		URL url = new URL(uri);
		HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/json");
		httpConnection.connect();
		
		OutputStream outputStream = httpConnection.getOutputStream();
		outputStream.write(requestObjectString.getBytes());
		outputStream.flush();
		
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"),true);
		String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        String fieldName="uploadFile";
		writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
 
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
         
        writer.append(LINE_FEED);
        writer.flush();
        
        
		if (httpConnection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "+ httpConnection.getResponseCode());
        }

		BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
				 
        String responseObjectString;
        while ((responseObjectString = responseBuffer.readLine()) != null) {
            ;
        }
        responseBuffer.close();
        httpConnection.disconnect();
		Object responseObject=JSONFactoryUtil.looseDeserialize(responseObjectString, responseType);
        return responseObject;
	}
	
	
	/**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }
    
    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    
    public static void main(String[] args) throws Exception {
		Options options=new Options();
		options.setLocation("https://jsonplaceholder.typicode.com/posts/1");
		String b=HttpUtil.URLtoString(options);
		System.out.println(b);
	}
    
    
    
    
    
    
}