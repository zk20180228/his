package cn.honry.oa.activiti.modeler.service;

import java.util.List;

import cn.honry.oa.activiti.modeler.vo.ModelerVO;

public interface ModelerPageService {
	List<ModelerVO> getModeler(String name,Integer page,Integer rows );
	int getModelerTotal(String name);
}
