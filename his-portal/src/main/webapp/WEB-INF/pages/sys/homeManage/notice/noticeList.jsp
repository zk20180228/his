<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<script type="text/javascript">
setTimeout(function(){
	bindEnterEvent('codes',searchFrom,'easyui');//回车键查询	
},200);
//人员map
var employeeMap=null;
var toolArr = new Array('首页','上一页','下一页','尾页','刷新');
$(function(){
	funformat();
	dataGrid(0);
	$('#tt').tabs({    
	    onSelect:function(title,index){    
	    	dataGrid(index);
	    }    
	}); 
});
//显示table信息数据
function dataGrid(index){
	if(index == 0){
		//通知
		$('#list1').datagrid({    
		    url:'<%=basePath%>sys/noticeManage/showList.action', 
		    pageSize:20,
			pageList:[20,40,60,100],
		    queryParams: {
		    	'info.infoPubflag':1,
		    	'info.infoType': 1
			},
		    rownumbers:true,idField: 'id',striped:true,border:false,
		    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
		    fitColumns:false,pagination:true,fit:true,
		    toolbar:'#toolbarId',
		    columns:[[    
		        {field:'id',checkbox:'true',width:'10%'},    
		        {field:'infoTitle',title:'标题',width:'40%'},    
		        {field:'infoKeyword',title:'关键字',width:'20%'},
		        {field:'infoPubuser',title:'发布人',width:'10%',
		        	formatter:funEmployeeMap
		        },    
		        {field:'infoPubtime',title:'发布时间',width:'15%'},  
		        {field:'infoOrder',title:'排序操作',width:'10%'}  
		    ]],
		    onLoadSuccess : function(data) {
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
		    	toUpDown(data,'list1');
		    },
		    onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			},
		    onDblClickRow:function(index, row){
				var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
				window.parent.addTab('信息浏览', url);
		    }
		});
	}else if(index == 1){
		//公告
		$('#list2').datagrid({    
		    url:'<%=basePath%>sys/noticeManage/showList.action', 
		    pageSize:20,
			pageList:[20,40,60,100],
		    queryParams: {
		    	'info.infoPubflag':1,
		    	'info.infoType': 2
			},
		    rownumbers:true,idField: 'id',striped:true,border:false,
		    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
		    fitColumns:false,pagination:true,fit:true,
		    toolbar:'#toolbarId',
		    columns:[[    
		        {field:'id',checkbox:'true',width:'10%'},    
		        {field:'infoTitle',title:'标题',width:'40%'},    
		        {field:'infoKeyword',title:'关键字',width:'20%'},
		        {field:'infoPubuser',title:'发布人',width:'10%',
		        	formatter:funEmployeeMap
		        }, 
		        {field:'infoPubtime',title:'发布时间',width:'15%'},   
		        {field:'infoOrder',title:'排序操作',width:'10%'}  
		    ]],
		    onLoadSuccess : function(data) {
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
		    	toUpDown(data,'list2');
		    },
		    onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			},
	 		onDblClickRow:function(index, row){
				var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
				window.parent.addTab('信息浏览', url);
		    }
		});
	}else if(index == 2){
		//新闻
		$('#list3').datagrid({    
		    url:'<%=basePath%>sys/noticeManage/showList.action', 
		    pageSize:20,
			pageList:[20,40,60,100],
		    queryParams: {
		    	'info.infoPubflag':1,
		    	'info.infoType': 3
			},
		    rownumbers:true,idField: 'id',striped:true,border:false,
		    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
		    fitColumns:false,pagination:true,fit:true,
		    toolbar:'#toolbarId',
		    columns:[[    
		        {field:'id',checkbox:'true',width:'10%'},    
		        {field:'infoTitle',title:'标题',width:'40%'},    
		        {field:'infoKeyword',title:'关键字',width:'20%'},
		        {field:'infoPubuser',title:'发布人',width:'10%',
		        	formatter:funEmployeeMap
		        }, 
		        {field:'infoPubtime',title:'发布时间',width:'15%'},
		        {field:'infoOrder',title:'排序操作',width:'10%'}  
		    ]],
		    onLoadSuccess : function(data) {
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
		    	toUpDown(data,'list3');
		    },
		    onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			},
	 		onDblClickRow:function(index, row){
				var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
				window.parent.addTab('信息浏览', url);
		    }
		});
	}else if(index == 5){
		//草稿
		$('#list4').datagrid({    
		    url:'<%=basePath%>sys/noticeManage/showList.action', 
		    pageSize:20,
			pageList:[20,40,60,100],
		    queryParams: {
		    	'info.infoPubflag':0
			},
		    rownumbers:true,idField: 'id',striped:true,border:false,
		    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
		    fitColumns:false,pagination:true,fit:true,
		    toolbar:'#toolbarId',
		    columns:[[    
		        {field:'id',checkbox:'true',width:'10%'},    
		        {field:'infoType',title:'类型',width:'10%',
		        	formatter:funinfoType
		        },    
		        {field:'infoTitle',title:'标题',width:'40%'},    
		        {field:'infoKeyword',title:'关键字',width:'20%'},
		        {field:'infoWirteuser',title:'撰写人',width:'10%',
		        	formatter:funEmployeeMap
		        },
		        {field:'updateTime',title:'撰写时间',width:'15%'},
		    ]],
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
		    onDblClickRow:function(index, row){
				var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
				window.parent.addTab('信息浏览', url);
		    }
		});
	}else if(index == 3){
		//医疗前沿
		$('#list5').datagrid({    
		    url:'<%=basePath%>sys/noticeManage/showList.action', 
		    pageSize:20,
			pageList:[20,40,60,100],
		    queryParams: {
		    	'info.infoPubflag':1,
		    	'info.infoType': 4
			},
		    rownumbers:true,idField: 'id',striped:true,border:false,
		    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
		    fitColumns:false,pagination:true,fit:true,
		    toolbar:'#toolbarId',
		    columns:[[    
		        {field:'id',checkbox:'true',width:'10%'},    
		        {field:'infoTitle',title:'标题',width:'40%'},    
		        {field:'infoKeyword',title:'关键字',width:'20%'},
		        {field:'infoWirteuser',title:'撰写人',width:'10%',
		        	formatter:funEmployeeMap
		        },
		        {field:'updateTime',title:'撰写时间',width:'15%'},
		        {field:'infoOrder',title:'排序操作',width:'10%'}  
		    ]],
		    onLoadSuccess : function(data) {
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
		    	toUpDown(data,'list5');
		    },
		    onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			},
		    onDblClickRow:function(index, row){
				var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
				window.parent.addTab('信息浏览', url);
		    }
		});
	}else if(index==4){
		//护理动态
		$('#list6').datagrid({    
		    url:'<%=basePath%>sys/noticeManage/showList.action', 
		    pageSize:20,
			pageList:[20,40,60,100],
		    queryParams: {
		    	'info.infoPubflag':1,
		    	'info.infoType': 5
			},
		    rownumbers:true,idField: 'id',striped:true,border:false,
		    checkOnSelect:true,selectOnCheck:false,singleSelect:true,
		    fitColumns:false,pagination:true,fit:true,
		    toolbar:'#toolbarId',
		    columns:[[    
		        {field:'id',checkbox:'true',width:'10%'},    
		        {field:'infoTitle',title:'标题',width:'40%'},    
		        {field:'infoKeyword',title:'关键字',width:'20%'},
		        {field:'infoWirteuser',title:'撰写人',width:'10%',
		        	formatter:funEmployeeMap
		        },
		        {field:'updateTime',title:'撰写时间',width:'15%'},
		        {field:'infoOrder',title:'排序操作',width:'10%'}  
		    ]],
		    onLoadSuccess : function(data) {
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
		    	toUpDown(data,'list6');
		    },
		    onBeforeLoad:function(param){
				$(this).datagrid('uncheckAll');
			},
		    onDblClickRow:function(index, row){
				var url="<%=basePath%>sys/noticeManage/contentView.action?info.id="+row.id;
				window.parent.addTab('信息浏览', url);
		    }
		});
	}
}

