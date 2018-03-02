<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<head>
<title>拆分属性</title>
<%@ include file="/common/metas.jsp"%>
	<script type="text/javascript">
	var packUnitList = "";
	var dosageformList = "";
	var packList = "";
	var minList = "";
	var deptMap = "";
	//加载页面
	$(function() {
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination : true,
			pageSize : 20,
			pageList : [ 20, 30, 50, 80, 100 ],
			onBeforeLoad:function (param) {
			$.ajax({
					url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
					data:{"type" : "packunit"},
					type:'post',
					success: function(data) {
						packUnitList = data;
					}
				});
				$.ajax({
					url: "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
					data:{"type" : "minunit"},
					type:'post',
					success: function(data) {
						minList = data;
					}
				});
				
				$.ajax({
					url: '<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action',
					data:{"type" : "dosageForm"},
					type:'post',
					success: function(data) {
						dosageformList = data;
					}
				});

	        },
			onDblClickRow : function(rowIndex, rowData) {
				var row = $('#list').datagrid('getSelected');
				if (row) {
					$.ajax({
						url: '<%=basePath%>drug/split/checkDrug.action',
						data:{"drugId" : row.id},
						type:'post',
						success: function(result) {
							conform(row);
						}
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
		bindEnterEvent('queryName',searchFrom,'easyui');
		bindEnterEvent('queryDrugName',searchDrugFrom,'easyui');
		var id = '${id}'; //存储数据ID
		$.ajax({
			url: '<%=basePath%>baseinfo/department/getDeptMapForId.action',
			type:'post',
			success: function(data) {
				deptMap = data;
			}
		});
		setTimeout(function(){
		//添加datagrid事件及分页
			$('#infolist').datagrid({
				pagination : true,
				pageSize : 20,
				pageList : [ 20, 30, 50, 80, 100 ],
				columns : [[ {
					field : 'ck',
					checkbox : true,
					width : '5%'
				}, {
					field : 'drugCode',
					title : '药品编码',
					width : '12%',
					hidden : true
				}, {
					field : 'drugName',
					title : '药品名称',
					width : '20%'
				}, {
					field : 'drugSpec',
					title : '规格',
					width : '12%'
				}, {
					field : 'drugPackUnit',
					title : '包装单位',
					width : '12%'
				}, {
					field : 'drugPackQty',
					title : '包装数量',
					width : '12%'
				}, {
					field : 'propertyCode',
					title : '拆分属性',
					editor : {
						type : 'combobox',
						options : {
							valueField : 'id',
							textField : 'name',
							method : 'get',
							multiple : false,
							editable : false,
							data : [ {
								id : 0,
								name : '不可拆分'
							}, {
								id : 1,
								name : '可拆分,配药时不取整'
							},{
								id : 2,
								name : '可拆分,配药时上取整'
							},{
								id : 3,
								name : '不可拆分,当日取整'
							} ],
							required : true
						},
						missingMessage : '请选择拆分属性'
					},
					width : '15%',
					formatter : function(value, row, index) {
							switch (value) {
							case '0':
								text = '不可拆分';
								break;
							case '1':
								text = '可拆分,配药时不取整';
								break;
							case '2':
								text = '可拆分,配药时上取整';
								break;
							case '3':
								text = '不可拆分,当日取整';
								break;
							default:
								text = '';
								break;
							}
							return text;
						}
				} ,{
					field : 'deptCode',
					title : '科室',
					editor : {
						type : 'combotree',
						options : {
							url : '<%=basePath%>baseinfo/department/treeDepartmen.action',
							method : 'post',
							editable:false,
							required : true,
							onSelect: function (node){
									if(node.id == '1' || node.attributes.pid == '1'){
									node.id = "";
									node.text = "";
									$.messager.alert("操作提示", "请选择具体科室！！", "warning");
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								}
							}
						}
					},
					width : '20%',
					formatter : function(value, row, index) {
						if(value!=null){
							return deptMap[value];
						}else{
							return "";
						}
					}
				}, {
					field : 'type',
					title : '分类',
					width : '12%',
					hidden : true
				}]],
				onBeforeLoad:function(){
					$('#infolist').datagrid('clearChecked');
					$('#infolist').datagrid('clearSelections');
				},
				onDblClickRow : function(rowIndex, rowData) {
					var row = $('#infolist').datagrid('getChecked');
					$('#infolist').datagrid('checkRow', rowIndex);
					if(row){
						$('#infolist').datagrid('beginEdit', rowIndex);
					}else{
						$.messager.alert("操作提示", "请选择要编辑的条目！", "warning");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
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
		},300);
	
	});

	/**
	 * 确认把数据填充到维护属性列表
	 * @author  lt
	 */
	function conform(row) {
		var index = $('#infolist').datagrid('appendRow', {
			drugCode : row.code,
			drugName : row.name,
			drugSpec : row.spec,
			drugPackUnit :packUnitFamater(row.drugPackagingunit,null,null),
			drugPackQty : row.packagingnum,
			deptCode:"",
			type:1
		}).datagrid('getRows').length - 1;
		$('#infolist').datagrid('checkRow', index);
		$('#infolist').datagrid('beginEdit', index);		 
	}

	/**
	 * 药品列表查询
	 * @author  lt
	 */
	function searchFrom() {
		var drugName = $.trim($('#queryName').val());
		$('#list').datagrid('load', {
			name : drugName
		});
	}
	/**
	 * 药品列表查询
	 * @author  lt
	 */
	function searchDrugFrom() {
		var drugName = $.trim($('#queryDrugName').val());
		$('#infolist').datagrid('load', {
			drugName1 : drugName
		});
	}
	//从xml文件中解析，读到下拉框
	function idCombobox(param) {
		$('#' + param).combobox({
			url : 'comboBox.action?str=' + param,
			valueField : 'id',
			textField : 'name',
			multiple : false,
			onLoadSuccess : function() {//请求成功后
				$(list).each(function() {
					if ($('#' + param).val() == this.Id) {
						$('#' + param).combobox("select", this.Id);
					}
				});
			}
		});
	}
	//保存
	function save() {
		var rows = $('#infolist').datagrid('getChecked');
		if (rows.length > 0) {//选中几行的话触发事件	
			$('#infolist').datagrid('acceptChanges');//提交所有从加载或者上一次调用acceptChanges函数后更改的数据。
			for(var i = 0; i< rows.length; i++){
				var index = $('#infolist').datagrid('getRowIndex',rows[i]);
				if(!$('#infolist').datagrid('validateRow',index)){
					$.messager.alert('提示',"请补全信息后提交！！");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				
			}
			
			if(getAllJsonCheck()==0){
				allDrugs=[]; 
				allDrugsToRemove=[];
				return
			};
			
			$('#infoJson').val(JSON.stringify($('#infolist').datagrid("getChecked")));
			
			$('#saveForm').form(
					'submit',
					{
						url : '<%=basePath%>drug/split/drugSplitSave.action',
						onSubmit : function() {
							if (!$('#saveForm').form('validate')) {
								$.messager.alert('提示',"验证没有通过,不能提交表单!");
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								return false;
							}
							$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
						},
						success : function(data) {
							$.messager.progress('close');
							$('#infolist').datagrid("reload");
							$('#infolist').datagrid('clearChecked');
							$.messager.alert('提示',"保存成功！");
						},
						error : function(data) {
							$.messager.progress('close');
							$('#infolist').datagrid('clearChecked');
							$.messager.alert('提示',"保存失败！");
						}
					});
		} else {
			$.messager.alert("操作提示", "请选择要保存的条目！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	
	
	
	//编辑
	function edit(){
		var row = $('#infolist').datagrid('getSelected');
		if(row){
			var index = $('#infolist').datagrid('getRowIndex',row);
			$('#infolist').datagrid('beginEdit', index);
		}else{
			$.messager.alert("操作提示", "请选择要编辑的条目！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//删除
	function del() {
		var rows = $('#infolist').datagrid('getSelections');
		if (rows.length>0) {
			$.messager.confirm('确认', '确定要删除选中信息吗?', function(res) {//提示是否删除
				if (res) {
					var ids = '';
					for ( var i = 0; i < rows.length; i++) {
						if(rows[i].id == null || rows[i].id == ''){
							$('#infolist').datagrid('deleteRow',$('#infolist').datagrid('getRowIndex',rows[i]));
						}else{
							if (ids != '') {
								ids += ',';
							}					
							ids += rows[i].id;
						}
																							
					};
					if(ids != ''){
						$.ajax({
							url : '<%=basePath%>drug/split/drugSplitDel.action?id='+ ids,
							type : 'post',
							success : function(data) {
								$('#infolist').datagrid('deleteRow',$('#infolist').datagrid('getRowIndex',rows[i]));	
								$.messager.alert('提示',data.resMsg);
								if(data.resCode=='success'){
									$('#infolist').datagrid('reload');
								}
							}
						}); 
					}
 					
				}
			});
		} else {
			$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//添加剂型
	function addDrugForm(){
		openForm();
	}
	
	//显示包装单位格式化，最小单位
	function minFamater(value){
		if(value!=null){
			for(var i=0;i<minList.length;i++){
				if(value==minList[i].encode){
					return minList[i].name;
				}
			}	
		}
	}
	
	//显示包装单位格式化
	function packUnitFamater(value,row,index){
		if(value!=null){
			for(var i=0;i<packUnitList.length;i++){
				if(value==packUnitList[i].encode){
					return packUnitList[i].name;
				}
			}	
		}
	}
	//显示包装单位格式化
	function dosageformFamater(value,row,index){
		if(value!=null){
			for(var i=0;i<dosageformList.length;i++){
				if(value==dosageformList[i].encode){
					return dosageformList[i].name;
				}
			}	
		}
	}
	/**
	 * 添加弹出框
	 */
 	function AddFormdilog(title, url) {
 		$('#windowOpen').dialog({    
 		    title: title,    
 		    width: '800',    
 		    height:'600',    
 		    closed: false,    
 		    cache: false,    
 		    href: url,    
 		    modal: true   
 		   });    
 	}
	/**
	 * 打开弹出框
	 */
 	function openBedDialog() {
 		$("#windowOpen").dialog("open"); 
 	};
	
	//路径
 	function openForm(){
		AddFormdilog("剂型", "<%=basePath%>drug/split/showWin.action"); 
 	};
 	
 	
    
 	
 	//校验重复数据begin---------------------------------------------------------------------------------------
 	Array.prototype.indexOf = function(val) {
		for (var i = 0; i < this.length; i++) {
		     if (this[i] == val) return i;
		}
		return -1;
	};
	//hedong 20160413 复写 数组js数组的remove
    Array.prototype.remove = function(val) {
		var index = this.indexOf(val);
		if (index > -1) {
		this.splice(index, 1);
		}
    };
 	var allDrugs=[];
 	var allDrugsToRemove=[];
 	//hedong 20160416 获得所有行数据的json格式  遍历并放入 allDrugs，allDrugsToRemove两个数组中
 	function getAllJsonCheck(){
 		var jsonAll = JSON.stringify($('#infolist').datagrid("getRows"));
 		var checkedJson = JSON.stringify($('#infolist').datagrid("getChecked"));
 		var datas = eval(jsonAll);
 		var datasChecked= eval(checkedJson);
 		
 		allDrugs=[];
 		allDrugsToRemove=[];
 		for(var i=0;i<datas.length;i++){
 			var tmpId="";
 			 if(datas[i].id){
 				tmpId=datas[i].id;
 			} 
 			var tmpDrugId=datas[i].drugCode;
 			var tmpDrugName=datas[i].drugName;
 			var tmpDeptCode=datas[i].deptCode;
 			allDrugs.push(tmpId+"_"+tmpDrugId+"_"+tmpDrugName+"_"+tmpDeptCode);
 			allDrugsToRemove.push(tmpId+"_"+tmpDrugId+"_"+tmpDrugName+"_"+tmpDeptCode);
 		}
 		
 		var checkedArr=[];
 		for(var i=0;i<datasChecked.length;i++){
 			var tmpId="";
 			 if(datasChecked[i].id){
 				tmpId=datasChecked[i].id;
 			} 
 			var tmpDrugId=datasChecked[i].drugCode;
 			var tmpDrugName=datasChecked[i].drugName;
 			var tmpDeptCode=datasChecked[i].deptCode;
 			checkedArr.push(tmpId+"_"+tmpDrugId+"_"+tmpDrugName+"_"+tmpDeptCode);
 		}
 		
 		return checkData(checkedArr);
 	}
 	
 	
 	/**
 	*hedong 20160416 
 	*checkedArr 被选中的单条数据
 	*将 被选中的单挑数据 从拥有全部数据的数组中进行删除 
 	*/
  function checkData(checkedArr){
 	  var flg1=1;
 	  for(var x=0;x<checkedArr.length;x++){
 	    allDrugsToRemove.remove(checkedArr[x]);//移除选中的数据
 	    if(checkDrugDept(checkedArr[x])==0){
 	       flg1 = 0;
 	       break;
 	    }
 	  }
 	  return flg1;
 	}

   /**
    *checkedData 被选中的单条数据
	*hedong 20160416 被选中的单条数据与排除当前数据的数据进行比较
	*/
 	function checkDrugDept(checkedData){
 	    var flg2=1;
 	    var checkedDataArr = checkedData.split("_");
 	    var drugId = checkedDataArr[1];
 	    var drugName = checkedDataArr[2];
 	    var deptCode = checkedDataArr[3];
 	    for(var x=0;x<allDrugsToRemove.length;x++){
 	       var drugSplitArr  = allDrugsToRemove[x].split("_");
 	       var drugIdTMP= drugSplitArr[1];
 	       var drugNameTMP = drugSplitArr[2];
 	       var deptCodeTMP = drugSplitArr[3];
 	       if(drugId==drugIdTMP&&deptCode==deptCodeTMP){
 	          $.messager.alert("操作提示", '数据出现重复!药品名称：'+drugNameTMP+'!', "warning");
 	         setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
 	          flg2=0;
 	          break;
 	       }
 	    }
 	    allDrugsToRemove=[];
 	    allDrugsToRemove=allDrugs;
 	    return flg2;
 	}
 	//校验重复数据end---------------------------------------------------------------------------------------
 	
 	// 药品列表查询重置
		function searchReload() {
			$('#queryName').textbox('setValue','');
			searchFrom();
		}
		// 药品列表查询重置
		function searchReloadNext() {
			$('#queryDrugName').textbox('setValue','');
			searchDrugFrom();
		}
</script>

</head>
<html>
	<body style="margin: 0px;padding: 0px;">
		<div class="easyui-layout" style="width: 100%; height: 100%; overflow: hidden;" id="divLayout">
			<div id="p" data-options="region:'west',border:false" style="width: 45%; padding: 0px; overflow: hidden;">
				<div id="el" class="easyui-layout" data-options="fit:true">   
					<div class="drugSplitList" data-options="region:'north',split:false,border:true" style="padding: 5px 2px;height:40px;border-top:0;border-right:0">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="" nowrap="nowrap"
									style="width:300px;">
									    查询条件：
									<input class="easyui-textbox" id="queryName"
										data-options="prompt:'药品名称,拼音,五笔,自定义,通用名'"
										style="width:250px;" />
								</td>
								<td>
									&nbsp;
									<a href="javascript:void(0)" onclick="searchFrom()"
										class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>   
					<div data-options="region:'center',border:false" style="width:100%;height:100%">
						<table id="list" data-options="url:'<%=basePath%>drug/split/queryDrugInfoList.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:true,fit:true,selectOnCheck:true,singleSelect:true,fitColumns:false">
							<thead>
								<tr>
									<th data-options="field:'name'">
										药品名称
									</th>
									<th data-options="field:'spec'">
										药品规格
									</th>
									<th data-options="field:'drugRetailprice'">
										零售价
									</th>
									<th data-options="field:'drugCommonname'">
										通用名
									</th>
									<th
										data-options="field:'drugPackagingunit',formatter:packUnitFamater">
										包装单位
									</th>
									<th
										data-options="field:'drugDosageform',formatter:dosageformFamater">
										剂型
									</th>
							</thead>
						</table>
						</div> 
				</div>
			</div>
			<div data-options="region:'center'" id="content" style="width:55%; height:100%;border-top:0">
				<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%;">
					<div class="drugSplitList" data-options="region:'north',split:false,border:true" style="height:40px;padding: 5px 2px;border-left:0;border-top:0">
						<table cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td style="" nowrap="nowrap" style="width:300px;">
									查询条件：
									<input class="easyui-textbox" id="queryDrugName"
										data-options="prompt:'药品名称,拼音,五笔,自定义,通用名'"
										style="width: 250px;" />
								</td>
								<td>
									<a href="javascript:void(0)" onclick="searchDrugFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
									<a href="javascript:void(0)" onclick="searchReloadNext()" class="easyui-linkbutton" iconCls="reset">重置</a>
								</td>
							</tr>
						</table>
					</div>   
					<div data-options="region:'center',border:false" style="width:100%;height:100%">
						<form id="saveForm" method="post">
							<input type="hidden" name="infoJson" id="infoJson">
							<input type="hidden" name="infoJsonAll" id="infoJsonAll">
						</form>
						<table id="infolist"
								data-options="url:'<%=basePath%>drug/split/queryDrugSplit.action',fit:true,method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:true,singleSelect:false,fitColumns:false,toolbar:'#toolbarId'">
						</table>
					</div> 
				</div>
			</div>
			<div id="toolbarId">
				<shiro:hasPermission name="${menuAlias}:function:save">
					<a href="javascript:void(0)" onclick="save()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true">保存</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:edit">
					<a href="javascript:void(0)" onclick="edit()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-edit',plain:true">编辑</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="${menuAlias}:function:delete">
					<a href="javascript:void(0)" onclick="del()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-remove',plain:true">删除</a>
				</shiro:hasPermission>
			</div>
		</div>
		<div id="windowOpen"></div>
	</div>
	</body>
</html>