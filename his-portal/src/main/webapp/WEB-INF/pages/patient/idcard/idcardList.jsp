<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript">
	var sexList = "";
	var countryList = "";	
	var certiList = "";
	var nationList = "";
	var marryList = "";
	var occuList = "";	
	var relationList = "";
	var idcardTypeList = "";
	var sexMap=new Map();
		//加载页面
	$(function(){
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
		//解析编码xml文件
		idCombobox("idcardTypeSerc","CodeIdcardType");
		
		var id='${id}'; //存储数据ID
		//添加datagrid事件及分页
		$('#list').datagrid({
			pagination:true,
			pageSize:20,
			pageList:[20,30,50,80,100],
			onBeforeLoad:function (param) {
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=country",
					type:'post',
					success: function(countrydata) {
						countryList =  countrydata;
					}
				});
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=certificate",
					type:'post',
					success: function(certidata) {
						certiList =  certidata ;
					}
				});
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=nationality",
					data:{"str" : "CodeNationality"},
					type:'post',
					success: function(nationdata) {
						nationList =  nationdata ;
					}
				});
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=marry",
					data:{"str" : "CodeMarry"},
					type:'post',
					success: function(marrydata) {
						marryList =  marrydata ;
					}
				});
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=occupation",
					data:{"str" : "CodeOccupation"},
					type:'post',
					success: function(occudata) {
						occuList =  occudata ;
					}
				});
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=relation",
					type:'post',
					success: function(relationdata) {
						relationList =  relationdata ;
					}
				});
				$.ajax({
					url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=idcardType",
					type:'post',
					success: function(idcardTypedata) {
						idcardTypeList =  idcardTypedata ;
					}
				});
	        },
			onLoadSuccess: function (data) {//默认选中
	            var rowData = data.rows;
	            $.each(rowData, function (index, value) {
	            	if(value.id == id){
	            		$('#list').datagrid('checkRow', index);
	            	}
	            });
	        }                
			,onDblClickRow: function (rowIndex, rowData) {//双击查看
					var row = $('#list').datagrid('getSelected');
					var idcardId = "";
					if(row){
	                      	if(row.idcardId!=null&&row.idcardId!=""){
							idcardId = row.idcardId;
						}else{
							idcardId = "";
						}
						AddOrShowEast('查看',"<c:url value='/patient/idcard/viewIdcard.action'/>?id="+idcardId+"&pid="+row.id);
			   		}
					                        
	                      
				}    
			});
		});
		
		//从xml文件中解析，读到下拉框
		function idCombobox(id,name) {
			$('#' + id).combobox({
				url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=idcardType",
				valueField : 'encode',
				textField : 'name',
				multiple : false
			});
		}
		
	
		function add(){
			AddOrShowEast('添加',"<c:url value='/patient/idcard/addIdcard.action'/>");
		}
		
		function edit(){
			var row = $('#list').datagrid('getSelected'); //获取当前选中行          
			var idcardId = "";
			if(row){
	            if(row.idcardId!=null&&row.idcardId!=""){
					idcardId = row.idcardId;
				}else{
					idcardId = "";
				}
				AddOrShowEast('编辑',"<c:url value='/patient/idcard/editInfoIdcard.action'/>?id="+idcardId+"&pid="+row.id);
	   		}else{
	   			$.messager.alert("操作提示","请选择一条记录进行编辑","warning");  
	   		}    
		}
		
		function del(){
			 //选中要删除的行
	                   var rows = $('#list').datagrid('getChecked');
	                  	if (rows.length > 0) {//选中几行的话触发事件	                        
					 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
							if (res){
								var ids = '';
								var idcardIds = '';
								for(var i=0; i<rows.length; i++){
									if(ids!=''){
										ids += ',';
									}
									ids += rows[i].id;
									if(idcardIds!=''){
										idcardIds += ',';
									}
									idcardIds += rows[i].idcardIds;
								};
								$.ajax({
									url: "<c:url value='/patient/patinent/delPatient.action'/>?id="+ids+"&idcardId="+idcardIds,
									type:'post',
									success: function() {
										$.messager.alert('提示','删除成功');
										$('#list').datagrid('reload');
									},error:function(){
										$.messager.alert('提示',"删除失败！");
									}
								});
							}
	                      });
	                  }
		}
		
		function reload(){
			//实现刷新栏目中的数据
			$('#list').datagrid('reload');
		}
	
		/**
		 * 查询
		 * @author  lt
		 * @date 2015-06-18
		 * @version 1.0
		 */
		function searchFrom() {
			var idcardTypeSerc = $('#idcardTypeSerc').combobox('getValue');
			var encode = $('#encode').val();
			$('#list').datagrid('load', {
				idcardType : idcardTypeSerc,
				patientName : encode	
			});
		}
	/**
		 * 动态添加标签页
		 * @author  lt
		 * @date 2015-06-19
		 * @version 1.0
		 */
		function AddOrShowEast(title, url) {
			var eastpanel=$('#panelEast'); //获取右侧收缩面板
			if(eastpanel.length>0){ //判断右侧收缩面板是否存在
				//重新装载右侧面板
		   		$('#divLayout').layout('panel','east').panel({
	                         href:url 
	                  });
			}else{//打开新面板
				$('#divLayout').layout('add', {
					region : 'east',
					width : 800,
					split : true,
					href : url,
					closable : true
				});
			}
		}
		
		/**
		 * 回车键查询
		 * @author  lt
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
		 * @date 2015-06-29
		 * @version 1.0
		 */
		function sexFamater(value,row,index){
			if(value!=null&&value!=''){
				return sexMap.get(value);
			}
		}
		/** 关系列表页 显示
		 * @author  lt
		 * @date 2015-06-29
		 * @version 1.0
		 */
		function relationFamater(value,row,index){
			if(value!=null){
				for(var i=0;i<relationList.length;i++){
					if(value==relationList[i].id){
						return relationList[i].name;
					}
				}
			}	
		}
		/** 卡类型列表页 显示
		 * @author  lt
		 * @date 2015-06-29
		 * @version 1.0
		 */
		function idcardTypeFamater(value,row,index){
			if(row!=null){
				for(var i=0;i<idcardTypeList.length;i++){
					if(row.idcardType==idcardTypeList[i].id){
						return idcardTypeList[i].name;
					}
				}
			}	
		}
		//退卡
		function backCard(){
			var row = $('#list').datagrid('getSelected');  //获取当前选中行          
			var idcardId = ""; 
	          	if(row){
	          		if(row.idcardId!=null&&row.idcardId!=""){
	          			idcardId = row.idcardId;
	          		}else{
	          			idcardId = "";
	          		}
	           	if(idcardId!=null&&idcardId!=""){
	           		$.ajax({
						url: "<c:url value='/checkAllAccount/checkAllAccount.action'/>?medicalId="+row.medicalrecordId,//验证就诊卡是否有门诊，住院账户及门诊，住院账户余额是否为0，及是否出院
						type:'post',
						success: function(checkBack) {
						var dataObj = eval("("+checkBack+")");
							if(dataObj!="yes"){
								if(dataObj=="state"){
									$.messager.alert("操作提示","未办理出院登记业务，不予退卡，请先办理出院登记业务！","warning"); 
								}else{
									$.ajax({
										url: "<c:url value='/patient/idcard/checkIsBackCard.action'/>",//根据参数表设置查询账户有余额的情况下是否可以退卡
										type:'post',
										success: function(data) {
											if(data=="1"){
												$.messager.confirm('确认',dataObj + '，可办理退卡业务，确定执行退卡操作？', function(res){
													if (res){
														$.ajax({
															url: "<c:url value='/patient/account/totalAccount.action'/>?idcardId="+row.idcardId,// 执行结清门诊账户操作
															type:'post',
															success: function(isTotal) {
																$.ajax({
																	url: "<c:url value='/inpatient/account/totalInpatientAccount.action'/>?medicalId="+row.medicalrecordId,// 执行结清住院账户操作
																	type:'post',
																	success: function(data) {
																		$.ajax({
																			url: "<c:url value='/patient/idcard/backIdcard.action'/>?idcardId="+row.idcardId+"&medicalId="+row.medicalrecordId,// 执行退卡销户操作
																			type:'post',
																			success: function(data) {
																				if(data=="yes"){
																					$('#list').datagrid('reload');
																					$.messager.alert("操作提示","退卡成功！","success");
																				}else{
																					$.messager.alert("操作提示","退卡失败！","error");
																				}
																			}
																		});	
																	}
																});	
																
															}
														});	
													}
												});
											}
											if(data=="2"){
												$.messager.alert("操作提示",dataObj+"，不能办理退卡业务，需先办理欠费或预交金结清等工作！","warning");
											}	
										}
									});
								}
								
							}else{
								$.messager.confirm('确认', '此患者已办理出出院登记及卡内账户已结清，可以办理退卡业务，确定执行退卡操作？', function(res){
									if (res){
										$.ajax({
										url: "<c:url value='/patient/idcard/backIdcard.action'/>?idcardId="+row.idcardId,
										type:'post',
										success: function(data) {
											if(data=="yes"){
												$('#list').datagrid('reload');
												$.messager.alert("操作提示","退卡成功！","success");
											}else{
												$.messager.alert("操作提示","退卡失败！","error");
											}
										}
										});	
									}
								});
							}
						}
					});	
	           	}else{
	           		$.messager.alert("操作提示","此患者没有就诊卡，不能办理退卡业务！","warning");   
	           	}
	   		}else{
	   			$.messager.alert("操作提示","请选择一条记录进行操作！","warning");   
	   		}
		}
	//挂失
	function lossCard(){
		var row = $('#list').datagrid('getSelected');  //获取当前选中行          
		var idcardId = ""; 
       	if(row){
       		if(row.idcardId!=null&&row.idcardId!=""){
       			idcardId = row.idcardId;
       		}else{
       			idcardId = "";
       		}
        	if(idcardId!=null&&idcardId!=""){
        		$.messager.confirm('操作提示', '确定执行挂失操作？', function(res){
					if (res){
						$.ajax({
						url: "<c:url value='/patient/idcard/lossIdcard.action'/>?idcardId="+row.idcardId,
						type:'post',
						success: function(data) {
							if(data=="yes"){
								$('#list').datagrid('reload');
								$.messager.alert("操作提示","挂失成功！","success");
							}else{
								$.messager.alert("操作提示","挂失失败！","error");
							}
						}
						});	
					}
				});
       		}else{
       		$.messager.alert("操作提示","此患者没有就诊卡，不能办理挂失业务！","warning");   
       	}
   		}else{
   			$.messager.alert("操作提示","请选择一条记录进行操作！","warning");   
   		}
	}
	//补卡
	function fillCard() {
		var row = $('#list').datagrid('getSelected');  //获取当前选中行   
		var idcardId = "";     
		if(row){
			if(row.idcardId!=null&&row.idcardId!=""){
       			idcardId = row.idcardId;
       		}else{
       			idcardId = "";
       		}  
			AddOrShowEast('补办就诊卡',"<c:url value='/patient/idcard/fillInfoIdcard.action'/>?id="+idcardId+"&pid="+row.id+"&random="+new Date().getTime());
       	}
	}
	//激活
	function activateCard(){
		var row = $('#list').datagrid('getSelected');  //获取当前选中行   
		var idcardId = "";     
		if(row){
       		if(row.idcardId!=null&&row.idcardId!=""){
       			idcardId = row.idcardId;
       		}else{
       			idcardId = "";
       		}
        	if(idcardId!=null&&idcardId!=""){
        		$.ajax({
					url: "<c:url value='/patient/idcard/checkIdcard.action'/>?idcardId="+row.idcardId,
					type:'post',
					success: function(data) {
						if(data=="yes"){
							$.messager.confirm('操作提示', '确定执行激活操作？', function(res){
							if (res){
								$.ajax({
									url: "<c:url value='/patient/idcard/activateIdcard.action'/>?idcardId="+row.idcardId,
									type:'post',
									success: function(data) {
										if(data=="yes"){
											$('#list').datagrid('reload');
											$.messager.alert("操作提示","激活成功！","success");
										}else{
											$.messager.alert("操作提示","激活失败！","error");
										}
									}
									});	
								}
							});
						}else{
							$.messager.alert("操作提示","此卡没有挂失，无需激活！","error");
						}
					}
				});
       		}else{
       		$.messager.alert("操作提示","此患者没有就诊卡，不能办理激活业务！","warning");   
       	}
   		}else{
   			$.messager.alert("操作提示","请选择一条记录进行操作！","warning");   
   		}
	} 
