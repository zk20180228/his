<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>住院预约</title>
<%@ include file="/common/metas.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=basePath%>easyui1.4.5/custom_css/from_base.css">
<script type="text/javascript">
	var contractunitMap = '';
	var idCardMap=null;
	var cityMap="";
	var empMap = null;
	var flag = false;
	var sexMap=new Map();

	var deptBqId="";
	
	//列表
	$(function(){
		//病历号框的回车查询功能
		bindEnterEvent('medId',searchFrom,'easyui');
		bindEnterEvent('medicalrecordIds',query,'easyui');
		$('#predoctName').combogrid({});
		$('#nurseCellCode2').combobox({disabled:true});
		$('#bedId').combobox({disabled:true});
		$('#predoctName').combogrid({disabled:true});
		$('#sexCode').combobox({
			url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
		    valueField:'encode',    
		    textField:'name',
		    editable:true
		});
		$('#list').datagrid({
			onBeforeLoad:function(){
				$('#list').datagrid('clearChecked');
				$('#list').datagrid('clearSelections');
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
			}
		});
		
	});	
	
	/*******************************开始读卡***********************************************/
	//定义一个事件（读卡） 已预约患者
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
				$('#medId').textbox('setValue',data);
				searchFrom();
			}
		});
	};
	
	//定义一个事件（读卡） 患者基本信息
	function read_baseCard_ic(){
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
				$('#medicalrecordIds').textbox('setValue',data);
				query();
			}
		});
	};
	/*******************************结束读卡***********************************************/
	/*******************************开始读身份证***********************************************/
		//定义一个事件（读身份证） 已预约患者
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
					$('#medicalrecordIds').textbox('setValue',data);
					query();
				}
			});
		};
		//定义一个事件（读身份证） 患者基本信息
		function read_baseCard_sfz(){
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
					$('#medId').textbox('setValue',data);
					searchFrom();
				}
			});
		};
	/*******************************结束读身份证***********************************************/
	//删除
	function del(){
	 //选中要删除的行
		var rows = $('#list').datagrid('getChecked');
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
					$.messager.progress({text:'保存中，请稍后...',modal:true});
					$.ajax({
						url: '<%=basePath %>inpatient/prepayin/delPrepayin.action?ids='+ids,
						type:'post',
						success: function(date) {
							$.messager.progress('close');
							if(date.resCode=='success'){
								$.messager.alert('提示','取消成功!');
								setTimeout(function(){
									$(".messager-body").window('close');
								},3500);
								//取消选择行  
								$('#list').datagrid('clearSelections');  
								$('#list').datagrid('reload');
							}else{
								$.messager.alert('提示','取消失败!');
							}
							
						}
					});
				}
			});
		}else{
			$.messager.alert('提示','请至少选择一条要删除的记录!');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return;
		}
	}
	//清除
	function clear(){
		$('#nurseCellCode2').combobox({disabled:true});
		$('#bedId').combobox({disabled:true});
		$('#predoctName').combogrid({disabled:true});
		$('#editForm').form('reset');
	}
	//显示数据
	function edit(data){
		if(data.medicalrecordId==null){
			$.messager.alert('提示',"无该患者，请您核对病历号！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else if(data.medicalrecordId=="1"){
			$.messager.alert('提示',"该患者已经住院，不能再次进行住院预约！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else if(data.medicalrecordId=="2"){
			$.messager.alert('提示',"该患者没有开立住院证，请先开立住院证再预约！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else if(data.medicalrecordId=="3"){
			$.messager.alert('提示',"该患者已经住院预约，不能再次进行住院预约！");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
		}else{
			//就诊卡号
				$("#nurseCellCode2").combobox({disabled:false});
				$('#bedId').combobox({disabled:false});
				$('#predoctName').combogrid({disabled:false});

				$("#diaInpatient").window('close');			
				$("#cardNo").textbox("setValue",data.idcardNo);
				$("#medicalrecordId").val(data.medicalrecordId);
				//入院科室
				$("#deptCode2").textbox("setValue",data.reportDeptName);
				$("#deptCode").val(data.reportDept);
				var dept=$("#deptCode").val();
				//护士站
				$("#nurseCellCode2").combobox({
					url: '<%=basePath %>inpatient/prepayin/queryNurInfo.action?reportDept='+dept,
				    valueField:'deptCode',    
				    textField:'deptName',
				})

				$("#nurseCellCode2").combobox('setValue',data.reportBedward);
				$("#nurseCellCode").val($("#nurseCellCode2").combobox("getValue"));
				//姓名
				$("#name").textbox("setValue",data.patientName);
				//性别
				$("#sexCode").combobox("setValue",data.reportSex);	
				//出生日期
				$("#birthday").val(data.reportBirthday.split(' ')[0]);
				//籍贯
				$("#dist").textbox("setValue",data.dist);
				//出生地
				$("#birthArea").textbox("setValue",cityMap[data.birthArea]);
				$("#birthArea1").val(data.birthArea);
				//身份证号
				var type =data.certificatesType;
				if(type!='3'){
					$("#idenNo").textbox("setValue",null);
					$("#idenNo1").val(data.certificatesNo);
				}else{
					$("#idenNo").textbox("setValue",data.certificatesNo);
					$("#idenNo1").val(data.certificatesNo);
				}
				//合同单位
				$("#pactCode2").textbox("setValue",contractunitMap[data.pactCode]);
				$("#pactCode").val(data.pactCode);
				//入院诊断
				$("#diagCode").textbox("setValue",data.reportDiagnose);
				//家庭所在县
				$("#homeDistrict2").textbox("setValue",cityMap[data.patientCity]);
				$("#homeDistrict").val(data.patientCity);
				//家庭住址
				$("#home").textbox("setValue",data.home);
				//家庭电话
				$("#homeTel").textbox("setValue",data.homeTel);
				//工作单位
				$("#workName").textbox("setValue",data.workName);
				//单位电话
				$("#workTel").textbox("setValue",data.workTel);
				//联系人
				$("#linkmaName").textbox("setValue",data.linkmanName);
				//联系人电话
				$("#linkmaTel").textbox("setValue",data.linkmanTel);
				//联系人地址
				$("#linkmaAdd").textbox("setValue",data.linkmanAddress);
				//医疗证号
				$("#mcardNo").textbox("setValue",data.mcardNo);	
 				//民族
				$("#nationCode").combobox("setValue",data.nationCode);
				var nationCode= data.patientNation;
				//婚姻状况
				$("#mari").combobox("setValue",data.mari);
				var mari= data.mari;
				//职业
				$("#profCode").combobox("setValue",data.profCode);
				var profCode= data.patientOccupation;
				//国籍
				$("#counCode").combobox("setValue",data.counCode);
				var counCode= data.patientNationality;
				//联系人关系
				$("#relaCode").combobox("setValue",data.relaCode);
				var relaCode= data.patientLinkrelation;
				deptBqId= data.reportBedward;
				$('#bedId').combobox({
			   		 url:'<%=basePath %>inpatient/recall/getcomboboxBystate.action?deptBqId='+deptBqId, 
			    	 async:false,		
				     valueField:'id',    
				     textField:'bedName',
				     multiple:false,
				     onSelect:function(data){
				    	$('#bedId').combobox('setValue',data.id);
				    	$('#bedNo').val(data.bedName);
				    }
				});
		}	
	}
	function query(){
		$('#predoctName').combogrid({disabled:false});
		var  medicalrecordIds = $('#medicalrecordIds').textbox('getValue');
		$("#medicalrecordId").val(medicalrecordIds);
		if(medicalrecordIds == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		if(flag==false){
			$.ajax({
				url: "<c:url value='/inpatient/prepayin/queryCity.action'/>",
				async:false,
				type:'post',
				success: function(data) {
					cityMap = data;
				}
			});
			$.ajax({
				url: "<c:url value='/inpatient/prepayin/queryContractunit.action'/>",
				async:false,
				type:'post',
				success: function(data) {
					contractunitMap = data;
				}
			});
			//性别渲染
			$.ajax({
				url : "<%=basePath %>/baseinfo/pubCodeMaintain/queryDictionary.action",
				async:false,
				data:{"type":"sex"},
				type:'post',
				success: function(data) {
					var v = data;
					for(var i=0;i<v.length;i++){
						sexMap.put(v[i].encode,v[i].name);
					}
				}
			});
			$('#profCode').combobox({
			    url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type='+"occupation",    
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
			$('#nationCode').combobox({
			    url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type='+"nationality",    
			    async:false,
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
			$('#counCode').combobox({
			    url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type='+"country",
			    async:false,
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
			$('#relaCode').combobox({
			    url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type='+"relation", 
			    async:false,
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
			$('#mari').combobox({
			    url:'<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action?type='+"marry", 
			    async:false,
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
				});
			$('#predoctName').combogrid({
				url : "<%=basePath%>inpatient/admission/findZhuyuanDoc.action?type=ys",
		 		disabled : false,
		 		mode:'remote',
		 		panelWidth:450, 
				rownumbers : true,//显示序号 
				pagination : true,//是否显示分页栏
				fitColumns : true,//自适应列宽
				pageSize : 5,//每页显示的记录条数，默认为10  
				pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
				idField : 'name',
				textField : 'name',
				columns : [ [ {
					field : 'jobNo',
					title : '编号',
					width : 200
				}, {
					field : 'name',
					title : '姓名',
					width : 200,
				}, {
					field : 'deptName',
					title : '所在科室',
					width : 200
				}] ],
				onSelect: function (rowIndex, rowData){
				    $('#predoctCode').val(rowData.jobNo);
				}
			});
			flag = true;
		}
		
		$.ajax({//根据病历号模糊查询
			url: '<%=basePath %>inpatient/prepayin/queryProofList.action?medId='+medicalrecordIds,
			type:'post',
			success: function(data){
				  $.messager.progress('close');
 				if(data.length>1){
 					$("#diaInpatient").window('open');
					$("#infoDatagrid").datagrid({
						url: '<%=basePath %>inpatient/prepayin/queryProofList.action?medId='+medicalrecordIds,
						columns:[[					
							{field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
							{field:'reportSex',title:'性别',width:'10%',align:'center',formatter:function(value,row,index){
								return sexMap.get(value);
							}},
							{field:'medicalrecordId',title:'病历号',width:'35%',align:'center'} ,
							{field:'bedId',title:'病床号',width:'35%',align:'center'} ,
							{field:'idcardNo',title:'门诊号',width:'35%',align:'center'} ,
						]] ,
						onDblClickRow:function(rowIndex, rowData){
							$("#diaInpatient").window('close');
							$.ajax({
								url:'<%=basePath %>inpatient/prepayin/queryProofData.action?medId='+rowData.medicalrecordId,
								data:{idcardNo:rowData.idcardNo},
								type:'post',
								success:function(date){
									var patlist=date;
									if(patlist.patientName!=null&&patlist.patientName!=''){
										$.ajax({//根据病历号查询信息
											url: '<%=basePath %>inpatient/prepayin/queryPrepayinVo.action?medId='+rowData.medicalrecordId,
											data:{no:rowData.idcardNo},
											type:'post',
											success: function(data) {
												edit(data);								
											}
										})	
									}else{
										 $.messager.alert('提示','该患者没有有效的住院证明');
										 setTimeout(function(){
												$(".messager-body").window('close');
											},3500);
										 return false;
									}
								}
							})
							
						}
					})
 				}else if(data.length==1){
 					$.ajax({
 						url:'<%=basePath %>inpatient/prepayin/queryProofData.action?medId='+data[0].medicalrecordId,
						data:{idcardNo:data[0].idcardNo},
						type:'post',
						success:function(date){
							var patlist=date;
							if(patlist.patientName!=null&&patlist.patientName!=''){
								$.ajax({
									url: '<%=basePath %>inpatient/prepayin/queryPrepayinVo.action?medId='+data[0].medicalrecordId,
									data:{no:data[0].idcardNo},
			 						type:'post',
			 						success:function(data){
			 							edit(data);								
			 						}
			 					})	
							}else{
								 $.messager.alert('提示','该患者没有有效的住院证明');
								 setTimeout(function(){
										$(".messager-body").window('close');
									},3500);
								 return false;
							}
						}
 					})
 				}else{
 					$('#predoctName').combogrid({disabled:true});
					$.messager.alert('提示','查无此人');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}
			}
		});
	}

	function submit(){ 
		//验证身份证号的格式
		var idenNo=$('#idenNo').textbox('getValue');
		if(idenNo!=null&&idenNo!=""){
			if(!isIdCardNo(idenNo)){
				return;
			}
		}
		if($("#homeTel").val()&&$("#homeTel").val()!=""&&!isTelphoneNum($("#homeTel").val())&&!isMobilephoneNum($("#homeTel").val())){
			$.messager.alert('提示',"家庭电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return false;
		}
	
		if($("#workTel").val()!="" && $("#workTel").val()!=null && !isTelphoneNum($("#workTel").val())&&!isMobilephoneNum($("#workTel").val())){
			$.messager.alert('提示',"单位电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return false;
		}
		if($("#preDate").val()==null||$("#preDate").val()==""){
			$.messager.alert('提示',"请选择预约日期");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return false;
		}
		if($("#linkmaTel").val()!="" && $("#linkmaTel").val()!=null && !isTelphoneNum($("#linkmaTel").val())&&!isMobilephoneNum($("#linkmaTel").val())){
			$.messager.alert('提示',"联系人电话格式不正确,格式如01088888888,010-88888888,0955-7777777或13800571506!");
			setTimeout(function(){
				$(".messager-body").window('close');
			},2000);
			return false;
		}
    	$("#nurseCellCode").val($("#nurseCellCode2").combobox("getValue"));
		
		var bedId = $('#bedId').combobox('getValue');
		$.messager.progress({text:"保存中,请稍等......",modal:true});
		
		$.ajax({
    		url:'<%=basePath %>inpatient/prepayin/queryBedWardInfo.action?bedId='+bedId,
    		async: false,		 
    		success:function(data){
    			$('#bedwardID').val(data[0].id);
    			$('#bedwardName').val(data[0].bedwardName);
    		}
    				
    	});
	    $('#editForm').form('submit',{  
	        url:'<%=basePath %>inpatient/prepayin/savePrepayin.action',  
	        onSubmit:function(){
				if (!$('#editForm').form('validate')) {
					$.messager.show({
						title : '提示信息',
						msg : '验证没有通过,不能提交表单!'
					});
					 $.messager.progress('close');
					return false;
				}
	        },  
	        success:function(data){
				$.messager.progress('close');
				if(data=="ok"){
					$.messager.alert('提示','保存成功');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
				}else{
					$.messager.alert('提示','保存失败');
				}
	        	$('#list').datagrid('reload');
	        	clear();
	        },
			error : function(data) {
				$.messager.alert('提示','保存失败！');
			}							         
	    }); 
     }	
     //查询
  	function searchFrom(){
  		var medicalrecordId =  $('#medId').val();
  		if(medicalrecordId == ''){
			$.messager.alert('提示','请输入病历号！');
			setTimeout(function(){
				$(".messager-body").window('close');
			},3500);
			return false;
		}
	    $('#list').datagrid('load', {
			medicalrecordId: medicalrecordId 
		});
	}	
	//按回车键提交表单！
	$('#searchTab').find('input').on('keyup', function(event) {
		if (event.keyCode == 13) {
			searchFrom();
		}
	});
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style type="text/css">
.prepayinListBorder .panel-header{
	border-top:0;
}
.prepayinListBorder .datagrid-wrap{
	border-left:0;
}
</style>
</head>
<body>
	<div class="easyui-layout prepayinListBorder" data-options="fit:true,border:false">
		<div data-options="region:'east',title:'已预约患者',collapsible:false,border:true" style="width:50%;">
			<div class="easyui-layout" data-options="fit:true,border:false">
				<div data-options="region:'north',split:false,border:false" style="height: 59px;">
					<table  id="searchTab" style="width:100%;padding-top: 15px;" >
						<tr>
							<td>
							病历号：
							<input class="easyui-textbox" id="medId" style="width:180px;" data-options="prompt:'输入病历号回车查询'" />
							<shiro:hasPermission name="${menuAlias}:function:query">
							&nbsp;&nbsp;<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:readCard">
   								<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
   							</shiro:hasPermission>
			        		<shiro:hasPermission name="${menuAlias}:function:readIdCard">
			        			<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
   							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:delete">
							<a href="javascript:void(0)" onclick="del()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel' ">取消预约</a>
							</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</div>
				<div data-options="region:'center',split:false,border:false" style="height: 100%;width: 100%;">
				<table id="list" class="easyui-datagrid" data-options="url:'<%=basePath %>inpatient/prepayin/queryPrepayin.action',checkOnSelect:true,selectOnCheck:false,singleSelect:false ,method:'post',rownumbers:true,idField: 'id',striped:true,singleSelect:true,pagination:true,pageSize:20,fit:true,fitColumns:true,toolbar:'#toolbarId'">
					<thead>
						<tr>
							<th data-options="field:'getIdUtil',checkbox:true"></th>
							<th data-options="field:'name'">患者姓名</th>
							<th data-options="field:'medicalrecordId'">病历号</th>
							<th data-options="field:'bedId'">病床号</th>
							<th data-options="field:'preDate'">预约时间</th>
							<th data-options="field:'predoctName'">预约医师</th>
							<th data-options="field:'deptCodeName'">入院科室</th>
							<th data-options="field:'mcardNo'">医疗证号</th>
							<th data-options="field:'linkmaName'">联系人</th>
							<th data-options="field:'linkmaTel'">联系人电话</th>
							<th data-options="field:'home'">家庭住址</th>
							<th data-options="field:'homeTel'">家庭电话</th>
						</tr>
					</thead>
				</table>
				</div>
			</div>
		</div>
		<div data-options="region:'center',title:'患者基本信息',border:false" style="width: 50%">
			<div class="easyui-layout" data-options="fit:true">
				<form id="editForm" method="post">
					<div data-options="region:'north',split:false" style="height: 59px;border-top:0">
						<table  style="width:100%;padding-top: 10px;" >
							<tr >
								<td nowrap="nowrap">病历号：
								<input id="medicalrecordIds" onkeydown="KeyDown()" style="width:180px;" class="easyui-textbox" data-options="prompt:'输入病历号回车查询',required:true" >
								<input type="hidden" name="medicalrecordId" id="medicalrecordId">
								<shiro:hasPermission name="${menuAlias}:function:query">
								&nbsp;&nbsp;<a href="javascript:void(0)" onclick="query()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:readCard">
       								<a href="javascript:void(0)"  class="easyui-linkbutton read_medical_card" type_id="read_medical_cardID" id="read_medical_cardID" type_value="read_medical_card" onclick="read_baseCard_ic();" data-options="iconCls:'icon-bullet_feed'">读卡</a>
       							</shiro:hasPermission>
					        	<shiro:hasPermission name="${menuAlias}:function:readIdCard">
					        		<a href="javascript:void(0)"  class="easyui-linkbutton read_identity" type_id="read_identityID" id="read_identityID" type_value="read_identity" onclick="read_baseCard_sfz();" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
       							</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:save">
									<a href="javascript:submit();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
								</shiro:hasPermission>
									<a href="javascript:clear();void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
								
								</td>
							</tr>
						</table>
					</div>
					<div data-options="region:'center',border:false" style="width:100%;">
						<table id="listPrepayin" class="honry-table" style="width:100%;height: 75%">
							<tr>
							
								<td class="honry-lable">就诊卡号：</td>
								<td><input id="cardNo" name="cardNo" class="easyui-textbox" data-options="required:true" readonly="readonly"  ></td>
								<td class="honry-lable">入院科室：</td>
								<td><input id="deptCode2" name="deptCodeName" class="easyui-textbox" data-options="required:true"  readonly="readonly" >
								<input id="deptCode" name="deptCode" type="hidden">
								</td>
							</tr>
							<tr>
								<td class="honry-lable">预约日期：</td>
								<td>
								<input id="preDate" name="preDate"  class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-{%d+1}',maxDate:'%y-%M-{%d+7}'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								<td class="honry-lable">姓名：</td>
								<td><input id="name" name="name" class="easyui-textbox"  data-options="required:true" readonly="readonly"  ></td>
							</tr>
							<tr>
								<td class="honry-lable">性别：</td>
								<td>
								<input type="hidden" id="sexHid" >
								<input id="sexCode" name="sexCode" class="easyui-textbox" data-options="required:true" readonly="readonly" ></td>
								<td class="honry-lable">合同单位：</td>
								<td><input id="pactCode2" name="pactCode2" class="easyui-textbox" readonly="readonly" />
									<input id="pactCode" name="pactCode" type="hidden" />
								</td>
							</tr>
							<tr>
								<td class="honry-lable">籍贯：</td>
								<td><input id="dist" name="dist" class="easyui-textbox"  readonly="readonly" ></td>
								<td class="honry-lable">民族：</td>
								<td><input id="nationCode" name="nationCode" class="easyui-textbox" readonly="readonly"  ></td>
							</tr>
							<tr>
								<td class="honry-lable">家庭所在县：</td>
								<td><input id="homeDistrict2" name="homeDistrict2" class="easyui-textbox" readonly="readonly"  />
									<input id="homeDistrict" name="homeDistrict" type="hidden"  />
								</td>
								<td class="honry-lable">出生日期：</td>
								<td>
								    <input id="birthday" name="birthday" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
							</tr>
							<tr>
								<td class="honry-lable">婚姻状况：</td>
								<td><input id="mari" name="mari" class="easyui-textbox" readonly="readonly" ></td>
								<td class="honry-lable">身份证号：</td>
								<td><input id="idenNo" name="idenNo1" class="easyui-textbox" readonly="readonly"  ></td>
								<input id="idenNo1" name="idenNo" type="hidden" >
							</tr>
							<tr>
								<td class="honry-lable">职业：</td>
								<td><input id="profCode" name="profCode" class="easyui-textbox" readonly="readonly" ></td>
								<td class="honry-lable">出生地：</td>
								<td><input id="birthArea" name="birthArea1" class="easyui-textbox" readonly="readonly" ></td>
								<input id="birthArea1" name="birthArea" type="hidden">
							</tr>
							<tr>
								<td class="honry-lable">国籍：</td>
								<td><input id="counCode" name="counCode" class="easyui-textbox"  readonly="readonly" ></td>
								<td class="honry-lable" style="width:200px">入院诊断：</td>
								<td><input id="diagCode" name="diagCode" class="easyui-textbox"  readonly="readonly" ></td>
							</tr>
							<tr>
								<td class="honry-lable">家庭住址：</td>
								<td><input id="home" name="home" class="easyui-textbox" readonly="readonly"  ></td>
								<td class="honry-lable">家庭电话：</td>
								<td><input id="homeTel" name="homeTel" class="easyui-textbox" readonly="readonly"  ></td>
							</tr>
							<tr>
								<td class="honry-lable">工作单位：</td>
								<td><input id="workName" name="workName" class="easyui-textbox" readonly="readonly"   ></td>
								<td class="honry-lable">单位电话：</td>
								<td><input id="workTel" name="workTel" class="easyui-textbox" readonly="readonly"  ></td>
							</tr>
							<tr>
								<td class="honry-lable">联系人：</td>
								<td><input id="linkmaName" name="linkmaName" class="easyui-textbox" readonly="readonly"  ></td>
								<td class="honry-lable">联系人关系：</td>
								<td><input id="relaCode" name="relaCode" class="easyui-textbox" readonly="readonly"  ></td>
							</tr>
							<tr>
								<td class="honry-lable">联系人住址：</td>
								<td><input id="linkmaAdd" name="linkmaAdd" class="easyui-textbox" readonly="readonly"  ></td>
								<td class="honry-lable">联系人电话：</td>
								<td><input id="linkmaTel" name="linkmaTel" class="easyui-textbox" readonly="readonly"  ></td>
							</tr>
						</table>					
						<table id="listPrepayin" class="honry-table" data-options="border:false"  style="width:100%;height: 25%;border-top:0" >
							<tr>
								<td class="honry-lable" style="border-top:0">护士站：</td>
								<td style="border-top:0"><input id="nurseCellCode2" name="nurseCellName" class="easyui-combobox" data-options="required:true" readonly="readonly" >
									<input id="nurseCellCode" name="nurseCellCode" type="hidden" >
								</td>
								<td class="honry-lable" style="width:200px;border-top:0">病床号：</td><%-- valueField:'id',textField:'bedName',url:'${pageContext.request.contextPath}/inpatient/queryPrepayinBed.action', --%>
								<td style="border-top:0"><input id="bedId" name="bedId" class="easyui-combobox"  data-options="required:true " ></td>
									<input id="bedNo" name="bedNo" type="hidden">
									<input id="bedwardName" name="bedwardName" type="hidden">
									<input id="bedwardID" name="bedwardID" type="hidden">
							</tr>
							<tr>
								<td class="honry-lable" >医疗证号：</td>
								<td><input id="mcardNo" name="mcardNo" class="easyui-textbox"  readonly="readonly"  ></td>
								<td class="honry-lable" >预约医生：</td>
								<td><input id="predoctName" name="predoctName"    data-options="required:true"></td>
								<input id="predoctCode" name="predoctCode" type="hidden">
							</tr>
							<tr>
								<td class="honry-lable">操作员：</td>
								<td colspan="3"><input id="emp" name="no" class="easyui-textbox"  value="${userName}"  readonly="readonly" ></td>
							</tr>
						</table>					
					</div>
				</form>
				 <div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:450;height:600;padding:10" data-options="modal:true, closed:true">   
		     		<table id="infoDatagrid"  data-options="border:false,fitColumns:true,singleSelect:true,fit:true,rownumbers:true">   
					</table>  
   				 </div>  
			</div>
		</div>
	</div>
</body>