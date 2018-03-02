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
<title>临床路径申请</title>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true" >   
	<div data-options="region:'west',split:false" style="width:15%;position: relative;">
		<div data-options="region:'north'" style="height:42px;width:calc(100% - 20px) ;border-top:0;border-left:0;border-right:0;background-color: #fff;position: absolute;top:0;z-index: 1">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap">
							<input  id="deptName" data-options="prompt:'请输入科室名'"  class="easyui-textbox"  style="width:180px"/>
							<a href="javascript:void(0)"  onclick="searchDept()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</td>
					</tr>
				</table>
		   </div> 
	    
		   <div  style="width: 100%;height: 100%; position: absolute; padding-top: 40px;overflow: auto;top: 0;box-sizing: border-box;">
				<div style="padding-left:5px">
					<ul id="inpatientDept"></ul> 
				</div> 
			</div>
	</div>   
	<div data-options="region:'center',border: false,split:false" style="width:85%;">
		<div class="easyui-layout" data-options="fit:true" > 
			<div data-options="region:'north'" style="height:42px;width:100%;border-top:0;border-left:0;border-right:0;">
				<table style="width:100%;padding: 0px 5px 0px 5px;height: 40px;">
					<tr>
						<td nowrap="nowrap">
							<a href="javascript:void(0)"  onclick="addApply()" class="easyui-linkbutton" data-options="iconCls:'icon-add'"  >添加</a>
							<a href="javascript:void(0)"  onclick="delApply()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"  >删除</a>
							<a href="javascript:void(0)"  onclick="refreshApply()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'"  >刷新</a>
						</td>
					</tr>
				</table>
			</div> 

			<div data-options="region:'center',border: false" style="width:100%;">
				<table id="list" class="easyui-datagrid" >
					<thead>
						<tr>
							<th data-options="field:'id',checkbox:true,align:'center'" ></th>
							<th data-options="field:'inpatient_no',align:'center',width:120">住院流水号</th>
							<th data-options="field:'medicalrecord_id',align:'center',width:120">病历号</th>
							<th data-options="field:'patientName',align:'center',width:80">患者姓名</th>
							<th data-options="field:'cpName',align:'center',width:120">临床路径名称</th>
							<th data-options="field:'version_no',align:'center',width:60">临床路径版本号</th>
							<th data-options="field:'apply_type',align:'center',width:65,
							formatter: function(value,row,index){
								if (row.apply_type=='1'){
									return '入径申请';
								}else{
									return '出径申请';
								}
							}">申请类别</th>
							<th data-options="field:'apply_doct_code',align:'center',width:100">申请医生</th>
							<th data-options="field:'apply_code',align:'center',width:100">申请科室</th>
							<th data-options="field:'apply_status',align:'center',width:65,
							formatter: function(value,row,index){
								if (row.apply_status=='0'){
									return '申请中';
								} else if (row.apply_status=='1'){
									return '已通过';
								}else{
									return '未通过';
								}
							}">申请状态</th>
							<th data-options="field:'apply_date',align:'center',width:150">申请时间</th>
							<th data-options="field:'apply_memo',align:'center',width:200">申请说明</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<div id="win"></div>  
