package cn.honry.statistics.bi.outpatient.optRecipedetail.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import oracle.net.aso.a;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.honry.base.bean.model.BiBaseOrganization;
import cn.honry.base.bean.model.BiRegister;
import cn.honry.base.bean.model.BiRegisterGrade;
import cn.honry.base.bean.model.SysDepartment;
import cn.honry.base.dao.impl.HibernateEntityDao;
import cn.honry.statistics.bi.outpatient.optRecipedetail.dao.OptRecipedetailDao;
import cn.honry.statistics.bi.outpatient.optRecipedetail.vo.OptRecipedetailVo;
import cn.honry.statistics.bi.outpatient.outpatientWorkload.vo.OutpatientWorkloadVo;
import cn.honry.statistics.util.dateVo.DateVo;
import cn.honry.utils.JSONUtils;

@Repository("optRecipedetailDao")
@SuppressWarnings({ "all" })
public class OptRecipedetailDaoImpl extends HibernateEntityDao<BiRegister>
		implements OptRecipedetailDao {

	@Resource(name = "sessionFactory")
	// 为父类HibernateDaoSupport注入sessionFactory的值
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public List<SysDepartment> queryAllDept() {
		return new ArrayList<SysDepartment>();
	}

	@Override
	public List<OptRecipedetailVo> querytOptRecipedetailDatagrid(
			String[] diArrayKey, List<Map<String, List<String>>> list,
			int dateType, DateVo datevo) {
		// 天数

		int num = 365;
		String sqldate = null;
		// 查询时间
		StringBuffer sql = new StringBuffer();// sql语句的StringBuffer对象
		StringBuffer order = new StringBuffer();// 最后order by
												// 的顺序（即：选择的维度顺序）StringBuffer对象

		if (datevo.getQuarter1() == 1) {

			datevo.setQuarter1(2);
			sql.append(" select ");
			// 遍历数组，去匹配所选择的维度拼接sql和order
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					sql.append(" t.REG_DEPT_CODE as deptDimensionality");
					order.append(diArrayKey[i]);
				}
				if ("doct_code".equals(diArrayKey[i])) {
					sql.append(" t.DOCT_CODE as doctorDimensionality");
					order.append(diArrayKey[i]);
				}
				if (i != diArrayKey.length - 2) {
					order.append(",");
				}
				sql.append(",");
			}
			sql.append("	    sum(t.qty) as qty, ");
			sql.append("	     sum(  t.UNIT_PRICE*t.qty) as sprice , ");
			if (dateType == 1) {

				// 同比修正版
				int n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code =b.reg_dept_code and t.DOCT_CODE =b.doct_code)) as an,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code=b.reg_dept_code)) as an,");
				}
				// 环比修正版
				n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code =b.reg_dept_code and t.DOCT_CODE =b.doct_code)) as mom,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code=b.reg_dept_code)) as mom,");
				}

				sql.append("         to_char(t.REG_DATE,'yyyy') as timeChose");
			} else if (dateType == 2 || dateType == 3) {
				if (dateType == 3) {
					int n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', '')) - 100 and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', '')) - 100 and t.reg_dept_code = b.reg_dept_code )) as an,");

					}

					n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(add_months(b.reg_date ,1), 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', ''))  and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom,");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(add_months(b.reg_date ,1), 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', ''))  and t.reg_dept_code = b.reg_dept_code )) as mom,");

					}
					sql.append("         to_char(t.REG_DATE,'yyyy/mm') as timeChose");
				}

				else {

					sql.append("  to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ,");

					int n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and to_number((replace(to_char(b.reg_date, 'q'), '/', '')))=to_number((replace(to_char(t.reg_date, 'q'), '/', ''))) and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', ''))-1  and to_number((replace(to_char(b.reg_date, 'q'), '/', '')))=to_number((replace(to_char(t.reg_date, 'q'), '/', '')))  and t.reg_dept_code = b.reg_dept_code )) as an,");

					}

					n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("   (sum(t.UNIT_PRICE * t.qty) / (select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) and to_number((replace(to_char(b.reg_date, 'mm'), '/', ''))) in(to_number(to_char(t.REG_DATE, 'q'))*3-3 , to_number(to_char(t.REG_DATE, 'q'))*3-4 , to_number(to_char(t.REG_DATE, 'q'))*3-5) and t.reg_dept_code  = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("   (sum(t.UNIT_PRICE * t.qty) / (select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) and to_number((replace(to_char(b.reg_date, 'mm'), '/', ''))) in (to_number(to_char(t.REG_DATE, 'q'))*3-3, to_number(to_char(t.REG_DATE, 'q'))*3-4, to_number(to_char(t.REG_DATE, 'q'))*3-5) and t.reg_dept_code  = b.reg_dept_code )) as mom");

					}
					// sql.append("  to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ");
				}
				// 同比

			} else if (dateType == 4) {

				int n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', '')) - 10000 and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', '')) - 10000 and t.reg_dept_code = b.reg_dept_code )) as an,");

				}

				n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date+1, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', ''))  and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date+1, 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', ''))  and t.reg_dept_code = b.reg_dept_code )) as mom,");

				}
				sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd') as timeChose");

			}
			sql.append("	  from v_opt_recipedetail  t ");
			sql.append("	 where 1=1  ");

			if (dateType == 1) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'");
			} else if (dateType == 2) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'   ");
				sql.append(" and to_char(t.REG_DATE,'q') between '"
						+ datevo.getQuarter1() + "' and '"
						+ datevo.getQuarter2() + "'   ");
			} else if (dateType == 3) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'   ");
				sql.append(" and to_char(t.REG_DATE,'mm') between '"
						+ (datevo.getMonth1() > 9 ? datevo.getMonth1() : "0"
								+ datevo.getMonth1())
						+ "' and '"
						+ (datevo.getMonth2() > 9 ? datevo.getMonth2() : "0"
								+ datevo.getMonth2()) + "'");
			} else if (dateType == 4) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'   ");
				sql.append(" and to_char(t.REG_DATE,'mm') between '"
						+ (datevo.getMonth1() > 9 ? datevo.getMonth1() : "0"
								+ datevo.getMonth1())
						+ "' and '"
						+ (datevo.getMonth2() > 9 ? datevo.getMonth2() : "0"
								+ datevo.getMonth2()) + "'");
				sql.append(" and to_char(t.REG_DATE,'dd') between '"
						+ (datevo.getDay1() > 9 ? datevo.getDay1() : "0"
								+ datevo.getDay1())
						+ "' and '"
						+ (datevo.getDay2() > 9 ? datevo.getDay2() : "0"
								+ datevo.getDay2()) + "'");
			}
			int n = 0;
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					StringBuilder value = new StringBuilder();
					for (int j = 0; j < list.get(n).get(diArrayKey[i]).size(); j++) {
						if (!"".equals(value.toString())) {
							value.append(",");
						}
						value.append("'"
								+ list.get(n).get(diArrayKey[i]).get(j) + "'");
					}
					sql.append(" and t.reg_dept_code  in (" + value.toString()
							+ ")");
				}
				if ("doct_code".equals(diArrayKey[i])) {
					StringBuilder value = new StringBuilder();
					for (int j = 0; j < list.get(n).get(diArrayKey[i]).size(); j++) {
						if (!"".equals(value.toString())) {
							value.append(",");
						}
						value.append("'"
								+ list.get(n).get(diArrayKey[i]).get(j) + "'");
					}
					sql.append(" and t.doct_code  in (" + value.toString()
							+ ")");
				}
				n++;
			}
			sql.append("	 group by  ");
			sql.append(order.toString());
			sql.append(",");
			// 对于时间的排序放在最后
			if (dateType == 1) {
				sql.append("         to_char(t.REG_DATE,'yyyy') ");
			} else if (dateType == 2) {
				sql.append("         to_char(t.REG_DATE,'yyyy')  ");
				sql.append("        ,to_char(t.REG_DATE,'q')   ");
			} else if (dateType == 3) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm') ");
			} else if (dateType == 4) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd')");
			}
			sql.append("	 order by ");
			sql.append(order.toString());
			sql.append(",");
			// 对于时间的排序放在最后
			if (dateType == 1) {
				sql.append("         to_char(t.REG_DATE,'yyyy') ");
			} else if (dateType == 2) {
				sql.append("         to_char(t.REG_DATE,'yyyy')  ");
				sql.append("        ,to_char(t.REG_DATE,'q')   ");
			} else if (dateType == 3) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm') ");
			} else if (dateType == 4) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd')");
			}
			SQLQuery queryObject1 = this.getSession().createSQLQuery(
					sql.toString());

			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					queryObject1.addScalar("deptDimensionality");
				}
				if ("doct_code".equals(diArrayKey[i])) {
					queryObject1.addScalar("doctorDimensionality");
				}
				if ("YNBOOK".equals(diArrayKey[i])) {
					queryObject1.addScalar("doctorlevelDimensionality");
				}

			}
			queryObject1.addScalar("qty", Hibernate.DOUBLE)
					.addScalar("sprice", Hibernate.DOUBLE)
					.addScalar("an", Hibernate.DOUBLE)
					.addScalar("mom", Hibernate.DOUBLE).addScalar("timeChose");

			List<OptRecipedetailVo> bdl = queryObject1.setResultTransformer(
					Transformers.aliasToBean(OptRecipedetailVo.class)).list();
			// ##########################################

			StringBuffer sql1 = new StringBuffer();
			datevo.setQuarter1(2);
			sql1.append(" select ");
			// 遍历数组，去匹配所选择的维度拼接sql1和order

			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					sql1.append(" t.REG_DEPT_CODE as deptDimensionality");

				}
				if ("doct_code".equals(diArrayKey[i])) {
					sql1.append(" t.DOCT_CODE as doctorDimensionality");

				}
				if (i != diArrayKey.length - 2) {
					order.append(",");
				}
				sql1.append(",");
			}
			sql1.append("	    sum(t.qty) as qty, ");
			sql1.append("	     sum(  t.UNIT_PRICE*t.qty) as sprice , ");

			sql1.append("  to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ,");

			n = 0;
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("doct_code".equals(diArrayKey[i])) {
					sql1.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and to_number((replace(to_char(b.reg_date, 'q'), '/', '')))=to_number((replace(to_char(t.reg_date, 'q'), '/', ''))) and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
					n++;
					break;
				}
			}
			if (n == 0) {
				sql1.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', ''))-1  and to_number((replace(to_char(b.reg_date, 'q'), '/', '')))=to_number((replace(to_char(t.reg_date, 'q'), '/', '')))  and t.reg_dept_code = b.reg_dept_code )) as an,");

			}

			n = 0;
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("doct_code".equals(diArrayKey[i])) {
					sql1.append("   (sum(t.UNIT_PRICE * t.qty) / (select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', ''))-1 and to_number((replace(to_char(b.reg_date, 'mm'), '/', ''))) in(10,11,12) and t.reg_dept_code  = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom");
					n++;
					break;
				}
			}
			if (n == 0) {
				sql1.append("   (sum(t.UNIT_PRICE * t.qty) / (select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', ''))-1 and to_number((replace(to_char(b.reg_date, 'mm'), '/', ''))) in (10,11,12) and t.reg_dept_code = b.reg_dept_code)) as mom");

			}
			// sql1.append("  to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ");

			// 同比

			sql1.append("	  from v_opt_recipedetail  t ");
			sql1.append("	 where 1=1  ");
			sql1.append(" and  to_char(t.REG_DATE,'yyyy') between '"
					+ datevo.getYear1() + "' and '" + datevo.getYear2()
					+ "'   ");
			sql1.append(" and to_char(t.REG_DATE,'q') ="
					+ (datevo.getQuarter1() - 1));

			n = 0;
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					StringBuilder value = new StringBuilder();
					for (int j = 0; j < list.get(n).get(diArrayKey[i]).size(); j++) {
						if (!"".equals(value.toString())) {
							value.append(",");
						}
						value.append("'"
								+ list.get(n).get(diArrayKey[i]).get(j) + "'");
					}
					sql1.append(" and t.reg_dept_code  in (" + value.toString()
							+ ")");
				}
				if ("doct_code".equals(diArrayKey[i])) {
					StringBuilder value = new StringBuilder();
					for (int j = 0; j < list.get(n).get(diArrayKey[i]).size(); j++) {
						if (!"".equals(value.toString())) {
							value.append(",");
						}
						value.append("'"
								+ list.get(n).get(diArrayKey[i]).get(j) + "'");
					}
					sql1.append(" and t.doct_code  in (" + value.toString()
							+ ")");
				}
				n++;
			}
			sql1.append("	 group by  ");
			sql1.append(order.toString());
			if (order.toString().length() <= 15) {
				sql1.append(",");
			}
			// 对于时间的排序放在最后

			sql1.append("         to_char(t.REG_DATE,'yyyy')  ");
			sql1.append("        ,to_char(t.REG_DATE,'q')   ");

			sql1.append("	 order by ");
			sql1.append(order.toString());
			if (order.toString().length() <= 15) {
				sql1.append(",");
			}
			// 对于时间的排序放在最后

			sql1.append("         to_char(t.REG_DATE,'yyyy')  ");
			sql1.append("        ,to_char(t.REG_DATE,'q')   ");

			SQLQuery queryObject2 = this.getSession().createSQLQuery(
					sql1.toString());

			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					queryObject2.addScalar("deptDimensionality");
				}
				if ("doct_code".equals(diArrayKey[i])) {
					queryObject2.addScalar("doctorDimensionality");
				}
				if ("YNBOOK".equals(diArrayKey[i])) {
					queryObject2.addScalar("doctorlevelDimensionality");
				}

			}
			queryObject2.addScalar("qty", Hibernate.DOUBLE)
					.addScalar("sprice", Hibernate.DOUBLE)
					.addScalar("an", Hibernate.DOUBLE)
					.addScalar("mom", Hibernate.DOUBLE).addScalar("timeChose");

			List<OptRecipedetailVo> bdl1 = queryObject2.setResultTransformer(
					Transformers.aliasToBean(OptRecipedetailVo.class)).list();

			for (int i = 0; i < bdl1.size(); i++) {
				bdl.add(bdl1.get(i));
			}

			if (bdl.get(0).getDoctorDimensionality() != null) {
				if (dateType == 1) {
					// 为以年为划分的维度增加缺少对象（有医生属性的）
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {

								if (choicelist(timelist, start)) {
									bdl.add(new OptRecipedetailVo(bdl.get(i)
											.getDeptDimensionality(), bdl
											.get(i).getDoctorDimensionality(),
											start + ""));
								}

								// todo 判断月日

							}

						}
					}

				} else if (dateType == 2) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = 1; j <= datevo.getQuarter2(); j++) {
									if (choicelistquarter(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												bdl.get(i)
														.getDoctorDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}
				}

				else if (dateType == 3) {
					// 为以月份划分的维度增加缺少对象（有医生属性的）

					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {
									if (choicelistmm(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												bdl.get(i)
														.getDoctorDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}

				} else if (dateType == 4) {
					// 为以日期划分的维度增加缺少对象（有医生属性的）
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {

									for (int j2 = datevo.getDay1(); j2 <= datevo
											.getDay2(); j2++) {
										if (choicelist1(timelist, j2, j, start)) {
											bdl.add(new OptRecipedetailVo(
													bdl.get(i)
															.getDeptDimensionality(),
													bdl.get(i)
															.getDoctorDimensionality(),
													start + "/" + j + "/" + j2));
										}

									}
								}

								// todo 判断月日

							}

						}
					}
				}
			} else {
				if (dateType == 1) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {

								if (choicelist(timelist, start)) {
									bdl.add(new OptRecipedetailVo(bdl.get(i)
											.getDeptDimensionality(), start
											+ ""));
								}

								// todo 判断月日

							}

						}
					}

				} else if (dateType == 2) {

					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = 1; j <= datevo.getQuarter2(); j++) {
									if (choicelistquarter(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}

				} else if (dateType == 3) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {
									if (choicelistmm(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}

				} else if (dateType == 4) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {

									for (int j2 = datevo.getDay1(); j2 <= datevo
											.getDay2(); j2++) {
										if (choicelist1(timelist, j2, j, start)) {
											bdl.add(new OptRecipedetailVo(bdl
													.get(i)
													.getDeptDimensionality(),
													start + "/" + j + "/" + j2));
										}

									}
								}

								// todo 判断月日

							}

						}
					}
				}
			}

			for (int i = 0; i < bdl.size(); i++) {
				if (bdl.get(i).getAn() == null) {
					bdl.get(i).setAn(0.00);
				}
				if (bdl.get(i).getMom() == null) {
					bdl.get(i).setMom(0.00);
				}
				if (bdl.get(i).getQty() == null) {
					bdl.get(i).setQty(0.00);
				}
				if (bdl.get(i).getSprice() == null) {
					bdl.get(i).setSprice(0.00);
				}
			}
			for (int i = 0; i < bdl.size(); i++) {

				bdl.get(i).setAn(
						(double) Math.round(bdl.get(i).getAn() * 100) / 100);
				bdl.get(i).setMom(
						(double) Math.round(bdl.get(i).getMom() * 100) / 100);

				bdl.get(i)
						.setSprice(
								(double) Math
										.round(bdl.get(i).getSprice() * 100) / 100);
			}

			if (bdl != null) {
				return bdl;
			}
			return new ArrayList<OptRecipedetailVo>();

		} else {

			// //////////////////////////////////////////////////////////////
			sql.append(" select ");
			// 遍历数组，去匹配所选择的维度拼接sql和order
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					sql.append(" t.REG_DEPT_CODE as deptDimensionality");
					order.append(diArrayKey[i]);
				}
				if ("doct_code".equals(diArrayKey[i])) {
					sql.append(" t.DOCT_CODE as doctorDimensionality");
					order.append(diArrayKey[i]);
				}
				if (i != diArrayKey.length - 2) {
					order.append(",");
				}
				sql.append(",");
			}
			sql.append("	    sum(t.qty) as qty, ");
			sql.append("	     sum(  t.UNIT_PRICE*t.qty) as sprice , ");
			if (dateType == 1) {

				// 同比修正版
				int n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code =b.reg_dept_code and t.DOCT_CODE =b.doct_code)) as an,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code=b.reg_dept_code)) as an,");
				}
				// 环比修正版
				n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code =b.reg_dept_code and t.DOCT_CODE =b.doct_code)) as mom,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and t.reg_dept_code=b.reg_dept_code)) as mom,");
				}

				sql.append("         to_char(t.REG_DATE,'yyyy') as timeChose");
			} else if (dateType == 2 || dateType == 3) {
				if (dateType == 3) {
					int n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', '')) - 100 and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', '')) - 100 and t.reg_dept_code = b.reg_dept_code )) as an,");

					}

					n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(add_months(b.reg_date ,1), 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', ''))  and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom,");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(add_months(b.reg_date ,1), 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm')), '/', ''))  and t.reg_dept_code = b.reg_dept_code )) as mom,");

					}
					sql.append("         to_char(t.REG_DATE,'yyyy/mm') as timeChose");
				}

				else {

					sql.append("  to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ,");

					int n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) - 1 and to_number((replace(to_char(b.reg_date, 'q'), '/', '')))=to_number((replace(to_char(t.reg_date, 'q'), '/', ''))) and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', ''))-1  and to_number((replace(to_char(b.reg_date, 'q'), '/', '')))=to_number((replace(to_char(t.reg_date, 'q'), '/', '')))  and t.reg_dept_code = b.reg_dept_code )) as an,");

					}

					n = 0;
					for (int i = 0; i < diArrayKey.length; i += 2) {
						if ("doct_code".equals(diArrayKey[i])) {
							sql.append("   (sum(t.UNIT_PRICE * t.qty) / (select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) and to_number((replace(to_char(b.reg_date, 'mm'), '/', ''))) in(to_number(to_char(t.REG_DATE, 'q'))*3-3 , to_number(to_char(t.REG_DATE, 'q'))*3-4 , to_number(to_char(t.REG_DATE, 'q'))*3-5) and t.reg_dept_code  = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom");
							n++;
							break;
						}
					}
					if (n == 0) {
						sql.append("   (sum(t.UNIT_PRICE * t.qty) / (select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy'), '/', ''))) =to_number(replace((to_char(t.REG_DATE, 'yyyy')), '/', '')) and to_number((replace(to_char(b.reg_date, 'mm'), '/', ''))) in (to_number(to_char(t.REG_DATE, 'q'))*3-3, to_number(to_char(t.REG_DATE, 'q'))*3-4, to_number(to_char(t.REG_DATE, 'q'))*3-5) and t.reg_dept_code  = b.reg_dept_code )) as mom");

					}
					// sql.append("  to_char(to_char(t.REG_DATE,'yyyy')||'/'||to_char(t.REG_DATE,'q')) as timeChose ");
				}
				// 同比

			} else if (dateType == 4) {

				int n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', '')) - 10000 and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as an,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', '')) - 10000 and t.reg_dept_code = b.reg_dept_code )) as an,");

				}

				n = 0;
				for (int i = 0; i < diArrayKey.length; i += 2) {
					if ("doct_code".equals(diArrayKey[i])) {
						sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date+1, 'yyyy/mm/dd'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', ''))  and t.reg_dept_code = b.reg_dept_code and t.DOCT_CODE = b.doct_code)) as mom,");
						n++;
						break;
					}
				}
				if (n == 0) {
					sql.append("(sum(t.UNIT_PRICE * t.qty) /(select sum(b.UNIT_PRICE * b.qty) from v_opt_recipedetail b where to_number((replace(to_char(b.reg_date+1, 'yyyy/mm'), '/', ''))) = to_number(replace((to_char(t.REG_DATE, 'yyyy/mm/dd')), '/', ''))  and t.reg_dept_code = b.reg_dept_code )) as mom,");

				}
				sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd') as timeChose");

			}
			sql.append("	  from v_opt_recipedetail  t ");
			sql.append("	 where 1=1  ");

			if (dateType == 1) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'");
			} else if (dateType == 2) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'   ");
				sql.append(" and to_char(t.REG_DATE,'q') between '"
						+ datevo.getQuarter1() + "' and '"
						+ datevo.getQuarter2() + "'   ");
			} else if (dateType == 3) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'   ");
				sql.append(" and to_char(t.REG_DATE,'mm') between '"
						+ (datevo.getMonth1() > 9 ? datevo.getMonth1() : "0"
								+ datevo.getMonth1())
						+ "' and '"
						+ (datevo.getMonth2() > 9 ? datevo.getMonth2() : "0"
								+ datevo.getMonth2()) + "'");
			} else if (dateType == 4) {
				sql.append(" and to_char(t.REG_DATE,'yyyy') between '"
						+ datevo.getYear1() + "' and '" + datevo.getYear2()
						+ "'   ");
				sql.append(" and to_char(t.REG_DATE,'mm') between '"
						+ (datevo.getMonth1() > 9 ? datevo.getMonth1() : "0"
								+ datevo.getMonth1())
						+ "' and '"
						+ (datevo.getMonth2() > 9 ? datevo.getMonth2() : "0"
								+ datevo.getMonth2()) + "'");
				sql.append(" and to_char(t.REG_DATE,'dd') between '"
						+ (datevo.getDay1() > 9 ? datevo.getDay1() : "0"
								+ datevo.getDay1())
						+ "' and '"
						+ (datevo.getDay2() > 9 ? datevo.getDay2() : "0"
								+ datevo.getDay2()) + "'");
			}
			int n = 0;
			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					StringBuilder value = new StringBuilder();
					for (int j = 0; j < list.get(n).get(diArrayKey[i]).size(); j++) {
						if (!"".equals(value.toString())) {
							value.append(",");
						}
						value.append("'"
								+ list.get(n).get(diArrayKey[i]).get(j) + "'");
					}
					sql.append(" and t.reg_dept_code  in (" + value.toString()
							+ ")");
				}
				if ("doct_code".equals(diArrayKey[i])) {
					StringBuilder value = new StringBuilder();
					for (int j = 0; j < list.get(n).get(diArrayKey[i]).size(); j++) {
						if (!"".equals(value.toString())) {
							value.append(",");
						}
						value.append("'"
								+ list.get(n).get(diArrayKey[i]).get(j) + "'");
					}
					sql.append(" and t.doct_code  in (" + value.toString()
							+ ")");
				}
				n++;
			}
			sql.append("	 group by  ");
			sql.append(order.toString());
			sql.append(",");
			// 对于时间的排序放在最后
			if (dateType == 1) {
				sql.append("         to_char(t.REG_DATE,'yyyy') ");
			} else if (dateType == 2) {
				sql.append("         to_char(t.REG_DATE,'yyyy')  ");
				sql.append("        ,to_char(t.REG_DATE,'q')   ");
			} else if (dateType == 3) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm') ");
			} else if (dateType == 4) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd')");
			}
			sql.append("	 order by ");
			sql.append(order.toString());
			sql.append(",");
			// 对于时间的排序放在最后
			if (dateType == 1) {
				sql.append("         to_char(t.REG_DATE,'yyyy') ");
			} else if (dateType == 2) {
				sql.append("         to_char(t.REG_DATE,'yyyy')  ");
				sql.append("        ,to_char(t.REG_DATE,'q')   ");
			} else if (dateType == 3) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm') ");
			} else if (dateType == 4) {
				sql.append("         to_char(t.REG_DATE,'yyyy/mm/dd')");
			}
			// System.out.println(sql.toString());
			SQLQuery queryObject1 = this.getSession().createSQLQuery(
					sql.toString());

			for (int i = 0; i < diArrayKey.length; i += 2) {
				if ("reg_dept_code".equals(diArrayKey[i])) {
					queryObject1.addScalar("deptDimensionality");
				}
				if ("doct_code".equals(diArrayKey[i])) {
					queryObject1.addScalar("doctorDimensionality");
				}
				if ("YNBOOK".equals(diArrayKey[i])) {
					queryObject1.addScalar("doctorlevelDimensionality");
				}

			}
			queryObject1.addScalar("qty", Hibernate.DOUBLE)
					.addScalar("sprice", Hibernate.DOUBLE)
					.addScalar("an", Hibernate.DOUBLE)
					.addScalar("mom", Hibernate.DOUBLE).addScalar("timeChose");
			List<OptRecipedetailVo> bdl = queryObject1.setResultTransformer(
					Transformers.aliasToBean(OptRecipedetailVo.class)).list();

			if (bdl.get(0).getDoctorDimensionality() != null) {
				if (dateType == 1) {
					// 为以年为划分的维度增加缺少对象（有医生属性的）
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {

								if (choicelist(timelist, start)) {
									bdl.add(new OptRecipedetailVo(bdl.get(i)
											.getDeptDimensionality(), bdl
											.get(i).getDoctorDimensionality(),
											start + ""));
								}

								// todo 判断月日

							}

						}
					}

				} else if (dateType == 2) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getQuarter1(); j <= datevo
										.getQuarter2(); j++) {
									if (choicelistquarter(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												bdl.get(i)
														.getDoctorDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}
				} else if (dateType == 3) {
					// 为以月份划分的维度增加缺少对象（有医生属性的）

					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {
									if (choicelistmm(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												bdl.get(i)
														.getDoctorDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}

				} else if (dateType == 4) {
					// 为以日期划分的维度增加缺少对象（有医生属性的）
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDoctorDimensionality())) {
							cell[q] = bdl.get(i).getDoctorDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDoctorDimensionality()
										.equals(bdl.get(i)
												.getDoctorDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {

									for (int j2 = datevo.getDay1(); j2 <= datevo
											.getDay2(); j2++) {
										if (choicelist1(timelist, j2, j, start)) {
											bdl.add(new OptRecipedetailVo(
													bdl.get(i)
															.getDeptDimensionality(),
													bdl.get(i)
															.getDoctorDimensionality(),
													start + "/" + j + "/" + j2));
										}

									}
								}

								// todo 判断月日

							}

						}
					}
				}
			} else {
				if (dateType == 1) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {

								if (choicelist(timelist, start)) {
									bdl.add(new OptRecipedetailVo(bdl.get(i)
											.getDeptDimensionality(), start
											+ ""));
								}

								// todo 判断月日

							}

						}
					}

				} else if (dateType == 2) {

					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getQuarter1(); j <= datevo
										.getQuarter2(); j++) {
									if (choicelistquarter(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}

				} else if (dateType == 3) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;
							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {
									if (choicelistmm(timelist, j, start)) {
										bdl.add(new OptRecipedetailVo(
												bdl.get(i)
														.getDeptDimensionality(),
												start + "/" + j));
									}

								}

							}
						}
					}

				} else if (dateType == 4) {
					String[] cell = new String[bdl.size() + 1];
					int q = 0;
					for (int i = 0; i < bdl.size(); i++) {
						if (choice(cell, bdl.get(i).getDeptDimensionality())) {
							cell[q] = bdl.get(i).getDeptDimensionality();
							q++;

							List timelist = new ArrayList();
							timelist.add(bdl.get(i).getTimeChose());
							for (int m = i + 1; m < bdl.size(); m++) {
								if (bdl.get(m)
										.getDeptDimensionality()
										.equals(bdl.get(i)
												.getDeptDimensionality()))
									timelist.add(bdl.get(m).getTimeChose());
							}
							for (int start = datevo.getYear1(); start <= datevo
									.getYear2(); start++) {
								for (int j = datevo.getMonth1(); j <= datevo
										.getMonth2(); j++) {

									for (int j2 = datevo.getDay1(); j2 <= datevo
											.getDay2(); j2++) {
										if (choicelist1(timelist, j2, j, start)) {
											bdl.add(new OptRecipedetailVo(bdl
													.get(i)
													.getDeptDimensionality(),
													start + "/" + j + "/" + j2));
										}

									}
								}

								// todo 判断月日

							}

						}
					}
				}
			}

			for (int i = 0; i < bdl.size(); i++) {
				if (bdl.get(i).getAn() == null) {
					bdl.get(i).setAn(0.00);
				}
				if (bdl.get(i).getMom() == null) {
					bdl.get(i).setMom(0.00);
				}
				if (bdl.get(i).getQty() == null) {
					bdl.get(i).setQty(0.00);
				}
				if (bdl.get(i).getSprice() == null) {
					bdl.get(i).setSprice(0.00);
				}
			}
			for (int i = 0; i < bdl.size(); i++) {

				bdl.get(i).setAn(
						(double) Math.round(bdl.get(i).getAn() * 100) / 100);
				bdl.get(i).setMom(
						(double) Math.round(bdl.get(i).getMom() * 100) / 100);

				bdl.get(i)
						.setSprice(
								(double) Math
										.round(bdl.get(i).getSprice() * 100) / 100);
			}

			if (bdl != null) {
				return bdl;
			}
			return new ArrayList<OptRecipedetailVo>();
		}
	}

	public boolean choice(String[] cell, String bdl) {
		for (int k = 0; k < cell.length; k++) {
			if (bdl.equals(cell[k])) {
				return false;
			}
		}
		return true;
	}

	public boolean choicelist(List list, int start) {

		for (int i = 0; i < list.size(); i++) {
			if (Integer.parseInt((String) list.get(i)) == start) {
				return false;
			}

		}
		return true;
	}

	public boolean choicelistmm(List list, int mm, int yyyy) {
		for (int i = 0; i < list.size(); i++) {
			String[] a = ((String) list.get(i)).split("/");

			if (Integer.parseInt(a[0]) == yyyy && Integer.parseInt(a[1]) == mm) {
				return false;
			}

		}
		return true;
	}

	public boolean choicelist1(List list, int j, int j2, int start) {

		for (int i = 0; i < list.size(); i++) {
			String[] a = ((String) list.get(i)).split("/");

			if (Integer.parseInt(a[0]) == start && Integer.parseInt(a[1]) == j2
					&& Integer.parseInt(a[2]) == j) {
				return false;
			}

		}
		return true;
	}

	public boolean choicelistquarter(List list, int j2, int start) {

		for (int i = 0; i < list.size(); i++) {
			String[] a = ((String) list.get(i)).split("/");

			if (Integer.parseInt(a[0]) == start && Integer.parseInt(a[1]) == j2) {
				return false;
			}

		}
		return true;
	}

	@Override
	public List<BiBaseOrganization> queryDeptForBiPublic(String deptType) {
		String sql = "select o.ORG_CODE as orgCode , o.ORG_NAME as orgName from BI_BASE_ORGANIZATION o";
		// 判断参数deptType科室类型是否有值
		if (StringUtils.isNotBlank(deptType)) {
			if (deptType.indexOf(",") != -1) {
				deptType = deptType.replace(",", "','");
				sql += " where o.org_kind_code in ('"
						+ deptType
						+ "') and o.org_Code <>'0000001' and o.org_Code <> '0000002'";
			} else {
				sql += " where o.org_kind_code= '"
						+ deptType
						+ "' and o.org_Code <>'0000001' and o.org_Code <> '0000002'";
			}

		}
		SQLQuery queryObject = this.getSession().createSQLQuery(sql);
		queryObject.addScalar("orgCode").addScalar("orgName");
		List<BiBaseOrganization> bdl = bdl = queryObject.setResultTransformer(
				Transformers.aliasToBean(BiBaseOrganization.class)).list();
		if (bdl != null) {
			return bdl;
		}
		return new ArrayList<BiBaseOrganization>();
	}

}
