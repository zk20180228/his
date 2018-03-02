package cn.honry.inner.baseinfo.stackInfo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessStackinfo;
import cn.honry.base.bean.model.User;
import cn.honry.inner.baseinfo.stackInfo.dao.StackinfoInInterDAO;
import cn.honry.inner.baseinfo.stackInfo.service.StackinfoInInterService;
import cn.honry.utils.SessionUtils;

@Service("stackinfoInInterService")
@Transactional
@SuppressWarnings({ "all" })
public class StackinfoInInterServiceImpl implements StackinfoInInterService{
	@Autowired
	@Qualifier(value = "stackinfoInInterDAO")
	private StackinfoInInterDAO stackinfoInInterDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public BusinessStackinfo get(String id) {
		return null;
	}


	@Override
	public void saveOrUpdate(BusinessStackinfo entity) {
		
	}
	@Override
	public List<BusinessStackinfo> searchStackinfoList() {
		String hql="from BusinessStackinfo as bs";
		List<BusinessStackinfo> stackinfoList=stackinfoInInterDAO.findByObjectProperty(hql, null);
		if(stackinfoList!=null && stackinfoList.size()>0){
			return stackinfoList;
		}
		return null;
	}

	@Override
	public List<BusinessStackinfo> findStackInfoByFk(String fkStackId) {
		List<BusinessStackinfo> stackInfoList=stackinfoInInterDAO.findStackInfoByFk(fkStackId);
		return stackInfoList;
	}

	@Override
	public void delete(String id) {
		User user2 = (User) SessionUtils.getCurrentUserFromShiroSession();
		stackinfoInInterDAO.del(id,user2.getAccount());
	}

	/**  
	 *  
	 * @Description：  根据组套id获得组套详情
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-7 下午02:54:49  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-7 下午02:54:49  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<BusinessStackinfo> getStackInfo(String stackId) {
		return stackinfoInInterDAO.findStackInfoByFk(stackId);
	}
	@Override
	public void deleteBusinessStackInfo(String stackId) {
		stackinfoInInterDAO.deleteBusinessStackInfo(stackId);
	}
}