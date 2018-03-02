<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<body>
	<div class="easyui-panel" id="panelEast" data-options="title:'麻醉登记信息',iconCls:'icon-form',border:false,fit:true" style="width: 100%;height:100%;">
		<form id="editForm" method="post">
			<input type="hidden" id="operationno" name="operationno" value="${operation.id }">
			<input type="hidden" name="id" value="${operation.id }">
			<input type="hidden" name="clinicCode" value="${operation.clinicCode }">
			<input type="hidden" name="name" value="${operation.name }">
			<input type="hidden" name="sexCode"  value="${operation.sex }">
			<input type="hidden" name="patientNo" value="${operation.patientNo }">
			<input type="hidden" name="opAn.operationId" value="${operation.id }">
			<input type="hidden" name="opAn.clinicCode" value="${operation.clinicCode }">
			<input type="hidden" name="opAn.name" value="${operation.name }">
			<input type="hidden" name="opAn.sexCode"  value="${operation.sex }">
			<input type="hidden" name="opAn.patientNo" value="${operation.patientNo }">
			<input type="hidden" name="opAn.deptCode" value="${operation.inDept}">
			<div style="padding: 5px 5px 5px 5px;width: 100%;height:100%" >
   				<table class="honry-table" cellpadding="1" cellspacing="1" border="1px solid black" style="width:100%; height:100%;border:1px solid #95b8e7;padding:5px;">
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">住院号/门诊号：</td>
   						<td id="clinicCode" style="width: 18%;">${operation.clinicCode }</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">姓名：</td>
   						<td id="name" style="width: 18%;">${operation.name}</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">性别：</td>
   						<td id="sex" style="width: 18%;"></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">科室：</td>
   						<td id="dept" name="deptCode" style="width: 18%"></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">床号：</td>
   						<td id="bedno" style="width: 18%"> ${operation.bedNo }</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">出生日期：</td>
   						<td id="birthday" style="width: 18%"><fmt:formatDate value="${operation.birthday}" pattern="yyyy-MM-dd"/> </td>
   					</tr>
   					<tr>
   						<td style="text-align: right;color: blue;width:15%"><strong>申请麻醉类型：</strong></td>
   						<td colspan="5" id="mazuiType"></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">麻醉方式：</td>
   						<td style="width: 18%;"><input class="easyui-combobox" id="anesType" name="opAn.anesType" data-options="required:true" value=""  data-options="required:true"/></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">麻醉时间：</td>
   						<td style="width: 18%;">
   						<input id="anaeDate" name="opAn.anaeDate" value="" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+1}-%M-%d'})" style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
   						<input id="tmpAnaeDate"  value="${opAn.anaeDate }" type="hidden"/>
   						</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">是否记账：</td>
   						<td style="width: 18%;"><input class="easyui-combobox" name="opAn.chargeFlag" id="chargeFlag" data-options="valueField: 'id', textField: 'value',data: [{ id: '1', value: '是'},{ id: '2', value: '不是'}]" value="${opAn.chargeFlag }"/></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">镇痛方式：</td>
   						<td style="width:18%"><input class="easyui-combobox" id="demuKind" name="opAn.demuKind"  value="${opAn.demuKind }"/></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">泵型：</td>
   						<td style="width:18%"><input class="easyui-combobox" id="demuModel" name="opAn.demuModel"  value="${opAn.demuModel }"/></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">麻醉效果：</td>
   						<td style="width:18%"><input class="easyui-textbox" id="anaeResult" name="opAn.anaeResult"  value="${opAn.anaeResult }"/></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">拔管时间：</td>
   						<td style="width:18%">
   						<input id="pulloutDate" name="opAn.pulloutDate" value="" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'{%y+1}-%M-%d'})" style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
   						<input id="tmpPulloutDate"  value="${opAn.pulloutDate }" type="hidden"/>
   						</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">拔管人：</td>
   						<td style="width:18%"><input id="pulloutOpcd" class="easyui-combogrid" name="opAn.pulloutOpcd" /><a href="javascript:delSelectedData('pulloutOpcd');"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">镇痛天数：</td>
   						<td style="width:18%"><input class="easyui-numberbox" id="demuDays" name="demuDays" value="${opAn.demuDays }" /></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">入PACU：</td>
   						<td style="width:18%"><input class="easyui-combobox" id="isPacu" name="opAn.isPacu" data-options="valueField: 'id', textField: 'value',data: [{ id: '1', value: '是'},{ id: '2', value: '不是'}]"   value="${opAn.isPacu }"  /></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">手术镇痛：</td>
   						<td style="width:18%"><input class="easyui-combobox" id="isDemulcent" name="opAn.isDemulcent" data-options="valueField: 'id', textField: 'value',data: [{ id: '1', value: '是'},{ id: '0', value: '否'}]" value="${opAn.isDemulcent }"/></td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">镇痛效果：</td>
   						<td style="width:18%"><input class="easyui-textbox" id="demuResult" name="opAn.demuResult"  value="${opAn.demuResult }"/></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">入室时间：</td>
   						<td style="width:18%">
   						<input id="inpacuDate" name="opAn.inpacuDate" value="" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:pickedToInDate,maxDate:'{%y+1}-%M-%d'})" style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
   						<input id="tmpInpacuDate"  value="${opAn.inpacuDate}" type="hidden"/>
   						</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">入室状态：</td>
   						<td colspan="3"><input class="easyui-textbox" id="inpacuStatus" name="opAn.inpacuStatus" data-options="valueField: 'id', textField: 'value',data: [{ id: '1', value: '良好'},{ id: '2', value: '观察'},{ id: '3', value: '危险'},{ id: '4', value: '一般'}]"  value="${opAn.inpacuStatus }" /></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">出室时间：</td>
   						<td style="width:18%">
   						<input id="outpacuDate" name="opAn.outpacuDate" value="" class="Wdate" type="text"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:pickedToOutDate,maxDate:'{%y+1}-%M-%d'})" style="width:172px;border: 1px solid #95b8e7;border-radius: 5px;"/>
   						<input id="tmpOutpacuDate"  value="${opAn.outpacuDate}" type="hidden"/>
   						</td>	
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">出室状态：</td>
   						<td colspan="3"><input class="easyui-textbox" id="outpacuStatus" name="opAn.outpacuStatus" data-options="valueField: 'id', textField: 'value',data: [{ id: '1', value: '良好'},{ id: '2', value: '观察'},{ id: '3', value: '危险'},{ id: '4', value: '一般'}]"  value="${opAn.outpacuStatus }"/></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">备注：</td>
   						<td colspan="5"><textarea rows="2" cols="67" id="remark" name="opAn.remark">${opAn.remark}</textarea></td>
   					</tr>
   					<tr>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">麻醉医师：</td>
   						<td style="width:18%">
   							<input id="anaeDocd" name="opAn.anaeDocd" value="${opAn.anaeDocd }" data-options="required:true" class="easyui-combobox" />
   							<a href="javascript:delSelectedData('anaeDocd');" class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
   						</td>
   						<td style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">麻醉助手：</td>
   						<td colspan="3">
   							<input id="anaeHelper" name="opAn.anaeHelper" value="${opAn.anaeHelper}" data-options="required:true" class="easyui-combobox"/>
   						</td>
   					</tr>
   					<tr id="trsia0">
   						<td  style="background: #E0ECFF;text-align: right;width:15%" class="changecolor">临时麻醉助手1：<input id="lmzs0" type="hidden"></td>
   						<td id='thsia2' style="width:18%"><input  class="easyui-combogrid" id="thelpersia0_"  name ="thelpersia0_"/>
   						<a href="javascript:void(0)" id="athsia0" onclick="add('thsia','2','athsia','0')" class="easyui-linkbutton" data-options="iconCls:'icon-add'">
   						</td>
   					</tr>
   				</table>
   				<div style="text-align:center;padding:5px;width: 100%;height:50px" >
   				 <shiro:hasPermission name="${menuAlias}:function:save">
				 	<a href="javascript:save();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>&nbsp;
				  </shiro:hasPermission>
				 <a href="javascript:close();" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>&nbsp;
   				</div>
   			</div>
   			
		</form>
	</div>
