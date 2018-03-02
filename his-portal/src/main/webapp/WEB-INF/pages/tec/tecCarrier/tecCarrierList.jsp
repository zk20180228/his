<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>医技设备维护</title>
	<%@ include file="/common/metas.jsp" %>
</head>
<body style="margin: 0px;padding: 0px">
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'north'" style="height: 40px;padding-top: 5px;border-top:0;">
		<table>
			<tr>
				<td>科室：<input class="easyui-combobox" id="queryDept"/></td>
				<td>设备：<input class="easyui-textbox" id="queryName"/></td>
				<td>是否空闲：<input class="easyui-combobox" id="queryisDis"   data-options="valueField: 'id',textField: 'text',
				data: [{id: 1,text: '是'},{id:0,text:'否'}]"/></td>
				<td>预计空闲日期：
				<input id="querydisTime" class="Wdate" type="text" value="${dataTime}" onSelect="change()" onClick="WdatePicker()" style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				&nbsp;&nbsp;&nbsp;
				<a href="javascript:searchFrom()"  class="easyui-linkbutton"  data-options="iconCls:'icon-search'">查询</a>
				<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',border:false">
		<table id="list" class="easyui-datagrid" >
<!-- 			<thead> -->
<!-- 				<tr> -->
<!-- 					<th data-options="field:'ck',checkbox:'true'" ></th> -->
<!-- 					<th data-options="field:'deptCode',width:'10%',formatter:functionDept">科室</th> -->
<!-- 					<th data-options="field:'carrierCode',width:'8%'">预约载体编码</th> -->
<!-- 					<th data-options="field:'carrierName',width:'8%'">预约载体名称</th> -->
<!-- 					<th data-options="field:'carrierType',width:'8%'">预约载体类别</th> -->
<!-- 					<th data-options="field:'carrierMemo',width:'10%'">备注</th> -->
<!-- 					<th data-options="field:'spellCode',width:'5%'">拼音码</th> -->
<!-- 					<th data-options="field:'wbCode',width:'5%'">五笔码</th> -->
<!-- 					<th data-options="field:'userCode',width:'5%'">自定义码</th> -->
<!-- 					<th data-options="field:'model',width:'5%',formatter:funModel">型号</th> -->
<!-- 					<th data-options="field:'dayQuota',width:'5%'">日限额</th> -->
<!-- 					<th data-options="field:'doctorQuota',width:'5%'">医生直接预约限额</th> -->
<!-- 					<th data-options="field:'webQuota',width:'5%'">患者自助预约限额</th> -->
<!-- 					<th data-options="field:'isDisengaged',width:'5%',formatter:funIsDisengaged">是否空闲</th> -->
<!-- 					<th data-options="field:'disengagedTime',width:'10%'">预计空闲日期</th> -->
<!-- 					<th data-options="field:'building',width:'10%'">所处建筑物</th> -->
<!-- 					<th data-options="field:'floor',width:'5%'">所处楼层</th> -->
<!-- 					<th data-options="field:'room',width:'5%'">所处房间</th> -->
<!-- 					<th data-options="field:'sortId',width:'5%'">排序号</th> -->
<!-- 					<th data-options="field:'isPrestoptime',width:'10%'">是否有预停用时间</th> -->
<!-- 					<th data-options="field:'preStarttime',width:'10%'">预启动时间</th> -->
<!-- 					<th data-options="field:'preStoptime',width:'10%'">预停止时间</th> -->
<!-- 					<th data-options="field:'avgTurnoverTime',width:'10%'">平均周转时间</th> -->
<!-- 					<th data-options="field:'deviceType',width:'10%',formatter:funDeviceType">设备类别</th> -->
<!-- 				</tr> -->
<!-- 			</thead> -->
		</table>
	</div>
</div>
<div id="toolbarId">
	<a href="javascript:add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	<a href="javascript:edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	<a href="javascript:del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
