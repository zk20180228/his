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
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
</head>		<!--  类别为2，4的科室树  -->
<body>
	<div>
		<div style="padding:10px">
    		<div id='d_item' style="width: 100%">
    			<div class='easyui-panel' style='padding: 5px 0px 5px 5px; overflow: hidden'>
    				<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'科室名'" style="width: 150px;"/>&nbsp;
   					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
					<a href='javascript:d_save();' class='easyui-linkbutton' data-options="iconCls:'icon-save'">保存</a>
				 	<a href='javascript:void(0);' onclick='closeDialog()' class='easyui-linkbutton' data-options="iconCls:'icon-cancel'">关闭</a>&nbsp;&nbsp;
					<a class='easyui-linkbutton' onclick='refresh()' data-options="iconCls:'icon-reload'"></a>
					<a class='easyui-linkbutton' onclick='collapseAll()' data-options="iconCls:'icon-fold'"></a>
					<a class='easyui-linkbutton' onclick='expandAll()' data-options="iconCls:'icon-open'"></a>
				</div>
				<div id='treeDiv' style="height:90%; width: 100%;overflow-y: auto;">
					<ul id='tDt'></ul>
				</div>
			</div>
	    </div>
	</div>

<script type="text/javascript">

<%--*** 类别:1药品;2专科;3结算类别;4特定收费窗口;5挂号级别   ***--%>
var dialog_itemType='${dialogItemType}';
$(function(){
	 bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
	if(dialog_itemType==2){
		 d_fundept();
	}else if(dialog_itemType==4){
		 d_charge();
	}
})

// drug dept settlement charge register

<%--*** 2  科室级别;添加数据  *****************************************************************************************************--%>
function d_fundept(){
	$('#tDt').tree({
		url:'<%=basePath%>drug/specialDispensingtable/queryDeptData.action?treeAll='+false,
		method : 'get',
		animate : true,
		lines : true,
		checkbox:true,
		onlyLeafCheck: true,
		cascadeCheck:false,
		onBeforeCollapse : function(node) {
			if ($('#tDt').tree('isLeaf',node) && node.id == 1) {
				return false;
			}
		},
		onSelect:function(node){
			$('#tDt').tree('check',node.target);
		},onBeforeCheck:function(node,checked){
			if("${isupdata}"==1){
				var nood = $('#tDt').tree('getChecked');
				if(nood.length >=1 &&node.id != nood[0].id){
					$.messager.alert('提示','只能选择一个科室！');
					setTimeout(function(){$(".messager-body").window('close')},1500);
					return false;
				}
			}
		}
	});
}
<%--*** 4 特定收费窗口;添加数据  *****************************************************************************************************--%>
function d_charge(){
	$('#tDt').tree({
		url:'<%=basePath%>drug/specialDispensingtable/queryChargeData.action',
		method : 'get',
		animate : true,
		lines : true,
		checkbox:true,
		onlyLeafCheck: true,
		cascadeCheck:false,
		onBeforeCollapse : function(node) {
			if ($('#tDt').tree('isLeaf',node) && node.id == 1) {
				return false;
			}
		},
		onSelect:function(node){
			$('#tDt').tree('check',node.target);
		},onBeforeCheck:function(node,checked){
			if("${isupdata}"==1){
				var nood = $('#tDt').tree('getChecked');
				if(nood.length >=1 &&node.id != nood[0].id){
					$.messager.alert('提示','只能选择一个科室！');
					setTimeout(function(){$(".messager-body").window('close')},1500);
					return false;
				}
			}
		}
	});
}

<%--*** ---判断---判断---保存  ***--%>
function d_save(){
	if(dialog_itemType==2){
		d_saveDept();
	}else if(dialog_itemType==4){
		d_saveCharge();
	}
}
/**
 * 关闭窗口
 * 
 */
