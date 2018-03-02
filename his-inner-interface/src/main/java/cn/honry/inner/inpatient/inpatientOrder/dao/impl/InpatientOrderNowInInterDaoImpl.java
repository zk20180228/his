package cn.honry.inner.inpatient.inpatientOrder.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BusinessFrequency;
import cn.honry.base.bean.model.BusinessHospitalbed;
import cn.honry.base.bean.model.DrugBillclass;
import cn.honry.base.bean.model.DrugBilllist;
import cn.honry.base.bean.model.DrugPreoutstore;
import cn.honry.base.bean.model.DrugSplit;
import cn.honry.base.bean.model.InpatientInfo;
import cn.honry.base.bean.model.InpatientKind;
import cn.honry.base.bean.model.InpatientOrder;
import cn.honry.base.bean.model.InpatientOrderNow;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.bean.model.User;
import cn.honry.base.bean.model.VidOrderBedname;
import cn.honry.base.bean.model.VidOrderBednameKs;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderInInterDao;
import cn.honry.inner.inpatient.inpatientOrder.dao.InpatientOrderNowInInterDao;
import cn.honry.inner.inpatient.inpatientOrder.vo.InpatientOrderInInterVO;
import cn.honry.inner.inpatient.inpatientOrder.vo.OrderInInterVO;


@Repository("inpatientOrderNowInInterDao")
@SuppressWarnings({"all"})
public class InpatientOrderNowInInterDaoImpl extends HibernateEntityDao<InpatientOrderNow> implements
		InpatientOrderNowInInterDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
