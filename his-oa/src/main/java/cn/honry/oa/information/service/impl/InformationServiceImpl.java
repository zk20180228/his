package cn.honry.oa.information.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.InformationAttachDownAuth;
import cn.honry.base.bean.model.InformationCheck;
import cn.honry.base.bean.model.InformationSubscripe;
import cn.honry.base.bean.model.OFBatchPush;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.bean.model.User;
import cn.honry.hiasMongo.basic.MongoBasicDao;
import cn.honry.inner.baseinfo.employee.service.EmployeeInInterService;
import cn.honry.inner.fastdfs.FastDFSClient;
import cn.honry.inner.system.upload.service.UploadFileService;
import cn.honry.mq.MessageSend;
import cn.honry.oa.information.dao.InformationAttachDownAuthDao;
import cn.honry.oa.information.dao.InformationDao;
import cn.honry.oa.information.service.InformationAttachDownAuthService;
import cn.honry.oa.information.service.InformationService;
import cn.honry.oa.information.vo.MenuCkeckedVO;
import cn.honry.oa.information.vo.MenuVO;
import cn.honry.oa.information.vo.SubscripeVO;
import cn.honry.utils.DateUtils;
import cn.honry.utils.HisParameters;
import cn.honry.utils.JSONUtils;
import cn.honry.utils.ShiroSessionUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Service("informationService")
@Transactional
@SuppressWarnings({ "all" })
public class InformationServiceImpl implements InformationService {
	@Autowired
	@Qualifier(value = "informationDao")
	private InformationDao informationDao;
	@Autowired
	@Qualifier(value = "informationAttachDownAuthDao")
	private InformationAttachDownAuthDao informationAttachDownAuthDao;
	@Autowired
	@Qualifier(value = "employeeInInterService")
	private EmployeeInInterService employeeInInterService;
	@Autowired
	@Qualifier(value = "informationAttachDownAuthService")
	private InformationAttachDownAuthService informationAttachDownAuthService;
	@Autowired
	@Qualifier(value = "uploadFileService")
	private UploadFileService uploadFileService;
	@Resource
	private MessageSend messageSend;
	@Override
	public Information get(String arg0) {
		return informationDao.get(arg0);
	}

