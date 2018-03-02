<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>挂号查询</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
$(function(){
	//科室下拉框
	$('#dept').combobox({    
		url : '<%=basePath %>baseinfo/department/queryDepartments.action',
		valueField : 'deptCode',
		textField : 'deptName',
		filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'deptCode';
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'deptPinyin';
			keys[keys.length] = 'deptWb';
			keys[keys.length] = 'deptInputcode';
			return filterLocalCombobox(q, row, keys);
	    },
		onSelect:function(rec){
			var url='<%=basePath %>baseinfo/employee/employeeCombobox.action?id='+rec.deptCode;
			$('#doctor').combobox('reload',url);
			$('#doctor').combobox('setValue','');
		}
	});
	//医生下拉框
	$('#doctor').combobox({  
		//不应该获取所有员工的，只需要获取医生,type为1的时候，职位是医生
		url : '<%=basePath %>register/newInfo/empComboboxAllSupport1.action?type=1',
		valueField : 'jobNo',
		textField : 'name',
		filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'jobNo';
			keys[keys.length] = 'name';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			return filterLocalCombobox(q, row, keys);
	    }
	});
	//挂号级别
	$('#grade').combobox({    
	    url: "<%=basePath%>register/newInfo/gradeCombobox.action",   
	    valueField:'code',    
	    textField:'name',
	    filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'code';
			keys[keys.length] = 'name';
			keys[keys.length] = 'codePinyin';
			return filterLocalCombobox(q, row, keys);
	    }
	 });
	//午别
	$('#noon').combobox({    
	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=midday",   
	    valueField:'encode',    
	    textField:'name'
	 });
bindEnterEvent('cardNo',query,'easyui');
});
// 回车查询
// $(window).keydown(function(event) {
//      if(event.keyCode == 13) {
//     	 query();
//      }
// });
// bindEnterEvent('',query,'easyui');
function query(){
	var cardno = $('#cardNo').textbox('getValue');
	var dept = $('#dept').combobox('getValue');
	var doctor = $('#doctor').combobox('getValue');
	var grade = $('#grade').combobox('getValue');
	var noon = $('#noon').combobox('getValue');
	if(cardno==""&&dept==""&&doctor==""&&grade==""&&noon==""){
		$.messager.alert('提示','请填写查询条件！');
		return ;
	}
	$('#registrationList').datagrid({
		url:'<%=basePath%>register/newInfo/queryRegistrarion.action',
		queryParams:{"ationInfo.cardNo":cardno,"ationInfo.doctCode":doctor,"ationInfo.deptCode":dept,"ationInfo.reglevlCode":grade,"ationInfo.noonCode":noon},
		pagination:true,
		pageSize:30,
		pageList:[30,50,80,100],
		onLoadSuccess: function(data){
			//分页工具栏作用提示
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({content:'回车跳转',showEvent:'focus',hideEvent:'blur',hideDelay:1});
			for(var i=0;i<aArr.length;i++){
				$(aArr[i]).tooltip({
					content:toolArr[i],
					hideDelay:1
				});
				$(aArr[i]).tooltip('hide');
			}
 	   }
	});
}
//重置
function searchReload(){
	delSelectedData('cardNo');
	delSelectedData('dept');
	delSelectedData('doctor');
	delSelectedData('grade');
	delSelectedData('noon');
	$('#registrationList').datagrid('loadData', { total: 0, rows: [] });
}
function registerstatus(value,row,index){
	if(value!=null){
		if(value=='0'){
			return "正常";
		}
		if(value=='1'){
			return "<span style='color:#5151A2'>换科</span>";
		}
		if(value=='2'){
			return "<span style='color:#FF5151'>退号</span>";
		}
		if(value=='3'){
			return "退费";
		}
	}else{
		return value;
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
	<div id="cc" class="easyui-layout"  data-options="fit:true" style="width:100%;height:100%;">   
	    <div  data-options="region:'north',border:false" style="height:35px;padding:5px 5px 0px 5px;">
		    <div id="searchTab" style="width: 100%;">
				就诊卡号：<input id="cardNo" name="cardNo" class="easyui-textbox" /> &nbsp;
			 	科室：<input id="dept" name="dept" class="easyui-combobox" /> &nbsp;
				医生：<input id="doctor" name="doctor" class="easyui-combobox" /> &nbsp;
				挂号级别：<input id="grade" name="grade" class="easyui-combobox" />&nbsp; 
			   	午别：<input id="noon" name="noon" class="easyui-combobox" /> &nbsp;
			   	<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a> &nbsp;
			   	<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
			</div>
	    </div>   
	    <div data-options="region:'center'" style="width:100%;">
	    	<table id="registrationList" class="easyui-datagrid"  data-options="striped:true,border:false,singleSelect:true,fit:true">
				<thead>
					<th data-options="field:'cardNo'">就诊卡号</th>
					<th data-options="field:'clinicCode'">门诊号</th>
					<th data-options="field:'patientName'">姓名</th>
					<th data-options="field:'regDate'">挂号日期</th>
					<th data-options="field:'patientSexName'">性别</th>
					<th data-options="field:'patientAge'">年龄</th>
					<th data-options="field:'patientIdenno'">身份证号</th>
					<th data-options="field:'doctName'">挂号医生</th>
					<th data-options="field:'deptName'">科室名称</th>
					<th data-options="field:'reglevlName'">挂号级别</th>
					<th data-options="field:'noonCodeNmae'">午别</th>
					<th data-options="field:'inState',formatter:registerstatus">状态</th>
					<th data-options="field:'operCode'">操作员</th>
				</thead>
			</table>
	    </div>   
	</div>  
</body>
</html>