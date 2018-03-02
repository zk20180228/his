<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		
	</head>
	<body>
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="height:44px;">
				<table style="padding: 5px 5px 5px 5px;">
					<tr>
						<td style="width:70px;" align="right" class="herbalSize">医嘱类型：</td>
						<td style="width:70px;">门诊医嘱</td>
						<td style="width:70px;" align="right" class="herbalSize">选择药房：</td>
					    <td style="width:125px;"><input id="pharmacyChinMediInfo" class="easyui-combobox" style="width:120px;height:25px;"></td>
					    <td style="width:50px;" align="right">付数：</td>
						<td style="width:55px;"><input id="adChinMediInfoModelNumTdId" class="easyui-numberbox" value="1" data-options="min:1,max:999" style="width:50px;"/></td>
						<td style="width:50px;" align="right">频次：</td>
				 		<td style="width:125px;"><input id="adChinMediInfoModelFreTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
						<td style="width:50px;" align="right">用法：</td>
						<td style="width:125px;"><input id="adChinMediInfoModelUseTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
						<td style="width:70px;" align="right" class="herbalSize">煎药方式：</td>
						<td><input id="adChinMediInfoModelModTdId" class="easyui-combobox" data-options="" style="width:120px;"/></td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false">
				<table id="adChinMediInfoModelDgId" class="easyui-datagrid" data-options="rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fit:true,toolbar:'#toolbarHerbalId'"> 
				</table>
			</div>
			<div data-options="region:'south',border:false" style="height:23px;" class="divLabel herbalSize1">
				&nbsp;&nbsp;<span style="font-size:12px">停用药品：</span><span style="height:8px;line-height:6px;display:inline-block;background-color: #FF9191">&nbsp;&nbsp;</span>
				&nbsp;&nbsp;<span style="font-size:12px">药房缺药：</span><span style="height:8px;line-height:6px;display:inline-block;background-color: #9D9DFF">&nbsp;&nbsp;</span>
				&nbsp;&nbsp;<span style="font-size:12px">职级不符：</span><span style="height:8px;line-height:6px;display:inline-block;background-color: #FFB08A">&nbsp;&nbsp;</span>
			</div>
		</div>
		<div id="adChinMediInfoModelWinDivId"></div>
		<div id="toolbarHerbalId">
			<a href="javascript:void(0)" onclick="adChinMediInfoModelAdd()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新开</a>
			<a href="javascript:void(0)" onclick="adChinMediInfoModelDel()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
			<a href="javascript:void(0)" onclick="adChinMediInfoModelSta()" class="easyui-linkbutton" data-options="iconCls:'icon-application_osx_link',plain:true">保存组套</a>
			<a href="javascript:void(0)" onclick="adChinMediInfoModelYes()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">确定</a>
			<a href="javascript:void(0)" onclick="adChinMediInfoModelCan()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">取消</a>
		</div>
		<script type="text/javascript">
			$(function(){
    			$('#adChinMediInfoModelFreTdId').combobox({//频次
					data:colorInfoFrequencyList,
					valueField:'encode',    
					textField:'name',
					value:judgeMap['drugFre'],
					editable:true,
					disabled:false,
					onHidePanel:function(none){
						var data = $(this).combobox('getData');
						var val = $(this).combobox('getValue');
						var result = true;
						for (var i = 0; i < data.length; i++) {
							if (val == data[i].encode) {
								result = false;
							}
						}
						if (result) {
							$(this).combobox("clear");
						}else{
							$(this).combobox('unselect',val);
							$(this).combobox('select',val);
						}
					},
					filter: function(q, row){
						var keys = new Array();
						keys[keys.length] = 'encode';
						keys[keys.length] = 'name';
						keys[keys.length] = 'pinyin';
						keys[keys.length] = 'wb';
						keys[keys.length] = 'inputCode';
						return filterLocalCombobox(q, row, keys);
					}
				});
				$('#adChinMediInfoModelModTdId').combobox({//煎药方式
					url:"<%=basePath%>outpatient/advice/querBoilmedicineway.action",
				    valueField:'encode',    
				    textField:'name',
				    editable:false,
					disabled:false   
				});
				$('#adChinMediInfoModelUseTdId').combobox({//用法
					data:colorInfoUsageList,
				    valueField:'encode',    
				    textField:'name',
				    value:judgeMap['drugUse'],
				    editable:true,
					disabled:false,
					onHidePanel:function(none){
						var data = $(this).combobox('getData');
						var val = $(this).combobox('getValue');
						var result = true;
						for (var i = 0; i < data.length; i++) {
							if (val == data[i].encode) {
								result = false;
							}
						}
						if (result) {
							$(this).combobox("clear");
						}else{
							$(this).combobox('unselect',val);
							$(this).combobox('select',val);
						}
					},
					filter: function(q, row){
                        var keys = new Array();
                        keys[keys.length] = 'encode';
                        keys[keys.length] = 'name';
                        keys[keys.length] = 'pinyin';
                        keys[keys.length] = 'wb';
                        keys[keys.length] = 'inputCode';
                        return filterLocalCombobox(q, row, keys);
	                }
				});
				$('#pharmacyChinMediInfo').combobox({    
					url:"<%=basePath%>publics/putGetDrug/PGDjsonForPutDeptID.action",
				    valueField:'id',    
				    textField:'deptName',
				    multiple:false,
				    editable:false,
				    onLoadSuccess: function (data) {
				    	$('#pharmacyChinMediInfo').combobox('select',$('#pharmacyCombobox').combobox('getValue'));
			        },
			        onSelect:function(record){
			        	$.post("<%=basePath%>outpatient/advice/savaPharmacyInfo.action",
					        {"pharmacyId":record.id},
					        function(dataMap){
					   			if(dataMap.resMsg=="error"){
					   				$.messager.alert('提示',dataMap.resCode);
					        	}else if(dataMap.resMsg=="success"){
					        		$('#pharmacyCombobox').combobox('select',record.id);
					        		var rows = $('#adChinMediInfoModelDgId').datagrid('getRows');
					        		if(rows!=null&&rows.length>0){
					        			var leng = rows.length;
					        			for(var i=0;i<leng;i++ ){
					        				$('#adChinMediInfoModelDgId').datagrid('deleteRow',$('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[0]));
					        			}
					        		}
					        		
					        	}else{
					        		$.messager.alert('提示','未知错误,请联系管理员!');
					        	}
					   		}
						);
			        }
				}); 
    			setTimeout(function(){
    				$('#adChinMediInfoModelDgId').edatagrid({
						pagination:false,
						onClickRow:function(rowIndex, rowData){
							$('#adChinMediInfoModelDgId').datagrid('unselectAll');
							$('#adChinMediInfoModelDgId').datagrid('selectRow',rowIndex);
							$('#adChinMediInfoModelDgId').datagrid('beginEdit',rowIndex);
						},
						onUnselect:function(rowIndex, rowData){
							var ed = $('#adChinMediInfoModelDgId').datagrid('getEditor', {index:rowIndex,field:'numb'});
							if(ed!=null){
								var numb = $(ed.target).numberbox('getText');
								if(rowData!=null&&(numb==null||numb=='')){
									$('#adChinMediInfoModelDgId').datagrid('updateRow',{
										index: rowIndex,
										row: {
											numb:1
										}
									});
								}else{
									$('#adChinMediInfoModelDgId').datagrid('updateRow',{
										index: rowIndex,
										row: {
											numb:numb
										}
									});
								}
							}
							$('#adChinMediInfoModelDgId').datagrid('cancelEdit',rowIndex);
						},
						onUnselectAll:function(rows){
							if(rows!=null){
								for(var i=0;i<rows.length;i++){
									var index = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',rows[i]);
									$('#adChinMediInfoModelDgId').datagrid('unselectRow',index);
								}
							}
						},
						columns:[[    
					        {field:'ck',checkbox:true},  
					        {field:'id',title:'id',hidden:true,width:'150'},  
					        {field:'name',title:'药品名称',
					        	editor:{type:'combogrid',
						        	options:{
						        		url:"<%=basePath%>outpatient/advice/queryWestMediInfoModel.action",
						        		pagination:true,
						        		required:true,
						        		pageSize:10,
										pageList:[10,30,50,80,100],
						        		panelWidth:450,    
							            idField:'id',    
							            textField:'name', 
							            mode:'remote', 
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
										},
							            onClickRow:function(rowIndex, rowData){
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
							            	var index = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected'));
							            	$('#adChinMediInfoModelDgId').datagrid('updateRow',{
												index: index,
												row: {
													id:rowData.code,
													name:rowData.name,
													spec:rowData.spec,//规格
													price:rowData.price,//价格
													numb:1,//数量
													unitId:rowData.unit+"_"+judgeMap["packunit"],//单位
													unit:colorInfoUnitsMap.get(rowData.unit+"_"+judgeMap["packunit"]),//单位
// 													useId:rowData.usemode,//用法Id
// 													use:colorInfoUsageMap.get(rowData.usemode),//用法
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
											$('#adChinMediInfoModelDgId').datagrid('endEdit',index);
											$('#adChinMediInfoModelDgId').datagrid('beginEdit',index);
							            },  
							            columns:[[    
							                {field:'name',title:'名称',width:100},    
							                {field:'spec',title:'规格',width:100},    
							                {field:'price',title:'价格',width:100},
							                {field:'commonName',title:'通用名',width:100},
							            ]]  
						        	}
					        	},width:'150'
					        },     
					        {field:'spec',title:'规格',width:'150'},
					        {field:'price',title:'价格',width:'150'},
					        {field:'numb',title:'数量',editor:{type:'numberbox',options:{precision:3,required:true}},width:'150'},
					        {field:'unitId',title:'单位Id',hidden:true,width:'150'},
					        {field:'unit',title:'单位',
					        	editor:{type:'combobox',
					        		options:{
					        			data:colorInfoUnitsList,
								    	editable:false,
								    	disabled:false,
								    	required:true,
									    valueField:'code',    
									    textField:'name',
									    onSelect:function(record){
									    	var index = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',$('#adChinMediInfoModelDgId').datagrid('getSelected'));
									    	$('#adChinMediInfoModelDgId').datagrid('endEdit',index);
									    	$('#adChinMediInfoModelDgId').datagrid('updateRow',{
												index: index,
												row: {
													unitId:record.code+"_"+record.organize,//单位
													unit:record.name//单位
												}
											});
											$('#adChinMediInfoModelDgId').datagrid('beginEdit',index);
									    }
						        	}
					        	},width:'150'
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
				    	]]			
					});
    			},500);
			});
			//新开
			function adChinMediInfoModelAdd(){
				var row = $('#adChinMediInfoModelDgId').datagrid('getSelected');
				if(row!=null){
					var index = $('#adChinMediInfoModelDgId').datagrid('getRowIndex',row);
					$('#adChinMediInfoModelDgId').datagrid('unselectRow',index);
				}
				var lastIndex = $('#adChinMediInfoModelDgId').datagrid('appendRow',{}).datagrid('getRows').length-1;
				$('#adChinMediInfoModelDgId').datagrid('selectRow',lastIndex);
				$('#adChinMediInfoModelDgId').datagrid('beginEdit',lastIndex);
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
						}
						var setNumVal = $('#adChinMediInfoModelNumTdId').numberbox('getText');
						if(setNumVal==null||setNumVal==''){
							$.messager.alert('提示','付数不能为空！');
							return;
						}
						var isValidFre = isValidComboboxValue('adChinMediInfoModelFreTdId','encode','name');
						if(!isValidFre){
							$.messager.alert('提示','请选择频次！');
							return;
						}
						var isValidUse = isValidComboboxValue('adChinMediInfoModelUseTdId','encode','name');
						if(!isValidUse){
							$.messager.alert('提示','请选择用法！');
							return;
						}
						$('#stackSaveModleDivId').dialog({    
						    title:'确认组套信息',    
						    width:1300,    
						    height:400,    
						    closed: false,
						    closable:false,    
						    cache: false,
						    modal: true   
						});
						for(var i=0;i<rows.length;i++){
							var unit = rows[i].unitId.split("_");
							$('#adStackInfoSaveModelDgId').datagrid('appendRow',{
								id:rows[i].id,//名称
								name:rows[i].name,//名称
								spec:rows[i].spec,//规格
								packagingnum:rows[i].packagingnum,//包装数量
								unit:unit[0],//单位
								unitView:colorInfoUnitsMap.get(rows[i].unitId),//单位
								defaultprice:rows[i].price,//默认价
//								childrenprice:'',//儿童价
//								specialprice:'',//特诊价
								frequencyCode:$('#adChinMediInfoModelFreTdId').combobox('getValue'),//频次编码
								frequencyCodeView:$('#adChinMediInfoModelFreTdId').combobox('getText'),//频次编码
								usageCode:$('#adChinMediInfoModelUseTdId').combobox('getValue'),//用法名称
								usageCodeView:$('#adChinMediInfoModelUseTdId').combobox('getText'),//用法名称
								onceDose:rows[i].oncedosage,//每次服用剂量
								doseUnit:rows[i].doseunit,//剂量单位
								doseUnitView:colorInfoUnitsMap.get(rows[i].doseunit+"_"+judgeMap["doseUnit"]),//剂量单位
								mainDrugshow:1,//主药标记
								dateBgn:null,//医嘱开始时间
								dateEnd:null,//医嘱结束时间
								//itemNote:rows[i].inspectPart,//检查部位
								days:setNumVal,//草药付数
								//intervaldays:'',//间隔天数
								//remark:setNumVal,//备注
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
				}
				var setNumVal = $('#adChinMediInfoModelNumTdId').numberbox('getText');
				if(setNumVal==null||setNumVal==''){
					$.messager.alert('提示','付数不能为空！');
					return;
				}
				var isValidFre = isValidComboboxValue('adChinMediInfoModelFreTdId','encode','name');
				if(!isValidFre){
					$.messager.alert('提示','请选择频次！');
					return;
				}
				var isValidUse = isValidComboboxValue('adChinMediInfoModelUseTdId','encode','name');
				if(!isValidUse){
					$.messager.alert('提示','请选择用法！');
					return;
				}
				var modVal = $('#adChinMediInfoModelModTdId').combobox('getText');
				if(modVal==null||modVal==''){
					$.messager.alert('提示','请选择煎药方式！');
					return;
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
										var totalUnitHid = rows[i].unitId.split("_");
										var lastIndex = $('#adDgList').datagrid('appendRow',{
											limit:rows[i].isProvincelimit==1?'X':(rows[i].isCitylimit==1)?'S':null,
											type:rows[i].sysType,//类型
											ty:1,//是否为药品
											adviceType:'临时医嘱',//医嘱类型
											adviceId:rows[i].id,//医嘱名称Id
											adviceName:rows[i].name,//医嘱名称Hid
											adviceNameView:((rows[i].restrictionofantibiotic==3&&$('#isAuditing').val()=='false')?img:'')+'['+rows[i].price+'元/'+rows[i].unit+']'+rows[i].name+'['+rows[i].spec+']',//医嘱名称
											adPrice:rows[i].price,//价格
											adPackUnitHid:rows[i].adPackUnitHid+"_"+judgeMap["packunit"],//包装单位
											adMinUnitHid:rows[i].minimumUnit+"_"+judgeMap["minunit"],//单位
											adDosaUnitHid:rows[i].doseunit,//剂量
											adDosaUnitHidJudge:rows[i].doseunit+"_"+judgeMap["doseUnit"],
											adDrugBasiHid:rows[i].basicdose,//基本剂量
											specs:rows[i].spec,//规格
											sysType:rows[i].sysType,//系统类别
											drugType:rows[i].type,//药品类别
											minimumcost:rows[i].minimumcost,//最小费用代码
											packagingnum:rows[i].packagingnum,//包装数量
											nature:rows[i].nature,//药品性质
											ismanufacture:rows[i].ismanufacture,//自制药标志
											dosageform:rows[i].dosageform,//剂型
											isInformedconsent:0,//是否知情同意书
											auditing:1,
											group:gV,//组
											totalNum:(rows[i].numb*setNumVal).toFixed(3),//总量
											totalUnitHid:totalUnitHid[0],//总单位Id
											totalUnitHidJudge:rows[i].unitId,//总单位Id
											totalUnit:rows[i].unit,//总单位
											dosageHid:rows[i].numb,//每次用量
											dosageMin:rows[i].numb,
											dosage:((rows[i].oncedosage==null||rows[i].oncedosage==0)?1:rows[i].oncedosage)+colorInfoUnitsMap.get(rows[i].minimumUnit+"_"+judgeMap["minunit"])+'='+(((rows[i].oncedosage==null||rows[i].oncedosage==0)?1:rows[i].oncedosage)*(rows[i].basicdose.toFixed(2))).toFixed(2)+colorInfoUnitsMap.get(rows[i].doseunit+"_"+judgeMap["doseUnit"]),//每次用量
											unit:colorInfoUnitsMap.get(rows[i].minimumUnit+"_"+judgeMap["minunit"]),//单位
											setNum:setNumVal,//付数
											frequencyHid:$('#adChinMediInfoModelFreTdId').combobox('getValue'),//频次编码
											frequency:$('#adChinMediInfoModelFreTdId').combobox('getText'),//频次编码
											usageNameHid:$('#adChinMediInfoModelUseTdId').combobox('getValue'),//用法名称
											usageName:$('#adChinMediInfoModelUseTdId').combobox('getText'),//用法名称
											//injectionNum:rowData,//院注次数
											openDoctor:$('#advUserName').val(),//开立医生
											executiveDeptHid:$('#pharmacyChinMediInfo').combobox('getValue'),//执行科室Id
											executiveDept:$('#pharmacyChinMediInfo').combobox('getText'),//执行科室
											isUrgentHid:0,//加急Id
											isUrgent:'否',//加急
											//inspectPartId:null,//检查部位Id
											//inspectPart:null,//检查部位
											//sampleTeptHid:null,
											//sampleTept:null,//样本类型
											minusDeptHid:$('#pharmacyChinMediInfo').combobox('getValue'),//扣库科室Id
											minusDept:$('#pharmacyChinMediInfo').combobox('getText'),//扣库科室
											remark:modVal,//备注
											inputPeop:$('#advUserName').val(),//录入人
											openDept:$('#outMedideptName').val(),//开立科室
											startTime:null,//开立时间
											endTime:null,//结束时间
											//stopPeop:rowData,//停止人
											isSkinHid:1,//是否需要皮试Id
											isSkin:'不需要皮试'//是否需要皮试
										}).datagrid('getRows').length-1;
										$('#adDgList').datagrid('selectRow',lastIndex);
									}
								}
								reloadRow();//刷新
								$('#chinMediModleDivId').dialog('close');
							}else{
								return;
							}
						});
					}else{
						for(var i=0;i<rows.length;i++){
							if(rows[i].id!=null&&rows[i].id!=''){
								var totalUnitHid = rows[i].unitId.split("_");
								var lastIndex = $('#adDgList').datagrid('appendRow',{
									limit:rows[i].isProvincelimit==1?'X':(rows[i].isCitylimit==1)?'S':null,
									type:rows[i].sysType,//类型
									ty:1,//是否为药品
									adviceType:'临时医嘱',//医嘱类型
									adviceId:rows[i].id,//医嘱名称Id
									adviceName:rows[i].name,//医嘱名称Hid
									adviceNameView:'['+rows[i].price+'元/'+rows[i].unit+']'+rows[i].name+'['+rows[i].spec+']',//医嘱名称
									adPrice:rows[i].price,//价格
									adPackUnitHid:rows[i].adPackUnitHid+"_"+judgeMap["packunit"],//包装单位
									adMinUnitHid:rows[i].minimumUnit+"_"+judgeMap["minunit"],//单位
									adDosaUnitHid:rows[i].doseunit,//剂量
									adDosaUnitHidJudge:rows[i].doseunit+"_"+judgeMap["doseUnit"],
									adDrugBasiHid:rows[i].basicdose,//基本剂量
									specs:rows[i].spec,//规格
									sysType:rows[i].sysType,//系统类别
									drugType:rows[i].type,//药品类别
									minimumcost:rows[i].minimumcost,//最小费用代码
									packagingnum:rows[i].packagingnum,//包装数量
									nature:rows[i].nature,//药品性质
									ismanufacture:rows[i].ismanufacture,//自制药标志
									dosageform:rows[i].dosageform,//剂型
									isInformedconsent:0,//是否知情同意书
									auditing:0,
									group:gV,//组
									totalNum:(rows[i].numb*setNumVal).toFixed(3),//总量
									totalUnitHid:totalUnitHid[0],//总单位Id
									totalUnitHidJudge:rows[i].unitId,//总单位Id
									totalUnit:rows[i].unit,//总单位
									dosageHid:rows[i].numb,//每次用量
									dosageMin:rows[i].numb,
									dosage:((rows[i].oncedosage==null||rows[i].oncedosage==0)?1:rows[i].oncedosage)+colorInfoUnitsMap.get(rows[i].minimumUnit+"_"+judgeMap["minunit"])+'='+(((rows[i].oncedosage==null||rows[i].oncedosage==0)?1:rows[i].oncedosage)*(rows[i].basicdose.toFixed(2))).toFixed(2)+colorInfoUnitsMap.get(rows[i].doseunit+"_"+judgeMap["doseUnit"]),//每次用量
									unit:colorInfoUnitsMap.get(rows[i].minimumUnit+"_"+judgeMap["minunit"]),//单位
									setNum:setNumVal,//付数
									frequencyHid:$('#adChinMediInfoModelFreTdId').combobox('getValue'),//频次编码
									frequency:$('#adChinMediInfoModelFreTdId').combobox('getText'),//频次编码
									usageNameHid:$('#adChinMediInfoModelUseTdId').combobox('getValue'),//用法名称
									usageName:$('#adChinMediInfoModelUseTdId').combobox('getText'),//用法名称
									//injectionNum:rowData,//院注次数
									openDoctor:$('#advUserName').val(),//开立医生
									executiveDeptHid:$('#pharmacyChinMediInfo').combobox('getValue'),//执行科室Id
									executiveDept:$('#pharmacyChinMediInfo').combobox('getText'),//执行科室
									isUrgentHid:0,//加急Id
									isUrgent:'否',//加急
									//inspectPartId:null,//检查部位Id
									//inspectPart:null,//检查部位
									//sampleTeptHid:null,
									//sampleTept:null,//样本类型
									minusDeptHid:$('#pharmacyChinMediInfo').combobox('getValue'),//扣库科室Id
									minusDept:$('#pharmacyChinMediInfo').combobox('getText'),//扣库科室
									remark:modVal,//备注
									inputPeop:$('#advUserName').val(),//录入人
									openDept:$('#outMedideptName').val(),//开立科室
									startTime:null,//开立时间
									endTime:null,//结束时间
									//stopPeop:rowData,//停止人
									isSkinHid:1,//是否需要皮试Id
									isSkin:'不需要皮试'//是否需要皮试
								}).datagrid('getRows').length-1;
								$('#adDgList').datagrid('selectRow',lastIndex);
							}
						}
						reloadRow();//刷新
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
			//初始化单位下拉框
			function initComboboxChinMedi(map){
				var retVal = "[";
				for(var key in map){
					if(retVal.length>1){
						retVal+=",";
					}
					retVal+="{\"id\":\""+key+"\",\"name\":\""+map[key]+"\"}";
				}
				retVal +="]";
				return eval("("+retVal+")");
			}
			
		</script>
	</body>
</html>
