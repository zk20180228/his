<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>手术批费管理</title>
	<%@ include file="/common/metas.jsp" %>
	<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true" >
		<div id="p" data-options="region:'west'" title="住院患者"
			style="width: 20%; padding: 10px">
				<div class="easyui-panel" style="padding:5px">
					&nbsp;住院号：<input class="easyui-textbox" id="medicalrecordId" name="medicalrecordId" onkeydown="KeyDown()"/><br>
					开始时间：<input id="startTime" name="startTime"  class="easyui-datebox" value="new Date();"/><br>
					结束时间：<input id="endTime" name="endTime"  class="easyui-datebox" value="new Date();"/><br>
				</div>
			<div id="treeDiv"><ul id="tDt"></ul></div>	
		</div>
		<div id="oper"></div>
		<div data-options="region:'center'">
			<div id="divLayout" class="easyui-layout" fit=true>
				<div data-options="region:'center',split:false,title:'手术收费通知单',iconCls:'icon-book'" style="padding: 5px;">
					<form id="editForm">
						<fieldset style="padding: 1%">
							<table class="honry-table" cellpadding="0" cellspacing="0"
								border="0" style="width: 100%">
								<tr>
									<td class="honry-lable">
										姓名：
									</td>
									<td>
										<input id="name" name="name" class="easyui-textbox" readonly="readonly">
									</td>
									<td class="honry-lable">
										性别：
									</td>
									<td>
										<input id="CodeSex" name="sex" class="easyui-textbox" readonly="readonly">
									</td>
									<td class="honry-lable">
										年龄：
									</td>
									<td>
										<input id="age" name="age" class="easyui-numberbox" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td class="honry-lable">
										住院科室：
									</td>
									<td>
										<input id="inDept" name="inDept.id" class="easyui-textbox" readonly="readonly">
									</td>
									<td class="honry-lable">
										病例号：
									</td>
									<td>
										<input id="patientNo" name="patientNo"  class="easyui-textbox" readonly="readonly">
									</td>
									<td class="honry-lable">
										自费项目：
									</td>
									<td>
										同意使用
									</td>
								</tr>
							</table>
						</fieldset>
						<fieldset style="height:60%">
							<div style="height:97%">
							<table id="list" class="easyui-edatagrid" data-options="singleSelect:'true',fit:'true'" >   
							    <thead>   
							        <tr>
							            <th data-options="field:'itemCode'" width="6%">编码</th>
							            <th data-options="field:'itemName'" width="6%">项目名称</th>   
							            <th data-options="field:'unitPrice',formatter:funPrice" width="6%">价格</th> 
							            <th data-options="field:'qty',formatter:funNum" width="6%">数量</th>       
							            <th data-options="field:'currentUnit'" width="6%">单位</th>
							            <th data-options="field:'payCost',formatter:funPayCost" width="6%">金额</th>
							            <th data-options="field:'executeDeptcode'" width="7%">执行科室</th>   
							        </tr>  
							    </thead> 
							</table>
							</div>
							<div style="text-align:center;">合计：<input id="zjId" type="text" value="0.00" disabled="disabled"></div> 
							
						</fieldset>
						<fieldset>
							<table class="honry-table" cellpadding="1" cellspacing="1"
								border="1px solid black">
								<tr>
									<td>
									<shiro:hasPermission name="${menuAlias}:function:add">
										<a href="javascript:saveFeed();void(0)"	class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
									</shiro:hasPermission>

									<a href="javascript:clearFeed();void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-remove'">清屏</a>

									<input type="hidden" id="operId" name="operId" value=""/>
									</td>
								</tr>
							</table>
						</fieldset>

					</form>
				</div>
			</div>
		</div>
			 <div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 60 40 60" data-options="modal:true, closed:true">   
		     <table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true">   
		</table>  
    </div>  
	</div>
	<script type="text/javascript">
		$(function(){
			var winH=$("body").height();
			$('#p').height(winH-78-30-27-28);    //78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
			$('#treeDiv').height(winH-78-30-27-38-28-38);
		});
		/**
		 * 回车键查询
		 * @author  wujiao
		 * @param title 标签名称
		 * @param title 跳转路径
		 * @date 2015-06-15
		 * @version 1.0
		 */
		 document.onkeydown=function KeyDown(){
		   var event = arguments[0]||window.event;     
		   if (event.keyCode == 13) {
				event.returnValue = false;
				event.cancel = true;
				searchFrom();
			}
		};
	
  		/**
		 * 填写住院编号后回车查询
		 * @author  zpty
		 * @date 2015-8-20
		 * @version 1.0
		 */
	     function searchFrom(){
	     	var medicalrecordId = $('#medicalrecordId').val();
	     	if(medicalrecordId.length!=6){
	     		$.messager.alert('提示','请输入住院流水号或者病历号的后六位');
	     		return;
	     	}
	     	$.ajax({
					url: '<%=basePath %>operation/itemAndFeeInfo/queryItemAndFeeinfo.action?id='+medicalrecordId,
					type:'post',
					success: function(data) {
						var idCardObj = eval("("+data+")");
						if(idCardObj.length>1){
							//弹框
							$("#diaInpatient").window('open');
							$("#infoDatagrid").datagrid({
								url:'<%=basePath%>operation/itemAndFeeInfo/getInfoByIdOrNo.action?id='+medicalrecordId + "&menuAlias=${menuAlias}",    
							    columns:[[    
							        {field:'inpatientNo',title:'住院号',width:'20%',align:'center'} ,    
							        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
							        {field:'reportSex',title:'性别',width:'20%',align:'center',formatter:function(value,row,index){
							        	return sexMap(value);
							        }} ,
							        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
							        {field:'certificatesNo',title:'身份证号',width:'20%',align:'center'} 
							    ]] ,
							    onDblClickRow:function(rowIndex, rowData){
							    	$.ajax({
										url: '<%=basePath %>operation/itemAndFeeInfo/itemListById.action?id='+rowData.medicalrecordId,
										type:'post',
										success: function(recipedetail) {
											var recList = eval("("+recipedetail+")");
											if(recList!=null&&recList.length>0){
												var listRows = $('#list').edatagrid('getRows');
												if(listRows!=null&&listRows.length>0){
													var listR = listRows.length;
													for(var j=0;j<listR;j++){
														$('#list').edatagrid('deleteRow',0);
													}
												 }
												
												if(recList!=null&&recList.length>0){
													for(var jj=0;jj<recList.length;jj++){
														var listIndex = $('#list').edatagrid('appendRow',{
															id:recList[jj].id,//id值
															itemCode: recList[jj].itemCode,//编码
															itemName: recList[jj].itemName,//名称
															qty: recList[jj].qty==null?1:recList[jj].qty,//数量
															currentUnit:'次',//单位
															executeDeptcode:recList[jj].executeDeptcode,//执行科室
															unitPrice:recList[jj].unitPrice==null?parseFloat(0.00):parseFloat(recList[jj].unitPrice),//单价
															payCost:parseFloat(parseFloat(recList[jj].qty==null?1:recList[jj].qty)*parseFloat(recList[jj].unitPrice==null?parseFloat(0.00):parseFloat(recList[jj].unitPrice)).toFixed(2)).toFixed(2)//金额
														}).edatagrid('getRows').length-1;
														$('#list').edatagrid('beginEdit',listIndex);
														var listRows = $('#list').edatagrid('getRows');
														var zlf = parseFloat(0.00);
														for(var jjj=0;jjj<listRows.length;jjj++){
															var rowIndex = $('#list').edatagrid('getRowIndex',listRows[jjj]);
															var z = parseFloat($('#payCost_'+rowIndex).text());
															zlf = parseFloat(zlf)+parseFloat(z);
														}
														var fy = parseFloat(zlf).toFixed(2);
														$('#zjId').val(fy);
														if($('#ssxjjeId').val()!=null&&$('#ssxjjeId').val()!=""){
															$('#zljeId').val(parseFloat(parseFloat($('#ssxjjeId').val()).toFixed(2)-parseFloat(fy).toFixed(2)).toFixed(2));
														}
													}
												}
											}
										}
										});
							    	 $('#name').textbox('setValue',rowData.patientName);
						    		 $('#CodeSex').textbox('setValue',sexMap.get(rowData.reportSex));
									 $('#age').textbox('setValue',rowData.reportAge);
									 $.ajax({
										 url:'<%=basePath%>operation/itemAndFeeInfo/getDeptByDeptId.action',
										 data:{
											 deptId:rowData.deptCode
										 },
										 success:function(data){
											 var deptMap=eval("("+data+")");
											 $('#inDept').textbox('setValue',deptMap.deptName);
										 }
									 });
									 $('#patientNo').textbox('setValue',rowData.medicalrecordId);
									 if(rowData.patientName!=null && rowData.patientName!=""){
										 //弹出选择手术页面
										 Adddilog("手术信息",'operation/itemAndFeeInfo/viewOperation.action?id='+medicalrecordId);
									 }else{
										 $.messager.alert('提示','查无此人！');
									 }
									$("#diaInpatient").window('close');
							    }
							});
						}
						else if (idCardObj.length=1){
							 $('#name').textbox('setValue',idCardObj.patientName);
							 $('#CodeSex').textbox('setValue',idCardObj.reportSex);
							 $('#age').textbox('setValue',idCardObj.reportAge);
							 $('#inDept').textbox('setValue',idCardObj.deptCode);
							 $('#patientNo').textbox('setValue',idCardObj.medicalrecordId);
							 
							 if(idCardObj.patientName!=null && idCardObj.patientName!=""){
								 //弹出选择手术页面
								 Adddilog("手术信息",'operation/itemAndFeeInfo/viewOperation.action?id='+medicalrecordId);
							 }else{
								 $.messager.alert('提示','查无此人！');
							 }
							 $.ajax({
									url: '<%=basePath %>operation/itemAndFeeInfo/itemListById.action?id='+medicalrecordId,
									type:'post',
									success: function(recipedetail) {
										var recList = eval("("+recipedetail+")");
										if(recList!=null&&recList.length>0){
											var listRows = $('#list').edatagrid('getRows');
											if(listRows!=null&&listRows.length>0){
												var listR = listRows.length;
												for(var j=0;j<listR;j++){
													$('#list').edatagrid('deleteRow',0);
												}
											if(recList!=null&&recList.length>0){
												for(var jj=0;jj<recList.length;jj++){
													var listIndex = $('#list').edatagrid('appendRow',{
														id:recList[jj].id,//id值
														itemCode: recList[jj].itemCode,//编码
														itemName: recList[jj].itemName,//名称
														qty: recList[jj].qty==null?1:recList[jj].qty,//数量
														currentUnit:'次',//单位
														executeDeptcode:recList[jj].executeDeptcode,//执行科室
														unitPrice:recList[jj].unitPrice==null?parseFloat(0.00):parseFloat(recList[jj].unitPrice),//单价
														payCost:parseFloat(parseFloat(recList[jj].qty==null?1:recList[jj].qty)*parseFloat(recList[jj].unitPrice==null?parseFloat(0.00):parseFloat(recList[jj].unitPrice)).toFixed(2)).toFixed(2)//金额
													}).edatagrid('getRows').length-1;
													$('#list').edatagrid('beginEdit',listIndex);
													var listRows = $('#list').edatagrid('getRows');
													var zlf = parseFloat(0.00);
													for(var jjj=0;jjj<listRows.length;jjj++){
														var rowIndex = $('#list').edatagrid('getRowIndex',listRows[jjj]);
														var z = parseFloat($('#payCost_'+rowIndex).text());
														zlf = parseFloat(zlf)+parseFloat(z);
													}
													var fy = parseFloat(zlf).toFixed(2);
													$('#zjId').val(fy);
													if($('#ssxjjeId').val()!=null&&$('#ssxjjeId').val()!=""){
														$('#zljeId').val(parseFloat(parseFloat($('#ssxjjeId').val()).toFixed(2)-parseFloat(fy).toFixed(2)).toFixed(2));
													}
												}
											}
										}
									}
									}
									});
						}
						else{
							$.messager.alert('提示','不存在该患者，请仔细检查您所输入的号码');
						}
					}
				});
	     }
	
	     	//加载dialog
			function Adddilog(title, url) {
				$('#oper').dialog({    
				    title: title,    
				    width: '80%',    
				    height:'80%',    
				    closed: false,    
				    cache: false,    
				    href: url,    
				    modal: true   
				   });    
			}
			//打开dialog
			function openDialog() {
				$('#oper').dialog('open'); 
			}
			//关闭dialog
			function closeDialog() {
				$('#oper').dialog('close');  
			}


	/**
	 * 在列别页面插入树
	 * @author  wj
	 * @param 
	 * @date 2015-06-03
	 * @version 1.0
	 */

	$('#tDt').tree({
		url : '<%=basePath %>operation/itemAndFeeInfo/drugUnorugInfo.action',
		method : 'get',
		animate : true,
		lines : true,
		lines:true,
			onBeforeCollapse:function(node){
			  	if(node.id=="1"){
					return false;
			  	}
			},
			onDblClick:function(node){
				if(node!=null){
					var index = $('#list').edatagrid('appendRow',{
						itemCode: node.id,//编码
						itemName: node.text,//名称
						qty: 1,//数量
						currentUnit:'次',//单位
						executeDeptcode:'',//执行科室
						unitPrice:node.attributes.undrugDefaultprice,//单价
						payCost:parseFloat(parseFloat(1)*parseFloat(node.attributes.undrugDefaultprice).toFixed(2)).toFixed(2)//金额
					}).edatagrid('getRows').length-1;
					$('#list').edatagrid('beginEdit',index);
					var rows = $('#list').edatagrid('getRows');
					var zlf = parseFloat(0.00);
					for(var i=0;i<rows.length;i++){
						var rowIndex = $('#list').edatagrid('getRowIndex',rows[i]);
						var z = parseFloat($('#payCost_'+rowIndex).text());
						zlf = parseFloat(zlf)+parseFloat(z);
					}
					var fy = parseFloat(zlf).toFixed(2);
					$('#zjId').val(fy);
				}
			}
		}); 
		
