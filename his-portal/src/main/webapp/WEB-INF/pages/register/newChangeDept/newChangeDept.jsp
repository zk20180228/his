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
var empMap="";//医生
var sexMap=new Map();
$(function(){
	bindEnterEvent('idcardNo',searchFrom,'easyui');
	bindEnterEvent('no',searchFrom,'easyui');
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
		url: "<%=basePath%>outpatient/newChangeDeptLog/querydeptComboboxs.action",
		type:'post',
		success: function(deptData) {
			deptMap = deptData;
		}
	});	
	
	$.ajax({
		url: "<%=basePath%>outpatient/newChangeDeptLog/queryempComboboxs.action",
		type:'post',
		success: function(empData) {
			empMap = empData;
		}
	});
});

function searchFrom(){
	var idcardNo = $('#idcardNo').textbox('getText');
	var no = $('#no').textbox('getText');
	if(idcardNo==""&&no==""){
		$.messager.alert("操作提示", "请输入就诊卡号或门诊号");
		return false;
	}
	queryInfoList(idcardNo,no);
	queryChangeDept(idcardNo,no);
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
var oldAllDoc = "";
function queryInfoList(idcardNo,no){
	$('#list').datagrid({
		url: "<%=basePath%>outpatient/newChangeDeptLog/queryRegisterMain.action",
		queryParams:{"ationInfo.cardNo":idcardNo,"ationInfo.clinicCode":no},
		onLoadSuccess :function(data){
			if(data.total==0){
				$.messager.alert("操作提示","没有该患者信息！");
				$('#name').text("");
				$('#sex').text("");
				$('#age').text("");
				$('#certificatesNo').text("");
				$('#certificatesType').text("");
				$('#phone').text("");
				$('#address').text("");
				$('#midicalrecordId').text("");
			}else if(data.total>0){
				for(var i = 0;i<data.total;i++){
					oldAllDoc += data.rows[i].doctCode+data.rows[i].noonCode+",";
				}
				var rows = $('#list').datagrid('getRows');
				$('#name').text(rows[0].patientName);
				$('#sex').text(sexMap.get(rows[0].patientSex));
				$('#age').text(rows[0].patientAge+rows[0].patientAgeunit);
				$('#certificatesNo').text(rows[0].patientIdenno);
				$('#certificatesType').text(rows[0].cardType);
				$('#phone').text(rows[0].relaPhone);
				$('#address').text(rows[0].address);
				$('#midicalrecordId').text(rows[0].midicalrecordId);
			}
		}
	});
}

function queryChangeDept(idcardNo,no){
	$('#listChange').datagrid({
		url: "<%=basePath%>outpatient/newChangeDeptLog/queryChangeDept.action",
		queryParams:{"ationInfo.cardNo":idcardNo,"ationInfo.clinicCode":no}
	});
}

//更换科室
function changeDept(){
	 var id = getIdUtil("#list");
// 	 alert(id);
	 if(id==null||id==""){
	 	return false;
	 }
	 var list = $('#list').datagrid('getSelected');
		var clinicCode = list.clinicCode;
		$.ajax({
			url:'<%=basePath%>register/newInfo/checkISsee.action',
			data:{"clinicCode":clinicCode},
			success:function(dataMap){
				
				if(dataMap.resMsg=="error"){
					$.messager.alert("操作提示",dataMap.resCode);
					return ;
				}else{
					AddOrShowEast('EditForm',"<%=basePath%>outpatient/newChangeDeptLog/eidtRgisterChangeDeptLog.action?ationInfo.id="+id+"&ationInfo.doctCode="+oldAllDoc);
				}
			},
			error:function(){
				$.messager.alert("操作提示","请求发送失败，请检查网络！");
			}
		});
	 
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
			width : 780,
			split : true,
			href : url,
			closable : true
		});
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

