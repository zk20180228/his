package cn.honry.inner.baseinfo.district.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.District;
import cn.honry.inner.baseinfo.district.dao.DistrictInnerDao;
import cn.honry.inner.baseinfo.district.service.DistrictInnerService;
import cn.honry.utils.RedisUtil;
//import cn.honry.register.blacklist.dao.BlackDao;
import cn.honry.utils.TreeJson;



@Service("districtInnerService")
@Transactional
@SuppressWarnings({ "all" })
public class DistrictInnerServiceImpl implements DistrictInnerService{
	
	
	@Resource
	private RedisUtil redis;
	
	@Autowired
	@Qualifier(value = "districtInnerDAO")
	private DistrictInnerDao districtDAO;

	@Override
	public void removeUnused(String id) {
		
	}

	@Override
	public District get(String id) {
		return districtDAO.get(id);
	}

	@Override
	public void saveOrUpdate(District entity) {
		String str = districtDAO.getSpellCode(entity.getCityName());
		int index=str.indexOf("$");
		String pinyin=str.substring(0,index);
		String wb=str.substring(index+1);
		if(StringUtils.isBlank(entity.getId())){//保存
			entity.setPinyin(pinyin);
			entity.setWb(wb);
			entity.setId(null);
			Integer ordertoPath = districtDAO.getOrder("order");
			entity.setOrdertoPath(ordertoPath);
			if("1".equals(entity.getParentId())){//父级为角色树
				entity.setParentId("1");
				entity.setPath(count(4,entity.getOrdertoPath().toString().length())+entity.getOrdertoPath()+",");
				entity.setUpperPath(null);//所有父级
				entity.setLevel(1);//层级
			}else{
				District parentDistrict = districtDAO.get(entity.getParentId());//获得父类
				entity.setPath(parentDistrict.getPath()+count(4,entity.getOrdertoPath().toString().length())+entity.getOrdertoPath()+",");
				if(StringUtils.isBlank(parentDistrict.getUpperPath())){
					entity.setUpperPath(parentDistrict.getId()+",");//所有父级
				}else{
					entity.setUpperPath(parentDistrict.getUpperPath()+parentDistrict.getId()+",");//所有父级
				}
				entity.setLevel(entity.getUpperPath().split(",").length+1);//层级
			}
			districtDAO.save(entity);
//			OperationUtils.getInstance().conserve(null,"行政区代码","INSERT INTO","T_DISTRICT",OperationUtils.LOGACTIONINSERT);
		}else{//修改
			if(!pinyin.equals(entity.getPinyin())||!wb.equals(entity.getWb())){
				entity.setPinyin(pinyin);
				entity.setWb(wb);					
			}
			District districtOld = districtDAO.get(entity.getId());
			if(!entity.getParentId().equals(districtOld.getParentId())){//如果修改了父节点 需要重新保存信息
				String ids = entity.getId();
			//	String path = entity.getOrdertoPath()+",";
				if("1".equals(entity.getParentId())){//父级为角色树
					entity.setPath(count(4,districtOld.getOrdertoPath().toString().length())+districtOld.getOrdertoPath()+",");
					entity.setUpperPath(null);//所有父级
					entity.setLevel(1);//层级
					/*entity.setOrdertoPath(districtOld.getOrdertoPath());
					entity.setOrder(districtOld.getOrdertoPath());*/
				}else{
					District parentDistrict = districtDAO.get(entity.getParentId());//获得父类
					entity.setPath(parentDistrict.getPath()+count(4,parentDistrict.getOrdertoPath().toString().length())+entity.getOrdertoPath()+",");//层级路径
					if(StringUtils.isBlank(parentDistrict.getUpperPath())){
						entity.setUpperPath(parentDistrict.getId()+",");//所有父级
					}else{
						entity.setUpperPath(parentDistrict.getUpperPath()+parentDistrict.getId()+",");//所有父级
					}
					entity.setLevel(entity.getUpperPath().split(",").length+1);//层级
					/*entity.setOrdertoPath(districtOld.getOrdertoPath());
					entity.setOrder(districtOld.getOrdertoPath());*/
					
				}
				
			}
			districtDAO.clear();
			districtDAO.save(entity);
//			OperationUtils.getInstance().conserve(entity.getId(),"行政区代码","UPDATE","T_DISTRICT",OperationUtils.LOGACTIONUPDATE);
		}
		
	}
	
