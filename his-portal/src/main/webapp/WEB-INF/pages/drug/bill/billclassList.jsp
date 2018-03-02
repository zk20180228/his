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
<title>摆药分类列表</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var flag = 0;
	var typeCodeList = "";
	var usageCodeList = "";
	var drugDosageformList = "";
	var drugDrugtypeList = "";
	var drugpropertiesList = "";
	/**
	 * 用法查询
	 */
	function queryyf(){
		$('#ypyf1').datagrid({
			url:'<%=basePath%>inpatient/billInpatientKind/viewUseage.action',
			queryParams:{codeyf:$('#ypyf').textbox('getText')}		
		});
	}
	/**
	 * 医嘱类别
	 */
	function queryyzlb(){
		$('#listyzlb').datagrid({
			url:'<%=basePath%>inpatient/billInpatientKind/viewKind.action',
			queryParams:{codeyzlb:$('#yzlb').textbox('getText')}		
		});
	}
	/**
	 * 药品药剂
	 */
	function queryypyj(){
		$('#listypyj').datagrid({
			url:'<%=basePath%>inpatient/billInpatientKind/viewDosageform.action',
			queryParams:{codeypyj:$('#ypyj').textbox('getText')}		
		});
	}
	/**
	 * 药品类别
	 */
	function queryyplb(){
		$('#listyplb').datagrid({
			url:'<%=basePath%>inpatient/billInpatientKind/viewDrugtype.action',
			queryParams:{codeyplb:$('#yplb').textbox('getText')}		
		});
	}
	/**
	 * 药品性质
	 */
	function queryypxz(){
		$('#listypxz').datagrid({
			url:'<%=basePath%>inpatient/billInpatientKind/viewDrugproperties.action',
			queryParams:{codeypsz:$('#ypxz').textbox('getText')}		
		});
	}
	$(function() {
		$('#ypyf').textbox('textbox').bind('keyup', function(event) {
			queryyf();
		});
		$('#yzlb').textbox('textbox').bind('keyup', function(event) {
			queryyzlb();
		});
		$('#ypyj').textbox('textbox').bind('keyup', function(event) {
			queryypyj();
		});
		$('#yplb').textbox('textbox').bind('keyup', function(event) {
			queryyplb();
		});
		$('#ypxz').textbox('textbox').bind('keyup', function(event) {
			queryypxz();
		});
		$('#list').datagrid({
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : true,
			pagination : true,
			fitColumns : true,
			pageSize : 6,
			pageList : [ 6, 20, 50, 100 ],
			url : '<%=basePath%>inpatient/bill/queryBillclass.action',
			columns : [ [ {
					field : 'id',
					title : 'id',
					width : '10%',
					hidden : true
				}, {
					field : 'billclassCode',
					title : '摆药单分类代码',
					type : 'textbox',
					options : {
						required : true
					},
					width : '15%'
				}, {
					field : 'billclassName',
					title : '摆药单分类名称',
					type : 'textbox',
					options : {
						required : true
					},
					width : '25%'
				}, {
					field : 'billclassAttr',
					title : '摆药单属性',
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'O',
							name : '一般摆药'
						}, {
							id : 'T',
							name : '特殊药品摆药'
						}, {
							id : 'R',
							name : '出院带药摆药'
						} ],
						required : true
					},
					width : '15%',
					formatter : function(value, row, index) {
						switch (row.billclassAttr) {
						case 'O':
							text = '一般摆药';
							break;
						case 'T':
							text = '特殊药品摆药';
							break;
						case 'R':
							text = '特殊药品摆药';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'printType',
					title : '打印类型',
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'T',
							name : '汇总'
						}, {
							id : 'D',
							name : '明细'
						}, {
							id : 'H',
							name : '草药'
						}, {
							id : 'R',
							name : '出院带药'
						} ],
						required : true
					},
					width : '15%',
					formatter : function(value, row, index) {
						switch (row.printType) {
						case 'T':
							text = '汇总';
							break;
						case 'D':
							text = '明细';
							break;
						case 'H':
							text = '草药';
							break;
						case 'R':
							text = '出院带药';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'validFlag',
					title : '是否有效',
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 1,
							name : '有效'
						}, {
							id : 0,
							name : '无效'
						} ],
						required : true
					},
					width : '10%',
					formatter : function(value, row, index) {
						switch (value) {
						case 1:
							text = '有效';
							break;
						case 0:
							text = '无效';
							break;
						default:
							text = '';
							break;
						}
						return text;
					}
				}, {
					field : 'mark',
					title : '备注',
					type : 'textbox',
					options : {
						required : true
					},
					width : '10%'
				} ] ],
			onClickRow : function(rowIndex, rowData) {
				$('#listyzlb').datagrid('unselectAll');
				$('#listyplb').datagrid('unselectAll');
				$('#listypxz').datagrid('unselectAll');
				$('#listypyj').datagrid('unselectAll');
				$('#ypyf1').datagrid('unselectAll');
				$.ajax({
					url: '<%=basePath%>inpatient/billInpatientKind/drugBilllistByclassId.action',
					data:{BillclassId:rowData.id},
					type:'post',
					success: function(payList){
						if(payList.length==0){
							return;
						}else{
							//医嘱类别
							var result=payList.typeCode; 
							for(var i=0;i<result.length;i++){
								var rows = $('#listyzlb').datagrid('getRows');
								for(var j=0;j<rows.length;j++){
									if(result[i].typeCode == rows[j].typeCode){
										var index =$('#listyzlb').datagrid('getRowIndex',rows[j]);
										$('#listyzlb').datagrid('selectRow',index);
									}
								}
							} 
							//药品类别
							var result1=payList.drugType; 
							for(var i=0;i<result1.length;i++){
								var rows = $('#listyplb').datagrid('getRows');
								for(var j=0;j<rows.length;j++){
									if(result1[i].drugType == rows[j].encode){
										var index =$('#listyplb').datagrid('getRowIndex',rows[j]);
										$('#listyplb').datagrid('selectRow',index);
									}
								}
							}
							//药品性质
							var result2=payList.drugQuality;
							for(var i=0;i<result2.length;i++){
								var rows = $('#listypxz').datagrid('getRows');
								for(var a=0;a<rows.length;a++){
									if(result2[i].drugQuality == rows[a].encode){
										var index =$('#listypxz').datagrid('getRowIndex',rows[a]);
										$('#listypxz').datagrid('selectRow',index);
									}
								}
							}
							//药品药剂
							var result3=payList.doseModelCode;
							for(var i=0;i<result3.length;i++){
								var rows = $('#listypyj').datagrid('getRows');
								for(var b=0;b<rows.length;b++){
									if(result3[i].doseModelCode == rows[b].encode){
										var index =$('#listypyj').datagrid('getRowIndex',rows[b]);
										$('#listypyj').datagrid('selectRow',index);
									}
								}
							}
							//药品用法
							var result4=payList.usageCode;
							for(var i=0;i<result4.length;i++){
								var rows = $('#ypyf1').datagrid('getRows');
								for(var c=0;c<rows.length;c++){
									if(result4[i].usageCode == rows[c].encode){
										var index =$('#ypyf1').datagrid('getRowIndex',rows[c]);
										$('#ypyf1').datagrid('selectRow',index);
									}
								}
							}
						}
					}
				});
			},
			onBeforeLoad:function(){
				$('#list').datagrid('clearSelections');
			},onLoadSuccess : function(data){
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
			}
		});
		$('#listaddandedit').edatagrid({
			checkOnSelect : true,
			selectOnCheck : true,
			singleSelect : true,
			columns : [ [ {
				field : 'id',
				title : 'id',
				width : '20%',
				hidden : true
			},{
				field : 'billclassName',
				title : '摆药单分类名称',
				editor : {
					type : 'textbox',
					options : {
						required : true
					}
				},
				width : '15%'
			}, {
				field : 'billclassAttr',
				title : '摆药单属性',
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'O',
							name : '一般摆药'
						}, {
							id : 'T',
							name : '特殊药品摆药'
						}, {
							id : 'R',
							name : '出院带药摆药'
						} ],
						required : true
					}
				},
				width : '15%',
				formatter : function(value, row, index) {
					switch (row.billclassAttr) {
					case 'O':
						text = '一般摆药';
						break;
					case 'T':
						text = '特殊药品摆药';
						break;
					case 'R':
						text = '特殊药品摆药';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'printType',
				title : '打印类型',
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 'T',
							name : '汇总'
						}, {
							id : 'D',
							name : '明细'
						}, {
							id : 'H',
							name : '草药'
						}, {
							id : 'R',
							name : '出院带药'
						} ],
						required : true
					}
				},
				width : '15%',
				formatter : function(value, row, index) {
					switch (row.printType) {
					case 'T':
						text = '汇总';
						break;
					case 'D':
						text = '明细';
						break;
					case 'H':
						text = '草药';
						break;
					case 'R':
						text = '出院带药';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'validFlag',
				title : '是否有效',
				editor : {
					type : 'combobox',
					options : {
						valueField : 'id',
						textField : 'name',
						method : 'get',
						multiple : false,
						editable : false,
						data : [ {
							id : 1,
							name : '有效'
						}, {
							id : 0,
							name : '无效'
						} ],
						required : true
					}
				},
				width : '15%',
				formatter : function(value, row, index) {
					switch (value) {
					case 1:
						text = '有效';
						break;
					case 0:
						text = '无效';
						break;
					default:
						text = '';
						break;
					}
					return text;
				}
			}, {
				field : 'mark',
				title : '备注',
				editor : {
					type : 'textbox'
				},
				width : '15%'
			} ] ]
		});
	});
	//提交验证 修改
	function edit() {
		$('#saveByUpdate').linkbutton('enable');
		$('#deletefl').linkbutton('enable');
		var rows = $('#list').datagrid('getSelections');
		if(rows==""){
			$.messager.alert('提示','请选择一条数据！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if(rows[0].id=="P"||rows[0].id=="R"){
			$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能维护分类明细！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		if (rows.length > 0) {//选中几行的话触发事件
			$('#classids').val('2');
			var ids = rows[0].id;
			$('#listaddandedit').datagrid({
				url : '<%=basePath%>inpatient/billInpatientKind/viewBilllist.action',
				queryParams:{BillclassId:ids},
				onLoadSuccess:function(){
					$('#listaddandedit').datagrid('beginEdit', 0);
				},
			});
		}
	}
	//添加
	function add() {
		$('#saveByUpdate').linkbutton('enable');
		$('#deletefl').linkbutton('enable');
		$('#classids').val('1');
		var rowss=$('#listaddandedit').datagrid('getRows');
		for(var i;i<rowss.length;i++){
			$('#listaddandedit').datagrid('endEdit', i);
		}
		if(rowss.length>0){
			$('#listaddandedit').datagrid('beginEdit', 0);
			return;
		}
		var index = $('#listaddandedit').edatagrid('appendRow', {}).edatagrid(
		'getRows').length - 1;
		$('#listaddandedit').edatagrid('checkRow', index);
		$('#listaddandedit').edatagrid('beginEdit', index);
	}
	//删除
	function del() {
		var rows = $('#list').datagrid('getSelections');
		if (rows.length > 0) {
			var ids = '';
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i].id == null) {//如果id为null 则为新添加行
					var dd = $('#list').edatagrid('getRowIndex', rows[i]);//获得行索引
					$('#list').edatagrid('deleteRow', dd);//通过索引删除该行
				} else {
					if(ids != null && ids != ""){
						ids += ",";
					}
					ids += rows[i].id;
				}
			}
			if (ids != null && ids != "") {
				$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
					if (res) {
						$.messager.progress({text:'删除中，请稍后...',modal:true});
						$.ajax({
							url : '<%=basePath%>inpatient/bill/delBillclass.action?idss=' + ids,
							data:{idss:ids},
							type : 'post',
							success : function() {
								$.messager.progress('close');
								$.messager.alert("操作提示", "删除成功！", "success");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								$('#list').datagrid('load',{
								});
								$('#listyzlb').datagrid('load',{
								});
								$('#listyplb').datagrid('load',{
								});
								$('#listypxz').datagrid('load',{
								});
								$('#listypyj').datagrid('load',{
								});
								$('#ypyf1').datagrid('load',{
								});
							}
						});
					}
				});
			}
		} else {
			$.messager.alert("操作提示", "请选择一条记录！", "error");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//保存
	function save(){
		var idss = $('#classids').val();
		if(idss=='1'){
			$('#listaddandedit').edatagrid('acceptChanges');
			$('#billJson').val(JSON.stringify( $('#listaddandedit').edatagrid("getRows")));
			var yzlb = $('#listyzlb').datagrid('getSelections');//医嘱类别
			var yplb = $('#listyplb').datagrid('getSelections');//药品类别
			var ypxz = $('#listypxz').datagrid('getSelections');//药品性质
			var ypyj = $('#listypyj').datagrid('getSelections');//药品药剂
			var yzyf = $('#ypyf1').datagrid('getSelections');//药品用法
			$('#billJsonYzsx').val(JSON.stringify($('#listyzlb').datagrid('getSelections')));
			$('#billJsonYplb').val(JSON.stringify($('#listyplb').datagrid('getSelections')));
			$('#billJsonYpxz').val(JSON.stringify($('#listypxz').datagrid('getSelections')));
			$('#billJsonYpyj').val(JSON.stringify($('#listypyj').datagrid('getSelections')));
			$('#billJsonYpyf').val(JSON.stringify($('#ypyf1').datagrid('getSelections')));
			var reos = $('#listaddandedit').edatagrid("getRows");
			if(reos[0].billclassName=="退药单"||reos[0].billclassName=="非医嘱摆药"){
				if(yzlb.length>0){
						$('#listaddandedit').datagrid('beginEdit', 0);
						$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择医嘱类别！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
				}else{
					if(yplb.length>0){
						$('#listaddandedit').datagrid('beginEdit', 0);
						$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品类别！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else{
						if(ypxz.length>0){
							$('#listaddandedit').datagrid('beginEdit', 0);
							$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品性质！');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else{
							if(ypyj.length>0){
								$('#listaddandedit').datagrid('beginEdit', 0);
								$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品药剂！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else{
								if(yzyf.length>0){
									$('#listaddandedit').datagrid('beginEdit', 0);
									$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品用法！');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}else{
									$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
									 if (!$('#editForm').form('validate')) {
										    $.messager.progress('close');	
											$.messager.alert('提示',"验证没有通过,不能提交表单!");
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											return false;
									}
									$('#editForm').form('submit',{
										url:'<%=basePath%>inpatient/bill/saveBillclass.action',
										success:function(data){
											$.messager.progress('close');
											$.messager.alert("操作提示", "添加成功！", "success");
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											var rows1 = $('#listaddandedit').datagrid('getRows');
											for(var i=0;i<rows1.length;i++){
												$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
											}
											$('#list').datagrid('load',{
											});
											$('#listyzlb').datagrid('load',{
											});
											$('#listyplb').datagrid('load',{
											});
											$('#listypxz').datagrid('load',{
											});
											$('#listypyj').datagrid('load',{
											});
											$('#ypyf1').datagrid('load',{
											});
										},
										error : function(data) {
											$.messager.progress('close');
											$.messager.alert('提示','保存失败！');
										}
									});
								}
							}
						}
					}
				}
			}else{
				if(yzlb.length==0){
					$('#listaddandedit').datagrid('beginEdit', 0);
					$.messager.alert('警告','请选择医嘱类别');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					if(yplb.length==0){
						$('#listaddandedit').datagrid('beginEdit', 0);
						$.messager.alert('警告','请选择药品类别');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else{
						if(ypxz.length==0){
							$('#listaddandedit').datagrid('beginEdit', 0);
							$.messager.alert('警告','请选择药品性质');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else{
							if(ypyj.length==0){
								$('#listaddandedit').datagrid('beginEdit', 0);
								$.messager.alert('警告','请选择药品药剂');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else{
								if(yzyf.length==0){
									$('#listaddandedit').datagrid('beginEdit', 0);
									$.messager.alert('警告','请选择药品用法');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}else{
									$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
									if (!$('#editForm').form('validate')) {
									    $.messager.progress('close');	
										$.messager.alert('提示',"验证没有通过,不能提交表单!");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										return false;
									}
									$('#editForm').form('submit',{
										url:'<%=basePath%>inpatient/bill/saveBillclass.action',
										success:function(data){
											$.messager.progress('close');
											$.messager.alert("操作提示", "添加成功！", "success");
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											var rows1 = $('#listaddandedit').datagrid('getRows');
											for(var i=0;i<rows1.length;i++){
												$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
											}
											$('#list').datagrid('load',{
											});
											$('#listyzlb').datagrid('load',{
											});
											$('#listyplb').datagrid('load',{
											});
											$('#listypxz').datagrid('load',{
											});
											$('#listypyj').datagrid('load',{
											});
											$('#ypyf1').datagrid('load',{
											});
										},
										error : function(data) {
											$.messager.progress('close');
											$.messager.alert('提示','保存失败！');
										}
									});
								}
							}
						}
					}
				}
			}
		}else if(idss=='2'){
			$('#listaddandedit').edatagrid('acceptChanges');
			$('#billJson').val(JSON.stringify( $('#listaddandedit').edatagrid("getRows")));
			var yzlb = $('#listyzlb').datagrid('getSelections');//医嘱类别
			var yplb = $('#listyplb').datagrid('getSelections');//药品类别
			var ypxz = $('#listypxz').datagrid('getSelections');//药品性质
			var ypyj = $('#listypyj').datagrid('getSelections');//药品药剂
			var yzyf = $('#ypyf1').datagrid('getSelections');//药品用法
			
			$('#billJsonYzsx').val(JSON.stringify($('#listyzlb').datagrid('getSelections')));
			$('#billJsonYplb').val(JSON.stringify($('#listyplb').datagrid('getSelections')));
			$('#billJsonYpxz').val(JSON.stringify($('#listypxz').datagrid('getSelections')));
			$('#billJsonYpyj').val(JSON.stringify($('#listypyj').datagrid('getSelections')));
			$('#billJsonYpyf').val(JSON.stringify($('#ypyf1').datagrid('getSelections')));
			
			var rowss = $('#list').datagrid('getSelections');
			if(rowss[0].id=="R"||rowss[0].id=="P"){
				if(yzlb.length>0){
					$('#listaddandedit').datagrid('beginEdit', 0);
					$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择医嘱类别！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					if(yplb.length>0){
						$('#listaddandedit').datagrid('beginEdit', 0);
						$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品类别！');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else{
						if(ypxz.length>0){
							$('#listaddandedit').datagrid('beginEdit', 0);
							$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品性质！');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else{
							if(ypyj.length>0){
								$('#listaddandedit').datagrid('beginEdit', 0);
								$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品药剂！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else{
								if(yzyf.length>0){
									$('#listaddandedit').datagrid('beginEdit', 0);
									$.messager.alert('警告','摆药分类是退药单或者是非医嘱摆药，不能选择药品用法！');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}else{
									$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
									if (!$('#editForm').form('validate')) {
									    $.messager.progress('close');	
										$.messager.alert('提示',"验证没有通过,不能提交表单!");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										return false;
									}
									$('#editForm').form('submit',{
										url:'<%=basePath%>inpatient/billInpatientKind/updateBillClassInfo.action',
										success:function(data){ 
											$.messager.progress('close');
											$.messager.alert("操作提示", "修改成功！", "success");
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											var rows1 = $('#listaddandedit').datagrid('getRows');
											for(var i=0;i<rows1.length;i++){
												$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
											}
											$('#list').datagrid('load',{
											});
											$('#listyzlb').datagrid('load',{
											});
											$('#listyplb').datagrid('load',{
											});
											$('#listypxz').datagrid('load',{
											});
											$('#listypyj').datagrid('load',{
											});
											$('#ypyf1').datagrid('load',{
											});
										},
										error : function(data) {
											$.messager.progress('close');
											$.messager.alert('提示','保存失败！');
										}
									});
								}
							}
						}
					}
				}
			}else{
				if(yzlb.length==0){
					$('#listaddandedit').datagrid('beginEdit', 0);
					$.messager.alert('警告','请选择医嘱类别');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					if(yplb.length==0){
						$('#listaddandedit').datagrid('beginEdit', 0);
						$.messager.alert('警告','请选择药品类别');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
					}else{
						if(ypxz.length==0){
							$('#listaddandedit').datagrid('beginEdit', 0);
							$.messager.alert('警告','请选择药品性质');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else{
							if(ypyj.length==0){
								$('#listaddandedit').datagrid('beginEdit', 0);
								$.messager.alert('警告','请选择药品药剂');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
							}else{
								if(yzyf.length==0){
									$('#listaddandedit').datagrid('beginEdit', 0);
									$.messager.alert('警告','请选择药品用法');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}else{
									$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
									if (!$('#editForm').form('validate')) {
									    $.messager.progress('close');	
										$.messager.alert('提示',"验证没有通过,不能提交表单!");
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										return false;
									}
									$('#editForm').form('submit',{
										url:'<%=basePath%>inpatient/billInpatientKind/updateBillClassInfo.action',
										success:function(data){
											$.messager.progress('close');
											$.messager.alert("操作提示", "修改成功！", "success");
											setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
											var rows1 = $('#listaddandedit').datagrid('getRows');
											for(var i=0;i<rows1.length;i++){
												$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
											}
											$('#list').datagrid('load',{
											});
											$('#listyzlb').datagrid('load',{
											});
											$('#listyplb').datagrid('load',{
											});
											$('#listypxz').datagrid('load',{
											});
											$('#listypyj').datagrid('load',{
											});
											$('#ypyf1').datagrid('load',{
											});
										},
										error : function(data) {
											$.messager.progress('close');
											$.messager.alert('提示','保存失败！');
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
	/**
	 * 删除添加的摆药分类
	 */
	function deleteBill(){
		var rows1 = $('#listaddandedit').datagrid('getRows');
		for(var i=0;i<rows1.length;i++){
			$('#listaddandedit').datagrid('deleteRow', $('#listaddandedit').datagrid('getRowIndex', rows1[i]));
		}
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body style="margin: 0px; padding: 0px"> 
<div id="cc" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',collapsible:false" style="width:100%;height:30%;border-top:0">
		<table id="list"
			data-options="method:'post',rownumbers:true,idField: 'id',striped:true,border:false,toolbar:'#toolbarId',fit:true">
		</table>
	</div>
	<div  id="tableHibble" data-options="region:'center'" style="width:100%;height:20%;border-top: 0px">
		<form id="editForm" method="post">
			<input type="hidden" id="billJson" name="billJson">
			<input type="hidden" id="billJsonYpyf" name="billJsonYpyf">
			<input type="hidden" id="billJsonYpyj" name="billJsonYpyj">
			<input type="hidden" id="billJsonYpxz" name="billJsonYpxz">
			<input type="hidden" id="billJsonYplb" name="billJsonYplb">
			<input type="hidden" id="billJsonYzsx" name="billJsonYzsx">
			<div style="height: 100%;width: 100%;">
				<table id="listaddandedit" style="width: 100%" 
				data-options="method:'post',rownumbers:true,idField:'id',striped:false,border:false,fit:true,toolbar:'#tool'">
				</table>
			</div>
		</form>
	</div>
	<div data-options="region:'south',split:true,collapsible:false" style="width:100%;height:50%;">
		<div id="bb" class="easyui-layout" data-options="fit:true,border:false">  
			<div data-options="region:'west',collapsible:false,border:false" style="width:20%;height: 100%">
				<div  id="ff" class="easyui-layout" data-options="fit:true" align="center">
					<div data-options="region:'north',title:'医嘱类别',collapsible:false,border:false" style="height: 60px;width: 100%" align="center">
						<input id="yzlb" class="easyui-textbox" data-options="prompt:'回车或点击按钮查询'"style="width: 135px"/>
						<a href="javascript:void(0)" onclick="queryyzlb()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<input id="feel" type="hidden">
						<input id="classids" type="hidden">
					</div>
					<div data-options="region:'center',border:false"  style="width: 100%">
						<table id="listyzlb" class="easyui-datagrid" style="width:100%;height:100%;"
							data-options="url:'<%=basePath%>inpatient/billInpatientKind/viewKind.action?menuAlias=${menuAlias}',
							checkOnSelect:false,singleSelect:false,selectOnCheck:false,method:'post',
							rownumbers:true,idField:'id',striped:true,border:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'typeCode',align:'center'" style="width: 20%">编码</th>
									<th data-options="field:'typeName',align:'center'" style="width: 73%">名称</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div data-options="region:'center',border:false,border:false" style="width: 60%;height: 100%">
				<div id="aa" class="easyui-layout" data-options="fit:true,border:false">
					<div data-options="region:'west',collapsible:false" style="height: 100%;width: 33%;border-top:0">
						<div  id="kk" class="easyui-layout" data-options="fit:true"  align="center">
							<div data-options="region:'north',title:'药品类别',collapsible:false,border:false" style="height: 60px;width: 100%" align="center">
								<input id="yplb" class="easyui-textbox" data-options="prompt:'回车或点击按钮查询'" style="width: 130px"/>
								<a href="javascript:void(0)" onclick="queryyplb()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</div>
							<div data-options="region:'center',border:false"  style="width: 100%">
								<table id="listyplb" class="easyui-datagrid" style="width:100%;height:100%;"
									data-options="url:'<%=basePath%>inpatient/billInpatientKind/viewDrugtype.action?menuAlias=${menuAlias}',
										checkOnSelect:false,singleSelect:false,selectOnCheck:false,method:'post',
										rownumbers:true,idField:'id',striped:true,border:false,fit:true">
									<thead>
										<tr>
											<th data-options="field:'encode',align:'center'" style="width: 20%">编码</th>
											<th data-options="field:'name',align:'center'" style="width: 73%">名称</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
					<div data-options="region:'center'" style="height: 100%;width:33%;border-top:0">
						<div id="ll" class="easyui-layout" data-options="fit:true,border:false" align="center">
							<div data-options="region:'north',title:'药品性质',collapsible:false,border:false" style="height: 60px;width: 100%" align="center">
								<input id="ypxz" class="easyui-textbox" data-options="prompt:'回车或点击按钮查询'" style="width: 135px"/>
								<a href="javascript:void(0)" onclick="queryypxz()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</div>
							<div data-options="region:'center',border:false"  style="width: 100%">
								<table id="listypxz" class="easyui-datagrid" style="width:100%;height:100%;"
									data-options="url:'<%=basePath%>inpatient/billInpatientKind/viewDrugproperties.action?menuAlias=${menuAlias}',
									checkOnSelect:false,singleSelect:false,selectOnCheck:false,method:'post',
									rownumbers:true,idField:'id',striped:true,border:false,fit:true">
									<thead>
										<tr>
											<th data-options="field:'encode',align:'center'" style="width: 20%">编码</th>
											<th data-options="field:'name',align:'center'" style="width: 73%">名称</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
					<div data-options="region:'east',iconCls:'icon-reload',collapsible:false" style="height: 100%;width:33%;border-top:0">
						<div  id="hh" class="easyui-layout" data-options="fit:true,border:false" align="center">
							<div data-options="region:'north',title:'药品药剂',collapsible:false,border:false" style="height: 60px;width: 100%" align="center">
								<input id="ypyj" class="easyui-textbox" data-options="prompt:'回车或点击按钮查询'" style="width: 130px"/>
								<a href="javascript:void(0)" onclick="queryypyj()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							</div>
							<div data-options="region:'center',border:false"  style="width: 100%">
								<table id="listypyj" class="easyui-datagrid" style="width:100%;"
									data-options="url:'<%=basePath%>inpatient/billInpatientKind/viewDosageform.action?menuAlias=${menuAlias}',
										checkOnSelect:false,singleSelect:false,selectOnCheck:false,method:'post',
										rownumbers:true,idField:'id',striped:true,border:false,fit:true">
									<thead>
										<tr>
											<th data-options="field:'encode',align:'center'" style="width: 20%">编码</th>
											<th data-options="field:'name',align:'center'" style="width: 73%">名称</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div data-options="region:'east',collapsible:false,border:false" style="width:20%;height: 100%">
				<div id="gg" class="easyui-layout" data-options="fit:true,border:false" align="center">
					<div data-options="region:'north',title:'药品用法',collapsible:false,border:false" style="height: 60px;width: 100%" align="center">
						<input id="ypyf" class="easyui-textbox" data-options="prompt:'回车或点击按钮查询'" style="width: 135px"/>
						<a href="javascript:void(0)" onclick="queryyf()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
					</div>
					<div data-options="region:'center',border:false"  style="width: 100%">
						<table id="ypyf1" class="easyui-datagrid" style="width:100%;"
							data-options="url:'<%=basePath%>inpatient/billInpatientKind/viewUseage.action?menuAlias=${menuAlias}',
							checkOnSelect:false,singleSelect:false,selectOnCheck:false,method:'post', 
							rownumbers:true,idField:'id',striped:true,border:false,fit:true">
							<thead>
								<tr>
									<th data-options="field:'encode',align:'center'" style="width: 20%">编码</th>
									<th data-options="field:'name',align:'center'" style="width: 73%">名称</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</div> 
	</div>
</div>
<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:add">
	<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:save">
	<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">
	<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
</div>
<div id="tool">
	<shiro:hasPermission name="${menuAlias}:function:save">
	<a href="javascript:void(0)" onclick="save()" id="saveByUpdate" class="easyui-linkbutton" data-options="iconCls:'icon-save',disabled:true">保存</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">
	<a href="javascript:void(0)" id="deletefl" onclick="deleteBill()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',disabled:true">删除</a>
	</shiro:hasPermission>
</div>
</body>
</html>
