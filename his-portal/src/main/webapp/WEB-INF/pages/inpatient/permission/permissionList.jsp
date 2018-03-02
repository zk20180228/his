<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>医嘱授权管理</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript">
	var mid="";
	var dept="";
	var deptMap="";
	var empMap="";
	var mainId="";
	var inpatientNo="";
	var payMap=new Map();
	$(function(){
		
		//回车事件
		bindEnterEvent('medicalreco',queryBymed,'easyui');
		$('#listlist').datagrid({
			onDblClickRow: function(rowIndex, rowData) {
				$('#mainId').val(rowData.id);
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
			},onBeforeLoad:function(param){
				$('#listlist').datagrid('clearChecked');
				$('#listlist').datagrid('clearSelections');
			}
		});
		$("#listlist").datagrid('loadData', { total: 0, rows: [] });
		//渲染表单中的挂号科室
		$.ajax({
			url: '<%=basePath%>inpatient/permission/queryDept.action',
			success: function(deptData){
				deptMap = deptData;
			}
		});
		//渲染表单中的挂号专家
		$.ajax({
			url: "<%=basePath%>inpatient/permission/queryUser.action",
			success: function(empData) {
				empMap = empData;
			}
		});
		$.ajax({
			url:  basePath+"/baseinfo/pubCodeMaintain/queryDictionary.action?type=paykind",
			async:false,
			success: function(data) {
				var payType = data;
				for(var i=0;i<payType.length;i++){
					payMap.put(payType[i].encode,payType[i].name);
				}
			}
		});
	});
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡）
	function read_card_ic(){
		var card_value = app.read_ic();
		if(card_value=='0'||card_value==undefined||card_value==''){
			$.messager.alert('提示','此卡号['+card_value+']无效');
			return;
		}
		$.ajax({
			url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
			data:{idcardOrRe:card_value},
			type:'post',
			async:false,
			success: function(data) { 
				if(data==null||data==''){
					$.messager.alert('提示','此卡号无效');
					return;
				}
				$('#medicalreco').textbox('setValue',data);
				queryBymed();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证）
		function read_card_sfz(){
			var card_value = app.read_sfz();
			if(card_value=='0'||card_value==undefined||card_value==''){
				$.messager.alert('提示','此卡号['+card_value+']无效');
				return;
			}
			$.ajax({
				url: "<%=basePath%>inpatient/info/queryMedicalrecordId.action",
				data:{idcardOrRe:card_value},
				type:'post',
				async:false,
				success: function(data) {
					if(data==null||data==''){
						$.messager.alert('提示','此卡号无效');
						return;
					}
					$('#medicalreco').textbox('setValue',data);
					queryBymed();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	
	function queryBymed(){
		mid=$('#medicalreco').val();
		clear();
		if(mid == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		else{
			$.messager.progress({text:'查询中，请稍后...',modal:true});
			$.ajax({
				url:"<%=basePath%>inpatient/permission/queryFormByIdM.action?mid="+mid,
				success:function(datalist){
					$.messager.progress('close');
					if(datalist.length==0){
						$.messager.alert('提示','病历号输入有误，请重新输入');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						$("#diaInpatient").window('close');
						return false;
					}else if(datalist.length==1){
						//判断住院状态是不是I
						if('I'==datalist[0].inState){
							if("${deptId}"==datalist[0].deptCode){
								$('#patientName').text(datalist[0].patientName);
								$('#deptCode').text(deptMap[datalist[0].deptCode]);
								$('#paykindCode').text(payMap.get(datalist[0].paykindCode));
								$('#freeCost').text(datalist[0].freeCost);
								$('#inpatientNo').val(datalist[0].inpatientNo);
								$('#medicalreco').textbox('setValue',datalist[0].medicalrecordId);
								qwer(datalist[0].inpatientNo);
							}else{
								$.messager.alert('提示','该患者不在当前登录科室下，无法进行授权');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
					    		$("#diaInpatient").window('close');
					    		clear();
							}
						}else{
							$.messager.alert('提示','该患者不是病房接诊状态，无法进行授权');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
				    		$("#diaInpatient").window('close');
						}
					}else if(datalist.length>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							data:datalist,
						    columns:[[    
						        {field:'inpatientNo',title:'住院流水号',width:'20%',align:'center'} ,    
						        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
						        {field:'reportSexName',title:'性别',width:'10%',align:'center'} ,
						        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
						        {field:'certificatesNo',title:'身份证号',width:'35%',align:'center'} 
						    ]] ,
						    onDblClickRow:function(rowIndex, rowData){
						    	if("${deptId}"==rowData.deptCode){	
									$('#medicalreco').textbox('setValue',rowData.medicalrecordId);
							    	$('#patientName').text(rowData.patientName);
									$('#deptCode').text(deptMap[rowData.deptCode]);
									$('#paykindCode').text(payMap.get(rowData.paykindCode));
									$('#freeCost').text(rowData.freeCost);
									inpatientNo=datalist[0].inpatientNo;
									$('#inpatientNo').val(datalist[0].inpatientNo);
									qwer(inpatientNo);
									$("#diaInpatient").window('close');
						    	}else{
									$.messager.alert('提示','该患者不在当前登录科室下，无法进行授权');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
						    	}
						    }
						});
					}
				}
			});
		}
	}
	function queryBymedicalrecordId(){
		mid=$('#medicalrecordId').val();
		clear();
		if(mid == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		else{
			$.messager.progress({text:'查询中，请稍后...',modal:true});
			$.ajax({
				url:"<%=basePath%>inpatient/permission/queryFormById.action?mid="+mid,
				success:function(datalist){
					$.messager.progress('close');
					if(datalist.length==0){
						$.messager.alert('提示','病历号输入有误，请重新输入');
						setTimeout(function(){
							$(".messager-body").window('close');
						},3500);
						$("#diaInpatient").window('close');
						return false;
					}else if(datalist.length==1){
						if("${deptId}"==datalist[0].deptCode){
							$('#patientName').text(datalist[0].patientName);
							$('#deptCode').text(deptMap[datalist[0].deptCode]);
							$('#paykindCode').text(payMap.get(datalist[0].paykindCode));
							$('#freeCost').text(datalist[0].freeCost);
							$('#inpatientNo').val(datalist[0].inpatientNo);
							$('#medicalrecordId').textbox('setValue',datalist[0].idcardNo);
							$('#medicalreco').textbox('setValue',datalist[0].medicalrecordId);
							qwer(datalist[0].inpatientNo);
						}else{
							$.messager.alert('提示','该患者不在当前登录科室下，无法进行授权');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
				    		$("#diaInpatient").window('close');
				    		clear();
						}
					}else if(datalist.length>1){
						$("#diaInpatient").window('open');
						$("#infoDatagrid").datagrid({
							data:datalist,
						    columns:[[    
						        {field:'inpatientNo',title:'住院流水号',width:'20%',align:'center'} ,    
						        {field:'medicalrecordId',title:'病历号',width:'20%',align:'center'} ,  
						        {field:'reportSexName',title:'性别',width:'10%',align:'center'} ,
						        {field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
						        {field:'certificatesNo',title:'身份证号',width:'35%',align:'center'} 
						    ]] ,
						    onDblClickRow:function(rowIndex, rowData){
						    	if("${deptId}"==rowData.deptCode){	
									$('#medicalreco').textbox('setValue',rowData.medicalrecordId);
							    	$('#patientName').text(rowData.patientName);
									$('#deptCode').text(deptMap[rowData.deptCode]);
									$('#paykindCode').text(payMap.get(rowData.paykindCode));
									$('#freeCost').text(rowData.freeCost);
									inpatientNo=datalist[0].inpatientNo;
									$('#inpatientNo').val(datalist[0].inpatientNo);
									qwer(inpatientNo);
									$("#diaInpatient").window('close');
						    	}else{
									$.messager.alert('提示','该患者不在当前登录科室下，无法进行授权');
									setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
						    	}
						    }
						});
					}
				}
			});
		}
	}
	function qwer(inpatientNo){
		$('#listlist').datagrid({
			url:"<%=basePath%>inpatient/permission/queryListById.action?menuAlias=${menuAlias}&inno="+inpatientNo
		});
	}
	//渲染科室		
	function functionDept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	//渲染结算科室		
	function functionPay(value,row,index){
		if(value!=null&&value!=''){
			return payMap[value];
		}
	}
	//渲染医生
	function functionEmp(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	function Adddilog(title,url){
		$('#panel').dialog({    
		    title: title,    
		    width: '60%',    
		    height:'55%',    
		    closed: false,    
		    cache: false,    
		    href: url,    
		    modal: true
		   });  
	}
	//关闭dialog
	function closeDialog() {
		$('#panel').dialog('close');  
	}
	function add(){
		var inno=$('#inpatientNo').val();
		if(inno!=null&&inno!=""){
			Adddilog("医嘱授权记录添加","<%=basePath %>inpatient/permission/listAddUpdate.action?inpatientNo="+inno);
		}else{
			$.messager.alert('提示','请查询要添加授权记录的病人');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	function clear(){
		$('#listlist').datagrid('loadData',{ total:0,rows:[]});
		$('#patientName').text('');
		$('#deptCode').text('');
		$('#paykindCode').text('');
		$('#freeCost').text('');
		$('#inpatientNo').val('');
		$('#mainId').val('');
		$('#medicalreco').textbox('setValue','');
	}
	function del(){
		//得到所有被选中的行
		var rows = $('#listlist').datagrid('getChecked');
		//所有被选择的行的id所拼接的字符串
		var ids='';
		for(var i=0;i<rows.length;i++){
			if(ids!=''){
				ids+=',';
			}
			ids += rows[i].id;
		}
		if(ids!=null && ids!=""){
			$.messager.confirm('提示',"你确定要删除选中信息吗",function(r){
		if(r){
				$.messager.progress({text:'删除中，请稍后...',modal:true});
				$.ajax({
					url:"<%=basePath%>inpatient/permission/delById.action",
					data:{"ids":ids},
					success:function(date){
						$.messager.progress('close');
						if(date.resCode=='success'){
							$.messager.alert('提示','删除成功');
							setTimeout(function(){
								$(".messager-body").window('close');
							},3500);
							queryBymed();
						}else{
							$.messager.alert('提示','删除失败');
						}
					},
					error:function(){
						$.messager.progress('close');
						$.messager.alert('提示','删除失败');
					}
				});
		}else{
			             return;
		}
		});
		}else{
			$.messager.alert('提示','请选择要删除的记录')
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
	function edit(){
		var row=$('#listlist').datagrid('getSelected');
		if(row==null||row==""){
			$.messager.alert('提示','请选择要进行修改的记录')
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
		var mid=row.id;
		if(mid!=null&&mid!=""){
			Adddilog("医嘱授权修改","<%=basePath%>inpatient/permission/listAddUpdate.action?manId="+mid);
		}else{
			$.messager.alert('提示','请选择要进行修改的记录')
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}
	}
</script>
</head>
<body>
	<div class="easyui-layout" fit="true">
		<div style="height:35px;margin-top: 3px">
					<input type="hidden" id="mainId" />
					<input type="hidden" id="inpatientNo" />
						<span style="margin-left: 5px">病历号：</span>
						<input class="easyui-textbox" id="medicalreco" name="medicalrecordId" data-options="prompt:'输入病历号回车查询'" />
						<shiro:hasPermission name="${menuAlias}:function:query">
							<a href="javascript:void(0)" onclick="queryBymed()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${menuAlias}:function:readCard">
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
						</shiro:hasPermission>
			        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
			        		<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						</shiro:hasPermission>
						<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</div>
				<div style="height:30px;margin-bottom: 3px;">
					 <span style="margin-left: 5px">姓名：</span>
					<span id="patientName" style="width: 80px">&nbsp;</span>
					<span style="margin-left: 40px">住院科室：</span>
					<span  id="deptCode" style="width: 80px">&nbsp;</span>
					<span style="margin-left: 10px">余额：</span>
					<span  id="freeCost" style="width: 80px">&nbsp;</span>
					<span style="margin-left: 30px">结算方式：</span>
					<span id="paykindCode" style="width:80px">&nbsp;</span>												
				</div>
		<table id="listlist" class="easyui-datagrid" style="width:100%;" data-options="fit:true,rownumbers:true,striped:true,checkOnSelect:true,selectOnCheck:false,border:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,40,60,80,100],singleSelect:true,toolbar:'#toolbarId'">
			<thead>
				<tr>
					<th data-options="field:'checkname',checkbox:'true'"></th>
					<th data-options="field:'id',hidden:'true'"></th>
					<th data-options="field:'deptCode',width:10,align:'center',formatter:functionDept">授权科室</th>
					<th data-options="field:'docCode',width:10,align:'center',formatter:functionEmp">授权医师</th>  
					<th data-options="field:'moStdt',width:15,align:'center'">处方起始日</th>  
					<th data-options="field:'moEddt',width:15,align:'center  '">处方结束日</th>  
					<th data-options="field:'operDate',width:15,align:'center'">授权时间</th>  
					<th data-options="field:'operCode',width:10,align:'center',formatter:functionEmp">授权操作者</th>  
					<th data-options="field:'remark',width:25,align:'center'">备注</th>  
				</tr>
			</thead>
		</table>
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
	</div>
	<div id="panel"></div>

    <div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:650;height:600;padding:10" data-options="modal:true, closed:true">   
   		<table id="infoDatagrid"  data-options="fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
		</table>  
	</div>
</body>
</html>