function closeDialog(){
	window.close();
}
<%--***	---判断---保存		将选择结算类别添加到项目名称和项目编码  ********************************************************--%>
//2专科  
function d_saveDept(){
	var nodes = $('#tDt').tree('getChecked');
// 	var isupdata="${isupdata}";
	 if("${isupdata}"==1){
		 if(nodes!=null&&nodes.length==1){
			 var rev="[";		
 			 $.each(nodes,function(i,n){
 			  	if(rev.length>1){
 			  		rev+=",";
 			  	}
 			  	rev+="{'id':'"+n.id+"','name':'"+n.text+"'}";
 			  }); 
 			rev+="]";
 			 var str="";
			 $.each(nodes,function(i,n){
				 if(str.length>0){
					 str+=",";
				 }
				str+=n.text;
			 });
			 window.opener.setValue(rev,str);
			 window.close();
		 }else{
 			 $.messager.alert('友情提示','只允许选择一条数据');
 			setTimeout(function(){$(".messager-body").window('close')},1500);
 		 }
	 }else{
		 if(nodes!=null){
	 			var rev="[";		
	 			 $.each(nodes,function(i,n){
	 			  	if(rev.length>1){
	 			  		rev+=",";
	 			  	}
	 			  	rev+="{'id':'"+n.id+"','name':'"+n.text+"'}";
	 			  }); 
	 			rev+="]";
	 			 var str="";
				 $.each(nodes,function(i,n){
					 if(str.length>0){
						 str+=",";
					 }
					str+=n.text;
				 });
				 window.opener.setValue(rev,str);
				 window.close();
	 		 }else{
	 			 $.messager.alert('友情提示','请选择要添加的数据');
	 			setTimeout(function(){$(".messager-body").window('close')},1500);
	 		 }
	 }
}

<%--*** ---判断---保存	 将选择特定收费窗口添加到项目名称和项目编码  *******************--%>
// 4 特定收费窗口
function d_saveCharge(){
	var nodes = $('#tDt').tree('getChecked');
// 	var isupdata="${isupdata}";
	 if("${isupdata}"==1){
		 if(nodes!=null&&nodes.length==1){
			 var rev="[";		
 			 $.each(nodes,function(i,n){
 			  	if(rev.length>1){
 			  		rev+=",";
 			  	}
 			  	rev+="{'id':'"+n.id+"','name':'"+n.text+"'}";
 			  }); 
 			rev+="]";
 			 var str="";
			 $.each(nodes,function(i,n){
				 if(str.length>0){
					 str+=",";
				 }
				str+=n.text;
			 });
			 window.opener.setValue(rev,str);
			 window.close();
		 }else{
 			 $.messager.alert('友情提示','只允许选择一条数据');
 			setTimeout(function(){$(".messager-body").window('close')},1500);
 		 }
	 }else{
		 if(nodes!=null){
	 			var rev="[";		
	 			 $.each(nodes,function(i,n){
	 			  	if(rev.length>1){
	 			  		rev+=",";
	 			  	}
	 			  	rev+="{'id':'"+n.id+"','name':'"+n.text+"'}";
	 			  }); 
	 			rev+="]";
	 			 var str="";
				 $.each(nodes,function(i,n){
					 if(str.length>0){
						 str+=",";
					 }
					str+=n.text;
				 });
				 window.opener.setValue(rev,str);
				 window.close();
	 		 }else{
	 			 $.messager.alert('友情提示','请选择要添加的数据');
	 			setTimeout(function(){$(".messager-body").window('close')},1500);
	 		 }
	 }
}

/** 
 * 
 *科室树查询 
 */
function searchTreeNodes(){
         var searchText = $('#searchTreeInpId').textbox('getValue');
         $("#tDt").tree("search", searchText);
   }

//科室部门树操作
function refresh() {//刷新树
	$('#searchTreeInpId').textbox('setValue','');
	$('#tDt').tree('reload');
}
function expandAll() {//展开树
	$('#tDt').tree('expandAll');
}
function collapseAll() {//关闭树
	$('#tDt').tree('collapseAll');
}



</script>
</body>
</html>	