</div>
<div id="addWin"></div>
<script type="text/javascript">
	//空闲时间
	function change(){
		searchFrom();
	};

	var deptNameList="";  //科室名字List
	var deviceTypeList="";  //设备名称List
	var modelList="";  //型号List
	//科室名字List
	$.ajax({ 
		url: "<%=basePath%>technical/TecCarrier/queryDept.action",
		type: "POST",
		success: function(deptData){
			deptNameList=deptData;
		}
	});
	//设备名称List
	$.ajax({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=sbtype',
		type:'post',
		success: function(typeData){
			deviceTypeList=typeData;
		}
	});
	//型号List
	$.ajax({
		url: '<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=version',
		type:'post',
		success: function(modelData){
			modelList=modelData;
		}
	});
	$(function(){
		bindEnterEvent('queryName',searchFrom,'easyui');   //查询条件
		//设备
		$("#queryName").textbox('textbox').bind('keyup',function(event){
			searchFrom();
		});
		//是否空闲
		$("#queryisDis").combobox({
			onChange:function(){
				searchFrom();
			}
		});
		$("#queryisDis").combobox('setValue',1);//为空闲设默认值
		//空闲时间
		/* $("#querydisTime").datebox({
			onChange:function(){
				searchFrom();
			}
		}); */
		
	});
	//科室下拉框查询条件
	$("#queryDept").combobox({
		url:'<%=basePath%>technical/TecCarrier/queryDept.action',
		valueField:'deptCode',
		textField:'deptName',
		filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'deptCode';
			keys[keys.length] = 'deptName';
			keys[keys.length] = 'deptPinyin';
			keys[keys.length] = 'deptWb';
			keys[keys.length] = 'deptInputcode';
			return filterLocalCombobox(q, row, keys);
		},
		onChange:function(){
			searchFrom();
		}
	});
	//本地过滤方法
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}
	setTimeout(function(){
$("#list").datagrid({
	rownumbers:true,
	striped:true,
	border:false,
	checkOnSelect:true,
	selectOnCheck:false,
	singleSelect:true,
	fitColumns:false,
	fit:true,
	pagination:true,
	pageSize:20,
	pageList:[20,30,50,80,100],
	url:"<%=basePath%>technical/TecCarrier/queryTecCarrier.action",
	toolbar:'#toolbarId',
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
	},

	onBeforeLoad:function(param){
		$(this).datagrid('uncheckAll');
	},
	columns:[[    
	          {field:'ck',checkbox:'true'},    
	          {field:'deptCode',title:'科室',width:'10%',formatter: 
	        	  function(value,row,index){
			        	  for(var i=0;i<deptNameList.length;i++){
					      		if(value==deptNameList[i].id){
					      			return deptNameList[i].deptName;
					      		}
					      	}
					}
				},    
	          {field:'carrierCode',title:'预约载体编码',width:'8%'},  
	          {field:'carrierName',title:'预约载体名称',width:'8%'},  
	          {field:'carrierType',title:'预约载体类别',width:'8%'},  
	          {field:'carrierMemo',title:'备注',width:'10%'},  
	          {field:'spellCode',title:'拼音码',width:'5%'},  
	          {field:'wbCode',title:'五笔码',width:'5%'},  
	          {field:'userCode',title:'自定义码',width:'5%'},  
	          {field:'model',title:'型号',width:'5%',formatter: 
	        	  function(value,row,index){
		        	  for(var i=0;i<modelList.length;i++){
		        			if(value==modelList[i].encode){
		        				return modelList[i].name;
		        			}
		        		}
				}},  
	          {field:'dayQuota',title:'日限额',width:'5%'},  
	          {field:'doctorQuota',title:'医生直接预约限额',width:'5%'},  
	          {field:'webQuota',title:'患者自助预约限额',width:'5%'},  
	          {field:'isDisengaged',title:'是否空闲',width:'5%',formatter: 
	        	  function(value,row,index){
		        	  if(value==1){
		        			return '是';
		        		}else{
		        			return '否';
		        		}
				}},  
	          {field:'disengagedTime',title:'预计空闲日期',width:'10%'},  
	          {field:'building',title:'所处建筑物',width:'10%'},  
	          {field:'floor',title:'所处楼层',width:'5%'},  
	          {field:'room',title:'所处房间',width:'5%'},  
	          {field:'sortId',title:'排序号',width:'5%'},  
	          {field:'isPrestoptime',title:'是否有预停用时间',width:'10%'},  
	          {field:'preStarttime',title:'预启动时间',width:'10%'},  
	          {field:'preStoptime',title:'预停止时间',width:'10%'},  
	          {field:'avgTurnoverTime',title:'平均周转时间',width:'10%'},  
	          {field:'deviceType',title:'设备类别',width:'10%',formatter: 
	        	  function(value,row,index){
		        	  for(var i=0;i<deviceTypeList.length;i++){
		        			if(value==deviceTypeList[i].encode){
		        				return deviceTypeList[i].name;
		        			}
		        		}
				}},  
	      ]]    

	
});
},200);
//添加
function add(){
	AddOrShowEast('添加页面','<%=basePath%>technical/TecCarrier/tecCarrierAdd.action');
}
//修改
function edit(){
	var list = $("#list").datagrid('getSelected');
	if(list==null||list==""){
		$.messager.alert('提示','请选择一条记录！');
		setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
		return;
	}
	if(getIdUtil("#list").length!=0){
		AddOrShowEast('修改页面',"<%=basePath%>technical/TecCarrier/tecCarrierEdit.action?id="+getIdUtil("#list"));
	}
	
}
//删除
function del(){
	//选中要删除的行
    var ids = $('#list').datagrid('getChecked');
   	if (ids.length > 0) {//选中几行的话触发事件	                        
	 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
			if (res){
				var idss = '';
				for(var i=0; i<ids.length; i++){
					if(idss!=''){
						idss += ',';
					}
					idss += ids[i].id;
				};
				$.ajax({
					url:"<%=basePath%>technical/TecCarrier/tecCarrierDel.action",
					data:{ids:idss},
					type:'post',
					success: function() {
						$.messager.alert('提示','删除成功');
						$('#list').datagrid('reload');
					}
				});
			}
        });
    }else{
    	$.messager.alert('警告！','请选择要删除的信息！','warning');
    	setTimeout(function(){
			$(".messager-body").window('close');
		},3500);
    }
}
//查询
function searchFrom(){
	var queryDept=$("#queryDept").combobox('getValue');   //科室
	var queryName=$("#queryName").textbox('getText');  //设备
	var queryisDis=$("#queryisDis").combobox('getValue');  //是否空闲
	var querydisTime=$("#querydisTime").val();  //预计空闲日期
	$("#list").datagrid('load',{
		queryDept:queryDept,
		queryName:queryName,
		queryisDis:queryisDis,
		querydisTime:querydisTime
	})
}

