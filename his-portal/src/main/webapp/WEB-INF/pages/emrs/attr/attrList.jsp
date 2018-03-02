<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>系统控件维护</title>
	<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<style type="text/css">
		.window .panel-header .panel-tool a{
  	  		background-color: red;	
		}
	</style>
	<script type="text/javascript">
	var type = 1;//控件类型  tabs控制
	//页面加载
	$(function(){
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			url: '<%=basePath %>emrs/attr/queryAttr.action?menuAlias=${menuAlias}',
			queryParams: {
				type: '1'
			},
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
			},
			onLoadSuccess : function(data){
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
				if(data.total == 0){
					$.messager.alert("提示","当前分类下还没有控件，请添加!!");
					setTimeout(function(){$(".messager-body").window('close')},3500);
				}
			},
			onDblClickRow : function(rowIndex, rowData){
				var tab = $('#tt').tabs('getSelected');
				var index = $('#tt').tabs('getTabIndex',tab);
				type = index + 1;
				Adddilog('查看系统控件','<%=basePath %>emrs/attr/toAttrView.action?menuAlias=${menuAlias}&idd='+rowData.attrId+'&type='+type);
			}
		})
		$('#list').datagrid('hideColumn','attrNotnull');
		$('#list').datagrid('hideColumn','attrMustSelect');
		$('#list').datagrid('hideColumn','attrStatflg');
		$('#list').datagrid('hideColumn','attrPrintflg');
		$('#list').datagrid('hideColumn','attrLength');
		$('#list').datagrid('hideColumn','attrPrecision');
		$('#list').datagrid('hideColumn','attrDateformat');
		$('#list').datagrid('hideColumn','attrValidup');
		$('#list').datagrid('hideColumn','attrValiddown');
	//知识库选项卡
	$('#tt').tabs({    
    	border:false,    
    	onSelect:function(title,index){    
    		type = index + 1;
    		$('#list').datagrid('unselectAll');
    		$('#list').datagrid('uncheckAll');
    		if(type == 1 || type == 2 || type == 3){//单选，复选，有无选
    			$('#list').datagrid('hideColumn','attrNotnull');
    			$('#list').datagrid('hideColumn','attrMustSelect');
    			$('#list').datagrid('hideColumn','attrStatflg');
    			$('#list').datagrid('hideColumn','attrPrintflg');
    			$('#list').datagrid('hideColumn','attrLength');
    			$('#list').datagrid('hideColumn','attrPrecision');
    			$('#list').datagrid('hideColumn','attrDateformat');
    			$('#list').datagrid('hideColumn','attrValidup');
    			$('#list').datagrid('hideColumn','attrValiddown');
    			
    			$('#list').datagrid('showColumn','options');
    		}
    		if(type == 4 || type == 5){//文本，多行文本
    			$('#list').datagrid('showColumn','attrNotnull');
    			$('#list').datagrid('showColumn','attrMustSelect');
    			$('#list').datagrid('showColumn','attrStatflg');
    			$('#list').datagrid('showColumn','attrPrintflg');
    			
    			$('#list').datagrid('hideColumn','options');
    			$('#list').datagrid('hideColumn','attrLength');
    			$('#list').datagrid('hideColumn','attrPrecision');
    			$('#list').datagrid('hideColumn','attrDateformat');
    			$('#list').datagrid('hideColumn','attrValidup');
    			$('#list').datagrid('hideColumn','attrValiddown');
    		}
    		if(type == 6){//数字
    			$('#list').datagrid('hideColumn','attrNotnull');
    			$('#list').datagrid('hideColumn','attrMustSelect');
    			$('#list').datagrid('hideColumn','attrStatflg');
    			$('#list').datagrid('hideColumn','attrPrintflg');
    			$('#list').datagrid('hideColumn','options');
    			$('#list').datagrid('hideColumn','attrDateformat');
    			
    			$('#list').datagrid('showColumn','attrLength');
    			$('#list').datagrid('showColumn','attrPrecision');
    			$('#list').datagrid('showColumn','attrValidup');
    			$('#list').datagrid('showColumn','attrValiddown');
    		}
    		if(type == 7){//时间
    			$('#list').datagrid('hideColumn','attrNotnull');
    			$('#list').datagrid('hideColumn','attrMustSelect');
    			$('#list').datagrid('hideColumn','attrStatflg');
    			$('#list').datagrid('hideColumn','attrPrintflg');
    			$('#list').datagrid('hideColumn','attrDateformat');
    			$('#list').datagrid('hideColumn','attrLength');
    			$('#list').datagrid('hideColumn','attrPrecision');
    			$('#list').datagrid('hideColumn','options');
    			
    			$('#list').datagrid('showColumn','attrValidup');
    			$('#list').datagrid('showColumn','attrValiddown');
    		}
    		
    		$('#list').datagrid('load',{
    			type: type
			});
    	}    
	});
	})
	function add(){//添加
		Adddilog('添加系统控件','<%=basePath %>emrs/attr/toAddView.action?menuAlias=${menuAlias}&type='+type);
	}
	function edit(){//修改
		var row = $("#list").datagrid("getSelected");  
	    if(row == undefined || row == null){
	    	$.messager.alert("操作提示","请选择一条记录！");
	    	setTimeout(function(){$(".messager-body").window('close')},3500);
	       	return null;
	    }else{ 
			Adddilog('修改系统控件','<%=basePath %>emrs/attr/toEditView.action?menuAlias=${menuAlias}&idd='+row.attrId+'&type='+type);
	    }
	}
	function del(){//删除
		var rows = $('#list').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	                        
			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var idd = '';
					
					for(var i=0; i<rows.length; i++){
						if(idd!=''){
							idd += ',';
						}
						idd += rows[i].attrId;
					};
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$.ajax({
						url: '<%=basePath %>emrs/attr/remove.action?idd='+idd,
						type:'post',
						success: function() {
							$.messager.progress('close');
							$.messager.alert('提示','删除成功');
							$('#list').datagrid('reload');
							rows.length = 0;
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请勾选要删除的控件');
			setTimeout(function(){$(".messager-body").window('close')},3500);
			}
		}
	/**
	 * 动态添加LayOut
	 * @param title 标签名称
	 * @param url 跳转路径
	 */
		function AddOrShowEast(title, url) {
			var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','east').panel({
                       href:url 
                });
			}else{//打开新面板
				$('#divLayout').layout('add', {
					region : 'east',
					width : 680,
					split : true,
					href : url,
					closable : true
				})
			}
		}
		//条件查询
		function searchFrom(){
			var queryName =  $('#queryName').val();
			}
		//加载模式窗口
		function Adddilog(title, url) {
			$('#temWins').dialog({    
			    title: title,    
			    width: '40%',    
			    height: '60%',    
			    closed: false,    
			    cache: false,    
			    href: url,    
			    modal: true,
			   });    
		}
		
		//书写类型  0不限次书写1仅首次单次书写2单次书写
		function formatyesorno(val,row,index){
			if(val == '0'){
				return '否'
			}
			if(val == '1'){
				return '是'
			}
		}
