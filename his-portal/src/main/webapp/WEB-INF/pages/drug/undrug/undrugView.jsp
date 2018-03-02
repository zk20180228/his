<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">   
      
    .honry-table td {   
        width:220px 
    }   
</style>
<body>
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width: 100%;">
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>项目名称：</span>
					</td>
					<td class="honry-view"  style="width:220px">
						${drugUndrug.name }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>自定义码：</span>
					</td>
					<td class="honry-view"  style="width:220px">
						${drugUndrug.undrugInputcode }&nbsp;
					</td>
				</tr>
				 <tr>
					<td class="honry-lable" style="width:220px">
						<span>拼音码：</span>
					</td>
					<td class="honry-view"  style="width:220px;">
						${drugUndrug.undrugPinyin }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>五笔码：</span>
					</td>
					<td class="honry-view"  style="width:220px;">
						${drugUndrug.undrugWb }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>国家编码：</span>
					</td>
					<td>
						${drugUndrug.undrugGbcode }&nbsp;
					</td >
					<td class="honry-lable" style="width:220px">
						<span>国际编码：</span>
					</td>
					<td>
						${drugUndrug.undrugGjcode }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>系统类别：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${sysType }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>状态：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:if test="${drugUndrug.undrugState eq '1'}">在用</c:if>
						<c:if test="${drugUndrug.undrugState eq '2'}">停用</c:if>
						<c:if test="${drugUndrug.undrugState eq '3'}">废弃</c:if>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>执行科室：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${deptName }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>项目约束：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugItemlimit }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>注意事项：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugNotes }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>项目范围：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugScope }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>检查要求：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugRequirements }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>最小费用：</span>
					</td>
					<td class="honry-view" style="width:220px"id="CodeDrugminimumcost">
						${drugMinCost }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>检查部位或标本：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${undrugInspectionsite }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>病史检查：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugMedicalhistory }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>默认价：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.defaultprice }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>儿童价：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.childrenprice }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>特诊价：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.specialprice }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>特诊比例：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugEmergencycaserate }&nbsp;
					</td>
				</tr>

				<tr>
					<td class="honry-lable" style="width:220px">
						<span>其他价1：</span>
					</td>
					<td>
						${drugUndrug.undrugOtherpricei }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>其他价2：</span>
					</td>
					<td style="width:220px">
						${drugUndrug.undrugOtherpriceii }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>单位：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.unit }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>规格：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.spec }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>申请单位名称：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugApplication }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>疾病分类：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${undrugDiseaseclassification }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>手术编码：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugOperationcode }&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>手术分类：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugOperationtype }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>专科名称：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugSpecialtyname }&nbsp;
					</td>
					<td class="honry-lable style="width:220px"">
						<span>设备编号：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugEquipmentno }&nbsp;
					</td>
					</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>手术规模：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugOperationscale }&nbsp;
					</td>
				
					<td class="honry-lable" style="width:220px">
						<span>有效范围：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugValidityrange eq '0'}">
							全部
						</c:when>
						<c:when test="${drugUndrug.undrugValidityrange eq '1'}">
							门诊
						</c:when>
						<c:otherwise>
							住院
						</c:otherwise>
						</c:choose>
					</td>
					</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>是否省限制：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsprovincelimit eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>是否市限制：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIscitylimit eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					</tr>
				    <tr>
					<td class="honry-lable" style="width:220px">
						<span>是否自费：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsownexpense eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>是否确认：</span>
					</td>
					<td style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIssubmit eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				 <tr>
					<td class="honry-lable" style="width:220px">
						<span>是否需要预约：</span>
					</td>
					<td style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIspreorder eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>是否对照：</span>
					</td>
					<td style="width:220px" class="honry-view">
						<c:choose>
						<c:when test="${drugUndrug.undrugCrontrast eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>是否组套：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsstack eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>是否特定项目：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsspecificitems eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>是否知情同意书：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsinformedconsent eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>是否计划生育：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsbirthcontrol eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>是否甲类：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsa eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>是否乙类：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsb eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>是否丙类：</span>
					</td>
					<td class="honry-view" style="width:220px">
						<c:choose>
						<c:when test="${drugUndrug.undrugIsc eq '1'}">
							是
						</c:when>
						<c:otherwise>
							否
						</c:otherwise>
						</c:choose>
					</td>
					<td class="honry-lable" style="width:220px">
						<span>备注：</span>
					</td>
					<td class="honry-view" style="width:220px">
						${drugUndrug.undrugRemark }&nbsp;
					</td>
				</tr>
				<tr>
					<td class="honry-lable" style="width:220px">
						<span>非药品编码:</span>
					</td>
						<td class="honry-view" style="width:220px">
						${drugUndrug.code}&nbsp;
					</td>
					<td class="honry-lable" style="width:220px">
						<span>样本类型：</span>
					</td>
					<td class="honry-view" style="width:220px" >
						${laboratorysample }&nbsp;
					</td>
				</tr>
				<tr>					
					<td colspan="1" class="honry-lable">
						创建时间：
					</td>
					<td class="honry-view" colspan="3">
						${drugUndrug.createTime}
					</td>
				</tr>
			</table>
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
			</div>
			<script type="text/javascript">
				
			</script>
		</body>
</html>