<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body style="margin: 0px; padding: 0px">
<div id="divLayout" class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height:135px;">
		<div class="easyui-layout" data-options="fit:true,border:false">
			<div data-options="region:'north',border:false" style="height:30px;margin-top: 3px">
				<shiro:hasPermission name="${menuAlias}:function:save ">
				<a onClick="submit()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				</shiro:hasPermission>
				<a onClick="cleard()" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
				<a onclick="closeLayout()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%">
				<form id="editForm" method="post" style="width: 100%">
<%-- 					<input type="hidden" id="id" name="id" value="${businessStack.id }"> --%>
					<input type="hidden" id="id" name="businessStack.id" value="${businessStack.id }">
					<input type="hidden" id="parent" name="businessStack.parent" value="${businessStack.parent}">
					<input type="hidden" id="createUser" name="businessStack.createUser" value="${businessStack.createUser}">
					<input type="hidden" id="createDept" name="businessStack.createDept" value="${businessStack.createDept}">
					<input type="hidden" id="createTime" name="businessStack.createTime" value="${businessStack.createTime}">
					<input type="hidden" name="stackInfoJson" id="stackInfoJson" />
					<table class="honry-table" style="width:100%">
						<tr>
							<td>科室:</td>
							<td>
								<input class="easyui-textbox" type="text" id="deptName" value="${deptName}" data-options="required:true" style="width: 150px" />
								<input type="hidden" id="deptId" name="businessStack.deptId" value="${businessStack.deptId}" />
							</td>
							<td>组套类型:</td>
							<td>
								<input type="hidden" id="stacktype1" name="businessStack.type" value="${businessStack.type}" style="width: 150px" />
								<input type="text" id="stacktype"  value="${businessStack.type}" style="width: 150px"/>
							</td>
							<td>组套来源:</td>
							<td>
								<input class="easyui-combobox" type="text" id="source" name="businessStack.source" value="${businessStack.source}" data-options="valueField:'id',textField:'text',data:stackSource,required:true" style="width: 150px" missingMessage="请输入组套来源" />
							</td>
							<td>组套对象:</td>
							<td>
								<input class="easyui-combobox" type="text" id="stackObject" name="businessStack.stackObject" value="${businessStack.stackObject}" data-options="valueField:'id',textField:'text',data:object,required:true" style="width: 150px" missingMessage="请输入组套类型" />
							</td>
							<td>组套医师:</td>
							<td>
								<input  class="inputCss" type="text" id="showDoc"  value="${businessStack.docShow}" style="width: 150px"  placeHolder="空格选择组套医师"/>
								<input id="hiddenDoc" name="businessStack.doc" value="${businessStack.doc}" type="hidden">
							</td>
						</tr>
						<tr>
							<td>名称:</td>
							<td>
								<input class="easyui-textbox" type="text" id="name" name="businessStack.name" value="${businessStack.name}" data-options="required:true" style="width: 150px" missingMessage="请输入名称" />
							</td>
							<td>自定义码:</td>
							<td>
								<input class="easyui-textbox" type="text" id="inputCode" name="businessStack.inputCode" value="${businessStack.inputCode}" />
							</td>
							<td>是否共享</td>
							<td>
								<input type="hidden" id="shareFlagh" name="businessStack.shareFlag" value="${businessStack.shareFlag}"/>
								<input type="checkBox"  id="shareFlag" onclick="javascript:checkBoxSelect('shareFlag',0,1)"/>
							</td>
							<td>是否需要确认：</td>
							<td>
								<input type="hidden" id="isConfirmh" name="businessStack.isConfirm" value="${businessStack.isConfirm}" /> 
								<input type="checkBox" id="isConfirm" onclick="javascript:checkBoxSelect('isConfirm',0,1)" /></td>
							</td>
							<td>是否需要预约：</td>
							<td>
								<input type="hidden" id="isOrderh" name="businessStack.isOrder" value="${businessStack.isOrder}"/> 
								<input type="checkBox" id="isOrder" onclick="javascript:checkBoxSelect('isOrder',0,1)" /></td>
							</td>
						</tr>
						<tr>
							<td>备注:</td>
							<td>
								<input class="easyui-textbox"
									id="remark" name="businessStack.remark" value="${businessStack.remark}" />
							</td>
							<td id="use" style="display:none;">组套用处:</td>
							<td id="use1" style="display:none;">
								<input class="easyui-combobox" id="stackInpmertype" name="businessStack.stackInpmertype" style="width:150px;"
									value="${businessStack.stackInpmertype}" data-options="valueField:'id',textField:'text',data:use"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	
	</div>
	<div data-options="region:'center',border:false" style="height: 80%">
	 <input type="hidden" id="inpId">
		<table id="dg1" ></table>
	</div>