<script type="text/javascript">
var dept = '${operation.inDept}';
var anesWay = '${opAn.anesType }';
var mazuiType = '${operation.anesType}';
var pull="${opAn.pulloutOpcd }";//拔管人
var oldMap=new Map();//加载麻醉信息map
var sexMap=new Map();
var aneType=new Map();
var nurseMap=new Map();//护士翻页渲染
$(function(){
	//性别渲染
	$.ajax({
		url : "<%=basePath %>baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"sex"},
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				sexMap.put(v[i].encode,v[i].name);
			}
			sexRend();
		}
	});
	
	//麻醉类型渲染
	$.ajax({
		url:"<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action",
		data:{"type":"aneType"},
		type:'post',
		success: function(data) {
			
			for(var i=0;i<data.length;i++){
				aneType.put(data[i].encode,data[i].name);
			}
		}
	});
	
	//获取镇痛方式
	$("#demuKind").combobox({
		 url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=demuKind",
		 valueField:'encode',    
	     textField:'name',
	});
	//获取泵型
	$("#demuModel").combobox({
		 url : "<%=basePath%>baseinfo/pubCodeMaintain/queryDictionary.action?type=demuModel",
		 valueField:'encode',    
	     textField:'name',
	});
	//下拉麻醉方式
	$('#anesType').combobox({    
		url: '<%=basePath %>operation/anaerecord/findAnaTypeCombobox.action',
	    valueField:'encode',    
	    textField:'name'
	});
	//护士渲染
	$.ajax({
		url : '<%=basePath %>operation/operationList/ssSysEmployeeList.action',
		type:'post',
		success: function(data) {
			var v = data;
			for(var i=0;i<v.length;i++){
				nurseMap.put(v[i].jobNo,v[i].name);
			}
		}
	});
	//麻醉医师
	$('#anaeDocd').combobox({    
		url:"<%=basePath %>operation/anesthesia/anesthesaneDoctor.action",
	    valueField:'id',    
	    textField:'empName',
	    onSelect:function(record){
	    	var anaeHelper = $('#anaeHelper').combobox('getValue');
	    	if(anaeHelper==record.id){
	    		$.messager.alert("操作提示","请不要与麻醉助手重复","info");
	    		setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
	    		$('#anaeDocd').combobox('setValue',"");
	    	}
	    },onHidePanel:function(none){
	        var data = $(this).combobox('getData');
	        var val = $(this).combobox('getValue');
	        var result = true;
	        for (var i = 0; i < data.length; i++) {
	            if (val == data[i].id) {
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
	    filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'id';
			keys[keys.length] = 'code';
			keys[keys.length] = 'empName';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputcode';
			return filterLocalCombobox(q, row, keys);
		}
	});
	//麻醉助手
	$('#anaeHelper').combobox({    
		url:"<%=basePath %>operation/anesthesia/anesthesaneDoctor.action",
	    valueField:'id',    
	    textField:'empName',
	    onSelect:function(record){
	    	var anaeDocd = $('#anaeDocd').combobox('getValue');
	    	if(anaeDocd==record.id){
	    		$.messager.alert("操作提示","请不要与麻醉医师重复","info");
	    		setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
	    		$('#anaeHelper').combobox('setValue',"");
	    	}
	    	
	    },
	    onHidePanel:function(none){
	        var data = $(this).combobox('getData');
	        var val = $(this).combobox('getValue');
	        var result = true;
	        for (var i = 0; i < data.length; i++) {
	            if (val == data[i].id) {
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
	    filter:function(q,row){
			var keys = new Array();
			keys[keys.length] = 'id';
			keys[keys.length] = 'code';
			keys[keys.length] = 'empName';
			keys[keys.length] = 'pinyin';
			keys[keys.length] = 'wb';
			keys[keys.length] = 'inputcode';
			return filterLocalCombobox(q, row, keys);
		}
	});
	
});		
	
	 /**  
	 *  
	 * @Description：过滤	
	 * @Author：zhangjin
	 * @CreateDate：2016-11-3
	 * @version 1.0
	 * @throws IOException 
	 *
	 */ 
	function filterLocalCombobox(q, row, keys){
		if(keys!=null && keys.length > 0){//
			for(var i=0;i<keys.length;i++){ 
				if(row[keys[i]]!=null&&row[keys[i]]!=''){
						var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1;
						if(istrue==true){
							return true;
						}
				}
			}
		}else{
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q.toUpperCase()) > -1;
		}
	}
	setTimeout(function(){
		var rowId=$("#rowId").val();
		//临时助手
		$.ajax({
			url:"<%=basePath %>operation/anaerecord/getThelpersia.action",
			data:{id:rowId,fore:"5"},	
			type:"post",
			success : function(data){
				if(data!=null&&data!=""){
					for(var i=0;i<data.length;i++){
							if(data[i].foreFlag=='5'){  //麻醉登记的手术人员安排信息
								if(i!=0){
									var a=2*i;
									if(!(i%3)){
										var l=i/3;
										var m=l-1;
										$("#trsia"+m).after("<tr id=\"trsia"+l+"\">"+
												"<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\"thsia"+(a+1)+"\" class=\"changecolor\">临时麻醉助手"+(i+1)+"：</td>"+
												"<td width=15% id=\"thsia"+(a+2)+"\"><input id=\"thelpersia"+i+"_"+data[i].id+"\"  class=\"easyui-combogrid\"  >"+
												"<a href=\"javascript:void(0)\" id=\"athsia"+i+"\" onclick=\"add('thsia','"+(a+2)+"','athsia','"+i+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
												"<a id=\"jathsia"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(a+2)+"','"+i+"')\"></a>"+
												"</td> </tr>");
									}else{
										$("#thsia"+a).after("<td align=\"right\" style=\"background-color: #E0ECFF;font-size:14px;width=15%\" id=\"thsia"+(a+1)+"\"  class=\"changecolor\">临时麻醉助手"+(i+1)+"：</td>"+
											"<td style=\"width=18%\" id=\"thsia"+(a+2)+"\"><input id=\"thelpersia"+i+"_"+data[i].id+"\"   class=\"easyui-combogrid\">"+
											"<a href=\"javascript:void(0)\" id=\"athsia"+i+"\" onclick=\"add('thsia','"+(a+2)+"','athsia','"+i+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
											"<a id=\"jathsia"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(a+2)+"','"+i+"')\"></a>"+
											"</td>");
									}
									$("#thelpersia"+i+"_"+data[i].id).combogrid({  //麻醉临时助手1
										url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
								 		idField : 'jobNo',
								 		textField : 'name',
								 		mode:"remote",
								 		panelAlign:'left',
								 		panelWidth:320,
								 		editable : true,
								 		pageList:[10,20,30,40,50],
										 pageSize:"10",
										 pagination:true,
									 	columns:[[   
												{field:'jobNo',title:'工作号',width:'130'},
									 	         {field:'name',title:'名称',width:'160'} 
									 	        
								 	     ]],
								 	    onHidePanel:function(none){
									 	    var val = $(this).combogrid('getValue');
								 	    	var pull2=$('#pulloutOpcd').combogrid("getValue");
								 			if(pull2){
								 				if(pull2==val){
								 					$(this).combogrid('clear');
											    	$.messager.alert("提示","临时助手与拔管人信息不能重复!","info");
											    	setTimeout(function(){
														$(".messager-body").window('close');
													},3000);
											    	return ;
									 	    	}
								 			}
								 	    	if(pullName(val)){
								 	    		$(this).combogrid('clear');
										    	$.messager.alert("提示","临时助手信息不能重复!","info");
										    	setTimeout(function(){
													$(".messager-body").window('close');
												},3000);
										    }							
									     },
									     onLoadSuccess: function () {
										    	var id=$(this).prop("id");
											    if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
										            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
										        } 
										   } 
									});
									$('#athsia'+i).linkbutton({    
									    iconCls: 'icon-add'   
									});  
									$('#jathsia'+i).linkbutton({    
									    iconCls: 'icon-remove'   
									}); 
									$('#athsia'+(i-1)).hide();
									$("#jathsia"+(i-1)).hide();
									$("#thelpersia"+i+"_"+data[i].id).combogrid('grid').datagrid('load',{q:data[i].emplCode});
									$("#thelpersia"+i+"_"+data[i].id).combogrid("setValue",data[i].emplCode);
								}else{
									$("#thelpersia0_").prop("id","thelpersia0_"+data[i].id);
									$("#thelpersia"+i+"_"+data[i].id).combogrid('grid').datagrid('load',{q:data[i].emplCode});
									$('#thelpersia0_'+data[i].id).combogrid("setValue",data[i].emplCode);
								}
								oldMap.put(data[i].id,data[i].emplName);
							}else{//麻醉安排的人员信息
								if(i!=0){
									var a=2*i;
									if(!(i%3)){
										var l=i/3;
										var m=l-1;
										$("#trsia"+m).after("<tr id=\"trsia"+l+"\">"+
												"<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\"thsia"+(a+1)+"\" class=\"changecolor\">临时麻醉助手"+(i+1)+"：</td>"+
												"<td width=15% id=\"thsia"+(a+2)+"\"><input id=\"thelpersia"+i+"_\"  class=\"easyui-combogrid\" >"+
												"<a href=\"javascript:void(0)\" id=\"athsia"+i+"\" onclick=\"add('thsia','"+(a+2)+"','athsia','"+i+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
												"<a id=\"jathsia"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(a+2)+"','"+i+"')\"></a>"+
												"</td> </tr>");
										
									}else{
										$("#thsia"+a).after("<td align=\"right\" style=\"background-color: #E0ECFF;font-size:14px;width=15%\" id=\"thsia"+(a+1)+"\" class=\"changecolor\" >临时麻醉助手"+(i+1)+"：</td>"+
												"<td style=\"width=18%\" id=\"thsia"+(a+2)+"\"><input id=\"thelpersia"+i+"_\"  class=\"easyui-combogrid\" >"+
												"<a href=\"javascript:void(0)\" id=\"athsia"+i+"\" onclick=\"add('thsia','"+(a+2)+"','athsia','"+i+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
												"<a id=\"jathsia"+i+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(a+2)+"','"+i+"')\"></a>"+
												"</td>");
									}
									$("#thelpersia"+i+"_").combogrid({  //麻醉临时助手1
										url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
								 		idField : 'jobNo',
								 		textField : 'name',
								 		mode:"remote",
								 		panelAlign:'left',
								 		panelWidth:320,
								 		editable : true,
								 		pageList:[10,20,30,40,50],
										 pageSize:"10",
										 pagination:true,
									 	columns:[[   
												{field:'jobNo',title:'工作号',width:'130'},
									 	         {field:'name',title:'名称',width:'160'} 
									 	        
								 	     ]],
								 	    onHidePanel:function(none){
									 	    var val = $(this).combogrid('getValue');
								 	    	var pull2=$('#pulloutOpcd').combogrid("getValue");
								 			if(pull2){
								 				if(pull2==val){
								 					$(this).combogrid('clear');
											    	$.messager.alert("提示","临时助手与拔管人信息不能重复!","info");
											    	setTimeout(function(){
														$(".messager-body").window('close');
													},3000);
											    	return ;
									 	    	}
								 			}
								 	    	if(pullName(val)){
								 	    		$(this).combogrid('clear');
										    	$.messager.alert("提示","临时助手信息不能重复!","info");
										    	setTimeout(function(){
													$(".messager-body").window('close');
												},3000);
										    }
									     },
									     onLoadSuccess: function () {
										    	var id=$(this).prop("id");
											    if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
										            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
										        } 
										   } 
									});
									$('#athsia'+i).linkbutton({    
									    iconCls: 'icon-add'   
									});  
									$('#jathsia'+i).linkbutton({    
									    iconCls: 'icon-remove'   
									}); 
									$('#athsia'+(i-1)).hide();
									$("#jathsia"+(i-1)).hide();
									$("#thelpersia"+i+"_").combogrid('grid').datagrid('load',{q:data[i].emplCode});
									$("#thelpersia"+i+"_").combogrid("setValue",data[i].emplCode);
								}else{
									var code=data[0].emplCode;
									$("#thelpersia0_").combogrid('grid').datagrid('load',{q:code});
									$("#thelpersia0_").combogrid('setValue',code);
								}
							}
						
					}
				}
			}
		});
		$("#pulloutOpcd").combogrid('grid').datagrid('load',{q:pull});
		$('#pulloutOpcd').combogrid('setValue',pull);
		if(dept!=null&&dept!=""){
			document.getElementById("dept").innerHTML = !deptMap[dept]?"":deptMap[dept];
		}
		if(mazuiType!=null&&mazuiType!=""){
			document.getElementById('mazuiType').innerHTML = aneType.get(mazuiType);
		}
		
		$('#anesType').combobox('select',anesWay);
	},300);

	//麻醉临时助手1
	$('#thelpersia0_').combogrid({    
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
 		idField : 'jobNo',
 		textField : 'name',
 		mode:"remote",
 		panelAlign:'left',
 		panelWidth:320,
 		editable : true,
 		pageList:[10,20,30,40,50],
		 pageSize:"10",
		 pagination:true,
	 	columns:[[   
				{field:'jobNo',title:'工作号',width:'130'},
	 	         {field:'name',title:'名称',width:'160'} 
	 	        
 	     ]],
 		onHidePanel:function(none){
 	    	var thelpersia=$('[id^=thelpersia]');
			var val = $(this).combogrid('getValue');
 	    	var pull2=$('#pulloutOpcd').combogrid("getValue");
 			if(pull2){
 				if(pull2==val){
 					$(this).combogrid('clear');
			    	$.messager.alert("提示","临时助手与拔管人信息不能重复!","info");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
			    	return ;
	 	    	}
 			}
 	    	if(pullName(val)){
 	    		$(this).combogrid('clear');
		    	$.messager.alert("提示","临时助手信息不能重复!","info");
		    	setTimeout(function(){
					$(".messager-body").window('close');
				},3000);
		    }
			
	     },
	     onLoadSuccess: function () {
		    	var id=$(this).prop("id");
			    if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
		            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
		        } 
		   } 
	});
	//拔管人
	$('#pulloutOpcd').combogrid({    
		url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
 		idField : 'jobNo',
 		textField : 'name',
 		mode:"remote",
 		panelAlign:'left',
 		panelWidth:320,
 		editable : true,
 		pageList:[10,20,30,40,50],
		 pageSize:"10",
		 pagination:true,
	 	columns:[[   
				{field:'jobNo',title:'工作号',width:'130'},
	 	         {field:'name',title:'名称',width:'160'} 
	 	        
 	     ]],
 	    onSelect:function(rowIndex, rowData){
 	    	var thelpersia=$('[id^=thelpersia]');
 	    	var id=$("#pulloutOpcd").combogrid("getValue");
			thelpersia.each(function(index,obj){
 				if($(obj).combogrid('getValue') == id &&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
 					$("#pulloutOpcd").combogrid("clear");
					$.messager.alert("提示","拔管人与助手之间不能重复！","info");
					setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
					return;
 				}
			});
	     },
	     onLoadSuccess: function () {
		    	var id=$(this).prop("id");
			    if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
		            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
		        } 
		   }   
	});
	

