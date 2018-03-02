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
			<div style="border: 1px solid #95b8e7; padding: 5px 5px 5px 5px;">
				<a href="javascript:void(0)" onclick="adChinMediInfoModelAdd()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:false">新开</a>
				<a href="javascript:void(0)" onclick="adChinMediInfoModelDel()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:false">删除</a>
				<a href="javascript:void(0)" onclick="adChinMediInfoModelSta()" class="easyui-linkbutton" data-options="iconCls:'icon-application_osx_link',plain:false">保存组套</a>
				<a href="javascript:void(0)" onclick="adChinMediInfoModelYes()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:false">确定</a>
				<a href="javascript:void(0)" onclick="adChinMediInfoModelCan()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:false">取消</a>
			</div>
			<div style="height:5px"></div>
			<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px 5px 5px 5px;">
				<tr>
					<td style="width:70px" align="right">医嘱类型：</td>
					<td style="width:100px" id="docTypeId"></td>
					<td style="width:70px" align="right">频次：</td>
			 		<td style="width:170px"><input id="adChinMediInfoModelFreTdId" class="easyui-combobox" data-options="" style="width:155px;"/></td>
					<td style="width:70px" align="right">开始时间：</td>
					<td style="width:170px"><input id="adChinMediInfoModelStaTdId" value="<fmt:formatDate value="${staDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" class="easyui-datetimebox" style="width:155px"></td>
					<td style="width:70px" align="right">选择药房：</td>
				    <td style="width:135px"><input id="pharmacyChinMediInfo" class="easyui-combobox" style="width:120px;height:25px;"></td>
				</tr>
				<tr>
					<td align="right">付数：</td>
					<td><input id="adChinMediInfoModelNumTdId" class="easyui-numberbox" value="1" data-options="min:1,max:999" style="width:90px;"/></td>
					<td align="right">煎药方式：</td>
					<td><input id="adChinMediInfoModelModTdId" class="easyui-combobox" data-options="" style="width:155px;"/></td>
					<td align="right">结束时间：</td>
					<td><input id="adChinMediInfoModelEndTdId" class="easyui-datetimebox"  style="width:155px"></td>
					<td></td>
					<td></td>
					<td></td>
				    <td></td>
				</tr>
			</table>
			<div style="height:5px"></div>
			<table style="padding:5px 5px 5px 5px;" id="adChinMediInfoModelDgId" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,width:'100%',height:280"> 
			</table>
		</div>
		<input type="hidden" id="inpId">
		<div id="adChinMediInfoModelWinDivId"></div>
		<script type="text/javascript">
		
			$(function(){
				var qq = $('#ttta').tabs('getSelected');				
				var tab = qq.panel('options');
				if(tab.title=='长期医嘱'){
					$('#docTypeId').html($('#longDocAdvType').combobox('getText'));
				}else if(tab.title=='临时医嘱'){
					$('#docTypeId').html($('#temDocAdvType').combobox('getText'));
				}
				
    			$('#adChinMediInfoModelFreTdId').combobox({//频次
				 	url:"<%=basePath%>baseinfo/frequency/likeFrequencyAll.action",
				    valueField:'encode',    
				    textField:'name',
				    editable:false,
					disabled:false   
				});
				$('#adChinMediInfoModelModTdId').combobox({//煎药方式
					url:"<%=basePath%>outpatient/advice/querBoilmedicineway.action",
				    valueField:'encode',    
				    textField:'name',
				    editable:false,
					disabled:false   
				});
				$('#pharmacyChinMediInfo').combobox({    
					url:"<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action",
				    valueField:'id',
				    textField:'deptName',
				    multiple:false,
				    editable:false,
				    onLoadSuccess: function (data) {
				    	$('#pharmacyChinMediInfo').combobox('select',$('#pharmacyInputId').val());
			        },
			        onSelect:function(record){
			        	$.post("<%=basePath%>outpatient/advice/savaPharmacyInfo.action",
					        {"pharmacyId":record.id},
					        function(data){
					        	var dataMap = eval("("+data+")");
					   			if(dataMap.resMsg=="error"){
					   				$('#pharmacyCombobox').combobox('select',$('#pharmacyInputId').val());
					   				$('#pharmacyChinMediInfo').combobox('select',$('#pharmacyInputId').val());
					   				$.messager.alert('提示',dataMap.resCode);
					        	}else if(dataMap.resMsg=="success"){
					        		$('#pharmacyInputId').val(record.id);
					        		$('#pharmacyInputName').val(record.deptName);
					        		$('#pharmacyCombobox').combobox('select',record.id);
					        		var rows = $('#adChinMediInfoModelDgId').datagrid('getRows');
					        		if(rows!=null&&rows.length>0){
					        			var leng = rows.length;
					        			for(var i=0;i<leng;i++ ){
					        				$('#adChinMediInfoModelDgId').datagrid('deleteRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[0]));
					        			}
					        		}
					        		
					        	}else{
					        		$('#pharmacyCombobox').combobox('select',$('#pharmacyInputId').val());
					        		$('#pharmacyChinMediInfo').combobox('select',$('#pharmacyInputId').val());
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	}
					   		}
						);
			        }
				}); 
    			setTimeout(function(){
    				$('#adChinMediInfoModelDgId').edatagrid({
    					method:'post',
    					striped:true,
    					border:false,
    					checkOnSelect:true,
    					selectOnCheck:false,
    					singleSelect:true,
    					fitColumns:false,	
    					columns:[[
							{field:'ckgtfer',checkbox:'true'},  
							{field:'id',title:'id',hidden:true,width:'150'},
							{field:'name',title:'药品名称',width:'150',
								editor:{
									type:'combogrid',
									options:{
										required : true,
										rownumbers : true,//显示序号 
										pagination : true,//是否显示分页栏
										striped : true,//数据背景颜色交替
										panelWidth : 550,//容器宽度
										fitColumns : true,//自适应列宽
										mode:'remote',
										pageSize : 5,//每页显示的记录条数，默认为10  
										pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
										url:"<%=basePath%>outpatient/advice/queryWestMediInfoModel.action",
										idField:'encode',    
							            textField:'name',
							            columns:[[    
									                {field:'name',title:'名称',width:100},    
									                {field:'spec',title:'规格',width:100},    
									                {field:'price',title:'价格',width:100},
									                {field:'commonName',title:'通用名',width:100},
									            ]],
							            rowStyler: function(index,row){
							            	if (row.ty==1&&row.surSum<=0){//库存不足
												return 'background-color:#9D9DFF;color:#fff;';
											}
											if (row.stop_flg==1){//停用
												return 'background-color:#FF9191;color:#fff;';
											}
											if(row.ty==1&&row.drugGrade!=null&&row.drugGrade!=''&&judgeDrugGradeMap[row.drugGrade]!=null&&judgeDrugGradeMap[row.drugGrade]!=''){
												if(judgeDocDrugGradeMap[judgeDrugGradeMap[row.drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[row.drugGrade]]==''){
													return 'background-color:#FFB08A;color:#fff;';
												}
											}
										},onClickRow:function(rowIndex, rowData){
							            	if(rowData.ty==1&&rowData.surSum<=0){
												$('#adChinMediInfoModelDgId').datagrid('deleteRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected')));
												adChinMediInfoModelAdd();
												$.messager.alert('提示','【'+rowData.name+'】库存不足,请重新选择！');
												return;
											}
							            	if (rowData.stop_flg==1){//停用
												$('#adChinMediInfoModelDgId').datagrid('deleteRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected')));
												adChinMediInfoModelAdd();
												$.messager.alert('提示','【'+rowData.name+'】已停用,请重新选择！');
												return;
											}
							            	if(rowData.ty==1){
												if(rowData.drugGrade!=null&&rowData.drugGrade!=''){
													if(judgeDrugGradeMap[rowData.drugGrade]==null||judgeDrugGradeMap[rowData.drugGrade]==''){
														$.messager.alert('提示','【'+rowData.name+'】的等级信息错误，请联系管理员！');
														return;
													}else{
														if(judgeDocDrugGradeMap[judgeDrugGradeMap[rowData.drugGrade]]==null||judgeDocDrugGradeMap[judgeDrugGradeMap[rowData.drugGrade]]==''){
															$('#adChinMediInfoModelDgId').datagrid('deleteRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected')));
															adChinMediInfoModelAdd();
															$.messager.alert('提示','您无法开立药品【'+rowData.name+'】，该药品等级为【'+judgeDrugGradeAllMap[rowData.drugGrade]+'】，请重新选择！');
															return;
														}
													}
												}
											}
							            	$('#adChinMediInfoModelDgId').datagrid('updateRow',{
												index: $('#inpId').val(),
												row: {
													id:rowData.code,
													name:rowData.name,
													spec:rowData.spec,//规格
													price:rowData.price,//价格
													numb:1,//数量
													unitId:rowData.unit,//单位
													unit:colorInfoUnitsMap.get(rowData.unit),//单位
													useId:rowData.usemode,//用法Id
													use:usemodeMap[rowData.usemode],//用法
													adPackUnitHid:rowData.unit,//包装单位
													minimumUnit:rowData.minimumUnit,//最小单位
													doseunit:rowData.doseunit,//剂量
													basicdose:rowData.basicdose,//基本剂量
													sysType:rowData.sysType,//系统类别
													type:rowData.type,//药品类别
													minimumcost:rowData.minimumcost,//最小费用代码
													packagingnum:rowData.packagingnum,//包装数量
													nature:rowData.nature,//药品性质
													ismanufacture:rowData.ismanufacture,//自制药标志
													dosageform:rowData.dosageform,//剂型
													oncedosage:rowData.oncedosage,//每次用量
													isProvincelimit:rowData.isProvincelimit,//是否省限制
													isCitylimit:rowData.isProvincelimit,//是否市限制
													restrictionofantibiotic:(rowData.restrictionofantibiotic==null||rowData.restrictionofantibiotic=='')?'':rowData.restrictionofantibiotic//抗菌药限制
												}
											});
							            	$('#adChinMediInfoModelDgId').edatagrid('endEdit',$('#inpId').val());
							            	$('#adChinMediInfoModelDgId').edatagrid('beginEdit',$('#inpId').val());
										}	
									}
								}
							},
							{field:'spec',title:'规格',width:'150'},
					        {field:'price',title:'价格',width:'150'},
					        {field:'numb',title:'数量',editor:{type:'numberbox',options:{precision:0,required:true}},width:'150'},
					        {field:'unitId',title:'单位Id',editor:{type:'textbox'},width:'150'},
					        {field:'unit',title:'单位',width:'150',
					        	editor:{
					        		type:'combobox',
					        		options:{
					        			data:colorInfoUnitsList,
								    	editable:false,
								    	disabled:false,
								    	required:true,
									    valueField:'encode',    
									    textField:'name',
									    onSelect:function(record){
									    	var unitId = $('#adChinMediInfoModelDgId').edatagrid('getEditor', {index:$('#inpId').val(),field:'unitId'});
 											$(unitId.target).textbox('setValue',record.encode);											
									    }
					        		}
					        	}
					        },
					        {field:'useId',title:'用法Id',editor:{type:'textbox'},width:'150'},
					        {field:'use',title:'用法',width:'150',
					        	editor:{ 
					        		type:'combogrid',
					        		options:{
					        			url:'<%=basePath%>inpatient/doctorAdvice/likeUseage.action', 
					        			required : true,
										fitColumns : true,//自适应列宽
										mode:'remote',
						        		panelWidth:150,    
							            idField:'encode',    
							            textField:'name', 
							            columns:[[    
									                {field:'name',title:'名称',width:150},    
									                {field:'encode',title:'code',width:100,hidden:true}   
									            ]],
									        onClickRow:function(rowIndex, rowData){
								        	var useId = $('#adChinMediInfoModelDgId').edatagrid('getEditor', {index:$('#inpId').val(),field:'useId'});
											$(useId.target).textbox('setValue',rowData.encode);
									    }
					        		}
					        	}
					        },
					        {field:'adPackUnitHid',title:'包装单位',hidden:true},
					        {field:'minimumUnit',title:'最小单位',hidden:true},
					        {field:'doseunit',title:'剂量',hidden:true},
					        {field:'basicdose',title:'基本剂量',hidden:true},
					        {field:'sysType',title:'系统类别',hidden:true},
					        {field:'type',title:'药品类别',hidden:true},
					        {field:'minimumcost',title:'最小费用代码',hidden:true},
					        {field:'packagingnum',title:'包装数量',hidden:true},
					        {field:'nature',title:'药品性质',hidden:true},
					        {field:'ismanufacture',title:'自制药标志',hidden:true},
					        {field:'dosageform',title:'剂型',hidden:true},
					        {field:'oncedosage',title:'每次用量',hidden:true},
					        {field:'isProvincelimit',title:'是否省限制',hidden:true},
					        {field:'isCitylimit',title:'是否市限制',hidden:true},
					        {field:'restrictionofantibiotic',title:'抗菌药限制',hidden:true}
    					]] ,
    					onBeforeEdit:function(index,row){
    						$('#inpId').val(index);
    					},
					});
    			},500);
			});
			//新开
			function adChinMediInfoModelAdd(){
				$('#adChinMediInfoModelDgId').datagrid('appendRow', {});
			}
			//删除
			function adChinMediInfoModelDel(){
				var rows = $('#adChinMediInfoModelDgId').datagrid('getChecked');
				if(rows!=null&&rows.length>0){
					for(var i=0;i<rows.length;i++){
						$('#adChinMediInfoModelDgId').datagrid('deleteRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i]));
					}
				}else{
					$.messager.alert('提示','请选择要删除的草药信息！');				
				}
			}
			//保存组套
			function adChinMediInfoModelSta(){
				$('#adChinMediInfoModelDgId').datagrid('unselectRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected')));
				var rows = $('#adChinMediInfoModelDgId').datagrid('getRows');
				if(rows!=null&&rows.length>0){
					if(rows.length==1){
						$.messager.alert('提示',"添加组套至少需要两条信息!");
						return
					}else{
						for(var i=0;i<rows.length;i++){
							if(rows[i].name==null||rows[i].name==''){
								$('#adChinMediInfoModelDgId').datagrid('unselectAll');
								var rowIndex = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i])
								$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
								$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
								$.messager.alert('提示','请选择草药信息！');		
								return;
							}
							if(rows[i].unitId==null||rows[i].unitId==''){
								$('#adChinMediInfoModelDgId').datagrid('unselectAll');
								var rowIndex = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i])
								$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
								$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
								$.messager.alert('提示','请填写【'+rows[i].name+'】的单位！');		
								return;
							}
							if(rows[i].useId==null||rows[i].useId==''){
								$('#adChinMediInfoModelDgId').datagrid('unselectAll');
								var rowIndex = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i])
								$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
								$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
								$.messager.alert('提示','请填写【'+rows[i].name+'】的用法！');	
								return;
							}
						}
						var setNumVal = $('#adChinMediInfoModelNumTdId').numberbox('getText');
						if(setNumVal==null||setNumVal==''){
							$.messager.alert('提示','付数不能为空！');
							return;
						}
						var frequencyHidVal = $('#adChinMediInfoModelFreTdId').combobox('getValue');
						var frequencyVal = $('#adChinMediInfoModelFreTdId').combobox('getText');
						if(frequencyHidVal==null||frequencyHidVal==''){
							$.messager.alert('提示','请选择频次！');
							return;
						}
						var staDate = $('#adChinMediInfoModelStaTdId').datetimebox('getValue');
						var endDate = $('#adChinMediInfoModelEndTdId').datetimebox('getValue');
						$('#stackSaveModleDivId').show();
						$('#stackSaveModleDivId').dialog({    
						    title:'确认组套信息',    
						    width:'80%',    
						    height:'50%',    
						    closed: false,
						    closable:false,    
						    cache: false,
						    modal: true   
						});
						StackInfoSource();
						for(var i=0;i<rows.length;i++){
							$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
								id:rows[i].id,//名称
								name:rows[i].name,//名称
								spec:rows[i].spec,//规格
								packagingnum:rows[i].packagingnum,//包装数量
								unit:rows[i].unitId,//单位
								unitView:colorInfoUnitsMap.get(rows[i].unitId),//单位
								defaultprice:rows[i].price,//默认价
								frequencyCode:frequencyHidVal,//频次编码
								frequencyCodeView:frequencyVal,//频次编码
								usageCode:rows[i].useId,//用法名称
								usageCodeView:rows[i].use,//用法名称
								onceDose:rows[i].basicdose,//每次服用剂量
								doseUnit:rows[i].adDosaUnitHid,//剂量单位
								doseUnitView:colorInfoUnitsMap.get(rows[i].doseunit),//剂量单位
								mainDrugshow:1,//主药标记
								dateBgn:staDate,//医嘱开始时间
								dateEnd:endDate,//医嘱结束时间
								days:setNumVal,//草药付数
								isDrugShow:1,//是否药品
								isDrugShowView:'是',
								stackInfoNum:rows[i].numb//开立数量
							});
						} 
					}
				}else{
					$.messager.alert('提示',"请选择需要添加组套的信息!");
					return;
				}
			}
			//确定
			function adChinMediInfoModelYes(){
				$('#adChinMediInfoModelDgId').datagrid('unselectRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected')));
				var rows = $('#adChinMediInfoModelDgId').datagrid('getRows');
				if(rows==null||rows.length<=0){
					$.messager.alert('提示','草药信息不能为空！');
					return;
				}
				var usageId = null;//用法
				for(var i=0;i<rows.length;i++){
					if(rows[i].name==null||rows[i].name==''){
						$('#adChinMediInfoModelDgId').datagrid('unselectAll');
						var rowIndex = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i])
						$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
						$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
						$.messager.alert('提示','请选择草药信息！');		
						return;
					}
					if(rows[i].unitId==null||rows[i].unitId==''){
						$('#adChinMediInfoModelDgId').datagrid('unselectAll');
						var rowIndex = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i])
						$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
						$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
						$.messager.alert('提示','请填写【'+rows[i].name+'】的单位！');		
						return;
					}
					if(rows[i].useId==null||rows[i].useId==''){
						$('#adChinMediInfoModelDgId').datagrid('unselectAll');
						var rowIndex = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i])
						$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
						$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
						$.messager.alert('提示','请填写【'+rows[i].name+'】的用法！');	
						return;
					}
					if(usageId==null){
						usageId = rows[i].useId;
					}
					if(rows[i].useId!=usageId){
						$.messager.alert('提示',"所开草药的用法必须相同！");
						return;
					}
				}
				var setNumVal = $('#adChinMediInfoModelNumTdId').numberbox('getText');
				if(setNumVal==null||setNumVal==''){
					$.messager.alert('提示','付数不能为空！');
					return;
				}
				var frequencyHidVal = $('#adChinMediInfoModelFreTdId').combobox('getValue');
				var frequencyVal = $('#adChinMediInfoModelFreTdId').combobox('getText');
				if(frequencyHidVal==null||frequencyHidVal==''){
					$.messager.alert('提示','请选择频次！');
					return;
				}
				var modVal = $('#adChinMediInfoModelModTdId').combobox('getText');
				if(modVal==null||modVal==''){
					$.messager.alert('提示','请选择煎药方式！');
					return;
				}
				var staDate = $('#adChinMediInfoModelStaTdId').datetimebox('getValue');
				var endDate = $('#adChinMediInfoModelEndTdId').datetimebox('getValue');
				if(endDate==null||endDate==''){
					endDate = null;
				}
				var row = $('#adChinMediInfoModelDgId').datagrid('getSelected');
				if(row!=null){
					var index = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',row);
					$('#adChinMediInfoModelDgId').datagrid('unselectRow',index);
				}
				if(rows!=null&&rows.length>0&&rows[0].id!=null&&rows[0].id!=''){
					var bool = false;
					for(var i=0;i<rows.length;i++){
						if(rows[i].restrictionofantibiotic==3&&$('#isAuditing').val()=='false'&&(specialDrugMap[rows[i].id]==null||specialDrugMap[rows[i].id]=='')){
							bool = true;
						}
					}
					var gV = null;
					if(rows.length>1){
						gV = groupVal;
						groupVal += 1;
					}
					if(bool){
						var mytime = new Date().format("yyyy-MM-dd hh:mm:ss");
						$.messager.defaults={
			    				ok:'是',
			    				cancel:'否',
			    				width:300,
			    				collapsible:false,
			    				minimizable:false,
			    				maximizable:false,
			    				closable:false
			    		};
						$.messager.confirm('提示信息', '添加草药中含有抗菌特限药，需要上级医师进行审核，是否开立？', function(r){
							if (r){
								var img = img = "<div style='float:left;margin-top:1px;margin-left:-2px;width:15px;height:12px;background:url(<%=basePath%>/themes/system/images/button/shen1.png);background-repeat:no-repeat;'></div>";
								for(var i=0;i<rows.length;i++){
									if(rows[i].id!=null&&rows[i].id!=''){
										var qq = $('#ttta').tabs('getSelected');				
										var tab = qq.panel('options');
										if(tab.title=='长期医嘱'){	
											var lastIndex = $('#infolistA').datagrid('appendRow',{
												changeNo:1,
												classCode:rows[i].sysType,//类型
												className:'中草药',//系统类别名称
												itemType:1,//是否为药品
												typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型
												typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱名称Id
												itemName:rows[i].name,//医嘱名称Hid
												itemPrice:rows[i].price,//价格
												drugpackagingUnit:rows[i].adPackUnitHid,//包装单位
												minUnit:rows[i].minimumUnit,//最小单位
												doseUnit:rows[i].doseunit,//剂量
												baseDose:rows[i].basicdose,//基本剂量
												specs:rows[i].spec,//规格
												drugType:rows[i].type,//药品类别
												packQty:rows[i].packagingnum,//包装数量
												drugQuality:rows[i].nature,//药品性质
												doseModelCode:rows[i].dosageform,//剂型
												permission:0,//是否知情同意书
												combNo:gV,//组
												qtyTot:rows[i].numb*setNumVal,//总量
												priceUnit:rows[i].unitId,//总单位Id
												totalUnit:rows[i].unit,//总单位
												doseOnce:rows[i].numb,//每次用量
												useDays:setNumVal,//付数
												frequencyCode:frequencyHidVal,//频次Id
												frequencyName:frequencyVal,//频次编码
												usageCode:rows[i].useId,//用法Id
												useName:rows[i].use,//用法名称
												docName:$('#username').val(),//开立医生
												execDpcd:$('#pharmacyInputId').val(),//执行科室Id
												execDpnm:$('#pharmacyInputName').val(),//执行科室
												emcFlag:0,//加急Id
												pharmacyCode:$('#pharmacyInputId').val(),//扣库科室Id
												moNote2:modVal,//备注
												recUsernm:$('#username').val(),//录入人
												listDpcd:$('#deptId').val(),//开立科室
												dateBgn:staDate!=''?staDate:mytime,//开立时间
												dateEnd:endDate!=''?endDate:mytime,//结束时间
												moDate:mytime,//开立时间
												hypotest:1,//是否需要皮试Id
												hypotestName:'不需要皮试',//是否需要皮试
												moStat:0,//医嘱状态
												sortId:sortIdCreate()
											}).datagrid('getRows').length-1;
											$('#infolistA').datagrid('selectRow',lastIndex);
										}else if(tab.title=='临时医嘱'){
											var lastIndex = $('#infolistB').datagrid('appendRow',{
												changeNo:1,
												classCode:rows[i].sysType,//类型
												className:'中草药',//系统类别名称
												itemType:1,//是否为药品
												typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型
												typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱名称Id
												itemName:rows[i].name,//医嘱名称Hid
												itemPrice:rows[i].price,//价格
												drugpackagingUnit:rows[i].adPackUnitHid,//包装单位
												minUnit:rows[i].minimumUnit,//最小单位
												doseUnit:rows[i].doseunit,//剂量
												baseDose:rows[i].basicdose,//基本剂量
												specs:rows[i].spec,//规格
												drugType:rows[i].type,//药品类别
												packQty:rows[i].packagingnum,//包装数量
												drugQuality:rows[i].nature,//药品性质
												doseModelCode:rows[i].dosageform,//剂型
												permission:0,//是否知情同意书
												combNo:gV,//组
												qtyTot:rows[i].numb*setNumVal,//总量
												priceUnit:rows[i].unitId,//总单位Id
												totalUnit:rows[i].unit,//总单位
												doseOnce:rows[i].numb,//每次用量
												useDays:setNumVal,//付数
												frequencyCode:frequencyHidVal,//频次Id
												frequencyName:frequencyVal,//频次编码
												usageCode:rows[i].useId,//用法Id
												useName:rows[i].use,//用法名称
												docName:$('#username').val(),//开立医生
												execDpcd:$('#pharmacyInputId').val(),//执行科室Id
												execDpnm:$('#pharmacyInputName').val(),//执行科室
												emcFlag:0,//加急Id
												pharmacyCode:$('#pharmacyInputId').val(),//扣库科室Id
												moNote2:modVal,//备注
												recUsernm:$('#username').val(),//录入人
												listDpcd:$('#deptId').val(),//开立科室
												dateBgn:staDate!=''?staDate:mytime,//开立时间
												dateEnd:endDate!=''?endDate:mytime,//结束时间
												moDate:mytime,//开立时间
												hypotest:1,//是否需要皮试Id
												hypotestName:'不需要皮试',//是否需要皮试
												moStat:0,//医嘱状态
												sortId:sortIdCreate()
											}).datagrid('getRows').length-1;
											$('#infolistB').datagrid('selectRow',lastIndex);
										}										
									}
								}
								$('#chinMediModleDivId').dialog('close');
							}else{
								return;
							}
						});
					}else{
						for(var i=0;i<rows.length;i++){
							if(rows[i].id!=null&&rows[i].id!=''){
								var qq = $('#ttta').tabs('getSelected');				
								var tab = qq.panel('options');
								if(tab.title=='长期医嘱'){	
									var lastIndex = $('#infolistA').datagrid('appendRow',{
										changeNo:1,
										classCode:rows[i].sysType,//类型
										className:'中草药',//系统类别名称
										itemType:1,//是否为药品
										typeName:$('#longDocAdvType').combobox('getText'),//医嘱类型
										typeCode:$('#longDocAdvType').combobox('getValue'),//医嘱名称Id
										itemCode:rows[i].id,//医嘱名称id
										itemName:rows[i].name,//医嘱名称
										itemPrice:rows[i].price,//价格
										drugpackagingUnit:rows[i].adPackUnitHid,//包装单位
										minUnit:rows[i].minimumUnit,//最小单位
										doseUnit:rows[i].doseunit,//剂量
										baseDose:rows[i].basicdose,//基本剂量
										specs:rows[i].spec,//规格
										drugType:rows[i].type,//药品类别
										packQty:rows[i].packagingnum,//包装数量
										drugQuality:rows[i].nature,//药品性质
										doseModelCode:rows[i].dosageform,//剂型
										permission:0,//是否知情同意书
										combNo:gV,//组
										qtyTot:rows[i].numb*setNumVal,//总量
										priceUnit:rows[i].unitId,//总单位Id
										totalUnit:rows[i].unit,//总单位
										doseOnce:rows[i].numb,//每次用量
										useDays:setNumVal,//付数
										frequencyCode:frequencyHidVal,//频次Id
										frequencyName:frequencyVal,//频次编码
										usageCode:rows[i].useId,//用法Id
										useName:rows[i].use,//用法名称
										docName:$('#username').val(),//开立医生
										execDpcd:$('#pharmacyInputId').val(),//执行科室Id
										execDpnm:$('#pharmacyInputName').val(),//执行科室
										emcFlag:0,//加急Id
										pharmacyCode:$('#pharmacyInputId').val(),//扣库科室Id
										moNote2:modVal,//备注
										recUsernm:$('#username').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室
										dateBgn:staDate!=''?staDate:mytime,//开立时间
										dateEnd:endDate,//结束时间
										moDate:mytime,//开立时间
										hypotest:1,//是否需要皮试Id
										hypotestName:'不需要皮试',//是否需要皮试
										moStat:0,//医嘱状态
										sortId:sortIdCreate()
									}).datagrid('getRows').length-1;
									$('#infolistA').datagrid('selectRow',lastIndex);
								}else if(tab.title=='临时医嘱'){
									var lastIndex = $('#infolistB').datagrid('appendRow',{
										changeNo:1,
										classCode:rows[i].sysType,//类型
										className:'中草药',//系统类别名称
										itemType:1,//是否为药品
										typeName:$('#temDocAdvType').combobox('getText'),//医嘱类型
										typeCode:$('#temDocAdvType').combobox('getValue'),//医嘱名称Id
										itemCode:rows[i].id,//医嘱名称id
										itemName:rows[i].name,//医嘱名称
										itemPrice:rows[i].price,//价格
										drugpackagingUnit:rows[i].adPackUnitHid,//包装单位
										minUnit:rows[i].minimumUnit,//最小单位
										doseUnit:rows[i].doseunit,//剂量
										baseDose:rows[i].basicdose,//基本剂量
										specs:rows[i].spec,//规格
										drugType:rows[i].type,//药品类别
										packQty:rows[i].packagingnum,//包装数量
										drugQuality:rows[i].nature,//药品性质
										doseModelCode:rows[i].dosageform,//剂型
										permission:0,//是否知情同意书
										combNo:gV,//组
										qtyTot:rows[i].numb*setNumVal,//总量
										priceUnit:rows[i].unitId,//总单位Id
										totalUnit:rows[i].unit,//总单位
										doseOnce:rows[i].numb,//每次用量
										useDays:setNumVal,//付数
										frequencyCode:frequencyHidVal,//频次Id
										frequencyName:frequencyVal,//频次编码
										usageCode:rows[i].useId,//用法Id
										useName:rows[i].use,//用法名称
										docName:$('#username').val(),//开立医生
										execDpcd:$('#pharmacyInputId').val(),//执行科室Id
										execDpnm:$('#pharmacyInputName').val(),//执行科室
										emcFlag:0,//加急Id
										pharmacyCode:$('#pharmacyInputId').val(),//扣库科室Id
										moNote2:modVal,//备注
										recUsernm:$('#username').val(),//录入人
										listDpcd:$('#deptId').val(),//开立科室
										dateBgn:staDate!=''?staDate:mytime,//开立时间
										dateEnd:endDate,//结束时间
										moDate:mytime,//开立时间
										hypotest:1,//是否需要皮试Id
										hypotestName:'不需要皮试',//是否需要皮试
										moStat:0,//医嘱状态
										sortId:sortIdCreate()
									}).datagrid('getRows').length-1;
									$('#infolistB').datagrid('selectRow',lastIndex);
								}								
							}
						}
						$('#chinMediModleDivId').dialog('close');
					}
				}else{
					$.messager.alert('提示','请添加草药信息！');
					return;
				}
				
			}
			//取消
			function adChinMediInfoModelCan(){
				$('#chinMediModleDivId').dialog('close');
			}
		</script>
	</body>
</html>
