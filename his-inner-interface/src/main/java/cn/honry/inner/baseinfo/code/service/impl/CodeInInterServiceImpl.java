package cn.honry.inner.baseinfo.code.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.base.bean.model.BusinessDictionary;
import cn.honry.base.bean.model.DutiesContrast;
import cn.honry.base.bean.model.TitleContrast;
import cn.honry.base.bean.model.TypeContrast;
import cn.honry.inner.baseinfo.code.dao.CodeInInterDAO;
import cn.honry.inner.baseinfo.code.service.CodeInInterService;
import cn.honry.inner.vo.ComboGroupVo;
import cn.honry.utils.RedisUtil;
import cn.honry.utils.TreeJson;
/**
 * 
 * @Description 公共编码资料service实现层
 * @author  lyy
 * @createDate： 2016年7月5日 下午3:34:22 
 * @modifier lyy
 * @modifyDate：2016年7月5日 下午3:34:22
 * @param：  
 * @modifyRmk：  
 * @version 1.0
 */
@Service("innerCodeService")
@Transactional
@SuppressWarnings({"all"})
public class CodeInInterServiceImpl implements CodeInInterService {
	@Resource
	private CodeInInterDAO innerCodeDao;
	
	Logger logger = Logger.getLogger(CodeInInterServiceImpl.class);
	
	@Resource
	private RedisUtil redis;
	
	@Override
	public void removeUnused(String id) {
	}

	@Override
	public BusinessDictionary get(String id) {
		return innerCodeDao.get(id);
	}

	@Override
	public void saveOrUpdate(BusinessDictionary entity) {
	}

	@Override
	public int getTotal(BusinessDictionary entity) {
		return innerCodeDao.getTotal(entity);
	}

	@Override
	public List<BusinessDictionary> getPage(String page, String rows, BusinessDictionary entity) {
		return innerCodeDao.getPage(page, rows, entity);
	}