/**  
 *  
 * @Description：删除指定项
 * @Author：zhangjin
 * @CreateDate：2016-6-16
 * @version 1.0
 * @throws IOException 
 *
 */
function removeTr(trId,aId,index,anu){
	var nu = parseInt(index)-1;
	var i=parseInt(anu)-1;
	$("#"+trId+index).remove();
	$("#"+trId+nu).remove();
 	$('#'+aId+i).show();
	$('#j'+aId+i).show();
}

//添加临时助手	( index ==tdindex)
function add(tdId,index,buta,aindex){
		var i=parseInt(index)+1;
		var nu=parseInt(aindex)+1;
		var a=parseInt(index);
		if(!(a%6)){
			var l=a/6;
			var m=l-1;
			$("#trsia"+m).after("<tr id=\"trsia"+l+"\">"+
					"<td align=\"right\" style=\"background-color: #E0ECFF;width=10%\" id=\""+tdId+i+"\" class=\"changecolor\" >临时麻醉助手"+(nu+1)+"：</td>"+
					"<td width=15% id=\""+tdId+(i+1)+"\"><input id=\"thelpersia"+nu+"_\" name=\"opTempassist1List["+i+"]\" class=\"easyui-combogrid\" >"+
					"<a href=\"javascript:void(0)\" id=\""+buta+nu+"\" onclick=\"add('thsia','"+(i+1)+"','athsia','"+nu+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
					"<a id=\"j"+buta+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(i+1)+"','"+nu+"')\"></a>"+
					"</td> </tr>");
		}else{
			$("#"+tdId+index).after("<td align=\"right\" style=\"background-color: #E0ECFF;font-size:14px;width=15%\" id=\""+tdId+i+"\" class=\"changecolor\" >临时麻醉助手"+(nu+1)+"：</td>"+
					"<td style=\"width=18%\" id=\""+tdId+(i+1)+"\"><input id=\"thelpersia"+nu+"_\"  class=\"easyui-combogrid\" >"+
					"<a href=\"javascript:void(0)\" id=\""+buta+nu+"\" onclick=\"add('thsia','"+(i+1)+"','athsia','"+nu+"')\" class=\"easyui-linkbutton\" data-options=\"iconCls:'icon-add'\">"+
					"<a id=\"j"+buta+nu+"\" href=\"javascript:void(0)\" onclick=\"removeTr('thsia','athsia','"+(i+1)+"','"+nu+"')\"></a>"+
					"</td>");
		}
		$("#thelpersia"+nu+"_").combogrid({//麻醉临时助手1
			url : '<%=basePath %>operation/operationList/ssSysEmployeeListfy.action',
	 		idField : 'jobNo',
	 		textField : 'name',
	 		mode:"remote",
	 		panelAlign:'left',
	 		panelWidth:320,
	 		editable : true,
	 		pageList:[10,20,30,40,50],
			 pageSize:"10",
			 pagination:true,
		 	columns:[[   
					{field:'jobNo',title:'工作号',width:'130'},
		 	         {field:'name',title:'名称',width:'160'} 
		 	        
	 	     ]],  
	 	    onHidePanel:function(none){
		 	     var val = $(this).combogrid('getValue');
	 			var pull=$('#pulloutOpcd').combogrid("getValue");
	 			if(pull){
	 				if(pull==val){
	 					$(this).combogrid('clear');
				    	$.messager.alert("提示","临时助手与拔管人信息不能重复!","info");
				    	setTimeout(function(){
							$(".messager-body").window('close');
						},3000);
				    	return ;
		 	    	}
	 			}
	 	    	if(pullName(val)){
	 	    		$(this).combogrid('clear');
			    	$.messager.alert("提示","临时助手信息不能重复!","info");
			    	setTimeout(function(){
						$(".messager-body").window('close');
					},3000);
			    }
		     },
		    onLoadSuccess: function () {
		    	var id=$(this).prop("id");
		       if ($("#"+id).combogrid('getValue')&&nurseMap.get($("#"+id).combogrid('getValue'))) {
	            	$("#"+id).combogrid('setText',nurseMap.get($("#"+id).combogrid('getValue')));
	            } 
	        }  
		});
		$('#'+buta+nu).linkbutton({    
		    iconCls: 'icon-add'   
		});  
		$('#j'+buta+nu).linkbutton({    
		    iconCls: 'icon-remove'   
		}); 
		$('#'+buta+aindex).hide();
		$("#j"+buta+aindex).hide();
	}
	
	