/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-21
 * @version 1.0
 */
function clears(){
	$('#queryDept').combobox('setValue','');
	$('#queryName').textbox('setValue','');
	$('#queryisDis').combobox('setValue','1');
	$('#querydisTime').val('${dataTime}');
	searchFrom();
}
//弹框
function AddOrShowEast(title,url){
	$('#addWin').dialog({
		title: title,
		width:'80%',
		height:'80%',
		closed: false,
		cache: false,
		href: url,
		modal:true
	});
}
//科室id显示name
function functionDept(value,row,index){
	for(var i=0;i<deptNameList.length;i++){
		if(value==deptNameList[i].id){
			return deptNameList[i].deptName;
		}
	}
}
// $(function(){
// 	setTimeout(function(value,row,index){
// 		functionDept(value,row,index)
// 			},200);
//  });
//设备渲染
function funDeviceType(value,index,row){
	for(var i=0;i<deviceTypeList.length;i++){
		if(value==deviceTypeList[i].encode){
			return deviceTypeList[i].name;
		}
	}
}
//是否空闲渲染
function funIsDisengaged(value,index,row){
	if(value==1){
		return '是';
	}else{
		return '否';
	}
}
//型号渲染
function funModel(value,index,row){
	for(var i=0;i<modelList.length;i++){
		if(value==modelList[i].encode){
			return modelList[i].name;
		}
	}
}
</script>
</body>
</html>