function add(){
	//调用bootstrap框架
	openNav('<%=basePath%>sys/noticeManage/noticeAdd.action', '文章发布', 'noticeaddid');
// 	if (window.parent.$('#tabs').tabs('exists','信息发布')){
// 		$.messager.confirm('友情提示', '信息窗口已存在，是否清除信息窗口内容?', function(r){
// 			if (r){
// 				window.parent.$('#tabs').tabs('select', '信息发布');//选中并刷新
// 				var currTab = window.parent.$('#tabs').tabs('getSelected');
// 				var url = $(currTab.panel('options').content).attr('src');
// 				if (url != undefined && currTab.panel('options').title != 'Home') {
// 					window.parent.$('#tabs').tabs('update', {
// 						tab : currTab,
// 						options : {
// 							content : createFrame(url)
// 						}
// 					});
// 				}
// 			}else{
// 				window.parent.$('#tabs').tabs('select', '信息发布');//选中
// 			}
// 		});
// 	} else {
<%-- 		var url="<%=basePath%>sys/noticeManage/noticeAdd.action"; --%>
// 		var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
// 		window.parent.$('#tabs').tabs('add',{
// 				title:'信息发布',
// 				content:content,
// 				closable:true
// 		});
// 	}
// 	window.parent.tabClose();
}