//为数量添加keyup事件
		function funNum(value,row,index){
			return '<input id="num_'+index+'" type="text" class="datagrid-editable-input numberbox-f textbox-f" value="'+value+'" data-options="min:0,precision:0" onkeyup="keyupNum(this)" style="width:100%"></input>';
		}
		
		//为单价添加id
		function funPrice(value,row,index){
			return '<div id="price_'+index+'">'+value+'</div>';
		}
		
		//为金额添加id
		function funPayCost(value,row,index){
			return '<div id="payCost_'+index+'">'+value+'</div>';
		}
		
		//为数量添加keyup事件
		function keyupNum(inpObj){
			if($(inpObj).val().length==1){
				$(inpObj).val($(inpObj).val().replace(/[^1-9]/g,''));
			}else{
				$(inpObj).val($(inpObj).val().replace(/\D/g,''));
			}
			var val = $(inpObj).val();
			var id = $(inpObj).prop("id").split("_");
			var price = $('#price_'+id[1]).text();
			var money = parseFloat(0.00).toFixed(2);
			if(val!=null&&val!=""){
				money = parseFloat(parseFloat(val)*parseFloat(price).toFixed(2)).toFixed(2);
			}
			$('#list').datagrid('updateRow',{
				index: id[1],
				row: {
					qty:val,
					payCost:money
				}
			});
			$('#num_'+id[1]).val("").focus().val(val);
			var rows = $('#list').edatagrid('getRows');
			var zlf = parseFloat(0.00);
			for(var i=0;i<rows.length;i++){
				var rowIndex = $('#list').edatagrid('getRowIndex',rows[i]);
				var z = parseFloat($('#payCost_'+rowIndex).text());
				zlf = parseFloat(zlf)+parseFloat(z);
			}
			var fy = parseFloat(zlf).toFixed(2);
			$('#zjId').val(fy);
		}

	/**
	 * 格式化日期
	 * @author  zpty
	 * @date 2015-6-19 9:25
	 * @version 1.0
	 */
	function formatDatebox(value) {
		if (value == null || value == '') {
			return '';
		}
		var dt;
		if (value instanceof Date) {
			dt = value;
		} else {

			dt = new Date(value);

		}

		return dt.format("yyyy-MM-dd"); //扩展的Date的format方法
	}
	//扩展的Date的format方法
	Date.prototype.format = function(format) {
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
		};
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};
	
