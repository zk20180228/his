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
		<div style="padding:5px 5px 5px 5px;">
			<input type="hidden" id="adStackInfoModelStackId" value="${inpatientOrder.id }">
			<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px 5px 5px 5px;">
				<tr>
					<td style="width:270px;">
						<a href="javascript:void(0)" onclick="adStackInfoModelYes()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">确定</a>
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="adStackInfoModelCan()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
				 		&nbsp;&nbsp;&nbsp;&nbsp;
				 		<span style="background-color: #FF9191">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			    		<span style="font-size:14px">停用药品</span>&nbsp;&nbsp;&nbsp;&nbsp;
				 		<span style="background-color: #9D9DFF">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			    		<span style="font-size:14px">药房缺药</span>&nbsp;&nbsp;&nbsp;&nbsp;
				 		<span style="background-color: #FFB08A">&nbsp;&nbsp;&nbsp;&nbsp;</span>
			    		<span style="font-size:14px">职级不符</span>&nbsp;&nbsp;&nbsp;&nbsp;
			    		选择药房：
				    	<input id="pharmacyStackInfo" class="easyui-combobox" style="width:120px;height:25px;">
					</td>
				</tr>
			</table>
			<div style="height:5px"></div>
			<table id="adStackInfoModelDgId" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'100%',height:350"> 
				<thead>   
			        <tr>   
			        	<th data-options="field:'ck',checkbox:true" ></th>
			            <th data-options="field:'drugRetailprice',width:70,align:'center'">价格</th>   
			            <th data-options="field:'name',width:200,align:'center'">医嘱名称</th>   
			            <th data-options="field:'spec',width:100,align:'center'">规格</th>  
			            <th data-options="field:'intervaldays',width:70,align:'center'">间隔时间</th>  
 			            <th data-options="field:'drugOncedosage',width:70,align:'center'">每次剂量</th>   
 			            <th data-options="field:'drugFrequency',width:140,align:'center',formatter:drugFrequencyFamater">频次</th>    
			            <th data-options="field:'stackInfoNum',width:70,align:'center'">数量</th>   
			            <th data-options="field:'days',width:70,align:'center'">付数</th>  
 			            <th data-options="field:'drugUsemode',width:140,align:'center',formatter:drugUsemodeFamater">用法</th>    
 			            <th data-options="field:'typeCode',width:70,align:'center'">医嘱类型</th>    
 			            <th data-options="field:'5',width:70,align:'center'">加急</th>   
			            <th data-options="field:'dateBgn',width:140,align:'center'">开始时间</th>   
 			            <th data-options="field:'6',width:140,align:'center'">开立时间</th>  
			            <th data-options="field:'unDrugDept',width:70,align:'center',formatter:implDepartmentFamaters">执行科室</th>   
			            <th data-options="field:'dateEnd',width:140,align:'center'">停止时间</th>   
 			            <th data-options="field:'7',width:70,align:'center'">停止医生</th>   
			            <th data-options="field:'stackInfoRemark',width:70,align:'center'">备注</th>   
			            <th data-options="field:'stackInfoOrder',width:50,align:'center'">顺序号</th> 	
			            <th data-options="field:'drugDoseunit',width:50,align:'center'"></th> 		 
			        </tr>   
			    </thead>   
			</table>
		</div>
		<input id="userName" type="hidden" value="${inpatientOrder.docName }"/>
		<input id="deptId" type="hidden" value="${inpatientOrder.listDpcd }"/>
		<input id="recUserName" type="hidden" value="${inpatientOrder.recUsernm }"/>
		<script type="text/javascript">
		
			var judgeDocDrugGradeMap = null;
			var judgeDrugGradeMap = null;
			var judgeDrugGradeAllMap = null;	

 			$.ajax({
 				url: "<%=basePath%>outpatient/advice/queryJudgeDocDrugGradeMap.action",
				type:'post',
				success: function(date) {
					judgeDocDrugGradeMap = date;
				}
			});
			$.ajax({
				url: "<%=basePath%>outpatient/advice/queryJudgeDrugGradeMap.action",
				type:'post',
				success: function(date) {
					judgeDrugGradeMap = date;
				}
			});
			$.ajax({
				url: "<%=basePath%>outpatient/advice/queryJudgeDrugGradeAllMap.action",
				type:'post',
				success: function(date) {
					judgeDrugGradeAllMap = date;
				}
			}); 
			
			$(function(){
				//查询系统类别
				$.ajax({
					url: "<%=basePath%>inpatient/docAdvManage/querySystemtype.action",				
					type:'post',
					success: function(systemTypedata) {					
						systemTypeMap = systemTypedata;										
					}
				});	
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
					queryParams:{infoId:$('#adStackInfoModelStackId').val(),drugstoreId:$('#pharmacyInputId').val()},
					rowStyler: function(index,row){
						if(row.ty==1){
							if (row.storeSum==null||row.storeSum==''||row.storeSum-row.preoutSum==0){//库存不足
								return 'background-color:#9D9DFF;color:#fff;';
							}
							if (row.stop_flg==1){//停用
								return 'background-color:#FF9191;color:#fff;';
							}
							if(row.drugGrade!=null&&row.drugGrade!=''&&judgeDrugGradeMap[row.drugGrade]!=null&&judgeDrugGradeMap[row.drugGrade]!=''){
								if(judgeDocDrugGradeMap[judgeDrugGradeMap[row.drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[row.drugGrade]]==''){
									return 'background-color:#FFB08A;color:#fff;';
								}
							}
						}
					},
					onLoadSuccess:function(){
                        $('#adStackInfoModelDgId').datagrid('checkAll');
                    }
				});				
			});
			//确定    组套确定按钮
			function adStackInfoModelYes(){
				var rows = $('#adStackInfoModelDgId').datagrid('getChecked');
				if(rows!=null&&rows.length>0){	
					var a = 0;
					for(var i=0;i<rows.length;i++){						
						if(rows[i].ty==1){
							if (rows[i].storeSum==null||rows[i].storeSum==''||rows[i].storeSum-rows[i].preoutSum==0){//库存不足
								$.messager.alert("提示","【"+rows[i].name+"】库存不足，无法添加！");
								return;
							}
							if (rows[i].stop_flg==1){//停用
								$.messager.alert("提示","【"+rows[i].name+"】已停用，无法添加！");
								return;
							}
							if(rows[i].ty==1&&rows[i].drugGrade!=null&&rows[i].drugGrade!=''){
								if(judgeDrugGradeMap[rows[i].drugGrade]==null||judgeDrugGradeMap[rows[i].drugGrade]==''){
									$.messager.alert('提示','【'+rows[i].name+'】的等级信息错误，请联系管理员！');
									return;
								}else{
									if(judgeDocDrugGradeMap[judgeDrugGradeMap[rows[i].drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[rows[i].drugGrade]]==''){
										$.messager.alert('提示','您无法开立药品【'+rows[i].name+'】，该药品等级为【'+judgeDrugGradeAllMap[rows[i].drugGrade]+'】，请重新选择！');
										return;
									}
								}
							}
						}						
						var data = $('#adProjectTdId').combobox('getData'); 
						for(var j=0;j<data.length;j++){
							if(rows[i].drugSystype==data[j].code){
								a = 1;
							}
						}
						if(a==0){
							$.messager.alert("提示","系统类别中不存在【"+systemTypeMap[rows[i].drugSystype]+"】，无法添加！");
							return;
						}
					}
					var isSkinHid = 0;
					var isSkin = '否';
					for(var i=0;i<rows.length;i++){
						if(rows[i].ty==1&&rows[i].drugIstestsensitivity!=null&&rows[i].drugIstestsensitivity!=0){
							$.messager.defaults={
				    				ok:'是',
				    				cancel:'否',
				    				width:250,
				    				collapsible:false,
				    				minimizable:false,
				    				maximizable:false,
				    				closable:false
				    		};
							$.messager.confirm('提示信息', '【'+rows[i].name+'】是否皮试？', function(r){
								if (r){
									isSkinHid = 1;
									isSkin = '是';
								}
							});
						}
					}
					addDatagridOfStack(rows,isSkinHid,isSkin);
					$('#stackModleDivId').dialog('close');
				}else{
					$.messager.alert('提示',"请选择组套信息!");
				}
			}
			//取消
			function adStackInfoModelCan(){
				$('#stackModleDivId').dialog('close');
			}
			function addDatagridOfStack(rows,isSkinHid,isSkin){					
				var rowsAll = $('#adStackInfoModelDgId').datagrid('getRows');
				var gV = null;
				if(rows.length>1){
					if(rows.length==rowsAll.length){
						gV = groupVal;
						groupVal += 1;
					}
				}	
				var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
				for(var i=0;i<rows.length;i++){
					var qq = $('#ttta').tabs('getSelected');				
					 var tab = qq.panel('options');
					 if(tab.title=='长期医嘱'){
						 var lastIndex = $('#infolistA').datagrid('appendRow',{
							 	id:'',
								itemCode:'',//特殊标识
								typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱类型代码
								typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型名称	
								classCode:rows[i].drugSystype,//类型	 
								className:systemTypeMap[rows[i].drugSystype],//类型名称
								itemCode:rows[i].id,//医嘱名称Id
								itemName:rows[i].name,//医嘱名称
								combNo:gV,//组
								qtyTot:rows[i].stackInfoNum,//总量
								priceUnit:rows[i].stackInfoUnit,//总单位Id								
								drugpackagingUnit:rows[i].drugPackagingunit,//包装单位
								minUnit:rows[i].unit,//最小单位
								baseDose:rows[i].ty==1?rows[i].drugBasicdose:'',//基本剂量
								doseUnit:rows[i].ty==1?rows[i].drugDoseunit:'',//剂量单位
								doseOnce:rows[i].ty==1?rows[i].drugOncedosage:'',//每次用量
								specs:rows[i].spec,//规格				
								packQty:rows[i].packagingnum,//包装数量
								drugType:rows[i].ty==1?rows[i].drugType:'',//药品类型
								drugQuality:rows[i].ty==1?rows[i].drugNature:'',//药品性质
								itemPrice:rows[i].drugRetailprice!=null?rows[i].drugRetailprice:'',//价格
								doseModelCode:rows[i].ty==1?rows[i].drugDosageform:'',//剂型																							
						//		dosageHid:(rows[i].ty==1&&rows[i].drugType!='4028809a50ad96320150ad9a196d0004')?(rows[i].drugOncedosage==null?1:rows[i].drugOncedosage):null,//每次用量
						//		unit:colorInfoUnitsMap.get(rows[i].unit),//单位
								useDays:rows[i].drugType=='4028809a50ad96320150ad9a196d0004'?rows[i].days:'',//付数
								frequencyCode:rows[i].drugFrequency,//频次Id
								frequencyName:frequencyMap[rows[i].drugFrequency],//频次编码
								usageCode:rows[i].drugUsemode,//用法Id
								useName:usemodeMap[rows[i].drugUsemode],//用法名称						
								execDpcd:rows[i].ty==1?$('#pharmacyInputId').val():rows[i].unDrugDept,//执行科室Id
								execDpnm:rows[i].ty==1?$('#pharmacyInputName').val():implDepartmentMap[rows[i].unDrugDept],//执行科室
								emcFlag:0,//加急Id
								isUrgent:0,//加急
								itemNote:rows[i].unDrugInspectionsite,//检查部位Id
								labCode:rows[i].labsample,//样本类型
								pharmacyCode:$('#pharmacyInputId').val(),//扣库科室Id
								moNote2:rows[i].drugRemark,//备注
								recUsernm:$('#recUserName').val(),//录入人
								listDpcd:$('#deptId').val(),//开立科室 
								dateBgn:rows[i].dateBgn,//开始时间 
								dateEnd:rows[i].dateEnd,//停止时间
								moDate:mytime,//开立时间
								docName:$('#userName').val(),//开立医生
								//endTime:rows[i],//停止时间
								//stopPeop:rows[i],//停止人
								permission:rows[i].unDrugIsinformedconsent,//是否知情同意书
								hypotest:isSkinHid,//是否需要皮试Id
								hypotestName:isSkin,//是否需要皮试
								moStat:0,//医嘱状态
								itemType:rows[i].ty,
								sortId:sortIdCreate()//顺序号	
							}).datagrid('getRows').length-1;
						 	$('#infolistA').datagrid('selectRow',lastIndex);
						 	if(i==0){
						 		$('#infolistA').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNoFlag:'┓'
									}
								});
						 	}else if(i==rows.length-1){
						 		$('#infolistA').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNoFlag:'┛'
									}
								});
						 	}else{
						 		$('#infolistA').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNoFlag:'┃'
									}
								});
						 	}
					 }else if(tab.title=='临时医嘱'){
							var lastIndex = $('#infolistB').datagrid('appendRow',{
								id:'',
								itemCode:'',//特殊标识
								typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱类型代码
								typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型名称	
								classCode:rows[i].drugSystype,//类型	
								className:systemTypeMap[rows[i].drugSystype],//类型名称
								itemCode:rows[i].id,//医嘱名称Id
								itemName:rows[i].name,//医嘱名称
								combNo:gV,//组
								qtyTot:rows[i].stackInfoNum,//总量
								priceUnit:rows[i].stackInfoUnit,//总单位Id								
								drugpackagingUnit:rows[i].drugPackagingunit,//包装单位
								minUnit:rows[i].unit,//最小单位
								baseDose:rows[i].ty==1?rows[i].drugBasicdose:'',//基本剂量
								doseUnit:rows[i].ty==1?rows[i].drugDoseunit:'',//剂量单位
								doseOnce:rows[i].ty==1?rows[i].drugOncedosage:'',//每次用量
								specs:rows[i].spec,//规格				
								packQty:rows[i].packagingnum,//包装数量
								drugType:rows[i].ty==1?rows[i].drugType:'',//药品类型
								drugQuality:rows[i].ty==1?rows[i].drugNature:'',//药品性质
								itemPrice:rows[i].drugRetailprice!=null?rows[i].drugRetailprice:'',//价格
								doseModelCode:rows[i].ty==1?rows[i].drugDosageform:'',//剂型																										
						//		dosageHid:(rows[i].ty==1&&rows[i].drugType!='4028809a50ad96320150ad9a196d0004')?(rows[i].drugOncedosage==null?1:rows[i].drugOncedosage):null,//每次用量
						//		unit:colorInfoUnitsMap.get(rows[i].unit),//单位
								useDays:rows[i].drugType=='4028809a50ad96320150ad9a196d0004'?rows[i].days:'',//付数
								frequencyCode:rows[i].drugFrequency,//频次Id
								frequencyName:frequencyMap[rows[i].drugFrequency],//频次编码
								usageCode:rows[i].drugUsemode,//用法Id
								useName:usemodeMap[rows[i].drugUsemode],//用法名称						
								execDpcd:rows[i].ty==1?$('#pharmacyInputId').val():rows[i].unDrugDept,//执行科室Id
								execDpnm:rows[i].ty==1?$('#pharmacyInputName').val():implDepartmentMap[rows[i].unDrugDept],//执行科室
								emcFlag:0,//加急Id
								isUrgent:0,//加急
								itemNote:rows[i].unDrugInspectionsite,//检查部位Id
								labCode:rows[i].labsample,//样本类型
								pharmacyCode:$('#pharmacyInputId').val(),//扣库科室Id
								moNote2:rows[i].drugRemark,//备注
								recUsernm:$('#recUserName').val(),//录入人
								listDpcd:$('#deptId').val(),//开立科室 
								dateBgn:rows[i].dateBgn,//开始时间 
								dateEnd:rows[i].dateEnd,//停止时间
								moDate:mytime,//开立时间
								docName:$('#userName').val(),//开立医生
								//endTime:rows[i],//停止时间
								//stopPeop:rows[i],//停止人
								permission:rows[i].unDrugIsinformedconsent,//是否知情同意书
								hypotest:isSkinHid,//是否需要皮试Id
								hypotestName:isSkin,//是否需要皮试
								moStat:0,//医嘱状态
								itemType:rows[i].ty,
								sortId:sortIdCreate()//顺序号	
							}).datagrid('getRows').length-1;
							if(i==0){
						 		$('#infolistB').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNoFlag:'┓'
									}
								});
						 	}else if(i==rows.length-1){
						 		$('#infolistB').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNoFlag:'┛'
									}
								});
						 	}else{
						 		$('#infolistB').datagrid('updateRow',{
									index: lastIndex,
									row: {
										combNoFlag:'┃'
									}
								});
						 	}
							$('#infolistB').datagrid('selectRow',lastIndex);							
					 }
				}
			}
			//系统类别 列表页 显示	
			function sysTypeFamater(value,row,index){			
				if(value!=null&&value!=""){
					return systemTypeMap[value];									
				}			
			}
			//药品频次列表页 显示		
			function drugFrequencyFamater(value,row,index){			
				if(value!=null&&value!=""){
					return frequencyMap[value];					
				}			
			}			
			//药品用法列表页 显示		
			function drugUsemodeFamater(value,row,index){			
				if(value!=null&&value!=""){
					return usemodeMap[value];					
				}			
			}
		</script>
	</body>
</html>
