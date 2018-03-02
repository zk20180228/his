package cn.honry.oa.patInformation.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.Information;
import cn.honry.base.bean.model.InformationSubscripe;
import cn.honry.base.bean.model.SysEmployee;
import cn.honry.base.service.BaseService;
import cn.honry.oa.patInformation.vo.MenuCkeckedVO;
import cn.honry.oa.patInformation.vo.MenuVO;

public interface PatInformationService extends BaseService<Information> {
	/**  
	 * 
	 * <p>根据id获取栏目信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月17日 下午3:46:59 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月17日 下午3:46:59 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param id
	 * @return
	 * @throws:
	 * @return: MenuVO
	 *
	 */
	MenuVO queryMenuByid(String id);
	/**  
	 * 
	 * <p> 根据栏目id查询审核权限</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月17日 下午7:58:56 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月17日 下午7:58:56 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuid
	 * @return
	 * @throws:
	 * @return: MenuCkeckedVO
	 *
	 */
	List<MenuCkeckedVO> queryMenuCheckByid(String menuid ,String type);
	/**<p>根据栏目code和类型判断权限</>
	 * @param menuid
	 * @param type
	 * @return
	 */
	List<MenuCkeckedVO> judgeAuthBymenuCode(String menuid ,String type);
	/**  
	 * 
	 * <p> 将文本保存到mongoDB中</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月17日 下午6:12:32 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月17日 下午6:12:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param content
	 * @param menuID
	 * @throws:
	 * @return: void
	 *
	 */
	void insertIntoMongo(String content,String menuID);
	/**  
	 * <p>从mongoDB中删除 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月25日 下午9:38:35 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月25日 下午9:38:35 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param content
	 * @param menuID
	 * void
	 */
	void deleteIntoMongo(String menuID);
	/**  
	 * 
	 * <p>查询栏目权限下的人员 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 上午10:56:06 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 上午10:56:06 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId 栏目id
	 * @param post 职务
	 * @param title 职称
	 * @return
	 * @throws:
	 * @return: List<SysEmployee>
	 *
	 */
	List<SysEmployee> queryAuthEmp(String menuId,String post,String title,String page,String rows);
	/**  
	 * 
	 * <p>查询栏目权限下的人员总条数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 上午11:27:11 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 上午11:27:11 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @param post
	 * @param title
	 * @return
	 * @throws:
	 * @return: int
	 *
	 */
	int queryAuthEmpTotal(String menuId,String post,String title);
	/**  
	 * 
	 * <p>获取浏览权限（角色） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 下午7:46:31 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 下午7:46:31 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * @throws:
	 * @return: List<MenuCkeckedVO>
	 *
	 */
	List<MenuCkeckedVO> queryRoleAuth(String menuId);
	/**  
	 * 
	 * <p>获取浏览权限（科室） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月18日 下午8:09:46 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月18日 下午8:09:46 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * @throws:
	 * @return: List<MenuCkeckedVO>
	 *
	 */
	List<MenuCkeckedVO> queryDeptAuth(String menuId);
	/**  
	 * 
	 *  <p>获取浏览权限（职务） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月19日 上午10:07:04 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月19日 上午10:07:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * @throws:
	 * @return: List<MenuCkeckedVO>
	 *
	 */
	List<MenuCkeckedVO> queryDutyAuth(String menuId);
	/**  
	 * 
	 * <p> 保存审核信息</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月19日 下午3:17:26 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月19日 下午3:17:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param vo
	 * @throws:
	 * @return: void
	 *
	 */
	void saveInformationCheck(String menuId,String title,String infoid);
	/**删除审核信息
	 * @param infoid
	 */
	void deleteCkeck(String infoid);
	/**  
	 * 
	 * <p> 保存订阅信息</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月19日 下午3:17:26 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月19日 下午3:17:26 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param vo
	 * @throws:
	 * @return: void
	 *
	 */
	void saveInformationSubscripe(String menuId,String title,String infoid);
	/**  
	 * 
	 * <p>获取发布权限下的栏目 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月20日 下午3:21:09 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月20日 下午3:21:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode 科室code
	 * @param dutyCode 职务code
	 * @param roleCode 角色code
	 * @param acount 用户code
	 * @throws:
	 * @return: List<MenuVO>
	 *
	 */
	List<MenuVO> findMenuVo(String deptCode,String dutyCode,String roleCode,String acount);
	/**  
	 * 
	 * <p>检测当前用户是都有栏目的发布权限 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月22日 下午6:05:04 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月22日 下午6:05:04 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param deptCode
	 * @param dutyCode
	 * @param roleCode
	 * @param acount
	 * @return
	 * @throws:
	 * @return: Map<String,String>
	 *
	 */
	Map<String,String> checkAuth(String menuid,String deptCode,String dutyCode,String roleCode,String acount);
	