function edit(){
	var tab = $('#tt').tabs('getSelected');
	var index = $('#tt').tabs('getTabIndex',tab);
	if(index == 0){
		var row = $('#list1').datagrid('getSelected'); //获取当前选中行    
		edits(row);
	}else if(index == 1){
		var row = $('#list2').datagrid('getSelected'); //获取当前选中行    
		edits(row);
	}else if(index == 2){
		var row = $('#list3').datagrid('getSelected'); //获取当前选中行    
		edits(row);
	}else if(index == 5){
		var row = $('#list4').datagrid('getSelected'); //获取当前选中行    
		edits(row);
	}else if(index == 3){
		var row = $('#list5').datagrid('getSelected'); //获取当前选中行    
		edits(row);
	}else if(index == 4){
		var row = $('#list6').datagrid('getSelected'); //获取当前选中行    
		edits(row);
	}
}

/***
 * 在修改中抽出来的方法
 */
function edits(row){
	//调用bootstrap框架
	if(row){
		openNav('<%=basePath%>sys/noticeManage/noticeEdit.action?info.id='+row.id, '文章修改', 'noticeeditid');
	}else{
		$.messager.alert('提示','请选择要修改的记录！');
		close_alert();
	}
// 	if(row){
// 		if (window.parent.$('#tabs').tabs('exists','信息发布')){
// 			$.messager.confirm('友情提示', '发布窗口已存在，是否重置发布窗口内容?', function(r){
// 				if (r){
// 					window.parent.$('#tabs').tabs('select', '信息发布');//选中并刷新
// 					var currTab = window.parent.$('#tabs').tabs('getSelected');
<%-- 					var url="<%=basePath%>sys/noticeManage/noticeEdit.action?info.id="+row.id; --%>
// 					if (url != undefined && currTab.panel('options').title != 'Home') {
// 						window.parent.$('#tabs').tabs('update', {
// 							tab : currTab,
// 							options : {
// 								content : createFrame(url)
// 							}
// 						});
// 					}
// 				}else{
// 					window.parent.$('#tabs').tabs('select', '信息发布');//选中
// 				}
// 			});
// 		} else {
<%-- 			var url="<%=basePath%>sys/noticeManage/noticeEdit.action?info.id="+row.id; --%>
// 			var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
// 			window.parent.$('#tabs').tabs('add',{
// 					title:'信息发布',
// 					content:content,
// 					closable:true
// 			});
// 		}
// 		window.parent.tabClose();
// 	}else{
// 		$.messager.alert('友情提示','请选择要修改的数据!','info');
// 		close_alert();
// 	}
}


/***
 * 删除操作
 */
