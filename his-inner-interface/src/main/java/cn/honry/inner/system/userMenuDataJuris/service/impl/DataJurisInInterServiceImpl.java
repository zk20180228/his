package cn.honry.inner.system.userMenuDataJuris.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.SysUserMenuDatajuris;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.system.user.dao.UserInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.dao.DataJurisInInterDAO;
import cn.honry.inner.system.userMenuDataJuris.service.DataJurisInInterService;
import cn.honry.inner.vo.AreaVo;
import cn.honry.inner.vo.DeptListVO;
import cn.honry.inner.vo.DeptVO;
import cn.honry.utils.HisParameters;
import cn.honry.utils.ShiroSessionUtils;

/**  
 *  
 * @className：MenuHeaderServiceImpl
 * @Description：  栏目列表字段维护
 * @Author：aizhonghua
 * @CreateDate：2017-6-17 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-6-17 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@Service("dataJurisInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class DataJurisInInterServiceImpl implements DataJurisInInterService{

	@Autowired
	@Qualifier(value = "dataJurisInInterDAO")
	private DataJurisInInterDAO dataJurisInInterDAO;
	@Autowired
	@Qualifier(value = "userInInterDAO")
	private UserInInterDAO userInInterDAO;
	@Autowired
	@Qualifier(value = "innerCodeDao")
	private CodeInInterDAO innerCodeDao;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public SysUserMenuDatajuris get(String id) {
		return dataJurisInInterDAO.get(id);
	}

	@Override
	public void saveOrUpdate(SysUserMenuDatajuris dataJuris) {
		dataJurisInInterDAO.save(dataJuris);
	}
	
	/**  
	 *  
	 * 根据栏目别名及科室类别获得用户栏目权限（科室）
	 * @Author：marongbin
	 * @CreateDate：
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<DeptListVO> queryDataJurisByMenuAndType(String menuAlias,String deptTypes) {
		//1.获得返回数据对象
		List<DeptListVO> vo = new ArrayList<DeptListVO>();
		//2.获得展示科室类别
		List<String> dTypeList = StringUtils.isNotBlank(deptTypes)?Arrays.asList(deptTypes.split(",")):null;
		//3.获得用户对象
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		//4.获得该用户院区数据权限
		List<String> hlAreaList = dataJurisInInterDAO.getHlAreaList(user.getAccount(),menuAlias);
		//5.获得该用户科室数据权限
		List<DeptVO> deptList = dataJurisInInterDAO.getJurisDept(user.getAccount(),menuAlias,hlAreaList);
		Map<String, DeptVO> deptMap = new HashMap<String, DeptVO>();
		if(deptList!=null){
			for(DeptVO deptVo : deptList){
				deptMap.put(deptVo.getId(), deptVo);
			}
		}
		//6.获得所属科室
		SysDepartment dept = ShiroSessionUtils.getCurrentUserDepartmentFromShiroSession();
		//7.获得关联科室
		List<SysDepartment> rDeptList = userInInterDAO.getRelatedDeptAccount(user.getAccount());
		//8.判断该用户是否拥有院区权限，有则封装对象
//		if(hlAreaList!=null){
//			Map<String,BusinessDictionary> areaMap = innerCodeDao.getBusDicMap(HisParameters.HOSPITALAREA);
//			DeptListVO pAreaVo = new DeptListVO();
//			pAreaVo.setParentMenu("院区");
//			if(hlAreaList.size()==1&&"ALL".equals(hlAreaList.get(0))){
//				List<BusinessDictionary> bdList = innerCodeDao.getDictionary(HisParameters.HOSPITALAREA);
//				if(bdList!=null){
//					List<DeptVO> areaVoList = new ArrayList<DeptVO>();
//					DeptVO areaVo = null;
//					for(BusinessDictionary bd : bdList){
//						areaVo = new DeptVO();
//						areaVo.setId(bd.getEncode());
//						areaVo.setName(bd.getName());
//						areaVo.setInputCode(bd.getInputCode());
//						areaVo.setPinyin(bd.getPinyin());
//						areaVo.setWb(bd.getWb());
//						areaVoList.add(areaVo);
//					}
//					pAreaVo.setMenus(areaVoList);
//					vo.add(pAreaVo);
//				}
//			}else{
//				DeptVO areaVo = null;
//				List<DeptVO> areaVoList = new ArrayList<DeptVO>();
//				for(String area : hlAreaList){
//					areaVo = new DeptVO();
//					areaVo.setId(areaMap.get(area).getEncode());
//					areaVo.setName(areaMap.get(area).getName());
//					areaVo.setInputCode(areaMap.get(area).getInputCode());
//					areaVo.setPinyin(areaMap.get(area).getPinyin());
//					areaVo.setWb(areaMap.get(area).getWb());
//					areaVoList.add(areaVo);
//				}
//				pAreaVo.setMenus(areaVoList);
//				vo.add(pAreaVo);
//			}
//		}
		//9.判断该用户科室数据权限中是否拥有常用科室，有则封装对象
		if(rDeptList!=null){
			DeptListVO deptVo = new DeptListVO();
			deptVo.setParentMenu("常用科室");
			LinkedHashMap<String,DeptVO> rDeptMap = new LinkedHashMap<String, DeptVO>();
			DeptVO rDeptVo = null;
			for(SysDepartment rDept : rDeptList){
				if(deptMap.get(rDept.getId())!=null&&rDeptMap.get(rDept.getId())==null){
					rDeptVo = new DeptVO();
					rDeptVo.setId(rDept.getId());
					rDeptVo.setName(rDept.getDeptName());
					rDeptVo.setType(rDept.getDeptType());
					rDeptVo.setCode(rDept.getDeptCode());
					rDeptVo.setInputCode(rDept.getDeptInputcode());
					rDeptVo.setPinyin(rDept.getDeptPinyin());
					rDeptVo.setWb(rDept.getDeptWb());
					rDeptMap.put(rDept.getId(), rDeptVo);
				}
			}
			if(rDeptMap.size()>0){
				List<DeptVO> deptVoList = new ArrayList<DeptVO>();
				DeptVO rdeptVo = null;
				for(Map.Entry<String,DeptVO> map : rDeptMap.entrySet()){
					if(deptTypes==null||(deptTypes!=null&&dTypeList!=null&&dTypeList.contains(map.getValue().getType()))){
						rdeptVo = new DeptVO();
						rdeptVo.setId(map.getValue().getId());
						rdeptVo.setName(map.getValue().getName());
						rdeptVo.setType(map.getValue().getType());
						rdeptVo.setCode(map.getValue().getCode());
						rdeptVo.setInputCode(map.getValue().getInputCode());
						rdeptVo.setPinyin(map.getValue().getPinyin());
						rdeptVo.setWb(map.getValue().getWb());
						deptVoList.add(rdeptVo);
					}
				}
				deptVo.setMenus(deptVoList);
				vo.add(deptVo);
			}
		}
		//10.判断该用户是否拥有科室权限，有则按科室类别进行封装对象
		if(deptList!=null&&deptList.size()>0){
			LinkedHashMap<String,List<DeptVO>> deptVoMap = new LinkedHashMap<String, List<DeptVO>>();
			for(DeptVO deptVO : deptList){
				if(deptVoMap.get(deptVO.getType())==null){
					List<DeptVO> deptVoList = new ArrayList<DeptVO>();
					deptVoList.add(deptVO);
					deptVoMap.put(deptVO.getType(), deptVoList);
				}else{
					deptVoMap.get(deptVO.getType()).add(deptVO);
				}
			}
			String[] deptArr = HisParameters.getDeptTypeOrder();
			Map<String, String> deptTypeMap = HisParameters.getDeptTypeMap();
			DeptListVO pDeptVo = null;
			List<DeptVO> deptVoList = new ArrayList<DeptVO>();
			for(String type : deptArr){
				if(deptTypes==null||(deptTypes!=null&&dTypeList.contains(type))){
					if(deptVoMap.get(type)!=null){
						pDeptVo = new DeptListVO();
						pDeptVo.setParentMenu(deptTypeMap.get(type));
						deptVoList = new ArrayList<DeptVO>();
						for(DeptVO deptVO : deptVoMap.get(type)){
							deptVoList.add(deptVO);
						}
						pDeptVo.setMenus(deptVoList);
						vo.add(pDeptVo);
					}
				}
			}
		}
		return vo.size()>0?vo:null;
	}
	
	/**  
	 *  
	 * 根据栏目别名得用户栏目权限（院区）
	 * @Author：aizhonghua
	 * @CreateDate：2017-7-12 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-7-12 下午18:59:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<AreaVo> queryDataJurisAreaByMenu(String menuAlias) {
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		return dataJurisInInterDAO.getDataJurisAreaList(user.getAccount(),menuAlias);
	}

	/**  
	 *  
	 * 根据栏目别名及科室类别获得用户栏目权限<br>
	 * 无权限返回null<br>
	 * (Integer)map.get("type")1院区级2科室级<br>
	 * (List<String>)map.get("code")院区或科室List编码
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	@Override
	public Map<String, Object> queryDataJurisByMenu(String menuAlias,String deptTypes) {
		return dataJurisInInterDAO.queryDataJurisByMenu(menuAlias,deptTypes);
	}

	/**  
	 *  
	 * 根据栏目别名及用户账户获得全部科室权限<br>
	 * @Author：aizhonghua
	 * @CreateDate：2017-6-28 下午18:59:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2017-6-28 下午18:59:31  
	 * @ModifyRmk：  
	 * @param:menuAlias栏目别名
	 * @param:deptTypes科室类别
	 * @version 1.0
	 *
	 */
	@Override
	public List<SysDepartment> getJurisDeptList(String menuAlias, String userAcc) {
		return dataJurisInInterDAO.getJurisDeptList(menuAlias,userAcc);
	}
	
}