//保存按钮
function save(){
	var operationno =  $('#operationno').val();
	if(operationno==""||operationno==null){
		$.messager.alert("操作提示","请选择一条手术申请记录");
		return;
	}
	var thelpersia="";
	var lszs=$('[id^=thelpersia]');
	var newMap=new Map();
	lszs.each(function(){
		var val=$(this).prop("id");
		var id=val.substring(val.indexOf("_")+1);
		var num=val.substring(10,11);
		var newValue=$(this).combogrid("getValue");
		var newText=$(this).combogrid("getText");
		newMap.put(newValue,newText);
		var oldValue=oldMap.get(newValue);
		if(!oldValue){
			if(newValue){
				thelpersia+=newValue+","+num+","+newText+"_add#";
			}
		}else{
			if(!newValue){
				thelpersia+= id+",_del#";//删除
			}else{
				if(oldValue!=newValue){
					thelpersia+= id+","+newValue+","+num+","+newText+"_upd#";//更新
				}
			}
		}
	});
	oldMap.each(function(key,value,index){
		if(newMap.get(key)==null||newMap.get(key)==""){
			thelpersia+=key+",_del#"//删除
		}
	});
	$('#editForm').form('submit',{  
    	url:"<%=basePath %>operation/anaerecord/saveOperationAnaerecord.action",  
    	onSubmit:function(param){
			if (!$('#editForm').form('validate')) {
				$.messager.progress('close');
				$.messager.show({
					title : '提示信息',
					msg : '验证没有通过,不能提交表单!'
				});
				return false;
			}
			$.messager.progress({text:'保存中，请稍后...',modal:true});
			param.thelper=thelpersia
    	},
        success:function(data){  
        	var res = eval("(" + data + ")");
        	$.messager.progress('close');
        	$.messager.alert("提示",res.resMsg);
        	if(res.resCode=='0'){
            	$("#list").datagrid("reload");
            	close();
        	}
        	
        }						         
	}); 
}
/**
 * 麻醉登记信息打印 hedong 2016-03-18
 */
