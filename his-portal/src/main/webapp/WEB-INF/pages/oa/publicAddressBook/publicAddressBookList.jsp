<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>
	<div id="divLayout" class="easyui-layout" class="easyui-layout" fit=true style="width:100%;height:100%;overflow-y: auto;">
		<div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="公共通讯录" style="width:290px;padding:0px;overflow: hidden;">
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
				<a href="javascript:void(0)" onclick="collapseAll()" class="icon-fold"></a>
				<a href="javascript:void(0)" onclick="expandAll()" class="icon-open"></a>
			</div>
			<div style="width:100%;height:100%;overflow-y: auto;">
				<ul id="tDt">数据加载中.....</ul>
			</div>
			<div id="tDtmm" class="easyui-menu" style="width:100px;">
				<shiro:hasPermission name="${menuAlias}:function:add"> 
					<div onclick="addOproom()" data-options="iconCls:'icon-add'" id="add">添加菜单</div>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:edit"> 
					<div onclick="editOproom()" data-options="iconCls:'icon-edit'" id="edit">修改菜单</div>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete"> 
					<div onclick="delOproom()" data-options="iconCls:'icon-remove'" id="remove">移除菜单</div>
				</shiro:hasPermission>
			</div>	
		</div>
		<div data-options="region:'center'" style="width:100%;height:95%;border-top:0px;" >
			<form id="serachForm" style="width: 100%;height:7%;padding: 5px 0; box-sizing: border-box;">
			    <table style="width: 100%;height: 100%;">
					<tr>
					<td>
						所属院区:<input id="areaCode" class="easyui-combobox" style="width:120px"/>
						楼号:<input id="noString" class="easyui-combobox" style="width:120px"/>
						楼层:<input id="floor" class="easyui-combobox" style="width:120px"/>
						类别名称:<input id="typeName" class="easyui-combobox" style="width:120px"/>
						科室名称:<input id="deptName" class="easyui-combobox" style="width:120px"/>
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="margin-left: 8px">查询</a>
	 						<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
					</tr>
				</table>
			</form>
			<div style="height:93%;border-top:0px;overflow: hidden;" >
				<input type="hidden" id="treeId" name="treeId">
				<input type="hidden" id="treeName" name="treeName">
				<table id="list" class="easyui-datagrid" style="width:100%"
						data-options="method:'post',rownumbers:true,idField:'id',striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId',fit:true">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true" ></th>
							<th data-options="field:'areaName',width:'10%'" >院区</th>
							<th data-options="field:'buildingName',width:'9%' ">楼号</th>
							<th data-options="field:'floorName',width:'9%'" >楼层</th>
							<th data-options="field:'floorType',width:'9%'">类别名称</th>
							<th data-options="field:'floorDept',width:'10%'">科室名称</th>
							<th data-options="field:'name',width:'10%'" >工作站</th>
							<th data-options="field:'phone',width:'10%'" >内线</th>
							<th data-options="field:'minPhone',width:'10%'" >移动电话</th>
							<th data-options="field:'officePhone',width:'10%'" >小号</th>
							<th data-options="field:'status',formatter:functionType,width:'5%'">状态</th>
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
	</div>	
	<div id="dialog"></div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>	
