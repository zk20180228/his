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
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="height:42px;">
				<input type="hidden" id="adStackInfoModelStackId" value="${stackId }">
				<table style="padding: 5px 5px 5px 5px;">
					<tr>
						<td>
							选择药房：
					    	<input id="pharmacyStackInfo" class="easyui-combobox" style="width:120px;height:25px;">
							<a href="javascript:void(0)" onclick="adStackInfoModelYes()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确定</a>
							<a href="javascript:void(0)" onclick="adStackInfoModelCan()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
				    		
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center'">
				<table id="adStackInfoModelDgId" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true,border:false"> 
					<thead>   
				        <tr>   
				        	<th data-options="field:'ck',checkbox:true" ></th>
				            <th data-options="field:'name',width:180,align:'right',halign:'center'">医嘱名称</th>   
				            <th data-options="field:'spec',width:100,align:'right',halign:'center'">规格</th>  
				            <th data-options="field:'drugRetailprice',width:70,align:'right',halign:'center'">价格</th>   
				            <th data-options="field:'intervaldays',width:100,align:'right',halign:'center'">间隔时间</th>  
	<!-- 			            <th data-options="field:'3',width:70,align:'center'">每剂数量</th>   -->
	<!-- 			            <th data-options="field:'frequencyCode',width:70,align:'center'">频次</th>    -->
				            <th data-options="field:'stackInfoNum',width:70,align:'right',halign:'center'">数量</th>   
				            <th data-options="field:'days',width:70,align:'right',halign:'center'">付数</th>  
	<!-- 			            <th data-options="field:'usageCode',width:70,align:'center'">用法</th>    -->
	<!-- 			            <th data-options="field:'typeCode',width:70,align:'center'">医嘱类型</th>    -->
	<!-- 			            <th data-options="field:'5',width:70,align:'center'">加急</th>   -->
				            <th data-options="field:'dateBgn',width:170,align:'right',halign:'center'">开始时间</th>   
	<!-- 			            <th data-options="field:'6',width:70,align:'center'">开立时间</th>   -->
				            <th data-options="field:'unDrugDept',width:120,align:'right',halign:'center',formatter:deptStackFamater">执行科室</th>   
	<!-- 			            <th data-options="field:'dateEnd',width:100,align:'center'">停止时间</th>    -->
	<!-- 			            <th data-options="field:'7',width:70,align:'center'">停止医生</th>   -->
				            <th data-options="field:'stackInfoRemark',width:100,align:'right',halign:'center'">备注</th>   
	<!-- 			            <th data-options="field:'stackInfoOrder',width:50,align:'center'">顺序号</th>    -->
				        </tr>   
				    </thead>   
				</table>
			</div>
			<div data-options="region:'south',border:false" style="height:23px;" class="divLabel">
				&nbsp;&nbsp;<span style="font-size:12px">停用药品：</span><span style="height:8px;line-height:6px;display:inline-block;background-color: #FF9191">&nbsp;&nbsp;</span>
				&nbsp;&nbsp;<span style="font-size:12px">药房缺药：</span><span style="height:8px;line-height:6px;display:inline-block;background-color: #9D9DFF">&nbsp;&nbsp;</span>
				&nbsp;&nbsp;<span style="font-size:12px">职级不符：</span><span style="height:8px;line-height:6px;display:inline-block;background-color: #FFB08A">&nbsp;&nbsp;</span>
			</div>
		</div>
		<script type="text/javascript">
			var projMap = new Map();
			$(function(){
				$('#pharmacyStackInfo').combobox({    
					url:"<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action",
				    valueField:'id',    
				    textField:'deptName',
				    multiple:false,
				    editable:false,
				    onLoadSuccess: function (data) {
				    	$('#pharmacyStackInfo').combobox('select',$('#pharmacyCombobox').combobox('getValue'));
			        },
			        onSelect:function(record){
			        	$.post("<%=basePath%>outpatient/advice/savaPharmacyInfo.action",
						        {"pharmacyId":record.id},
						        function(dataMap){
						   			if(dataMap.resMsg=="error"){
						   				$.messager.alert('提示',dataMap.resCode);
						        	}else if(dataMap.resMsg=="success"){
						        		$('#pharmacyCombobox').combobox('select',record.id);
						        		$('#adStackInfoModelDgId').datagrid('load');
						        	}else{
						        		$.messager.alert('提示','未知错误,请联系管理员!');
						        	}
						   		}
						);
			        }
				}); 
				$('#adStackInfoModelDgId').datagrid({
					pagination:false,
					url:"<%=basePath%>outpatient/updateStack/getStackInfoByInfoIdForView.action",
					queryParams:{infoId:$('#adStackInfoModelStackId').val()},
					rowStyler: function(index,row){
						if(row.ty==1){
							if (row.storeSum==null||row.storeSum==''||row.storeSum-row.preoutSum==0){//库存不足
								return 'background-color:#9D9DFF;color:#fff;';
							}
							if (row.stop_flg==1){//停用
								return 'background-color:#FF9191;color:#fff;';
							}
// 							if(row.drugGrade!=null&&row.drugGrade!=''&&judgeDrugGradeMap[row.drugGrade]!=null&&judgeDrugGradeMap[row.drugGrade]!=''){
// 								if(judgeDocDrugGradeMap[judgeDrugGradeMap[row.drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[row.drugGrade]]==''){
// 									return 'background-color:#FFB08A;color:#fff;';
// 								}
// 							}
						}
					},
					onLoadSuccess:function(){
						$('#adStackInfoModelDgId').datagrid('checkAll');
					}
				});	
				var projData = $('#adProjectTdId').combobox('getData');
				if(projData!=null&&projData.length>0){
					for(var i=0;i<projData.length;i++){
						if(projData[i].code!='0'){
							projMap.put(projData[i].code,projData[i].name);
						}
					}
				}
			});
			//取消
			function adStackInfoModelCan(){
				$('#chinMediModleDivId').dialog('close');
			}
			function deptStackFamater(value,row,index){
				return colorInfoExeDeptMap.get(value);
			}
		</script>
	</body>
</html>
