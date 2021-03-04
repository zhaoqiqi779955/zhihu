package utils;

import java.util.HashMap;
import java.util.Map;

/** 提供简单的 Content-Type 识别功能 
 * 
 * @author shaofa
 *
 */
public class MimeUtil
{
	public static Map<String,String> types = new HashMap<>();
	
	static {
		// 常见的文件类型
		String[][] commonTypes = {
				{"htm", "text/html"},
				{"html", "text/html"},
				{"jpg", "image/jpeg"},
				{"jpeg", "image/jpeg"},
				{"gif", "image/gif"},
				{"png", "image/png"},
				{"txt", "text/plain"},
				{"xml", "text/xml"},
				{"js", "application/javascript"},
				{"css", "text/css"},
				{"jsp", "text/html"},
				{"jsp", "text/jsp"},
		};
		for(int i=0; i<commonTypes.length; i++)
		{
			types.put(commonTypes[i][0], commonTypes[i][1]);
		}		
	}
	
	// 取得后缀
	public static String getSuffix(String path)
	{
		path = path.replace('\\', '/');
		int p1 = path.lastIndexOf('/');
		int p2 = path.lastIndexOf('.');
		if(p2 > p1)
		{
			return path.substring(p2+1);
		}
		
		return "";
	}
	
	public static String getContentType(String suffix)
	{
		suffix = suffix.toLowerCase();
		String type = types.get(suffix);
		if(type != null)
		{
			return type;
		}
		return "application/octet-stream";
	}
	
//	public static void main(String[] args)
//	{ 
//		        
//		String contentType = getContentType("js");
//		System.out.println("type: " + contentType);
//		
//	    System.out.println("Exit.");
//	}

}
