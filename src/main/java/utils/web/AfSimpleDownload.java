package utils.web;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/** 简单的文件下载, 不支持 HTTP-Range 
 * 
 * @author shaofa
 * 如何设置了filename将不会显示而是直接下载
 */
public class AfSimpleDownload implements View
{
	/** 通常是一个文件流
	 *  
	 * */
	public InputStream inputStream;
	
	/** Content-Type 
	 * 默认为 application/octet-stream 
	 */
	public String contentType = "application/octet-Stream";
	
	/** Content-Length
	 * 可以不设置。默认按 Transfer-Encoding: chunked 编码发送
	 */
	public int contentLength = 0;	
	
	/** 下载时提示的文件名
	 * Content-Disposition: attachment; filename*=UTF-8''XXXX.jpg
	 */
	public String fileName; // 下载时提示保存的文件名
	
	/** 字符集
	 * 默认为 UTF-8
	 */
	public String charset = "UTF-8";
	
	/** 额外的头部
	 * 比如 Cache-Control
	 * 
	 */
	public Map<String,String> headers = new HashMap<>();
	
	
	public AfSimpleDownload(InputStream inputStream)
	{		
		this.inputStream = inputStream;
	}
	
	public AfSimpleDownload(InputStream inputStream,String contentType)
	{		
		this.inputStream = inputStream;
		this.contentType = contentType;
	}
	
	/** 模拟构造一个 js / css 文本的下载
	 * 
	 * @param content 内容
	 * @param contentType 内容类型 text/css , application/javascript
	 * @param charset "UTF-8"
	 */
	public AfSimpleDownload(String content, String contentType, String charset)
	{		
		this.charset = charset;
		this.contentType = contentType;
		this.headers.put("Cache-Control", "no-cache");// 伪静态文件禁止缓存
		
		try {
			byte[] bbb = content.getBytes(charset);
			this.inputStream = new ByteArrayInputStream(bbb);
			this.contentLength = bbb.length;
		}catch(Exception e)
		{
			// 字符编码错误，一般不会发生
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void render(Map<String, ?> model
			, HttpServletRequest req
			, HttpServletResponse resp) throws Exception
	{		
		resp.setContentType(contentType);
		resp.setCharacterEncoding(charset);

		// 如果不设置 Content-Length，则默认为 Transfer-Encoding: chunked 
		if(contentLength > 0)
		{
			resp.setContentLength( contentLength);
		}
		
		// 有的业务场景，下载要求提示一个文件名
		if(fileName != null)
		{
			String name = URLEncoder.encode(fileName, charset);
			String disposition = String.format("attachment; filename*=%s''%s"
					, charset, name);
			resp.setHeader("Content-Disposition", disposition);
		}
		
		// 额外的头部
		if(this.headers != null)
		{
			for(String key : headers.keySet())
			{
				String value = headers.get(key);
				resp.setHeader(key, value);
			}
		}
		
		// 发送数据给客户端
		OutputStream outputStream = resp.getOutputStream();		
		try
		{
			byte[] buf = new byte[8192];
			while (true)
			{
				int n = inputStream.read(buf);
				if (n <= 0)	break;
				
				outputStream.write(buf, 0, n);
			}
		}finally
		{
			// 关闭输入流 ( 通常是一个文件流 )
			try{ inputStream.close();} catch(Exception ex){}
		}
		
		outputStream.close();		
	}
	


	/////////////////////////////////////////////	
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public void setInputStream(InputStream inputStream)
	{
		this.inputStream = inputStream;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}
	public void setContentLength(int contentLength)
	{
		this.contentLength = contentLength;
	}

}