</div>
</body>
</html>
<script type="text/javascript">
	
	$(function(){
		
		//加载科室树
		deptInit("");
	
		$("#list").datagrid({
			method: 'post',
			data:[],
			fit: true,
			remoteSort: false,
			pagination: true,
			pageSize: 20,
			idField:'id',
			fitColumns:true,
			rownumbers:true,
			pageList: [20, 30, 50, 100],
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


	
	function loadData(node){
		//先判断是否加载
		var sons =$('#inpatientDept').tree('getChildren', node.target);
		//判断当前节点是否是叶子节点
		var isSon =$('#inpatientDept').tree('isLeaf', node.target);
		if(!isSon){
			if(sons!=null&&sons!=undefined&&sons.length>0){
				//什么也不做
			}else{
				 //1.在用户展开节点的时候加载患者列表
				if(node.id!="root"){//root节点下是不会再添加子节点
					$.ajax({
						type:"post",
						data:"id="+node.id,
						url:"${pageContext.request.contextPath}/outpatient/CPWay/patientList.action",
						success:function(backData){
							if(backData!=null&&backData!=undefined){
								$('#inpatientDept').tree('append', {
									parent: node.target,
									data:backData
								});
							}
						}
					})
				} 
			}
		}
		 //2.用户在展开的时候加载右边的患者临床路径申请列表
		if(node.id!="root"){
			$('#list').datagrid({
				url:'${pageContext.request.contextPath}/outpatient/CPWay/cPWayPatientList.action',
				queryParams: {
					id: node.id
				}
			});
		}
	}
	
	
	function addApply(){
		var node =$('#inpatientDept').tree("getSelected");
		if(node!=null){
			var isSon =$('#inpatientDept').tree('isLeaf', node.target);
			if(node.id=='root'||isSon){//不能使根节点，不能使叶子节点
				$.messager.alert('提示','请选择有效的科室！'); 
				return ;
			}else{
				$('#win').window({
					width : 500,
					height : 550,
					title : "添加",
					modal : true,
					minimizable : false,
					maximizable : false,
					content : "<iframe src='${pageContext.request.contextPath}/outpatient/CPWay/toAddApplyUI.action?id="+node.id+"'"+" height='100%' width='100%' frameborder='0px' ></iframe>"
				});
				
			}
		}else{
			$.messager.alert('提示','请选择科室！');
		}
	}
 
	
	
	function delApply(){
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){    
		    if (r){    
		    	 var ids =$('#list').datagrid("getSelections");
		 	    if(ids!=null&&ids!=undefined&&ids.length>0){
		 	    	var cPWAppId='';
		 	    	for(var a=0;a<ids.length;a++){
		 	    		if(ids[a].apply_status=="0"){//删除时，只能删除状态为申请状态的数据
		 	    			cPWAppId+=ids[a].id+",";
		 	    		}
		 	    	}
		 	    	if(cPWAppId==''){
		 	    		$.messager.alert('提示','所选项中无申请状态的数据,无法进行删除！'); 
		 	    		return;
		 	    	}else{
		 	    		$.ajax({
		 					type:"post",
		 					data:"cPWAppId="+cPWAppId,
		 					url:"${pageContext.request.contextPath}/outpatient/CPWay/delCPWayPatient.action",
		 					success:function(backData){
		 						if(backData.data=="success"){
		 							$.messager.alert('提示','删除成功！'); 
		 							//清除所选择的行
		 							$('#list').datagrid('clearSelections');
		 							//刷新列表
		 							refreshApply();
		 						}else{
		 							$.messager.alert('提示','网络异常,请稍后重试!'); 
		 						}
		 						
		 					}
		 				})
		 	    	}
		 	    	
		 	    }else{
		 	    	$.messager.alert('提示','请至少选择一行记录！'); 
		 	    } 
		    }    
		}); 
	}

	function refreshApply(){
		var node =$('#inpatientDept').tree("getSelected");
		if(node!=null){
			loadData(node)
		}else{
			$.messager.alert('提示','请选择科室！'); 
		}
	}
	
	//用于加载科室树
	function deptInit(deptName){
		$.ajax({
			type:"post",
			url:'${pageContext.request.contextPath}/outpatient/CPWay/inpatientDeptTree.action',
			data:"deptName="+deptName,
			success:function(backData){
				if(backData!=null&&backData!=undefined){
					//住院科室tree树
					$('#inpatientDept').tree({
						lines : true,
						cache : false,
						animate : true,
						data:backData,
						onClick:function(node){
							loadData(node)
						},
						onBeforeExpand: function(node){
							loadData(node)
							 return true;
						}
					}); 
				}
				
			}
		})
	}
	
	//搜索科室树
	function searchDept(){
		var deptName= $("#deptName").val();
		 deptInit(deptName);
	}
	

</script>