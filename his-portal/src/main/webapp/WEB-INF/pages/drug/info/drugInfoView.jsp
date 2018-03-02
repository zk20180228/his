<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<body>
	<div id="tt" class="easyui-tabs" data-options="" style="">
		<div title="药品信息" style="padding:10px">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
				<tr>
					<td class="honry-lable">名称：</td>
	    			<td>${drugInfo.name}</td>
	    			<td class="honry-lable">名称自定义：</td>
	    			<td>${drugInfo.drugNameinputcode}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">通用名称：</td>
	    			<td>${drugInfo.drugCommonname}</td>
	    			<td class="honry-lable">通用名称自定义码：</td>
	    			<td>${drugInfo.drugCnameinputcode}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">基本药物1：</td>
	    			<td>${drugInfo.duugBasici}</td>
	    			<td class="honry-lable">基本药物2：</td>
	    			<td>${drugInfo.duugBasicii}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">基本药物自定义码：</td>
	    			<td>${drugInfo.drugBasicinputcode}</td>
	    			<td class="honry-lable">招标识别码：</td>
	    			<td>${drugInfo.drugBiddingcode}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">英文品名：</td>
	    			<td>${drugInfo.drugEnname}</td>
	    			<td class="honry-lable">英文别名：</td>
	    			<td>${drugInfo.drugEnalias}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">英文通用名：</td>
	    			<td>${drugInfo.drugEncommonname}</td>
	    			<td class="honry-lable">国家编码：</td>
	    			<td>${drugInfo.drugGbcode}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">库位编码：</td>
	    			<td>${drugInfo.drugLibid}</td>
	    			<td class="honry-lable">规格：</td>
	    			<td>${drugInfo.spec}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable"> 药品类别：</td>
	    			<td>${drugType}</td>
	    			<td class="honry-lable">系统类别：</td>
	    			<td>${sysType}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">最小费用：</td>
	    			<td>${drugMinCost}</td>
	    			<td class="honry-lable">药品性质：</td>
	    			<td>${drugNature}</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">剂型：</td>
	    			<td>${dosageForm}</td>
					<td class="honry-lable">药品等级：</td>
					<td>${drugGrad}</td>
				</tr>
	    		<tr>	
	    			<td class="honry-lable">包装单位：</td>
	    			<td>${drugPackUnit}</td>
	    			<td class="honry-lable">包装数量：</td>
	    			<td>${drugInfo.packagingnum}</td>
	    		</tr>
	    		<tr>
					<td class="honry-lable">最小单位：</td>
	    			<td>${drugUnit}</td>
					<td class="honry-lable">基本剂量：</td>
	    			<td>${drugInfo.drugBasicdose}</td>
	    		</tr>
	    		<tr>
	    			<td class="honry-lable">剂量单位：</td>
	    			<td>${drugDoseunit}</td>
					<td class="honry-lable">零售价：</td>
	    			<td>${drugInfo.drugRetailprice}</td>
	    		</tr>
	    		<tr>	
	    			<td class="honry-lable">最高零售价：</td>
	    			<td>${drugInfo.drugMaxretailprice}</td>
					<td class="honry-lable">批发价：</td>
	    			<td>${drugInfo.drugWholesaleprice}</td>
	    		</tr>
	    		<tr>	
	    			<td class="honry-lable">购入价：</td>
	    			<td>${drugInfo.drugPurchaseprice}</td>
					<td class="honry-lable">价格形式：</td>
	    			<td>${drugPriceForm}</td>
	    		</tr>
	    		<tr>	
					<td class="honry-lable">生产厂家：</td>
	    			<td>${manufacturer}</td>
	    			<td class="honry-lable" >抗菌药限制特性：</td>
	    			<td>
	    			<c:if test="${drugInfo.drugRestrictionofantibiotic eq '1'}">非抗菌药</c:if>
	    			<c:if test="${drugInfo.drugRestrictionofantibiotic eq '2'}">无限制</c:if>
	    			<c:if test="${drugInfo.drugRestrictionofantibiotic eq '3'}">职级限制</c:if>
	    			<c:if test="${drugInfo.drugRestrictionofantibiotic eq '4'}">特殊管理</c:if>
	    			</td>
	    		</tr>
	    		<tr>
    				<td class="honry-lable">拆分属性：</td>
	    			<td>
	    			<c:if test="${drugInfo.selfFlg eq '1'}">门诊可拆分包装单位</c:if>
	    			<c:if test="${drugInfo.selfFlg eq '2'}">门诊不可拆分包装单位</c:if>
	    			</td>
	    			<td class="honry-lable">药品编码：</td>
	    			<td>${drugInfo.code}</td>
	    		</tr>
	    		<tr>
	    		    <td class="honry-lable" >大输液标志：</td>
	    			<td>${infusionFlg}</td>
					<td class="honry-lable" >是否自费：</td>
	    			<td>
	    			<c:if test="${drugInfo.selfFlg eq '0'}">公费</c:if>
	    			<c:if test="${drugInfo.selfFlg eq '1'}">自费</c:if>
	    			<c:if test="${drugInfo.selfFlg eq '2'}">部分负担</c:if>
	    			</td>
				</tr>
			
			<tr>
			<td class="honry-lable" >敏试：</td>
    		<td>
    		<c:if test="${drugInfo.drugIstestsensitivity eq '0'}">不需要皮试</c:if>
    		<c:if test="${drugInfo.drugIstestsensitivity eq '1'}">青霉素皮试</c:if>
    		<c:if test="${drugInfo.drugIstestsensitivity eq '2'}">原药皮试</c:if>
    		</td>
			<td class="honry-lable" >产自：</td>
    		<td>
    		<c:if test="${drugInfo.drugIsmanufacture eq '0'}">未知</c:if>
    		<c:if test="${drugInfo.drugIsmanufacture eq '1'}">国产</c:if>
    		<c:if test="${drugInfo.drugIsmanufacture eq '2'}">进口</c:if>
    		<c:if test="${drugInfo.drugIsmanufacture eq '3'}">自制</c:if>
    		<c:if test="${drugInfo.drugIsmanufacture eq '4'}">合资</c:if>
    		</td>
			
			</tr>
    		<tr>					
				<td colspan="1" class="honry-lable">
				创建时间：
				</td>
				<td colspan="3" class="honry-lable">
				${drugInfo.createTime}
				</td>
			</tr>
	    		<tr>					
					<td colspan="4">
					是否新药：&nbsp;
		    			<input type="hidden"  id="drugIsnew"name="drugIsnew" value="${drugInfo.drugIsnew }"/>
		    			<input type="checkBox" id="drugIsnewch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否GMP：&nbsp;
		    			<input type="hidden" id="drugIsgmp"name="drugIsgmp" value="${drugInfo.drugIsgmp}"/>
		    			<input type="checkBox" id="drugIsgmpch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
		   			  是否市限制：&nbsp;
	    			<input type="hidden" id="drugIscitylimit" name="drugIscitylimit" value="${drugInfo.drugIscitylimit}"/>
	    			<input type="checkBox"  id="drugIscitylimitch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否合作医疗：&nbsp;
		    			<input type="hidden" id="drugIscooperativemedical"name="drugIscooperativemedical" value="${drugInfo.drugIscooperativemedical}"/>
		    			<input type="checkBox" id="drugIscooperativemedicalch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    			终端确认：&nbsp;
		    			<input type="hidden" id="drugIsterminalsubmit"name="drugIsterminalsubmit" value="${drugInfo.drugIsterminalsubmit}"/>
		    			<input type="checkBox" id="drugIsterminalsubmitch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
		    		是否辅材：&nbsp;
	    			<input type="hidden" id="supriceFlgh" name="supriceFlg" value="${drugInfo.supriceFlg}"/>
	    			<input type="checkBox"  id="supriceFlg" onclick="javascript:checkBoxSelect('drugIsterminalsubmit',0,1)" readonly="readonly"/>
		   			贵重标志：&nbsp;
	    			<input type="hidden" id="appendFlgh" name="appendFlg" value="${drugInfo.appendFlg}"/>
	    			<input type="checkBox"  id="appendFlg" onclick="javascript:checkBoxSelect('drugIsterminalsubmit',0,1)" readonly="readonly"/>
	    			
		    		</td>
		   			</tr>
		   			<tr>
		   			<td colspan="4">
		   			是否缺药：&nbsp;
		   			<input type="hidden" id="drugIslack"name="drugIslack" value="${drugInfo.drugIslack}"/>
		   			<input type="checkBox" id="drugIslackch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
					是否OTC：&nbsp;
					<input type="hidden" id="drugIsotc"name="drugIsotc" value="${drugInfo.drugIsotc}"/>
					<input type="checkBox" id="drugIsotcch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
	 				是否协定处方：&nbsp;
	    			<input type="hidden" id="drugIsagreementprescription"name="drugIsagreementprescription" value="${drugInfo.drugIsagreementprescription}"/>
	    			<input type="checkBox" id="drugIsagreementprescriptionch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
					是否大屏幕显示：&nbsp;
			    	<input type="hidden" id="drugIsscreen"name="drugIsscreen" value="${drugInfo.drugIsscreen}"/>
			    	<input type="checkBox" id="drugIsscreench" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否是招标药：&nbsp;
	    			<input type="hidden" id="drugIstender"name="drugIstender" value="${drugInfo.drugIstender}"/>
		    		<input type="checkBox" id="drugIstenderch" onclick="javascript:checkBoxSelect(0,1)" readonly="readonly"/>&nbsp;&nbsp;&nbsp;&nbsp;
		   			</td>
	   			</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			</div>
		</div>
		<div title="使用方法" style="padding:10px">
			<table id="secTable" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%">
			<tr>
				<td class="honry-lable" >使用方法：</td>
    			<td>${drugUseMode}</td>
    			<td class="honry-lable">一次用量：</td>
    			<td>${drugInfo.drugOncedosage}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">频次：</td>
    			<td>${frequence}</td>
    			<td class="honry-lable">存储条件：</td>
    			<td>${drugStroage}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">一级药理：</td>
    			<td>${drugInfo.drugPrimarypharmacology}</td>
    			<td class="honry-lable">二级药理：</td>
    			<td>${drugInfo.drugTwogradepharmacology}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">三级药理：</td>
    			<td>${drugInfo.drugThreegradepharmacology}</td>
    			<td class="honry-lable">入院时间：</td>
    			<td>${drugInfo.drugEntertimeStr}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">供货公司：</td>
    			<td>${supplyCompany}</td>
    			<td class="honry-lable">批文信息：</td>
    			<td>${drugInfo.drugDocument}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">注册商标：</td>
    			<td>${drugInfo.drugTrademark}</td>
    			<td class="honry-lable">产地：</td>
    			<td>${drugInfo.drugPlaceoforigin}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">执行标准：</td>
    			<td>${drugInfo.drugOperativenorm}</td>
    			<td class="honry-lable">库位号：</td>
    			<td>${drugInfo.drugLibno}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">条形码：</td>
    			<td>${drugInfo.drugBarcode}</td>
				<td class="honry-lable">中标公司：</td>
    			<td>${drugInfo.drugWinningcompany}</td>
    		</tr>
    		<tr>	
    			<td class="honry-lable">合同代码：</td>
    			<td>${drugInfo.drugContractcode}</td>
				<td class="honry-lable">中标价：</td>
    			<td>${drugInfo.drugBiddingprice}</td>
    		</tr>
    		<tr>	
    			<td class="honry-lable" ><span>重量</span>：</td>
    			<td>${drugInfo.weight}</td>
				<td class="honry-lable" ><span>重量单位：</span></td>
    			<td>${drugWeight}</td>
    		</tr>
    		<tr>	
    			<td class="honry-lable" ><span>体积</span>：</td>
    			<td>${drugInfo.volum}</td>
				<td class="honry-lable" ><span>体积单位：</span></td>
    			<td>${drugVolum}</td>
    		</tr>
    		<tr>	
    			<td class="honry-lable">起始日期：</td>
    			<td>${drugInfo.drugStartDateStr}</td>
				<td class="honry-lable">终止日期：</td>
    			<td>${drugInfo.drugEndDateStr}</td>
    		</tr>
    		<tr>
    		<td class="honry-lable" ><span">有效期：</span></td>
    		<td>${drugInfo.effMonth}</td>
			<td class="honry-lable" ><span>药品的治疗方向分类：</span></td>
    		<td>${drugclass}</td>
			
    		</tr>
    		<tr>
    			<td class="honry-lable" ><span>有效成分：</span></td>
    			<td>${drugInfo.drugActiveingredient}</td>
    			<td class="honry-lable" style="height: 50px">药品简介：</td>
    			<td>${drugInfo.drugBrev}</td>
			</tr>
				<tr>
    			<td class="honry-lable" style="height: 50px">说明书：</td>
    			<td>${drugInfo.drugInstruction}</td>
    			<td class="honry-lable">注意事项：</td>
    			<td>${drugInfo.drugNotes}</td>
    		</tr>
    		<tr>
				<td class="honry-lable">备注：</td>
    			<td colspan="3" style="height: 50px" >${drugInfo.drugRemark}</td>
			</tr>
		</table> 
		<div style="text-align: center; padding: 5px">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
		
		</div>			
		</div>
	</div>	
	<script type="text/javascript">
	     if ($('#appendFlgh').val() == 1) {
	         	$('#appendFlg').attr("checked", true);
	        }
	        $('#appendFlg').prop('disabled',true); 
	     if ($('#supriceFlgh').val() == 1) {
	         	$('#supriceFlg').attr("checked", true);
	        }
	        $('#supriceFlg').prop('disabled',true); 
	      if ($('#drugIsprovincelimit').val() == 1) {
	         	$('#drugIsprovincelimitch').attr("checked", true);
	        }
	         	$('#drugIsprovincelimitch').prop('disabled',true); 
          if ($('#drugIsnew').val() == 1) {
	         	$('#drugIsnewch').attr("checked", true);
	        }
	         	$('#drugIsnewch').prop('disabled',true); 
	         if ($('#drugIstestsensitivity').val() == 1) {
	         	$('#drugIstestsensitivitych').attr("checked", true);
	        }
	         	$('#drugIstestsensitivitych').prop('disabled',true); 
	         if ($('#drugIsgmp').val() == 1) {
	         	$('#drugIsgmpch').attr("checked", true);
	        }
	         	$('#drugIsgmpch').prop('disabled',true); 
	         if ($('#drugIslack').val() == 1) {
	         	$('#drugIslackch').attr("checked", true);
	        }
	         	$('#drugIslackch').prop('disabled',true); 
	         if ($('#drugIsterminalsubmit').val() == 1) {
	         	$('#drugIsterminalsubmitch').attr("checked", true);
	        }
	         	$('#drugIsterminalsubmitch').prop('disabled',true); 
	         if ($('#drugIscitylimit').val() == 1) {
	         	$('#drugIscitylimitch').attr("checked", true);
	        }
	         	$('#drugIscitylimitch').prop('disabled',true); 
	         if ($('#drugIsownexpense').val() == 1) {
	         	$('#drugIsownexpensech').attr("checked", true);
	        }
	         	$('#drugIsownexpensech').prop('disabled',true); 
	         if ($('#drugIsscreen').val() == 1) {
	         	$('#drugIsscreench').attr("checked", true);
	        }
	         	$('#drugIsscreench').prop('disabled',true); 
	        
	         if ($('#drugIsotc').val() == 1) {
	         	$('#drugIsotcch').attr("checked", true);
	        }
	         	$('#drugIsotcch').prop('disabled',true); 
	        
	         if ($('#drugIsagreementprescription').val() == 1) {
	         	$('#drugIsagreementprescriptionch').attr("checked", true);
	        }
	         	$('#drugIsagreementprescriptionch').prop('disabled',true); 
	        
	        if ($('#drugIscooperativemedical').val() == 1) {
	         	$('#drugIscooperativemedicalch').attr("checked", true);
	        }
	         	$('#drugIscooperativemedicalch').prop('disabled',true); 
	        if ($('#drugIstender').val() == 1) {
	         	$('#drugIstenderch').attr("checked", true);
	        }
	         	$('#drugIstenderch').prop('disabled',true); 
	        function closeDialogDrugsViewDiv(){
	        	$('#addDrugsView').dialog('close');
	        }
	        
	        
</script>	
</body>
</html>