<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>特限药品申请</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<style type="text/css">
.easyui-dialog .panel-header .panel-tool a{
    background-color: red;	
}
</style>
<script>
/**树节点查询**/
function searchTreeNodes(){
    var searchText = $('#searchTreeInpId').textbox('getValue');
    $("#tDt").tree("search", searchText);
}
var accountUnit=null;
		/**
		*科室map
		*/
		var deptMap="";
		/**
		*员工map
		*/
		var empMap="";
		/**
		*病历号
		*/
		var medicalrecordId="";
		/**
		*特限药品表主键ID
		*/
		var mainId="";
		/**
		*默认加载
		*/
		/**药品渲染**/
		var medicalMap=new Map();
		$(function(){
			bindEnterEvent('searchTreeInpId',searchTreeNodes,'easyui');
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
			$.ajax({
				url:'<%=basePath%>publics/drugSpedrug/queryDrugInfoCodeAndName.action',
				success:function(data){
 					for(var i=0;i<data.length;i++){
 						medicalMap.put(data[i].code,data[i].name);
 					}
				}
			});
			//年龄渲染
			var ageUnit;
			/**
			 * 在列别页面插入树
			 * @author  
			 * @param 
			 * @date 2015-06-03
			 * @version 1.0
			 */
			//加载患者树		    
			$('#tDt').tree({
				url : '<%=basePath%>inpatient/doctorAdvice/treeDoctorAdvice.action',
				animate : true,
				lines : true,
				formatter : function(node) {//统计节点总数
					var s = node.text;
					if(node.children.length>0){
						if (node.children) {
							s += '&nbsp;<span style=\'color:blue\'>('
									+ node.children.length + ')</span>';
						}
					}
					return s;
				},
				onClick:function(node){
					$(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
				},
				onDblClick : function(node) {//点击节点
					var inpatientNo = node.attributes.inpatientNo;
					var medicalrecordId = node.attributes.medicalrecordId;
					clear();
					$.ajax({
						url:'<%=basePath%>publics/drugSpedrug/queryDrugspe.action',
						data : {medId : inpatientNo, index : '2'},
						success:function(date){
							
							var data=date;
							if(data.length>1){
								$("#drugwin").window('open');
								$("#druglist").datagrid({
									url:'<%=basePath%>publics/drugSpedrug/queryDrugspe.action?medId='+inpatientNo,
									columns:[[
									            {field:'clinicCode',title:'住院流水号号',width:'20%',align:'center'} ,
									            {field:'name',title:'姓名',width:'15%',align:'center'} ,
									            {field:'patientDept',title:'患者所在科室',width:'20%',align:'center',formatter:functiondept} ,
									            {field:'comDoctor',title:'主管医生',width:'15%',align:'center',formatter:functiondoc} ,
									            {field:'drugCode',title:'药品名称',width:'30%',align:'center',formatter: function(value,row,index){
													if(''!=value&&null!=value){
														return medicalMap.get(value);
													}
											}} 
									]],
									onDblClickRow:function(i,rowData){
										if(data[i].brithday != undefined){
											ageUnit = DateOfBirth(data[i].brithday);
											//年龄
											$('#age').textbox('setValue',ageUnit.get("nianling")+ageUnit.get("ageUnits"));
											//隐士年龄
											$('#age1').val(ageUnit.get("nianling"));
											ageUnit=ageUnit.get("nianling")+ageUnit.get("ageUnits");
										}
										$('#comDoctor').val(data[i].comDoctor);
										//主键ID
										$('#id').val(data[i].id);
										$('#mainId').val(data[i].id);
										//姓名
										$('#name').textbox('setValue',data[i].name);
										//性别
										$('#CodeSex').textbox('setValue',data[i].sexName);
										
										//科室
 										$('#patientDept2').val(data[i].patientDept);
										$('#patientDept').textbox('setValue',deptMap[data[i].patientDept]);
										//病床号
										$('#bedNo').textbox('setValue',data[i].bedNo);
										//病历号
										$('#clinicCode').textbox('setValue',data[i].clinicCode);
										//入院时间
										$('#inDate').val(data[i].inDate);
										//入院诊断
										$('#diagnose').textbox('setValue',data[i].diagnose);
										//药品名称
										if(""==data[i].drugName||null==data[i].drugName){
										$('#drugName').val(medicalMap.get(data[i].drugCode));
										}else{
										$('#drugName').val(data[i].drugName);
										}
										$('#drugCode').combogrid('setText',data[i].drugName);
										$('#drugCode2').val(data[i].drugCode)
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
										$("#drugwin").window('close');
									} 
								});
							}else if(data.length==1){
								if(data[0].brithday != undefined){
									ageUnit = DateOfBirth(data[0].brithday);
									//年龄
									$('#age').textbox('setValue',ageUnit.get('nianling')+ageUnit.get("ageUnits"));
									$('#age1').val(ageUnit.get('nianling'));
									ageUnit = ageUnit.get("nianling") + ageUnit.get("ageUnits");
								}
								//主键ID
								$('#id').val(data[0].id);
								$('#mainId').val(data[0].id);
								 $('#comDoctor').val(data[0].comDoctor);
								//姓名
								$('#name').textbox('setValue',data[0].name);
								//性别
								$('#CodeSex').combobox('setValue',data[0].sex);
								//科室
  								 $('#patientDept2').val(data[0].patientDept);
 								 $('#patientDept').textbox('setText',deptMap[data[0].patientDept]);
								//病历号
								$('#clinicCode').textbox('setValue',data[0].clinicCode);
								//病床号
								$('#bedNo').textbox('setValue',data[0].bedNo);
								//入院时间
								$('#inDate').val(data[0].inDate);
								//入院诊断
								$('#diagnose').textbox('setValue',data[0].diagnose);
								//药品名称
								if(""==data[0].drugName||null==data[0].drugName){
									$('#drugName').val(medicalMap.get(data[0].drugCode));
								}else{
									$('#drugName').val(data[0].drugName);
								}
								$('#drugCode').combogrid('setValue',data[0].drugName);
								$('#drugCode2').val(data[0].drugCode);
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
							}else if(data.length=="0"){
								queryPatientInfo(medicalrecordId);
							}else{
								$.messager.alert("未知错误，请联系管理员");
								close_alert();
							}
						}
					});
					
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
				idField : 'encode',
				textField : 'name',
				columns : [ [
				{field : 'id',title : 'id',hidden:true},
				{field : 'code',title : 'code',hidden:true},
				{field : 'name',title : '药品名称', width: '40%'},
				{field : 'drugCommonname',title : '通用名称',width: '20%'},
				{field : 'spec',title : '规格', width: '30%'},
				] ],
				onClickRow:function(rowIndex,rowdata){
					$('#spec').textbox('setValue',rowdata.spec);
					$('#drugName').val(rowdata.name);
					$('#drugCode2').val(rowdata.code);
				}
			});
			
			//性别下拉框
			$('#CodeSex').combobox({
				 valueField:'encode',
				 textField:'name',
				 url:"<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex",
				 required:true
			})
			//初始化下拉框
			$('#purpose').combobox({
				data:[{"id":1,"text":"预防性"},{	"id":2,"text":"治疗性"},{"id":3,"text":"经验性用药"}],
				valueField:'id',    
			    textField:'text',
				width:150,
			
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
			    delay:5,
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
			$('#applicDoctor').combobox({
				 url:'<%=basePath%>publics/consultation/queryLoginUserDept.action',    
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
			$('#applicDoctor').combobox('setValue','${empId}');
			 bindEnterEvent('applicDoctor',popWinToEmployee,'easyui');//绑定回车事件
			 /**
			  * 用法回车弹出事件高丽恒
			  * 2016-03-22 14:41
			  */
			 bindEnterEvent('CodeUseage',popWinToCodeUseage,'easyui');
				function popWinToCodeUseage(){
					var tempWinPath = "<%=basePath%>popWin/popWinCode/toCodePopWinDB.action?type=useage&textId=CodeUseage";
					var aaa=window.open (tempWinPath,'newwindow',' left=150,top=80,width='+ (screen.availWidth - 300) +',height='+ (screen.availHeight-170) +',scrollbars,resizable=yes,toolbar=no')
				}
			$('#issensitive').combobox({
							    data:[{"id":0,"text":"是"},{	"id":1,"text":"否"}],     
							    valueField:'id',    
							    textField:'text',
							    required:true,
							    disabled:false,
							    multiple:false
	
			 });
		});
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

	   //从xml文件中解析，读到下拉框,只有性别用到了,这里改造成性别专用
		function idCombobox(param){
			$('#'+param).combobox({
			    url : "<c:url value='/baseinfo/pubCodeMaintain/queryDictionary.action'/>?type=sex ",
			    valueField:'encode',    
			    textField:'name',
			    multiple:false
			});
		}	
			
		function getSelected() {//获得选中节点
			var node = $('#tDt').tree('getSelected');
			if (node) {
				if (node.attributes.isNO != "") {
					var id = node.id;
					return id;
				} else {
					return "";
				}
			} else {
				return "";
			}
		}
	/**
	*患者信息赋值方法
	*/
	function queryPatientInfo(medicalrecordId){
		$.ajax({
			url: '<%=basePath%>publics/drugSpedrug/querylistInpatientInfo.action?medId='+medicalrecordId,
			success: function(data) {
				var idCardObj = data;
				 $('#name').textbox('setValue',idCardObj.patientName);
				 $('#patientDept').textbox('setValue',deptMap[idCardObj.deptCode]);
				 $("#patientDept2").val(idCardObj.deptCode);
				 if(idCardObj.reportBirthday != undefined){
					ageUnit = DateOfBirth(idCardObj.reportBirthday);
					//显示年龄
					$('#age').textbox('setValue',ageUnit.get("nianling")+ageUnit.get("ageUnits"));
					//隐士年龄
					$('#age1').val(ageUnit.get("nianling"));
					ageUnit=ageUnit.get("nianling")+ageUnit.get("ageUnits");
				 }
				 $('#bedNo').textbox('setValue',idCardObj.bedName);
				 $('#inDate').val(idCardObj.inDate);
				 $('#clinicCode').textbox('setValue',idCardObj.inpatientNo);
				 $('#CodeSex').combobox('setValue',idCardObj.reportSex);
				 $('#diagnose').textbox('setValue',idCardObj.diagName);
				 //住院医生
				 $('#comDoctor').val(idCardObj.houseDocCode);

			}
		}); 
	}
		
	/**
	*表单提交submit信息
	*/
  	function submit(){
		//申请医生验证
		if(null==empMap[$('#applicDoctor').combobox('getValue')]){
			$.messager.alert("提示","请选择申请医师");
			close_alert();
			return false;
		}
		if(null==$('#inDate').val()||''==$('#inDate').val()){
			$.messager.alert('提示信息','入院日期不能为空')
			close_alert();
			return false;
		}
		var drugCode=$('#drugCode').textbox('getValue');
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
							if(result!=null&&result!=''&&result=='true'){
								mainId=result;
								$.messager.alert("提示","保存成功");
							    //实现刷新
							  	clear();
						   }else{
							   $.messager.alert("提示","保存失败");
						   }
						 },
						error:function(result){
							$.messager.progress('close');
							$.messager.alert("提示","保存失败");
						}
				  	});
				}else{
					$.messager.alert("提示","未知错误请联系管理员");
					close_alert();
				}
			}
		});
  	}
  	
  /**
  *打印报表 
  */
	function printList(mainId) {
	  	 var age=$('#age').textbox('getText');
	  	 var name=$('#name').textbox('getText');
  		 var timerStr = Math.random();
		 var his_name = encodeURIComponent(encodeURIComponent(name));
  		  window.open ('<%=basePath%>iReport/iReportPrint/iReportToTSGLLKJYSQ.action?randomId='+timerStr+'&HIS_NAME='+his_name+'&mainId='+mainId+'&fileName=TSGLLKJYSQ&age='+encodeURIComponent(encodeURIComponent(age))+'&name='+encodeURIComponent(encodeURIComponent(name)),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
	}
  	function printIreport(){
  		  mainId=$('#mainId').val();
	  	 if(mainId==null||mainId==''||mainId=='undefind'){
			$.messager.alert('提示',"请先保存要进行申请的药品信息");
			close_alert();
	  		 return;
	  	 }else{
	  		printList(mainId);
	  	 }
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
	*新增按钮、清除表单信息、保留患者个人信息
	*/
	function add(){
		if($('#mainId').val()==''){
			$.messager.alert('提示','该患者还未申请过特限药品,请保存后再进行新增操作!');
			close_alert();
			return;
		}
		clear();
		queryPatientInfo(medicalrecordId);
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
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
<style>
	#treeLayOut .panel-header{
		border-top:0
	}
</style>
</head>
<body style="height: 100%;width: 100%;">
<div class="easyui-layout" data-options="fit:true" id="treeLayOut" >
	
<!--	左边树-->
	<div id="p" data-options="region:'west',split:true" title="特限药记录" style="width:20%;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false">
				<input type="text" class="easyui-textbox" id="searchTreeInpId" data-options="prompt:'患者姓名'" style="width: 200px;"/>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchTreeNodes()" style="margin-top: 2px;">搜索</a>
			</div>
			<div id="treeDiv" style="width: 100%;" data-options="region:'center',border:false">
				<ul id="tDt"></ul>  
			</div>
		</div>
	</div>
<!--	右边面板-->
		<div  id="list" data-options="title:'特限药',iconCls:'icon-form',region:'center'" style="width:80%;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%;height:40px;">
					<shiro:hasPermission name="${menuAlias }:function:edit"> 
						<a href="javascript:add();" style=" margin: 5px" class="easyui-linkbutton"
							data-options="iconCls:'icon-add'">新增</a>
						<a href="javascript:submit();" style=" margin: 5px" class="easyui-linkbutton"
							data-options="iconCls:'icon-save'">保存</a>
					</shiro:hasPermission> 
					<shiro:hasPermission name="${menuAlias }:function:print">
					<a href="javascript:void(0)" style=" margin: 5px" onclick="printIreport()"
						class="easyui-linkbutton"
						data-options="iconCls:'icon-printer'">打印</a>
					  </shiro:hasPermission> 
						<a href="javascript:clear();" style=" margin: 5px" class="easyui-linkbutton"
							data-options="iconCls:'icon-clear'">清除</a>
				</div>
			<div data-options="region:'center',border:false" style="width: 100%;height:91%;padding:0px 0px 0px 5px;">
		<form id="editForm" method="post">
			<input type="hidden" id="id" name="id">
						<table class="honry-table" cellpadding="0" cellspacing="0" border="0" style="width: 99%;height:100%;" data-options="fit:true,border:false">
							<tr>
								<td colspan="6">患者基本信息</td>
							</tr>
							<tr>
								<input id="mainId" type="hidden"/> <%--主键ID --%>
								<input id="comDoctor" name="comDoctor" type="hidden"/> <%--主管医生字段 --%>
								<input id="drugName" name="drugName" type="hidden"/> <%--药品名称--%>			
								<td  class="honry-lable">
									患者姓名：
								</td>
								<td style="width: 2%">
									<input id="name" class="easyui-textbox" name="name" data-options="required:true"/>
								</td>
								<td  class="honry-lable">
									性别：
								</td>
								<td style="width: 2%">
									<input  id="CodeSex" class="easyui-combobox" name="sex" readonly="readonly"/>
								</td>
								<td  class="honry-lable">
									年龄：
								</td>
								<td style="width: 2%" >
									<input id="age" class="easyui-textbox"  readonly="readonly" data-options="required:true"/>
									 <input id="age1" type="hidden" name="age" >
								</td>
							</tr>
							<tr>
								<td  style="width:1%"class="honry-lable">
								 科室：
								</td>
								<td style="width: 2%">
									<input id="patientDept" class="easyui-textbox" name="patientDept2" readonly="readonly"
										 data-options="required:true" />
									<input id="patientDept2" name="patientDept" type="hidden">
									
								</td>
								<td  class="honry-lable">
									床号：
								</td>
								<td  >
									<input class="easyui-textbox" name="bedNo" id="bedNo" readonly="readonly"
										  data-options="required:true"/>
								</td>
								<td class="honry-lable">
									住院流水号：
								</td>
								<td  >
									<input id="clinicCode" class="easyui-textbox" name="clinicCode" readonly="readonly" data-options="required:true"/>
								</td>
								
							</tr>
							<tr>
								<td  class="honry-lable">
									入院时间：
								</td>
								<td >
									<input id="inDate" class="Wdate" name="inDate" type="text" onClick="WdatePicker()"  style="width:150px;border: 1px solid #95b8e7;border-radius: 5px;" readonly="readonly"/>
								</td>
								<td class="honry-lable">
									入院诊断：
								</td>
								<td colspan="5">
									<input id="diagnose" style="width: 75%" class="easyui-textbox" name="diagnose"  data-options="required:true"/>
								</td>
							</tr>
							<tr>
								<td colspan="6">药品基本信息</td>
							</tr>
							<tr>
								<td class="honry-lable">
									药品名称：
								</td>
								<td >
									<input id="drugCode" class="easyui-combogrid"   data-options="required:true"/>
									<input id="drugCode2" name="drugCode" type="hidden"/> <%--药品编号--%>
									
								</td>
								<td class="honry-lable" >
									规格：
								</td>
								<td >
									<input id="spec"class="easyui-textbox" name="spec"  readonly="readonly" />
								</td>
								<td class="honry-lable">
									用药目的：
								</td>
								<td >
									<input id="purpose" name="purpose" data-options="required:true,editable:false"/> 
								</td>
							</tr>
							<tr>
								<td class="honry-lable">
									用法：
								</td>
								<td  >
									<input id="CodeUseage" name="usage"  class="easyui-combobox" data-options=""/>
									<a href="javascript:delSelectedData('CodeUseage');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
								</td>
								<td class="honry-lable" >
									用量：
								</td>
								<td>
									<input id="dosage" class="easyui-textbox" name="dosage"  data-options="required:true"/>
								</td>
								<td class="honry-lable">
									用药依据：
								</td>
								<td >
									<input id="drugBased" name="drugBased" class="easyui-textbox" data-options="required:true"/>
								</td>
							</tr>
							<tr>
								<td  colspan="6">患者基本病情</td>
							</tr>
							<tr>
								<td class="honry-lable">
									已送病院学检查：
								</td>
								<td >
									<input id="isexam"  name="isexam" value="1"
										   data-options="required:true,editable:false"/>
								</td>
								<td class="honry-lable">
									样本类型：
								</td>
								<td>
									<input id="sampleType" class="easyui-textbox" name="sampleType" 
										 />
								</td>
								<td class="honry-lable" rowspan="2">
									所申请药物对该病原菌过敏感：
								</td>
								<td colspan="3" rowspan="2">
								<input id="issensitive"  name="issensitive" value="1"
								 style="width: 150px"  data-options="editable:false"/>
								</td>
							</tr>
							<tr>
								<td class="honry-lable">
									已有细菌培养及药敏结果：
								</td>
								<td >
									<input  id="isbacterial" name="isbacterial"  value="1"
									 data-options="required:true,editable:false"/>
								</td>
								<td class="honry-lable">
									请填写病菌：
								</td>
								<td >
								<input id="bacterial" name="bacterial"
								 class="easyui-textbox"  />
								</td>						
							</tr>
							<tr>
								<td class="honry-lable" >
									申请医师：
								</td>
								<td >
								<input id="applicDoctor" name="applicDoctor" 
								  data-options="required:true" />
								 <a href="javascript:delSelectedData('applicDoctor');"  class="easyui-linkbutton"
								    data-options="iconCls:'icon-opera_clear',plain:true,editable:false"></a>
								</td>
								<td class="honry-lable" >
									申请依据：
								</td>
								<td colspan="5">
								<input id="applyReason" class="easyui-textbox" name="applyReason" style="width:76%"
								  data-options="required:true" />
								</td>
							</tr>
							<tr>
								<td class="honry-lable">
									感染诊断或可能感染诊断：
								</td>
								<td colspan="5">
									<input id="infectiondiagnosis" class="easyui-textbox" name="infectiondiagnosis" style="width:58%"
										 data-options="required:true"/>
								</td>
							</tr>
							<tr>
								<td class="honry-lable">
									住院期间已使用抗菌药物：
								</td>
								<td colspan="5">
								<input id="useddrug" class="easyui-textbox" name="useddrug" style="width:58%"
								  data-options="required:true" />
								</td>
							</tr>
							<tr>
								<td class="honry-lable">
									特殊使用级抗菌药物专家组意见：
								</td>
								<td colspan="5">
								<input id="groupOpinion" class="easyui-textbox" name="groupOpinion" style="width:58%"
								data-options="required:true" />
								</td>
							</tr>
					</table>
			</form>
			</div>
			</div>
		</div>
	</div>
	 <div id="drugwin" class="easyui-dialog" title="患者选择" style="width:45%;height:60%;padding:5 5 5 5" data-options="modal:true, closed:true">   
	   <table id="druglist"  style="width:100%;height:100%" data-options="fitColumns:true,singleSelect:true"></table>
	 </div>
</body>
<script type="text/javascript">
	 var nowlogInDept="${nowLoginDept}";
	 if(nowlogInDept!=null&&nowlogInDept!=''){
	 	 $("body").setLoading({
	 			id:"body",
	 			isImg:false,
	 			text:"请选择登录科室"
	 	});
	 }
	 </script>
</html>