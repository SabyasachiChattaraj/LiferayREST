package common.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.liferay.portal.kernel.json.JSONFactoryUtil;


/**
 * @author Sabyasachi
 */
public class CommonServiceUtil {
	


	public static Object callREST(String uri, Class clazz) throws Exception {
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		int responsecode = conn.getResponseCode();
		String json="";
		if(responsecode != 200)
			throw new RuntimeException("HttpResponseCode: " +responsecode);
			else
			{
				Scanner sc = new Scanner(url.openStream());
				while(sc.hasNext())
				{
				json+=sc.nextLine();
				}
				System.out.println("\nJSON data in string format");
				System.out.println(json);
				sc.close();

			}
		return JSONFactoryUtil.looseDeserialize(json, clazz);
		
		
	}

}