<script type="text/javascript">
var menuAlias = '${menuAlias}'
$(function(){
	$('#list').datagrid({
		pagination: true,
		pageSize: 20,
		pageList: [20,30,50,80,100],
		url:'${pageContext.request.contextPath}/oa/publicAddressBook/queryVoList.action',
   		onBeforeLoad:function (param) {
			$('#list').datagrid('clearChecked');
			$('#list').datagrid('clearSelections');
        },
		onLoadSuccess: function (data) {//默认选中
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
			//合并单元格
			$('#list').datagrid("autoMergeCells", ['floorDept']);
        }
	})
	comboboxAll();
	/**
	 * 合并单元格
	 */
	 $.extend($.fn.datagrid.methods, {
	        autoMergeCells: function (jq, fields) {
	            return jq.each(function () {
	                var target = $(this);
	                if (!fields) {
	                    fields = target.datagrid("getColumnFields");
	                }
	                var rows = target.datagrid("getRows");
	                var i = 0,
	                j = 0,
	                temp = {};
	                for (i; i < rows.length; i++) {
	                    var row = rows[i];
	                    j = 0;
	                    for (j; j < fields.length; j++) {
	                        var field = fields[j];
	                        var tf = temp[field];
	                        if (!tf) {
	                            tf = temp[field] = {};
	                            tf[row[field]] = [i];
	                        } else {
	                            var tfv = tf[row[field]];
	                            if (tfv) {
	                                tfv.push(i);
	                            } else {
	                                tfv = tf[row[field]] = [i];
	                            }
	                        }
	                    }
	                }
	                $.each(temp, function (field, colunm) {
	                    $.each(colunm, function () {
	                        var group = this;
	                        if (group.length > 1) {
	                            var before,
	                            after,
	                            megerIndex = group[0];
	                            for (var i = 0; i < group.length; i++) {
	                                before = group[i];
	                                after = group[i + 1];
	                                if (after && (after - before) == 1) {
	                                    continue;
	                                }
	                                var rowspan = before - megerIndex + 1;
	                                if (rowspan > 1) {
	                                    target.datagrid('mergeCells', {
	                                        index: megerIndex,
	                                        field: field,
	                                        rowspan: rowspan
	                                    });
	                                }
	                                if (after && (after - before) != 1) {
	                                    megerIndex = after;
	                                }
	                            }
	                        }
	                    });
	                });
	            });
	        }
	    });
})	
	//搜索下拉框
	function comboboxAll(){
		//所属院区
		$('#areaCode').combobox({
			url : "<c:url value='/oa/publicAddressBook/queryVoAreaList.action'/>",
			valueField : 'name',
			textField : 'name',
			multiple : false
		});	
		//楼号
		$('#noString').combobox({
			url : "<c:url value='/oa/publicAddressBook/queryVoNoStringList.action'/>",
			valueField : 'name',
			textField : 'name',
			multiple : false
		});	
		//楼层
		$('#floor').combobox({
			url : "<c:url value='/oa/publicAddressBook/queryVoFloorList.action'/>",
			valueField : 'name',
			textField : 'name',
			multiple : false
		});
		//类别名称
		$('#typeName').combobox({
			url : "<c:url value='/oa/publicAddressBook/queryVoTypeList.action'/>",
			valueField : 'name',
			textField : 'name',
			multiple : false
		});
		//科室
		$('#deptName').combobox({
			url : "<c:url value='/oa/publicAddressBook/queryVoDeptList.action'/>",
			valueField : 'name',
			textField : 'name',
			multiple : false,
		});
	}