function del(){
	var tab = $('#tt').tabs('getSelected');
	var index = $('#tt').tabs('getTabIndex',tab);
	
	if(index == 0){
		var obj=$('#list1').datagrid('getChecked');
	}else if(index == 1){
		var obj=$('#list2').datagrid('getChecked');
	}else if(index == 2){
		var obj=$('#list3').datagrid('getChecked');
	}else if(index == 5){
		var obj=$('#list4').datagrid('getChecked');
	}else if(index == 3){
		var obj=$('#list5').datagrid('getChecked');
	}else if(index == 4){
		var obj=$('#list6').datagrid('getChecked');
	}
	var arr =new Array();
	if(obj.length>0){
		$.each(obj,function(i,n){
			arr[i]=n.id;
			j=i+1;
		});
		
		$.messager.confirm('确认对话框', '您想要删除'+j+'条记录吗？', function(r){
			if (r){
				$.ajax({
					url:'<%=basePath%>sys/noticeManage/del.action',
					type:'post',
					traditional:true,//数组提交解决方案
					data:{'ids':arr},
					dataType:'json',
					success:function(data){
						if(data.resCode == 0){
			 	    		$.messager.alert('友情提示','操作完成！');
			 	    		if(index == 0){
			 	    			$('#list1').datagrid('reload');
			 	    		}else if(index == 1){
			 	    			$('#list2').datagrid('reload');
			 	    		}else if(index == 2){
			 	    			$('#list3').datagrid('reload');
			 	    		}else if(index == 5){
			 	    			$('#list4').datagrid('reload');
			 	    		}else if(index == 3){
			 	    			$('#list5').datagrid('reload');
			 	    		}else if(index == 4){
			 	    			$('#list6').datagrid('reload');
			 	    		}
				    	}else{
				    		$.messager.alert('友情提示',data.resMsg,'error');
				    	}
					}
				});
			}
		});
	}else{
		$.messager.alert('友情提示','请勾选要删除的行!','info');
		close_alert();
	}
}

/***
 * 搜索条件
 */
function searchFrom(){
	var tab = $('#tt').tabs('getSelected');
	var index = $('#tt').tabs('getTabIndex',tab);
	var codes = $('#codes').textbox('getValue');	
	if(index == 0){
		//通知
		$('#list1').datagrid('load',{
			'info.infoType': 1,
			'info.infoPubflag':1,
			'info.infoBrev':codes
		});
	}else if(index == 1){
		//公告
		$('#list2').datagrid('load',{
			'info.infoPubflag':1,
			'info.infoType':2,
			'info.infoBrev':codes
		});
	}else if(index == 2){
		//新闻
		$('#list3').datagrid('load',{
			'info.infoPubflag':1,
			'info.infoType': 3,
			'info.infoBrev':codes
		});
	}else if(index == 5){
		//草稿
		$('#list4').datagrid('load',{
			'info.infoPubflag':0,
			'info.infoBrev':codes
		});
	}else if(index == 3){
		//医疗前沿
		$('#list5').datagrid('load',{
			'info.infoPubflag':1,
			'info.infoType': 4,
			'info.infoBrev':codes
		});
	}else if(index == 4){
		//医疗前沿
		$('#list6').datagrid('load',{
			'info.infoPubflag':1,
			'info.infoType': 5,
			'info.infoBrev':codes
		});
	}
}
/**
 * 重置
 * @author huzhenguo
 * @date 2017-03-17
 * @version 1.0
 */
function clears(){
	$('#codes').textbox('setValue','');
	searchFrom();
}

