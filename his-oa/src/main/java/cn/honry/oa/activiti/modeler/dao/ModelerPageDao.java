package cn.honry.oa.activiti.modeler.dao;

import java.util.List;

import cn.honry.oa.activiti.modeler.vo.ModelerVO;

public interface ModelerPageDao {
	List<ModelerVO> getModeler(String name,Integer page,Integer rows );
	int getModelerTotal(String name);
}
