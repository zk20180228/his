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
		    <div data-options="region:'center',title:'历史医嘱'" style="padding:0px 0px 8px 0px">
				<div id="tttb" class="easyui-tabs" data-options="fit:true,tabPosition:'bottom'">   
					<div title="长期医嘱" id="hislongda" style="padding: 5px 5px 5px 5px;" data-options="fit:true"> 
						 	<table id="infolistC" class="easyui-datagrid" fit="true">
								<thead>
									<tr>
										<th data-options="field:'ck',checkbox:true"></th>
										<th data-options="field:'moStat',hidden:true"></th>
										<th data-options="field:'id',hidden:true">医嘱执行单编号(id)</th>
										<th data-options="field:'typeCode',hidden:true">医嘱类别代码</th>												
										<th data-options="field:'typeName'">医嘱类型</th>
										<th data-options="field:'classCode',hidden:true">系统类型</th>
										<th data-options="field:'className',hidden:true">系统名称</th>
										<th data-options="field:'itemName',formatter:itemNameFamater">医嘱名称</th>
										<th data-options="field:'combNo'">组</th>
										<th data-options="field:'packQty'">总量</th>
										<th data-options="field:'priceUnit',hidden:true">单位（总量单位）</th>
										<th data-options="field:'doseOnce'">每次量</th>
										<th data-options="field:'doseUnit',hidden:true">单位（剂量单位）</th>
										<th data-options="field:'specs',hidden:true">规格</th>
										<th data-options="field:'drugDosageform',hidden:true">剂型代码</th>
										<th data-options="field:'drugType',hidden:true">药品类型</th>
										<th data-options="field:'drugNature',hidden:true">药品性质</th>
										<th data-options="field:'drugRetailprice',hidden:true">价格</th>														
										<th data-options="field:'useDays'">付数</th>
										<th data-options="field:'frequencyCode',hidden:true">频次id</th>												
										<th data-options="field:'frequencyName'">频次</th>
										<th data-options="field:'usageCode',hidden:true">用法Id</th>
										<th data-options="field:'useName'">用法名称</th>																								
										<th data-options="field:'dateBgn'">开始时间</th>
										<th data-options="field:'dateEnd'">停止时间</th>
										<th data-options="field:'moDate'">开立时间</th>
										<th data-options="field:'docName'">开立医生</th>
										<th data-options="field:'execDpcd',hidden:true">执行科室Id</th>												
										<th data-options="field:'execDpnm'">执行科室</th>
										<th data-options="field:'emcFlag',hidden:true">急Id</th>
										<th data-options="field:'isUrgent'">急</th>
										<th data-options="field:'labCode',formatter:sampleFamater">样本类型</th>
										<th data-options="field:'itemNote',width:100,formatter:checkPointFamater">检查部位</th>
										<th data-options="field:'kkks'">扣库科室</th>
										<th data-options="field:'moNote2'">备注</th>
										<th data-options="field:'recUsernm'">录入人</th>
										<th data-options="field:'listDpcd',formatter:implDepartmentFamaters">开立科室</th>
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
							<table id="infolistD" class="easyui-datagrid" fit="true"> 
							<thead>
								<tr>												
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'moStat',hidden:true"></th>
									<th data-options="field:'id',hidden:true">医嘱执行单编号(id)</th>
									<th data-options="field:'typeCode',hidden:true">医嘱类别代码</th>												
									<th data-options="field:'typeName'">医嘱类型</th>
									<th data-options="field:'classCode',hidden:true">系统类型</th>
									<th data-options="field:'className',hidden:true">系统名称</th>
									<th data-options="field:'itemName',formatter:itemNameFamater">医嘱名称</th>
									<th data-options="field:'combNo'">组</th>
									<th data-options="field:'packQty'">总量</th>
									<th data-options="field:'priceUnit',hidden:true">单位（总量单位）</th>
									<th data-options="field:'doseOnce'">每次量</th>
									<th data-options="field:'doseUnit',hidden:true">单位（剂量单位）</th>
									<th data-options="field:'specs',hidden:true">规格</th>
									<th data-options="field:'drugDosageform',hidden:true">剂型代码</th>
									<th data-options="field:'drugType',hidden:true">药品类型</th>
									<th data-options="field:'drugNature',hidden:true">药品性质</th>
									<th data-options="field:'drugRetailprice',hidden:true">价格</th>														
									<th data-options="field:'useDays'">付数</th>
									<th data-options="field:'frequencyCode',hidden:true">频次id</th>												
									<th data-options="field:'frequencyName'">频次</th>
									<th data-options="field:'usageCode',hidden:true">用法Id</th>
									<th data-options="field:'useName'">用法名称</th>																								
									<th data-options="field:'dateBgn'">开始时间</th>
									<th data-options="field:'dateEnd'">停止时间</th>
									<th data-options="field:'moDate'">开立时间</th>
									<th data-options="field:'docName'">开立医生</th>
									<th data-options="field:'execDpcd',hidden:true">执行科室Id</th>												
									<th data-options="field:'execDpnm'">执行科室</th>
									<th data-options="field:'emcFlag',hidden:true">加急标记</th>
									<th data-options="field:'isUrgent',width:40,align:'center',formatter:emcFamater">急</th>
									<th data-options="field:'labCode',formatter:sampleFamater">样本类型</th>
									<th data-options="field:'itemNote',width:100,formatter:checkPointFamater">检查部位</th>
									<th data-options="field:'kkks'">扣库科室</th>
									<th data-options="field:'moNote2'">备注</th>
									<th data-options="field:'recUsernm'">录入人</th>
									<th data-options="field:'listDpcd',formatter:implDepartmentFamaters">开立科室</th>
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
	function loadDatagridC(decmpsState,inpatientNo){
		$("#infolistC").datagrid({  
			height:$("body").height()-365,
			fit:true,
			singleSelect:true,
			method:'post',
			rownumbers:true,		
			striped:true,
			border:true,
			selectOnCheck:false,
			checkOnSelect:true,
			fitColumns:false ,
			pagination:true,	
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrderHis.action?inpatientOrder.decmpsState='+decmpsState+'&inpatientOrder.inpatientNo='+inpatientNo,
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
                e.preventDefault(); //阻止浏览器捕获右键事件
                //$(this).datagrid("clearSelections"); //取消所有选中项
                $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
                $('#adviceListMenu').menu('show', {//显示右键菜单
                    left: e.pageX,//在鼠标点击处显示菜单
                    top: e.pageY
                });
            }
		});
	}

	function loadDatagridD(decmpsState,inpatientNo){
		$("#infolistD").datagrid({  
			height:$("body").height()-365,
			fit:true,
			singleSelect:true,
			method:'post',
			rownumbers:true,
			striped:true,
			border:true,
			selectOnCheck:false,
			checkOnSelect:true,
			fitColumns:false ,
			pagination:true,	
			pageSize:20,
			pageList:[20,30,50,80,100],
			url:'<%=basePath%>inpatient/docAdvManage/queryInpatientOrderHis.action?inpatientOrder.decmpsState='+decmpsState+'&inpatientOrder.inpatientNo='+inpatientNo,
			onRowContextMenu: function(e, rowIndex, rowData) { //右键时触发事件
                e.preventDefault(); //阻止浏览器捕获右键事件
                //$(this).datagrid("clearSelections"); //取消所有选中项
                $(this).datagrid("selectRow", rowIndex); //根据索引选中该行	                
                $('#adviceListMenu').menu('show', {//显示右键菜单
                    left: e.pageX,//在鼠标点击处显示菜单
                    top: e.pageY
                });
            }
		});
	}
	//加载标签页
	function loadTabss(inpatientNo){	
		if($("#inpatientId").val()!=""){
			loadDatagridC(1,inpatientNo);
			loadDatagridD(0,inpatientNo);
		}
		$('#tttb').tabs({
		    border:false,
		    onSelect:function(title,index){			    	
		    	var qq = $('#tttb').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){					
					loadDatagridC(1,inpatientNo);
				}else{										
					loadDatagridD(0,inpatientNo);					
				}
		    }
		});
	}
		
	</script>
	</body>
</html>