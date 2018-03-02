<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<style type="text/css">
.panel-header,.panel-body{
	border-top:0
}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true" >   
	<div data-options="region:'center'" style="width: 100%;height: 100%;">
		<div class="easyui-layout" data-options="fit:true" > 
			<div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap"  >查询条件：
							<input  id="codes" data-options="prompt:'工号,姓名,学部,总支'"  class="easyui-textbox" style="width: 200px"/>
							&nbsp;部门:<input id="deptCode" class="easyui-combobox" style="width:120px" />
							&nbsp;职称:<input id="workTitle" class="easyui-combobox" style="width:120px"/>
							&nbsp;职务:<input id="workPost" class="easyui-combobox" style="width:120px"/>
							&nbsp;编制:<input id="workstate" class="easyui-combobox" style="width:120px"/>
							<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:reset();" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
							<a href="javascript:void(0)" onclick="confirmFrom()" class="easyui-linkbutton" >确定</a>
						</td>
					</tr>
				</table>
			</div> 
			<div data-options="region:'center',border: false" style="width:100%;">
				<table id="list" class="easyui-datagrid" data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true" text-align: center">id</th>
							<th data-options="field:'division'" style="width:10% " >学部</th>
							<th data-options="field:'department'" style="width:10%">部门</th>
							<th data-options="field:'generalbranch'" style="width:10%">总支</th>
							<th data-options="field:'employeeJobNo'" style="width:8% ">工号</th>
							<th data-options="field:'employeeName'" style="width:9% ">姓名</th>
							<th data-options="field:'employeeSexName'" style="width:5% " >性别</th>
							<th data-options="field:'titleName'" style="width:10%">人员职称</th>
							<th data-options="field:'dutiesName'" style="width:10%">人员职务</th>
							<th data-options="field:'nationalName'" style="width:8% ">民族</th>
							<th data-options="field:'organizationName'" style="width:8% ">编制类别</th>
							<th data-options="field:'politicalstatusName'" style="width:8% ">政治面貌</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>

</div>
<script type="text/javascript">
var titleList=null;
var deptid=null;
var typeList = null;
var havSelect = true;
//科室下拉框及时定位查询 
$('#deptCode').combobox({
	url: "<%=basePath%>baseinfo/department/departmentCombobox.action", 
	valueField : 'deptCode',
	textField : 'deptName',
	filter:function(q,row){
		var keys = new Array();
		keys[keys.length] = 'deptCode';
		keys[keys.length] = 'deptName';
		keys[keys.length] = 'deptPinyin';
		keys[keys.length] = 'deptWb';
		keys[keys.length] = 'deptInputcode';
		if(filterLocalCombobox(q, row, keys)){
			row.selected=true;
		}else{
			row.selected=false;
		}
		return filterLocalCombobox(q, row, keys);
    },
	onLoadSuccess:function(data){
		if(data!=null && data.length==1){
			var code= data[0].deptCode;
			$('#deptCode').combobox('select',code);
		}
	},
	onSelect:function(rec){
		var code=rec.deptCode;
		havSelect = false;
	},
	onHidePanel:function(){
	 	var data = $(this).combobox('getData');
	    var val = $(this).combobox('getValue');
	    var result = true;
	    for (var i = 0; i < data.length; i++) {
	        if (val == data[i].deptCode) {
	            result = false;
	        }
	    }
	    if (result) {
	        $(this).combobox("clear");
	    }else{
	        $(this).combobox('unselect',val);
	        $(this).combobox('select',val);
	    }
		if(havSelect){
			var isOnly = 0;
			var onlyOne = null;
			for(var i = 0;i<$("#deptCode").combobox("getData").length;i++){
				if($("#deptCode").combobox("getData")[i].selected){
					isOnly++;
					onlyOne = $("#deptCode").combobox("getData")[i];
				}
			}
			if((isOnly-1)==0){
				var depCode = onlyOne.deptCode;
				$('#deptCode').combobox('setValue',deptMap[depCode]);
				$('#deptCode').combobox('select',depCode);
			}
		}
		havSelect=true;							
	}
});
//职务下拉框
$('#workPost').combobox({
	url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=duties'/>",
	valueField : 'encode',
	textField : 'name',
	filter:function(q,row){
		var keys = new Array();
		keys[keys.length] = 'encode';
		keys[keys.length] = 'name';
		keys[keys.length] = 'pinyin';
		keys[keys.length] = 'wb';
		if(filterLocalCombobox(q, row, keys)){
			row.selected=true;
		}else{
			row.selected=false;
		}
		return filterLocalCombobox(q, row, keys);
    },
	onLoadSuccess:function(data){
		if(data!=null && data.length==1){
			var code= data[0].encode;
			$('#workPost').combobox('select',code);
		}
	},
	onSelect:function(rec){
		var code=rec.encode;
		havSelect = false;
	},
	onHidePanel:function(){
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
		if(havSelect){
			var isOnly = 0;
			var onlyOne = null;
			for(var i = 0;i<$("#workPost").combobox("getData").length;i++){
				if($("#workPost").combobox("getData")[i].selected){
					isOnly++;
					onlyOne = $("#workPost").combobox("getData")[i];
				}
			}
			if((isOnly-1)==0){
				var encode = onlyOne.encode;
				$('#workPost').combobox('setValue',deptMap[encode]);
				$('#workPost').combobox('select',encode);
			}
		}
		havSelect=true;							
	}
});	
//职称下拉框
$('#workTitle').combobox({
	url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=title'/>",
	valueField : 'encode',
	textField : 'name',
	filter:function(q,row){
		var keys = new Array();
		keys[keys.length] = 'encode';
		keys[keys.length] = 'name';
		keys[keys.length] = 'pinyin';
		keys[keys.length] = 'wb';
		if(filterLocalCombobox(q, row, keys)){
			row.selected=true;
		}else{
			row.selected=false;
		}
		return filterLocalCombobox(q, row, keys);
    },
	onLoadSuccess:function(data){
		if(data!=null && data.length==1){
			var code= data[0].encode;
			$('#workTitle').combobox('select',code);
		}
	},
	onSelect:function(rec){
		var code=rec.encode;
		havSelect = false;
	},
	onHidePanel:function(){
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
		if(havSelect){
			var isOnly = 0;
			var onlyOne = null;
			for(var i = 0;i<$("#workTitle").combobox("getData").length;i++){
				if($("#workTitle").combobox("getData")[i].selected){
					isOnly++;
					onlyOne = $("#workTitle").combobox("getData")[i];
				}
			}
			if((isOnly-1)==0){
				var encode = onlyOne.encode;
				$('#workTitle').combobox('setValue',deptMap[encode]);
				$('#workTitle').combobox('select',encode);
			}
		}
		havSelect=true;							
	}
});	
//编制类型下拉框 
$('#workstate').combobox({
	url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=workstate'/>",
	valueField:'encode',
	textField:'name',
	multiple:false,
	filter:function(q,row){
		var keys = new Array();
		keys[keys.length] = 'encode';
		keys[keys.length] = 'name';
		keys[keys.length] = 'pinyin';
		keys[keys.length] = 'wb';
		if(filterLocalCombobox(q, row, keys)){
			row.selected=true;
		}else{
			row.selected=false;
		}
		return filterLocalCombobox(q, row, keys);
    },
	onLoadSuccess:function(data){
		if(data!=null && data.length==1){
			var code= data[0].encode;
			$('#workstate').combobox('select',code);
		}
	},
	onSelect:function(rec){
		var code=rec.encode;
		havSelect = false;
	},
	onHidePanel:function(){
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
		if(havSelect){
			var isOnly = 0;
			var onlyOne = null;
			for(var i = 0;i<$("#workstate").combobox("getData").length;i++){
				if($("#workstate").combobox("getData")[i].selected){
					isOnly++;
					onlyOne = $("#workstate").combobox("getData")[i];
				}
			}
			if((isOnly-1)==0){
				var encode = onlyOne.encode;
				$('#workstate').combobox('setValue',deptMap[encode]);
				$('#workstate').combobox('select',encode);
			}
		}
		havSelect=true;							
	}
});

