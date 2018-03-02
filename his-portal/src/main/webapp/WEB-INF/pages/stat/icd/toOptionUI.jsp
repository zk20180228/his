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
.tree-title {
    font-size: 14px;
    }

</style>
<title>icd-10编码分类维护</title>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true" >   
	<div data-options="region:'west',title:'ICD-10编码细分类',split:false" style="width:24%;">
		<ul id="icdAssorts"></ul>  
	</div>   
	<div data-options="region:'center',title:'ICD-10编码列表',border: false,split:false" style="width:78%;">
		<div class="easyui-layout" data-options="fit:true" > 
			<div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap">查询条件：
							<input  id="icdCode" data-options="prompt:'ICD-10编码'"  class="easyui-textbox"  style="width: 200px"/>
							<a href="javascript:void(0)"  onclick="searchICD()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							<a href="javascript:void(0)"  onclick="assortLink()" class="easyui-linkbutton" data-options="iconCls:'icon-add'"  >关联细分类</a>
						</td>
					</tr>
				</table>
			</div> 

			<div data-options="region:'center',border: false" style="width:100%;">
				<div id="divLayout" class="easyui-layout" data-options="fit:true" >   
						<div data-options="region:'center',border: false" style="width:80%;">
								<table id="list" class="easyui-datagrid">
									<thead>
										<tr>
											<th data-options="field:'id',checkbox:true,align:'center'" ></th>
											<th data-options="field:'icdCode',align:'center'"  style="width:20%;" >诊断码</th>
											<th data-options="field:'icdName',align:'center'"  style="width:25%;" >诊断名称</th>
											<th data-options="field:'assort_Name',align:'center'"  style="width:25%;">分类名称</th>
										</tr>
									</thead>
								</table>
						</div>
						<div data-options="region:'east',title:'编码细分类添加',collapsible:true" style="width:20%;">
								<form id="ff" method="post" style="width:100%;height:500px;">   
								    <div style="margin-top: 20px;margin-left: 5px;">   
								        <label for="name">名称:</label>   
								        <input class="easyui-validatebox" type="text" id="assort_Name" name="assort_Name" data-options="required:true" />   
								    </div>   
									<div>
										<a href="javascript:void(0)"  onclick="assortAdd()" class="easyui-linkbutton" iconCls="icon-add" style="margin-left:100px;margin-top: 20px;">保存</a>
									</div>
								</form>  
						</div>
				</div>
			</div>
		</div>
	</div>

</div>
</body>
</html>
<script type="text/javascript">

	$(function(){
		//加载icd编码细分类tree
		$('#icdAssorts').tree({    
		    url:'${pageContext.request.contextPath}/icdAssort/icdAssortTree.action'
		});  
		
		$("#list").datagrid({
			method: 'post',
			url:"${pageContext.request.contextPath}/icdAssort/icdList.action",
			fit: true,
			remoteSort: false,
			pagination: true,
			pageSize: 300,
			idField:'id',
			fitColumns:true,
			rownumbers:true,
			pageList: [20, 30, 50, 100,300,500],
			onLoadSuccess: function(data) {
				//分页工具栏作用提示
				var pager = $(this).datagrid('getPager');
				var aArr = $(pager).find('a');
				var iArr = $(pager).find('input');
				$(iArr[0]).tooltip({
					content: '回车跳转',
					showEvent: 'focus',
					hideEvent: 'blur',
					hideDelay: 1
				});
				for(var i = 0; i < aArr.length; i++) {
					$(aArr[i]).tooltip({
						content: toolArr[i],
						hideDelay: 1
					});
					$(aArr[i]).tooltip('hide');
				}
			}
		
		});
		
		
		
	})

	//添加细分类
	function assortAdd(){
		var nodes = $('#icdAssorts').tree('getSelected');//获取选中的节点
		var id='';
		if(nodes!=null&&nodes!=undefined){
			id=nodes.id;
		}else{
			$.messager.alert('提示','请选择一个节点！'); 
			return ;
		}
		
		
		var assort_Name= $("#assort_Name").val();
		if(assort_Name==''||assort_Name==null||undefined==assort_Name){
				$.messager.alert('提示','请输入编码细分类的名称!'); 
				return ;
		}
		
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/icdAssort/addIcdAssort.action",
			data:"id="+id+"&assort_Name="+assort_Name,
			success:function(backData){
				if(backData.msg=="success"){
					$.messager.alert('提示','添加成功！'); 
					$("#assort_Name").val("");
				}else{
					$.messager.alert('提示','添加失败！'); 
				}
			}
		})
		
	}
	
	//关联细分类
	function assortLink(){
		
		var nodes = $('#icdAssorts').tree('getSelected');//获取选中的节点
		var assortId='';
		if(nodes!=null&&nodes!=undefined){
			assortId=nodes.id;
		}else{
			$.messager.alert('提示','请选择一个节点！'); 
			return ;
		}
		
		//获取列表中被选中的行
		var icdIds =$('#list').datagrid('getChecked');
		var icdId='';
		if(icdIds!=null&&icdIds!=undefined){
			for(var i=0;i<icdIds.length;i++){
				if(i==(icdIds.length-1)){
					icdId+=icdIds[i].id;
				}else{
					icdId+=icdIds[i].id+",";
				}
				
			}
		}else{
			$.messager.alert('提示','请选择一项诊断！'); 
			return ;
		}
		
		//进行关联
		$.ajax({
			type:"post",
			data:"assortId="+assortId+"&icdId="+icdId,
			url:"${pageContext.request.contextPath}/icdAssort/updateIcdSorrt.action",
			success:function(backData){
				if(backData.msg=="success"){
					$.messager.alert('提示','关联成功！'); 
					//清除所选择的行
					$('#list').datagrid('clearSelections');
					//刷新icd列表
					searchICD();
				}else{
					$.messager.alert('提示','关联失败！'); 
				}
				
			}
			
		})
		
	}
	
	
	
	//icd编码列表查询
	function searchICD(){
		var icdCode=$("#icdCode").val();
		$('#list').datagrid('load', {    
			icdCode: icdCode  
		});  
	}
	



</script>