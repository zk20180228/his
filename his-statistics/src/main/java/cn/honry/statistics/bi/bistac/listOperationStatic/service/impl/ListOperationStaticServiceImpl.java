package cn.honry.statistics.bi.bistac.listOperationStatic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.bi.bistac.listOperationStatic.dao.ListOperationStaticDao;
import cn.honry.statistics.bi.bistac.listOperationStatic.service.ListOperationStaticService;
import cn.honry.statistics.bi.bistac.listOperationStatic.vo.ListOperationStaticVo;

@Service("listOperationStaticService")
@Transactional
@SuppressWarnings({"all"})
public class ListOperationStaticServiceImpl implements ListOperationStaticService{
	@Autowired
	@Qualifier(value = "listOperationStaticDao")
	private ListOperationStaticDao listOperationStaticDao;
	/**  
	 * 
	 * 手术情况统计
	 * @Author: huzhenguo
	 * @CreateDate: 2017年5月5日 上午10:30:21 
	 * @Modifier: huzhenguo
	 * @ModifyDate: 2017年5月5日 上午10:30:21 
	 * @ModifyRmk:  
	 * @version: V1.0
	 *
	 */
	@Override
	public ListOperationStaticVo queryVo(String startTime, String endTime) {
		return listOperationStaticDao.queryVo(startTime,endTime);
	}
	
}