	/**  
	 * <p>查询文章信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午2:44:33 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午2:44:33 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param menuid
	 * @param page
	 * @param rowss
	 * @return
	 * List<Information>
	 */
	List<Information> findinformationList(String name,String menuid,String page,String rows,String type,String checkflag,String pubflag);
	/**  
	 * <p>总页数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午3:16:25 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午3:16:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param name
	 * @param menuid
	 * @param type
	 * @return
	 * int
	 */
	int findinformationTotal(String name,String menuid,String type,String checkflag,String pubflag);
	/**  
	 * <p> 根据id获取文章信息（包含附件权限信息）</p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月24日 下午5:33:03 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月24日 下午5:33:03 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuid
	 * @return
	 * Information
	 */
	Information getInformationMsg(String menuid);
	/**  
	 * <p>删除栏目信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月25日 下午1:43:39 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月25日 下午1:43:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * void
	 */
	void delInformation(String menuId);
	/**  
	 * <p>从mongoDB中取文本 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月25日 下午2:52:12 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月25日 下午2:52:12 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @return
	 * String
	 */
	String getContentFromMongo(String infoid);
	/**  
	 * <p>通过地址删除上传的附件（仅删除表里的数据） </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午5:38:09 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午5:38:09 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param fileurl
	 * void
	 */
	void deleteFile(String fileurl);
	/**  
	 * <p>更改已读 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月26日 下午7:58:54 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月26日 下午7:58:54 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param menuId
	 * @param acount
	 * void
	 */
	void updateSubscripe(String menuId,String acount);
	/**  
	 * <p>文章审核 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年7月27日 下午4:48:38 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年7月27日 下午4:48:38 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * @return
	 * Map<String,String>
	 */
	Map<String,String> informationAudit(String infoid,String type);
	/**  
	 * <p>获取查看权限下的所有信息 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年8月1日 上午9:52:44 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年8月1日 上午9:52:44 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return
	 * Map<String,Object>
	 */
	Map<String,Object> getInfomationView(String menuId,String page,String rows);
	/**保存文章
	 * @param info
	 * @param imgFile
	 * @param imgfilename
	 * @param file
	 * @param attachname
	 * @param authority
	 */
	void saveInfo(Information info,File imgFile,String imgfilename,List<File> file,String attachname,List<String> authority,String content);
	void editInfo(Information info,File imgFile,String imgfilename,List<File> file,String attachname,List<String> authority,String content,String oldfilename,String oldfileurl,List<String> oldfileauth);
	/**  
	 * <p>获取审核权限下的所有信息 </p>
	 * @Author: zpty
	 * @CreateDate: 2017年8月6日 上午9:52:44 
	 * @version: V1.0
	 * @return
	 * Map<String,Object>
	 */
	Map<String, Object> getInformationCheck(String menuId, String page,	String rows);
	/**  
	 * 获取当前信息的订阅表信息
	 * @Author: zpty
	 * @CreateDate: 2017年8月13日 上午9:52:44 
	 * @version: V1.0
	 * @return
	 * Map<String,Object>
	 */
	InformationSubscripe querySubscripe(String infoMenuid, String account);
	/**  
	 * <p>更新浏览次数 </p>
	 * @Author: mrb
	 * @CreateDate: 2017年11月18日 下午3:18:51 
	 * @Modifier: mrb
	 * @ModifyDate: 2017年11月18日 下午3:18:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param infoid
	 * void
	 */
	void updViews(String infoid);
}
