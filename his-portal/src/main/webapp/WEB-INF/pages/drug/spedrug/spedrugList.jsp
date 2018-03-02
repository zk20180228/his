<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
	}
#elayout .tabs-panels{
border-left:0
}
</style>
<script type="text/javascript">
	var stype1=1;
	var stype0=2;
	var qwer=2;//特殊类别
	var num1="";//表1下标
	var num2="";//表2下标
	var yongfaList="";//用法
	var pinciMap="";//频次
	var specname="";//获取的指定科室值
	$(function(){
		//渲染用法
		$.ajax({
			url: "<%=basePath%>drug/spedrug/queryYongfa.action", 
			type:'post',
			success: function(yongData){
				yongfaMap = yongData;
			}
		});
		//渲染频次
		$.ajax({
			url: "<%=basePath%>drug/spedrug/queryPinci.action", 
			type:'post',
			success: function(pinciData){
				pinciMap = pinciData;
			}
		});
		reloadRightTable2();
		$('#elayout').tabs({
			onSelect:function(title,index){
				if(index == 1){
					reloadRightTable1();
					qwer=1;
				}else if(index == 0){
					reloadRightTable2();
					qwer=2
				}
			}
		});
		
		//回车事件
		bindEnterEvent('drugName',queryDrugInfo,'easyui')
		//选择选项卡  为特殊类别赋值
		$('#tableLeft').datagrid({
			pagination:true,
			pageSize:30,
			pageList:[10,20,30,40,50],
			url:"<%=basePath%>drug/spedrug/queryDrugInfo.action?menuAlias=${menuAlias}",
			onBeforeLoad:function (param) {
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
					data:{"type" : "useage"},
					type:'post',
					success: function(yongfadata) {
						yongfaList = yongfadata;
					}
				});
			},
			onDblClickRow:function(rowIndex,rowData){
				var row = $('#tableLeft').datagrid('getSelected'); //获取当前选中行    
				var rowDataLeft=JSON.stringify(row)
		           if(rowDataLeft!=null&&rowDataLeft!=""){
		        	   $('#AddList').dialog({    
		       		    title: "添加特限药品维护明细",    
		       		    width: '800px',    
		       		    height:'240px',    
		       		    closed: false,    
		       		    cache: false,
		       		 	method:"post",
		       		 	queryParams:{"rowDataLeft":rowDataLeft},
		       		    href: "<%=basePath%>drug/spedrug/openAddWindow.action?menuAlias=${menuAlias}",    
		       		    modal: true   
		       		   });    
					}
			},onLoadSuccess:function(row, data){
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
	});
	//加载RightTable1列表
	function reloadRightTable1(){
		var ed="";		
		$('#tableRight1').datagrid({
			pagination:true,
			pageSize:30,
			pageList:[10,20,30,40,50],
			url:"<%=basePath%>drug/spedrug/querySpedrugInfo.action?stype1="+stype1 + "&menuAlias=${menuAlias}",
			onBeforeLoad:function(){
				$('#tableRight1').datagrid('clearChecked');
				$('#tableRight1').datagrid('clearSelections');
			},
			onDblClickRow:function(rowIndex, rowData){
				if(rowData!=null&&rowData!=""){
				AdddilogModel("修改特限药品维护","<%=basePath%>drug/spedrug/openUpdateWindow.action",rowData.id);
				}
			}
		});
	}
	//加载RightTable2列表
	function reload2(){
		$('#tableRight2').datagrid('reload');
	}
	//加载RightTable2列表
	function reload1(){
		$('#tableRight1').datagrid('reload');
	}
	//加载RightTable2列表
	function reloadRightTable2(){
		$('#tableRight2').datagrid({
			pagination:true,
			pageSize:30,
			pageList:[10,20,30,40,50],
			url:"<%=basePath%>drug/spedrug/querySpedrugInfo.action?stype0="+stype0 + "&menuAlias=${menuAlias}",
			onBeforeLoad:function(){
				$('#tableRight2').datagrid('clearChecked');
				$('#tableRight2').datagrid('clearSelections');
			},
			onDblClickRow:function(rowIndex, rowData){
	            if(rowData!=null&&rowData!=""){
	            	AdddilogModel("修改特限药品维护","<%=basePath%>drug/spedrug/openUpdateWindow.action",rowData.id);
	            }
			},onLoadSuccess:function(row, data){
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
	}
	function AdddilogModel(title,url,id){
		$('#updateList').dialog({    
			title: title,    
			width: '400px',    
			height:'280px',    
			closed: false,    
			cache: false,
			method:"post",
			queryParams:{"id":id},
			href: url,  
			modal: true   
		});
	}
	//关闭模式窗口
	function closeDialog() {
		$('#updateList').dialog('close');  
	}
	function closeDialogAdd() {
		$('#AddList').dialog('close');  
	}
	//(条件)查询药品表
	function queryDrugInfo(){
		var dName=$.trim($('#drugName').textbox('getValue'));
		$('#tableLeft').datagrid({
			queryParams:{djName:dName}
		});
	}
	function deleteT(){
		var pp = $('#elayout').tabs('getSelected');    
		var title = pp.panel('options').title;
		var row = "";
		if(title=='科室'){
			row=$('#tableRight2').datagrid('getChecked');
		}else if(title=='医生'){
			row=$('#tableRight1').datagrid('getChecked');
		}else{
			row = "";
		}
		if(row!=""){
			var row11=JSON.stringify(row);
			$.ajax({
				url:"<%=basePath%>drug/spedrug/deleterows.action",
				type:'post',
				data:{row11:row11},
				success:function(data){
					$.messager.alert('提示',data.resMsg);
					if(data.resCode=='success'){
						reload1();
						reload2();
					}
				}
			});
		}else{
			$.messager.alert('操作提示',"请选择要删除的记录");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	
	//渲染用法
	function funyongfa(value,row,index){
		if(yongfaList.length!=0){
			if(value!=null){
				for(var i =0;i<yongfaList.length;i++){
					if(value==yongfaList[i].encode){
						return yongfaList[i].name;
					}
				}
			}
		}
	}
	//渲染频次
	function functionpinci(value,row,index){
		for(var i=0;i<pinciMap.length;i++){
			if(value==pinciMap[i].encode){
				return pinciMap[i].name;
			}else{
				return value;
			}
		}
		
	}
	
	// 药品列表查询重置
	function searchReload() {
		$('#drugName').textbox('setValue','');
		queryDrugInfo();
	}
</script>
</head>
<body style="margin: 0px;padding: 0px;">
<div style="width:100%;fit="true">
	<div class="easyui-layout" fit=true style="width:100%;height: 100%;">
		<div id="leftleft" data-options="region:'west',split:true,border:false" style="height:100%;width: 40%">
			<div class="easyui-layout" data-options="fit:true">   
				<div data-options="region:'north',split:false,border:true" style="height:40px;border-top:0;border-bottom:0">
					<div style="padding: 4px;border-bottom: 1px solid #95b8e7;" class="changeskinBottom">
						<input class="easyui-textbox" id="drugName" name="drugName" style="margin:90px 0 0 70px" data-options="prompt:'名称/编码 回车查询'">
		          			<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryDrugInfo()" data-options="iconCls:'icon-search'"  >查询</a>
							</shiro:hasPermission>
						<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</div>
				</div>   
				<div data-options="region:'center',border:true" style="width:100%;height:100%">
					<div style="height:100%" >
						<table id="tableLeft" class="easyui-datagrid" data-options="striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false">
							<thead >
								<tr>
									<th data-options="field:'id',hidden:'true'">
									<th data-options="field:'name',width:'20%'">药品名称</th>																			
									<th data-options="field:'spec',width:'20%'">规格</th>																			
									<th data-options="field:'drugUsemode',width:'30%',formatter:funyongfa">用法</th>																			
									<th data-options="field:'drugFrequency',width:'30%',formatter:functionpinci">频次</th>
								</tr>
							</thead>
						</table>
					</div>
				</div> 
			</div>
		</div>
		<div id="rightright" data-options="region:'center',split:false" style="height:100%;width: 60%;border-top:0;">
			<div id="elayout" class="easyui-tabs" style="width: 100%;height:100%;border-left:0" data-options="plain:true,fit:true">
				<div id="keshi" title="科室"  style="width:100%;border-left:0">
					<table id="tableRight2" data-options="toolbar: '#tb1',striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:false,fitColumns:false">
						<thead >
							<tr>
								<th data-options="field:'checkId1',checkbox:true">
								<th data-options="field:'id',hidden:'true'">
								<th data-options="field:'drugCode',hidden:'true'">
								<th data-options="field:'speType',hidden:'true'">
								<th data-options="field:'tradeName',width:'26%'">名称</th>
								<th data-options="field:'specs',width:'20%'">规格</th>
								<th data-options="field:'speCode',width:'20%',hidden:'true'">科室编码</th>
								<th data-options="field:'speName',width:'20%'">指定科室</th>	
								<th data-options="field:'memo',width:'30%'">备注</th>	
							</tr>
						</thead>
					</table>
				</div>
				<div id="yisheng" title="医生" style="width: 100%;border-left:0">
					<table id="tableRight1" data-options="striped:true,border:false,fit:true,checkOnSelect:true,selectOnCheck:false,singleSelect:false,fitColumns:false,toolbar: '#tb'">
						<thead >
							<tr>
								<th data-options="field:'checkId2',checkbox:true">
								<th data-options="field:'id',hidden:'true'">
								<th data-options="field:'drugCode',hidden:'true'">
								<th data-options="field:'speType',hidden:'true'">
								<th data-options="field:'tradeName',width:'24%'">名称</th>																			
								<th data-options="field:'specs',width:'20%'">规格</th>																			
								<th data-options="field:'speCode',width:'20%',hidden:'true'">医生编码</th>																			
								<th data-options="field:'speName',width:'20%'">指定医生</th>	
								<th data-options="field:'memo',width:'30%'">备注</th>	
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
		<div id="tb" >
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="deleteT()" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
		</div>
		<div id="tb1"  >
			<shiro:hasPermission name="${menuAlias}:function:delete">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="deleteT()" data-options="iconCls:'icon-remove',plain:true">删除</a>
			</shiro:hasPermission>
		</div>
		<div id="AddList"></div>
		<div id="updateList"></div>
	</div>
</div>
</body>
</html>