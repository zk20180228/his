<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<table id="secTable"  class="honry-table secEditTabDateSize" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%;">
			<tr>
				<td class="honry-lable" ><span>使用方法：</span></td>
    			<td><input  class="easyui-combobox"  id="CodeUseage" name="drugUsemode" onkeydown="KeyDown(0,'CodeUseage')" value="${drugInfo.drugUsemode}"   /></td>
    			<td class="honry-lable"  >
    			<span>一次用量：</span></td>
    			<td><input class="easyui-numberbox" type="text" id="drugOncedosage" name="drugOncedosage" value="${drugInfo.drugOncedosage}"   /></td>
    		</tr>
    		<tr>
				<td class="honry-lable" ><span>频次：</span></td>
    			<td>
    			<input class="easyui-combobox"  id="drugFrequency" name="drugFrequency" value="${drugInfo.drugFrequency}"/>
    			</td>
    			<td class="honry-lable" ><span>存储条件：</span></td>
    			<td><input id="CodeDrugstorage" name="drugStorage" value="${drugInfo.drugStorage}"  onkeydown="KeyDown(0,'CodeDrugstorage')"  /></td>
    		</tr>
    		
    		<tr>
				<td class="honry-lable" ><span>一级药理：</span></td>
    			<td><input id="drugPrimarypharmacology" name="drugPrimarypharmacology" value="${drugInfo.drugPrimarypharmacology}"  onkeydown="KeyDown(0,'drugPrimarypharmacology')" /></td>
    			<td class="honry-lable" ><span>二级药理：</span></td>
    			<td><input id="drugTwogradepharmacology" name="drugTwogradepharmacology" value="${drugInfo.drugTwogradepharmacology}"   onkeydown="KeyDown(0,'drugTwogradepharmacology')"   /></td>
    		</tr>
    		<tr>
				<td class="honry-lable"><span>三级药理：</span></td>
    			<td><input id="drugThreegradepharmacology" name="drugThreegradepharmacology" value="${drugInfo.drugThreegradepharmacology}"    onkeydown="KeyDown(0,'drugThreegradepharmacology')"  /></td>
    			<td class="honry-lable" ><span>入院时间：</span></td>
    			<td>
    				<input id="drugEntertime" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="drugEntertime" value="${drugInfo.drugEntertimeStr}"/>
    			</td>
    		</tr>
    		<tr>
				<td class="honry-lable" ><span>供货公司：</span></td>
    			<td>
    				<input class="easyui-combobox" type="text" id="drugSupplycompany" name="drugSupplycompany" value="${drugInfo.drugSupplycompany}" />
    				
    			</td>
    			<td class="honry-lable" ><span>批文信息：</span></td>
    			<td><input class="easyui-textbox" type="text" id="drugDocument" name="drugDocument" value="${drugInfo.drugDocument}"   /></td>
    		</tr>
    		<tr>
				<td class="honry-lable" ><span>注册商标：</span></td>
    			<td><input class="easyui-textbox" type="text" id="drugTrademark" name="drugTrademark" value="${drugInfo.drugTrademark}"   /></td>
    			<td class="honry-lable" ><span>产地：</span></td>
    			<td><input class="easyui-textbox" type="text" id="drugPlaceoforigin" name="drugPlaceoforigin" value="${drugInfo.drugPlaceoforigin}"   /></td>
    		</tr>
    		<tr>
				<td class="honry-lable" ><span>执行标准：</span></td>
    			<td><input class="easyui-textbox" type="text" id="drugOperativenorm" name="drugOperativenorm" value="${drugInfo.drugOperativenorm}"   /></td>
    			<td class="honry-lable" ><span>库位号：</span></td>
    			<td><input class="easyui-textbox" type="text" id="drugLibno" name="drugLibno" value="${drugInfo.drugLibno}"   /></td>
    		</tr>
    		<tr>
				<td class="honry-lable" ><span>条形码：</span></td>
    			<td><input class="easyui-textbox" type="text" id="drugBarcode" name="drugBarcode" value="${drugInfo.drugBarcode}"   /></td>
    			<td class="honry-lable" ><span>中标公司：</span></td>
    			<td>
    				<input  class="easyui-textbox" id="drugWinningcompany" name="drugWinningcompany" value="${drugInfo.drugWinningcompany}"  style="border-color: #95b8e7;border-style: solid;" border=1px; onmouseout="validCheck();"/>
    			</td>
    		</tr>
    		<tr>	
    			<td class="honry-lable" ><span>合同代码</span>：</td>
    			<td><input class="easyui-textbox" type="text" id="drugContractcode" name="drugContractcode" value="${drugInfo.drugContractcode}"   /></td>
				<td class="honry-lable" ><span>中标价：</span></td>
    			<td><input class="easyui-numberbox" type="text" id="drugBiddingprice" name="drugBiddingprice" value="${drugInfo.drugBiddingprice}" data-options="min:0,precision:4"  /></td>
    		</tr>
    		<tr>	
    			<td class="honry-lable" ><span>重量</span>：</td>
    			<td><input class="easyui-textbox" type="text" id="weight" name="weight" value="${drugInfo.weight}"   /></td>
				<td class="honry-lable" ><span>重量单位：</span></td>
    			<td><input class="easyui-textbox" type="text" id="weightUnit" name="weightUnit" value="${drugInfo.weightUnit}"   /></td>
    		</tr>
    		<tr>	
    			<td class="honry-lable" ><span>体积</span>：</td>
    			<td><input class="easyui-textbox" type="text" id="volum" name="volum" value="${drugInfo.volum}"   /></td>
				<td class="honry-lable" ><span>体积单位：</span></td>
    			<td><input class="easyui-textbox" type="text" id="volUnit" name="volUnit" value="${drugInfo.volUnit}"   /></td>
    		</tr>
    		<tr>	
    			<td class="honry-lable" ><span>起始日期：</span></td>
    			<td><input id="drugStartdate" class="Wdate" type="text" onclick="var endDate=$dp.$('drugEnddate');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'drugEnddate\')}'})" name="drugStartdate" value="${drugInfo.drugStartDateStr}"/></td>
				<td class="honry-lable" ><span>终止日期：</span></td>
    			<td><input id="drugEnddate" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'drugStartdate\')}'})" name="drugEnddate" value="${drugInfo.drugEndDateStr}"/></td>
    		</tr>
    		<tr>
    		<td class="honry-lable" ><span>有效期：</span></td>
    		<td><input class="easyui-numberbox" id="effMonth" name="effMonth" value="${drugInfo.effMonth}"   /></td>
			<td class="honry-lable" ><span>药品的治疗方向分类：</span></td>
    		<td><input class="easyui-combobox" id="CodeDrugclass" name="drugClass" value="${drugInfo.drugClass}"   /></td>
			
    		</tr>
    		<tr>
    			<td class="honry-lable" ><span>有效成分：</span></td>
    			<td colspan="3"><input class="easyui-textbox" type="text" id="drugActiveingredient" name="drugActiveingredient" value="${drugInfo.drugActiveingredient}"  style="width:100%;height: 50px" /></td>
			</tr>
			<tr>
				<td class="honry-lable" ><span>药品简介：</span></td>
    			<td colspan="3"><input class="easyui-textbox" type="text" id="drugBrev" name="drugBrev" value="${drugInfo.drugBrev}" style="width:100%;height: 50px" /></td>
    		</tr>
    		<tr>
				<td class="honry-lable" ><span>说明书：</span></td>
    			<td colspan="3"><input class="easyui-textbox" type="text" id="drugInstruction" name="drugInstruction" value="${drugInfo.drugInstruction}" style="width:100%;height: 50px" /></td>
    		<tr>	
    			<td class="honry-lable" ><span>注意事项：</span></td>
    			<td colspan="3"><input class="easyui-textbox" type="text" id="drugNotes" name="drugNotes" value="${drugInfo.drugNotes}" style="width:100%;height: 50px" /></td>
    		</tr>
    		<tr>
				<td class="honry-lable" >备注：</td>
    			<td colspan="3"><input class="easyui-textbox" id="drugRemark" name="drugRemark" value="${drugInfo.drugRemark}"  style="width:100%;height: 50px" /></td>
			</tr>
		</table> 
		<div style="text-align: center; padding: 5px">
			<c:if test="${empty drugInfo.id}">
				<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			</c:if>
			<a  href="javascript:submit(0)" data-options="iconCls:'icon-save'"  class="easyui-linkbutton">保存</a>
			<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
		</div>
	</body>
</html>
