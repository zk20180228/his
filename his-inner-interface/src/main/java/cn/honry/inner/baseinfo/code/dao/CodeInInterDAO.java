package cn.honry.inner.baseinfo.code.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DutiesContrast;
import cn.honry.base.bean.model.TitleContrast;
import cn.honry.base.bean.model.TypeContrast;
import cn.honry.base.dao.EntityDao;
import cn.honry.inner.vo.ComboGroupVo;
/**
 * 
 * @Description  公共编码资料DAO层
 * @author  lyy
 * @createDate： 2016年7月5日 下午3:18:00 
 * @modifier lyy
 * @modifyDate：2016年7月5日 下午3:18:00
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public interface CodeInInterDAO extends EntityDao<BusinessDictionary> {
	/**
	 * 
	 * @Description 查询公共编码总条数
	 * @author  lyy
	 * @createDate： 2016年7月5日 下午3:17:53 
	 * @modifier lyy
	 * @modifyDate：2016年7月5日 下午3:17:53
	 * @param：   entity 实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	int getTotal(BusinessDictionary entity);
	/**
	 * 
	 * @Description 查询公共编码资料
	 * @author  lyy
	 * @createDate： 2016年7月5日 下午3:18:13 
	 * @modifier lyy
	 * @modifyDate：2016年7月5日 下午3:18:13
	 * @param：  page 分页栏页数   rows  总条数  entity 实体
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessDictionary> getPage(String page, String rows, BusinessDictionary entity);
	/**
	 * 
	 * @Description  根据类型 查询公共编码资料
	 * @author  lyy
	 * @createDate： 2016年7月5日 下午3:19:21 
	 * @modifier lyy
	 * @modifyDate：2016年7月5日 下午3:19:21
	 * @param：   type 类型
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessDictionary> getDictionary(String type);
	/**
	 * 
	 * @Description 根据类型或者code 查询公共编码资料
	 * @author  lyy
	 * @createDate： 2016年7月5日 下午3:25:51 
	 * @modifier lyy
	 * @modifyDate：2016年7月5日 下午3:25:51
	 * @param：  type 类型      code 代码
	 * @modifyRmk：  
	 * @version 1.0
	 */
    BusinessDictionary getDictionaryByCode(String type,String code);
    
	/**
	 * 下拉框查询
	 * @param code 参数  查询条件的参数
	 * @return List<CodeCenterfeecode>
	 */
	List<BusinessDictionary> likeSearch(String code);
	
	/**
	 * 
	 * @Description 根据类型和名称 查询公共编码资料
	 * @author  aizhonghua
	 * @createDate： 2016年7月5日 下午3:25:51 
	 * @modifier aizhonghua
	 * @modifyDate：2016年7月5日 下午3:25:51
	 * @param：  type 类型     name 名称
	 * @modifyRmk：  
	 * @version 1.0
	 */
	BusinessDictionary getDictionaryByName(String type, String name);
	
	/**
	 * 下拉框查询
	 * @param code 参数  查询条件的参数
	 * @param   type 类型
	 * @return List<CodeCenterfeecode>
	 */
	List<BusinessDictionary> likeTypeSearch(String type,String code,String page,String  rows);
	
	/**
	 * 统计总记录数
	 * @param   type 类型
	 * @param queryName拼音码、自定义码、职称编码、五笔码、职称名称
	 * @return
	 */
	int getTypeTotal(String type,String code);
	/**
	 * 
	 * @Description 
	 * @author  lyy
	 * @createDate： 2016年8月16日 下午5:46:05 
	 * @modifier lyy
	 * @modifyDate：2016年8月16日 下午5:46:05
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessDictionary> searchCode(String type, String code);
	/**
	 * 
	 * @Description 
	 * @author hanzurong
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<BusinessDictionary> querybkackList(String type);
	/**  
	 * 
	 * <p> 获取全部（非）药品单位分组 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月18日 下午1:39:08 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月18日 下午1:39:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: List<ComboGroupVo>
	 *
	 */
	List<ComboGroupVo> getUnitAllGroup();
	
	Map<String, String> getBusDictionaryMap(String type);
	
	Map<String, BusinessDictionary> getBusDicMap(String type);
	/**
	 * @author conglin
	 * 疾病分类去重
	 */
	List<BusinessDictionary> getDictionaryICD(String type);
	/**  
	 * 
	 * <p>查询职称，连带职称对照信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月30日 上午9:21:50 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月30日 上午9:21:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<TitleContrast> getTitleContrasts();
	
	/**  
	 * 
	 * <p>查询职称，连带职称对照信息</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年7月30日 上午9:21:50 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年7月30日 上午9:21:50 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	List<DutiesContrast> getDutiesContrasts();
	/**  
	 * 
	 * <p>查询人员类型信息</p>
	 * @Author: zpty
	 * @CreateDate: 2017年8月14日 上午9:21:50 
	 * @version: V1.0
	 *
	 */
	List<TypeContrast> getTypeContrasts();
}