	/**  
	 *  
	 * @Description：  计算层级路径
	 * @Author：aizhonghua
	 * @CreateDate：2015-7-31 上午10:31:33  
	 * @Modifier：aizhonghua
	 * @ModifyDate：2015-7-31 上午10:31:33  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	public String count(Integer length,Integer dlen){
		String cV = "";
		Integer wS = length - dlen;
		if(wS>0){
			for (int i = 1; i <= wS; i++) {
				cV += "0";
			}
		}
		return cV;
	}

	
	/**  
	 *  
	 * @Description：  分页查询－获得列表数据
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> getPage(String page, String rows,
			District district) {
		return districtDAO.getPage(page,rows,district);
	}
	/**  
	 *  
	 * @Description：  分页查询－获得总条数
	 * @Author：wujiao
	 * @CreateDate：2015-10-29 上午11:59:48  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-10-29上午11:59:48  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public int getTotal(District district) {
		return districtDAO.getTotal(district);
	}

	/**  
	 *  
	 * @Description：  获得栏目树
	 * @Author：wujiao
	 * @CreateDate：2015-11-2 上午12:16:53  
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-2 上午12:16:53  
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public List<TreeJson> queryDistrictTree( String parId) {
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		//根节点
		TreeJson gTreeJson = new TreeJson();
		gTreeJson.setId("1");
		gTreeJson.setText("中国");
		Map<String,String> gAttMap = new HashMap<String, String>();
		gTreeJson.setAttributes(gAttMap);
		treeJsonList.add(gTreeJson);
		List<District> districtList=null;
		try {
			districtList = (List<District>) redis.get("district_queryAll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(districtList==null){
			districtDAO.queryDistrictTree(parId);
		}
		if(districtList!=null&&districtList.size()>0){
			for(District dis : districtList){
				TreeJson ysTreeJson = new TreeJson();
				ysTreeJson.setId(dis.getCityCode());
				ysTreeJson.setText(dis.getCityName());
				Map<String,String> ysAttMap = new HashMap<String, String>();
				ysAttMap.put("pid",StringUtils.isBlank(dis.getParentId())?"1":dis.getParentId());
				ysAttMap.put("ids",dis.getId());
				ysAttMap.put("cityName", dis.getCityName());
				ysTreeJson.setAttributes(ysAttMap);
				treeJsonList.add(ysTreeJson);
			}
		}
		return treeJsonList;
	}
	
	
	/**  
	 *  
	 * @Description：  获得栏目树 三级联动
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53    
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> queryDistricttreeSJLD( Integer  ld , String parId) {
		List<District> districtList = districtDAO.queryDistrictTreeSJLD(ld,parId);
		return districtList;
	}
	
	/**  
	 *  
	 * @Description：  获得栏目树 三级联动查询
	 * @Author：zpty
	 * @CreateDate：2015-12-16 上午12:16:53    
	 * @version 1.0
	 *
	 */
	@Override
	public List<District> queryDistricttreeSJLDCX(String parId) {
		List<District> districtListOnly = districtDAO.queryDistrictTreeSJLDCX(parId);
		return districtListOnly;
	}
	
