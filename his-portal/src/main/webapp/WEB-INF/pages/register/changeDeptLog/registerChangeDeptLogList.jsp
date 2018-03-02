<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
//根据就诊卡号或门诊号查询患者信息以及挂号记录
var deptMap="";//部门
var gradeMap="";//级别
var empMap="";//医生
var contMap="";//合同单位
var typeMap="";//挂号类别
var certiMap = "";//证件类别
var sexMap=new Map();
$(function(){
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	$.ajax({
		url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=certificate'/>",
		type:'post',
		success: function(certidata) {
			certiMap =  certidata ;
		}
	});
	
	bindEnterEvent('idcardNo',searchFrom,'easyui');
	bindEnterEvent('no',searchFrom,'easyui');
});

function searchFrom(){
	var idcardNo =	$('#idcardNo').textbox('getValue');
	var no = $('#no').textbox('getValue');
	if(idcardNo==""&&no==""){
		$.messager.alert("操作提示", "请输入就诊卡号或门诊号");
		return false;
	}
	var state = null;//状态输入
	if(idcardNo!=""&&no==""){
		state = "1";//输入就诊卡，没有输入门诊号
	}else if(idcardNo==""&&no!=""){
		state = "2";//输入门诊号但没输入就诊卡号
	}else if(idcardNo!=""&&no!=""){
		state = "3";//都输入
	}
	//给患者信息赋值
	$.ajax({
		url: "<c:url value='/outpatient/changeDeptLog/findPatientList.action'/>?idcardNo="+idcardNo+"&no="+no,
		type:'post',
		success: function(data){
			var dataObj = eval("("+data+")");
			if(dataObj.resMsg=="error"){
				$.messager.alert("操作提示", dataObj.resCode);
			}else if(dataObj.resMsg=="success"){
				$('#name').text(dataObj.infoPatientList.patientId.patientName);
				//得到患者的出生年月日 age
				var age = dataObj.infoPatientList.patientId.patientBirthday;
				//new一个当前时间
				var aDate = new Date();   
				//获取当前年份（4位）
	  			var thisYear = aDate.getFullYear();
	  			//截取（患者的出生日期的年份）
				ageYear=age.substr(0,4);
				//计算（年龄）
				ages=(thisYear-ageYear);
				//判断是否未满1岁
				if(ages==0){
					//未满一岁的儿童算出月份或天数
					//获取当前月份
					var thisMonth = aDate.getMonth()+1; 
					//获取当前几号
					var thisDate = aDate.getDate();
					thisMonths = parseInt(thisMonth);
					thisDates = parseInt(thisDate);
					//计算当前未满一年工多少天
					var	months = (thisMonths+1)*30+thisDates;
					//截取患者月份
					ageMonth = age.substr(5,2);
					ageMonths = parseInt(ageMonth);
					//截取患者date
					ageDate = age.substr(8,2);
					ageDates = parseInt(ageDate);
					//计算患者患者未满一年一共多少天
					var monthss = (ageMonths+1)*30+ageDates;
					var dates = months-monthss;
					dates = parseInt(dates);
					//判断是否满月
					if(dates/30<1){
						$('#age').text(dates+'天');
						
					}else{
						var monthAge = dates%30;
						$('#age').text(monthAge+'月');
					}
				}else if(ages!=0){
					$('#age').text(ages+'岁');
				}
				$('#sex').text(sexMap.get(dataObj.infoPatientList.patientId.patientSex));
				$('#phone').text(dataObj.infoPatientList.patientId.patientPhone);
				$('#address').text(dataObj.infoPatientList.patientId.patientAddress);
				$('#certificatesNo').text(dataObj.infoPatientList.patientId.patientCertificatesno);
				$('#midicalrecordId').text(dataObj.infoPatientList.patientId.medicalrecordId);
				$('#certificatesType').text(certiFamater(dataObj.infoPatientList.patientId.patientCertificatestype));
			}
		}
	});	
	$.ajax({
		url: "<c:url value='/outpatient/changeDeptLog/querydeptComboboxs.action'/>",
		type:'post',
		success: function(deptData) {
			deptMap = eval("("+deptData+")");
		}
	});	
	$.ajax({
		url: "<c:url value='/outpatient/changeDeptLog/querycontComboboxs.action'/>",
		type:'post',
		success: function(contData) {
			contMap = eval("("+contData+")");
		}
	});
	$.ajax({
		url: "<c:url value='/outpatient/changeDeptLog/querytypeComboboxs.action'/>",
		type:'post',
		success: function(typeData) {
			typeMap = eval("("+typeData+")");
		}
	});
	$.ajax({
		url: "<c:url value='/outpatient/changeDeptLog/querygradeComboboxs.action'/>",
		type:'post',
		success: function(gradeData) {
			gradeMap = eval("("+gradeData+")");
		}
	});	
	$.ajax({
		url: "<c:url value='/outpatient/changeDeptLog/queryempComboboxs.action'/>",
		type:'post',
		success: function(empData) {
			empMap = eval("("+empData+")");
			//加载卡号查询列表
			$('#list').datagrid({
				url: "<c:url value='/outpatient/changeDeptLog/queryRegisterChangeDeptLog.action'/>?menuAlias=${menuAlias}&idcardNo="+idcardNo+"&no="+no+"&state="+state,
				onLoadSuccess :function(data){
				//选择高亮
					if(state==3){
						var rows = $('#list').datagrid('getRows');
						for(var i=0;i<rows.length;i++){
							if(no.trim()==rows[i].no.trim()){
								$('#list').datagrid('selectRow',$('#list').datagrid('getRowIndex',rows[i].id));
							}
						}
					}
				}
			});
			$('#listChange').datagrid({
				url: "<c:url value='/outpatient/changeDeptLog/queryChangeDeptLogList.action'/>?menuAlias=${menuAlias}&idcardNo="+idcardNo+"&no="+no+"&state="+state,
				onLoadSuccess :function(data){
				//选择高亮
					if(state==3){
						var rows = $('#listChange').datagrid('getRows');
						for(var i=0;i<rows.length;i++){
							if(no.trim()==rows[i].no.trim()){
								$('#list').datagrid('selectRow',$('#listChange').datagrid('getRowIndex',rows[i].id));
							}
						}
					}
				}
			});
		}
	});
}