</script>  
</head>
<body class="easyui-layout"> 
<!-- 系统控件维护 -->
	<div id="divLayout" class="easyui-layout" style="width:100%;height:100%;overflow-y: auto;"fit=true>
  		<div data-options="region:'north',split:false,border:false" style="width:100%;height:auto;overflow: hidden;">
	   		<div id="tt" class="easyui-tabs" data-options="border:false" style="width:100%;height:29px;text-align: center;">
				<div title="单选" data-options="closable:false">
	    		</div>   
	   			<div title="多选" data-options="closable:false">   
	    		</div>   
	   			<div title="有无选" data-options="closable:false">   
	    		</div>   
	    		<div title="录入提示" data-options="closable:false">   
	    		</div>   
	    		<div title="多行文本" data-options="closable:false">   
	    		</div>   
	    		<div title="数字" data-options="closable:false">   
	    		</div>   
	    		<div title="时间" data-options="closable:false">   
	    		</div>   
			</div>   
   		</div>  
    	<div data-options="region:'center',split:true,border:false" style="width:100%;height: 100%">
			<table id="list" style="width:100%;" data-options="fit:true,url:'<%=basePath %>emrs/attr/queryAttr.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true" ></th>
						<th data-options="field:'attrCode',width:'9%',align:'center'">控件代码</th>
						<th data-options="field:'attrName',width:'9%',align:'center'">控件名称</th>
						<th data-options="field:'inputcode',width:'9%',align:'center'">控件自定义码</th>
						<th data-options="field:'attrPrefix',width:'9%',align:'center'">前缀</th>
						<th data-options="field:'attrSuffix',width:'9%',align:'center'">后缀</th>
						<th data-options="field:'options',width:'35%',align:'center'">选项</th>
						<th data-options="field:'attrNotnull',width:'9%',align:'center',formatter:formatyesorno">是否必选必填</th>
						<th data-options="field:'attrMustSelect',width:'9%',align:'center',formatter:formatyesorno">是否必须选择操作</th>
						<th data-options="field:'attrStatflg',width:'9%',align:'center',formatter:formatyesorno">是否用于科研</th>
						<th data-options="field:'attrPrintflg',width:'9%',align:'center',formatter:formatyesorno">是否打印</th>
						<th data-options="field:'attrLength',width:'9%',align:'center'">长度</th>
						<th data-options="field:'attrPrecision',width:'9%',align:'center'">小数位</th>
						<th data-options="field:'attrDateformat',width:'10%',align:'center'">时间格式</th>
						<th data-options="field:'attrValidup',width:'10%',align:'center'">有效范围上限</th>
						<th data-options="field:'attrValiddown',width:'10%',align:'center'">有效范围下限</th>
					</tr>
				</thead>
			</table>
   		</div>
   		<div id="toolbarId">
			<shiro:hasPermission name="${menuAlias}:function:add">
				<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:edit">
				<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>	
			</shiro:hasPermission>
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
		</div>
	</div>
	
	
<div id="temWins"></div>
</body> 
</html>