function formatYooboox(val,row){
	if(val=="01"){
		return '现场挂号';
	}else if(val=="02"){
		return '预约挂号';
	}else if(val=="03"){
		return '特诊挂号';
	}
}
function searchReload(){
	delSelectedData('idcardNo');
	delSelectedData('no');
	$('.allEmpty').empty();
	$('#list').datagrid('loadData', { total: -1, rows: [] });
	$('#listChange').datagrid('loadData', { total: 0, rows: [] });
}
</script>
</head>
<body>
<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;">
	<div style="padding:5px 5px 5px 5px;">	 
	    <table style="width:100%;border:1px solid #95b8e7;padding:5px;" class="changeskin">
			<tr>
				<td>就诊卡号：
					<input class="easyui-textbox"  id="idcardNo" data-options="prompt:'回车查询'"/>
					&nbsp;门诊号：<input class="easyui-textbox" id="no" data-options="prompt:'回车查询'"/>
					<shiro:hasPermission name="${menuAlias}:function:query">
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search">查询</a>
					</shiro:hasPermission>
					&nbsp;<a href="javascript:void(0)" class="easyui-linkbutton" onclick="changeDept()" iconCls="icon-change">换科</a>
					&nbsp;<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 100%">
			<tr>
	   			<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">姓名：</td>
	   			<td style="width:15%;" class="allEmpty" id="name"></td>
				<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">性别：</td>
				<td style="width:15%;" class="allEmpty" id="sex"></td>
				<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">年龄：</td>
	   			<td style="width:15%;" class="allEmpty" id="age"></td>
	   			<td style="width:10%;background: #E0ECFF;text-align: right;" class="medchangeskin">联系电话：</td>
	   			<td style="width:15%;" class="allEmpty" id="phone"></td>
			</tr>
			<tr>	
				<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">证件号：</td>
				<td class="allEmpty" id="certificatesNo"></td>
				<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">证件类型：</td>
    			<td class="allEmpty" id="certificatesType"></td>
				<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">通讯地址：</td>
    			<td class="allEmpty" id="address"></td>
    			<td style="background: #E0ECFF;text-align: right;" class="medchangeskin">病历号：</td>
    			<td class="allEmpty" id="midicalrecordId"></td>
   			</tr>
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table class="easyui-datagrid" style="width: 100%" title="挂号信息" id="list" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">   
		    <thead>   
		        <tr> 
		            <th data-options="field:'deptCode',hidden:true" style="width: 15%">挂号科室Code</th>
		            <th data-options="field:'deptName'" style="width: 15%">挂号科室</th>   
		            <th data-options="field:'reglevlCode',hidden:true" style="width: 15%">挂号级别Code</th> 
		            <th data-options="field:'reglevlName'" style="width: 15%">挂号级别</th>  
		            <th data-options="field:'doctCode',hidden:true" style="width: 15%">挂号专家COde</th>  
		            <th data-options="field:'doctName'" style="width: 15%">挂号专家</th>  
		            <th data-options="field:'noonCode'" style="width: 15%" formatter="formatCheckBox">挂号午别</th>
		            <th data-options="field:'pactCode',hidden:true" style="width: 15%">合同单位Code</th>  
		            <th data-options="field:'pactName'" style="width: 15%">合同单位</th>  
		            <th data-options="field:'ynbook'" style="width: 15%" formatter="formatYooboox">挂号类别</th>
		            <th data-options="field:'sumCost'" style="width: 9%">挂号费</th>
		        </tr>   
		    </thead>   
		</table>
	</div>
	<div style="padding: 0px 5px 5px 5px;">
		<table class="easyui-datagrid" style="width: 100%" title="换科信息" id="listChange" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">   
		    <thead>   
		        <tr> 
		            <th data-options="field:'oldDept'" style="width: 15%" formatter="functionDept">原科室</th>   
		            <th data-options="field:'newDept'" style="width: 15%" formatter="functionDept">现科室</th>   
		            <th data-options="field:'oldDoc'" style="width: 15%" formatter="functionEmp">原专家</th>   
		            <th data-options="field:'newDoc'" style="width: 15%" formatter="functionEmp">现专家</th>
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