</script>
</head>
<body>
	<div id="divLayout" class="easyui-layout" fit=true
		style="width: 100%; height: 100%; overflow-y: auto;">
		<div style="padding: 5px 5px 0px 5px;">
			<table style="width: 100%; border: 1px solid #95b8e7; padding: 5px;">
				<tr>
					<td  nowrap="nowrap">
						关键字：
						<input class="easyui-textbox" type="text" ID="encode" data-options="prompt:'患者姓名,医保手册号,卡号,病历号'" style="width: 250px;"
							onkeydown="KeyDown()" />&nbsp;&nbsp;
						卡类型：
						<input ID="idcardTypeSerc" onkeydown="KeyDown()" />
						<!--<shiro:hasPermission name="JZKGL:function:query"> -->
						&nbsp;&nbsp;
						<a href="javascript:void(0)" onclick="searchFrom()"
							class="easyui-linkbutton" iconCls="icon-search">查询</a>
						<!--</shiro:hasPermission>-->
					</td>
				</tr>
			</table>
		</div>
		<div style="padding: 0px 5px 5px 5px;">
			<table id="list" style="width: 100%;"
				data-options="url:'${pageContext.request.contextPath}/patient/patinent/listPatient.action',rownumbers:true,striped:true,border:true,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,toolbar:'#toolbarId'">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true,width:'8%'"></th>
						<th
							data-options="field:'patientName',width:'6%'">
							患者名称
						</th>
						<th
							data-options="field:'patientHandbook',width:'8%'">
							医保手册号
						</th>
						<th
							data-options="field:'patientSex',formatter:sexFamater,width:'5%'">
							性别
						</th>
						<th
							data-options="field:'patientPhone' ,width:'8%'">
							电话
						</th>
						<th
							data-options="field:'patientEmail',width:'10%'">
							电子邮箱
						</th>
						<th
							data-options="field:'patientLinkman',width:'8%'">
							联系人
						</th>
						<th
							data-options="field:'patientLinkrelation',formatter:relationFamater,width:'8%'">
							联系人关系
						</th>
						<th data-options="field:'medicalrecordId',width:'10%'">
							病历号
						</th>
						<th data-options="field:'idcardNo',width:'10%'">
							卡号
						</th>
						<th data-options="field:'idcardCreatetime',width:'8%'">
							建卡时间
						</th>
						<th
							data-options="field:'idcardType',formatter:idcardTypeFamater,width:'7%'">
							卡类型
						</th>
						<th data-options="field:'idcardOperator',width:'7%'">
							操作人员
						</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<div id="toolbarId">
		<shiro:hasPermission name="${menuAlias}:function:add">
			<a href="javascript:void(0)" onclick="add()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-add',plain:true">添加</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:edit">
			<a href="javascript:void(0)" onclick="edit()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-edit',plain:true">修改</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:delete">
			<a href="javascript:void(0)" onclick="del()"
				class="easyui-linkbutton"
				data-options="iconCls:'icon-remove',plain:true">删除</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:backCard">
		<a href="javascript:void(0)" onclick="backCard()"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-remove',plain:true">退卡</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:lossCard">
		<a href="javascript:void(0)" onclick="lossCard()"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-02',plain:true">挂失</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:activateCard">
		<a href="javascript:void(0)" onclick="activateCard()"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-2012080412263',plain:true">激活</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="${menuAlias}:function:fillCard">	
		<a href="javascript:void(0)" onclick="fillCard()"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-20130406014311476_easyicon_net_16',plain:true">补卡</a>
		</shiro:hasPermission>
	</div>
	</body>
</html>