</div> 
<div id="selectDoctor"></div>
<div id="tb">
	<a href="javascript:void(0)" class="easyui-linkbutton"
		data-options="iconCls:'icon-add',plain:true" onclick="append()">添加</a>
	<a href="javascript:void(0)" class="easyui-linkbutton"
		data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">移除</a>
	<span style='color: #C24641; font-size: 10px;'> 双击列表进行修改</span>
</div>
	<script>
		 var stackType = [
		                 {"id":1,"text":"药品组套"},
		                 {"id":2,"text":"非药品组套"},
		                 ];
	   var stackSource = [
	                     {"id":1,"text":"全院"},
	                     {"id":2,"text":"科室"},
	                     {"id":3,"text":"医生"}
	                     ];
		    var object = [
			             {"id":1,"text":"财务组套"},
			             {"id":2,"text":"医嘱组套"},
			             ];
		    var use = [
			          {"id":1,"text":"住院"},
			      	  {"id":2,"text":"门诊"}];//组套用处
		    var isDrug = [
			             {"id":1,"text":"是"},
			             {"id":2,"text":"否"}
			             ];
			var isMain = [
			             {"id":1,"text":"是"},
			             {"id":2,"text":"否"}
			             ];
		var editIndex = undefined;
		//取出主表id
		var id =$('#id').val() ;
		var deptNameOld =$('#deptName').val() ;
		var packUnitList=null;
		var unitMap = new Map();
		var stacktype ="${businessStack.type}";
		var chose ="";

		$(function(){
			$.ajax({
				url:"<%=basePath %>baseinfo/stack/exaggeratePackagingUnit.action",
				async:true,
				type:'post',
				success: function(packUnitdata) {
					packUnitList = packUnitdata;
					for(var i=0;i<packUnitList.length;i++){
						unitMap.put(packUnitList[i].encode,packUnitList[i].name);
					}
				}
			});
			$('#stacktype').combobox({    
				valueField:'id',
				textField:'text',
				data:stackType,
				onSelect:function(record){
					$.messager.alert('提示','组套类型禁止修改');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$('#stacktype').combobox('setValue',"${businessStack.type}");
					$('#stacktype').combobox('readonly',true);
				}
			}); 
			$('#stackObject').combobox({    
				onSelect:function(record){
					$.messager.alert('提示','组套对象禁止修改');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$('#stackObject').combobox('setValue',"${businessStack.stackObject}");
					$('#stackObject').combobox('readonly',true);
				}
			}); 
			var Dx = "${businessStack.stackObject}";
			var use = "${businessStack.stackInpmertype}";
			if(Dx=='2'){
				$('#use').show();
		 		$('#use1').show();
		 		$('#stackInpmertype').combobox({
	 				required:true
	 			});
			}
		 	if(Dx=="1"){
	 			$('#use').hide();
	 			$('#use1').hide();
	 			$('#stackInpmertype').combobox({
	 				required:false
	 			});
		 	}
			//stackInpmertype
			if($('#shareFlagh').val()==1){//判断是否编辑状态，编辑状态修改复选框选中状态
				$('#shareFlag').attr("checked", true); 
			}
			if($('#isConfirmh').val()==1){//判断是否编辑状态，编辑状态修改复选框选中状态
				$('#isConfirm').attr("checked", true); 
			}
			if($('#isOrderh').val()==1){//判断是否编辑状态，编辑状态修改复选框选中状态
				$('#isOrder').attr("checked", true); 
			}
			setTimeout(function(){
				initEdatagrid($('#stacktype1').val());
            },2);
			bindBlackEvent("showDoc",keyUpDoctor);
		});
		function initEdatagrid(val){
			chose = $('#stacktype').combobox('getValue');
			$('#dg1').edatagrid({
				url:"<c:url value='/baseinfo/stack/findStackInfoListByType.action'/>?id="+ $('#id').val()+"&stacktype="+stacktype,
				singleSelect:false,
				fit:true,
				border:false,
				toolbar: '#tb',
				columns:[[
					{field:'ck',checkbox:true},
					{field:'id',title:'id',hidden:true},
					{field:'stackInfoItemId',title:'项目编号(实际保存字段)',
						editor:{type:'textbox',options:{required : true}},hidden:true},   
					{field:'stackInfoItemIdShow',title:'项目名称',width:'250px',
						editor:{type:'combogrid', options:{
						required : true,
						rownumbers : true,//显示序号 
						pagination : true,//是否显示分页栏
						striped : true,//数据背景颜色交替
						panelWidth : 550,//容器宽度
						fitColumns : true,//自适应列宽
						mode:'remote',
						pageSize : 5,//每页显示的记录条数，默认为10  
						pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
						url :"<c:url value='/baseinfo/stack/stackInfoName.action'/>", 
						queryParams:{flag:$('#stacktype').combobox('getValue')},
						idField : 'name',
						textField : 'name',
						columns : [ [
							{field : 'id',title : 'id',hidden:true},
							{field : 'name',title : '名字',width : '40%'},
							{field : 'spec',title : '规格',	width: '16%'},
							{field : 'code',title : '编码',width : '16%'}, 
							{field : 'packagingnum',title : '包装数量',width : '12%'},
							{field : 'drugPackagingunit',width : '12%',title : '包装单位',	formatter: packUnitFamater},
							{field : 'unit',title : '单位',width : '6%',formatter: packUnitFamater}, 
							{field : 'drugRetailprice',title : '默认价',width : '8%'},
							{field : 'childrenprice',title : '儿童价',width : '8%'} , 
							{field : 'specialprice',title : '特诊价 ',width : '8%'}  
						] ],
						onClickRow : function(rowIndex, rowData) {
							var rowss=$('#dg1').edatagrid('getRows');
							for(var q=0;q<rowss.length;q++){
								if(rowData.code==null || rowData.code==""){
									$.messager.alert('提示','该药品的编码为空');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
									return;
								}else{
									if(rowData.code==rowss[q].stackInfoItemId){
										var stackInfoItemId = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoItemId'});
										$(stackInfoItemId.target).textbox('setValue',null);
										var drugPackagingunit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'drugPackagingunit'});
										var unit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'unit'});
										if(stacktype=='1'){
											$(drugPackagingunit.target).combogrid('setValue',null); 
										}else if(stacktype=='2'){
											$(unit.target).textbox('setValue',null); 
										}
										var stackInfoUnit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoUnit'});
										$(stackInfoUnit.target).textbox('setValue',null); 
										var defaultpricetg = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'defaultprice'});
										$(defaultpricetg.target).textbox('setValue',null);
										$.messager.alert('提示','该药品已存在，请重新选择药品!');
										setTimeout(function(){
											$(".messager-body").window('close');
										},3500);
										return;
									}
								}
								
							}
							var stackInfoItemId = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoItemId'});
							$(stackInfoItemId.target).textbox('setValue',rowData.code);
							var drugPackagingunit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'drugPackagingunit'});
							var unit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'unit'});
							var stackInfoUnit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoUnit'});
							if(stacktype=='1'){
								$(drugPackagingunit.target).combogrid('setValue',unitMap.get(rowData.drugPackagingunit)); 
								$(stackInfoUnit.target).textbox('setValue',rowData.drugPackagingunit); 

							}else if(stacktype=='2'){
								$(unit.target).textbox('setValue',rowData.unit); 
								$(stackInfoUnit.target).textbox('setValue',rowData.unit); 
							}
							var defaultpricetg = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'defaultprice'});
							if(rowData.defaultprice=='0'){
								$(defaultpricetg.target).textbox('setValue','0');
							}else{
								$(defaultpricetg.target).textbox('setValue',rowData.defaultprice);

							}
							var childrenpricetg = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'childrenprice'});
							$(childrenpricetg.target).textbox('setValue',rowData.childrenprice);
							var specialpricetg = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'specialprice'});
							$(specialpricetg.target).textbox('setValue',rowData.specialprice);
							var combogrid=$(drugPackagingunit.target).combogrid('grid');
						},onBeforeLoad:function(param){
							var stackInfoItemId = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoItemId'});
							$(stackInfoItemId.target).textbox('setValue',null);
						}
					}}},
					{field:'stackInfoNum',title:'开立数量',width:'90px',
						editor:{type:'numberbox',options:{required : true}}},    
					{field:'stackInfoUnit',title:'包装单位(实际保存单位)',width:'10%',
						editor:{type:'textbox'},hidden:true},
					{field:'drugPackagingunit',title:'药品单位',width:'90px',editor:{type:'combogrid' , options:{
						mode:'remote',
						idField : 'name',
						textField : 'name',
						url:'<%=basePath%>baseinfo/stack/packagingUnitcomboBox.action',
						columns:[[
							{field : 'id',title : 'id',hidden:true},
							{field : 'encode',title : 'encode',hidden:true},
							{field : 'name',title : '单位名字',width : '90%'} 
						]],
						onClickRow : function(rowIndex, rowData) {
							var stackInfoUnit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoUnit'});
							$(stackInfoUnit.target).textbox('setValue',rowData.encode);
						},onBeforeLoad:function(param){
							var stackInfoUnit = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoUnit'});
							$(stackInfoUnit.target).textbox('setValue',null);
						}
					}}},
					{field:'unit',title:'单位',width:'50px',
						editor:{type:'textbox'}}, 
					{field:'stackInfoDeptid',title:'执行科室(实际保存字段)',width:'10%',
						editor:{type:'textbox'},hidden:true},
					{field:'stackInfoItemName',title:'执行科室',width:'150px',editor:{type:'combogrid' , options:{
						mode:'remote',
						url :"<%=basePath%>baseinfo/stack/queryDept.action", 
						idField : 'deptName',
						textField : 'deptName',
						columns:[[
							{field : 'id',title : 'id',hidden:true},
							{field:'deptCode',title:'deptCode',hidden:true},
							{field : 'deptName',title : '名字',width : '80%'} 
						]],
						onClickRow : function(rowIndex, rowData) {
							var stackInfoDeptid = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoDeptid'});
							$(stackInfoDeptid.target).textbox('setValue',rowData.deptCode);
						},onBeforeLoad:function(param){
							var stackInfoDeptid = $('#dg1').edatagrid('getEditor', {index:$('#inpId').val(),field:'stackInfoDeptid'});
							$(stackInfoDeptid.target).textbox('setValue',null);
						}
					}}},
					{field:'combNo',title:'组合号',width:'100px',editor:{type:'textbox'}},    
					{field:'stackInfoRemark',title:'备注',width:'100px',editor:'textbox'},
					{field:'defaultprice',title:'默认价',width:'90px',editor:{type:'textbox',options:{readonly:true,required:true}}},
					{field:'childrenprice',title:'儿童价',editor:{type:'textbox'},hidden:true},
					{field:'specialprice',title:'特诊价 ',editor:{type:'textbox'},hidden:true}
				]],
				onBeforeLoad:function(data){ 
				      if(stacktype=='1'){ 
				    	  $("#dg1").edatagrid("showColumn", "drugPackagingunit"); // 设置隐藏列   
				          $("#dg1").edatagrid("hideColumn", "unit"); // 设置隐藏列    
				      }else if(stacktype=='2'){
				    	  $("#dg1").edatagrid("hideColumn", "drugPackagingunit"); // 设置隐藏列   
				    	  $("#dg1").edatagrid("showColumn", "unit"); // 设置隐藏列   
				      }    
				 },
				onBeforeEdit:function(index,row){
					$('#inpId').val(index);
				},onDblClickRow:function(index, row){
					var defaultpricetg = $('#dg1').edatagrid('getEditor', {index:index,field:'defaultprice'});
					if(row.defaultprice=='0'){
						$(defaultpricetg.target).textbox('setValue','0');
					}else{
						$(defaultpricetg.target).textbox('setValue',row.defaultprice);

					}
				}
			});
		}
