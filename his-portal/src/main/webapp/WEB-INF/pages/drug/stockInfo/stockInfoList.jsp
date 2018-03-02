<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>药品仓库维护</title>
<%@ include file="/common/metas.jsp"%>
</head>
	<body>
		<div id="cc" class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',fit:true,border:false"> 
				<div  class="easyui-layout" data-options="fit:true">
					<div data-options="region:'north',split:false" style="padding:5px 0px 0px 5px;border:0px;height:60px;">	        
						<form id="search" method="post" action=""style="height: 100%;width: 100%">
							<input type="hidden" name="drugTypeHidden" id="drugTypeHidden">
							<table cellspacing="0" fit="true" cellpadding="0" border="0px solid black" style="height: 100%;width: 100%">
								<tr>
									<td class="stockInfoList">
									药品名称：<input class="easyui-textbox" ID="drugName" name="drugName"  style="width:120px;"/>
									&nbsp;药品生产厂家：<input class="easyui-textbox" ID="drugManfactory" name="drugManfactory"  style="width:120px;"/>
									&nbsp;药品性质：<input class="easyui-combobox" ID="drugQuality" data-options="url:'${pageContext.request.contextPath}/baseinfo/pubCodeMaintain/queryDictionary.action?type=drugProperties', valueField:'encode',textField:'name',multiple:false" name="drugQuality"  style="width:130px;"/>
									&nbsp;科室：<input ID="deptId" name="deptId" type="hidden" /><input id="deptIdText"  style="width: 200" />	<a href="javascript:delSelectedData('deptId,deptIdText');" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
									&nbsp;药品类别：<input class="easyui-combobox" id="drugType" data-options="url:'${pageContext.request.contextPath}/baseinfo/pubCodeMaintain/queryDictionary.action?type=drugType', valueField:'encode',textField:'name',multiple:false" name="drugType"  style="width:125px;"/>
									</td>
								</tr>
								<tr>
									<td>
									有效期：&nbsp;<input id="drugValidDateS" name="drugValidDateS" class="Wdate" style="width:120px;" type="text" onFocus="var endDate=$dp.$('drugValidDateE');WdatePicker({dateFmt:'yyyy-MM-dd',onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'drugValidDateE\')}'})"/>
									&nbsp;至&nbsp;<input id="drugValidDateE" name="drugValidDateE" class="Wdate" style="width:120px;" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'drugValidDateS\')}'})" />
					    			&nbsp;库存0不显示:<input type="checkBox"  id="storeSum" onclick="javascript:checkBoxQuickSelect('storeSum',1)"/>
						    		&nbsp;停用不显示:<input type="checkBox" id="stop" onclick="javascript:checkBoxQuickSelect('stop',3)"/>
					    			&nbsp;库存：<input class="easyui-combobox" ID="storeSumSymbol" data-options="data:[{id:'1',text:'大于等于'},{id:'0',text:'等于'},{id:'2',text:'小于等于'}], valueField:'id',textField:'text',multiple:false" name="storeSumSymbol"  style="width:98px;"/>
									&nbsp;库存数量：<input class="easyui-numberbox" ID="storeSumNumber" name="storeSumNumber"  style="width:70px;"/>
									<shiro:hasPermission name="${menuAlias}:function:query">
					    				<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
					    				<a href="javascript:void(0)" onclick="searchReload()" class="easyui-linkbutton" iconCls="reset">重置</a>
					    			</shiro:hasPermission>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div data-options="region:'center'" style="padding-top:5px;height: 87%">
						<table id="list"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',toolbar: '#tb'">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true" ></th>
									<th data-options="field:'id',hidden:true" ></th>
									<th data-options="field:'drugId',width:100,hidden:true" >药品编码</th>
									<th data-options="field:'drugName',width:'13%'" >名称</th>
									<th data-options="field:'storageDeptid',width:'5%',formatter:deptCombo" >科室</th>
									<th data-options="field:'drugNameinputcode',width:'6%'" >名称自定义码</th>
									<th data-options="field:'drugSpec',width:'5%'" >规格</th>
									<th data-options="field:'drugDosageform',width:'4%',formatter:dosageformFamater" >剂型</th>
									<th data-options="field:'drugRetailprice',width:'5%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}" >零售价</th>
									<th data-options="field:'drugWholesaleprice',hidden:true,align:'right'" >批发价</th>
									<th data-options="field:'drugMinimumunit',width:'5%'" >最小单位</th>
									<th data-options="field:'drugCommonname',width:'11%'" >通用名称</th>
									<th data-options="field:'packQty',width:'4%'" >包装数量</th>
									<th data-options="field:'packUnit',width:'4%'" >包装单位</th>
									<th data-options="field:'drugType',width:'4%',formatter:drugTypeFamater" >药品类别</th>
									<th data-options="field:'drugQuality',width:'5%',formatter:drugQualityFamater" >药品性质</th>
									<th data-options="field:'validDate',width:'7%',editor:{type:'datebox'}" >有效期</th>
									<th data-options="field:'placeCode',width:'3%',editor:{type:'textbox'}" >货位号</th>
									<th data-options="field:'storePackSum',width:'4%'" >库存量</th>
									<th data-options="field:'storeSum',width:'5%'" >库存总数量</th>
									<th data-options="field:'storeCost',width:'5%',align:'right',formatter: function (value, row, index) {if (row != null) { return parseFloat(value).toFixed(4);}}" >总金额</th>
									<th data-options="field:'validFlag',width:'7%',editor:{type:'combobox', options:{valueField:'id',textField:'text',data:validDrug}},formatter:validFamater" >药品有效性状态</th>
									<th data-options="field:'lowSum',width:'5%',editor:{type:'numberbox',options:{required:true}}" >最低库存量</th>
									<th data-options="field:'topSum',width:'5%',editor:{type:'numberbox',options:{required:true}}" >最高库存量</th>
									<th data-options="field:'dailtycheckFlag',width:'5%',editor:{type:'combobox', options:{valueField:'id',textField:'text',data:[{id:'1',text:'需要'},{id:'0',text:'非' }]}},formatter:daliyFamater" >日盘点标志</th>
									<th data-options="field:'manageQuality',width:'6%',editor:{type:'combobox', options:{valueField:'encode',textField:'name',url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type=drugProperties'}},formatter:propertiesFamater" >药品库存性质</th>
									<th data-options="field:'preoutSum',hidden:true" >预扣库存数量</th>
									<th data-options="field:'preoutCost',hidden:true,align:'right'" >预扣库存金额</th>
									<th data-options="field:'lackFlag',width:'4%',editor:{type:'combobox', options:{valueField:'id',textField:'text',data:[{id:'1',text:'是'},{id:'0',text:'否'}]}},formatter:lackFamater" >是否缺药</th>
									<th data-options="field:'remark',width:'10%',editor:{type:'textbox'}" >备注</th>
								</tr>
							</thead>
						</table>
						<div id="tb" style="height: auto">
							<form id="uploadForm" enctype="multipart/form-data" method="post">
								<shiro:hasPermission name="${menuAlias}:function:add">
									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="submit()">保存</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:query">
									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="view()">查看明细</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:export">
									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-down'" onclick="exportExcel()">导出</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:download">
									<a href="javascript:void(0)" id="stockInfo" name="药品仓库导入数据模板" onclick="modelExcel(this.id,'药品仓库导入数据模板')" class="easyui-linkbutton"  data-options="iconCls:'icon-arrow_down',plain:true">下载模板</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:upload">
									&nbsp;<input name="file" type="file" id="fileFileName" />
									<input type="button" value="浏览" OnClick="JavaScript:$('#fileFileName').click();">
									<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-up',plain:true" onclick="save()" >开始上传</a>
				   				</shiro:hasPermission>
							</form>
						</div>				
						<form id="editForm" method="post">
							<input type="hidden" id="stockinfoJson" name="stockinfoJson" >
							<input type="hidden" name="infoJson" id="infoJson">
						</form>
						<div id="viewDetail"></div>
					</div>
				</div>
			</div> 
		</div>	
		<script type="text/javascript">
			var deptMap;
			$.extend($.fn.datagrid.defaults.editors, {    
				datebox: {    
			        init: function(container, options){    
			            var input = $('<input  class="Wdate" type="text" onClick="WdatePicker({dateFmt:\'yyyy-MM-dd\',maxDate:\'{%y+5}-%M-%d\'})"  style="width:200px;border: 1px solid #95b8e7;border-radius: 5px;"/>').appendTo(container);    
			            return input;    
			        }, 
			        getValue: function(target){    
			            return $(target).val();    
			        },    
			        setValue: function(target, value){    
			            $(target).val(value);    
			        },    
			        resize: function(target, width){    
			            var input = $(target);    
			            if ($.boxModel == true){    
			                input.width(width - (input.outerWidth() - input.width()));    
			            } else {    
			                input.width(width);    
			            }    
			        }    
		    	}
			});
		    var validDrug=[{id:1,text:'在用'},{id:0,text:'停用'}];
			var str="";
			var drugpropertiesList="";
			var drugMinimumunitList="";
			var drugDosageformList="";
			var drugDrugtypeList="";
			var drugpropertiesList="";
			var drugpackagingunitList="";
			$.ajax({
				url: "<%=basePath%>publics/consultation/querydeptComboboxs.action", 
				success: function(deptData) {
					deptMap = deptData;
				}
			});
			$(function(){
				$('#list').datagrid({
					checkOnSelect:true,
					selectOnCheck:false,
					singleSelect:true,
					pagination : true,
					url:'${pageContext.request.contextPath}/drug/stockinfo/queryDrugStockInfo.action',
					pageSize : 20,
					pageList : [ 20, 30, 50, 80, 100 ],
					onBeforeLoad:function (param) {
						$('#list').datagrid('uncheckAll');
						$.ajax({
							url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
							data:{"type" : "drugProperties"},
							type:'post',
							success: function(data) {
								drugpropertiesList = data;
							}
						});
						$.ajax({
							url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
							data:{"type" : "dosageForm"},
							type:'post',
							success: function(data) {
								drugDosageformList = data;
							}
						});
						$.ajax({
							url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
							data:{"type" : "drugType"},
							type:'post',
							success: function(data) {
								drugDrugtypeList = data;
							}
						});
						$.ajax({
							url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
							data:{"type" : "drugProperties"},
							type:'post',
							success: function(data) {
								drugpropertiesList = data;
							}
						});
						// 2016年10月18日  GH 注释掉  查询包装单位编码，暂时不需要   因修改为直接从表中获取单位，
						//$.ajax({
						//	url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>",
						//	data:{"type" : "packunit"},
						//	type:'post',
						//	success: function(data) {
						//		drugpackagingunitList = data;
						//	}
						//});
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
				 $('#fileFileName').hide();
			});
			
			//下载模板
			function modelExcel(id,name){
				window.location.href ="<%=basePath%>drug/stockinfo/downLoadExcelMode.action?exceLName="+id+"&name="+encodeURIComponent(encodeURIComponent(name));
			}
			//快速过滤复选框
			function checkBoxQuickSelect(id,val){
				if($('#'+id).is(':checked')){
					if(id=='storeSum'){
						$('#list').datagrid('load', {
								storeSum: val
						});
					}if(id=='stop'){
						$('#list').datagrid('load', {
							stop: val
						});
					}if(id=='lowSum'){
						$('#list').datagrid('load', {
							lowSum: val
						});
					}if(id=='upSum'){
						$('#list').datagrid('load', {
							upSum: val
						});
					}
				}else{
					$('#list').datagrid('load', {});
				}
			}
			//查询
			function searchFrom(){
			    var deptId =$.trim($('#deptId').val());//查询条件科室ID
			    var drugName = $.trim($('#drugName').textbox('getValue'));//药品名称
			    var drugType =	$.trim($('#drugType').combobox('getValue'));//药品类别
			    var drugManfactory =$.trim($('#drugManfactory').textbox('getValue'));//生产厂家
			    var drugQuality = $.trim($('#drugQuality').combobox('getValue'));//药品性质
			    var drugValidDateS =$.trim($('#drugValidDateS').val());
			    var drugValidDateE =$.trim($('#drugValidDateE').val());
			    var storeSumSymbol = $.trim($('#storeSumSymbol').combobox('getValue'));
			    var storeSumNumber =$.trim($('#storeSumNumber').textbox('getValue'));
			    $('#list').datagrid('load', {
			    	deptId: deptId,
			    	drugName:drugName,
			    	drugType:drugType,
			    	drugManfactory:drugManfactory,
			    	drugQuality:drugQuality,
			    	drugValidDateS:drugValidDateS,
			    	drugValidDateE:drugValidDateE,
			    	storeSumSymbol:storeSumSymbol,
			    	storeSumNumber:storeSumNumber
				});
			}
			//查看详细
			function view(){
				var row = $('#list').datagrid('getSelected'); //获取当前选中行     
                if(row){
               		Adddilog("药品库存列表","<%=basePath%>drug/stockinfo/queryDrugStorageUrl.action?drugStockinfoId="+row.id);
			   	}
			}
			//导出列表//由于数据过多,直接导出容易崩溃,这里导出方法换成选中数据导出
			function exportExcel() {
				var rows = $('#list').datagrid("getChecked");
				if (rows.length == 0) {
					$.messager.alert("操作提示", "请选择要导出的记录！", "warning");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					return false;
				}
				$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
					if (res) {
						//为了结束编辑行 不然获取的rows里没有编辑状态下的数据
						for ( var i = 0; i < rows.length; i++) {
							$('#list').datagrid("endEdit",
							$('#list').datagrid("getRowIndex", rows[i]));
						}
						$('#infoJson').val(JSON.stringify(rows));
						$('#editForm').form('submit', {
							url :"<%=basePath%>drug/stockinfo/exportStockInfo.action",
							success : function(data) {
								$.messager.alert("操作提示", "导出成功！", "success");
							},
							error : function(data) {
								$.messager.alert("操作提示", "导出失败！", "error");
							}
						});
					}
				});
			}
			//提交验证 
			function submit(){
				$('#list').datagrid('acceptChanges');
				$('#stockinfoJson').val(JSON.stringify( $('#list').datagrid("getChecked")));
				$.messager.progress({text:'保存中，请稍后...',modal:true});	// 显示进度条
				$('#editForm').form('submit',{
			        url:"<%=basePath%>drug/stockinfo/saveStockinfo.action",
			        success:function(data){  
			        	$.messager.progress('close');
						$("#list").datagrid("reload");
			        },
					error : function(data) {
						$.messager.progress('close');
						$.messager.alert('提示',"保存失败！");	
					}			         
				 }); 
			}
			//双击编辑
			$('#list').datagrid({  
				onDblClickRow: function(rowIndex, rowData){
					$('#list').datagrid('beginEdit',rowIndex);
					var validFlag = $('#list').datagrid('getEditor', {index:rowIndex,field:'validFlag'});
					$(validFlag.target).textbox('setValue','1');
					$(validFlag.target).textbox('setText','在用');
					var dailtycheckFlag = $('#list').datagrid('getEditor', {index:rowIndex,field:'dailtycheckFlag'});
					$(dailtycheckFlag.target).textbox('setValue','0');
					$(dailtycheckFlag.target).textbox('setText','非');
				}
			});
			
			function isContains(str, substr) {
			    return str.indexOf(substr) >= 0;
			}
			function save(){
				 var fileObj = document.getElementById("fileFileName").files[0]; // 获取文件对象
				 if(fileObj){
					$('#uploadForm').form('submit',{
				        url:"<c:url value='/drug/stockinfo/importStockInfo.action'/>",
				        success:function(retVal){
							  if(retVal=='"OK"'){
								  $.messager.alert('提示',"上传成功!");
								  $("#list").datagrid("reload");
							  }else {
								  $.messager.alert('提示',"上传失败！");
							 }
				        },
						error : function(data) {
							$.messager.alert('提示',"保存失败！");	
						}			         
					 }); 
				 }else{
					 $.messager.alert('提示',"请浏览选择文件!");
				 }
			}
			//回车事件
			function KeyDown() {  
		   		if (event.keyCode == 13) {  
			        event.returnValue=false;  
			        event.cancel = true;  
			        searchFrom();  
			    }  
			} 
			//格式化药品有效性
			function validFamater(val,row){
				if (val == 1){
					return '在用';
				}if (val == 0){
					return '停用';
				}
			}
			//格式化是否缺药
			function lackFamater(val,row){
				if (val == 1){
					return '是';
				} if (val == 0) {
					return '否';
				}
			}
			//格式化日盘点
			function daliyFamater(val,row){
				if (val == 1){
					return '需要';
				} if (val == 0) {
					return '非';
				}
			}
			//格式化库存属性
			function propertiesFamater(value,row,index){
				for(var i=0;i<drugpropertiesList.length;i++){
					if(value==drugpropertiesList[i].encode){
						return drugpropertiesList[i].name;
					}
				}	
			}
			//这里最小单位的list全部替换成包装单位的list
			function minimumunitFamater(value,row,index){
				for(var i=0;i<drugpackagingunitList.length;i++){
					if(value==drugpackagingunitList[i].encode){
						return drugpackagingunitList[i].name;
					}
				}	
			}
			function dosageformFamater(value,row,index){
				for(var i=0;i<drugDosageformList.length;i++){
					if(value==drugDosageformList[i].encode){
						return drugDosageformList[i].name;
					}
				}	
			}
			function drugTypeFamater(value,row,index){
				for(var i=0;i<drugDrugtypeList.length;i++){
					if(value==drugDrugtypeList[i].encode){
						return drugDrugtypeList[i].name;
					}
				}	
			}
			function drugQualityFamater(value,row,index){
				for(var i=0;i<drugpropertiesList.length;i++){
					if(value==drugpropertiesList[i].encode){
						return drugpropertiesList[i].name;
					}
				}	
			}
			function packUnitFamater(value,row,index){
				for(var i=0;i<drugpackagingunitList.length;i++){
					if(value==drugpackagingunitList[i].encode){
						return drugpackagingunitList[i].name;
					}
				}	
			}
			//加载dialog
			function Adddilog(title, url) {
				$('#viewDetail').dialog({    
				    title: title,    
				    width: '60%',    
				    height:'80%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				});    
			}
			//打开dialog
			function openDialog() {
				$('#viewDetail').dialog('open'); 
			}
			//关闭dialog
			function closeDialog() {
				$('#viewDetail').dialog('close');  
			}
			
			
		 /**
		   * 回车弹出科室弹框
		   * @author  zhuxiaolu
		   * @param deptIsforregister 是否是挂号科室 1是 0否
		   * @param textId 页面上commbox的的id
		   * @date 2016-03-22 14:30   
		   * @version 1.0
		   */
		   $('#deptIdText').textbox({
				readonly:true,
				required:false,
				prompt:"请回车选择科室"
			});
			bindEnterEvent('deptIdText',popWinToDept,'easyui');//绑定回车事件
				
		 	function popWinToDept(){
			 	popWinDeptCallBackFn = function(node){
				 	$("#deptId").val(node.deptCode);
				 	$('#deptIdText').textbox('setValue',node.deptName);
			   };
				var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?textId=deptId";
				window.open (tempWinPath,'newwindow',' left=400,top=200,width='+ (screen.availWidth -700) +',height='+ (screen.availHeight-370)+',scrollbars,resizable=yes,toolbar=yes')
			}
		
			// 列表查询重置
			function searchReload() {
				$('#search').form('clear');
				searchFrom();
			}
			//科室渲染
			function deptCombo(value,row,index){
				if(value!=null){
					return deptMap[value];
				}
			}
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>