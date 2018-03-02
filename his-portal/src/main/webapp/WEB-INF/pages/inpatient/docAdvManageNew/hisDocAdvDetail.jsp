<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱管理历史医嘱</title>
</head>
	<body>
		<div id="layouthis" class="easyui-layout" fit="true">   
		    <div data-options="region:'center',border:false" style="padding:0px 0px 0px 0px">
				<div id="tttb" class="easyui-tabs" data-options="fit:true,tabPosition:'bottom'">   
					<div title="长期医嘱" id="hislongda" style="padding: 5px 5px 5px 5px;" data-options="fit:true"> 
						 	<table id="infolistC" class="easyui-datagrid" data-options="fit:true,singleSelect:true,method:'post',rownumbers:true,		
									striped:true,border:true,selectOnCheck:false,checkOnSelect:true,fitColumns:false,pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true"></th>
										<th data-options="field:'moStat',hidden:true"></th>
										<th data-options="field:'id',hidden:true">医嘱执行单编号(id)</th>
										<th data-options="field:'typeCode',hidden:true">医嘱类别代码</th>												
										<th data-options="field:'typeName'" style="width: 100px">医嘱类型</th>
										<th data-options="field:'classCode',hidden:true">系统类型</th>
										<th data-options="field:'className',hidden:true">系统名称</th>
										<th data-options="field:'itemName'" style="width: 150px">医嘱名称</th>
										<th data-options="field:'combNoFlag'" style="width: 23px">组</th>
										<th data-options="field:'combNo',hidden:true" style="width: 100px">组</th>
										<th data-options="field:'qtyTot'" style="width: 50px">总量</th>
										<th data-options="field:'priceUnit',hidden:true">单位（总量单位）</th>
										<th data-options="field:'doseOnce'" style="width: 50px">每次量</th>
										<th data-options="field:'doseUnit',hidden:true">单位（剂量单位）</th>
										<th data-options="field:'specs',hidden:true">规格</th>
										<th data-options="field:'drugDosageform',hidden:true">剂型代码</th>
										<th data-options="field:'drugType',hidden:true">药品类型</th>
										<th data-options="field:'drugNature',hidden:true">药品性质</th>
										<th data-options="field:'drugRetailprice',hidden:true">价格</th>														
										<th data-options="field:'useDays'" style="width: 50px">付数</th>
										<th data-options="field:'frequencyCode',hidden:true">频次id</th>												
										<th data-options="field:'frequencyName'" style="width: 150px">频次</th>
										<th data-options="field:'usageCode',hidden:true">用法Id</th>
										<th data-options="field:'useName'" style="width: 150px">用法名称</th>																								
										<th data-options="field:'dateBgn'" style="width: 150px">开始时间</th>
										<th data-options="field:'dateEnd'" style="width: 150px">停止时间</th>
										<th data-options="field:'moDate'" style="width: 150px">开立时间</th>
										<th data-options="field:'docName'" style="width: 100px">开立医生</th>
										<th data-options="field:'execDpcd',hidden:true">执行科室Id</th>												
										<th data-options="field:'execDpnm'" style="width: 100px">执行科室</th>
										<th data-options="field:'emcFlag',formatter:emcFamater" style="width: 50px">急</th>
										<th data-options="field:'labCode',formatter:colorInfoSampleTeptMapFamater" style="width: 100px">样本类型</th>
										<th data-options="field:'itemNote',formatter:colorInfoCheckpointMapFamater,width:100">检查部位</th>
										<th data-options="field:'kkks'">扣库科室</th>
										<th data-options="field:'moNote2'">备注</th>
										<th data-options="field:'recUsernm'">录入人</th>
										<th data-options="field:'listDpcdName'">开立科室</th>
										<th data-options="field:'listDpcd',hidden:true">开立科室code</th>
										<th data-options="field:'dcUsernm'">停止人</th>
										<th data-options="field:'drugBasicdose',hidden:true">基本剂量</th>
										<th data-options="field:'undrugInspectionsite',hidden:true">检查部位检体</th>
										<th data-options="field:'undrugIsinformedconsent',hidden:true">患者是否同意</th>	
										<th data-options="field:'hypotest',hidden:true">是否需要皮试</th> 	   
										<th data-options="field:'sortId'">顺序号</th>											
									</tr>
								</thead>
							</table>
				    </div>   
						<div title="临时医嘱" id="histemda" style="padding: 5px 5px 5px 5px;" data-options="fit:true">
							<table id="infolistD" class="easyui-datagrid" data-options="fit:true,singleSelect:true,method:'post',rownumbers:true,		
									striped:true,border:true,selectOnCheck:false,checkOnSelect:true,fitColumns:false,pagination:true,pageSize:20,pageList:[20,30,50,80,100]">
							<thead>
								<tr>												
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'moStat',hidden:true"></th>
									<th data-options="field:'id',hidden:true">医嘱执行单编号(id)</th>
									<th data-options="field:'typeCode',hidden:true">医嘱类别代码</th>												
									<th data-options="field:'typeName'" style="width: 100px">医嘱类型</th>
									<th data-options="field:'classCode',hidden:true">系统类型</th>
									<th data-options="field:'className',hidden:true">系统名称</th>
									<th data-options="field:'itemName'" style="width: 150px">医嘱名称</th>
									<th data-options="field:'combNoFlag'" style="width: 23px">组</th>
									<th data-options="field:'combNo',hidden:true" style="width: 100px">组</th>
									<th data-options="field:'qtyTot'" style="width: 50px">总量</th>
									<th data-options="field:'priceUnit',hidden:true">单位（总量单位）</th>
									<th data-options="field:'doseOnce'" style="width: 50px">每次量</th>
									<th data-options="field:'doseUnit',hidden:true">单位（剂量单位）</th>
									<th data-options="field:'specs',hidden:true">规格</th>
									<th data-options="field:'drugDosageform',hidden:true">剂型代码</th>
									<th data-options="field:'drugType',hidden:true">药品类型</th>
									<th data-options="field:'drugNature',hidden:true">药品性质</th>
									<th data-options="field:'drugRetailprice',hidden:true">价格</th>														
									<th data-options="field:'useDays'" style="width: 50px">付数</th>
									<th data-options="field:'frequencyCode',hidden:true">频次id</th>												
									<th data-options="field:'frequencyName'" style="width: 150px">频次</th>
									<th data-options="field:'usageCode',hidden:true">用法Id</th>
									<th data-options="field:'useName'" style="width: 150px">用法名称</th>																								
									<th data-options="field:'dateBgn'" style="width: 150px">开始时间</th>
									<th data-options="field:'dateEnd'" style="width: 150px">停止时间</th>
									<th data-options="field:'moDate'" style="width: 150px">开立时间</th>
									<th data-options="field:'docName'" style="width: 100px">开立医生</th>
									<th data-options="field:'execDpcd',hidden:true">执行科室Id</th>												
									<th data-options="field:'execDpnm'" style="width: 100px">执行科室</th>
									<th data-options="field:'emcFlag',formatter:emcFamater" style="width: 50px">急</th>
									<th data-options="field:'labCode',formatter:colorInfoSampleTeptMapFamater" style="width: 100px">样本类型</th>
									<th data-options="field:'itemNote',width:100,formatter:colorInfoCheckpointMapFamater">检查部位</th>
									<th data-options="field:'kkks'">扣库科室</th>
									<th data-options="field:'moNote2'">备注</th>
									<th data-options="field:'recUsernm'">录入人</th>
									<th data-options="field:'listDpcdName'">开立科室</th>
									<th data-options="field:'listDpcd',hidden:true">开立科室code</th>
									<th data-options="field:'dcUsernm'">停止人</th>
									<th data-options="field:'drugBasicdose',hidden:true">基本剂量</th>	
									<th data-options="field:'undrugInspectionsite',hidden:true">检查部位检体</th>
									<th data-options="field:'undrugIsinformedconsent',hidden:true">患者是否同意</th>	
		            				<th data-options="field:'hypotest',hidden:true">是否需要皮试</th> 					            				 
									<th data-options="field:'sortId'">顺序号</th>												
								</tr>
							</thead>
						</table>
					    </div> 
					</div>
			    </div>    
		</div>  
			
	<script type="text/javascript">
	var decmpsState="";
	var inpatientNo="";
	$(function(){
		var node = $('#tDt').tree('getSelected');
		if(node==null){
			loadDatagridC(1,null,null)
		}else{
			if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
			}else{
				loadDatagridC(1,inpatientNo,null);	
			}
		}
		$('#tttb').tabs({
		    border:false,
		    onSelect:function(title,index){			    	
		    	var qq = $('#tttb').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='临时医嘱'){
					var node = $('#tDt').tree('getSelected');
					if(node==null){
						loadDatagridD(1,null,null)
					}else{
						if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
						}else{
							loadDatagridD(0,node.attributes.inpatientNo,null);	
						}
					}
		    	}else{
		    		var node = $('#tDt').tree('getSelected');
					if(node==null){
					}else{
						if(node.attributes.inpatientNo==""||node.attributes.inpatientNo==null){
						}else{
							loadDatagridC(1,node.attributes.inpatientNo,null);	
						}
					}
		    	}
		    }
		});
	});
	function loadDatagridC(decmpsState,inpatientNo,recordId){
		$("#infolistC").datagrid({  
			url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrderHis.action',
			queryParams:{'inpatientOrder.decmpsState':decmpsState,'inpatientOrder.inpatientNo':inpatientNo,recordId:recordId},
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
                e.preventDefault(); //阻止浏览器捕获右键事件
                $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
                $('#adviceListMenu').menu('show', {//显示右键菜单
                    left: e.pageX,//在鼠标点击处显示菜单
                    top: e.pageY
                });
            },onLoadSuccess:function(data){
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
				var row = $('#infolistC').datagrid('getRows');
				var rows = $('#infolistC').datagrid('getRows');
				if(row.length>0){
					for(var q=0;q<row.length;q++){
						var a=0;
						for(var i=0;i<rows.length;i++){
							if(row[q].combNo==rows[i].combNo && row[q].combNo!=null && row[q].combNo!=''){
								a++;
							}
						}
						if(a==1){
							$('#infolistC').datagrid('updateRow',{
								index: q,
								row: {
									combNoFlag:''
								}
							});
						}else if(a==2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});									
										}else if(j!=0 && (row[q].combNo!=rows[j-1].combNo)){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});												
										}else if((j!=rows.length-1) && (row[q].combNo!=rows[j+1].combNo)){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}									
									}								
								}
							}						
						}else if(a>2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j!=0 && row[q].combNo!=rows[j-1].combNo){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else if(j!=rows.length-1 && row[q].combNo!=rows[j+1].combNo){
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else{
											$('#infolistC').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┃'
												}
											});	
										}																		
									}								
								}
							}
						}
					}	
				}					 
			}
		});
	}

	function loadDatagridD(decmpsState,inpatientNo,recordId){
		$("#infolistD").datagrid({
			url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrderHis.action',
			queryParams:{'inpatientOrder.decmpsState':decmpsState,'inpatientOrder.inpatientNo':inpatientNo,recordId:recordId},
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
                e.preventDefault(); //阻止浏览器捕获右键事件
                $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
                $('#adviceListMenu').menu('show', {//显示右键菜单
                    left: e.pageX,//在鼠标点击处显示菜单
                    top: e.pageY
                });
            },onLoadSuccess:function(data){
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
				var row = $('#infolistD').datagrid('getRows');
				var rows = $('#infolistD').datagrid('getRows');
				if(row.length>0){
					for(var q=0;q<row.length;q++){
						var a=0;
						for(var i=0;i<rows.length;i++){
							if(row[q].combNo==rows[i].combNo && row[q].combNo!=null && row[q].combNo!=''){
								a++;
							}
						}
						if(a==1){
							$('#infolistD').datagrid('updateRow',{
								index: q,
								row: {
									combNoFlag:''
								}
							});	 
						}else if(a==2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});									
										}else if(j!=0 && (row[q].combNo!=rows[j-1].combNo)){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});												
										}else if((j!=rows.length-1) && (row[q].combNo!=rows[j+1].combNo)){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}									
									}								
								}
							}						
						}else if(a>2){
							for(var j=0;j<rows.length;j++){
								if(row[q].combNo==rows[j].combNo){
									if(row[q].sortId==rows[j].sortId){
										if(j==0){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j!=0 && row[q].combNo!=rows[j-1].combNo){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┓'
												}
											});
										}else if(j==rows.length-1){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else if(j!=rows.length-1 && row[q].combNo!=rows[j+1].combNo){
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┛'
												}
											});	
										}else{
											$('#infolistD').datagrid('updateRow',{
												index: q,
												row: {
													combNoFlag:'┃'
												}
											});	
										}																		
									}								
								}
							}
						}
					}	
				}					 
			}
		});
	}
	</script>
	</body>
</html>