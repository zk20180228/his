package cn.honry.inner.baseinfo.code.service;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DutiesContrast;
import cn.honry.base.bean.model.TitleContrast;
import cn.honry.base.bean.model.TypeContrast;
import cn.honry.base.service.BaseService;
import cn.honry.inner.vo.ComboGroupVo;
import cn.honry.utils.TreeJson;
/**
 * 
 * @Description 公共编码资料service层
 * @author  lyy
 * @createDate： 2016年7月5日 下午3:34:09 
 * @modifier lyy
 * @modifyDate：2016年7月5日 下午3:34:09
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
public interface CodeInInterService extends BaseService<BusinessDictionary> {
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
     *
     * @Description 根据类型去查公共编码资料Map方法
     * @author  lyy
     * @createDate： 2016年7月11日 下午2:19:12 
     * @modifier lyy
     * @modifyDate：2016年7月11日 下午2:19:12
     * @param：  type 类型，例如：民族，系统类别等等
     * @modifyRmk：  
     * @version 1.0
     */
    Map<String, String> getBusDictionaryMap(String type);
    
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
	 * 
	 * @Description 根据type去查询编码树
	 * @author  lyy
	 * @createDate： 2016年7月28日 下午4:23:56 
	 * @modifier lyy
	 * @modifyDate：2016年7月28日 下午4:23:56
	 * @param：  
	 * @modifyRmk：  
	 * @version 1.0
	 */
	List<TreeJson> QueryTreeDictionary(String type,String treeName);
	
	/**
	 * 统计总记录数
	 * @param   type 类型
	 * @param queryName拼音码、自定义码、职称编码、五笔码、职称名称
	 * @return
	 */
	int getTypeTotal(String type,String code);
	/**
	 * 查询列表,带分页,带条件
	 * @param code 参数  查询条件的参数
	 * @param   type 类型
	 * @param   page, rows
	 * @return List<CodeCenterfeecode>
	 */
	List<BusinessDictionary> likeTypeSearch(String type,String code,String page,String  rows);
	/**
	 * 
	 * @Description 
	 * @author  lyy
	 * @createDate： 2016年8月16日 下午5:18:34 
	 * @modifier lyy
	 * @modifyDate：2016年8月16日 下午5:18:34
	 * @param：   type 类型    code 条件
	 * @modifyRmk：  
	 * @return List<CodeCenterfeecode>
	 * @version 1.0
	 */
	List<BusinessDictionary> searchCode(String type, String code);
	/**
	 * @param code
	 * @param   type 类型
	 * @param   page, rows
	 * @return List<CodeCenterfeecode>
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
	/**
	 * @author conglin
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
	
	/**  
	 * 
	 * <p>通过字段名以及数据组成的二维数组生成excel文件字节流</p>
	 * @Author: dutianliang
	 * @CreateDate: 2017年10月20日 下午4:31:32 
	 * @Modifier: dutianliang
	 * @ModifyDate: 2017年10月20日 下午4:31:32 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param values 字段名以及数据组成的二维数组
	 *
	 */
	byte[] exportExcel(String[][] values);
}