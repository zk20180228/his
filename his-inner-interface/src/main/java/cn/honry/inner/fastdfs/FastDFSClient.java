package cn.honry.inner.fastdfs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import cn.honry.utils.ReaderProperty;

public class FastDFSClient {
	private static ReaderProperty readerProperty =  new ReaderProperty("config.properties");
	
	/** 文件上传访问地址 **/
	public static String baseImgUrl;
	/** 外网文件上传访问地址 **/
	public static String outBaseImgUrl;
	
	private TrackerClient trackerClient = null;
	private TrackerServer trackerServer = null;
	private StorageServer storageServer = null;
	private static StorageClient1 storageClient = null;
	
	public static String getBaseImgUrl(HttpServletRequest request){
		baseImgUrl = readerProperty.getValue_String("imgBaseUrl");
		outBaseImgUrl = readerProperty.getValue_String("outImgBaseUrl");
		if (request != null) {
			String remoteAddr = request.getHeader("Referer") != null ? request
					.getHeader("Referer").trim() : "";
			String outIP = "0000000000";
			if (outBaseImgUrl.replace("http://", "").contains(":")) {
				outIP = outBaseImgUrl.replace("http://", "");
				outIP = outIP.substring(0, outIP.indexOf(":"));
			} else {
				outIP = outBaseImgUrl.replace("http://", "");
				outIP = outIP.substring(0, outIP.indexOf("/"));
			}

			if (remoteAddr != null && remoteAddr.contains(outIP)) {
				return outBaseImgUrl;
			}
		}
		return baseImgUrl;
	}
	// fdfsclient的配置文件的路径
	public FastDFSClient() throws Exception {

		String confName = FastDFSClient.class.getResource("/").getPath();
		confName = confName + "fdfs_client.conf";
//		String confName = "E:\\java\\workspace\\his\\his-portal\\src\\main\\resources\\fdfs_client.conf";
		ClientGlobal.init(confName);
		trackerClient = new TrackerClient();
		trackerServer = trackerClient.getConnection();
		storageServer = null;
		storageClient = new StorageClient1(trackerServer, storageServer);
	}

	/**
	 * 上传文件方法
	 * <p>
	 * Title: uploadFile
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param fileName
	 *            文件全路径
	 * @param extName
	 *            文件扩展名，不包含（.）
	 * @param metas
	 *            文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
		String result = storageClient.upload_file1(fileName, extName, metas);
		return result;
	}

	public String uploadFile(String fileName) throws Exception {
		return uploadFile(fileName, null, null);
	}

	public String uploadFile(String fileName, String extName) throws Exception {
		return uploadFile(fileName, extName, null);
	}
	/**
	 * 上传文件方法
	 * <p>
	 * Title: uploadFile
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param fileContent
	 *            文件的内容，字节数组
	 * @param extName
	 *            文件扩展名
	 * @param metas
	 *            文件扩展信息
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {

		String result = storageClient.upload_file1(fileContent, extName, metas);
		return result;
	}

	public String uploadFile(byte[] fileContent) throws Exception {
		return uploadFile(fileContent, null, null);
	}
	public String uploadFile(byte[] fileContent, String extName) throws Exception {
		return uploadFile(fileContent, extName, null);
	}
    /**
     * 下载文件
     * @param fileId 文件ID（上传文件成功后返回的ID）
     * @param outFile 文件下载保存位置
     * @return
     */
    public static int downloadFile(String fileId, File outFile) {
        FileOutputStream fos = null;
        try {
            byte[] content = storageClient.download_file1(fileId);
            fos = new FileOutputStream(outFile);
            fos.write(content);
            fos.flush();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
    //查看文件信息  
    public static void getFileInfo(String groupName,String filepath) throws Exception{  
        System.out.println("获取文件信息=======================");  
        TrackerClient tracker = new TrackerClient();  
        TrackerServer trackerServer = tracker.getConnection();  
        StorageServer storageServer = null;  
  
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);  
        FileInfo fi = storageClient.get_file_info(groupName,filepath);  
        System.out.println("所在服务器地址:"+fi.getSourceIpAddr());  
        System.out.println("文件大小:"+fi.getFileSize());  
        System.out.println("文件创建时间:"+fi.getCreateTimestamp());  
        System.out.println("文件CRC32 signature:"+fi.getCrc32());  
    }  
      
    public static void getFileMate(String groupName,String filepath) throws Exception{  
        System.out.println("获取文件Mate=======================");  
        TrackerClient tracker = new TrackerClient();  
        TrackerServer trackerServer = tracker.getConnection();  
        StorageServer storageServer = null;  
  
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);  
        NameValuePair nvps[] = storageClient.get_metadata(groupName,filepath);  
        for (NameValuePair nvp : nvps) {  
                System.out.println(nvp.getName() + ":" + nvp.getValue());  
        }  
    }  
      
    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 删除失败返回-1，否则返回0
     */
    public static int deleteFile(String fileId) {
        try {
            return storageClient.delete_file1(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    /**
     * 
     * <p> </p>
     * @CreateDate: 2017年9月18日 下午4:47:37 
     * @ModifyDate: 2017年9月18日 下午4:47:37 
     * @ModifyRmk:  
     * @version: V1.0
     * @param:file 文件  filename 完整的文件名
     * @throws:
     * @return: String
     *
     */
    public  String uploadFile(File file,String fileName){
    	
    	try {
			FastDFSClient client = new FastDFSClient();
			String extName = null;
			if (fileName.contains(".")) {
				extName = fileName.substring(fileName.lastIndexOf(".")+1);
			}
			FileInputStream fileInputStream = new FileInputStream(file);
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			
			byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
			int rc = 0; 
			while ((rc = fileInputStream.read(buff, 0, 100)) > 0) { 
			swapStream.write(buff, 0, rc); 
			} 
			byte[] inputbyte = swapStream.toByteArray();
			
			String fileurl = client.uploadFile(inputbyte, extName);
			swapStream.close();
			fileInputStream.close();
			return fileurl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    public String uploadFile(InputStream inputStream, String fileName) {
		try {
			FastDFSClient client = new FastDFSClient();
			String extName = null;
			if (fileName.contains(".")) {
				extName = fileName.substring(fileName.lastIndexOf(".")+1);
			}
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			//buff用于存放循环读取的临时数据
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] inputbyte = swapStream.toByteArray();
			String fileurl = client.uploadFile(inputbyte, extName);
			swapStream.close();
			inputStream.close();
			return fileurl;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

}
