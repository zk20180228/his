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
	<script>
	var sexMap=new Map();
	//性别渲染
	$.ajax({
		url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
		}
	});
	</script>
		<style type="text/css">
		.tableCss {
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #95b8e7;
			border-top: 1px solid #95b8e7;
		}
		
		.tableLabel {
			text-align: right;
			width: 150px;
		}
		
		.tableCss td {
			border-right: 1px solid #95b8e7;
			border-bottom: 1px solid #95b8e7;
			padding: 5px 15px;
			word-break: keep-all;
			white-space: nowrap;
		}
		</style>
	</head>
	<body style="margin: 0px;padding: 0px;">
		<div id="el" class="easyui-layout" data-options="fit:true">   
			<div id="west" data-options="region:'west',split:false" style="width:18%;border-top:0">
				<div style="padding:5px 5px 5px 5px;">
					<input type="hidden" id="deptName" value="${deptName}">
					<input type="hidden" id="userName" value="${userName}">
					<ul id="tt">加载中，请稍等...</ul>
				</div>
			</div>   
			<div id="center" data-options="region:'center',border:false" style="width:82%;height:100%">
				<div id="el" class="easyui-layout" data-options="fit:true">   
					<div data-options="region:'north',split:false,border:false" style="width:82%;height:101px">
						<div style="padding:5px 5px 5px 5px;">
							<shiro:hasPermission name="${menuAlias}:function:add">
								<a href="javascript:void(0)" onclick="add()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>&nbsp;&nbsp;
							</shiro:hasPermission>
								<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">删除</a>&nbsp;&nbsp;
								<a href="javascript:void(0)" onclick="cancel()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">取消</a>&nbsp;&nbsp;
							<shiro:hasPermission name="${menuAlias}:function:save">
								<a href="javascript:void(0)" onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;&nbsp;
							</shiro:hasPermission>
						</div>
						<table class="tableCss" cellspacing="5" cellpadding="5">
							<tr>
								<td class="tableLabel" align="right">病历号：</td>
								<td style="width: 200px;"><input class="easyui-textbox" id="inpatNo" data-options="prompt:'住院号，回车查询'"/><input type="hidden" id="inpatNoHid"></td>
								<td class="tableLabel" align="right">患者姓名：</td>
								<td style="width: 200px;" id="pname"></td>
								<td class="tableLabel" align="right">性别：</td>
								<td style="width: 200px;" id="sex"></td>
								<td class="tableLabel" align="right">年龄：</td>
								<td style="width: 200px;;" id="age"></td>
								<td class="tableLabel" align="right">合同单位：</td>
								<td style="width: 200px;" id="pact"></td>
								
							</tr>
							<tr>
								<td class="tableLabel" align="right">住院科室：</td>
								<td style="width: 200px;" id="dept"></td>
								<td class="tableLabel" align="right">病区：</td>
								<td style="width: 200px;" id="ward"></td>
								<td class="tableLabel" align="right">床位：</td>
								<td style="width: 200px;" id="bed"></td>
								<td class="tableLabel" align="right">主治医师：</td>
								<td style="width: 200px;" id="doctor"></td>
								<td class="tableLabel" align="right">可用余额：</td>
								<td style="width: 200px;" id="balance"></td>
							</tr>
						</table>
					</div>   
					<div data-options="region:'center'" style="width:100%;height:100%">
						<table id="list" data-options="method:'post',striped:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,rownumbers:true,fit:true,border:false"></table>
					</div> 
				</div>
			</div> 
		</div>
		<script type="text/javascript">
		
		/**  
		 * 加载后执行的方法
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		$(function(){
			
			
			$('#tt').tree({
				url:'<%=basePath%>technical/tecconfirm/queryPatientTree.action',
				method:'get',
				animate:true,
				lines:true,
				onDblClick:function(node){
					if(node.id=='root'){
						$('#inpatNo').textbox('setValue',"");
					}else{
						$('#inpatNo').textbox('setValue',node.id);
					}
					searchPatient();
				}
			});
			$.extend($.fn.validatebox.defaults.rules, {    
				valiConfirmNum: {    
					validator: function(value, param){
						var id = +$.trim($(this).closest("td[field='confirmNum']").siblings("td[field='id']").children('div').html());
						if(id!=null&&id!=''){
							var qty = +$.trim($(this).closest("td[field='confirmNum']").siblings("td[field='totalNum']").children('div').html());
							var confirmNum = +$.trim($(this).closest("td[field='confirmNum']").siblings("td[field='alreadyNum']").children('div').html());
							var r = (+$.trim(value))>(qty-confirmNum);
							return (!r);   
						}else{
							return true;
						}
					},
					message: '执行数量不能大于项目数量！'   
				}
			});
			$('#list').datagrid({
				url:'<%=basePath%>technical/tecconfirm/queryTecconfirmList.action',
				queryParams:{inpatNo:null},
				pagination:false,
				columns:[[
				          {field:'ck',checkbox:true},
				          {field:'id',title:'id',width:70,hidden:true},
				          {field:'exeDept',title:'执行科室',width:100},
				          {field:'code',title:'项目编码',width:100},
				          {field:'name',title:'项目名称',width:180,editor:{type:'combogrid',options:{
				        	  url:'<%=basePath%>technical/tecconfirm/queryUnDrugList.action',    
				        	  mode:'remote',
				        	  idField:'name',
				        	  textField:'name',
				        	  panelWidth:500,
				        	  pagination:true,
				        	  required:true,
							  pageSize:20,
							  pageList:[20,30,50,80,100],
				              columns:[[    
				                  {field:'name',title:'名称',width:150},    
				                  {field:'undrugPinyin',title:'拼音码',width:100},
				                  {field:'undrugWb',title:'五笔码',width:100},
				                  {field:'undrugInputcode',title:'自定义码',width:100},
				              ]],
				              onClickRow:function(rowIndex, rowData){
				            	  var index = $('#list').datagrid('getRowIndex',$('#list').datagrid('getSelected'));
				            	  $('#list').datagrid('updateRow',{index:index,row:{code:rowData.id,name:rowData.name,price:rowData.defaultprice}});
				            	  $('#list').datagrid('endEdit',index);
				            	  $('#list').datagrid('beginEdit',index);
				              }
				          }}},
				          {field:'doc',title:'开立医生',width:100},
				          {field:'totalNum',title:'总数量',width:100},
				          {field:'alreadyNum',title:'已确认数量',width:100},
				          {field:'confirmNum',title:'确认数量',width:100,editor:{type:'numberbox',options:{required:true,validType:'valiConfirmNum',min:0}}},
				          {field:'price',title:'单价',width:100},
				          {field:'totalMoney',title:'总额',width:100},
				          {field:'resource',title:'资源',width:100},
				          {field:'state',title:'状态',width:100},
				          {field:'device',title:'对应设备',width:100,editor:{type:'textbox',options:{required:true}}},
				          {field:'exeUser',title:'技师',width:100,editor:{type:'textbox',options:{required:true}}},
				]],
				onClickRow:function(){
					var rows = $(this).datagrid('getRows');
					for(var i=0;i<rows.length;i++){
						$(this).datagrid('endEdit',i);
						var b = $(this).datagrid('validateRow',i);
						if(!b){
							$(this).datagrid('selectRow',i);
							return false;
						}
					}
				},
				onDblClickRow:function(rowIndex, rowData){
					var rows = $(this).datagrid('getRows');
					for(var i=0;i<rows.length;i++){
						$(this).datagrid('endEdit',i);
						var b = $(this).datagrid('validateRow',i);
						if(!b){
							$(this).datagrid('selectRow',i);
							return false;
						}
					}
					$(this).datagrid('beginEdit',rowIndex);
					if(rowData.id!=null&&rowData.id!=''){
						var ed = $(this).datagrid('getEditor',{index:rowIndex,field:'name'});
						if(ed!=null){
							$(ed.target).combogrid('readonly',true);
						}
					}
					
				}
			});
			bindEnterEvent('inpatNo',searchPatient,'easyui');//回车
		});
		
		/**  
		 *  
		 * 回车事件，查询患者信息
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function searchPatient(){
			var inpatNo = $('#inpatNo').textbox('getValue');
			clearPatientInfo();
			$.ajax({
				url:'<%=basePath%>technical/tecconfirm/queryPatient.action',
				data:{inpatNo:inpatNo},
				type:'post',
				success: function(date) {
					if(date.length!=null&&date.length!=''){
						if(date.length==1){
							if(date[0].pname!=null&&date[0].pname!=''){
								$('#inpatNoHid').val(inpatNo);
								$('#pname').text(date[0].pname);
								$('#sex').text(sexMap.get(date[0].sex));
								$('#age').text(date[0].age);
								$('#pact').text(date[0].pact);
								$('#dept').text(date[0].dept);
								$('#ward').text(date[0].ward);
								$('#bed').text(date[0].bed);
								$('#doctor').text(date[0].doctor);
								$('#balance').text(date[0].balance);
							}
						}
						$('#list').datagrid('load',{inpatNo:inpatNo});
					}else{
						$.messager.alert("提示", "该住院号信息不存在！");
					}
				}
			});
		}
		
		/**  
		 *  
		 * 添加方法
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function add(){
			var inpatNo = $('#inpatNoHid').val();
			if(inpatNo!=null&&inpatNo!=''){
				var rows = $('#list').datagrid('getRows');
				for(var i=0;i<rows.length;i++){
					$('#list').datagrid('endEdit',i);
					var b = $('#list').datagrid('validateRow',i);
					if(!b){
						$('#list').datagrid('selectRow',i);
						return false;
					}
				}
				var lastIndex = $('#list').datagrid('appendRow',{exeDept:$('#deptName').val(),doc:$('#userName').val()}).datagrid('getRows').length-1;
				$('#list').datagrid('beginEdit',lastIndex);
				$('#list').datagrid('selectRow',lastIndex);
			}else{
				$.messager.alert("提示", "请选择患者信息！");
			}
		}
		
		/**  
		 *  
		 * 清除患者信息
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function clearPatientInfo(){
			$('#inpatNoHid').val("");
			$('#pname').text("");
			$('#sex').text("");
			$('#age').text("");
			$('#pact').text("");
			$('#dept').text("");
			$('#ward').text("");
			$('#bed').text("");
			$('#doctor').text("");
			$('#balance').text("");
		}
		
		/**  
		 *  
		 * 删除添加的非药品信息
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function del(){
			var rows = $('#list').datagrid('getChecked');
			if(rows.length>0){
				for(var i=0;i<rows.length;i++){
					if(rows[i].id==null||rows[i].id==''){
						var index = $('#list').datagrid("getRowIndex",rows[i]);
						$('#list').datagrid("deleteRow",index);
					}
				}
			}else{
				$.messager.alert("提示", "请选择要删除的信息！");
			}
			
		}
		
		/**  
		 *  
		 * 取消编辑状态
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function cancel(){
			var row = $('#list').datagrid('getSelected');
			if(row!=null){
				$('#list').datagrid('cancelEdit',$('#list').datagrid('getRowIndex',row));
			}else{
				$.messager.alert("提示", "请选择要取消编辑的信息！");
			}
		}
		
		/**  
		 *  
		 * 保存
		 * @Author：aizhonghua
		 * @CreateDate：2016-4-28 下午06:56:59  
		 * @Modifier：aizhonghua
		 * @ModifyDate：2016-4-28 下午06:56:59  
		 * @ModifyRmk：  
		 * @version 1.0
		 *
		 */
		function save(){
			var rows = $('#list').datagrid('getChecked');
			if(rows.length>0){
				var inpatNo = $('#inpatNoHid').val();
				for(var i=0;i<rows.length;i++){
					var index = $('#list').datagrid("getRowIndex",rows[i]);
					$('#list').datagrid('endEdit',index);
					$('#list').datagrid('cancelEdit',index);
				}
				var result = true;
				for(var i=0;i<rows.length;i++){
					 var index = $('#list').datagrid("getRowIndex",rows[i]);
					 $('#list').datagrid('beginEdit',index);
					 var validResult = $('#list').datagrid('validateRow',index)
					 if(validResult){
						 $('#list').datagrid('endEdit',index);
					 }else{
						 result = false;
						 $('#list').datagrid('selectRow',index);
						 return false;
					 }
					 result = validResult&&result;
				}
				if(result){
					$.post('<%=basePath%>technical/tecconfirm/saveTecconfirmList.action',{"inpatNo":inpatNo,"dataArr":$.toJSON(rows)},function(dataMap){
						if(dataMap.resCode=="success"){
							$.messager.alert("提示",dataMap.resMsg);
							$('#inpatNo').textbox('setValue',"");
							clearPatientInfo();
							$('#tt').tree('reload');
							$('#list').datagrid('reload',{inpatNo:null});
						}else if(dataMap.resCode=="arrearage"){
							$.messager.defaults={
				    				ok:'继续',
				    				cancel:'取消',
				    				width:250,
				    				collapsible:false,
				    				minimizable:false,
				    				maximizable:false,
				    				closable:false
				    		};
							$.messager.confirm('提示信息',dataMap.resMsg+"，"+"本次需要费用"+dataMap.totCost+"元！是否继续？", function(r){
								if (r){
									$.post('<%=basePath%>technical/tecconfirm/saveTecconfirmList.action',{"inpatNo":inpatNo,"dataArr":$.toJSON(rows),"goon":1},function(dataMap){
										$.messager.alert("提示",dataMap.resMsg);
										if(dataMap.resCode=="success"){
											$('#inpatNo').textbox('setValue',"");
											clearPatientInfo();
											$('#tt').tree('reload');
											$('#list').datagrid('reload',{inpatNo:null});
										}else{
											$.messager.alert("提示",dataMap.resMsg);
										}
									});
								}
							});
						}else{
							$.messager.alert("提示",dataMap.resMsg);
						}
					});
				}
			}else{
				$.messager.alert("提示", "请选择要保存的信息！");
			}
		}
		</script>
	</body>
</html>