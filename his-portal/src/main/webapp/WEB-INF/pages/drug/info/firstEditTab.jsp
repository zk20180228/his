<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<table id="friTable" class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%;" >
			<tr >
				<td style="font-size: 14 " class="honry-lable" style="width: 20px;">名称：</td>
    			<td style="font-size: 14 "><input class="easyui-textbox" id="name" name="name" value="${drugInfo.name}" data-options="required:true"  missingMessage="请输入名称" /></td>
    			<td style="font-size: 14 "class="honry-lable" style="width: 20px;">名称自定义：</td>
    			<td style="font-size: 14 "><input class="easyui-textbox" id="drugNameinputcode" name="drugNameinputcode" value="${drugInfo.drugNameinputcode}"  /></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 " class="honry-lable" >通用名称：</td>
    			<td style="font-size: 14 "  ><input class="easyui-textbox" id="drugCommonname" name="drugCommonname" value="${drugInfo.drugCommonname}" data-options="required:true"   missingMessage="请输入通用名称"/></td>
    			<td style="font-size: 14 "  class="honry-lable" >通用名称自定义码：</td>
    			<td style="font-size: 14 "  ><input class="easyui-textbox" id="drugCnameinputcode" name="drugCnameinputcode" value="${drugInfo.drugCnameinputcode}"   /></td>
    		</tr>
    		<tr>
				<td  style="font-size: 14 " class="honry-lable" >基本药物1：</td>
    			<td  style="font-size: 14 " ><input class="easyui-textbox" id="duugBasici" name="duugBasici" value="${drugInfo.duugBasici}"    /></td>
    			<td style="font-size: 14 "  class="honry-lable" >基本药物2：</td>
    			<td style="font-size: 14 "  ><input class="easyui-textbox" id="duugBasicii" name="duugBasicii" value="${drugInfo.duugBasicii}"     /></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >基本药物自定义码：</td>
    			<td  style="font-size: 14 " ><input class="easyui-textbox" id="drugBasicinputcode" name="drugBasicinputcode" value="${drugInfo.drugBasicinputcode}"   /></td>
    			<td style="font-size: 14 "  class="honry-lable" >招标识别码：</td>
    			<td  style="font-size: 14 " ><input class="easyui-textbox" id="drugBiddingcode" name="drugBiddingcode" value="${drugInfo.drugBiddingcode}"   /></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >英文品名：</td>
    			<td style="font-size: 14 "  ><input class="easyui-textbox" id="drugEnname" name="drugEnname" value="${drugInfo.drugEnname}"   /></td>
    			<td style="font-size: 14 "  class="honry-lable" >英文别名：</td>
    			<td style="font-size: 14 "  ><input class="easyui-textbox" id="drugEnalias" name="drugEnalias" value="${drugInfo.drugEnalias}"   /></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >英文通用名：</td>
    			<td style="font-size: 14 "  ><input class="easyui-textbox" id="drugEncommonname" name="drugEncommonname" value="${drugInfo.drugEncommonname}"   /></td>
    			<td style="font-size: 14 "  class="honry-lable" >国家编码：</td>
    			<td  style="font-size: 14 " ><input class="easyui-textbox" id="drugGbcode" name="drugGbcode" value="${drugInfo.drugGbcode}"   /></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >库位编码：</td>
    			<td  style="font-size: 14 "   ><input class="easyui-textbox" id="drugLibid" name="drugLibid" value="${drugInfo.drugLibid}"   /></td>
    			<td style="font-size: 14 "  class="honry-lable" >规格：</td>
    			<td style="font-size: 14 "   ><input class="easyui-textbox" id="spec" name="spec" value="${drugInfo.spec}"    data-options="required:true"  missingMessage="请输入规格"/></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "   class="honry-lable" >药品类别：</td>
    			<td style="font-size: 14 "  >
    				<input id="CodeDrugtype"  name="drugType" value="${drugInfo.drugType}" onkeydown="KeyDown(0,'CodeDrugtype')"   data-options="required:true"  missingMessage="请输入类别"/>
    			</td>
    			<td style="font-size: 14 "  class="honry-lable" >系统类别：</td>
    			<td  style="font-size: 14 " >
    				<input  id="CodeSystemtype" name="drugSystype" onkeydown="KeyDown(0,'CodeSystemtype')" value="${drugInfo.drugSystype}"    data-options="required:true"  missingMessage="请输入系统类别"/>
    				<a href="javascript:delSelectedData('CodeSystemtype');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
    			</td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >最小费用：</td>
    			<td style="font-size: 14 "  >
    				<input id="CodeDrugminimumcost" name="drugMinimumcost" onkeydown="KeyDown(0,'CodeDrugminimumcost')"value="${drugInfo.drugMinimumcost}"    data-options="required:true"  missingMessage="请输入最小费用"/>
    				<a href="javascript:delSelectedData('CodeDrugminimumcost');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
    			</td>
    			<td style="font-size: 14 "  class="honry-lable" >药品性质：</td>
    			<td  style="font-size: 14 " >
    				<input id="CodeDrugproperties" name="drugNature" onkeydown="KeyDown(0,'CodeDrugproperties')" value="${drugInfo.drugNature}"   data-options="required:true"  missingMessage="请输入药品性质"/>
    			</td>
    		</tr>
    		<tr>
    			<td style="font-size: 14 "  class="honry-lable" >剂型：</td>
    			<td style="font-size: 14 "  >
    				<input id="CodeDosageform" name="drugDosageform" onkeydown="KeyDown(0,'CodeDosageform')" value="${drugInfo.drugDosageform}"    data-options="required:true"  missingMessage="请输入剂型"/>
    			</td>
				<td  style="font-size: 14 " class="honry-lable" >药品等级：</td>
				<td  style="font-size: 14 " >
					<input  id="CodeDruggrade" name="drugGrade" onkeydown="KeyDown(0,'CodeDruggrade')" value="${drugInfo.drugGrade}"   />
				</td>
			</tr>
    		<tr>	
    			<td style="font-size: 14 "  class="honry-lable" >包装单位：</td>
    			<td style="font-size: 14 "  ><input id="CodeDrugpackagingunit" name="drugPackagingunit" onkeydown="KeyDown(0,'CodeDrugpackagingunit')" value="${drugInfo.drugPackagingunit}" data-options="required:true"  missingMessage="请输入包装单位"/>
    			<a href="javascript:delSelectedData('CodeDrugpackagingunit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
    			</td>
    			<td style="font-size: 14 "  class="honry-lable" >包装数量：</td>
    			<td style="font-size: 14 "  ><input class="easyui-numberbox" id="packagingnum" name="packagingnum" value="${drugInfo.packagingnum}" data-options="required:true"  missingMessage="请输入包装数量"/></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >最小单位：</td>
    			<td style="font-size: 14 "  ><input  id="CodeMinimumunit" name="unit" onkeydown="KeyDown(0,'CodeMinimumunit')" value="${drugInfo.unit}"  data-options="required:true"  missingMessage="请输入最小单位"/>
    										<a href="javascript:delSelectedData('CodeMinimumunit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
    			</td>
				<td style="font-size: 14 "  class="honry-lable" >基本剂量：</td>
    			<td  style="font-size: 14 " ><input class="easyui-numberbox" id="drugBasicdose" name="drugBasicdose" value="${drugInfo.drugBasicdose}" data-options="required:true,precision:3"   missingMessage="请输入基本剂量"/>
    			</td>
    		</tr>
    		<tr>
    			<td style="font-size: 14 "  class="honry-lable" >剂量单位：</td>
    			<td  style="font-size: 14 " ><input  id="drugDoseunitTmp" name="drugDoseunit"  value="${drugInfo.drugDoseunit}"  data-options="required:true"   missingMessage="请输入剂量单位" />
    			<a href="javascript:delSelectedData('drugDoseunit');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
    			</td>
				<td style="font-size: 14 "  class="honry-lable" >零售价：</td>
    			<td style="font-size: 14 "  >
    				<c:if test="${drugInfo.id !=null}">
    					<input class="easyui-numberbox" id="drugRetailprice" name="drugRetailprice" value="${drugInfo.drugRetailprice}"  readonly="readonly" data-options="required:true,min:0,precision:4"  missingMessage="请输入零售价"/>
    				</c:if>
    				<c:if test="${drugInfo.id ==null}">
    					<input class="easyui-numberbox" id="drugRetailprice" name="drugRetailprice" value="${drugInfo.drugRetailprice}"  data-options="required:true,min:0,precision:4"  missingMessage="请输入零售价" />
    				</c:if>
    			</td>
    		</tr>
    		<tr>	
    			<td style="font-size: 14 "  class="honry-lable" >最高零售价：</td>
    			<td style="font-size: 14 "  >
	    			<c:if test="${drugInfo.id !=null}">
	    				<input class="easyui-numberbox" id="drugMaxretailprice" name="drugMaxretailprice" value="${drugInfo.drugMaxretailprice}"  data-options="min:0,precision:4" readonly="readonly"/>
	    			</c:if>
    				<c:if test="${drugInfo.id ==null}">
    					<input class="easyui-numberbox" id="drugMaxretailprice" name="drugMaxretailprice" value="${drugInfo.drugMaxretailprice}"  data-options="min:0,precision:4" />
    				</c:if>
    			</td>
				<td  style="font-size: 14 " class="honry-lable" >批发价：</td>
    			<td style="font-size: 14 "  >
    				<c:if test="${drugInfo.id !=null}">
    					<input class="easyui-numberbox" id="drugWholesaleprice" name="drugWholesaleprice" value="${drugInfo.drugWholesaleprice}"  data-options="required:true,min:0,precision:4"  readonly="readonly"/>
    				</c:if>
    				<c:if test="${drugInfo.id ==null}">
    					<input class="easyui-numberbox" id="drugWholesaleprice" name="drugWholesaleprice" value="${drugInfo.drugWholesaleprice}" data-options="required:true,min:0,precision:4"  />
    				</c:if>
    			</td>
    		</tr>
    		<tr>	
    			<td  style="font-size: 14 " class="honry-lable" >购入价：</td>
    			<td  style="font-size: 14 " >
    				<c:if test="${drugInfo.id !=null}">
    					<input class="easyui-numberbox" id="drugPurchaseprice" name="drugPurchaseprice" value="${drugInfo.drugPurchaseprice}"  data-options="required:true,min:0,precision:4"  readonly="readonly"/></td>
    				</c:if>
    				<c:if test="${drugInfo.id ==null}">
    					<input class="easyui-numberbox" id="drugPurchaseprice" name="drugPurchaseprice" value="${drugInfo.drugPurchaseprice}"  data-options="required:true,min:0,precision:4" /></td>
    				</c:if>
				<td style="font-size: 14 "  class="honry-lable" >价格形式：</td>
    			<td  style="font-size: 14 " ><input  id="CodeDrugpricetype" name="drugPricetype" onkeydown="KeyDown(0,'CodeDrugpricetype')" value="${drugInfo.drugPricetype}"   /></td>
    		</tr>
    		<tr>
   				<td  style="font-size: 14 " class="honry-lable" >生产厂家：</td>
    			<td  style="font-size: 14 " ><input id="drugManufacturer" name="drugManufacturer" value="${drugInfo.drugManufacturer}"   data-options="required:true"  missingMessage="请选择生产厂商"/>
    			<a href="javascript:delSelectedData('drugManufacturer');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
    			</td>
    			<td style="font-size: 14 "  class="honry-lable" >抗菌药限制特性：</td>
    			<td style="font-size: 14 " ><input class="easyui-combobox"  id="drugRestrictionofantibiotic" name="drugRestrictionofantibiotic" value="${drugInfo.drugRestrictionofantibiotic}"data-options="valueField:'id',textField:'text',data:drugRestrictionofantibiotic"   /></td>
    		</tr>
    		<tr>
				<td style="font-size: 14 "  class="honry-lable" >拆分属性：</td>
    			<td style="font-size: 14 " ><input class="easyui-combobox" id="drugSplitattr" name="drugSplitattr" value="${drugInfo.drugSplitattr}" data-options="valueField:'id',textField:'text',data:split,required:true" missingMessage="请输入拆分属性" /></td>
				<td style="font-size: 14 "  class="honry-lable" >是否停用：</td>
    			<td style="font-size: 14 " ><input class="easyui-combobox"  id="stopFlg" name="stop_flg" data-options="valueField:'id',textField:'text',data:yesNo" value="${drugInfo.stop_flg}"   /></td>
			</tr>
    		<tr>
    		    <td style="font-size: 14 "  class="honry-lable" >大输液标志：</td>
    			<td style="font-size: 14 "  ><input  id="CodeDruginfusion" name="infusionFlg" onkeydown="KeyDown(0,'CodeinfusionFlg')" value="${drugInfo.infusionFlg}"  data-options="required:true"  /></td>
				<td style="font-size: 14 "  class="honry-lable" >是否自费：</td>
    			<td style="font-size: 14 " ><input class="easyui-combobox"  id="selfFlg" name="selfFlg" onclick="javascript:checkboxexpense()"value="${drugInfo.selfFlg}"data-options="valueField:'id',textField:'text',data:selfFlg"   /></td>
			</tr>
			<tr>
			<td style="font-size: 14 "  class="honry-lable" >敏试：</td>
    		<td style="font-size: 14 " ><input class="easyui-combobox"  id="drugIstestsensitivity" name="drugIstestsensitivity" value="${drugInfo.drugIstestsensitivity}"data-options="valueField:'id',textField:'text',data:drugIstestsensitivity"   /></td>
			
			<td style="font-size: 14 "  class="honry-lable" >产自：</td>
    		<td style="font-size: 14 " ><input class="easyui-combobox"  id="drugIsmanufacture" name="drugIsmanufacture" value="${drugInfo.drugIsmanufacture}"data-options="valueField:'id',textField:'text',data:drugIsmanufacture"   /></td>
			
			</tr>
			<tr>
			<td style="font-size: 14 "  class="honry-lable" >药品编码：</td>
    		<td style="font-size: 14 " colspan="3">
    		<input class="easyui-textbox"  id="code" name="code" value="${drugInfo.code}" data-options="required:true"   missingMessage="推荐格式:以大写字母'Y'开头,长度为2-50位" /></td>
			
			</tr>
    		<tr>
    			<td colspan="4" >
    				是否新药：&nbsp;
    				<input type="hidden" id="drugIsnewh" name="drugIsnew" value="${drugInfo.drugIsnew }"/>
	    			<input type="checkBox"  id="drugIsnew" onclick="javascript:checkBoxSelect('drugIsnew',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否GMP：&nbsp
	    			<input type="hidden" id="drugIsgmph" name="drugIsgmp" value="${drugInfo.drugIsgmp}"/>
	    			<input type="checkBox"  id="drugIsgmp" onclick="javascript:checkBoxSelect('drugIsgmp',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否市限制：&nbsp
	    			<input type="hidden" id="drugIscitylimit" name="drugIscitylimit" value="${drugInfo.drugIscitylimit}"/>
	    			<input type="checkBox"  id="drugIscitylimitch" onclick="javascript:checkboxiscity()"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否合作医疗：&nbsp
	    			<input type="hidden" id="drugIscooperativemedicalh" name="drugIscooperativemedical" value="${drugInfo.drugIscooperativemedical}"/>
	    			<input type="checkBox" id="drugIscooperativemedical" onclick="javascript:checkboxlimit()"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			终端确认：&nbsp
	    			<input type="hidden" id="drugIsterminalsubmith" name="drugIsterminalsubmit" value="${drugInfo.drugIsterminalsubmit}"/>
	    			<input type="checkBox"  id="drugIsterminalsubmit" onclick="javascript:checkBoxSelect('drugIsterminalsubmit',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否辅材：&nbsp
	    			<input type="hidden" id="supriceFlgh" name="supriceFlg" value="${drugInfo.supriceFlg}"/>
	    			<input type="checkBox"  id="supriceFlg" onclick="javascript:checkBoxSelect('supriceFlg',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			贵重标志：&nbsp
	    			<input type="hidden" id="appendFlgh" name="appendFlg" value="${drugInfo.appendFlg}"/>
	    			<input type="checkBox"  id="appendFlg" onclick="javascript:checkBoxSelect('appendFlg',0,1)"/>
	    				
    			</td>
    			</tr>
    			<tr>
    			<td colspan="4">
	    			是否缺药：&nbsp
	    			<input type="hidden" id="drugIslackh" name="drugIslack" value="${drugInfo.drugIslack}"/>
	    			<input type="checkBox"  id="drugIslack" onclick="javascript:checkBoxSelect('drugIslack',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否OTC：&nbsp
	    			<input type="hidden" id="drugIsotch" name="drugIsotc" value="${drugInfo.drugIsotc}"/>
	    			<input type="checkBox"  id="drugIsotc" onclick="javascript:checkBoxSelect('drugIsotc',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否协定处方：&nbsp
    				<input type="hidden" id="drugIsagreementprescriptionh" name="drugIsagreementprescription" value="${drugInfo.drugIsagreementprescription}"/>
	    			<input type="checkBox" id="drugIsagreementprescription" onclick="javascript:checkBoxSelect('drugIsagreementprescription',0,1)"/>
	    			&nbsp;&nbsp;&nbsp;&nbsp;
	    			是否大屏幕显示：&nbsp
	    			<input type="hidden" id="drugIsscreenh" name="drugIsscreen" value="${drugInfo.drugIsscreen}"/>
	    			<input type="checkBox" id="drugIsscreen" onclick="javascript:checkBoxSelect('drugIsscreen',0,1)"/>
    				&nbsp;&nbsp;&nbsp;&nbsp;
    				是否是招标药:&nbsp;
    				<input type="hidden" id="drugIstenderh"name="drugIstender" value="${drugInfo.drugIstender}"/>
	    			<input type="checkBox" id="drugIstender" onclick="javascript:checkBoxSelect('drugIstender',0,1)" />&nbsp;&nbsp;&nbsp;&nbsp;
		    		
    			</td>
   			</tr>
			<tr id="StopReasonHidden">
				<td class="honry-lable" >停用原因：</td>
    			<td colspan="3"><input class="easyui-textbox" id="stopReason" name="stopReason" value="${drugInfo.stopReason}"  data-options="required:true" missingMessage="请输入停用原因"style="width:723px;height: 50px" /></td>
			</tr>
		</table>
		<div style="text-align: center; padding: 5px">
			<c:if test="${empty drugInfo.id}">
				<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
			</c:if>
			<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
		</div>
		<script type="text/javascript">
			var yesNo = [{"id":'1',"text":"是"},{"id":'0',"text":"否"}];
			//拆分属性
			var split = [{"id":1,"text":"门诊可拆分包装单位"},{"id":2,"text":"门诊不可拆分包装单位"}];
			//0公费1自费2-部分负担
			var selfFlg = [{"id":'0',"text":"公费"},{"id":'1',"text":"自费"},{"id":'2',"text":"部分负担"}];
			///**1非抗菌药2无限制3职级限制4特殊管理**/
			var drugRestrictionofantibiotic = [{"id":1,"text":"非抗菌药"},{"id":2,"text":"无限制"},{"id":3,"text":"职级限制"},{"id":4,"text":"特殊管理"}];
			
			///**0不需要皮试1青霉素皮试2原药皮试**/
			var drugIstestsensitivity = [{"id":'0',"text":"不需要皮试"},{"id":'1',"text":"青霉素皮试"},{"id":'2',"text":"原药皮试"}];
			/**1国产2进口3自制4合资**/
			var drugIsmanufacture = [{"id":'0',"text":"未知"},{"id":'1',"text":"国产"},{"id":'2',"text":"进口"},{"id":'3',"text":"自制"},{"id":'4',"text":"合资"}];
			
			
			//验证当中标公司输入框输入时，复选框是否是招标药为选中状态
			function validCheck(){
				if($('#drugWinningcompany').val()!=""){
					$('#drugIstender').attr("checked", true);
				}
			}
			//根据是否停用，显示停用原因
			$("#stopFlg").combobox({
				 onChange:function(value){
			    	if(value == "1"){
			    		$('#stopReason').textbox('enableValidation');
				    	$("#StopReasonHidden").show(); 
				    	isStopNum=isStopNum+1;
			    	}else{
			    		$('#stopReason').textbox('disableValidation');
			    		$("#StopReasonHidden").hide();
			    		isStopNum=isStopNum+1;
			    	}
				  } 
			});
		</script>
	</body>
</html>
