<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'west',collapsed:true,split:true,title:'患者',tools:'#toolId'" style="width:180;tetx-align:center;">
		    	<ul id="patientTree"></ul> 
		    	<div id="toolId">
					<a href="javascript:void(0)" onclick="refPatientTree()" class="icon-reload"></a>
				</div>
		    </div>   
		    <div data-options="region:'center'">
			    <div class="easyui-layout" data-options="fit:true">   
				    <div data-options="region:'north',title:'病床维护',collapsible:false" style="height:35%;">
				    	<table id="list" style="width:200px" >   
						</table> 
				    </div>   
				    <div data-options="region:'center',fit:true" style="letter-spacing:5px;width:100%">
				    	<div style="padding:10px"><b>床位使用率</b></div>
	    				<form id="editForm" method="post">
					    	<table class="honry-table" style="cellpadding:'0';width:100%; cellspacing:'0'; border:'0px'; align:'center' ">
								<tr>
									<td style="background:#fafafa">床　　号:</td>
					    			<td><input id="bedName" name="bedName"></td>
									<td style="background:#fafafa">床位状态:</td>
					    			<td>
					    				<input id="bedState" name="bedState">  
									</td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">房间号　:</td>
					    			<td><input id="businessBedward"/></td>
									<td style="background:#fafafa">是否在用:</td>
					    			<td >
					    				 <select id="isUse" class="easyui-combobox" name="isUse" style="width:152px" >
					    				 <option> </option>
								  <option>是</option>   
                                  <option>否</option>   
                                </select> 
									</td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">床位等级:</td>
									<td>
					    				<input id="bedLevel" name="bedLevel"/>  
									</td>
									<td style="background:#fafafa">床位编制:</td>
									<td>
					    				<input id="bedOrgan" name="bedOrgan" />  
									</td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">床位电话:</td>
					    			<td><input id="bedPhone" class="easyui-textbox"/></td>
									<td style="background:#fafafa">护理组　:</td>
					    			<td><input id="nursestation" class="easyui-textbox"/></td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">住院医生:</td>
									<td>
					    				<input id="houseDocCode" name="" class="easyui-textbox" />  
									</td >
									<td style="background:#fafafa">主治医师:</td>
									<td>
					    				<input id="chargeDocCode" name="" class="easyui-textbox" />  
									</td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">主任医生:</td>
									<td>
					    				<input id="chiefDocCode" name="" class="easyui-textbox" />  
									</td>
									<td style="background:#fafafa">责任护士:</td>
									<td>
					    				<input id="dutyNurseCode" name="" class="easyui-textbox" />  
									</td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">归　　属:</td>
					    			<td><input id="bedBelong" class="easyui-textbox"/></td>
									<td style="background:#fafafa">排　　序:</td>
					    			<td><input id="bedwardOrder" class="easyui-textbox"/></td>
				    			</tr>
								<tr>
									<td style="background:#fafafa">床位医生:</td>
									<td>
					    				<input id="bedYiS" name=""  class="easyui-textbox" />  
									</td>
				    			</tr>
					    	</table>	
					    </form>
					    <div style="text-align:center;padding:5px">
					    	<shiro:hasPermission name="${menuAlias }:function:add">
					    		<a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:add();">添加</a>
					   		</shiro:hasPermission>
					   		<shiro:hasPermission name="${menuAlias }:function:delete">
					    		<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:del();">删除</a>
					    	</shiro:hasPermission>
					   		<shiro:hasPermission name="${menuAlias }:function:save">
					    		<a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:save();">保存</a>
					    	</shiro:hasPermission>
					    </div>
				    </div>   
				</div> 
		    	
		    </div>   
	</div>
	<script type="text/javascript">
	$(function(){
		//床号
		$('#bedName').combobox({    
		    valueField:'id',    
		    textField:'bedName',
		    url:'<%=basePath%>baseinfo/hospitalbed/querybedNameByBedward.action',
		    method:'get',multiple:false,editable:false
		}); 
		//床房
		$('#businessBedward').combobox({    
		    valueField:'id',    
		    textField:'bedwardName',
		    url:'<%=basePath%>baseinfo/hospitalbed/querybusinessBedward.action',
		    method:'get',multiple:false,editable:false,
		    onSelect:function(record){
		    	$('#bedName').combobox('reload','<%=basePath %>baseinfo/hospitalbed/querybedNameByBedward.action?id='+record.id);
		    }
		});
		//床位状态
		$('#bedState').combobox({    
		    valueField:'id',    
		    textField:'name',
		    url:'<%=basePath%>comboBox.action?str='+"CodeBedStatus",    
		    method:'get',multiple:false,editable:false
		});
		//床位等级
		$('#bedLevel').combobox({    
		    valueField:'id',    
		    textField:'name',
		    url:'<%=basePath%>comboBox.action?str='+"CodeBedGrade",    
		    method:'get',multiple:false,editable:false
		});
		//床位编制
		$('#bedOrgan').combobox({    
		    valueField:'id',    
		    textField:'name',
		    url:'<%=basePath%>comboBox.action?str='+"CodeBedPreparation",    
		    method:'get',multiple:false,editable:false
		});
		
		//显示病床信息
	    $("#list").datagrid({
	    	  url:'<%=basePath %>baseinfo/hospitalbed/querybedNameByBedward.action', 
	    	  singleSelect:true,
	    	  rownumbers:true,
	    	  method:'post',
	    	  fit:true,
	    	  fitColumns:true,
	    	  pageSize:5,
	    	  columns:[[    
	    	        {field:'bedName',title:'床号',width:"11%",align:"center"},    
	    	        {field:'nursestation',title:'护理组',width:"11%",align:"center"},    
	    	        {field:'bedLevel',title:'床位等级',width:"11%",align:"center"},
	    	        {field:'bedFee',title:'床位费',width:"11%",align:"center"},    
	    	        {field:'bedOrgan',title:'床位编制',width:"11%",align:"center"},    
	    	        {field:'bedState',title:'状态',width:"11%",align:"center"},    
	    	        {field:'patientId',title:'住院号',width:"11%",align:"center"},  
	    	        {field:'bedPhone',title:'病床电话',width:"11%",align:"center"},    
	    	        {field:'isUse',title:'是否在用',width:"11%",align:"center"}
	    	    ]]   
	    });
		 $('#list').datagrid({
			onDblClickRow:function(rowIndex, rowData){
				$('#bedName').combobox('setValue',rowData.bedName);//床号
				
				$('#businessBedward').combobox('setValue',rowData.businessBedward.bedwardName);//房间号
			
				var row = $('#list').datagrid('getSelected');
				var d=row.bedState;
				if(d==1){
					//床位状态
					$('#bedState').combobox('setValue',"在用");
					
					$('#isUse').combobox('setValue',"是");
					}//是否在用
				else{
					$('#bedState').combobox('setValue',"空床");
					$('#isUse').combobox('setValue',"否");//是否在用
				}
				
				 var c=row.bedLevel;
				 if(c==1){
					//床位等级
					 $('#bedLevel').combobox('setValue',"VIP");
				 }  
				 else if(c==2){
						//床位等级
						 $('#bedLevel').combobox('setValue',"单人间");
					 } 
				 else if(c==3){
						//床位等级
						 $('#bedLevel').combobox('setValue',"双人间");
					 } 
				 else if(c==4){
						//床位等级
						 $('#bedLevel').combobox('setValue',"三人间");
					 } 
				 else if(c==5){
						//床位等级
						 $('#bedLevel').combobox('setValue',"四人间");
					 } 
				 else if(c==6){
						//床位等级
						 $('#bedLevel').combobox('setValue',"加床");
					 } 
				 else{
					 $('#bedLevel').combobox('setValue'," ");
				 }
				
				$('#bedOrgan').combobox('setValue',rowData.bedOrgan);//床位编制
				$('#bedPhone').textbox('setValue',rowData.bedPhone);//床位电话
				$('#nursestation').textbox('setValue',rowData.nurseCellCode);//护理组
				$('#houseDocCode').textbox('setValue',rowData.houseDocCode);//住院医生
				$('#chargeDocCode').textbox('setValue',rowData.chargeDocCode);//主治医师
				$('#chiefDocCode').textbox('setValue',rowData.chiefDocCode);//主任医生
				$('#dutyNurseCode').textbox('setValue',rowData.dutyNurseCode);//责任护士
				$('#bedBelong').textbox('setValue',rowData.bedBelong);//归属
				$('#bedwardOrder').textbox('setValue',rowData.bedOrder);//排序
			}
		}); 
	});
	
	//添加
	function add(){
		var node = $('#patientTree').tree('getSelected');
		if(node!=null&&node!=""){
			var rows = $('#list').treegrid('getRows');
			if(rows!=null&&rows.length>0){
				$('#list').datagrid('updateRow',{
					index: 0,
					row: {
						bedName: $('#bedName').combobox('getText'),//床号
						nursestation: $('#nursestation').val(),
						bedLevel: $('#bedLevel').combobox('getText'),
						bedFee: 0.00,
						bedOrgan: $('#bedOrgan').combobox('getText'),
						bedState: $('#bedState').combobox('getText'),
						//patientId: '',
						bedPhone: $('#bedPhone').val()
						//isUse: ''
					}
				});
			}else{
				$('#list').datagrid('appendRow',{
					bedName: $('#bedName').combobox('getText'),//床号
					nursestation: $('#nursestation').val(),
					bedLevel: $('#bedLevel').combobox('getText'),
					bedFee: 0.00,
					bedOrgan: $('#bedOrgan').combobox('getText'),
					bedState: $('#bedState').combobox('getText'),
					//patientId: '',
					bedPhone: $('#bedPhone').val()
					//isUse: ''
				});
			}
		}else{
			$.messager.alert('提示','请选择患者!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	//删除
	function del(){
		var rows = $('#list').treegrid('getChecked');
    	if (rows.length > 0) {//选中几行的话触发事件	                        
		 	$.messager.confirm('确认', '确定要删除选中信息吗?', function(res){//提示是否删除
				if (res){
					var ids = '';
					for(var i=0; i<rows.length; i++){
						if(rows[i].id!=null){
							ids += ',';
							ids += rows[i].id;
						}else{
							var dd = $('#list').datagrid('getRowIndex',rows[i]);//获得行索引
							$('#list').datagrid('deleteRow',0);
						}
					};
					if(ids!=''){
						$.ajax({
							url: 'delBedinfo.action?id='+ids,
							type:'post',
							success: function() {
								$.messager.alert('提示','删除成功!');
								var node = $('#patientTree').tree('getSelected');
								$('#list').datagrid({url:'<%=basePath%>nursestation/nurse/queryBedinfoList.action?id='+node.id}); 
							}
						});
					}
				}
        	});
    	}else{
    		$.messager.alert('提示','请选择要删除的信息！');
    		setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
    	}
	}
	//保存
	function save(){
		var rows = $('#list').datagrid('getRows');
		if(rows!=null&&rows.length>0){
			if(confirm("确定保存修改信息?")){
		        $.post('<%=basePath %>baseinfo/hospitalbed/saveBedinfo.action',{"jsonData":$.toJSON(rows)},function(result){
		   			if(result=="success"){
		   				$.messager.alert('提示','操作成功!');
		   				reloadFeed();
		   			}else if(result=="authority"){
		   				$.messager.alert('提示','当前用户无此权限,请联系管理员!');
		   				setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
		   				reloadFeed();
		   			}else{
		   				$.messager.alert('提示','操作失败!');
		   			}
		   		});
	        }else{
	        	return;
	        }
		}else{
			$.messager.alert('提示','请添加病床信息!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	
	//患者树
	$('#patientTree').tree({    
		url:'<%=basePath%>inpatient/info/queryPatientTree.action',
		lines:true,
		onBeforeCollapse:function(node){ 
		  	if(node.id=="1"){
				return false;
		  	}
		},
		onClick:function(node){
			clear();
			if(node.id!=null&&node.id!="1"){
				$('#list').datagrid({url:'<%=basePath%>nursestation/nurse/queryBedinfoList.action?id='+node.id}); 
			}
		}
	}); 
	
	//患者树功能
	function refPatientTree(){//刷新树
		$('#patientTree').tree('reload'); 
	}
	
	//清除页面填写信息
	function clear(){
		$('#editForm').form('reset');
	}
	
	//刷新
	function reloadFeed(){
	    var tab = self.parent.$('#tabs').tabs('getSelected');  // 获取选择的面板
	    self.parent.$('#tabs').tabs('update', {
	    	tab: tab,
	    	options: {
	    		title: '病床维护',
	    		href: '<%=basePath%>outpatient/listFeedetail.action'  // 新内容的URL
	    	}
	    });
	}
	</script>
</body>
</html>