	@Override
	public List<BusinessDictionary> getDictionary(String type) {
		List<BusinessDictionary> hget=null;
		try {
			hget = (List<BusinessDictionary>) redis.hget("code", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(hget!=null && hget.size()>0){
			return hget;
		}else{
			List<BusinessDictionary> list = innerCodeDao.getDictionary(type);
			try {
				redis.hset("code", type, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
	}

	@Override
	public BusinessDictionary getDictionaryByCode(String type, String code) {
		List<BusinessDictionary> hget=null;
		try {
			hget = (List<BusinessDictionary>) redis.hget("code", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BusinessDictionary bd=null;
		if(hget!=null && hget.size()>0){
			for (BusinessDictionary business : hget) {
				if(business.getEncode() != null && business.getEncode().equals(code)){
					bd= business;
				}
			}
		}
		if(bd==null){
			bd= innerCodeDao.getDictionaryByCode(type, code);
		}
		return bd;
	}

	/**  
	 * 
	 * 根据编码类别获得map
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月31日 下午4:42:39 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月31日 下午4:42:39 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @param:
	 * @throws:
	 * @return: 
	 *
	 */
	@Override
	public Map<String, String> getBusDictionaryMap(String type) {
		Map<String, String> hget=null;
		try {
			hget = (Map<String, String>) redis.hget("codeMap", type);
			logger.info("从缓存中读取编码map  key：codeMap  filed：" + type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		BusinessDictionary bd=null;
		if(hget == null || hget.size() == 0){
			hget = innerCodeDao.getBusDictionaryMap(type);
			try {
				redis.hset("codeMap", type, hget);
				logger.info("向缓存中添加编码map  key：codeMap  filed：" + type);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hget;
	}
	
	/**
	 * 下拉框查询
	 * @param code 参数  查询条件的参数
	 * @return List<CodeCenterfeecode>
	 */
	@Override
	public List<BusinessDictionary> likeSearch(String code) {
		return innerCodeDao.likeSearch(code);
	}

	/**
	 * 
	 * @Description 根据类型和名称 查询公共编码资料
	 * @author  aizhonghua
	 * @createDate： 2016年7月5日 下午3:25:51 
	 * @modifier aizhonghua
	 * @modifyDate：2016年7月5日 下午3:25:51
	 * @param：  type 类型     name 名称
	 * @modifyRmk：  
	 * @version 1.0
	 */
	@Override
	public BusinessDictionary getDictionaryByName(String type, String name) {
		return innerCodeDao.getDictionaryByName(type, name);
	}

	@Override
	public List<TreeJson> QueryTreeDictionary(String type,String treeName) {
		List<BusinessDictionary> dictionaryList=null;
		try {
			dictionaryList = (List<BusinessDictionary>) redis.hget("code", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(dictionaryList==null|| dictionaryList.size()==0){
			dictionaryList=innerCodeDao.getDictionary(type);
			try {
				redis.hset("code", type, dictionaryList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<TreeJson> treeJsonList = new ArrayList<TreeJson>();
		TreeJson treeJson = new TreeJson();
		treeJson.setId("1");
		treeJson.setText(treeName);
		Map<String,String> map = new HashMap<String, String>();
		treeJson.setAttributes(map);
		treeJsonList.add(treeJson);
		if(dictionaryList!=null&&dictionaryList.size()>0){
			for (BusinessDictionary treebus : dictionaryList) {
				TreeJson treeJsonbus = new TreeJson();
				treeJsonbus.setId(treebus.getEncode());
				treeJsonbus.setText(treebus.getName());
				Map<String,String> attributes = new HashMap<String, String>();
				attributes.put("pid","1");
				treeJsonbus.setAttributes(attributes);
				treeJsonList.add(treeJsonbus);
			}
		}
		return TreeJson.formatTree(treeJsonList);
	}

	@Override
	public List<BusinessDictionary> likeTypeSearch(String type, String code,String page,String  rows) {
		return innerCodeDao.likeTypeSearch(type,code,page,rows);
	}

	@Override
	public int getTypeTotal(String type, String code) {
		return innerCodeDao.getTypeTotal(type,code);
	}

	@Override
	public List<BusinessDictionary> searchCode(String type, String code) {
		return innerCodeDao.searchCode(type,code);
	}

	@Override
	public List<BusinessDictionary> querybkackList(String type) {
		return innerCodeDao.querybkackList(type);
	}

	/**  
	 * 
	 * <p> 获取全部（非）药品单位分组 </p>
	 * @Author: aizhonghua
	 * @CreateDate: 2016年10月18日 下午1:39:08 
	 * @Modifier: aizhonghua
	 * @ModifyDate: 2016年10月18日 下午1:39:08 
	 * @ModifyRmk:  
	 * @version: V1.0
	 * @return: List<ComboGroupVo>
	 *
	 */
	@Override
	public List<ComboGroupVo> getUnitAllGroup() {
		List<ComboGroupVo> list=null;
		try {
			list = (List<ComboGroupVo>)redis.hget("code", "getUnitAllGroup");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(list!=null&&list.size()>0){
			return list;
		}
		list = innerCodeDao.getUnitAllGroup();
		try {
			redis.hset("code", "getUnitAllGroup", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<BusinessDictionary> getDictionaryICD(String type) {
		return innerCodeDao.getDictionaryICD(type);
	}

	@Override
	public List<TitleContrast> getTitleContrasts() {
		return innerCodeDao.getTitleContrasts();
	}

	@Override
	public List<DutiesContrast> getDutiesContrasts() {
		return innerCodeDao.getDutiesContrasts();
	}
	
	@Override
	public List<TypeContrast> getTypeContrasts() {
		return innerCodeDao.getTypeContrasts();
	}
	
	@Override
	public byte[] exportExcel(String[][] values) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		//设置单元格格式为文本格式
		HSSFDataFormat format = workbook.createDataFormat();
		// 列头样式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setDataFormat(format.getFormat("@"));//设置单元格格式为"文本"
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中对齐
		HSSFFont headerFont = workbook.createFont();//创建字体
		headerFont.setFontHeightInPoints((short) 12);//字体大小
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体
		headerStyle.setFont(headerFont);//设置列头样式
		// 单元格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(format.getFormat("@"));//设置单元格格式为"文本"
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//居中对齐
		HSSFFont cellFont = workbook.createFont();//创建字体
		cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//不加粗
		cellStyle.setFont(cellFont);//设置数据单元格样式
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet();
		// 遍历集合数据，产生数据行
		if(values != null && values.length > 0){
			for (int i = 0; i < values.length; i++) {
				String[] rows = values[i];
				HSSFRow row = sheet.createRow(i); //创建一行
				if (rows != null && rows.length > 0) {
					for (int j = 0; j < rows.length; j++) {
						row.createCell(j);
						if(i == 0){//分别给列头和数据单元格设置格式
							row.getCell(j).setCellStyle(headerStyle);
							row.getCell(j).setCellType(HSSFCell.CELL_TYPE_STRING);
						}else {
							row.getCell(j).setCellStyle(cellStyle);
							row.getCell(j).setCellType(HSSFCell.CELL_TYPE_STRING);
						}
						row.getCell(j).setCellValue(rows[j]);
					}
				}
			}
			// 自动调整宽度
			for (int i = 0; i < values[0].length; i++) {
				sheet.autoSizeColumn(i);
			}
		}
		try {
			workbook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toByteArray();
	}
}
