<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@ include file="/common/metas.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<body>
		<div class="easyui-panel" id = "panelEast" data-options="title:'',iconCls:'icon-form'" style="width:490px;height:100%;border:0px;">
			<div class="easyui-panel" data-options="region:'center',split:false" style="width:100%;border-top:0">
				<div id="divLayout" class="easyui-layout" fit=true style="width: 100%;">
					<div style="padding:5px;">
						<a href="javascript:void(0)" onclick="submit()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:false">保存</a>	
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:false"" onclick="closeLayout()">关闭</a>
				    </div>
				    <form id="editForm" method="post" border="false" >
						<input type="hidden" name="medicalGroupJson" id="medicalGroupJson"/>
						<input  type="hidden" id="medicalgroupId" name="medicalgroup.id"  value="${medicalgroup.id }" readonly>
						<table class="honry-table" cellpadding="0" cellspacing="0" border="true"  style="width:100%;border-right:none;border-left:none;border-bottom:none">
							<tr>
								<td class="honry-lable" style="border-left:0">
									<span>科室:</span>&nbsp;&nbsp;
								</td>
								<td style="text-align: left;border-right:0">
									<input id="deptCodeHidden"    type="hidden" value="${medicalgroup.deptId}"  style="width:50%" missingMessage="请选择所属科室">
									<input id="deptId" type="hidden" value="${deptId}" style="width:50%"/>
									<input id="deptNames"  type="hidden" style="width:50%"/>
									<input id="deptCode" name="medicalgroup.deptId" value="${medicalgroup.deptId}" type="hidden"  style="width: 50%"/>
			    	
			    					<input id="deptCodeText" type="text"  style="width: 50%"/>
									<a href="javascript:delSelectedData('deptCodeText');"  class="easyui-linkbutton" 
									data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
							 </tr>
						  	 <tr>
								<td class="honry-lable" style="border-left:0">
									<span>名称:</span>&nbsp;&nbsp;
								</td>
								<td style="text-align: left;border-right:0"> 
									<input class="easyui-textbox" id="name" name="medicalgroup.name" value="${medicalgroup.name}" data-options="required:true" missingMessage="请填写名称" style="width:50%">							
								</td>
							 </tr>
							 <tr>
								<td class="honry-lable" style="border-left:0">
									<span>自定义码:</span>&nbsp;&nbsp;
								</td>
								<td style="text-align: left;border-right:0">
									<input id="inputCode"  name="medicalgroup.inputCode"  class="easyui-textbox" value="${medicalgroup.inputCode}" style="width:50%" >
								</td>
							 </tr>
							 <tr>
								<td class="honry-lable">
									<span>所属院区:</span>&nbsp;&nbsp;
								</td>
								<td class="honry-info">
									<input id="areaCode" class="easyui-combobox" 
										name="medicalgroup.areaCode"
									 value="${medicalgroup.areaCode }" 
									 data-options="required:true" 
									 missingMessage="请选择所属院区"" style="width: 50%"/>
								</td>
							</tr>
							 <tr>
								<td class="honry-lable" style="border-left:0;border-bottom:0;">
									<span>备注:</span>&nbsp;&nbsp;
								</td>
								<td style="text-align: left;border-right:0;border-bottom:0;">
									<input id="remark"  name="medicalgroup.remark" value="${medicalgroup.remark}" class="easyui-textbox"style="width:50%" >
								</td>
							 </tr>
						 </table>
					 </form>
				 </div>
			</div>
			<div class="easyui-panel" data-options="region:'south',split:false,fit:true"style="width: 100%;border-top:0;">
				<input type="hidden" id="inpId">
				<table id="medicalInfos" style="width: 100%;" border="false" data-options="singleSelect:false,toolbar:'#infos',onBeginEdit:function(index,row){
																													$('#inpId').val(index);
																													var ed = $('#medicalInfos').datagrid('getEditor', {index:index,field:'isOwnDept'});
																													var d = $('#medicalInfos').datagrid('getEditor', {index:index,field:'doctor'});
																													var v = $(ed.target).combobox('getValue');
																													var doctorName= $(d.target).combogrid('getValue');
																													var deptRange = null;
																													if(v=='是'){
																														deptRange = 1;
																													}else if(v=='否'){
																														deptRange = 0;
																													}
																													var ed = $('#medicalInfos').datagrid('getEditor', {index:$('#inpId').val(),field:'doctor'});
																													$(ed.target).combogrid({
																														disabled : false,
																														rownumbers : true,//显示序号 
																														pagination : true,//是否显示分页栏
																														striped : true,//数据背景颜色交替
																														panelWidth : 400,//容器宽度
																														fitColumns : true,//自适应列宽
																														pageSize : 5,//每页显示的记录条数，默认为10  
																														pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
																														mode : 'remote',//数据表格将从远程服务器加载数据
																														url : '<%=basePath%>baseinfo/medicalGroup/getEmpByDept.action',
																														idField : 'name',
																														textField : 'name',
																														columns : [ [{
																															field : 'id',
																															title : '员工id',
																															hidden:true
																														},{
																															field : 'name',
																															title : '名字',
																															width : 200
																														},{
																															field : 'jobNo',
																															title : '工作号',
																															width : 200
																														}, {
																															field : 'deptId',
																															title : '部门',
																															width : 200,
																															formatter: function(value,row,index){
																																			if (row.deptId){
																																				return row.deptId.deptName;
																																			} else {
																																				return value;
																																			}																															}
																															
																														}, {
																															field : 'post',
																															title : '职务',
																															width : 200,
																															formatter: dutiesFamater
																														} ] ],
																														onBeforeLoad:function (param) {
																														$.ajax({
																																url: '<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>',
																											                    data:{'type' : 'duties'},
																											                    type:'post',
																											                    success: function(dutiesData) {
																												                dutiesList = dutiesData;
																											                       }
																										                    });
																										                    
																															if(param.dept==null&&param.deptRange==null){
																															
																															param.dept=$('#deptCode').val();
																															var sign=row.isOwnDept;
																															if(sign=='是'){
																															param.deptRange=1;
																															}else{
																															param.deptRange=0;
																																}
																															}
																												        }
																													});
																													$('#medicalInfos').datagrid('beginEdit',index); 
																													if(doctorName!=null&&doctorName!=''){
																														$(d.target).combogrid('setValue',doctorName)
																													}
																												}">
					<thead>
						<tr>
							<th data-options="field:'ck',checkbox:true, width : '5%'" ></th>
							<th data-options="field:'id', width : '25%',hidden:true">医疗组子表id</th>
							<th data-options="field:'doctorId', width : '25%',editor:{type:'textbox'},hidden:true">医生编号</th>
							<th data-options="field:'isOwnDept', width : '35%',
															editor:{type:'combobox',
																	options:{
																		disabled:true,//将编辑禁用 
																		required : true,
																		valueField:'id',
																		textField:'value',
																		editable:false,
																		data: [{id: '是',value: '是'},{id: '否',value: '否'}],
																		onSelect:function(record){
																			if(record.id=='是'){
																				val = 1;
																			}else if(record.id=='否'){
																				val = 0;
																			}
																			var tr = $(this).parent('td').parent('tr').parent('tbody').parent('table').parent('div').parent('td').parent('tr');
																			var ind = $(tr).attr('id').split('-')[4];
																			var ed = $('#medicalInfos').datagrid('getEditor', {index:ind,field:'doctor'});
																			$(ed.target).combogrid('grid').datagrid('load',{dept:$('#deptCode').val(),deptRange:val});
																		 }
																	  }
																	}" >现科室员工</th>
							<th data-options="field:'doctor', width : '20%',editor:{type:'combogrid',
																					options:{
																						onClickRow : function(rowIndex, rowData) {
																							var depted = $('#medicalInfos').datagrid('getEditor', {index:$('#inpId').val(),field:'doctorId'});
																							$(depted.target).textbox('setValue',rowData.id);
																						},
																					},
																					
																			}"  >医生姓名</th>
							<th data-options="field:'remark', width : '25%',editor:{type:'textbox'}" >备注</th>
							
						</tr>
					</thead>
				</table>
				<div id="infos">
					<a href="javascript:void(0)" onclick="addInfo()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">添加医生</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除医生</a>
				</div>
			</div> 
		</div>
		<script type="text/javascript">
		 var popWinDeptCallBackFn = null;
			var val = null;
			var deptRange="";//判断添加员工是住院部门下的还仅是自己选定科室下的员工 （1代表住院部下员工  ;0代表选择科室下员工）
			var dutiesList=null;
			$(function(){
				$('#deptCodeText').textbox({
					readonly:true,
					required:true,
					missingMessage:"请选择工作科室",
					value:(function(){
						var deptNames=$("#deptNames").val();
 						$('#deptCodeText').textbox('setValue',deptNames);
					}),
					
				});
				if($('#medicalgroupId').val()!='' && $('#medicalgroupId').val()!=null){
					var groupId=$('#medicalgroupId').val();
					$('#medicalInfos').edatagrid({
						url:"<c:url value='/baseinfo/medicalGroup/findGroupInfoList.action'/>?groupId="+groupId
					});
				}else{
					$('#deptCode').val($('#deptId').val());
					$('#medicalInfos').edatagrid({});
					
				}
				
			});
			//添加医生明细
			function addInfo(){
				var index=$('#medicalInfos').edatagrid('appendRow',{}).datagrid('getRows').length - 1;
				$('#medicalInfos').datagrid('beginEdit', index);
				var ed = $('#medicalInfos').edatagrid('getEditor', {  
				    index : index,  
				    field : 'isOwnDept'  
				    }); 
				$(ed.target).combobox({disabled:false});//combobox为editor对应的type 
			}
			//提交验证 
			function submit(){
				$('#medicalInfos').edatagrid('acceptChanges');
				 $('#medicalGroupJson').val(JSON.stringify( $('#medicalInfos').edatagrid("getRows")));
				var rowsCount=$('#medicalInfos').datagrid('getRows');
				var count=rowsCount.length;
				for(var i=0;i<count;i++){
					if(!rowsCount[i].doctorId){
						if(rowsCount[i].doctor){
							$.messager.alert('提示信息','医生信息'+rowsCount[i].doctor+'错误,请下拉选中');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}else{
							$.messager.alert('提示信息','医生信息错误,请填写必要的医生信息:医生姓名');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
						}
						return false;
					}
					for(var j=i;j<count-1;j++){
						if(rowsCount[i].doctor==rowsCount[j+1].doctor){
							$.messager.alert('提示信息',rowsCount[i].doctor+'医生信息重复,请双击选择修改');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							return false;
						}
					}
				}
				 $('#editForm').form('submit',{
			        url:"<c:url value='/baseinfo/medicalGroup/saveOrUpdateMedicalGroup.action'/>",  
			        onSubmit:function(){ 
			            return $(this).form('validate');  
			        },  
			        success:function(data){  
			        	$('#divLayout').layout('remove','east');
						$("#medicalGroup").datagrid("reload");
			        },
					error : function(data) {
						$.messager.alert('提示信息','保存失败！');
					}			         
				 }); 
			}
			function endEditing(){
				var index = $('#medicalInfos').edatagrid("getRows").length;
				for (var i=0;i<index;i++){
				   $('#medicalInfos').edatagrid('acceptChanges');
				   $('#medicalInfos').edatagrid('beginEdit', i);
			       var dataGridRowData = $('#medicalInfos').edatagrid('getRows')[i];
			       var isOwnDept = dataGridRowData.isOwnDept;
			       if(!isOwnDept||isOwnDept==null){
			       	  var indexNum = i+1;
			       	$.messager.alert("提示信息","第"+indexNum+"行请选择");
			       	setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				      return false;
			       }
				}
				return true;
			}
			//子表删除
			function removeit() {
	            var rows = $('#medicalInfos').datagrid('getChecked');
	          	if (rows.length > 0) {//选中几行的话触发事件	                        
			 		$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
						if (res){
							var ids = '';
							var ind;
							for(var i=0; i<rows.length; i++){
								if(rows[i].id){
									ids += ',';
									ids += rows[i].id;
								}
								ind=$('#medicalInfos').datagrid('getRowIndex',rows[i]);
								$('#medicalInfos').datagrid('deleteRow',ind)
								
							};
							if(''!=ids){
							$.post("<%=basePath%>baseinfo/medicalGroup/delMedicalInfo.action?ids="+ids+"&deleteFalg=1",function(data){
								 var retVal =data;
								  if(retVal=="no"){
									  $.messager.alert('提示信息','删除失败！');
								  }else if(retVal=="yes"){
									  $.messager.alert('提示信息','操作成功！');
									  $('#medicalInfos').datagrid('reload');
								  }else{
									  $.messager.alert('提示信息','连接错误，操作失败!');
								  }
							});
							}
						}
	              	});
	          	}	
			}
			//显示职位格式化
			function dutiesFamater(value){
				if(value!=null){
					for(var i=0;i<dutiesList.length;i++){
						if(value==dutiesList[i].encode){
							return dutiesList[i].name;
						}
					}	
				}
			}
			 
			
			bindEnterEvent('deptCodeText',popWinToDept,'easyui');//绑定回车事件
			/**
			* 回车弹出科室选择窗口
			* @author  zhuxiaolu
			* @param deptIsforregister 是否是挂号科室 1是 0否
			* @param textId 页面上commbox的的id
			* @date 2016-03-22 14:30   
			* @version 1.0
			*/

			function popWinToDept(){
					popWinDeptCallBackFn = function(node){
			    	$("#deptCode").val(node.id);
					$('#deptCodeText').textbox('setValue',node.deptName);
				};
				var tempWinPath = "<%=basePath%>popWin/popWinDepartment/toDepartmentPopWin.action?deptType=I&textId=reportDept";
				window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+(screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=yes')
			}
			
			$('#areaCode').combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=hospitalArea",
				valueField : 'encode',
				textField : 'name',
				multiple : false,
			});
		</script>
		<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
	</body>
</html>	