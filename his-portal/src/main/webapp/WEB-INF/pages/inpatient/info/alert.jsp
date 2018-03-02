<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<jsp:include page="/javascript/default.jsp"></jsp:include>
<body>
	<div id="divLayout" class="easyui-layout" fit=true>
		<div
			data-options="region:'center',split:false,title:'住院信息列表',iconCls:'icon-book'"
			style="padding: 10px;">
			<table id="infolist" data-options="url:'inpatient/info/queryAllInpatientInfoAlert.action?menuAlias=${menuAlias}',method:'post',rownumbers:true,idField: 'id',striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'inpatientNo'">
							住院流水号
						</th>
						<th data-options="field:'medicalType',formatter:medicalFamater">
							医疗类别
						</th>
						<th data-options="field:'medicalrecordId'">
							病历号
						</th>
						<th data-options="field:'idcardNo'">
							就诊卡号
						</th>
						<th data-options="field:'mcardNo'">
							医疗证号
						</th>
						<th data-options="field:'patientName'">
							姓名
						</th>
						<th data-options="field:'certificatesType',formatter:certiFamater">
							证件类型
						</th>
						<th data-options="field:'certificatesNo'">
							证件号码
						</th>
						<th data-options="field:'reportSex',formatter:sexFamater">
							性别
						</th>
						<th data-options="field:'reportBirthday'">
							出生日期
						</th>
						<th data-options="field:'prepayCost'">
							预约金额
						</th>
						<th data-options="field:'moneyAlert'">
							警戒线
						</th>
						<th data-options="field:'alterBegin'">
							警戒线开始时间
						</th>
						<th data-options="field:'alterEnd'">
							警戒线结束时间
						</th>
						<th data-options="field:'bloodDress'">
							血压
						</th>
						<th data-options="field:'bloodCode',formatter:bloodFamater">
							血型编码
						</th>
						<!--<th data-options="field:'hepatitisFlag'">
							重大疾病标志
						</th>
						<th data-options="field:'anaphyFlag'">
							过敏标志
						</th>
						--><th data-options="field:'deptCode'">
							科室代码
						</th>
						<th data-options="field:'bedName'">
							床号
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="add"></div>
	<div id="toolbarId">
	<shiro:hasPermission name="${menuAlias}:function:edit">
		<a href="javascript:void(0)" onclick="edit()" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${menuAlias}:function:delete">		
		<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
	</shiro:hasPermission>
		<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
	</div>
	<script type="text/javascript">
		var sexList = "";
		var medicalList = "";	
		var certiList = "";
		var bloodList = "";
		var bedList = "";
		//加载页面
			$(function(){
			   var winH=$("body").height();
				//$('#p').height(winH-78-30-27-2);//78:页面顶部logo的高度，30:菜单栏的高度，27：tab栏的高度，
				$('#infolist').height(winH-78-30-27-26);
				var id='${id}'; //存储数据ID
				//添加datagrid事件及分页
				$('#infolist').datagrid({
					pagination:true,
					pageSize:20,
					pageList:[20,30,50,80,100],
					onBeforeLoad:function (param) {
			            $.ajax({
							url: 'likeSex.action',
							type:'post',
							success: function(sexdata) {
								sexList = eval("("+ sexdata +")");
							}
						});
						$.ajax({
							url: 'likeMedicaltype.action',
							type:'post',
							success: function(medidata) {
								medicalList = eval("("+ medidata +")");
							}
						});
						$.ajax({
							url: 'outpatient/changeDeptLog/likeCertificate.action',
							type:'post',
							success: function(certidata) {
								certiList = eval("("+ certidata +")");
							}
						});
						$.ajax({
							url: 'likeBloodtype.action',
							type:'post',
							success: function(blooddata) {
								bloodList = eval("("+ blooddata +")");
							}
						});
						
			        },
					onLoadSuccess: function (data) {//默认选中
			            var rowData = data.rows;
			            $.each(rowData, function (index, value) {
			            	if(value.id == id){
			            		$('#infolist').datagrid('checkRow', index);
			            	}
			            });
			        }                
					,onDblClickRow: function (rowIndex, rowData) {//双击查看
							var row = $('#infolist').datagrid('getSelected');	                        
	                        if(row){
	                        	Adddilog('EditForm','inpatient/info/viewInpatientAlert.action?id='+row.id);
					   		}
						}    
					});
					$('#infolist').datagrid({   
                         rowStyler:function(index,row){   
                         if (row.remarkalert==1){   
                           return 'background-color:red;';   
                        }   
                       }   
                     });
				});
				
				
				function edit(){
					var row = $('#infolist').datagrid('getSelected'); //获取当前选中行                        
                    if(row){
                       	// AddOrShowEast('编辑','editInitInpatientInfo.action?id='+row.id);
                       	Adddilog("编辑",'inpatient/info/editInitInpatientAlert.action?id='+row.id);
			   		}
				}
				
				function del(){
					 //选中要删除的行
		                    var rows = $('#infolist').datagrid('getChecked');
	                    	if (rows.length > 0) {//选中几行的话触发事件	                        
							 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
									if (res){
										var ids = '';
										for(var i=0; i<rows.length; i++){
											if(ids!=''){
												ids += ',';
											}
											ids += rows[i].id;
										};
										$.ajax({
											url: 'inpatient/info/delInpatientInfo.action?id='+ids,
											type:'post',
											success: function() {
												$.messager.alert('提示','删除成功');
												$('#infolist').datagrid('reload');
											}
										});
									}
	                        });
	                    }
				}
				
				function reload(){
					//实现刷新栏目中的数据
					$('#infolist').datagrid('reload');
				}
	
	
				/**
				 * 格式化复选框
				 * @author  lt
				 * @date 2015-6-19 9:25
				 * @version 1.0
				 */
				function formatCheckBox(val,row){
					if (val == 1){
						return '是';
					} else {
						return '否';
					}
				}				
			
				/**
				 * 查询
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-19
				 * @version 1.0
				 */
				function searchFrom() {
					var inpatientNoSerc = $('#inpatientNoSerc').val();
					var idcardNoSerc = $('#idcardNoSerc').val();
					var bedIdSerc = $('#bedIdSerc').val();
					$('#infolist').datagrid('load', {
						inpatientNo : inpatientNoSerc,
						idcardNo : idcardNoSerc,
						bedId : bedIdSerc
					});
				}
				
				/**
				 * 添加弹出框
				 * @author  lt
				 * @param title 标签名称
				 * @param url 跳转路径
				 * @date 2015-06-25
				 * @version 1.0
				 */
				function Adddilog(title, url) {
					$('#add').dialog({    
					    title: title,    
					    width: '70%',    
					    height:'90%',    
					    closed: false,    
					    cache: false,    
					    href: url,    
					    modal: true   
					   });    
				}
				//打开dialog
				function openDialog() {
					$('#add').dialog('open'); 
				}
				//关闭dialog
				function closeDialog() {
					$('#add').dialog('close');  
				}
				
				/**
				 * 回车键查询
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-19
				 * @version 1.0
				 */
				function KeyDown()  
				{  
				    if (event.keyCode == 13)  
				    {  
				        event.returnValue=false;  
				        event.cancel = true;  
				        searchFrom();  
				    }  
				} 
				/**
				 * 性别列表页 显示
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-29
				 * @version 1.0
				 */
				function sexFamater(value,row,index){
					for(var i=0;i<sexList.length;i++){
						if(value==sexList[i].id){
							return sexList[i].name;
						}
					}	
				}
				/**
				 * 医疗类别列表页 显示
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-29
				 * @version 1.0
				 */
				function medicalFamater(value,row,index){
					for(var i=0;i<medicalList.length;i++){
						if(value==medicalList[i].id){
							return medicalList[i].name;
						}
					}	
				}
							/**
				 * 证件类型列表页 显示
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-29
				 * @version 1.0
				 */
				function certiFamater(value,row,index){
					for(var i=0;i<certiList.length;i++){
						if(value==certiList[i].id){
							return certiList[i].name;
						}
					}	
				}
				/** 血型列表页 显示
				 * @author  lt
				 * @param title 标签名称
				 * @param title 跳转路径
				 * @date 2015-06-29
				 * @version 1.0
				 */
				function bloodFamater(value,row,index){
					for(var i=0;i<bloodList.length;i++){
						if(value==bloodList[i].id){
							return bloodList[i].name;
						}
					}	
				}
			
		</script>
</body>
</html>