package cn.honry.inner.drug.sendWicket.dao;

import java.util.List;
import java.util.Map;

import cn.honry.base.bean.model.StoTerminal;
import cn.honry.base.dao.EntityDao;

/**  
 *  
 * @Author：aizhonghua
 * @CreateDate：2016-2-24 上午11:56:31  
 * @Modifier：aizhonghua
 * @ModifyDate：2016-2-24 上午11:56:31  
 * @ModifyRmk：  
 * @version 1.0
 *
 */
@SuppressWarnings({"all"})
public interface SendWicketInInterDAO extends EntityDao<StoTerminal>{
		/** 获取所有的发药窗口（包括停用的）
		* @Title: getAllSendWicket 
		* @Description: 获取所有的发药窗口
		* @return List<StoTerminal>    返回类型 
		* @author dtl 
		* @date 2016年12月23日
		*/
		List<StoTerminal> getAllSendWicket();
		/**
		 * @Description 获取调剂方式，竞争方式/均分方式；配药/发药；
		 * @author  marongbin
		 * @createDate： 2017年3月15日 下午1:50:18 
		 * @modifier 
		 * @modifyDate：
		 * @param code 药房code
		 * @return: Map<String,String> TJFS=调剂方式（0平均1竞争）TJYJ=调剂依据（0配药1发药）；resCode = success(获取到了数据)；resCode = error(没有获取到数据)
		 * @modifyRmk：  
		 * @version 1.0
		 */
		Map<String,String> getBusinessExtend(String code);

}
