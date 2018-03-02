<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<form id="treeEditForm" method="post" >
		<div title="Tab1" style="padding: 20px;">
			<input type="hidden" id="id" name="id" value="${operationapply.id }">
			<input type="hidden" id="clinicCode" name="clinicCode.id" value="${operationapply.clinicCode.id }">
			<input type="hidden" id="patientNo" name="patientNo" value="${operationapply.patientNo }">
			<input type="hidden" id="pasource" name="pasource" value="${operationapply.pasource }">
			<input type="hidden" id="name" name="name" value="${operationapply.name }">
			<input type="hidden" id="age" name="age" value="${operationapply.age }">
			<input type="hidden" id="sex" name="sex" value="${operationapply.sex }">
			<input type="hidden" id="birthday" name="birthday" value="${operationapply.birthday }">
			<input type="hidden" id="profession" name="profession" value="${operationapply.profession }">
			<input type="hidden" id="address" name="address" value="${operationapply.address }">
			<input type="hidden" id="telephone" name="telephone" value="${operationapply.telephone }">
			<input type="hidden" id="prepayCost" name="prepayCost" value="${operationapply.prepayCost }">
			<input type="hidden" id="inDept" name="inDept.id" value="${operationapply.inDept.id }">
			<input type="hidden" id="wardNo" name="wardNo" value="${operationapply.wardNo }">
			<input type="hidden" id="bedNo" name="bedNo" value="${operationapply.bedNo }">
			<input type="hidden" id="bloodCode" name="bloodCode" value="${operationapply.bloodCode }">
			<input type="hidden" id="diagnose1" name="diagnose1" value="${operationapply.diagnose1 }">
			<input type="hidden" id="diagnose2" name="diagnose2" value="${operationapply.diagnose2 }">
			<input type="hidden" id="diagnose3" name="diagnose3" value="${operationapply.diagnose3 }">
			<input type="hidden" id="opName1" name="opName1" value="${operationapply.opName1 }">
			<input type="hidden" id="opName2" name="opName2" value="${operationapply.opName2 }">
			<input type="hidden" id="opName3" name="opName3" value="${operationapply.opName3 }">
			<input type="hidden" id="opType" name="opType" value="${operationapply.opType }">
			<input type="hidden" id="opRoom" name="opRoom" value="${operationapply.opRoom }">
			<input type="hidden" id="opDoctor" name="opDoctor" value="${operationapply.opDoctor }">
			<input type="hidden" id="opDoctordept" name="opDoctordept" value="${operationapply.opDoctordept }">
			<input type="hidden" id="opAssist1" name="opAssist1" value="${operationapply.opAssist1 }">
			<input type="hidden" id="opAssist2" name="opAssist2" value="${operationapply.opAssist2 }">
			<input type="hidden" id="opAssist3" name="opAssist3" value="${operationapply.opAssist3 }">
			<input type="hidden" id="opTempassist1" name="opTempassist1" value="${operationapply.opTempassist1 }">
			<input type="hidden" id="opTempassist2" name="opTempassist2" value="${operationapply.opTempassist2 }">
			<input type="hidden" id="guiDoctor" name="guiDoctor" value="${operationapply.guiDoctor }">
			<input type="hidden" id="preDate" name="preDate" value="${operationapply.preDate }">
			<input type="hidden" id="duration" name="duration" value="${operationapply.duration }">
			<input type="hidden" id="helperNum" name="helperNum" value="${operationapply.helperNum }">
			<input type="hidden" id="washNurse" name="washNurse" value="${operationapply.washNurse }">
			<input type="hidden" id="accoNurse" name="accoNurse" value="${operationapply.accoNurse }">
			<input type="hidden" id="prepNurse" name="prepNurse" value="${operationapply.prepNurse }">
			<input type="hidden" id="execDept" name="execDept" value="${operationapply.execDept }">
			<input type="hidden" id="isane" name="isane" value="${operationapply.isane }">
			<input type="hidden" id="aneDoctor" name="aneDoctor" value="${operationapply.aneDoctor }">
			<input type="hidden" id="anesType" name="anesType" value="${operationapply.anesType }">
			<input type="hidden" id="aneWay" name="aneWay" value="${operationapply.aneWay }">
			<input type="hidden" id="aneNote" name="aneNote" value="${operationapply.aneNote }">
			<input type="hidden" id="consoleType" name="consoleType" value="${operationapply.consoleType }">
			<input type="hidden" id="applyDoctor" name="applyDoctor" value="${operationapply.applyDoctor }">
			<input type="hidden" id="applyRemark" name="applyRemark" value="${operationapply.applyRemark }">
			
			<input type="hidden" id="apprDoctor2" name="apprDoctor2" value="${operationapply.apprDoctor2 }">
			<input type="hidden" id="apprDate2" name="apprDate2" value="${operationapply.apprDate2 }">
			<input type="hidden" id="apprRemark2" name="apprRemark2" value="${operationapply.apprRemark2 }">
			<input type="hidden" id="apprDoctor3" name="apprDoctor3" value="${operationapply.apprDoctor3 }">
			<input type="hidden" id="apprDate3" name="apprDate3" value="${operationapply.apprDate3 }">
			<input type="hidden" id="apprRemark3" name="apprRemark3" value="${operationapply.apprRemark3 }">
			<input type="hidden" id="degree" name="degree" value="${operationapply.degree }">
			<input type="hidden" id="inciType" name="inciType" value="${operationapply.inciType }">
			<input type="hidden" id="infectType" name="infectType" value="${operationapply.infectType }">
			<input type="hidden" id="isgerm" name="isgerm" value="${operationapply.isgerm }">
			<input type="hidden" id="screenup" name="screenup" value="${operationapply.screenup }">
			<input type="hidden" id="console" name="console" value="${operationapply.console }">
			<input type="hidden" id="receptDate" name="receptDate" value="${operationapply.receptDate}">
			<input type="hidden" id="isagreelook" name="isagreelook" value="${operationapply.isagreelook }">
			<input type="hidden" id="bloodNum" name="bloodNum" value="${operationapply.bloodNum }">
			<input type="hidden" id="bloodUnit" name="bloodUnit" value="${operationapply.bloodUnit }">
			<input type="hidden" id="opsNote" name="opsNote" value="${operationapply.opsNote }">
			<input type="hidden" id="status" name="status" value="${operationapply.status }">
			<input type="hidden" id="isfinished" name="isfinished" value="${operationapply.isfinished }">
			<input type="hidden" id="folk" name="folk" value="${operationapply.folk }">
			<input type="hidden" id="relaCode" name="relaCode" value="${operationapply.relaCode }">
			<input type="hidden" id="folkComment" name="folkComment" value="${operationapply.folkComment }">
			<input type="hidden" id="isurgent" name="isurgent" value="${operationapply.isurgent }">
			<input type="hidden" id="ischange" name="ischange" value="${operationapply.ischange }">
			<input type="hidden" id="isheavy" name="isheavy" value="${operationapply.isheavy }">
			<input type="hidden" id="isspecial" name="isspecial" value="${operationapply.isspecial }">
			<input type="hidden" id="isunite" name="isunite" value="${operationapply.isunite }">
			<input type="hidden" id="uniteOpid" name="uniteOpid" value="${operationapply.uniteOpid }">
			<input type="hidden" id="uniteDisease" name="uniteDisease" value="${operationapply.uniteDisease }">
			<input type="hidden" id="isneedacco" name="isneedacco" value="${operationapply.isneedacco }">
			<input type="hidden" id="isneedprep" name="isneedprep" value="${operationapply.isneedprep }">
			<input type="hidden" id="isneedpathology" name="isneedpathology" value="${operationapply.isneedpathology }">
			<input type="hidden" id="isautoblood" name="isautoblood" value="${operationapply.isautoblood }">
			<input type="hidden" id="dcny" name="dcny" value="${operationapply.dcny }">
			<input type="hidden" id="opertionposition" name="opertionposition" value="${operationapply.opertionposition }">
			<input type="hidden" id="issecondopertion" name="issecondopertion" value="${operationapply.issecondopertion }">
			<input type="hidden" id="isownexpense" name="isownexpense" value="${operationapply.isownexpense }">
			<input type="hidden" id="clinical" name="clinical" value="${operationapply.clinical }">
			<input type="hidden" id="contraindication" name="contraindication" value="${operationapply.contraindication }">
			<input type="hidden" id="indication" name="indication" value="${operationapply.indication }">
			<input type="hidden" id="stitution" name="stitution" value="${operationapply.stitution }">
			<input type="hidden" id="preparation" name="preparation" value="${operationapply.preparation }">
			<input type="hidden" id="complication" name="complication" value="${operationapply.complication }">
			<input type="hidden" id="discussion" name="discussion" value="${operationapply.discussion }">
			<input type="hidden" id="measures" name="measures" value="${operationapply.measures }">
			<input type="hidden" id="isgroupconsultation" name="isgroupconsultation" value="${operationapply.isgroupconsultation }">
			<input type="hidden" id="createUser" name="createUser" value="${operationapply.createUser }">
			<input type="hidden" id="createDept" name="createDept" value="${operationapply.createDept }">
			<input type="hidden" id="createTime" name="createTime" value="${operationapply.createTime }">
			<input type="hidden" id="updateUser" name="updateUser" value="${operationapply.updateUser }">
			<input type="hidden" id="updateTime" name="updateTime" value="${operationapply.updateTime }">
			<input type="hidden" id="stop_flg" name="stop_flg" value="${operationapply.stop_flg }">
			<input type="hidden" id="del_flg" name="del_flg" value="${operationapply.del_flg }">
			<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black">
				<tr align="center">
					<td>审批医生编码:</td>
					<td><input class="easyui-combobox" id="spysbm" name="apprDoctor" value="${operationapply.apprDoctor }" data-options="required:true" style="width:290px" missingMessage="请选择审批医生编码"></td>
				</tr>
				<tr align="center">
	    			<td>审批备注:</td>
	    			<td><input class="easyui-textbox" type="text" id="apprRemark" name="apprRemark" value="${operationapply.apprRemark }" data-options="required:true" style="width:290px" missingMessage="请输入审批备注"/></td>
	    		</tr>
			</table>	
			<div style="text-align: center; padding: 5px">
				<a href="javascript:void(0)" data-options="iconCls:'icon-save'" onclick="submitTreeForm()" class="easyui-linkbutton">确定</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-clear'" onclick="clearTreeForm()" class="easyui-linkbutton">清空</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'" onclick="closeDialog()" class="easyui-linkbutton">关闭</a>
			</div>
		</div>		
	</form>
	<script type="text/javascript">
	//提交验证
	function submitTreeForm(){
		$('#treeEditForm').form('submit', {
			url : 'addOperApprInfo.action',
			data:$('#treeEditForm').serialize(),
	        dataType:'json',
			onSubmit : function() {
				if(!$('#treeEditForm').form('validate')){
					$.messager.show({  
				         title:'提示信息' ,   
				         msg:'验证没有通过,不能提交表单!'  
				    }); 
				    return false ;
				}
			},
			success : function(data) {
				closeDialog();
				refresh();
				$("#list").datagrid("reload");
			},
			error : function(data) {
				$.messager.alert('提示信息','操作失败！');
			}
		}); 
	}
	//清除所填信息
	function clearTreeForm(){
		$('#treeEditForm').form('reset');
	}
	
	//审批医生编码
	$('#spysbm').combobox({
		url : 'baseinfo/employee/employeeCombobox.action',
		valueField : 'id',
		textField : 'name',
		multiple : false,
		editable : false
	});

</script>
</body>
</html>