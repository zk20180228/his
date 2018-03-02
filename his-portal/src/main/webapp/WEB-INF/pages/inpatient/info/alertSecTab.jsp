<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
	</head>
	<body>
		<table id="secTable"  class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
   			<tr>
				<td>入院日期:</td>
    			<td><input class="easyui-datebox" id="inDate" readonly name="inDate" value="${inpatientInfo.inDate }" 
    			  style="width:60%"/></td>
				<td>科室代码:</td>
    			<td><input class="easyui-textbox" id="deptCode" readonly name="deptCode" value="${inpatientInfo.deptCode }"  
    			 style="width:60%"  /></td>
   			</tr>
   			<tr>
				<td>结算类别:</td>
    			<td><input id="CodeSettlement" name="paykindCode" readonly value="${inpatientInfo.paykindCode }" onkeydown="KeyDown(0,'CodeSettlement')" 
 style="width:60%" /></td>
				<td>合同单位代码:</td>
    			<td><input class="easyui-textbox" id="pactCode" readonly name="pactCode" value="${inpatientInfo.pactCode }"   style="width:60%" /></td>
   			</tr>
   			<tr>
				<td>病床号:</td>
    			<td><input class="easyui-validatebox" id="bedId" readonly  name="bedId" value="${inpatientInfo.bedId }"   style="width:60%" readonly="readonly" onclick="openBed();"/></td>
				<td>护理单元代码:</td>
    			<td><input class="easyui-textbox" id="nurseCellCode" readonly name="nurseCellCode" value="${inpatientInfo.nurseCellCode }"   style="width:60%" /></td>
   			</tr>
   			<tr>					
				<td>医师代码(住院):</td>
    			<td><input class="easyui-textbox" id="houseDocCode" readonly name="houseDocCode" value="${inpatientInfo.houseDocCode }"   style="width:60%" /></td>
				<td>医师代码(主治):</td>
    			<td><input class="easyui-textbox" id="chargeDocCode" readonly name="chargeDocCode" value="${inpatientInfo.chargeDocCode }"   style="width:60%" /></td>
			</tr>
			<tr>					
				<td>医师代码(主任):</td>
    			<td><input class="easyui-textbox" id="chiefDocCode" name="chiefDocCode" value="${inpatientInfo.chiefDocCode }"   style="width:60%" /></td>
				<td>护士代码（责任）:</td>
    			<td><input class="easyui-textbox" id="dutyNurseCode" name="dutyNurseCode" value="${inpatientInfo.dutyNurseCode }"   style="width:60%" /></td>
			</tr>
			<tr>					
				<td>入院情况:</td>
    			<td><input class="easyui-textbox" id="inCircs" readonly name="inCircs" value="${inpatientInfo.inCircs }"   style="width:60%"  /></td>
				<td>诊断名称:</td>
    			<td><input class="easyui-textbox" id="diagName" readonly name="diagName" value="${inpatientInfo.diagName }"   style="width:60%"  /></td>
   			</tr>
   			<tr>
				<td>入院途径:</td>
    			<td><input class="easyui-textbox" id="inAvenue" readonly name="inAvenue" value="${inpatientInfo.inAvenue }"   style="width:60%" /></td>
				<td>入院来源:</td>
    			<td><input  id="CodeSourse" name="inSource" readonly value="${inpatientInfo.inSource }" onkeydown="KeyDown(0,'CodeSourse')"   style="width:60%" /></td>
   			</tr>
   			<tr>
				<td>住院次数:</td>
    			<td><input class="easyui-textbox" id="inTimes" readonly name="inTimes" value="${inpatientInfo.inTimes }"   style="width:60%" /></td>
				<td>婴儿标志:</td>
    			<td><input class="easyui-combobox" id="stopAcount" readonly name="stopAcount" value="${inpatientInfo.stopAcount }" data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]" style="width:60%"  /></td>
   			</tr>
   			<tr>					
				<td>病案状态:</td>
    			<td><input class="easyui-combobox" id="babyFlag" readonly  name="babyFlag" value="${inpatientInfo.babyFlag }" data-options="valueField: 'value',textField: 'label',data: [{label: '无需病案',value: '0'},{label: '需要病案',value: '1'},{label: '医生站形成病案',value: '2'},{label: '病案室形成病案',value: '3'},{label: '病案封存',value: '4'}]" style="width:60%" /></td>
				<td>住院登记:</td>
    			<td><input class="easyui-combobox" id="inState" readonly name="inState" value="${inpatientInfo.inState }" data-options="valueField: 'value',textField: 'label',data: [{label: '住院登记',value: 'R'},{label: '病房接诊',value: 'I'},{label: '出院登记',value: 'B'},{label: '出院结算',value: 'O'},{label: '预约出院',value: 'P'},{label: '无费退院',value: 'N'}]" style="width:60%" /></td>
			</tr>
			<tr>					
				<td>是否请假:</td>
    			<td><input class="easyui-combobox" id="leaveFlag" readonly name="leaveFlag" value="${inpatientInfo.leaveFlag }" data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]" style="width:60%" /></td>
				<td>出院日期（预约）:</td>
    			<td><input class="easyui-datebox" id="prepayOutdate" readonly readonly name="prepayOutdate" value="${inpatientInfo.prepayOutdate }"   style="width:60%" /></td>
			</tr>
			<tr>					
				<td>出院日期:</td>
    			<td><input class="easyui-datebox" id="outDate"  readonly name="outDate" readonly value="${inpatientInfo.outDate}"   style="width:60%"  /></td>
				<td>转归代号:</td>
    			<td><input class="easyui-textbox" id="zg" name="zg" readonly value="${inpatientInfo.zg }"   style="width:60%"  /></td>
   			</tr>
   			<tr>
				<td>开据医师:</td>
    			<td><input class="easyui-textbox" id="emplCode" name="emplCode" readonly value="${inpatientInfo.emplCode }"   style="width:60%"/></td>
				<td>是否在ICU:</td>
    			<td><input class="easyui-combobox" id="inIcu" name="inIcu" readonly value="${inpatientInfo.inIcu }" data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]" style="width:60%"/></td>
   			</tr>
   			<tr>
				<td>病案是否送入病案室:</td>
    			<td><input class="easyui-combobox" id="casesendFlag" readonly name="casesendFlag" value="${inpatientInfo.casesendFlag }" data-options="valueField: 'value',textField: 'label',data: [{label: '是',value: '1'},{label: '否',value: '0'}]" style="width:60%" /></td>
				<td>护理级别:</td>
    			<td><input  id="CodeNurselevel" name="tend" readonly value="${inpatientInfo.tend}" onkeydown="KeyDown(0,'CodeNurselevel')"   style="width:60%"  /></td>
   			</tr>
   			<tr>					
				<td>病危:</td>
    			<td><input class="easyui-combobox" id="criticalFlag" readonly name="criticalFlag" value="${inpatientInfo.criticalFlag }" data-options="valueField: 'value',textField: 'label',data: [{label: '普通',value: '0'},{label: '病重',value: '1'},{label: '病危',value: '2'}]" style="width:60%"/></td>
				<td>备注:</td>
    			<td><input class="easyui-textbox" id="remark" name="remark" value="${inpatientInfo.remark }" data-options="multiline:true" style="width:60%;height:60px;" /></td>
			</tr>
   	</table>
	<div style="text-align: center; padding: 5px">
		<c:if test="${empty inpatientInfo.id}">
			<a href="javascript:submit(1);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">连续添加</a>
		</c:if>
		<a id="hospitalOKbtn" href="javascript:submit(0)" data-options="iconCls:'icon-save'"  class="easyui-linkbutton">确定</a>
		<a id="hospitalClearbtn" href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clear()" class="easyui-linkbutton">清空</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" id="closetable" data-options="iconCls:'icon-cancel'" onclick="closeDialog()">关闭</a>
	</div>
	
	<script type="text/javascript">
	$(function(){
		$("#closetable").linkbutton({
			onClick:function(){
				$("#secTable").table('close');
			}
		})
		
	})
	</script>
	</body>
</html>
