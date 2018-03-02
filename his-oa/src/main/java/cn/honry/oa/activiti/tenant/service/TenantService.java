package cn.honry.oa.activiti.tenant.service;

import cn.honry.oa.activiti.tenant.vo.TenantVo;

public interface TenantService {

	String getTenantId();

    String getTenantCode();

    String getUserRepoRef();

    TenantVo getTenantDto();
}
