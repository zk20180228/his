<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head><title>门诊特限药品申请</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>

<style type="text/css">
.window .panel-header .panel-tool a{
  	  	background-color: red;	
	}
</style>
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
<script>
		/**
		*科室map
		*/
		var deptMap=null;
		/**
		*患者看诊号
		*/
		var medicalrecordId=null;
		/**
		*特限药申请记录主键Id
		*/
		var mainId=null;
		/**
		*保存当前的用户年龄单位
		*/
		var countAge=null;
		var drugCodeToName=new Map();
		//默认加载
		$(function(){
			 //申请医生
			 $('#applicDoctor') .combobox({    
				 url:"<%=basePath%>publics/consultation/queryLoginUserDept.action",    
				    valueField:'jobNo',    
				    textField:'name',
				    multiple:false,
				    onHidePanel:function(none){
				        var data = $(this).combobox('getData');
				        var val = $(this).combobox('getValue');
				        var result = true;
				        for (var i = 0; i < data.length; i++) {
				            if (val == data[i].jobNo) {
				                result = false;
				            }
				        }
				        if (result) {
				            $(this).combobox("clear");
				        }else{
				            $(this).combobox('unselect',val);
				            $(this).combobox('select',val);
				        }
				    },
				    filter: function(q, row){
				        var keys = new Array();
				        keys[keys.length] = 'jobNo';
				        keys[keys.length] = 'name';
				        keys[keys.length] = 'pinyin';
				        keys[keys.length] = 'wb';
				        keys[keys.length] = 'inputCode';
				        return filterLocalCombobox(q, row, keys);
				    }
	   		 });
			 //科室
			$.ajax({
				url: "<%=basePath%>publics/consultation/querydeptComboboxs.action", 
				success: function(deptData) {
					deptMap = deptData;
				}
			});
			//医生（渲染）
			$.ajax({
				url:"<%=basePath%>publics/consultation/queryEmpMapPublic.action",
				success:function(data){
					empMap=data;
				}
			});
			//药品渲染
			$.ajax({
				url:'<%=basePath%>publics/drugSpedrug/queryDrugInfoCodeAndName.action',
				success:function(data){
 					for(var i=0;i<data.length;i++){
 						drugCodeToName.put(data[i].code,data[i].name);
 					}
				}
			});
			//初始化药品弹框
			$('#drugCode').combogrid({
				required : true,
				rownumbers : true,//显示序号 
				pagination : true,//是否显示分页栏
				striped : true,//数据背景颜色交替
				panelWidth : 550,//容器宽度
				fitColumns : true,//自适应列宽
				
				mode:'remote',
				pageSize : 5,//每页显示的记录条数，默认为10  
				pageList : [ 5, 10 ],//可以设置每页记录条数的列表  
				url :"<%=basePath%>publics/drugSpedrug/queryDrugInfoSpe.action", 
				idField : 'code',
				textField : 'name',
				columns : [ [
				{field : 'code',title : 'code',hidden:true},
				{field : 'name',title : '药品名称', width: '40%'},
				{field : 'drugCommonname',title : '通用名称',width: '20%'},
				{field : 'spec',title : '规格', width: '30%'},
				] ],
				onClickRow:function(rowIndex,rowdata){
					$('#spec').textbox('setValue',rowdata.spec);
					$('#drugName').val(rowdata.name);
				}
			});
			//初始化下拉框
			$('#purpose').combobox({
				data:[{"id":1,"text":"预防性"},{	"id":2,"text":"治疗性"},{"id":3,"text":"经验性用药"}],
				valueField:'id',    
			    textField:'text',
				width:150
			});
			$('#isexam').combobox({
			    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],    
			    valueField:'id',    
			    textField:'text',
			    multiple:false
			});
			$('#isbacterial').combobox({
			    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],    
			    valueField:'id',    
			    textField:'text',
			    multiple:false
			});
									    
			$('#CodeUseage').combobox({
					url: "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action?type=useage'/>", 
			    	valueField:'encode',    
				    textField:'name',
				    onHidePanel:function(none){
				        var data = $(this).combobox('getData');
				        var val = $(this).combobox('getValue');
				        var result = true;
				        for (var i = 0; i < data.length; i++) {
				            if (val == data[i].encode) {
				                result = false;
				            }
				        }
				        if (result) {
				            $(this).combobox("clear");
				        }else{
				            $(this).combobox('unselect',val);
				            $(this).combobox('select',val);
				        }
				    },
				    filter: function(q, row){
				        var keys = new Array();
				        keys[keys.length] = 'encode';
				        keys[keys.length] = 'name';
				        keys[keys.length] = 'pinyin';
				        keys[keys.length] = 'wb';
				        keys[keys.length] = 'inputCode';
				        return filterLocalCombobox(q, row, keys);
				    }
			});	
			$('#issensitive').combobox({
				    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],     
				    valueField:'id',    
				    textField:'text',
				    required:true,
				    disabled:false,
				    multiple:false
			});	
			bindEnterEvent('applicDoctor',popWinToEmployee,'easyui');//绑定回车事件
			 /**
			  * 用法回车弹出事件高丽恒
			  * 2016-03-22 14:41
			  */
			 bindEnterEvent('CodeUseage',popWinToCodeUseage,'easyui');
			//设置申请医生为当前登录医生
			$('#applicDoctor').combobox('setValue','${empId}');
			
			bindEnterEvent('medId',searchPatient,'easyui');
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
					$('#medId').textbox('setValue',data);
					searchPatient();
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
						$('#medId').textbox('setValue',data);
						searchPatient();
					}
				});
			};
		/*******************************结束读身份证***********************************************/
		 /**
		  * 用法回车弹出事件高丽恒
		  * 2016-03-22 14:41
		  */
		function popWinToCodeUseage(){
			var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=useage&textId=CodeUseage";
			var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
		}
	//查询方法
	function search(medicalrecordId){
		$.ajax({
			url:"<%=basePath%>publics/drugSpedrug/queryresInfo.action?id="+medicalrecordId+"&menuAlias=${menuAlias}",
			success:function(data){ 
				dataMap=data;
				if(dataMap.key=="success"){
					$('#patientDept').textbox('setValue',dataMap.value.dept);
					$('#patientDept').textbox('setText',deptMap[dataMap.value.dept]);
					$('#name').textbox('setValue',dataMap.value.patientName);
					//挂号医生（主管医生）
					$('#comDoctor').val(dataMap.value.doctor);
					//门诊号
					$('#clinicCode').textbox('setValue',dataMap.value.cliniCode);
					//性别
					$('#CodeSex').textbox('setValue',sexMap.get(dataMap.value.patientSex));
					$('#strSex').val(dataMap.value.patientSex)
					//门诊诊断
					$('#diagnose').textbox('setValue',dataMap.value.notes);
					//看诊时间
					$('#createTime').val((dataMap.value.startTime).split(' ')[0]);
					var ages=DateOfBirth(dataMap.value.patientBirthday);
					$('#age').textbox('setValue',ages.get("nianling")+dataMap.value.ageUnit);
					$('#age1').val(ages.get("nianling"));
				}else{
					$.messager.alert('提示',dataMap.value);
					setTimeout(function(){$(".messager-body").window('close')},1500);
				}
			}
		});
	}
			