//划价保存
		function saveFeed(){
		var medicalrecordId = $('#medicalrecordId').val();
		var zjId = $('#zjId').val();
			var rows = $('#list').edatagrid('getRows');
			if(rows!=null&&rows.length>0 && medicalrecordId!=null && medicalrecordId!=""){
				if(confirm("确定保存修改信息?")){
					$.messager.progress({text:'保存中，请稍后...',modal:true});
						
			        $.post('<%=basePath %>operation/itemAndFeeInfo/saveInpatientItemList.action?zjId='+zjId+'&medicalrecordId='+medicalrecordId,{"jsonData":$.toJSON(rows)},function(result){
			   			if(result=="success"){
			   				$.messager.progress('close');
			   				$.messager.alert('提示',"操作成功!");
			   				reloadFeed();
			   			}else if(result=="authority"){
			   				$.messager.progress('close');
			   				$.messager.alert('提示',"当前用户无此权限,请联系管理员!");
			   				reloadFeed();
			   			}else{
			   				$.messager.progress('close');
			   				$.messager.alert('提示',"操作失败!");
			   			}
			   		});
		        }else{
		        	return;
		        }
			}else{
				$.messager.alert('提示',"没有信息!");
			}
		}
		
		
		//清屏
		function clearFeed(){
			var listRows = $('#list').edatagrid('getRows');
			if(listRows!=null&&listRows.length>0){
				var listR = listRows.length;
				for(var j=0;j<listR;j++){
					$('#list').edatagrid('deleteRow',0);
				}
			}
			$('#editForm').form('reset');
		}
</script>
</body>
</html>

