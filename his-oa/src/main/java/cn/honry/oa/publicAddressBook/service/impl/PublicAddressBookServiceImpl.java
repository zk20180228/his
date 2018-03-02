package cn.honry.oa.publicAddressBook.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.PublicAddressBook;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysMenu;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.baseinfo.department.dao.DeptInInterDAO;
import cn.honry.inner.baseinfo.department.service.DeptInInterService;
import cn.honry.inner.system.utli.OperationUtils;
import cn.honry.oa.publicAddressBook.dao.PublicAddressBookDAO;
import cn.honry.oa.publicAddressBook.service.PublicAddressBookService;
import cn.honry.utils.ShiroSessionUtils;
import cn.honry.utils.TreeJson;
@Service("publicAddressBookService")
@Transactional
@SuppressWarnings({"all"})
public class PublicAddressBookServiceImpl implements PublicAddressBookService{
	@Autowired
	@Qualifier(value="innerCodeService")
	private CodeInInterService  innerCodeService;
	public void setInnerCodeService(CodeInInterService innerCodeService) {
		this.innerCodeService = innerCodeService;
	}
	@Autowired
	@Qualifier(value = "deptInInterService")
	private DeptInInterService deptInInterService;
	public void setDeptInInterService(DeptInInterService deptInInterService) {
		this.deptInInterService = deptInInterService;
	}
	@Autowired
	@Qualifier("publicAddressBookDAO")
	private PublicAddressBookDAO publicAddressBookDAO;
	@Resource
	private CodeInInterDAO innerCodeDao;
	/**  
	 * 
	 * 公共通讯录树
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 上午10:37:25 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 上午10:37:25 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<TreeJson> findTree(String type) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		List<PublicAddressBook> publicBookList = publicAddressBookDAO.findTree();
		List<BusinessDictionary> areaList = innerCodeDao.getDictionary("hospitalArea");
		String rType="";
		//加入树的根节点
		TreeJson pTreeJson = new TreeJson();
		pTreeJson.setId("root");
		pTreeJson.setText("郑州大学第一附属医院");
		Map<String,String> rAttributes = new HashMap<String, String>();
		pTreeJson.setAttributes(rAttributes);
		treeJsonList.add(pTreeJson);
		if (publicBookList!=null && publicBookList.size()>0) {
			for (PublicAddressBook publicBook : publicBookList) {
				if(StringUtils.isNoneBlank(type)){
					if ("super".equals(type)) {
						//没有工作站的树
						if(!"55".equals(publicBook.getNodeType())){
							TreeJson treeJson = new TreeJson();
							treeJson.setId(publicBook.getId());
							treeJson.setText(publicBook.getName());
							Map<String,String> attributes = new HashMap<String, String>();
							attributes.put("pid",publicBook.getParentCode());
							attributes.put("nodeType",publicBook.getNodeType());
							treeJson.setAttributes(attributes);
							treeJsonList.add(treeJson);
						}
					}else{
						if(!"00".equals(type)){
							//没有选中节点以下的树
							if(!type.equals(publicBook.getNodeType())){
								TreeJson treeJson = new TreeJson();
								treeJson.setId(publicBook.getId());
								treeJson.setText(publicBook.getName());
								Map<String,String> attributes = new HashMap<String, String>();
								attributes.put("pid",publicBook.getParentCode());
								attributes.put("nodeType",publicBook.getNodeType());
								treeJson.setAttributes(attributes);
								treeJsonList.add(treeJson);
							}
						}
					}
				}else{
					TreeJson treeJson = new TreeJson();
					treeJson.setId(publicBook.getId());
					treeJson.setText(publicBook.getName());
					Map<String,String> attributes = new HashMap<String, String>();
					attributes.put("pid",publicBook.getParentCode());
					attributes.put("nodeType",publicBook.getNodeType());
					treeJson.setAttributes(attributes);
					treeJsonList.add(treeJson);
				}
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}
	@Override
	public PublicAddressBook get(String arg0) {
		return publicAddressBookDAO.get(arg0);
	}
	@Override
	public void removeUnused(String arg0) {
	}
	@Override
	public void saveOrUpdate(PublicAddressBook arg0) {
	}
	/**  
	 * 
	 * 修改、添加
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月17日 下午5:11:41 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月17日 下午5:11:41 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void saveOrupdataBook(PublicAddressBook publicAddressBook) {
		String userId = ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount();
		SysDepartment longinDept = ShiroSessionUtils.getCurrentUserLoginDepartmentFromShiroSession();
		String deptId = longinDept == null ? "" : longinDept.getDeptCode();
		if ("郑州大学第一附属医院".equals(publicAddressBook.getParentCode())) {
			publicAddressBook.setParentCode("root");
		}
		//科室类型赋值
		if ("33".equals(publicAddressBook.getNodeType())) {
			List<BusinessDictionary> dictionaryList = innerCodeService.getDictionary("depttype");
			for (BusinessDictionary businessDictionary : dictionaryList) {
				if (businessDictionary.getEncode().equals(publicAddressBook.getName())) {
					publicAddressBook.setName(businessDictionary.getName());
				}
			}
		}
		//科室名赋值
		if ("44".equals(publicAddressBook.getNodeType())) {
			List<SysDepartment> dept = deptInInterService.getDept();
			for (SysDepartment vo : dept) {
				if (vo.getDeptCode().equals(publicAddressBook.getName())) {
					publicAddressBook.setName(vo.getDeptName());
				}
			}
		}
		if(StringUtils.isEmpty(publicAddressBook.getId())){
			//添加
			Integer order = publicAddressBookDAO.getOrder("order");
			publicAddressBook.setOrder(order);//排序
			if ("root".equals(publicAddressBook.getParentCode())) {//父级为医院
				publicAddressBook.setPath(count(4,publicAddressBook.getOrder().toString().length())+publicAddressBook.getOrder()+",");
				publicAddressBook.setSuperPath("root,");//所有父级
				publicAddressBook.setNodeLevel(1);//层级
			}else{
				PublicAddressBook book = publicAddressBookDAO.get(publicAddressBook.getParentCode());//获得父类
				publicAddressBook.setPath(book.getPath()+count(4,publicAddressBook.getOrder().toString().length())+publicAddressBook.getOrder()+",");//层级路径
				if(StringUtils.isNotBlank(book.getSuperPath())){
					publicAddressBook.setSuperPath(book.getSuperPath()+book.getId()+",");//所有父级
					if ("44".equals(book.getNodeType())) {
						String superPath = book.getSuperPath();
						String[] paths = superPath.split(",");
						if (StringUtils.isNotBlank(paths[1])) {
							//院区
							PublicAddressBook addressBook1 = publicAddressBookDAO.get(paths[1]);
							publicAddressBook.setAreaName(addressBook1.getName());
						}
						if (StringUtils.isNotBlank(paths[2])) {
							//楼号
							PublicAddressBook addressBook2 = publicAddressBookDAO.get(paths[2]);
							publicAddressBook.setBuildingName(addressBook2.getName());
						}
						if (StringUtils.isNotBlank(paths[3])) {
							//楼层
							PublicAddressBook addressBook3 = publicAddressBookDAO.get(paths[3]);
							publicAddressBook.setFloorName(addressBook3.getName());
						}
						if (StringUtils.isNotBlank(paths[4])) {
							//科室类型
							PublicAddressBook addressBook4 = publicAddressBookDAO.get(paths[4]);
							publicAddressBook.setFloorType(addressBook4.getName());
						}
						if (StringUtils.isNotBlank(book.getId())) {
							//科室
							PublicAddressBook addressBook5 = publicAddressBookDAO.get(book.getId());
							publicAddressBook.setFloorDept(addressBook5.getName());
						}
					}
				}
				publicAddressBook.setNodeLevel(publicAddressBook.getPath().split(",").length);//层级
			}
			publicAddressBook.setId(null);
			publicAddressBook.setCreateDept(deptId);
			publicAddressBook.setCreateTime(new Date());
			publicAddressBook.setCreateUser(userId);
			publicAddressBookDAO.save(publicAddressBook);
			OperationUtils.getInstance().conserve(null,"公共通讯录","INSERT","T_OA_PUBLIC_BOOK",OperationUtils.LOGACTIONINSERT);
		}else{
			PublicAddressBook addressBook = publicAddressBookDAO.get(publicAddressBook.getId());
			if(!publicAddressBook.getParentCode().equals(addressBook.getParentCode())){//如果修改了父节点 需要重新保存信息
				List<PublicAddressBook> childById = publicAddressBookDAO.getChildById(publicAddressBook.getSuperPath()+publicAddressBook.getId()+",");
				PublicAddressBook superAddressBook = publicAddressBookDAO.get(publicAddressBook.getParentCode());
				String ids = publicAddressBook.getId();
				String path = count(4,publicAddressBook.getOrder().toString().length())+publicAddressBook.getOrder()+",";
				if ("root".equals(publicAddressBook.getParentCode())) {//父级为医院
					addressBook.setPath(path);
					addressBook.setSuperPath("root,");//所有父级
					addressBook.setNodeLevel(1);//层级
				}else{
					addressBook.setPath(superAddressBook.getPath()+path);
					addressBook.setSuperPath(superAddressBook.getSuperPath()+publicAddressBook.getParentCode()+",");//所有父级
					addressBook.setNodeLevel(addressBook.getPath().split(",").length);//层级
				}
				if ("55".equals(addressBook.getNodeType())) {
					String superPath = addressBook.getSuperPath();
					String[] paths = superPath.split(",");
					if (StringUtils.isNotBlank(paths[1])) {
						//院区
						PublicAddressBook addressBook1 = publicAddressBookDAO.get(paths[1]);
						addressBook.setAreaName(addressBook1.getName());
					}
					if (StringUtils.isNotBlank(paths[2])) {
						//楼号
						PublicAddressBook addressBook2 = publicAddressBookDAO.get(paths[2]);
						addressBook.setBuildingName(addressBook2.getName());
					}
					if (StringUtils.isNotBlank(paths[3])) {
						//楼层
						PublicAddressBook addressBook3 = publicAddressBookDAO.get(paths[3]);
						addressBook.setFloorName(addressBook3.getName());
					}
					if (StringUtils.isNotBlank(paths[4])) {
						//科室类型
						PublicAddressBook addressBook4 = publicAddressBookDAO.get(paths[4]);
						addressBook.setFloorType(addressBook4.getName());
					}
					if (StringUtils.isNotBlank(publicAddressBook.getParentCode())) {
						//科室
						PublicAddressBook addressBook5 = publicAddressBookDAO.get(publicAddressBook.getParentCode());
						addressBook.setFloorDept(addressBook5.getName());
					}
				}
				for (PublicAddressBook book : childById) {
					ids=ids+","+book.getId();
					String[] p = book.getPath().split(path);
					book.setPath((StringUtils.isBlank(addressBook.getPath())?"":addressBook.getPath())+(p.length>1?p[1]:""));
					String[] u = book.getSuperPath().split(addressBook.getParentCode()+",");
					book.setSuperPath((StringUtils.isBlank(superAddressBook.getSuperPath())?"":superAddressBook.getSuperPath())+(StringUtils.isBlank(publicAddressBook.getParentCode())?"":publicAddressBook.getParentCode()+",")+(u.length>1?u[1]:""));
					book.setNodeLevel(book.getPath().split(",").length);//层级
					book.setUpdateUser(userId);
					book.setUpdateTime(new Date());
					
					if ("55".equals(book.getNodeType())) {
						String superPath = book.getSuperPath();
						String[] paths = superPath.split(",");
						if (StringUtils.isNotBlank(paths[1])) {
							//院区
							PublicAddressBook addressBook1 = publicAddressBookDAO.get(paths[1]);
							book.setAreaName(addressBook1.getName());
						}
						if (StringUtils.isNotBlank(paths[2])) {
							//楼号
							PublicAddressBook addressBook2 = publicAddressBookDAO.get(paths[2]);
							book.setBuildingName(addressBook2.getName());
						}
						if (StringUtils.isNotBlank(paths[3])) {
							//楼层
							PublicAddressBook addressBook3 = publicAddressBookDAO.get(paths[3]);
							book.setFloorName(addressBook3.getName());
						}
						if (StringUtils.isNotBlank(paths[4])) {
							//科室类型
							PublicAddressBook addressBook4 = publicAddressBookDAO.get(paths[4]);
							book.setFloorType(addressBook4.getName());
						}
						if (StringUtils.isNotBlank(book.getId())) {
							//科室
							PublicAddressBook addressBook5 = publicAddressBookDAO.get("55".equals(publicAddressBook.getNodeType())?publicAddressBook.getParentCode():book.getParentCode());
							book.setFloorDept(addressBook5.getName());
						}
					}
				}
				publicAddressBookDAO.saveOrUpdateList(childById);
			}
			addressBook.setName(publicAddressBook.getName());
			addressBook.setNodeType(publicAddressBook.getNodeType());
			addressBook.setParentCode(publicAddressBook.getParentCode());
			addressBook.setStatus(publicAddressBook.getStatus());
			addressBook.setStop_flg(publicAddressBook.getStop_flg() == null ? 0 : publicAddressBook.getStop_flg());
			addressBook.setPhone(publicAddressBook.getPhone() == null ? "" : publicAddressBook.getPhone());
			addressBook.setMinPhone(publicAddressBook.getMinPhone() == null ? "" : publicAddressBook.getMinPhone());
			addressBook.setOfficePhone(publicAddressBook.getOfficePhone() == null ? "" : publicAddressBook.getOfficePhone());
			addressBook.setUpdateTime(new Date());
			addressBook.setUpdateUser(userId);
			publicAddressBookDAO.save(addressBook);
			OperationUtils.getInstance().conserve(null,"公共通讯录","UPDATE","T_OA_PUBLIC_BOOK",OperationUtils.LOGACTIONINSERT);
		}
	}
	/**  
	 *  
	 * @Description：  计算层级路径
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-31 上午10:31:33  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-31 上午10:31:33  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String count(Integer length,Integer dlen){
		String cV = "";
		Integer wS = length - dlen;
		if(wS>0){
			for (int i = 1; i <= wS; i++) {
				cV += "0";
			}
		}
		return cV;
	}
	/**  
	 * 
	 * 获得栏目的楼号
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoNoStringList() {
		return publicAddressBookDAO.getVoNoStringList();
	}
	/**  
	 * 
	 * 获得栏目的楼层
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 下午2:27:23 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 下午2:27:23  
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoFloorList() {
		return publicAddressBookDAO.getVoFloorList();
	}
	/**  
	 * 
	 * 获得栏目的全部工作站
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月18日 上午11:55:51 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月18日 上午11:55:51 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getPublicBookVoList(String page, String rows,String id,String nodeType,String areaCode,
			String noString, String floor, String typeName, String deptCode) {
		List<PublicAddressBook> list = publicAddressBookDAO.getPublicBookList(page,rows,id,nodeType, areaCode, noString, floor, typeName, deptCode);
		if (list!=null && list.size()>0) {
			return list;
		}
		return new ArrayList<PublicAddressBook>();
	}
	/**  
	 * 
	 * 科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoDeptList() {
		return publicAddressBookDAO.getVoDeptList();
	}
	/**  
	 * 
	 * 类别名称
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoTypeList() {
		return publicAddressBookDAO.getVoTypeList();
	}
	/**  
	 * 
	 * 删除
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午10:12:49 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午10:12:49 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void delMenu(String ids) {
		List<PublicAddressBook> childByIds = publicAddressBookDAO.getChildByIds(ids);
		if(childByIds!=null && childByIds.size()>0){
			for(PublicAddressBook book : childByIds){
				ids+=","+book.getId();
			}
		}
		publicAddressBookDAO.del(ids,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(ids,"公共通讯录","UPDATE","T_OA_PUBLIC_BOOK",OperationUtils.LOGACTIONDELETE);
	}
	/**  
	 * 
	 * 根据科室分类查所有科室
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 下午5:15:48 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 下午5:15:48 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<SysDepartment> getDept(String deptType) {
		return publicAddressBookDAO.getDept(deptType);
	}
	/**  
	 * 
	 * 院区
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月19日 上午9:09:07 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月19日 上午9:09:07 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public List<PublicAddressBook> getVoAreaList() {
		return publicAddressBookDAO.getVoAreaList();
	}
	/**  
	 * 
	 * 获得栏目的全部工作站数量
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月25日 下午6:24:14 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月25日 下午6:24:14 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public int getPublicBookTotal(String page, String rows, String id,
			String nodeType, String areaCode, String noString, String floor,
			String typeName, String deptCode) {
		return publicAddressBookDAO.getPublicBookTotal(page, rows, id, nodeType, areaCode, noString, floor, typeName, deptCode);
	}
	/**  
	 * 
	 * 删除工作站
	 * @Author: huzhenguo
	 * @CreateDate: 2017年7月26日 上午11:22:30 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年7月26日 上午11:22:30 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public void delWork(String ids) {
		publicAddressBookDAO.del(ids,ShiroSessionUtils.getCurrentUserFromShiroSession().getAccount());
		OperationUtils.getInstance().conserve(ids,"公共通讯录","UPDATE","T_OA_PUBLIC_BOOK",OperationUtils.LOGACTIONDELETE);
	}
}