//加载关系树
	$('#tDt').tree({ 
    url:"<%=basePath %>oa/publicAddressBook/publicBookTree.action",
    method:'get',
    animate:false,
    lines:true,
    formatter:function(node){//统计节点总数
		var s = node.text;
		if(node.children.length>0){
			if (node.children){
				s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
			}
		}
		return s;
	},onContextMenu: function(e,node){//添加右键菜单
		e.preventDefault();
		$(this).tree('select',node.target);
		if(node.id=="root"){
			$("#add").show();
			$("#edit").hide();
			$("#remove").hide();
		}
		if(node.attributes.nodeType=="55"){
			$("#add").hide();
			$("#edit").show();
			$("#remove").show();
		}
		if(node.attributes.nodeType=="00" || node.attributes.nodeType=="11" || node.attributes.nodeType=="22" || node.attributes.nodeType=="33" || node.attributes.nodeType=="44"){
			$("#add").show();
			$("#edit").show();
			$("#remove").show();
		}
		$('#tDtmm').menu('show',{
			left: e.pageX,
			top: e.pageY
		});
	},onLoadSuccess:function(node,data){
		if(data.length>0){
			$('#tDt').tree('collapseAll');
		}
	},
	onSelect: function(node){//点击节点
		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		 id = node.id;
		 var nodeType=node.attributes.nodeType;
		$('#list').datagrid('load', {
			id: id,
			nodeType:nodeType
		});
		$('#treeId').val(node.id);  //拿到树的id
		$('#treeName').val(node.text);
	},onBeforeCollapse:function(node){
		if(node.id=="root"){
			return false;
		}
	}  
});
	function refresh(){//刷新树
   		$('#tDt').tree('options').url = "<%=basePath%>oa/publicAddressBook/publicBookTree.action";
		$('#tDt').tree('reload'); 
	}
   	function expandAll(){//展开树
		$('#tDt').tree('expandAll');
	}
   	function collapseAll(){//关闭树
		$('#tDt').tree('collapseAll');
	}
	function addOproom(){//添加
		var id = $("#tDt").tree('getSelected').id
   		Adddilog("添加菜单",'<%=basePath%>oa/publicAddressBook/addPublicAddressBook.action?id='+id,"40%","30%");
	}
	function editOproom(){//修改
		var id = $("#tDt").tree('getSelected').id
   		Adddilog("修改菜单",'<%=basePath%>oa/publicAddressBook/editPublicAddressBook.action?id='+id,"40%","30%");
	}
	function delOproom(){//移除
		var id = $("#tDt").tree('getSelected').id
  		$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
  			if (res){
				$.ajax({
					url: '<%=basePath%>oa/publicAddressBook/delMenu.action?id='+id,
					type:'post',
					success: function() {
						$.messager.alert("提示",'删除成功!');
						$('#tDt').tree('reload');
						searchFrom();
						comboboxAll();
					}
				});										
			}
		});
	}
	//加载dialog
	function Adddilog(title, url, w, h) {
		$('#dialog').dialog({    
		    title: title,    
		    width: w,    
		    height: h,    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		});    
	}
	//查询
	 function searchFrom(){
		$('#list').datagrid('load',{
			areaCode:$('#areaCode').combobox('getValue'),
			noString:$('#noString').combobox('getValue'),
			floor:$('#floor').combobox('getValue'),
			typeName:$('#typeName').combobox('getValue'),
			deptCode:$('#deptName').combobox('getValue')
		});
	 }
	 // 重置
	function clear(){
		$('#serachForm').form('reset');
		searchFrom();
	}
	//状态
	function functionType(value,row,index){
		if(value==0){
			return "普通";
		}else{
			return "常用";
		}
	}
	//删除工作站
	function del(){
	//选中要删除的行
       var iid = $('#list').datagrid('getChecked');
       if (iid.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += ',';
						}
						ids += iid[i].id;
					};
					$.ajax({
						url: "<c:url value='/oa/publicAddressBook/delWork.action'/>?id="+ids,
						type:'post',
						success: function() {
							$.messager.alert('提示','删除成功');
							$('#tDt').tree('reload');
							searchFrom();
							comboboxAll();
						}
					});	
					
				}
	        });
       }else{
    	    $.messager.alert('提示信息','请选择要删除的信息！');
  	    	 setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
  	     }	
	}
	//修改工作站	
	function edit(){
		var row = $('#list').datagrid('getSelected');
		  if(row != null){
		   		Adddilog("修改工作站",'<%=basePath%>oa/publicAddressBook/editPublicAddressBook.action?id='+row.id,"40%","55%");
               $('#list').datagrid('reload');
		  }else{
	   			$.messager.alert('提示','请点中要修改信息！');	
				close_alert();
   		  }
	}
	//添加工作站
	function add(){
		var node = $('#tDt').tree('getSelected');
		if(node){
			if(node.attributes.nodeType!='55'){
				Adddilog("添加工作站",'<%=basePath%>oa/publicAddressBook/addPublicAddressBook.action?id='+node.id,"40%","30%");
			}else{
				$.messager.alert('提示','不能进行添加操作！');
			}
		}else{
			$.messager.alert('提示','请选择菜单！');	
			close_alert();
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>