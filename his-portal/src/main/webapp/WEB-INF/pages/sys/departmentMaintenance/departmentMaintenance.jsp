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
		<div id="p" data-options="region:'west',split:true,tools:'#toolSMId'" title="分管科室维护" style="width:290px;padding:0px;overflow: hidden;">
			<div id="toolSMId">
				<a href="javascript:void(0)" onclick="refresh()" class="icon-reload"></a>
			</div>
			<div style="width:100%;height:100%;overflow-y: auto;">
				<ul id="tDt">数据加载中.....</ul>
			</div>
			<div id="tDtmm" class="easyui-menu" style="width:100px;">
				<shiro:hasPermission name="${menuAlias}:function:add"> 
					<div onclick="addOproom()" data-options="iconCls:'icon-add'" id="add">添加领导/负责人</div>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete"> 
					<div onclick="delOproom()" data-options="iconCls:'icon-remove'" id="remove">删除领导/负责人</div>
				</shiro:hasPermission>
			</div>	
		</div>
		<div data-options="region:'center'" style="width:100%;height:95%;border-top:0px;" >
			<form id="serachForm" style="width: 100%;height:7%;padding: 5px 0; box-sizing: border-box;">
			    <table style="width: 100%;height: 100%;">
					<tr>
					<td>
<!-- 						姓名:<input id="selName" class="easyui-combobox" style="width:120px"/> -->
						科室:<input id="deptCode" class="easyui-combobox" style="width:120px"/>
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
							<th data-options="field:'name',width:'10%'" >姓名</th>
							<%--<th data-options="field:'divisionName',width:'10%'">学部</th>--%>
							<th data-options="field:'deptName',width:'15%'">科室</th>
							<th data-options="field:'deptCode',width:'10%'">科室编号</th>
							<th data-options="field:'type',width:'10%'" formatter="formatType">类型</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias}:function:add">
					<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">关联科室</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">	
					<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
		</div>	
	</div>	
	<div id="dialog"></div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>	
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}'
var	havSelect;
// 	$('#selName').combobox({
// 		url : "<c:url value='/baseinfo/departmentMaintenance/queryEmployeeExtendList.action'/>",
// 		valueField : 'employeeJobNo',
// 		textField : 'employeeName',
// 		multiple : false
// 	});
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
$(function(){
	$('#list').datagrid({
		pagination: true,
		pageSize: 20,
		pageList: [10,20,30],
		url:'${pageContext.request.contextPath}/baseinfo/departmentMaintenance/queryDeptMaintenance.action',
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
        }
	})
})	

//加载关系树
	$('#tDt').tree({ 
    url:"<%=basePath %>baseinfo/departmentMaintenance/deptMaintenanceTree.action",
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
	},
	onContextMenu: function(e,node){//添加右键领导
		e.preventDefault();
		$(this).tree('select',node.target);
        if(node.id=="root"){//添加院领导
            $("#add").show();
            $("#remove").hide();
            $('#tDtmm').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        }else if(node.id=="rootD"){//添加部门领导
            $("#add").show();
            $("#remove").hide();
            $('#tDtmm').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        }else if(node.id=="r"){
            $('#tDtmm').menu('hide',{
                left: e.pageX,
                top: e.pageY
            });
        }else{
			$("#add").hide();
			$("#remove").show();
            $('#tDtmm').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
		}

	},
	// onLoadSuccess:function(node,data){
	// 	if(data.length>0){
	// 		$('#tDt').tree('collapseAll');
	// 	}
	// },
	onSelect: function(node){//点击节点
		$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
		 id = node.id;
		$('#list').datagrid('load', {
			treeCode: id
		});
		$('#treeId').val(node.id);  //拿到树的id
		$('#treeName').val(node.text);
// 		$('#selName').combobox('setValue','');
		$('#deptCode').combobox('setValue','');
	},
	onBeforeCollapse:function(node){
		if(node.id=="r"){
			return false;
		}
	}  
});
	function refresh(){//刷新树
   		$('#tDt').tree('options').url = "<%=basePath%>baseinfo/departmentMaintenance/deptMaintenanceTree.action";
		$('#tDt').tree('reload'); 
	}
	function delOproom(){//删除领导
		//选中要删除的行
	       var node = $('#tDt').tree('getSelected');
	       if(node){
	    	   $.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
					if (res){
						var account = node.id;
						$.ajax({
							url: "<c:url value='/baseinfo/departmentMaintenance/delName.action'/>?account="+account,
							type:'post',
							success: function() {
								$.messager.alert('提示','删除成功');
								searchFrom();
								refresh();
							},
							errror:function(){
								$.messager.alert('提示','删除失败，请重新再试！');
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
	function addOproom(){//添加
        var node = $('#tDt').tree('getSelected');
        var type = node.id;
   		Adddilog("添加领导/负责人",'<%=basePath%>baseinfo/departmentMaintenance/addDeptMaintenance.action?type='+type);
	}
	function add(){//关联科室
		var node = $('#tDt').tree('getSelected');
		if(node){
			if(node.id!='root'){
				Adddilog("关联科室",'<%=basePath%>baseinfo/departmentMaintenance/addDeptMaintenance.action?account='+id);
			}else{
				$.messager.alert('提示','请选择领导/负责人！');
			}
		}else{
			$.messager.alert('提示','请选择领导/负责人！');
			close_alert();
		}
	}
	//加载dialog
	function Adddilog(title, url) {
		$('#dialog').dialog({    
		    title: title,    
		    width: '30%',    
		    height:'40%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true   
		});    
	}
	//查询
	 function searchFrom(){
		var deptCode = $('#deptCode').combobox('getValue');
		if(deptCode!=null){
			$('#tDt').find('div.tree-node-selected').removeClass('tree-node-selected');
			var node=$('#tDt').tree('find','root');
			$('#tDt').tree('select',node.target);
		}
		$('#list').datagrid('load',{
// 			treeCode:$('#selName').combobox('getValue'),
			deptCode:deptCode
		});
	 }
	 // 重置
	function clear(){
// 		$('#selName').combobox('setValue','');
		$('#deptCode').combobox('setValue','');
		searchFrom();
	}
	//删除
	function del(){
		//选中要删除的行
       var iid = $('#list').datagrid('getChecked');
       if (iid.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					$.messager.progress({text:'删除中，请稍后...',modal:true});
					var ids = '';
					for(var i=0; i<iid.length; i++){
						if(ids!=''){
							ids += "','";
						}
						ids += iid[i].id;
					};
					$.ajax({
						url: "<c:url value='/baseinfo/departmentMaintenance/delDeptMaintenance.action'/>?ids="+ids,
						type:'post',
						success: function() {
							$.messager.progress('close');
							$.messager.alert('提示','删除成功');
							$('#list').datagrid('reload');
							refresh();
						},
						errror:function(){
							$.messager.alert('提示','删除失败，请重新再试！');
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


    function formatType(val,row,index){
        if (0 == val){
            return '院领导';
        } else {
            return '负责人';
        }
    }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>