//表单提交submit信息
  	function submit(){
  		var drugCode=$('#drugCode').textbox('getValue');
  		if(!$('#editForm').form('validate')){
  			$.messager.alert("提示","验证不通过，请修改后保存！");
  			close_alert();
  		}
  		$.ajax({
  			url:'<%=basePath%>publics/drugSpedrug/querydrugdoc.action?drugCode='+drugCode,
  			success:function(data){
  				dataMap=data;
  				if(dataMap.key=="success"){
					$.messager.alert('提示',dataMap.value);
					setTimeout(function(){$(".messager-body").window('close')},1500);
  				}else if(dataMap.key=="none"){
				  	$('#editForm').form('submit',{
				  		url:'<%=basePath%>publics/drugSpedrug/saveOrUpdateDrugSpedrug.action?type=0',
				  		 onSubmit:function(){ 
						     return $(this).form('validate');
						     $.messager.progress({text:'保存中，请稍后...',modal:true});
						 },  
						success:function(result){
							$.messager.progress('close');
							if(result!=null&&result!=''&&result!='undefind'){
								mainId=result;
								$.messager.alert("提示","保存成功");
							   //实现刷新
							 	clear();
						   }
						 },
						error:function(result){
							$.messager.progress('close');
							$.messager.alert("提示","保存失败");
						}
				  	});
  				}
  			}
  		});
  	}
	/**
	*清除所填信息
	*/
	function clear() {
		$('#editForm').form('clear');
		$('#isexam').combobox('setValue','1');
		$('#issensitive').combobox('setValue','1');
		$('#isbacterial').combobox('setValue','1');
		$('#applicDoctor').combobox('setValue','${empId}');
	}
	/**
	*根据输入就诊卡号查询用户信息
	*/
	function searchPatient(){
		var medId=$.trim($('#medId').textbox('getValue'));
		if(medId==null||medId==""){
			return false;
		}
		$.messager.progress({text:'查询中，请稍后...',modal:true});
		clear();
		//模糊查询患者
		$.ajax({
			url:'<%=basePath%>publics/drugSpedrug/queryDrugspeClin.action?cardId='+medId,
			success:function(data){
				$.messager.progress('close');
				var patientlist=data;
				 if(patientlist.length>1){
					$("#diaInpatient").window('open');
					$("#infoDatagrid").datagrid({
						data:data,
					    columns:[[    
					        {field:'patientName',title:'姓名',width:'25%',align:'center'} ,   
					        {field:'patientSex',title:'性别',width:'25%',align:'center',
					        	formatter:function(value,row,index){
					        		return sexMap.get(value);
			            }} ,
					        {field:'clinicCode',title:'看诊号',width:'25%',align:'center'} ,  
					        {field:'patientIdenno',title:'身份证号',width:'25%',align:'center'} 
					    ]] ,
					    onDblClickRow:function(rowIndex, rowData){
					    	medicalrecordId=rowData.midicalrecordId;
					    	clinicCode=rowData.clinicCode;
					    	$("#diaInpatient").window('close');
					    	searchValue(clinicCode,rowData.createTime,rowData.patientBirthday,medicalrecordId); 
					  
					    }
					});
				  }else if(patientlist.length==0){
						$.messager.alert('提示',"病历号有误，请重新输入");
						close_alert();
				  }else if(patientlist.length==1){
					  medicalrecordId=patientlist[0].midicalrecordId;
					  //完整就诊卡号
					  countAge=patientlist[0].patientAgeunit;
					  clinicCode=patientlist[0].clinicCode;
					  searchValue(clinicCode,patientlist[0].createTime,patientlist[0].patientBirthday,medicalrecordId);  
				  } 
			}
		});
	}
	/**
	*根据就诊卡号查询患者的门诊特限药申请记录
	*/
	function searchValue(clinicCode,createTime,brithday,medicalrecordId){
		$.ajax({
			url:'<%=basePath%>publics/drugSpedrug/queryDrugspe.action?medId',
			data : {medId : clinicCode, index : '1'},
			success:function(date){
				var data=date;
				if(data.length>1){
					$("#drugwin").window('open');
					$("#druglist").datagrid({
						data:data,
						columns:[[
						            {field:'clinicCode',title:'看诊号',width:'20%',align:'center'},
						            {field:'name',title:'姓名',width:'20%',align:'center'} ,
						            {field:'patientDept',title:'患者所在科室',width:'20%',align:'center',
						            	formatter:function(value,row,index){
						            		return deptMap[value];
						            }} ,
						            {field:'comDoctor',title:'主管医生',width:'20%',align:'center',formatter:functiondoc} ,
						            {field:'drugCode',title:'药品名称',width:'20%',align:'center',formatter: function(value,row,index){
											if(''!=value&&null!=value){
												return drugCodeToName.get(value);
											}
									}}]],
						onDblClickRow:function(i,rowData){
							
							//主键ID
							$('#mainId').val(data[i].id);
							//姓名
							$('#name').textbox('setValue',data[i].name);
							//性别
							$('#CodeSex').textbox('setValue',data[i].sexName);
							$('#strSex').val(data[i].sex);
							//科室
							$('#patientDept').textbox('setValue',data[i].patientDept);
							$('#patientDept').textbox('setText',deptMap[data[i].patientDept]);
							//入院时间
							$('#createTime').val(createTime.split(' ')[0]);
							//病历号
							$('#clinicCode').textbox('setValue',data[i].clinicCode);
							//入院诊断
							$('#diagnose').textbox('setValue',data[i].diagnose);
							//药品名称
							$('#drugCode').combogrid('setValue',data[i].drugCode);
							//规格
							$('#spec').textbox('setValue',data[i].spec);
							//用药目的
							$('#purpose').combobox('setValue',data[i].purpose);
							//用法
							$('#CodeUseage').combobox('setValue',data[i].usage);
							//用量
							$('#dosage').textbox('setValue',data[i].dosage);
							//用药依据
							$('#drugBased').textbox('setValue',data[i].drugBased);
							//已送病院学检查
							$('#isexam').combobox('setValue',data[i].isexam);
							//样本类型
							$('#sampleType').textbox('setValue',data[i].sampleType);
							//所申请药物对该病原菌过敏感
							$('#issensitive').combobox('setValue',data[i].issensitive);
							//已有细菌培养及药敏结果
							$('#isbacterial').combobox('setValue',data[i].isbacterial);
							//请填写病菌
							$('#bacterial').textbox('setValue',data[i].bacterial);
							//住院期间已使用抗菌药物
							$('#useddrug').textbox('setValue',data[i].useddrug);
							//申请医师
							$('#applicDoctor').combobox('setValue',data[i].applicDoctor);
							//感染诊断或可能感染诊断
							$('#infectiondiagnosis').textbox('setValue',data[i].infectiondiagnosis);
							//申请依据
							$('#applyReason').textbox('setValue',data[i].applyReason);
							//特殊使用级抗菌药物专家组意见
							$('#groupOpinion').textbox('setValue',data[i].groupOpinion);
							//根据病历号查询患者年龄单位
							ageUnit=DateOfBirth(brithday);
							//年龄                   
							$('#age').textbox('setValue',ageUnit.get("nianling")+ageUnit.get("ageUnits"));
							//隐士年龄
							$('#age1').val(ageUnit.get("nianling"));
							$("#drugwin").window('close');
						} 
					});
				}else if(data.length==1){
					
					//主键ID
					$('#mainId').val(data[0].id);
					//姓名
					$('#name').textbox('setValue',data[0].name);
					//性别
					$('#CodeSex').textbox('setValue',data[0].sexName);
					$('#strSex').val(data[0].sex);
					//科室
					 $('#patientDept').textbox('setValue',data[0].patientDept);
					 $('#patientDept').textbox('setText',deptMap[data[0].patientDept]);
					//入院时间
					$('#createTime').val((data[0].createTime).split(' ')[0]);
					//病历号
					$('#clinicCode').textbox('setValue',data[0].clinicCode);
					//入院诊断
					$('#diagnose').textbox('setValue',data[0].diagnose);
					//药品名称
					$('#drugCode').combogrid('setValue',data[0].drugCode);
					//规格
					$('#spec').textbox('setValue',data[0].spec);
					//用药目的
					$('#purpose').combobox('setValue',data[0].purpose);
					//用法
					$('#CodeUseage').combobox('setValue',data[0].usage);
					//用量
					$('#dosage').textbox('setValue',data[0].dosage);
					//用药依据
					$('#drugBased').textbox('setValue',data[0].drugBased);
					//已送病院学检查
					$('#isexam').combobox('setValue',data[0].isexam);
					//样本类型
					$('#sampleType').textbox('setValue',data[0].sampleType);
					//所申请药物对该病原菌过敏感
					$('#issensitive').combobox('setValue',data[0].issensitive);
					//已有细菌培养及药敏结果
					$('#isbacterial').combobox('setValue',data[0].isbacterial);
					//请填写病菌
					$('#bacterial').textbox('setValue',data[0].bacterial);
					//住院期间已使用抗菌药物
					$('#useddrug').textbox('setValue',data[0].useddrug);
					//申请医师
					$('#applicDoctor').combobox('setValue',data[0].applicDoctor);
					//感染诊断或可能感染诊断
					$('#infectiondiagnosis').textbox('setValue',data[0].infectiondiagnosis);
					//申请依据
					$('#applyReason').textbox('setValue',data[0].applyReason);
					//特殊使用级抗菌药物专家组意见
					$('#groupOpinion').textbox('setValue',data[0].groupOpinion);
					 ageUnit=DateOfBirth(brithday);
					//年龄                   
					$('#age').textbox('setValue',ageUnit.get("nianling")+ageUnit.get("ageUnits"));
					//隐士年龄
					$('#age1').val(ageUnit.get("nianling"));
				}else if(data.length=="0"){
					search(medicalrecordId);
				}else{
					$.messager.alert('提示',"未知错误，请联系管理员");
					close_alert();
				}
			}
		});
	}
	/**
	*重新添加
	*/
	function add(){
		if($('#name').val()==''){
			$.messager.alert('提示','请先查询要新增的患者');
			close_alert();
			return;
		}
		clear();
		search(medicalrecordId);
	}
	 /**
	  *打印报表 
	  */
	function printList(mainId) {
  		 var timerStr = Math.random();
  		 var name=$('#name').textbox('getText');
  		 var age=$('#age').textbox('getText');
		 var his_name = encodeURIComponent(encodeURIComponent(name));
  		  window.open ('<%=basePath%>iReport/iReportPrint/iReportToTSGLLKJYSQ.action?randomId='+timerStr+'&name='+his_name+'&mainId='+mainId+'&fileName=TSGLLKJYSQ&age='+encodeURIComponent(encodeURIComponent(age)),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
	}
  	function printIreport(){
  		  mainId=$('#mainId').val();
	  	 if(mainId==null||mainId==''||mainId=='undefind'){
	  		$.messager.alert('提示','请先保存药品信息');
	  		close_alert();
	  		 return;
	  	 }else{
	  		printList(mainId);
	  	 }
  	}
  	/**
	*科室渲染
	*/
	function functiondept(value,row,index){
		if(value!=null&&value!=''){
			return deptMap[value];
		}
	}
	/**
	*医生渲染
	*/
	function functiondoc(value,row,index){
		if(value!=null&&value!=''){
			return empMap[value];
		}
	}
	/**
	   * 回车弹出申请医师弹框
	   * @author  zhuxiaolu
	   * @param textId 页面上commbox的的id
	   * @date 2016-03-22 14:30   
	   * @version 1.0
	   */
	   function popWinToEmployee(){
		    var tempWinPath = "<%=basePath%>popWin/popWinEmployee/toEmployeePopWin.action?textId=applicDoctor&employeeType=1";
			window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth -300) +',height='+ (screen.availHeight-
				170)+',scrollbars,resizable=yes,toolbar=yes')
	   }
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="title:'门诊特限药品申请',fit:true" >
			<div data-options="region:'north',border:false"  style="width:100%;height:35px">
				<table>
					<tr>
						<td>&nbsp;病历号：</td>
						<td>
							<input class="easyui-textbox" id="medId" data-options="prompt:'请输入病历号'"/>
						</td>
						<td>
							<a href="javascript:searchPatient();" class="easyui-linkbutton" style="margin:2px;"	data-options="iconCls:'icon-search'">查询</a>
							<a href="javascript:void(0)" cardNo="" onclick="read_card_ic()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读卡</a>
							<a href="javascript:void(0)" cardNo="" onclick="read_card_sfz()" class="easyui-linkbutton" data-options="iconCls:'icon-bullet_feed'">读身份证</a>
						</td>
					</tr>
				</table>
			</div>
			<div data-options="region:'center',border:false" style="width:100%;">
			<input type="hidden" id="id" name="id" value="${id }">	
				<form id="editForm" method="post" style="width:100%;" >
				
					<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width:99%;height:70%;" > 
						<input id="mainId" name="id" type="hidden"/> <%--主键ID --%>
						<input id="comDoctor" name="comDoctor" type="hidden"/> <%--主管医生字段 --%>
						<input id="drugName" name="drugName" type="hidden"/> <%--药品名称--%>
						<tr>
							<td colspan="6">患者基本信息
							</td>
						</tr>
						<tr>
							<td class="honry-lable" style="width: 15%;">
								患者姓名：
							</td>
							<td >
								<input id="name" class="easyui-textbox" name="name" readonly="true"
									   />
							</td>
							<td   class="honry-lable" style="width: 15%;">
								性别：
							</td>
							<td >
							<input  id="CodeSex" class="easyui-textbox"  readonly="readonly" />
							<input id="strSex" name="sex" type="hidden" >
							</td>
							<td  class="honry-lable" style="width: 15%;">
								年龄：
							</td>
							<td >
								<input id="age" class="easyui-textbox"   readonly="readonly" />
								<input id="age1" type="hidden" name="age">
							</td>
						</tr>
						<tr>
							<td   class="honry-lable">
							 科室：
							</td>
							<td >
								<input id="patientDept" class="easyui-textbox" name="patientDept" readonly="readonly"
									 />
							</td>
							<td  class="honry-lable">
								看诊号：
							</td>
							<td >
								<input id="clinicCode" class="easyui-textbox" name="clinicCode"  data-options="" readonly/>
							</td>
							<td class="honry-lable">
								看诊时间：
							</td>
							<td >
								  <input id="createTime" name="createTime" class="Wdate" type="text" onClick="WdatePicker({maxDate:'%y-%M-%d'})"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="readonly"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								门诊诊断：
							</td>
							<td colspan="5">
								<input id="diagnose"  class="easyui-textbox"  name="diagnose" style="width: 78%" />
							</td>
						</tr>
						<tr>
							<td colspan="6">药品基本信息
							</td>
						</tr>
						<tr>
							<td class="honry-lable">药品名称：</td>
							<td >
								<input id="drugCode" class="easyui-combogrid" name="drugCode"   data-options="required:true"/>
							</td>
							<td class="honry-lable">规格：</td>
							<td >
							<input id="spec"class="easyui-textbox" name="spec"  data-options="required:true"/>
							</td>
							<td class="honry-lable">用药目的：</td>
							<td >
								<input id="purpose" name="purpose" class="easyui-combobox"  data-options="required:true"/> 
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								用法：
							</td>
							<td  >
								<input id="CodeUseage" name="usage" class="easyui-combobox"  data-options="required:true,editable:false"/>
								<a href="javascript:delSelectedData('CodeUseage');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>	
							</td>
							<td class="honry-lable">
								用量：
							</td>
							<td >
							<input id="dosage" class="easyui-textbox" name="dosage"  data-options="required:true"/>
							</td>
							<td class="honry-lable">
								用药依据：
							</td>
							<td >
								<input id="drugBased" name="drugBased" class="easyui-textbox"  data-options="required:true"/>
							</td>
						</tr>
			 			<tr>
							<td colspan="6">患者基本病情
							</td>
						</tr>
						<tr>
						</tr>
						<tr>
							<td class="honry-lable">
								已送病院学检查：
							</td>
							<td >
								<input id="isexam"  name="isexam" class="easyui-combobox" value="1"
									  data-options="required:true,editable:false"/>
							</td>
							<td class="honry-lable">
								样本类型：
							</td>
							<td>
								<input id="sampleType" class="easyui-textbox" name="sampleType"  />
							</td>
							<td class="honry-lable" rowspan="2">
								所申请药物对该病原菌过敏感：
							</td>
							<td colspan="3" rowspan="2">
							<input id="issensitive"  name="issensitive" class="easyui-combobox" value="1" />
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								已有细菌培养及药敏结果：
							</td>
							<td >
								<input  id="isbacterial" name="isbacterial" class="easyui-combobox" value="1" data-options="required:true,editable:false"/>
							</td>
							<td class="honry-lable">
								请填写病菌：
							</td>
							<td >
							<input id="bacterial" name="bacterial"class="easyui-textbox"  />
							</td>						
						</tr>
						<tr>
							<td class="honry-lable">
								申请医师：
							</td>
							<td >
								<input id="applicDoctor" class="easyui-combobox" name="applicDoctor" data-options="required:true" />
								<a href="javascript:delSelectedData('applicDoctor');"  class="easyui-linkbutton"
									    data-options="iconCls:'icon-opera_clear',plain:true"></a>
							</td>
							<td class="honry-lable">
								申请依据：
							</td>
							<td colspan="3">
								<input id="applyReason" class="easyui-textbox" name="applyReason"  data-options="required:true" style="width:78%"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								住院期间已使用抗菌药物：
							</td>
							<td colspan="5">
							<input id="useddrug" class="easyui-textbox" name="useddrug"  data-options="required:true" style="width:60%"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								感染诊断或可能感染诊断：
							</td>
							<td colspan="8">
								<input id="infectiondiagnosis" class="easyui-textbox" name="infectiondiagnosis" style="width:60%"
									  data-options="required:true"/>
							</td>
						</tr>
						<tr>
							<td class="honry-lable">
								特殊使用级抗菌药物专家组意见：
							</td>
							<td colspan="8">
								<input id="groupOpinion" class="easyui-textbox" name="groupOpinion"  data-options="required:true" style="width:60%"/>
							</td>
						</tr>
					</table>
					<br>
					<div align="center" style="width: 90%;">
						<shiro:hasPermission name="${menuAlias }:function:edit">
							<a href="javascript:add(0);"  class="easyui-linkbutton" data-options="iconCls:'icon-add'">新增</a>
							<a href="javascript:submit(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
						</shiro:hasPermission>
							<a href="javascript:void(0)"  onclick="printIreport()" class="easyui-linkbutton"data-options="iconCls:'icon-printer'">打印</a>
							<a href="javascript:clear();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
					</div>
					</form>
					</div>
	
</div>
	<div id="drugwin" class="easyui-dialog" title="患者选择" style="width:45%;height:60%;padding:5 5 5 5" data-options="modal:true, closed:true">   
	   		<table id="druglist"  style="width:100%;height:100%" data-options="fitColumns:true,singleSelect:true"></table>
	 	</div>
	 	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:45%;height:60%;padding:5 5 5 5" data-options="modal:true, closed:true">   
	   		<table id="infoDatagrid"  style="width:100%;height:100%" data-options="fitColumns:true,singleSelect:true"></table>
	 	</div>
</body>
</html>