	@Override
	public void removeUnused(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOrUpdate(Information arg0) {
		Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
		String dept = null;
		String acount = null;
		SysDepartment department = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		if(department!=null){
			dept = department.getDeptCode();
		}
		if(user!=null){
			acount = user.getAccount();
		}
		String name = nameMap.get(acount);
		if(StringUtils.isBlank(arg0.getId())){
			arg0.setId(null);
			arg0.setCreateDept(dept);
			arg0.setCreateTime(new Date());
			arg0.setInfoPubtime(new Date());
			arg0.setCreateUser(acount);
			arg0.setInfoEditor(acount);
			arg0.setInfoPubuser(acount);
			arg0.setInfoWirteuser(acount);
			arg0.setUpdateTime(new Date());
			arg0.setUpdateUser(acount);
			arg0.setPubuserName(name);
			arg0.setWriterName(name);
			arg0.setEditorName(name);
			informationDao.save(arg0);
		}else{
			Information information = informationDao.get(arg0.getId());
			information.setInfoMenuid(arg0.getInfoMenuid());
			information.setInfoTitle(arg0.getInfoTitle());
			information.setInfoKeyword(arg0.getInfoKeyword());
			information.setInfoBrev(arg0.getInfoBrev());
			information.setOutTime(arg0.getOutTime());
			information.setTitleImg(arg0.getTitleImg());
			information.setInfoCheckFlag(arg0.getInfoCheckFlag());
			information.setInfoPubflag(arg0.getInfoPubflag());
			information.setPubuserName(name);
			information.setInfoPubuser(acount);
			informationDao.update(information);
		}
	}

	@Override
	public MenuVO queryMenuByid(String id) {
		return informationDao.findMenuByid(id);
	}

	@Override
	public void insertIntoMongo(String content, String menuID) {
		Document doc = new Document();
		doc.append("informationId", menuID);
		doc.append("content", content);
		new MongoBasicDao().insertData("INFOMATION_CONTENT", doc);
		
	}

	@Override
	public List<MenuCkeckedVO> queryMenuCheckByid(String menuid,String type) {
		return informationDao.queryMenuCheckByid(menuid,type);
	}

	@Override
	public List<SysEmployee> queryAuthEmp(String menuId, String post,
			String title,String page,String rows) {
		return informationDao.queryAuthEmp(menuId, post, title, page, rows);
	}

	@Override
	public int queryAuthEmpTotal(String menuId, String post, String title) {
		return informationDao.queryAuthEmpTotal(menuId, post, title);
	}

	@Override
	public List<MenuCkeckedVO> queryRoleAuth(String menuId) {
		return informationDao.queryRoleAuth(menuId);
	}

	@Override
	public List<MenuCkeckedVO> queryDeptAuth(String menuId) {
		return informationDao.queryDeptAuth(menuId);
	}

	@Override
	public List<MenuCkeckedVO> queryDutyAuth(String menuId) {
		return informationDao.queryDutyAuth(menuId);
	}
	@Override
	public void saveInformationCheck(String menuId,String title,String infoid) {
		List<MenuCkeckedVO> list = informationDao.queryMenuCheckByid(menuId, "2");//审核
		MenuVO menuVO = informationDao.getparentCodeAndName(menuId);
		InformationCheck check = null;
		informationDao.deleteCkeck(infoid);
		if(list!=null&&list.size()>0){
			for (MenuCkeckedVO vo : list) {//审核信息改为存个人 2017年8月4日18:12:22
				if(vo.getType()!=null&&!"1".equals(vo.getType())){//不是个人
					if("0".equals(vo.getType())){//所有人
						Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
						for (Entry<String, String> m :nameMap.entrySet()) {
							check = new InformationCheck();
							check.setInformationId(infoid);
							check.setPersonType(vo.getType());
							check.setCheckPersion(m.getKey());
							check.setIsCheck(0);
							informationDao.save(check);
							this.sedMessager(infoid,  DateUtils.formatDateY_M_D_H_M_S(new Date()), "[审核]"+title, "1", m.getKey(),menuId,"1",menuVO.getName(),menuVO.getParentCode(),menuVO.getParentName());
						}
					}else{
						List<SubscripeVO> findSub = informationDao.findSub(vo.getType(), vo.getCode());
						for (SubscripeVO su : findSub) {
							check = new InformationCheck();
							check.setInformationId(infoid);
							check.setPersonType("1");
							check.setCheckPersion(su.getCode());
							check.setIsCheck(0);
							informationDao.save(check);
							this.sedMessager(infoid, DateUtils.formatDateY_M_D_H_M_S(new Date()), "[审核]"+title, "1", su.getCode(),menuId,"1",menuVO.getName(),menuVO.getParentCode(),menuVO.getParentName());
						}
					}
				}else{
					check = new InformationCheck();
					check.setInformationId(infoid);
					check.setPersonType(vo.getType());
					check.setCheckPersion(vo.getCode());
					check.setIsCheck(0);
					informationDao.save(check);
					this.sedMessager(infoid,  DateUtils.formatDateY_M_D_H_M_S(new Date()), "[审核]"+title, "1", vo.getCode(),menuId,"1",menuVO.getName(),menuVO.getParentCode(),menuVO.getParentName());
				}
			}
		}
	}

	@Override
	public void saveInformationSubscripe(String menuId,String title,String infoid) {
		List<MenuCkeckedVO> list = informationDao.queryMenuCheckByid(menuId, "3");//订阅
		MenuVO menuVO = informationDao.getparentCodeAndName(menuId);
		InformationSubscripe sub = null;
		for (MenuCkeckedVO vo : list) {
			String rightType = vo.getType();
			if(StringUtils.isNotBlank(rightType)&&"0".equals(rightType)){//全部人员
				Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
				for (Entry<String, String> code : nameMap.entrySet()) {
					sub = new InformationSubscripe();
					sub.setInformationId(infoid);
					sub.setIsRead(0);
					sub.setSubscripePerson(code.getKey());
					sub.setType(vo.getType());
					informationDao.save(sub);
				}
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("info_id", infoid);
				map.put("info_pubtime", DateUtils.formatDateY_M_D_H_M_S(new Date()));
				map.put("info_title", title);
				map.put("info_type", 1);
				map.put("msg_type", "msg_type_notice_message");
				map.put("menuId", menuId);//栏目code
				map.put("jid", "all");
				map.put("menuName", menuVO.getName());//栏目name
				map.put("parMenuCode", menuVO.getParentCode());//第一级栏目code
				map.put("parMenuName", menuVO.getParentName());//第一级栏目NAME
				map.put("type", 0);
				OFBatchPush obp = new OFBatchPush();
				obp.setBody(JSONUtils.toJson(map));
				obp.setCreateTime(new Date());
				obp.setStatus(1);
				informationDao.save(obp);
			}else{
				if(vo.getType()!=null&&!"1".equals(vo.getType())){
					List<SubscripeVO> findSub = informationDao.findSub(vo.getType(), vo.getCode());
					for (SubscripeVO su : findSub) {
						sub = new InformationSubscripe();
						sub.setInformationId(infoid);
						sub.setIsRead(0);
						sub.setSubscripePerson(su.getCode());
						sub.setType("1");
						informationDao.save(sub);
						this.sedMessager(infoid, DateUtils.formatDateY_M_D_H_M_S(new Date()), title, "1", su.getCode(),menuId,"0",menuVO.getName(),menuVO.getParentCode(),menuVO.getParentName());
					}
				}else{
					sub = new InformationSubscripe();
					sub.setInformationId(infoid);
					sub.setIsRead(0);
					sub.setSubscripePerson(vo.getCode());
					sub.setType("1");
					informationDao.save(sub);
					this.sedMessager(infoid, DateUtils.formatDateY_M_D_H_M_S(new Date()), title, "1", vo.getCode(),menuId,"0",menuVO.getName(),menuVO.getParentCode(),menuVO.getParentName());
				}
			}
		}
	}
	/**  
	 * <p> </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月29日 上午11:34:30 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月29日 上午11:34:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param info_id 文章id
	 * @param info_pubtime 发布时间
	 * @param info_title 文章标题
	 * @param info_type 文章类型
	 * @param jid 发送给谁
	 * @param menuId 栏目id
	 * @param type  类型 0_订阅；1_审核 2_未通过
	 * @param menuName 栏目名称  
	 * @param parCode 一级栏目code
	 * @param parName  一级栏目name
	 * void
	 */
	public void sedMessager(String info_id,String info_pubtime,String info_title,String info_type,String jid,String menuId,String type,String menuName,String parCode,String parName){
		try {
			Map<String,String> map = new HashMap<String, String>();
			map.put("info_id", info_id);
			map.put("info_pubtime", info_pubtime);
			map.put("info_title", info_title);
			map.put("info_type", info_type);
			map.put("jid", jid);
			map.put("msg_type", "msg_type_notice_message");
			map.put("menuId", menuId);//栏目code
			map.put("menuName", menuName);//栏目name
			map.put("parMenuCode", parCode);//第一级栏目code
			map.put("parMenuName", parName);//第一级栏目NAME
			map.put("type", type);
			String json = JSONUtils.toJson(map);
			System.err.println(json);
			messageSend.sendMessage(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<MenuVO> findMenuVo(String deptCode, String dutyCode,
			String roleCode, String acount) {
		return informationDao.findMenuVo(deptCode, dutyCode, roleCode, acount);
	}

	@Override
	public Map<String, String> checkAuth(String menuid, String deptCode,
			String dutyCode, String roleCode, String acount) {
		Map<String,String> map = new HashMap<String, String>();
		List<MenuCkeckedVO> list = informationDao.checkAuth(menuid, deptCode, dutyCode, roleCode, acount);
		if(list!=null&&list.size()>0){
			map.put("resCode", "success");
		}else{
			map.put("resCode", "error");
		}
		return map;
	}

	@Override
	public List<Information> findinformationList(String name, String menuid,
			String page, String rows, String type,String checkflag,String pubflag) {
		return informationDao.queryInformationList(name, menuid, page, rows,checkflag,pubflag);
	}

	@Override
	public int findinformationTotal(String name, String menuid, String type,String checkflag,String pubflag) {
		return informationDao.queryInformationTotal(name, menuid,checkflag,pubflag);
	}

	@Override
	public Information getInformationMsg(String menuid) {
		Map<String,List<InformationAttachDownAuth>> map = new HashMap<String, List<InformationAttachDownAuth>>();
		Information info = informationDao.get(menuid);
		List<InformationAttachDownAuth> list = informationAttachDownAuthDao.getAuthByMenuID(info.getId(),false);
		for (InformationAttachDownAuth auth : list) {//根据文件地址分类
			if(map.containsKey(auth.getFileURL())){
				List<InformationAttachDownAuth> list2 = map.get(auth.getFileURL());
				list2.add(auth);
				map.put(auth.getFileURL(), list2);
			}else{
				List<InformationAttachDownAuth> list3 =new ArrayList<InformationAttachDownAuth>();
				list3.add(auth);
				map.put(auth.getFileURL(), list3);
			}
		}
		if(!map.isEmpty()){
			String auth = "";
			String attachName = "";
			String attachURL = "";
			String authid = "";
			for (Entry<String, List<InformationAttachDownAuth>> m : map.entrySet()) {
				if(StringUtils.isNotBlank(attachURL)){
					attachURL += "#";
					attachName += "#";
					authid += "#";
					auth += ";";
				}
				attachURL += m.getKey();
				attachName += m.getValue().get(0).getFilename();
				Map<Integer,List<InformationAttachDownAuth>> aMap = new HashMap<Integer, List<InformationAttachDownAuth>>();
				for (InformationAttachDownAuth at : m.getValue()) {
					if(aMap.containsKey(at.getType())){
						List<InformationAttachDownAuth> alist = aMap.get(at.getType());
						alist.add(at);
						aMap.put(at.getType(), alist);
					}else{
						List<InformationAttachDownAuth> alist2 = new ArrayList<InformationAttachDownAuth>();
						alist2.add(at);
						aMap.put(at.getType(), alist2);
					}
				}
				String strauth = "";
				for(Entry<Integer,List<InformationAttachDownAuth>> am : aMap.entrySet()){
					if(StringUtils.isNotBlank(strauth)){
						strauth += "#";
					}
					String str = "";
					String strid = "";
					for (InformationAttachDownAuth au : am.getValue()) {
						if(StringUtils.isNotBlank(str)){
							str += ",";
							authid += ",";
						}
						str += au.getCode() + ":" +au.getName();
						authid += au.getId();
					}
					strauth += am.getKey()+"_"+str;
				}
				auth += strauth;
			}
			info.setAttach(auth);
			info.setAttachURL(attachURL);
			info.setAttachName(attachName);
			info.setAuthid(authid);
		}
		return info;
	}

	@Override
	public void delInformation(String menuId) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String acount = "";
		if(user!=null){
			acount = user.getAccount();
		}
		for (String menuid : menuId.split(",")) {
			Information information = informationDao.get(menuid);
			information.setDel_flg(1);
			information.setDeleteTime(new Date());
			information.setDeleteUser(acount);
			informationDao.update(information);
		}
	}

	@Override
	public String getContentFromMongo(String infoid) {
		BasicDBObject where = new BasicDBObject();
		where.put("informationId", infoid);
		DBCursor cursor = new MongoBasicDao().findAlldata("INFOMATION_CONTENT", where );
		String content = null;
		while(cursor.hasNext()){
			DBObject next = cursor.next();
			content = (String) next.get("content");
			System.out.println("InformationServiceImpl.获取代理服务器IP：  "+HisParameters.getfileURI(ServletActionContext.getRequest())+"/upload/");
			content = content.replace("/upload/", HisParameters.getfileURI(ServletActionContext.getRequest())+"/upload/");
		}
		return content;
	}

	@Override
	public void deleteIntoMongo(String menuID) {
		BasicDBObject where = new BasicDBObject();
		where.put("informationId", menuID);
		new MongoBasicDao().remove("INFOMATION_CONTENT", where);
	}

	@Override
	public void deleteFile(String fileurl) {
		int i = informationAttachDownAuthDao.deleteFile(fileurl);
	}

	@Override
	public void updateSubscripe(String menuId, String acount) {
		informationDao.updateSubscripe(menuId, acount);
	}

	@Override
	public Map<String, String> informationAudit(String infoid,String type) {
		Map<String,String> map = new HashMap<String, String>();
		Information info = informationDao.get(infoid);
		if(info.getInfoCheckFlag()!=null&&info.getInfoCheckFlag()!=0){//已审核
			map.put("resCode", "success");
			map.put("resMsg", "该信息已审核!");
		}else{
			Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
			User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
			MenuVO menuVO = informationDao.getparentCodeAndName(info.getInfoMenuid());
			String acount = "";
			if(user!=null){
				acount = user.getAccount();
			}
			String name = nameMap.get(acount);
			info.setInfoChecker(acount);
			info.setCheckerName(name);
			info.setInfoPubtime(new Date());
			if(StringUtils.isNotBlank(type)&&"0".equals(type)){//未通过
				info.setInfoPubflag(1);//未通过也必须是发布状态
				info.setInfoCheckFlag(2);
				this.sedMessager(info.getId(), DateUtils.formatDateY_M_D_H_M_S(new Date()), "[退回]"+info.getInfoTitle(), "1", info.getInfoWirteuser(),info.getInfoMenuid(),"2",menuVO.getName(),menuVO.getParentCode(),menuVO.getParentName());
			}else{
				info.setInfoPubflag(1);
				info.setInfoCheckFlag(1);
				this.saveInformationSubscripe(info.getInfoMenuid(), info.getInfoTitle(), info.getId());
			}
			informationDao.update(info);
			map.put("resCode", "success");
			map.put("resMsg", "操作成功!");
		}
		return map;
	}

	@Override
	public Map<String, Object> getInfomationView(String menuId,String page,String rows) {
		Map<String,Object> map = new HashMap<String, Object>();
		int total = informationDao.getInfomationViewTotal(menuId);
		List<Information> list = informationDao.getInfomationView(menuId, page, rows);
		Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
		for (Information information : list) {
			information.setPubuserName(nameMap.get(information.getInfoPubuser()));
			information.setWriterName(nameMap.get(information.getInfoWirteuser()));
			information.setEditorName(nameMap.get(information.getInfoEditor()));
			information.setCheckerName(nameMap.get(information.getInfoChecker()));
		}
		map.put("rows", list);
		map.put("total", total);
		return map;
	}
	
	@Override
	public Map<String, Object> getInformationCheck(String menuId,String page,String rows) {
		Map<String,Object> map = new HashMap<String, Object>();
		int total = informationDao.getInformationCheckTotal(menuId);
		List<Information> list = informationDao.getInformationCheck(menuId, page, rows);
		Map<String, String> nameMap = employeeInInterService.queryEmpCodeAndNameMap();
		for (Information information : list) {
			information.setPubuserName(nameMap.get(information.getInfoPubuser()));
			information.setWriterName(nameMap.get(information.getInfoWirteuser()));
			information.setEditorName(nameMap.get(information.getInfoEditor()));
			information.setCheckerName(nameMap.get(information.getInfoChecker()));
		}
		map.put("rows", list);
		map.put("total", total);
		return map;
	}
	@Override
	public List<MenuCkeckedVO> judgeAuthBymenuCode(String menuid, String type) {
		return informationDao.judgeAuthBymenuCode(menuid, type);
	}

	@Override
	public void deleteCkeck(String infoid) {
		informationDao.deleteCkeck(infoid);
	}

	@Override
	public void saveInfo(Information info, File imgFile, String imgfilename,
			List<File> file, String attachname, List<String> authority,String content) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String acount = "";
		if(user!=null){
			acount = user.getAccount();
		}
		try{
			String fileUploadURL = null;
			InformationAttachDownAuth author = null;
			//标题图片上传filename
			if(imgFile!=null){
				FastDFSClient client = new FastDFSClient();
				String titleImgurl = client.uploadFile(imgFile, imgfilename);
//				String titleImgurl = uploadFileService.fileUpload(imgFile, imgfilename,"1",acount);
				info.setTitleImg(titleImgurl);
			}
			if(info.getInfoPubflag()!=null&&2!=info.getInfoPubflag()){//当不存为草稿时
				//查询栏目信息；根据栏目获取审核信息
				MenuVO menuVO = this.queryMenuByid(info.getInfoMenuid());
				if(menuVO!=null&&menuVO.getPublishDirt()==0){//不可以直接发布
					//审核处理
					info.setInfoPubflag(1);
					info.setInfoCheckFlag(0);
					this.saveOrUpdate(info);
					this.saveInformationCheck(info.getInfoMenuid(),info.getInfoTitle(),info.getId());
				}else{//直接发布
					info.setInfoPubflag(1);
					info.setInfoCheckFlag(1);
					this.saveOrUpdate(info);
					// 订阅信息处理
					this.saveInformationSubscripe(info.getInfoMenuid(),info.getInfoTitle(),info.getId());
				}
			}else{
				info.setInfoPubflag(2);
				info.setInfoCheckFlag(0);
				this.saveOrUpdate(info);
			}
			if(file!=null){//附件上传
				info.setInfoAttach(fileUploadURL);
				String[] attachName = attachname.split("#");
				int i = 0;
				for (File file2 : file) {
					String fname = attachName[i];
					String flname = fname.substring(fname.lastIndexOf("\\"), fname.length());
//					fileUploadURL = uploadFileService.fileUpload(file2,flname,"1",acount);
					FastDFSClient client = new FastDFSClient();
					fileUploadURL = client.uploadFile(file2, flname);
					if(authority!=null){//附件权限处理
						String[] auth = authority.get(i).split("#");
						if(auth.length>0){
							for (String str : auth) {
								String[] typeauth = str.split("_");
								if(typeauth.length>0&&StringUtils.isNoneBlank(typeauth[0])){
									if(typeauth.length>=2){
										String type = typeauth[0];
										String code = typeauth[1];
										if(StringUtils.isNotBlank(code)){
											for (String cd : code.split(",")) {
												author = new InformationAttachDownAuth();
												author.setFileURL(fileUploadURL);
												author.setFilename(flname);
												author.setInformationId(info.getId());
												author.setType(StringUtils.isNotBlank(type)?Integer.valueOf(type):null);
												author.setCode(cd.split(":")[0]);
												author.setName(cd.split(":")[1]);
												informationAttachDownAuthService.saveOrUpdate(author);
											}
										}
									}
								}else{
									author = new InformationAttachDownAuth();
									author.setFileURL(fileUploadURL);
									author.setFilename(flname);
									author.setInformationId(info.getId());
									author.setType(0);//0表示全部人员
									author.setCode("all");
									author.setName("全部人员");
									informationAttachDownAuthService.saveOrUpdate(author);
								}
							}
						}
					}else{
						author = new InformationAttachDownAuth();
						author.setFileURL(fileUploadURL);
						author.setFilename(flname);
						author.setInformationId(info.getId());
						author.setType(0);//0表示全部人员
						author.setCode("all");
						author.setName("全部人员");
						informationAttachDownAuthService.saveOrUpdate(author);
					}
					i++;
				}
			}
			//将大文本数据放到mongoDB中
			this.insertIntoMongo(content, info.getId());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void editInfo(Information info, File imgFile, String imgfilename,
			List<File> file, String attachname, List<String> authority,
			String content, String oldfilename, String oldfileurl,
			List<String> oldfileauth) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		String acount = "";
		if(user!=null){
			acount = user.getAccount();
		}
		try{
			String fileUploadURL = null;
			//标题图片上传filename
			if(imgFile!=null){
				FastDFSClient client = new FastDFSClient();
				String titleImgurl = client.uploadFile(imgFile, imgfilename);
//				String titleImgurl = uploadFileService.fileUpload(imgFile, imgfilename,"1",acount);
				info.setTitleImg(titleImgurl);
			}
			if(info.getInfoPubflag()!=null&&2!=info.getInfoPubflag()){//当不存为草稿时
				//查询栏目信息；根据栏目获取审核信息
				MenuVO menuVO = this.queryMenuByid(info.getInfoMenuid());
				if(menuVO!=null&&menuVO.getPublishDirt()==0){//不可以直接发布
					//审核处理
					info.setInfoPubflag(1);
					info.setInfoCheckFlag(0);
					this.saveOrUpdate(info);
					this.saveInformationCheck(info.getInfoMenuid(),info.getInfoTitle(),info.getId());
				}else{//直接发布
					info.setInfoPubflag(1);
					info.setInfoCheckFlag(1);
					// 订阅信息处理
					this.saveOrUpdate(info);
					this.saveInformationSubscripe(info.getInfoMenuid(),info.getInfoTitle(),info.getId());
				}
			}else{
				info.setInfoPubflag(2);
				info.setInfoCheckFlag(0);
				this.saveOrUpdate(info);
			}
			informationAttachDownAuthService.delAuth(info.getId());
			if(file!=null){//附件上传
				info.setInfoAttach(fileUploadURL);
				String[] attachName = attachname.split("#");
				int i = 0;
				for (File file2 : file) {
					String fname = attachName[i];
					String flname = fname.substring(fname.lastIndexOf("\\")+1, fname.length());
//					fileUploadURL = uploadFileService.fileUpload(file2,flname,"1",acount);
					FastDFSClient client = new FastDFSClient();
					fileUploadURL = client.uploadFile(file2, flname);
					if(authority!=null){//附件权限处理
						String[] auth = authority.get(i).split("#");
						if(auth.length>0){
							for (String str : auth) {
								String[] typeauth = str.split("_");
								if(typeauth.length>0&&StringUtils.isNoneBlank(typeauth[0])&&typeauth.length>=2){
									String type = typeauth[0];
									String code = typeauth[1];
									if(StringUtils.isNotBlank(code)){
										for (String cd : code.split(",")) {
											InformationAttachDownAuth author = new InformationAttachDownAuth();
											author.setFileURL(fileUploadURL);
											author.setFilename(flname);
											author.setInformationId(info.getId());
											author.setType(StringUtils.isNotBlank(type)?Integer.valueOf(type):null);
											author.setCode(cd.split(":")[0]);
											author.setName(cd.split(":")[1]);
											informationAttachDownAuthService.saveOrUpdate(author);
										}
									}
								}else{
									InformationAttachDownAuth author = new InformationAttachDownAuth();
									author.setFileURL(fileUploadURL);
									author.setFilename(flname);
									author.setInformationId(info.getId());
									author.setType(0);//0表示全部人员
									author.setCode("all");
									author.setName("全部人员");
									informationAttachDownAuthService.saveOrUpdate(author);
								}
							}
						}
					}else{
						InformationAttachDownAuth author = new InformationAttachDownAuth();
						author.setFileURL(fileUploadURL);
						author.setFilename(flname);
						author.setInformationId(info.getId());
						author.setType(0);//0表示全部人员
						author.setCode("all");
						author.setName("全部人员");
						informationAttachDownAuthService.saveOrUpdate(author);
					}
					i++;
				}
			}
			if(oldfileauth!=null&&oldfileauth.size()>0){
				String[] oldname = oldfilename.split("#");
				String[] oldurl = oldfileurl.split("#");
				int i = 0;
				for (String auth : oldfileauth) {
					String[] auths = auth.split("#");
					for(String str : auths){
						String[] type_Value = str.split("_");
						if(type_Value!=null&&type_Value.length>1){
							String type = type_Value[0];
							String code = type_Value[1];
							if(StringUtils.isNotBlank(code)){
								for (String cd : code.split(",")) {
									InformationAttachDownAuth author = new InformationAttachDownAuth();
									author.setFileURL(oldurl[i]);
									author.setFilename(oldname[i]);
									author.setInformationId(info.getId());
									author.setType(StringUtils.isNotBlank(type)?Integer.valueOf(type):null);
									author.setCode(cd.split(":")[0]);
									author.setName(cd.split(":")[1]);
									informationAttachDownAuthService.saveOrUpdate(author);
								}
							}
						}
					}
					i++;
				}
				
			}
			//将大文本数据放到mongoDB中
			this.deleteIntoMongo(info.getId());
			this.insertIntoMongo(content, info.getId());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public InformationSubscripe querySubscripe(String infoMenuid, String account) {
		return informationDao.querySubscripe(infoMenuid,account);
	}

	@Override
	public void updViews(String infoid) {
		informationDao.updViews(infoid);
	}
	
}