function toUpDown(data,param){
	var grid = $('#'+param);
	var options = grid.datagrid('getPager').data("pagination").options;
	var curr = options.pageNumber;
	var total = options.total;
	var pageSize = options.pageSize;
	var rows = data.rows;
	var map = null;
	map = new Map();
	var rowData = data.rows;
	for ( var i = 0; i < rows.length; i++) {
		var index = grid.datagrid('getRowIndex',rows[i]);
		var a = "";
		if (rows.length == 1) {//仅有一个父级不现实按钮
		} else if (curr == 1
				&& index == 0) {//第一行
			a = '<a class="downCls" onclick="toDown(\''+ rows[i].id+'\',\''+param+'\')" href="javascript:void(0)" style="height:20"></a>';
		} else if ((index + 1)
				+ ((curr - 1) * pageSize) == total) {//最后一行
			a = '<a class="upCls" onclick="toUp(\''+ rows[i].id+'\',\''+param+'\')" href="javascript:void(0)" style="height:20"></a>';
		} else {
			a = '<a class="upCls" onclick="toUp(\''+ rows[i].id+'\',\''+param+'\')" href="javascript:void(0)" style="height:20"></a>';
			a += '<a class="downCls" onclick="toDown(\''+ rows[i].id+'\',\''+param+'\')" href="javascript:void(0)" style="height:20"></a>';
		}
		grid.datagrid('updateRow',{
			index : index,
			row : {
				infoOrder : a
			}
		});
	}
	$('.upCls').linkbutton({text:'上移',plain:true,iconCls:'icon-up'}); 
	$('.downCls').linkbutton({text:'下移',plain:true,iconCls:'icon-down'});
}

//上移
function toUp(rowId,param) {
	editOrder(param,rowId, 1);
}
//下移
function toDown(rowId,param) {
	editOrder(param,rowId, 2);
}

//传向后台  移动
function editOrder(param,currentId, flag) {
	$.post("<%=basePath%>sys/noticeManage/editOrder.action", {
		"info.id" : currentId,
		"orderFlag" : flag
	}, function(result) {
		if (result.resCode == 0) {
			$('#'+param).datagrid("reload");
		} else {
			$.messager.alert('友情提示',result.resMsg,'warning');
		}
	});
}










//--------------------------------------------------------------------------渲染方法--------------------------------------------------//

function funformat(){
	$.ajax({
		url:'<%=basePath%>baseinfo/employee/getEmplMap.action',
		type:'post',
		async: false,
		success: function(data) {
			employeeMap = data;
		}
	});
}

function funinfoType(value,row,index){
	if(value == 1){
		return '通知';
	}else if(value == 2){
		return '公告';
	}else if(value == 3){
		return '新闻';
	}else if(value == 4){
		return '医疗前沿';
	}else if(value == 5){
		return '信息提醒';
	}
}

//渲染人员map
function funEmployeeMap(value,row,index){
	if(value!=null&&value!=''){
		return employeeMap[value];
	}
}
</script>
</head>
<body >   
    <div class="easyui-layout" data-options="fit:true" style="">
	    <div data-options="region:'north',border:false" style="height:35px;text-align:left;padding:1px;">
			<table style="width:100%;border:0px;padding:1px;">
				<tr>
					<td style="font-size:14px" >
						<input  id="codes" data-options="prompt:'请输入标题或关键字...'"  class="easyui-textbox"  />
						<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
						</shiro:hasPermission>
					</td>
				</tr>
			</table>
		</div>
		<div  data-options="region:'center',border:false">
			<div id="tt" class="easyui-tabs" data-options="pill:true,fit:true" style="width:100%;height:700;">   
			    <div title="院内通知">   
			        <table id="list1" style="width: 100%" data-options="fit:true"></table>
			    </div>   
			    <div title="院内公告">   
			        <table id="list2" style="width: 100%" data-options="fit:true"></table>
			    </div>   
			    <div title="院内新闻">   
			        <table id="list3" style="width: 100%" data-options="fit:true"></table>
			    </div>   
			     <div title="医疗前沿">   
			        <table id="list5" style="width: 100%" data-options="fit:true"></table>
			    </div> 
			     <div title="信息提醒">   
			        <table id="list6" style="width: 100%" data-options="fit:true"></table>
			    </div> 
			    <div title="草稿箱">   
			        <table id="list4" style="width: 100%" data-options="fit:true"></table>
			    </div>   
			</div> 
		</div> 
		<div id="toolbarId">
			<span style="margin: 0 5 0 0;">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			</span>
	    	<span style="margin: 0 5 0 0">
	    	<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
			</shiro:hasPermission>
			</span>
	    	<span style="margin: 0 5 0 0">
	    	<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
			</span> 
		</div> 
    </div>
</body> 
</html>