//渲染科室
function functionDept(value,row,index){
	if(value!=null&&value!=''){
		return deptMap[value];
	}
}	
//渲染人员
function functionEmp(value,row,index){
	if(value!=null&&value!=''){
		return empMap[value];
	}
}	
//渲染级别
function functionGrade(value,row,index){
	if(value!=null&&value!=''){
		return gradeMap[value];
	}
}	
//渲染合同单位
function functionConit(value,row,index){
	if(value!=null&&value!=''){
		return contMap[value];
	}
}
//渲染挂号类别	
function functionType(value,row,index){
	if(value!=null&&value!=''){
		return typeMap[value];
	}
}	
//渲染午别
function formatCheckBox(val,row){
	if(val=="1"){
		return '上午';
	}else if(val=="2"){
		return '下午';
	}else if(val=="3"){
		return '晚上';
	}
}
//渲染挂号类别
function certiFamater(value){
	if(value!=null&&value!=''){
		return certiMap[value];
	}
}
//更换科室
function changeDept(){
	 var id = getIdUtil("#list");
	 if(id==null||id==""){
	 	return false;
	 }
	 AddOrShowEast('EditForm',"<c:url value='/outpatient/changeDeptLog/eidtRgisterChangeDeptLog.action'/>?id="+id);
}
//跳转
function AddOrShowEast(title, url) {
	var eastpanel = $('#panelEast'); //获取右侧收缩面板
	if (eastpanel.length > 0) { //判断右侧收缩面板是否存在
		//重新装载右侧面板
		$('#divLayout').layout('panel', 'east').panel({
			href : url
		});
	} else {//打开新面板
		$('#divLayout').layout('add', {
			region : 'east',
			width : 580,
			split : true,
			href : url,
			closable : true
		});
	}
}

</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;">
	<div style="padding:5px 5px 5px 5px;">	 
	    <table style="width:100%;border:1px solid #95b8e7;padding:5px;">
			<tr>
				<td>就诊卡号：
					&nbsp;&nbsp;&nbsp;&nbsp;<input class="easyui-textbox"  id="idcardNo"/>
					&nbsp;&nbsp;&nbsp;&nbsp;门诊号：
					<input class="easyui-textbox" id="no"/>
					<shiro:hasPermission name="${menuAlias}:function:query">
					&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="${menuAlias}:function:edit">
					&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="changeDept()" iconCls="icon-edit">换科</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
			<tr>
	   			<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">姓名：</td>
	   			<td style="width:15%;" id="name"></td>
				<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">性别：</td>
				<td style="width:15%;" id="sex"></td>
				<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">年龄：</td>
	   			<td style="width:15%;" id="age"></td>
	   			<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">联系电话：</td>
	   			<td style="width:15%;" id="phone"></td>
			</tr>
			<tr>	
				<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">证件号：</td>
				<td id="certificatesNo"></td>
				<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">证件类型：</td>
    			<td  id="certificatesType"></td>
				<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">通讯地址：</td>
    			<td id="address"></td>
    			<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">病历号：</td>
    			<td id="midicalrecordId"></td>
   			</tr>
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table class="easyui-datagrid" style="width: 100%" title="挂号信息" id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">   
		    <thead>   
		        <tr> 
		            <th data-options="field:'dept',formatter:functionDept" style="width: 15%">挂号科室</th>   
		            <th data-options="field:'grade',formatter:functionGrade" style="width: 15%">挂号级别</th>   
		            <th data-options="field:'expxrt',formatter:functionEmp" style="width: 15%">挂号专家</th>   
		            <th data-options="field:'midday'" style="width: 15%" formatter="formatCheckBox">挂号午别</th>
		            <th data-options="field:'contractunit',formatter:functionConit" style="width: 15%">合同单位</th>  
		            <th data-options="field:'type',formatter:functionType" style="width: 15%">挂号类别</th>
		            <th data-options="field:'fee'" style="width: 9%">挂号费</th>
		        </tr>   
		    </thead>   
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table class="easyui-datagrid" style="width: 100%" title="换科信息" id="listChange" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">   
		    <thead>   
		        <tr> 
		            <th data-options="field:'oldDept',formatter:functionDept" style="width: 15%">原科室</th>   
		            <th data-options="field:'newDept',formatter:functionDept" style="width: 15%">现科室</th>   
		            <th data-options="field:'oldDoc',formatter:functionEmp" style="width: 15%">原专家</th>   
		            <th data-options="field:'newDoc',formatter:functionEmp" style="width: 15%">现专家</th>
		            <th data-options="field:'reason'" style="width: 20%">换号原因</th>  
		            <th data-options="field:'remark'" style="width: 18%">备注</th>
		        </tr>   
		    </thead>   
		</table>  
	</div>
</div>
<div id="deptdiv"></div>
</body>
</html>