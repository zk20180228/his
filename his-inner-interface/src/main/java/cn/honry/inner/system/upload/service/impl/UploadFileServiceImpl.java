package cn.honry.inner.system.upload.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.inner.system.upload.vo.FileResVo;
import cn.honry.inner.system.upload.vo.ReturnUploadImage;
import cn.honry.utils.HisParameters;

import com.google.gson.Gson;
@Service("uploadFileService")
@Transactional
@SuppressWarnings({ "all" })
public class UploadFileServiceImpl implements UploadFileService {
	private Logger logger=Logger.getLogger(UploadFileServiceImpl.class);
	@Override
	public String fileUpload(File file, String fileName,String comefrom,String acount) {
		HttpURLConnection conn = null;
		DataInputStream in = null;
		OutputStream out = null;
		try{
		String serverPath = HisParameters.getfileURI(null)+"/uploadFile/uploadfile.action?comefrom="+comefrom+"&acount="+acount;
		URL url = new URL(serverPath);
		// 换行符  
        final String newLine = "\r\n";  
        final String boundaryPrefix = "--";  
        // 定义数据分隔线  
        String BOUNDARY = "========7d4a6d158c9";  
        // 服务器的域名  
        conn = (HttpURLConnection) url.openConnection();  
        // 设置为POST情  
        conn.setRequestMethod("POST");  
        // 发送POST请求必须设置如下两行  
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setUseCaches(false);  
        // 设置请求头参数  
        conn.setRequestProperty("connection", "Keep-Alive");  
        conn.setRequestProperty("Charsert", "UTF-8");  
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  

        out = new DataOutputStream(conn.getOutputStream());  
        // 上传文件  
//        File file = new File(file.getPath());  
        StringBuilder sb = new StringBuilder();  
        sb.append(boundaryPrefix);  
        sb.append(BOUNDARY);  
        sb.append(newLine);  
        // 文件参数,upfile参数名可以随意修改  
        sb.append("Content-Disposition: form-data;name=\"upfile\";filename=\"" + file.getPath() + "\"" + newLine);  
        sb.append("Content-Type:application/octet-stream");  
        // 参数头设置完以后需要两个换行，然后才是参数内容  
        sb.append(newLine);  
        sb.append(newLine);  

        // 将参数头的数据写入到输出流中  
        out.write(sb.toString().getBytes());  
			 //写入文件名
        out.write(fileName.trim().getBytes());
        //写入分隔符
        out.write('|');
        // 数据输入流,用于读取文件数据  
        in = new DataInputStream(new FileInputStream(file));  
        byte[] bufferOut = new byte[1024];  
        int bytes = 0;  
        // 每次读1KB数据,并且将文件数据写入到输出流中  
        while ((bytes = in.read(bufferOut)) != -1) {  
            out.write(bufferOut, 0, bytes);  
        }  
        // 最后添加换行  
//        String[] fileNames = fileName.split("\\.");
//        String last = fileNames[fileNames.length-1];
//        if(StringUtils.isBlank(last) || !"apk".equals(last)){
//        	out.write(newLine.getBytes());  
//        }
        in.close();  

        // 定义最后数据分隔线，即--加上BOUNDARY再加上--。  
        byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine).getBytes();  
        // 写上结尾标识  
        out.write(end_data);  
        out.flush();  
        out.close();  
        logger.info("fileUpload 文件上传完毕！");
        //定义BufferedReader输入流来读取URL的响应  
        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); 
        StringBuilder builder = new StringBuilder();
        String rt = null;
        while((rt = reader.readLine())!=null){
        	builder.append(rt);
        }
        rt=builder.toString();
        logger.info("fileUpload 接收返回值开始..."+rt);
        CharSequence subSequence = rt.subSequence(rt.indexOf("\"url\":\"")+7, rt.indexOf(",", rt.indexOf("\"url\":\""))-1);
        String returnData = subSequence.toString();
        logger.info("fileUpload 返回值："+returnData);
		conn.disconnect();//关闭连接
		/*Gson gson = new Gson();
		ReturnUploadImage fromJson = gson.fromJson(rt,ReturnUploadImage.class);
		String returnData = fromJson.getUrl();*/
		return returnData;
	}catch (Exception e) {
		logger.info(e);
        e.printStackTrace();
    }finally{
    	if(conn!=null){
    		conn.disconnect();
    	}
    	try{
    		if(in!=null){
    			in.close();
    		}
    		if(out!=null){
    			out.flush();
    			out.close();
    		}
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    }
		return null;
	}

	/**
     * 解析文件名称和路径
     * @param fileReason
     * @return
     */
    public List<FileResVo> getFileReason(String fileReason,HttpServletRequest request){
    	List<FileResVo> list = new ArrayList<>();
    	if(StringUtils.isBlank(fileReason)){
    		return list;
    	}
    	String imgBaseUrl = FastDFSClient.getBaseImgUrl(request);
    	String uri = HisParameters.getfileURI(request);
    	String[] reasons = fileReason.split(";");
    	for (String res : reasons) {
    		int i = res.indexOf("#");
			if(StringUtils.isBlank(res)||i<=0){
				continue;
			}
			FileResVo vo = new FileResVo();
			vo.setName(res.substring(0,i));
			if (res!=null&&res.contains("group")) {
				vo.setUrl(imgBaseUrl+res.substring(i+1));
			}else {
				vo.setUrl(uri+res.substring(i+1));
			}
			list.add(vo);
		}
    	return list;
    }
    
    
	@Override
	public String fileUploadFixed(File file, String fileName,String comefrom,String acount,String fixedFileName) {
		HttpURLConnection conn = null;
		DataInputStream in = null;
		OutputStream out = null;
		try{
		String serverPath = HisParameters.getfileURI(null)+"/uploadFile/uploadfileToFixedFile.action?comefrom="+comefrom+"&acount="+acount+"&fixedFileName="+fixedFileName;
		URL url = new URL(serverPath);
		// 换行符  
        final String newLine = "\r\n";  
        final String boundaryPrefix = "--";  
        // 定义数据分隔线  
        String BOUNDARY = "========7d4a6d158c9";  
        // 服务器的域名  
        conn = (HttpURLConnection) url.openConnection();  
        // 设置为POST情  
        conn.setRequestMethod("POST");  
        // 发送POST请求必须设置如下两行  
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setUseCaches(false);  
        // 设置请求头参数  
        conn.setRequestProperty("connection", "Keep-Alive");  
        conn.setRequestProperty("Charsert", "UTF-8");  
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);  

        out = new DataOutputStream(conn.getOutputStream());  
        // 上传文件  
//        File file = new File(file.getPath());  
        StringBuilder sb = new StringBuilder();  
        sb.append(boundaryPrefix);  
        sb.append(BOUNDARY);  
        sb.append(newLine);  
        // 文件参数,upfile参数名可以随意修改  
        sb.append("Content-Disposition: form-data;name=\"upfile\";filename=\"" + file.getPath() + "\"" + newLine);  
        sb.append("Content-Type:application/octet-stream");  
        // 参数头设置完以后需要两个换行，然后才是参数内容  
        sb.append(newLine);  
        sb.append(newLine);  

        // 将参数头的数据写入到输出流中  
        out.write(sb.toString().getBytes());  
			 //写入文件名
        out.write(fileName.trim().getBytes());
        //写入分隔符
        out.write('|');
        // 数据输入流,用于读取文件数据  
        in = new DataInputStream(new FileInputStream(file));  
        byte[] bufferOut = new byte[1024];  
        int bytes = 0;  
        // 每次读1KB数据,并且将文件数据写入到输出流中  
        while ((bytes = in.read(bufferOut)) != -1) {  
            out.write(bufferOut, 0, bytes);  
        }  
        // 最后添加换行  
//        String[] fileNames = fileName.split("\\.");
//        String last = fileNames[fileNames.length-1];
//        if(StringUtils.isBlank(last) || !"apk".equals(last)){
//        	out.write(newLine.getBytes());  
//        }
        in.close();  

        // 定义最后数据分隔线，即--加上BOUNDARY再加上--。  
        byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine).getBytes();  
        // 写上结尾标识  
        out.write(end_data);  
        out.flush();  
        out.close();  
        //定义BufferedReader输入流来读取URL的响应  
        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); 
        StringBuilder builder = new StringBuilder();
        String rt = null;
        while((rt = reader.readLine())!=null){
        	builder.append(rt);
        }
        rt=builder.toString();
        logger.info("fileUploadFixed 接收返回值开始..."+rt);
        CharSequence subSequence = rt.subSequence(rt.indexOf("\"url\":\"")+7, rt.indexOf(",", rt.indexOf("\"url\":\""))-1);
        String returnData = subSequence.toString();
        logger.info("fileUploadFixed 返回值："+returnData);
		conn.disconnect();//关闭连接
		/*Gson gson = new Gson();
		ReturnUploadImage fromJson = gson.fromJson(rt,ReturnUploadImage.class);
		String returnData = fromJson.getUrl();*/
		return returnData;
	}catch (Exception e) {
		logger.info(e);
        e.printStackTrace();
    }finally{
    	if(conn!=null){
    		conn.disconnect();
    	}
    	try{
    		if(in!=null){
    			in.close();
    		}
    		if(out!=null){
    			out.flush();
    			out.close();
    		}
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    }
		return null;
	}
}
