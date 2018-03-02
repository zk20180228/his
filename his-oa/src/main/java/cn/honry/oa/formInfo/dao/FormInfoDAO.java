package cn.honry.oa.formInfo.dao;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import cn.honry.base.bean.model.OaFormInfo;
import cn.honry.base.dao.EntityDao;
import cn.honry.oa.formInfo.vo.KeyValVo;

/**  
 *  
 * @className：FormInfoDAO
 * @Description：  自定义表单维护
 * @Author：aizhonghua
 * @CreateDate：2017-7-17 下午18:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2017-7-17 下午18:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface FormInfoDAO extends EntityDao<OaFormInfo>{

	/**  
	 *  
	 * 获取列表-获取总条数
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int getFormInfoTotal(String search);

	/**  
	 *  
	 * 获取列表-获取展示信息
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<OaFormInfo> getFormInfoRows(String page, String rows, String search);

	/**  
	 *  
	 * 停用/启用
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int stopflgFormInfo(List<String> stopflgList, int flg);

	/**  
	 *  
	 * 删除
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int delFormInfo(List<String> delList);

	/**  
	 *  
	 * 查询code是否重复
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-18 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-18 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	int findFormCode(String id, String formCode);
	
	/**  
	 *  
	 * 创建表
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-18 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-18 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Integer createTable(String formName, ArrayList<KeyValVo> keyValueList);

	/**  
	 *  
	 * 接口-获取可用表单
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	List<KeyValVo> getValidFormInfo();

	/**  
	 *  
	 * 接口-获取表单主件<br>
	 * code表单编码
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-17 下午18:56:31  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-17 下午18:56:31  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	Clob getPartOfFormInfo(String code);

	/**
	 * <p>根据code查找from</p>
	 * @Author: yuke
	 * @CreateDate: 2017年9月26日 下午2:30:16 
	 * @Modifier: yuke
	 * @ModifyDate: 2017年9月26日 下午2:30:16 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: OaFormInfo
	 *
	 */
	OaFormInfo getFromByCode(String tableCode);

}