function printMZDJ(){
	var operationno = $('#operationno').val();
	var timerStr = Math.random();
	if(operationno){
		window.open ("<c:url value='/iReport/iReportPrint/iReportToMZDJ.action?randomId='/>"+timerStr+"&tid="+operationno+"&fileName=MZDJ",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
	}else{
		$.messager.alert("提示","无法打印麻醉登记信息！请联系管理员！");
	}
	 
}


//临时助手信息的验证
function pullName(name){
	var mc = $('[id^=thelpersia]');
 	var b = 0;
	mc.each(function(index,obj){
		if($(obj).combogrid('getValue') == name&&$(obj).combogrid('getValue')!=""&&$(obj).combogrid('getValue')!=null){
			b++;
		}
	});
 	 return b>1?true:false;
}
        
/**
 * @Description:渲染麻醉方式
 * @Author: huangbiao
 * @CreateDate: 2016年4月12日
 * @param1:value:单元格的值
 * @param2:rowData：行数据
 * @param3:rowIndex:索引
 * @Modifier:
 * @ModifyDate:
 * @ModifyRmk:
 * @version: 1.0
 */
function funcaneWay(value){
	if(value!=null&&value!=""){
		return aneWayMap[value];
	}
	return value;
}
function sexRend(){
	var sex = "${operation.sex}";
	$('#sex').html(sexMap.get(sex));
}


//hedong 20170322 日期控件更改后  带有控件处直接取值后会多出毫秒数  2016-10-18 16:02:22.0 故增加id为tmpAnaeDate，tmpPulloutDate， tmpInpacuDate，tmpOutpacuDate 的隐藏域  通过截串的方式来处理
if($('#tmpAnaeDate').val()){
	$('#anaeDate').val($('#tmpAnaeDate').val().substring(0,19));
}
if($('#tmpPulloutDate').val()){
	$('#pulloutDate').val($('#tmpPulloutDate').val().substring(0,19));
}
if($('#tmpInpacuDate').val()){
	$('#inpacuDate').val($('#tmpInpacuDate').val().substring(0,19));
}
if($('#tmpOutpacuDate').val()){
	$('#outpacuDate').val($('#tmpOutpacuDate').val().substring(0,19));
}

//入室时间 hedong 20170322 日期控件选中时绑定的事件
function pickedToInDate(){
	var outpacuDate = $('#outpacuDate').val();
	var inpacuDate = $('#inpacuDate').val();
	if(outpacuDate!=null&&outpacuDate!=""){
		if(inpacuDate>outpacuDate){
			$.messager.alert("操作提示","入室时间不能大于出室时间","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3000);
			$('#inpacuDate').val("");
		}
	}
}
//出室时间 hedong 20170322 日期控件选中时绑定的事件
function pickedToOutDate(){
	var outpacuDate = $('#outpacuDate').val();
	var inpacuDate = $('#inpacuDate').val();
	if(inpacuDate!=null&&inpacuDate!=""){
		if(inpacuDate>outpacuDate){
			$.messager.alert("操作提示","出室时间不能小于出入室时间","info");
			setTimeout(function(){
				$(".messager-body").window('close');
			},3000);
			$('#outpacuDate').val("");
		}
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>