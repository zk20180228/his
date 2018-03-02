package cn.honry.oa.activiti.tenant.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.oa.activiti.tenant.service.TenantService;
import cn.honry.oa.activiti.tenant.vo.TenantVo;


@Service("tenantService")
@Transactional
@SuppressWarnings({ "all" })
public class TenantServiceImpl implements TenantService {

	private TenantVo tenantVo= new TenantVo();
	
	public TenantServiceImpl(){
		tenantVo.setId("1");
		tenantVo.setCode("default");
		tenantVo.setRef("1");
	}
	
	@Override
	public String getTenantId() {
		return tenantVo.getId();
	}

	@Override
	public String getTenantCode() {
		return tenantVo.getCode();
	}

	@Override
	public String getUserRepoRef() {
		return tenantVo.getRef();
	}

	@Override
	public TenantVo getTenantDto() {
		return tenantVo;
	}

}
