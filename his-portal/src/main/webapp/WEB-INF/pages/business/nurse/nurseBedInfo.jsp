<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body >
<div id="updateBedInfoLayout" class="easyui-layout" data-options="fit:true"> 
	<div data-options="region:'north'" style="background:#FCFCFC;height:65px;padding-top: 10px;">
			&nbsp;病房号：<input id="roomNumF"  class="easyui-textbox"   data-options="prompt:'回车查询'">
			&nbsp;床 号：<input id="bedNameF" class="easyui-textbox"    data-options="prompt:'回车查询'">
			&nbsp;床位等级：<input id="bedLevelF"  data-options="">
			&nbsp;床位状态：<input id="bedStateF" style="width: 150px;"   data-options="">
			&nbsp;床位编制：<input id="bedOrganF"     data-options="">
			<shiro:hasPermission name="${menuAlias}:function:query">
			&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:set">
			<a href="javascript:void(0)" onclick="clearw()" class="easyui-linkbutton" iconCls="reset">重置</a>
			</shiro:hasPermission>
	</div>
	<div data-options="region:'center'" style="background:#FCFCFC;height:50%">
		<table id="bedInfoList" style="height:100%">
		</table>
		<div id="rightClick" class="easyui-menu" data-options="" style="width: 120px;">
			<div id="jiebao"  onclick="unpack()"   data-options="iconCls:'icon-mini_edit'">解包</div>
		</div>
	</div>
	
	<div data-options="region:'south',title:'病床操作',collapsed:true" style="height:50%;background:#FFFFFF">
		<form id="bedForm" method="post">
			<table  style="width: 70%; border:0px solid black">
				<tr style="height: 20px"></tr>
				<tr>
					<td style="text-align: right; width: 11%;">床 号：</td>
					<td style="width: 11%"><input id="bedName" name="businessHospitalbed.bedName"class="easyui-textbox" data-options="required:true"></td>
					<td style="text-align: right; width: 11%;">床位状态：</td>
					<td style="width: 11%"><input id="bedState" style="width: 150px;" name="businessHospitalbed.bedState"></td>
					<td style="text-align: right; width: 11%;">主任医生：</td>
					<td style="width: 11%"><input id="zrysName"  style="width: 150px;" name="businessHospitalbed.chiefDocName"
					data-options="iconCls:'icon-user_gray'">
					<input id="zrys" type="hidden" name="businessHospitalbed.chiefDocCode">
					<input id="hiddenBedId" name="businessHospitalbed.id" type="hidden">
					</td>
				</tr>
				<tr style="height: 10px"></tr>
				<tr>
				    <td style="text-align: right; width: 11%;">归 属：</td>
					<td style="width: 11%"><input id="bedBelong" class="easyui-textbox" name="businessHospitalbed.bedBelong"></td>
					<td style="text-align: right; width: 11%;">床位编制：</td>
					<td style="width: 11%"><input id="bedOrgan"class="easyui-combobox" name="businessHospitalbed.bedOrgan" 
					data-options=""></td>
					<td style="text-align: right; width: 11%;">责任护士：</td>
					<td style="width: 11%"><input id="zrhsName" style="width: 150px;" name="businessHospitalbed.dutyNurseName" 
						data-options=" iconCls:'icon-user_female'">
						<input id="zrhs" type="hidden" name="businessHospitalbed.dutyNurseCode">
						  </td>
				</tr>
				<tr style="height: 10px"></tr>
				<tr>
					<td style="text-align: right; width: 11%;">电 话：</td>
					
					<td style="width: 11%"><input id="bedPhone"class="easyui-numberbox" name="businessHospitalbed.bedPhone"></td>
					
					<td style="text-align: right; width: 11%;">床位等级：</td>
					
					<td style="width: 11%"><input id="bedLevel"
						class="easyui-combobox" name="businessHospitalbed.bedLevel">
						</td>
					<td style="text-align: right; width: 11%;">住院医生：</td>
					
					<td style="width: 11%"><input id="zyysName" style="width: 150px;"  name="businessHospitalbed.houseDocName"
						data-options="iconCls:'icon-user_home'">
						<input id="zyys" name="businessHospitalbed.houseDocCode" type="hidden" >
					</td>
					
					
				</tr>
				<tr style="height: 10px"></tr>
				<tr>
					<td style="text-align: right; width: 11%;">费 用：</td>
					<td style="width: 11%">
					<input id="bedRoom"class="easyui-numberbox" name="businessHospitalbed.bedFee">
					</td>
					<td style="text-align: right; width: 11%;">是否在用：</td>
					<td style="width: 11%">
					<input id="isUser"class="easyui-combobox"
						data-options="editable:false,
						valueField: 'label',
						textField: 'value',
						data: [
						{label: '1',value: '是'},
						{label: '2',value: '否'}
						]"></td>
					<td style="text-align: right; width: 11%;">主治医师：</td>
					<td style="width: 11%"><input id="zzysName" style="width: 150px;"  name="businessHospitalbed.chargeDocName"
						data-options="iconCls:'icon-user_suit'">
						<input id="zzys" type="hidden" name="businessHospitalbed.chargeDocCode">
						</td>
				</tr>
				<tr style="height: 10px"></tr>
				<tr>
					<td style="text-align: right; width: 11%;">排 序：</td>
					<td style="width: 11%"><input id="descOrdce" class="easyui-textbox" name="businessHospitalbed.bedOrder"></td>
					<td style="text-align: right; width: 11%;">护理组：</td>
					<td style="width: 11%">
					<input id="nursetendGroup"  class="easyui-textbox" name="businessHospitalbed.nursetendGroup">
					</td>
					<td style="text-align: right; width: 11%;">病房号：</td>
					<td style="width: 11%">
					<input id="roomNum" class="easyui-textbox" readonly="readonly">
					</td>
				</tr>
				
				<tr style="height: 10px"></tr>
				<tr>
				    <td style="text-align: right; width: 11%;"></td>
					<td style="width: 11%"></td>
				    <td style="text-align: right; width: 11%;"></td>
				    <td>
					<a id="updateBedBtn" class="easyui-linkbutton" onClick="updateBed()" data-options="iconCls:'icon-save'">保存</a>
					</td>
					<td hidden><input id="hulizu" class="easyui-textbox" name="inpatientBedinfo.nurseCellCode"/></td>
					
				</tr>
				<tr style="height: 10px"></tr>
			</table>
		</form>


	</div>

