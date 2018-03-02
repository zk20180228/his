package cn.honry.statistics.finance.inpatientUDbalance.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.honry.statistics.doctor.regisdocscheinfo.vo.RegisDocScheInfoVo;
import cn.honry.statistics.finance.inpatientUDbalance.dao.InpatientUDBDao;
import cn.honry.statistics.finance.inpatientUDbalance.service.InpatientUDBService;
import cn.honry.statistics.finance.inpatientUDbalance.vo.InpatientUDBVo;
import cn.honry.utils.CommonStringUtils;
import cn.honry.utils.DateUtils;
import cn.honry.utils.FileUtil;

@Service("inpatientUDBService")
@Transactional
@SuppressWarnings({ "all" })
public class InpatientUDBServiceImpl implements InpatientUDBService {
	
	@Autowired
	@Qualifier(value = "inpatientUDBDao")
	private InpatientUDBDao inpatientUDBDao;
	
	@Override
	public InpatientUDBVo get(String arg0) {
		return null;
	}

	@Override
	public void removeUnused(String arg0) {
		
	}

	@Override
	public void saveOrUpdate(InpatientUDBVo arg0) {
		
	}

	@Override
	public List<InpatientUDBVo> queryDateInfo(String date) {
		
		String date1=date+" 00:00:00";
		String date2=date+" 23:59:59";
		
		return inpatientUDBDao.queryDateInfo(date1,date2);
	}


	@Override
	public void exportExcel(ServletOutputStream stream, List<InpatientUDBVo> idbvol) throws Exception {
		
		//创建workbook   
        HSSFWorkbook workbook = new HSSFWorkbook();   
       
        //创建sheet页  
        HSSFSheet sheet = workbook.createSheet("门诊收款员缴款单");   
       
        //创建单元格  
        HSSFRow row = sheet.createRow(0);   
        HSSFCell c0 = row.createCell(0);   
        c0.setCellValue(new HSSFRichTextString("收款员"));   
        HSSFCell c1 = row.createCell(1);   
        c1.setCellValue(new HSSFRichTextString("收入"));   
        HSSFCell c2 = row.createCell(4);   
        c2.setCellValue(new HSSFRichTextString("支出"));   
        HSSFCell c3 = row.createCell(7);   
        c3.setCellValue(new HSSFRichTextString("实收"));   
        HSSFCell c4 = row.createCell(10);   
        c4.setCellValue(new HSSFRichTextString("日结时间"));   
        HSSFRow row1 = sheet.createRow(1);   
        HSSFCell c6 = row1.createCell(1);   
        c6.setCellValue(new HSSFRichTextString("现金"));   
        HSSFCell c7 = row1.createCell(2);   
        c7.setCellValue(new HSSFRichTextString("支票"));   
        HSSFCell c8 = row1.createCell(3);   
        c8.setCellValue(new HSSFRichTextString("其他"));  
        HSSFCell c9 = row1.createCell(4);   
        c9.setCellValue(new HSSFRichTextString("现金"));   
        HSSFCell c10 = row1.createCell(5);   
        c10.setCellValue(new HSSFRichTextString("支票"));   
        HSSFCell c11 = row1.createCell(6);   
        c11.setCellValue(new HSSFRichTextString("其他"));  
        HSSFCell c12 = row1.createCell(7);   
        c12.setCellValue(new HSSFRichTextString("现金"));   
        HSSFCell c13 = row1.createCell(8);   
        c13.setCellValue(new HSSFRichTextString("支票"));   
        HSSFCell c14 = row1.createCell(9);   
        c14.setCellValue(new HSSFRichTextString("其他"));  
        Region region1 = new Region(0, (short)0, 1, (short)0);   
        Region region2 = new Region(0, (short)1, 0, (short)3);   
        Region region3 = new Region(0, (short)4, 0, (short)6);   
        Region region4 = new Region(0, (short)7, 0, (short)9);   
        Region region5 = new Region(0, (short)10, 1, (short)10);   
        sheet.addMergedRegion(region1);   
        sheet.addMergedRegion(region2);   
        sheet.addMergedRegion(region3);   
        sheet.addMergedRegion(region4);   
        sheet.addMergedRegion(region5); 
        int j=1;
        for(InpatientUDBVo vo:idbvol){
        	HSSFRow hrow = sheet.createRow(j+1);
        	hrow.createCell(0).setCellValue(vo.getName());
        	hrow.createCell(1).setCellValue(vo.getIcash());
        	hrow.createCell(2).setCellValue(vo.getIcheck());
        	hrow.createCell(3).setCellValue(vo.getIother());
        	hrow.createCell(4).setCellValue(vo.getOcash());
        	hrow.createCell(5).setCellValue(vo.getOcheck());
        	hrow.createCell(6).setCellValue(vo.getOother());
        	hrow.createCell(7).setCellValue(vo.getScash());
        	hrow.createCell(8).setCellValue(vo.getScheck());
        	hrow.createCell(9).setCellValue(vo.getSother());
        	hrow.createCell(10).setCellValue(vo.getTime());
        	j++;
        }
       
		workbook.write(stream);
	}

}