/*************************datagrid列表操作BEGIN*************************************/
//添加
function append(){
	var type=$('#stacktype').combobox('getValue');
	var isDrugValue;
	var isDrugShow;
	if(type==1){
		isDrugValue=1;
		isDrugShow='是';
	}else if(type==2){
		isDrugValue=2;
		isDrugShow='否';
	}
	$('#dg1').edatagrid('appendRow',{
		isDrugShow:isDrugShow,
		isDrug:isDrugValue,
		stackInfoNum:1
	});
}
//删除
function removeit(){
	var rows = $('#dg1').edatagrid('getChecked');
	if (rows.length > 0) {
		$.messager.confirm('确认', '确定要移出选中信息吗?', function(res) {//提示是否删除
			if (res) {
				var ids = '';
				for ( var i = 0; i < rows.length; i++) {
					if(rows[i].id!=null&&rows[i].id!=''){
						if(ids!=null&&ids!=''){
							ids += ',';
						}
						ids += rows[i].id
					}else{
						var dd = $('#dg1').edatagrid('getRowIndex',rows[i]);//获得行索引
						$('#dg1').edatagrid('deleteRow', dd);//通过索引删除该行
						return ;
					}
				}
				if(ids!=null){
					$.messager.progress({text:'正在删除组套，请稍后...',modal:true});
					$.ajax({
						url:"<%=basePath%>baseinfo/stack/delStackinfo.action",
						data:{id:ids},
						success:function(data){
							if(data=="yes"){
								$.messager.progress('close');
								$.messager.alert('提示','删除成功！');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
					        	$('#divLayout').layout('remove','east');
					             //实现刷新栏目中的数据
					             $('#tDt').tree('reload');
			                     $("#list").datagrid("reload");
							}else{
								$.messager.progress('close');
								$.messager.alert('提示','删除失败！');
							}
						},error : function(data) {
							$.messager.progress('close');
							$.messager.alert('提示','删除失败！');
						}
					});
				}
			} else {
				$.messager.alert("操作提示", "请选择要删除的条目！", "warning");
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
			}
		});
	}
}
/***************************datagrid列表操作END***********************************/
	//提交验证
	function submit(){
		var rows= $('#dg1').edatagrid('getRows');
		for(var j=0;j<rows.length;j++){
			var type=$('#stacktype').combobox('getValue');
			$('#dg1').edatagrid('endEdit',j);
			var b=$('#dg1').edatagrid('validateRow',j);
			if(type!=3){
				if(type==1){
					if(rows[j]['stackInfoUnit']==null||rows[j]['stackInfoUnit']==''){
						$.messager.alert('提示',"请填写单位");
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						return ;
					}
				}else{
				}
			}
			if(!b){
				$('#dg1').edatagrid('selectRow',j);
				$.messager.alert('提示','列表验证没有通过，不能保存，可能是下拉列表不是选择的，是输入的');
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return;
			}
		}
		
	 	if (endEditing()){
			$('#dg1').edatagrid('acceptChanges');
			$('#stackInfoJson').val(JSON.stringify( $('#dg1').edatagrid("getRows")));
			var deptName =$('#deptName').val();
			if(deptNameOld==deptName){
			}else{
				if(deptName!=""){
					$('#deptId').val(deptName);
				}
			}
			if(checkSubmit()){
				$('#editForm').form('submit',{
			        url:"<c:url value='/baseinfo/stack/saveOrUpdate.action'/>",
			        onSubmit:function(){
			        	if(!$(this).form('validate')){
							$.messager.show({  
						         title:'提示信息' ,   
						         msg:'验证没有通过,不能提交表单!'  
						    }); 
						    return false ;
						}
			            $.messager.progress({text:'保存中，请稍后...',modal:true});
			        },  
			        success:function(data){
			        	$.messager.progress('close');
			        	$.messager.alert('提示','保存成功');
			        	setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
			        	$('#divLayout').layout('remove','east');
			             //实现刷新栏目中的数据
			             $('#tDt').tree('reload');
	                     $("#list").datagrid("reload");
			        },
					error : function(data) {
						$.messager.alert('提示',"保存失败！");
					}			         
			  	}); 
			}
		 	
		}
	}
	function checkSubmit(){
		var value=$('#stackObject').combobox('getValue');
		if(value == "3"){
			var doc=$("#showDoc").val(); 
			if(doc =null ||doc==""){
				$.messager.alert('警告','请填写组套医师');  
				setTimeout(function(){
					$(".messager-body").window('close');
				},3500);
				return false;
			}
		}
		return true;
	}
	function endEditing(){
		var index = $('#dg1').edatagrid("getRows").length;
		$('#dg1').edatagrid('acceptChanges');
		for (var i=0;i<index;i++){
		   $('#dg1').edatagrid('beginEdit', i);
	       var dataGridRowData = $('#dg1').edatagrid('getRows')[i];
	       var stackInfoFee = dataGridRowData.stackInfoFee;
		}
		return true;
	}

	function keyUpDoctor() { 
		Adddilog("选择组套医师","<c:url value='/sys/queryRoleUser.action?falgs=1'/>",'selectDoctor');
	}
	//关闭dialog
	function closeDialogUser(id) {
		$('#'+id).dialog('close');  
	}
	//加载dialog
	function Adddilog(title, url,id) {
		$('#'+id).dialog({    
		    title: title,    
		    width: '70%',    
		    height:'80%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true 
		   });  
	}
	//复选框   ---------------------是否共享 1-共享  0 不共享
	function checkBoxSelect(id,defalVal,setVal){
		var hiddenId=id+"h";
		if($("#"+id).is(':checked')){
			$('#'+hiddenId).val(setVal);
		}else{
			$('#'+hiddenId).val(defalVal);
		}
	}
	//打开dialog
	function openDialog(id) {
		$('#'+id).dialog('open'); 
	}
	//关闭dialog
	function closeDialog(id) {
		$('#'+id).dialog('close');  
	}
	//清除所填信息
	function cleard(){
		$('#editForm').form('reset');	}
	function closeLayout(){
		$('#divLayout').layout('remove','east');
	}
	//显示单位格式化
	function packUnitFamater(value){
		if(stacktype=='1' ){
			if(value!=null){
				for(var i=0;i<packUnitList.length;i++){
					if(value==packUnitList[i].encode){
						return packUnitList[i].name;
					}
				}	
			}
		}else if(stacktype=='2'){
			return value;
		}
		
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>