</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var bedOrMap=new Map();
var bedStateMap=new Map();
var toolArr = new Array('首页','上一页','下一页','尾页','刷新','删除','统计图表');
function unpack(){
	var rowData = $('#bedInfoList').datagrid('getSelections');
	$.ajax({
		url : "<%=basePath%>nursestation/nurse/saveunPackbed.action",
		data:{id:rowData[0].id},
		type:'post',
		success: function(data) {
			if(data=="success"){
				$.messager.alert('提示','解包成功！');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				searchFrom();
			}else{
				$.messager.alert('提示','解包成功');
			}
		}
	});
}
//清空查询区
function clearw(){
	$('#bedNameF').textbox('setValue',"");
	$('#bedOrganF').combobox('setValue',"");
	$('#bedStateF').combobox('setValue',"");
	$('#roomNumF').textbox('setValue',"");
	$('#bedLevelF').combobox('setValue',"");
	$('#bedInfoList').datagrid('load', {
	});
}
//渲染床位状态
function funBedState(value,row,index){
	if(value!=null&&value!=''){
		return bedStateMap.get(value);
	}
}
//	渲染床位编制
function funBedOrgan(value,row,index){
	if(value!=null&&value!=''){
		return bedOrMap.get(value);
	}
}

/**
 * 回车键查询
 * @author  lt
 * @date 2015-06-19
 * @version 1.0
 */
