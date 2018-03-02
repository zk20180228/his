package cn.honry.statistics.icd.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.honry.base.bean.model.User;
import cn.honry.statistics.icd.dao.IcdAssortDao;
import cn.honry.statistics.icd.service.IcdAssortService;
import cn.honry.statistics.icd.vo.IcdAssortTree;
import cn.honry.statistics.icd.vo.IcdAssortVo;
import cn.honry.utils.DateUtils;
import cn.honry.utils.ShiroSessionUtils;
@Service("icdAssortService")
public class IcdAssortServiceImpl implements IcdAssortService {

	@Resource
	private  IcdAssortDao icdAssortDao;
	
	@Override
	public List<IcdAssortTree> icdTree(String parent_Id) {
		
		return icdAssortDao.icdTree(parent_Id);
	}

	@Override
	public void addIcdAssort(String assort_Name, String parent_Id) {
		
		//数据模型填充
		IcdAssortVo icdAssortVo = new IcdAssortVo();
		String id = UUID.randomUUID().toString().replace("-", "");
		icdAssortVo.setId(id);
		icdAssortVo.setAssort_Name(assort_Name);
		icdAssortVo.setParent_Id(parent_Id);//当为root时，代表一级节点
		User user = ShiroSessionUtils.getCurrentUserFromShiroSession();
		icdAssortVo.setCreateUser(user.getAccount());
		icdAssortVo.setUpdateUser(user.getAccount());
		icdAssortVo.setCreateTime(DateUtils.formatDateY_M_D_H_M_S(new Date()));
		icdAssortVo.setUpdateTime(DateUtils.formatDateY_M_D_H_M_S(new Date()));
		
		icdAssortDao.addIcdAssort(icdAssortVo);
		//更新父节点的是否是父节点为1
		icdAssortDao.updateParentNode(parent_Id);
	
	}

	@Override
	public List<IcdAssortVo> findIcdList(String page, String rows,String icdCode) {
		
		return icdAssortDao.findIcdList(page, rows, icdCode);
	}

	@Override
	public Integer findIcdCount(String page, String rows, String icdCode) {
		
		return icdAssortDao.findIcdCount(page, rows, icdCode);
	}

	@Override
	public void updateIcdSorrt(String icdId, String assortId) {
		
		icdAssortDao.updateIcdSorrt(icdId, assortId);
	}
	@Override
	public List<String> queryIcdCodesByIcdAssortId(String icdAssortId) {
		List<String> icdAssortIdList = findIcdAssortIds(icdAssortId);
		icdAssortIdList.add(icdAssortId);
		return findIcdCodesByAssortId(icdAssortIdList);
	}

	@Override
	public List<IcdAssortTree> icdTree(String id, String assortName) {
		//1.根据父分类id查询子分类列表
		List<IcdAssortTree> list = new ArrayList<>(icdAssortDao.findSons(id, assortName));
		//处理数据
		for (IcdAssortTree t : list) {
			t.setState("closed");
		}
		//2.根据父分类id查询当前父分类节点的下icd诊断码列表
		List<String> list2 = new ArrayList<String>();
		if (StringUtils.isNotBlank(id)) {
			list2.add(id);
			List<IcdAssortTree> list3 = findIcdAssortTreeByAssortId(list2);
			//处理数据
			if (list3 != null && list3.size() > 0) {
				for (IcdAssortTree i : list3) {
					i.setState("open");
					i.setText(i.getText() + "[" + i.getId() + "]");
				}
				list.addAll(list3);
			}
		} else if (StringUtils.isNotBlank(assortName)) {
			//当id是空值,assortName不为空时,说明是模糊查询
			//当是模糊搜索时按顺序返回数据
//            List<IcdAssortTree> resList = new ArrayList<IcdAssortTree>();
//            if (list != null && list.size() > 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    List<String> ids = new ArrayList<String>();
//                    ids.add(list.get(i).getId());
//                    List<IcdAssortTree> list4 = findIcdAssortTreeByAssortId(ids);
//                    //处理数据
//                    for (IcdAssortTree t : list4) {
//                        t.setState("open");
//                        t.setText(t.getText() + "[" + t.getId() + "]");
//                    }
//
//                    resList.add(list.get(i));
//                    //查询该栏目下的子栏目
//                    if (StringUtils.isNotBlank(list.get(i).getId())) {
//                        List<IcdAssortTree> sons = icdAssortDao.findSons(list.get(i).getId(), null);
//                        if (sons != null && sons.size() > 0) {
//                            //节点处理
//                            for (IcdAssortTree s : sons) {
//                                s.setState("closed");
//                            }
//                            //先添加子栏目
//                            resList.addAll(sons);
//                        }
//                    }
//
//                    resList.addAll(list4);
//                }
//            }
//            return resList;
		}
		return list;
	}

	/**
	 * <p>查询当前分类列表下的子分类列表</p>
	 *
	 * @param icdAssortId
	 * @return
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月15日 下午1:46:23
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月15日 下午1:46:23
	 * @ModifyRmk:
	 * @version: V1.0
	 * @throws:
	 */
	private List<String> findIcdAssortIds(String icdAssortId) {
		List<String> list = icdAssortDao.findIcdAssortIds(icdAssortId);
		List<String> resList = new ArrayList<>(list);
		for (String id : list) {
			resList.addAll(findIcdAssortIds(id));
		}
		return resList;
	}


	/**
	 * <p>根据icd分类id查询icd诊断码列表 </p>
	 *
	 * @param list icd的分类id的list集合
	 * @return
	 * @Author: zhangkui
	 * @CreateDate: 2017年12月15日 下午1:48:28
	 * @Modifier: zhangkui
	 * @ModifyDate: 2017年12月15日 下午1:48:28
	 * @ModifyRmk:
	 * @version: V1.0
	 * @throws:
	 */
	public List<IcdAssortTree> findIcdAssortTreeByAssortId(List<String> list) {

		return icdAssortDao.findIcdAssortTreeByAssortId(list);
	}

	public List<String> findIcdCodesByAssortId(List<String> list) {

		return icdAssortDao.findIcdCodesByAssortId(list);
	}

	@Override
	public String queryIcdAssortNameByIcdAssortId(String icdAssortId) {
		return icdAssortDao.queryIcdAssortNameByIcdAssortId(icdAssortId);
	}

	@Override
	public String queryIcdNameByIcdId(String icdId) {
		return icdAssortDao.queryIcdNameByIcdId(icdId);
	}

	@Override
	public String queryIcdCodeByIcdId(String icdId) {
		return icdAssortDao.queryIcdCodeByIcdId(icdId);
	}

	@Override
	public String queryIcdNameByIcdCode(String icdCode) {
		return icdAssortDao.queryIcdNameByIcdCode(icdCode);
	}

	@Override
	public List<String> queryAllIcdCodes() {
		return icdAssortDao.queryAllIcdCodes();
	}

	@Override
	public List<String> queryIcdCodeByLike(String where) {
		return icdAssortDao.queryIcdCodeByLike(where);
	}

}
