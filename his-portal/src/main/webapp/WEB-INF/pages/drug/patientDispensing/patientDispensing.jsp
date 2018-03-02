<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@ include file="/common/metas.jsp"%>
<title>病区患者摆药查询</title>
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
<body style="margin: 0px; padding: 0px;">
	<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div id="top" data-options="region:'north',border:false" style="height:40px;">
				<table padding:5px 5px 5px 5px;">
					<tr>
						<td>
							<a href="javascript:void(0)" onclick="searchGrid()" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:20px;margin-top:5px;">查询</a>
							<a href="javascript:void(0)" onclick="reload()" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" style="margin-left:20px;margin-top:5px;">刷新</a>
						</td>
					</tr>
				</table>
		</div>
		<div id="p" data-options="region:'west'" style="width: 13%;">
			<ul id="tDt1" style="width:100%;height:100%;"></ul>
		</div>
		<div data-options="region:'center'" >
			<div id="divLayout" class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north',split:false,border:false" style="width:100%;height: 45px;"> 
					<table cellspacing="0" cellpadding="0" border="0" data-options="fit:true">
						<tr>
							<td style="padding: 10px 5px;">&nbsp;&nbsp;病历号：</td>
							<td>
								<input id="medicalrecordName" style="width:160px;" class="easyui-textbox" data-options="prompt:'请输入病历号后六位回车查询'"/>																
							</td>
							<td style="padding: 10px 5px;">&nbsp;&nbsp;申请时间：</td>
							<td>
								<input id="applyDate" style="width:170px;" class="easyui-datetimebox"/> 至
								<input id="endDate" style="width:170px;" value="${now }" class="easyui-datetimebox"/>															
							</td>
							<td style="padding: 10px 5px;">&nbsp;&nbsp;
								<input id="tradeName" style="width:170px;" class="easyui-textbox" data-options="prompt:'请输入药品名称回车查询'"/>
							</td>															
						</tr>
					</table>
				</div>			
				<div data-options="region:'center',split:false,border:false">	
					<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="url:'<%=basePath%>statistics/PatientDispensing/queryVinpatientApplyoutlist.action',singleSelect:'true',pagination:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true">
						<thead>
							<tr>
								<th data-options="field:'patientName',width:'8%',align:'center' ">姓名</th>
								<th data-options="field:'tradeName',width:'12%',align:'center'">药品名称</th>
								<th data-options="field:'specs',width:'10%',align:'center'">规格</th>
								<th data-options="field:'dfqCexp',width:'10%',align:'center'">频次</th>
								<th data-options="field:'useName',width:'10%',align:'center'">用法</th>
								<th data-options="field:'applyNum',width:'5%',align:'center' ">总量</th>
								<th data-options="field:'doseUnit',width:'5%',align:'center'">单位</th>
								<th data-options="field:'drugDeptCode',width:'8%',align:'center'">取药药房</th>
								<th data-options="field:'sendType',width:'6%',align:'center'">发送类型</th>
								<th data-options="field:'billclassCode',width:'8%',align:'center'">摆药单</th>
								<th data-options="field:'applyOpercode',width:'8%',align:'center'">发送人</th>
								<th data-options="field:'applyDate',width:'10%',align:'center'">发送时间</th>
								<th data-options="field:'printEmpl',width:'8%',align:'center' ">发药人</th>
								<th data-options="field:'printDate',width:'10%',align:'center'">发药时间</th>
								<th data-options="field:'validState',width:'5%',align:'center'">有效性</th>
								<th data-options="field:'pinyin',width:'5%',align:'center'">拼音码</th>
								<th data-options="field:'wb',width:'5%',align:'center'">五笔码</th>
								<th data-options="field:'baiyao',width:'5%',align:'center'">是否摆药</th>
							</tr>
						</thead>
					</table>			
				</div>
			</div>
		</div>
	</div> 
	<div id="diaInpatient" class="easyui-dialog" title="患者选择" style="width:500;height:500;padding:30 30 30 30" data-options="modal:true, closed:true">   
		<table id="infoDatagrid"  style="width:400px;height:400" data-options="fitColumns:true,singleSelect:true">   
		</table>
	</div>
	<script type="text/javascript">
		var inpa="";
		$(function(){
			$('#applyDate').datetimebox({
					onChange:function(newValue,oldValue){
					var beginDate = $('#applyDate').datetimebox('getValue');
					var endDate = $('#endDate').datetimebox('getValue');
					var tradeName = $('#tradeName').textbox('getValue');
				   $('#billSearchHzList').datagrid('load',{
					    inpatientNo: inpa,
						beginDate:beginDate,
						endDate:endDate,
						tradeName:tradeName
					});
				}
			})
			$('#endDate').datetimebox({
					onChange:function(newValue,oldValue){
					var beginDate = $('#applyDate').datetimebox('getValue');
					var endDate = $('#endDate').datetimebox('getValue');
					var tradeName = $('#tradeName').textbox('getValue');
				   $('#billSearchHzList').datagrid('load',{
					    inpatientNo: inpa,
						beginDate:beginDate,
						endDate:endDate,
						tradeName:tradeName
					});
				}
			})
			$('#tradeName').textbox('textbox').bind('keyup', function(event) {
				searchGrid();
			});
			bindEnterEvent('medicalrecordName',searchGrid,'easyui');//绑定回车事件
			//bindEnterEvent('tradeName',searchGrid,'easyui');//绑定回车事件
			$('#tDt1').tree({ 
				   url:"<%=basePath%>statistics/PatientDispensing/InfoTree.action",
				   method:'get',
				   animate:true,  //点在展开或折叠的时候是否显示动画效果
				   lines:true,    //是否显示树控件上的虚线
				   formatter:function(node){//统计节点总数
					  var s = node.text;
					  if (node.children){
						s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				      }  
					  return s;
				   },onClick:function(node){
					   var beginDate = $('#applyDate').datetimebox('getValue');
					   var endDate = $('#endDate').datetimebox('getValue');
					   var tradeName = $('#tradeName').textbox('getValue');
					   inpa = node.attributes.no;
					   $('#billSearchHzList').datagrid('load',{
						    inpatientNo: inpa,
							beginDate:beginDate,
							endDate:endDate,
							tradeName:tradeName
						});
				   }
			});
		});
		function searchGrid(){
			var medicalrecordId = $('#medicalrecordName').val();//病历号
			if(medicalrecordId!=null&&medicalrecordId!=""){
				if(medicalrecordId.length!=6){
					$.messager.alert('提示','请正确输入病历号或者住院流水号后6位！');
					setTimeout(function(){
						$(".messager-body").window('close');
					},3500);
					$('#medicalrecordName').textbox('setValue','');
					return;
				}else{
					$.ajax({
						url:"<%=basePath%>statistics/PatientDispensing/queryInpatientInfolist.action",
						data:{medicalrecordId:medicalrecordId},
						type:'post',
						success:function(data){
							if(data.length==0){
								$.messager.alert('提示','未查到患者');  
								$('#medicalrecordName').textbox('setValue',"");
							}else if(data.length==1){
								inpa=data[0].inpatientNo;
								var beginDate = $('#applyDate').datetimebox('getValue');
							    var endDate = $('#endDate').datetimebox('getValue');
							    var tradeName = $('#tradeName').textbox('getText');
							    $('#billSearchHzList').datagrid('load',{
								    inpatientNo: data[0].inpatientNo,
									beginDate:beginDate,
									endDate:endDate,
									tradeName:tradeName
								});
							}else if(data.length>1){
								$("#diaInpatient").window('open');
								$("#infoDatagrid").datagrid({
									url:"<%=basePath%>statistics/PatientDispensing/queryInpatientInfolist.action",
									queryParams:{medicalrecordId:medicalrecordId},
									columns:[[
										{field:'medicalrecordId',title:'病历号',width:'30%',align:'center'} ,  
										{field:'reportSex',title:'性别',width:'10%',align:'center',formatter:function(value,row,index){
											return sexMap(value);
										}} ,
										{field:'patientName',title:'姓名',width:'20%',align:'center'} ,   
										{field:'certificatesNo',title:'身份证号',width:'40%',align:'center'} 
									]] ,
									onDblClickRow:function(rowIndex, rowData){
										$("#diaInpatient").window('close');
										var beginDate = $('#applyDate').datetimebox('getValue');
									    var endDate = $('#endDate').datetimebox('getValue');
									    var tradeName = $('#tradeName').textbox('getText');
									    inpa=rowData.inpatientNo;
									    $('#billSearchHzList').datagrid('load',{
										    inpatientNo: rowData.inpatientNo,
											beginDate:beginDate,
											endDate:endDate,
											tradeName:tradeName
										});
									}
								});	
							}
						}
					});
				}
			}else{
				var beginDate = $('#applyDate').datetimebox('getValue');
			    var endDate = $('#endDate').datetimebox('getValue');
			    var tradeName = $('#tradeName').textbox('getText');
				$('#billSearchHzList').datagrid('load',{
				    inpatientNo: inpa,
					beginDate:beginDate,
					endDate:endDate,
					tradeName:tradeName
				});
			}
		}
		//刷新
		function reload(){
			$('#medicalrecordName').textbox('setValue',"");
			var beginDate = $('#applyDate').datetimebox('setValue',"");
		    var endDate = $('#endDate').datetimebox('setValue',"${now}");
		    var tradeName = $('#tradeName').textbox('setValue',"");
			$('#billSearchHzList').datagrid('load',{
			    inpatientNo: null
			});
			$('#tDt1').tree("reload");
		}
	</script>
</body>
</html>