package cn.honry.inner.system.upload.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.honry.inner.system.upload.vo.FileResVo;

/**
 * @Description 文件上传service
 * @author  marongbin
 * @createDate： 2017年3月30日 下午2:35:30 
 * @modifier 
 * @modifyDate：
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public interface UploadFileService {
	/**
	 * @Description 
	 * @author  marongbin
	 * @createDate： 2017年3月30日 下午2:37:33 
	 * @modifier 
	 * @modifyDate：
	 * @param file 要上传的文件
	 * @param fileName 文件名
	 * @param comefrom 来源  0_his;1_oa;2_mobile
	 * @param acount 上传人  即登录系统的工号
	 * @return: String 返回全路径
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String fileUpload(File file,String fileName,String comefrom,String acount); 
	
	/**
     * 解析文件名称和路径
     * @param fileReason
     * @return
     */
    List<FileResVo> getFileReason(String fileReason,HttpServletRequest request);
    
    /**
	 * @Description apk上传，获取固定地址
	 * @author  zxl
	 * @createDate： 2017年3月30日 下午2:37:33 
	 * @modifier 
	 * @modifyDate：
	 * @param file 要上传的文件
	 * @param fileName 临时文件名
	 * @param comefrom 来源  0_his;1_oa;2_mobile
	 * @param acount 上传人  即登录系统的工号
	 * @param fixedFileName 固定文件名
	 * @return: String 返回全路径
	 * @modifyRmk：  
	 * @version 1.0
	 */
	String fileUploadFixed(File file,String fileName,String comefrom,String acount,String fixedFileName); 
}