$(function(){
	$('#fileFileName').hide();
	$('#list').datagrid({
		pagination: true,
		pageSize: 20,
		url: '<%=basePath %>baseinfo/employeeExtend/queryExtend.action',
		pageList: [20,30,50,100],
		onDblClickRow: function (rowIndex, rowData) {//将该条信息返回原页面
			if('function' === typeof window.opener.popWinEmpCallBackFn){
				window.opener.popWinEmpCallBackFn(rowData);
			}
			window.close();
		},
		onBeforeLoad:function(){
			//翻页时清空前页的选中项
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
		},
		onLoadSuccess:function(row, data){
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
			}}
	});
	bindEnterEvent('codes',searchFrom,'easyui');
});
	
//条件查询
function searchFrom(){
	var codes =  $.trim($('#codes').textbox('getValue'));
	var deptCode = $('#deptCode').combobox('getValue');
	var workPost = $('#workPost').combobox('getValue');
	var workTitle = $('#workTitle').combobox('getValue');
	var workstate = $('#workstate').combobox('getValue');
	$('#list').datagrid('load', {
		queryName: codes,
		deptCode: deptCode,
		workPost: workPost,
		workTitle: workTitle,
		workstate: workstate,
	});
}	


//加载dialog
function Adddilog(url) {//是否有滚动条,是否居中显示,是否可以改变大小
	return window.open(url,'newwindow',' left=273,top=149,width='+ (screen.availWidth -605) +',height='+ (screen.availHeight-352));
}


function refreshdata(){
	$('#list').datagrid('reload');
}
//重置
function reset(){
	$("#codes").textbox("setValue","");
	$('#deptCode').textbox('setValue','');
	$('#workPost').combobox('setValue','');
	$('#workTitle').combobox('setValue','');
	$('#workstate').combobox('setValue','');
	$('#list').datagrid('load',{});
}
//确定
function confirmFrom(){
	var rowData = $("#list").datagrid("getChecked");
	if(rowData.length>0){
		$.messager.confirm("确定","确定委托给选中的人？",function(res){
			if(res){
				if('function' === typeof window.opener.popWinEmpCallBackFn){
					window.opener.popWinEmpCallBackFn(rowData);
				}
				window.close();
			}
		})
	}
}

</script>
</body>
</html>