	/**
	 * 获取某一城市的所有下级
	 * @param code 城市代码
	 * @return
	 */
	public List<District> queryByCityCode(String code){
		List<District> list=null;
		try {
			list = (List<District>) redis.hget("district", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null){
			return list;
		}else{
			if(StringUtils.isNotBlank(code)){
				return districtDAO.queryByPid(code);
			}
		}
		return new ArrayList();
	}
	
	/**  
	 *  
	 * @Description：  删除
	 * @Author：wujiao
	 * @CreateDate：2015-11-2 上午12:16:53   
	 * @Modifier：wujiao
	 * @ModifyDate：2015-11-2 上午12:16:53   
	 * @ModifyRmk：  
	 * @version 1.0
	 *
	 */
	@Override
	public void delDis(String id) {
		districtDAO.delDis(id);
//		OperationUtils.getInstance().conserve(id,"行政区代码","UPDATE","T_DISTRICT",OperationUtils.LOGACTIONDELETE);

		
	}


	/**
	 * 解析上传的excel文件
	 * zpty 20151124
	 * @param inputStream
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String anaylzeExcelXls(InputStream inputStream){
		try {
			HSSFWorkbook	hssfWorkbook = new HSSFWorkbook(inputStream);
			ArrayList arr = new ArrayList();
			
			String oneId="";//第一层子节点id,其父节点id为1
			String twoId="";//第二层子节点id,父节点id为第一层子节点id
			String threeId="";//第三层子节点id,父节点id为第二层子节点id
			String fourId="";//第四层子节点id,父节点id为第三层子节点id
			int oneOrder=0;//第一层orderpath
			int twoOrder=0;//第二层orderpath
			int threeOrder=0;//第三层orderpath
			int fourOrder=0;//第四层orderpath
			
		 // 循环工作表Sheet   
		  for(int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++){   
			  HSSFSheet hssfSheet = hssfWorkbook.getSheetAt( numSheet);   
		      		if(hssfSheet == null){   
		      				continue;   
		      		}   
		     
		      		// 循环行Row    
		      		for(int rowNum = 1; rowNum <=  hssfSheet.getLastRowNum(); rowNum++){   
		      			HSSFRow hssfRow = hssfSheet.getRow( rowNum);   
		      			if(hssfRow == null){   
		      					continue;   
		      			}
		      			District attr=new District();
		      			
		      			UUID uuid = UUID.randomUUID();//生成一个uuid
		      			String disId=uuid.toString();//uuid变成字符串
		      			attr.setId(disId);  //设置此条数据的id  			
		      			
		      			String one="";//第一个格子内容
		      			String two="";//第二个格子内容
		      			String three="";//第三个格子内容
		      			String four="";//第四个格子内容
		      			String five="";//第五个格子内容
		      			String str="";//拼音码五笔码
		      			
		      			// 循环列Cell     
		      			for(int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++){   
		      				HSSFCell hssfCell = hssfRow.getCell( (short) cellNum);   
		      				if(hssfCell == null){   
		      					continue;   
		      				}

		      				if(cellNum==0){
		      					one = getValue( hssfCell);
		      				}else if(cellNum==1){
		      					two = getValue( hssfCell);
		      				}else if(cellNum==2){
		      					three = getValue( hssfCell);
		      				}else if(cellNum==3){
		      					four = getValue( hssfCell);
		      				}else if(cellNum==4){
		      					five = getValue( hssfCell);
		      				}		      			  
		      			}
		      			if("".equals(three) && "".equals(four) && "".equals(five)){
		      				oneId=disId;//第一层子节点
		      				oneOrder=rowNum;
		      			}else if(!"".equals(three)){
		      				twoId=disId;//第二层子节点
		      				twoOrder=rowNum;
		      			}else if(!"".equals(four)){
		      				threeId=disId;//第三层子节点
		      				threeOrder=rowNum;
		      			}else if(!"".equals(five)){
		      				fourId=disId;//第四层子节点
		      				fourOrder=rowNum;
		      			}		      			
		      			
		      			 //设置字段
		      			attr.setCityCode(one);//citycode城市代码
		      			attr.setShortname("");//城市简称(暂时没有)
		      			attr.setEname("");//英文名称(暂时没有)
		      			
		      			if("".equals(three) && "".equals(four) && "".equals(five)){
			      			attr.setCityName(two);//城市名称
		      				attr.setParentId("1");//第一层子节点//生成路径排序号
		      				attr.setPath(lpad(4,rowNum)+",");//城市路径
		      				attr.setUpperPath("");//所有父级
		      				attr.setLevel(1);//城市层级
		      				str = districtDAO.getSpellCode(two);//拼音码五笔码
		      			}else if(!"".equals(three)){
			      			attr.setCityName(three);//城市名称
		      				attr.setParentId(oneId);//第二层子节点//生成路径排序号
		      				attr.setPath(lpad(4,oneOrder)+","+ lpad(4,rowNum)+",");//城市路径
		      				attr.setUpperPath(oneId+",");//所有父级
		      				attr.setLevel(2);//城市层级
		      				str = districtDAO.getSpellCode(three);//拼音码五笔码
		      			}else if(!"".equals(four)){
		      				attr.setCityName(four);//城市名称
		      				attr.setParentId(twoId);//第三层子节点//生成路径排序号
		      				attr.setPath(lpad(4,oneOrder)+","+ lpad(4,twoOrder)+","+ lpad(4,rowNum)+",");//城市路径
		      				attr.setUpperPath(oneId+","+twoId+",");//所有父级
		      				attr.setLevel(3);//城市层级
		      				str = districtDAO.getSpellCode(four);//拼音码五笔码
		      			}else if(!"".equals(five)){
		      				attr.setCityName(five);//城市名称
		      				attr.setParentId(threeId);//第四层子节点//生成路径排序号
		      				attr.setPath(lpad(4,oneOrder)+","+ lpad(4,twoOrder)+","+ lpad(4,threeOrder)+","+ lpad(4,rowNum)+",");//城市路径
		      				attr.setUpperPath(oneId+","+twoId+","+threeId+",");//所有父级
		      				attr.setLevel(4);//城市层级
		      				str = districtDAO.getSpellCode(five);//拼音码五笔码
		      			}		     
		      			
		      			attr.setOrdertoPath(rowNum);//生成路径排序号
		      			attr.setOrder(rowNum);//排序号
		      			
		      			if(two.indexOf("市")>0){//是否是直辖市1:否,2:是
		      				attr.setMunicpalityFlag(2);
		      			}else{
		      				attr.setMunicpalityFlag(1);
		      			}
		      			
		      			//拼音码五笔码生成
		      			int index=str.indexOf("$");
		      			String pinyin=str.substring(0,index);
		      			String wb=str.substring(index+1);
		      			
		      			attr.setPinyin(pinyin);//拼音码
		      			attr.setWb(wb);//五笔码
		      			attr.setDefined(one);//自定义码
		      			attr.setValidFlag(1);//是否有效1:有效,2:无效
		      			attr.setRemark("");//备注
		      			arr.add(attr);
		      			
		      	}   
		    }
		  if(arr.size()>0){
			  Query querys = getSession().createSQLQuery("DELETE FROM t_district");
			  querys.executeUpdate();
			  for (int j = 0; j < arr.size(); j++) {
				  District attrs = (District) arr.get(j);
				  Query query = getSession().createSQLQuery("insert into t_district(ID,CITY_CODE,CITY_NAME,CITY_SHORTNAME,CITY_ENAME,CITY_PARENTID,CITY_PATH,CITY_ORDERTOPATH,CITY_UPPERPATH,CITY_LEVEL,CITY_ORDER,MUNICIPALITY_FLAG,CODE_PINYIN,CODE_WUBI,CODE_DEFINED,VALID_FLAG,REMARK) values ('"+attrs.getId()+"','"+attrs.getCityCode()+"','"+attrs.getCityName()+"','"+attrs.getShortname()+"','"+attrs.getEname()+"','"+attrs.getParentId()+"','"+attrs.getPath()+"',"+attrs.getOrdertoPath()+",'"+attrs.getUpperPath()+"',"+attrs.getLevel()+","+attrs.getOrder()+","+attrs.getMunicpalityFlag()+",'"+attrs.getPinyin()+"','"+attrs.getWb()+"','"+attrs.getDefined()+"',"+attrs.getValidFlag()+",'"+attrs.getRemark()+"')");
				  query.executeUpdate();
			  }
		  }
		} catch (Exception e) {
			System.out.println("解析上传的excel文件出错");
			return "3";
	    }		
		return "0";
	}
	
	@Resource(name = "sessionFactory")    
    private SessionFactory sessionFactory;    
    
    public Session getSession() {    
        return sessionFactory.getCurrentSession();    
    }    
    
	//补足path
	 private String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }	
	
	private static String getValue(HSSFCell xssfCell){   
	    if(xssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN){   
	      return String.valueOf( xssfCell.getBooleanCellValue());   
	   }else if(xssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){   
		  if(HSSFDateUtil.isCellDateFormatted(xssfCell)){
			  Date dateCellValue = xssfCell.getDateCellValue();
			  return new SimpleDateFormat("yyyy-MM-dd").format(dateCellValue);
		  }else{
			  return String.valueOf( (int)xssfCell.getNumericCellValue());   
		  }
	      
	    }else{ 
	    	return xssfCell.getStringCellValue();   
	    } 
	}

	@Override
	public String getDistriByCountyCode(String code) {
		List<District> districtList = districtDAO
				.queryDistrictTreeSJLDCX(code);
		String one = "";
		String two = "";
		String three = "";
		if (districtList != null && districtList.size() > 0) {
			String path = districtList.get(0).getUpperPath();
			if (StringUtils.isNotBlank(path)) {
				String[] pathss = path.split(",");
				if (pathss.length < 3) {
					one = pathss[0];
					two = pathss[1];
				} else {
					one = pathss[0];
					two = pathss[1];
				}
			}
			three = districtList.get(0).getCityName();
		}
		districtList = districtDAO
				.queryDistrictTreeSJLDCX(one);
		if (districtList != null && districtList.size() > 0) {
			one = districtList.get(0).getCityName();
		}
		districtList = districtDAO
				.queryDistrictTreeSJLDCX(two);
		if (districtList != null && districtList.size() > 0) {
			two = districtList.get(0).getCityName();
		}
		return one + "," + two + "," + three;
	}

	@Override
	public Integer vailCode(String disCode, String disId) {
		Integer num = districtDAO.vailCode(disCode, disId);
		if(num == null){
			return 0;
		}
		return num;
	}

}