function KeyDown(){  
    if (event.keyCode == 13)  
    {  
        event.returnValue=false;  
        event.cancel = true;  
        searchFrom();  
    }  
} 

function searchFrom(){
	var bedNameF =$('#bedNameF').textbox('getValue'); 
    var bedOrganF =	$('#bedOrganF').combobox('getValue'); 
    var bedStateF =$('#bedStateF').combobox('getValue');  
    var roomNumF =$('#roomNumF').textbox('getValue'); 
    var bedLevelF =$('#bedLevelF').combobox('getValue'); 
	$('#bedInfoList').datagrid('load', {
		bedNameF:bedNameF,
		bedOrganF: bedOrganF,
		bedStateF:bedStateF,
		roomNumF: roomNumF,
		bedLevelF:bedLevelF
	});
}

//更新床位信息
function updateBed(){
	var bedId=$("#hiddenBedId").val(); //双击datagrid得到的病床Id。
	
	$("#bedForm").form('submit',{
		 url:"<%=basePath%>nursestation/nurse/updateBedInfo.action",
		 onSubmit:function(){
				if (!$('#bedForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					return false;
				}
				$.messager.progress({text:'正在处理病床信息，请稍等....',modal:true});
	        }, 
	        success:function(data){
	        	$.messager.progress('close');
	        	if(data=="success"){
	        		$.messager.alert('提示',"修改成功！");
	        		setTimeout(function(){
						$(".messager-body").window('close');
				},3500);
	        		$("#bedForm").form('clear');
					$('#bedInfoList').datagrid('reload');
	        	}else if(data=="error"){
	        		$.messager.alert('提示',"修改失败！");
					$('#bedInfoList').datagrid('reload');
	        	}
	        } ,
	        error:function(){
	        	$.messager.progress('close');
	        	$.messager.alert('提示','未知错误，请联系管理员!');
	        }
	})
};


$(function(){

	$.ajax({
	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
		type:'post',
		success: function(data) {
			var type = data;
			for(var i=0;i<type.length;i++){
				bedStateMap.put(type[i].encode,type[i].name);
			}
		}
	});
	
	
	
	$.ajax({
	    url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=badorgan",
		type:'post',
		success: function(data) {
			var ortype = data;
			for(var i=0;i<ortype.length;i++){
				bedOrMap.put(ortype[i].encode,ortype[i].name);
			}
		}
	});	
	//床位状态
	 $("#bedState").combobox({ 
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
		valueField:'encode',    
		textField:'name',
		multiple:false,
		onHidePanel:function(none){
    	    var data = $(this).combobox('getData');
    	    var val = $(this).combobox('getValue');
    	    var result = true;
    	    for (var i = 0; i < data.length; i++) {
    	        if (val == data[i].encode) {
    	            result = false;
    	        }
    	    }
    	    if (result) {
    	        $(this).combobox("clear");
    	    }else{
    	        $(this).combobox('unselect',val);
    	        $(this).combobox('select',val);
    	    }
    	},
    	filter: function(q, row){
    	    var keys = new Array();
    	    keys[keys.length] = 'encode';
    	    keys[keys.length] = 'name';
    	    keys[keys.length] = 'pinyin';
    	    keys[keys.length] = 'wb';
    	    keys[keys.length] = 'inputCode';
    	    return filterLocalCombobox(q, row, keys);
    	},
	});
	//床位等级
	$("#bedLevel").combobox({ 
		 url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bedGrade",
		 valueField:'encode',    
	     textField:'name',
	     multiple:false,
	     required:true,
	     onHidePanel:function(none){
	    	    var data = $(this).combobox('getData');
	    	    var val = $(this).combobox('getValue');
	    	    var result = true;
	    	    for (var i = 0; i < data.length; i++) {
	    	        if (val == data[i].encode) {
	    	            result = false;
	    	        }
	    	    }
	    	    if (result) {
	    	        $(this).combobox("clear");
	    	    }else{
	    	        $(this).combobox('unselect',val);
	    	        $(this).combobox('select',val);
	    	    }
	    	},
	    	filter: function(q, row){
	    	    var keys = new Array();
	    	    keys[keys.length] = 'encode';
	    	    keys[keys.length] = 'name';
	    	    keys[keys.length] = 'pinyin';
	    	    keys[keys.length] = 'wb';
	    	    keys[keys.length] = 'inputCode';
	    	    return filterLocalCombobox(q, row, keys);
	    	},
	     onSelect :function(data){
	    	 $.ajax({
	    		url : "<%=basePath%>nursestation/nurse/findBedFee.action",
    			type:'post',
    			data: {"bedLevelF":data.encode},
    			success: function(data) {
    				$('#bedRoom').textbox('setValue',data);  
    			}
    		});
		}
	});
	
	
	//床位状态(查询)
	 $("#bedStateF").combobox({ 
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bedtype",
		valueField:'encode',    
		textField:'name',
		multiple:false,
		onHidePanel:function(none){
    	    var data = $(this).combobox('getData');
    	    var val = $(this).combobox('getValue');
    	    var result = true;
    	    for (var i = 0; i < data.length; i++) {
    	        if (val == data[i].encode) {
    	            result = false;
    	        }
    	    }
    	    if (result) {
    	        $(this).combobox("clear");
    	    }else{
    	        $(this).combobox('unselect',val);
    	        $(this).combobox('select',val);
    	    }
    	},
    	filter: function(q, row){
    	    var keys = new Array();
    	    keys[keys.length] = 'encode';
    	    keys[keys.length] = 'name';
    	    keys[keys.length] = 'pinyin';
    	    keys[keys.length] = 'wb';
    	    keys[keys.length] = 'inputCode';
    	    return filterLocalCombobox(q, row, keys);
    	},
		onSelect :function(data){
			searchFrom();
		}
	});
	//床位等级(查询)
	$("#bedLevelF").combobox({ 
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=bedGrade",
		valueField:'encode',    
		textField:'name',
		multiple:false,
		 onHidePanel:function(none){
	    	    var data = $(this).combobox('getData');
	    	    var val = $(this).combobox('getValue');
	    	    var result = true;
	    	    for (var i = 0; i < data.length; i++) {
	    	        if (val == data[i].encode) {
	    	            result = false;
	    	        }
	    	    }
	    	    if (result) {
	    	        $(this).combobox("clear");
	    	    }else{
	    	        $(this).combobox('unselect',val);
	    	        $(this).combobox('select',val);
	    	    }
	    	},
	    	filter: function(q, row){
	    	    var keys = new Array();
	    	    keys[keys.length] = 'encode';
	    	    keys[keys.length] = 'name';
	    	    keys[keys.length] = 'pinyin';
	    	    keys[keys.length] = 'wb';
	    	    keys[keys.length] = 'inputCode';
	    	    return filterLocalCombobox(q, row, keys);
	    	},
		onSelect :function(data){
			searchFrom();
		}
	});
	
	//床位编制(查询)
	$("#bedOrganF").combobox({ 
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=badorgan",
		valueField:'encode',    
		textField:'name',
		multiple:false,
		onHidePanel:function(none){
    	    var data = $(this).combobox('getData');
    	    var val = $(this).combobox('getValue');
    	    var result = true;
    	    for (var i = 0; i < data.length; i++) {
    	        if (val == data[i].encode) {
    	            result = false;
    	        }
    	    }
    	    if (result) {
    	        $(this).combobox("clear");
    	    }else{
    	        $(this).combobox('unselect',val);
    	        $(this).combobox('select',val);
    	    }
    	},
    	filter: function(q, row){
    	    var keys = new Array();
    	    keys[keys.length] = 'encode';
    	    keys[keys.length] = 'name';
    	    keys[keys.length] = 'pinyin';
    	    keys[keys.length] = 'wb';
    	    keys[keys.length] = 'inputCode';
    	    return filterLocalCombobox(q, row, keys);
    	},
		onSelect :function(data){
			searchFrom();
		}
	});
	//床位编制
	$("#bedOrgan").combobox({ 
		url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=badorgan",
		valueField:'encode',
		textField:'name',
		multiple:false,
		onHidePanel:function(none){
    	    var data = $(this).combobox('getData');
    	    var val = $(this).combobox('getValue');
    	    var result = true;
    	    for (var i = 0; i < data.length; i++) {
    	        if (val == data[i].encode) {
    	            result = false;
    	        }
    	    }
    	    if (result) {
    	        $(this).combobox("clear");
    	    }else{
    	        $(this).combobox('unselect',val);
    	        $(this).combobox('select',val);
    	    }
    	},
    	filter: function(q, row){
    	    var keys = new Array();
    	    keys[keys.length] = 'encode';
    	    keys[keys.length] = 'name';
    	    keys[keys.length] = 'pinyin';
    	    keys[keys.length] = 'wb';
    	    keys[keys.length] = 'inputCode';
    	    return filterLocalCombobox(q, row, keys);
    	},
	});
	bindEnterEvent('bedNameF',searchFrom,'easyui');
	bindEnterEvent('roomNumF',searchFrom,'easyui');
	bindEnterEvent('bedStateF',searchFrom,'easyui');
	bindEnterEvent('bedLevelF',searchFrom,'easyui');
	bindEnterEvent('bedOrganF',searchFrom,'easyui');
	  $('#zyysName').combogrid({
	  		url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
	  		disabled : false,
	  		mode:'remote',
	  		panelWidth:450, 
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			fitColumns : true,//自适应列宽
			pageSize : 5,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
			idField : 'name',
			textField : 'name',
			columns : [ [ {
				field : 'jobNo',
				title : '编号',
				width : 200
			}, {
				field : 'name',
				title : '姓名',
				width : 200,
			}, {
				field : 'deptName',
				title : '所在科室',
				width : 200
			}] ],
			onSelect :function(rowIndex, rowData){
				$("#zyys").val(rowData.jobNo);
			}
	   
  	});
	  
	  $('#zrysName').combogrid({ 
	  		url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
	  		disabled : false,
	  		mode:'remote',
	  		panelWidth:450, 
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			fitColumns : true,//自适应列宽
			pageSize : 5,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
			idField : 'name',
			textField : 'name',
			columns : [ [ {
				field : 'jobNo',
				title : '编号',
				width : 200
			}, {
				field : 'name',
				title : '姓名',
				width : 200,
			}, {
				field : 'deptName',
				title : '所在科室',
				width : 200
			}] ],
			onSelect :function(rowIndex, rowData){
				$("#zrys").val(rowData.jobNo);
			}
			
	});
	  
	  $('#zzysName').combogrid({ 
	  		url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
	  		disabled : false,
	  		mode:'remote',
	  		panelWidth:450, 
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			fitColumns : true,//自适应列宽
			pageSize : 5,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
			idField : 'name',
			textField : 'name',
			columns : [ [ {
				field : 'jobNo',
				title : '编号',
				width : 200
			}, {
				field : 'name',
				title : '姓名',
				width : 200,
			}, {
				field : 'deptName',
				title : '所在科室',
				width : 200
			}] ],
			onSelect :function(rowIndex, rowData){
				$("#zzys").val(rowData.jobNo);
			}
			
	});
	  
	  $('#zrhsName').combogrid({ 
	  		url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=hs",
	  		disabled : false,
	  		mode:'remote',
	  		panelWidth:450, 
			rownumbers : true,//显示序号 
			pagination : true,//是否显示分页栏
			fitColumns : true,//自适应列宽
			pageSize : 5,//每页显示的记录条数，默认为10  
			pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
			idField : 'name',
			textField : 'name',
			columns : [ [ {
				field : 'jobNo',
				title : '编号',
				width : 200
			}, {
				field : 'name',
				title : '姓名',
				width : 200,
			}, {
				field : 'deptName',
				title : '所在科室',
				width : 200
			}] ],
			onSelect :function(rowIndex, rowData){
				$("#zrhs").val(rowData.jobNo);
			}
			
	});
	  
	if($("#hiddenBedId").val()!=null){
		$("#updateBedInfoLayout").layout('expand','south'); 
	}
	$("#bedInfoList").datagrid({
		fit:true,
		pagination:true,
		rownumbers:true,
		pageSize:10,
		pageList:[10,40,80],
		fitColumns:true,
		checkOnSelect:false,
		selectOnCheck:false,
		singleSelect:true,
		loadMsg:'病床加载中，请稍候........',
		url:"<%=basePath%>nursestation/nurse/getBedInfoList.action",
		columns:[[    
		          {field:'bedwardName',title:'病房号',width:'11%',hidden:true,align:'center'},  
		          {field:'roomNum',title:'病房号',width:'7%',align:'center'},    
		          {field:'bedName',title:'床位号',width:'5%',align:'center'},    
		          {field:'bedLevel',title:'床位等级',align:'center',width:'9%'},
		          {field:'bedState',title:'床位状态',width:'9%',align:'center',formatter:funBedState},    
		          {field:'bedOrgan',title:'床位编制',width:'11%',align:'center',formatter:funBedOrgan},      
		          {field:'bedPhone',title:'床位电话',width:'11%',align:'center'},
		          {field:'bedBelong',title:'归属',width:'11%',align:'center'},    
		          {field:'bedFee',title:'费用',width:'11%',align:'center'}, 
		          {field:'bedOrder',title:'排序',width:'9%',align:'center'},
		          {field:'patientName',title:'当前病人',width:'11%',align:'center'}
		      ]] ,
		      onBeforeLoad:function(param){
		    	  	$('#bedInfoList').datagrid('clearChecked');
					$('#bedInfoList').datagrid('clearSelections');
				   var pager4 = $('#bedInfoList').datagrid('getPager');    // 得到datagrid的pager对象  
    	    		pager4.pagination({    
    	    		    showPageList:true,    
    	    		    buttons:[{    
    	    		        iconCls:'icon-bullet_minus',    
    	    		        handler:function(){  
    	    		        	var rows=$("#bedInfoList").datagrid('getSelections');     
    	    		        	if(rows.length>0){
    	    		        	for (var j = 0; j < rows.length; j++) {
    	    		        		if(rows[j].bedOrgan=="4"){
    	    		        			$.messager.alert('警告',''+rows[j].bedName+'床位属于编制内，不可删除！');  
    	    		        			setTimeout(function(){
    	    		    					$(".messager-body").window('close');
    	    		    			},3500);
    	    		        			return;
    	    		        		}else if(rows[j].bedState=="2"||rows[j].bedState=="4"){
    	    		        			$.messager.alert('警告',''+rows[j].bedName+'床位占用状态，不可删除！');  
    	    		        			setTimeout(function(){
    	    		    					$(".messager-body").window('close');
    	    		    			},3500);
    	    		        			return;
    	    		        		}else{
    	    		        	 	    $.messager.confirm('确认', '确定要删除选中信息吗?', function(res){
    	    		        			if (res){
    	    		        				var ids = '';
    	    		        				for(var i=0; i<rows.length; i++){
    	    		        					if(ids!=''){
    	    		        						ids += ',';
    	    		        					}
    	    		        					ids += rows[i].id;
    	    		        				};
    	    		        				$.ajax({
    	    		        					type:'post',
    	    		        					url: "<%=basePath%>nursestation/nurse/deleteBedInfo.action",
    	    		        					data:{bid:ids},
    	    		        					success: function() {
    	    		        						$.messager.alert('警告','删除成功');
    	    		        						setTimeout(function(){
    	    		        							$(".messager-body").window('close');
    	    		        					},3500);
    	    		        						$('#bedInfoList').datagrid('reload');
    	    		        					}
    	    		        			    });
    	    		        			}
    	    		          	});
    	    		        	}
    	    		        	}
    	    		        	}else{
    	    		        		$.messager.alert('警告','请至少选择一条床位记录进行删除！');  
    	    		        		setTimeout(function(){
    	    							$(".messager-body").window('close');
    	    					},3500);
    	    		        	}
    	    		            }    
    	    		          },{    
    	    		        iconCls:'icon-chart_bar',    
    	    		        handler:function(){
   	    		        		var url="<%=basePath%>nursestation/nurse/statisticalChart.action";
   	    		        		if (window.parent.$('#tabsWe').tabs('exists', '统计图表')){
   	    		        			window.parent.$('#tabsWe').tabs('select', '统计图表');
   	    		        				return;
   	    		        			}
   	    		        		window.parent.$('#tabsWe').tabs('add',{
   	    		        			title:'统计图表',
   	    		        			// 新内容的URL
   	    		        			content:"<iframe scrolling='auto' frameborder='0'  src="+url+"  style='width:100%;height:100%;'></iframe>",
   	    		        			closable:true
   	    		        		});
   	    		            }    
   	    		      }]     
   	    		})
		   },
		      onDblClickRow:function(rowIndex, rowData){
		    	  
		    	  $("#bedName").textbox('setValue',rowData.bedName);
		    	  $("#hiddenBedId").val(rowData.id);
		    	  $("#bedPhone").numberbox('setValue',rowData.bedPhone);
		    	  $("#bedRoom").numberbox('setValue',rowData.bedFee);
		    	  $("#bedBelong").textbox('setValue',rowData.bedBelong);
		    	  $("#descOrdce").textbox('setValue',rowData.bedOrder);
		    	  $("#bedState").combobox('setValue',rowData.bedState);
		    	  $("#bedLevel").combobox('setValue',rowData.bedLevelEncode);
		    	  $("#bedOrgan").combobox('setValue',rowData.bedOrgan);
		    	  $("#nursetendGroup").textbox('setValue',rowData.nursetendGroup);
		    	  $("#roomNum").textbox('setValue',rowData.roomNum);
		    	  $("#zyys").val(rowData.houseDocCode);
		    	  $("#zrys").val(rowData.chiefDocCode);
		    	  $("#zrhs").val(rowData.dutyNurseCode);
		    	  $("#zzys").val(rowData.chargeDocCode);
		    	  
		    	  $("#zyysName").combogrid('setValue',rowData.houseDocName);
		    	  $("#zrysName").combogrid('setValue',rowData.chiefDocName);
		    	  $("#zrhsName").combogrid('setValue',rowData.dutyNurseName);
		    	  $("#zzysName").combogrid('setValue',rowData.chargeDocName);
		    	  
		    	  //是否在用
		    	  if(rowData.bedState!="7"&&rowData.bedState!="1"){
		    		  $("#isUser").combobox('setValue','是');
		    	  }else{
		    		  $("#isUser").combobox('setValue','否');
		    	  };
		    	 
		      },
		      onRowContextMenu:function(e, index, row){
		    	  e.preventDefault();
		    		$('#bedInfoList').datagrid('selectRow',index);
					if(row.bedState=="2"){
						$('#jiebao').css("display","block");
						$('#rightClick').menu('show',{
							left: e.pageX,
							top: e.pageY
						});
					}else{
						$('#jiebao').css("display","none");
					}
		      }
	});
})
</script>
</body>
</html>