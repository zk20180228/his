	  <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病区摆药查询</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
		<div id="cc" data-options="region:'west',border:false" style="width:350px">
			 <div id="ttl" class="easyui-tabs" data-options="fit:true">					
				  <div title="未摆药患者" data-options="fit:true">
				  	<div id="divLayout2" class="easyui-layout" data-options="fit:true" style="width:350px">												  
					  <div data-options="region:'north',border:false" align="center" style="height:73px;padding-top: 5px;" >
					    	<table>
					    		<tr>
					    			<td >
 										<input class="easyui-combobox" id="pw" style="width: 100px">
					    				<input type="text" id="medicalrecordIdSerc"  class="easyui-textbox" data-options="prompt: '床位号、病历号、姓名查询'"/>
					    			</td>
					    		</tr>
					    		<tr style="height: 5px;"></tr>
					    		<tr>
					    		   <td>
					    		   <shiro:hasPermission name="${menuAlias}:function:query">
					    			<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		    						</shiro:hasPermission>
									<shiro:hasPermission name="${menuAlias}:function:print">
		    						<a href="javascript:void(0)" onclick="adPrintAdvice()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
					    			 </shiro:hasPermission>
					    		   </td>
					    		</tr>
					    	</table>
					    </div>   
					      <div data-options="region:'center'" style="border-right:0;width:350px">
					    	<ul id="tDt1" style="width:100%;height:100%;">正在加载，请稍后。。。</ul>
					    </div> 
					  </div>
				  </div>
				  <div title="已摆药患者" data-options="fit:true">
					  <div id="divLayout3" class="easyui-layout" data-options="fit:true">	
					  <div data-options="region:'north',border:false" align="center" style="height:73px;padding-top: 5px;">
					    	<table>
					    		<tr>
					    			<td >
 										<input class="easyui-combobox" id="py" style="width: 100px">
					    				<input type="text"  id="medicalrecordIdSercs"  class="easyui-textbox" data-options="prompt: '床位号、病历号、姓名查询'"/>
					    			</td>
					    		</tr>
					    		<tr style="height: 5px;"></tr>
					    		<tr>
					    		   <td>
					    			<a   href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		    						<a   href="javascript:void(0)" onclick="adPrintAdvice()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
					    		   </td>
					    		</tr>
					    	</table>
					    </div>   
					    <div data-options="region:'center'" style="border-right:0">
					    	<ul id="tDt2" style="width:100%;height:100%;">正在加载，请稍后。。。</ul>
					    </div>
					   </div>   
				  </div>						
			 </div>				
		</div>
		<div data-options="region:'center'" style="border-top:0">
			<div id="divLayout1" class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north',split:false,border:false" style="width:100%;height: 68px;padding-top:0px"> 
					<table style="width:100%" cellspacing="0" cellpadding="0" border="0" data-options="fit:true">
						<tr>
							<td  style="width:100%;padding-left:10px;padding-right:10px;padding-top:5px;" colspan="3" >
								<a href="javascript:searchData(1);void(0)" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:2px;margin-top:0px;margin-bottom: 7px">查询</a>
								<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset" style="margin-left:3px;margin-top:0px;margin-bottom: 7px">重置</a>
								<a href="javascript:searchData(4);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当年</a>
								<a href="javascript:searchData(5);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当月</a>
								<a href="javascript:searchData(8);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">上月</a>
								<a href="javascript:searchData(7);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">十五天</a>
								<a href="javascript:searchData(3);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">七天</a>
								<a href="javascript:searchData(2);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">三天</a>
								<a href="javascript:searchData(6);void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-date'" style="float: right;margin-left: 10px">当天</a>
							</td>															
						</tr>
						<tr>
							<td style="padding-left:10px" class="nurseDrugDispensSize"><span id="seTitle">日期：</span>
							<span id="seTime">
								<input id="startTime" name="startTime" class="Wdate" value="${startTime}" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="endTime" class="Wdate" type="text" onClick="WdatePicker()" value="${endTime }" style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</span>
							&nbsp;&nbsp;药品：
								<input id="deptId" type="hidden" value="${deptId}">		
								<input type="text" id="medicalSerc" style="width:160px;" class="easyui-textbox" data-options="prompt:'输入药品名称、名称拼音码、名称五笔码、名称自定义码/回车查询'" />	
							</td>
						</tr>
					</table>
				</div>							
				<div data-options="region:'center',split:false,border:false">				
					<div id="tt" class="easyui-tabs" data-options="fit:true,border:false">   
						<div title="摆药汇总" style="padding:0px" > 
							<jsp:include page="drugDispensSum.jsp"></jsp:include>											
						</div>
						<div title="摆药明细" style="padding:0px">
							<jsp:include page="drugDispensDetail.jsp"></jsp:include>
						</div>					
		    		</div>																				
				</div>
			</div>
		</div>
</div>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
var drugpackagingunitMaps = "";//药品单位Map
var billMap=new Map(); //摆药单
var jixing=new Map();//剂型渲染
//查询药品单位
$.ajax({
	url: "<%=basePath%>inpatient/docAdvManage/queryDrugpackagingunit.action",				
	type:'post',
	success: function(drugpackagingunitdata) {					
		drugpackagingunitMaps = drugpackagingunitdata;										
	}
});

//查询摆药单
$.ajax({
	url: "<%=basePath%>statistics/NurseDrugDispens/queryBillNameList.action",				
	type:'post',
	success: function(data) {					
		if(data!=null&&data!=""){
			billMap=data;
		}										
	}
});
//剂型渲染
$.ajax({
	url: "<%=basePath%>/baseinfo/pubCodeMaintain/queryDictionaryMap.action",				
	type:'post',
	data:{type:'dosageForm'},
	success: function(data) {					
		if(data!=null&&data!=""){
			jixing=data;
		}										
	}
});
function clear(){
	$('#startTime').val("${startTime}");
	$('#endTime').val("${endTime }");
	$('#deptId').val("");
	$('#medicalSerc').textbox('setValue',"");
	searchData(1);
}
//摆药单的渲染
function billClassFormatter(value,row,index){
	if(value!=null&&value!=""){	
		if(billMap[value]!=null&&billMap[value]!=""){
			return billMap[value];
		}
		return value;
	}
}
//单位 列表页 显示	
function drugUnitFamaters(value,row,index){	
	if(value!=null&&value!=""){					
		if(drugpackagingunitMaps[value]!=null&&drugpackagingunitMaps[value]!=""){
			return drugpackagingunitMaps[value];
		}
		return value;				
	}			
}
function sendTimes(){
	var qq = $('#ttl').tabs('getSelected');				
	var tab = qq.panel('options');
	if(tab.title=='未摆药患者'){
		$('#seTitle').show();
		$('#sendTime').show();
		$('#weiFont').show();
		$('#yiFont').hide();
		$('#weiFonts').show();
		$('#yiFonts').hide();
	}else if(tab.title=='已摆药患者'){
		$('#seTitle').show();
		$('#sendTime').show();
		$('#weiFont').hide();
		$('#yiFont').show();
		$('#weiFonts').hide();
		$('#yiFonts').show();
	}
	$('#ttl').tabs({
	    border:false,
	    onSelect:function(title,index){
	    	var qq = $('#ttl').tabs('getSelected');				
	    	var tab = qq.panel('options');
	    	if(tab.title=='未摆药患者'){
	    		$('#seTitle').show();
	    		$('#sendTime').show();
	    		$('#weiFont').show();
	    		$('#yiFont').hide();
	    		$('#weiFonts').show();
	    		$('#yiFonts').hide();
	    	}else if(tab.title=='已摆药患者'){
	    		$('#seTitle').show();
	    		$('#sendTime').show();
	    		$('#weiFont').hide();
	    		$('#yiFont').show();
	    		$('#weiFonts').hide();
	    		$('#yiFonts').show();
    		    var qq = $('#tt').tabs('getSelected');				
				var tab = qq.panel('options');
				  var zy3=$('#py').combobox("getValue");
				  if(zy3=='1'){
					  if(tab.title=='摆药汇总'){
							 var node = $('#tDt2').tree('find', 1);
							   $('#tDt2').tree('check', node.target);
							   searchData(1);
						}
				  }
				
	    	}
	    }
	});
}
$(function(){
	//选项卡选中事件
	$('#tt').tabs({    
    border:false,    
    onSelect:function(title){
    	var nodes = $('#tDt1').tree('getChecked');
    	if(nodes==''||nodes==null){
    		return;
    	}else{
    		var startTime = $('#startTime').val();
    		var endTime = $('#endTime').val();
    		if(startTime!=null&&startTime!=""){
    			  startTime=startTime+" 00:00:00";
    			  }
    		  if(endTime!=null&&endTime!=""){
    			  endTime=endTime+" 23:59:59";
    		      }
    		  var drugName=$('#medicalSerc').val();	
    		var inpatientNo='';
    		var inpatientno='';
    		for(var i=0;i<nodes.length;i++){
    			if(nodes[0].text=='本区患者'){
    				var node=nodes[0];
    				if(i<nodes.length-1){
    					inpatientno=node.children[i].attributes.no;
    				}				
    			}else{
    				inpatientno=nodes[i].attributes.no;
    			}
    			if(i==0 && nodes.length>1){
    				inpatientNo += inpatientno+",";
    			}else if(i>0 && i<nodes.length-1){
    				inpatientNo += inpatientno+",";
    			}else if(i>0 && i==nodes.length-1){
    				inpatientNo += inpatientno;
    			}else if(i==0 && nodes.length==1){
    				inpatientNo += inpatientno;
    			}					
    		}
    		if(title=='摆药汇总'){
    			$('#drugDispensSumList').datagrid({			
    				url:'<%=basePath%>statistics/NurseDrugDispens/queryDrugDispensSum.action',	
    				queryParams: {
    					'inpatientInfo.inpatientNo':inpatientNo,
    					drugName:drugName,
    					startTime:startTime,
    					endTime:endTime,
    					type:"0"
    				},
    			});
    		}else if(title=='摆药明细'){
    			$('#drugDispensDetailList').datagrid({			
    				url:'<%=basePath%>statistics/NurseDrugDispens/queryDrugDispensDetail.action',
    				pagination:true,
    				fitColumns:true,
    				fit:true,
    				queryParams: {
    					'inpatientInfo.inpatientNo':inpatientNo,
    					drugName:drugName,
    					startTime:startTime,
    					endTime:endTime,
    					type:"0"
    				},
    			});
    		}
    	}
    }    
});
	$('#pw').combobox({
	    data:[{"id":1,"text":"在院"},{"id":0,"text":"出院"},{"id":'12',"text":"全部"}],  
	    valueField:'id',    
	    textField:'text',
	    required:true,    
	    editable:true,
	    onLoadSuccess:function(none){
	    	var val = $(this).combobox('getData');  
            for (var item in val[0]) {  
                if (item =='id') {  
                    $(this).combobox('select', val[0][item]);  
                }  
            }  
	    }
	});
	    $('#py').combobox({
		    data:[{"id":1,"text":"在院"},{"id":0,"text":"出院"},{"id":'12',"text":"全部"}],  
		    valueField:'id',    
		    textField:'text',
		    required:true,    
		    editable:true,
		    onLoadSuccess:function(none){
		    	var val = $(this).combobox('getData');  
                for (var item in val[0]) {  
                    if (item =='id') {  
                        $(this).combobox('select', val[0][item]);  
                    }  
                }  
		    }
	    });
	sendTimes();
   var pw=$("#pw").combobox('getValue');
   var py=$("#py").combobox('getValue');
	if(pw=="1"){
		loadtree(1);
         }  

	if(py=="1"){
		loadtree1(1);
	}
    $('#pw').combobox({
	onChange:function(newValue,oldValue){
	if(newValue=="12"){
		loadtree(12);
	}else if(newValue=="0"){
		loadtree(2);
	}else if(newValue=="1"){
    	loadtree(1);
	}
	}
});
$('#py').combobox({
	onChange:function(newValue,oldValue){
	if(newValue=="12"){
			loadtree1(12);
	}else if(newValue=="0"){
			loadtree1(2);
	}else if(newValue=="1"){
    	    loadtree1(1);
	}
	}
});
	bindEnterEvent('medicalSerc',searchData,'easyui');
	bindEnterEvent('medicalrecordIdSerc',searchFrom,'easyui');
	bindEnterEvent('medicalrecordIdSercs',searchData,'easyui');
});


function loadtree(flag){
	$('#tDt1').tree({ 
		   url:"<%=basePath%>statistics/NurseDrugDispens/nurseChargeTree.action",
		   queryParams:{type:"0",a:flag},
		   onlyLeafCheck:false,
	  	   checkbox:true,
		   method:'get',
		   animate:true,  //点在展开或折叠的时候是否显示动画效果
		   lines:true,    //是否显示树控件上的虚线
		   formatter:function(node){//统计节点总数
			  var s = node.text;
			  if (node.children){
				  if (node.id==1){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				  }
				 }  
			  return s;
		   },
		   onBeforeLoad:function(node, param){
			   var node = $('#tDt1').tree('find', 1);
			   if(node!=null){
				   $('#tDt1').tree('remove', node.target);
				   $('#tDt1').append("<span>正在加载，请稍后。。。</span>");
			   }
		   },
		   onLoadSuccess:function(node,data){
			   var pw=$('#pw').combobox("getValue");
			   var qq = $('#tt').tabs('getSelected');				
				var tab = qq.panel('options');
				if(pw=='1'){
					if(tab.title=='摆药汇总'){
						 var node = $('#tDt1').tree('find', 1);
						   $('#tDt1').tree('check', node.target);
						   if(node.children.length!="0"){
							   searchData(1);
						   }
					}
				}
					
					
		   },
		   onClick:function(node){
			   var nodes = $('#tDt1').tree('getChecked');
			   for(var i in nodes){
				   if(nodes[i].id==node.id){
					   $('#tDt1').tree('uncheck',node.target);
					   return;
				   }
			   }
			   $('#tDt1').tree('check',node.target);
		   }
	});
}
function loadtree1(flag){
	$('#tDt2').tree({ 
		   url:"<%=basePath%>statistics/NurseDrugDispens/nurseChargeTree.action",
		   queryParams:{type:"1",a:flag},
		   onlyLeafCheck:false,
	  	   checkbox:true,
		   method:'get',
		   animate:true,  //点在展开或折叠的时候是否显示动画效果
		   lines:true,    //是否显示树控件上的虚线
		   formatter:function(node){//统计节点总数
			  var s = node.text;
			  if (node.children){
				if(node.id==1){
					s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
				}  
			  }
			  return s;
		   },
		   onBeforeLoad:function(node, param){
			   var node = $('#tDt2').tree('find', 1);
			   if(node!=null){
				   $('#tDt2').tree('remove', node.target);
				   $('#tDt2').append("<span>正在加载，请稍后。。。</span>");
			   }
		   },
		   onClick:function(node){
			   var nodes = $('#tDt2').tree('getChecked');
			   for(var i in nodes){
				   if(nodes[i].id==node.id){
					   $('#tDt2').tree('uncheck',node.target);
					   return;
				   }
			   }
			   $('#tDt2').tree('check',node.target);
		   }
		  
	});
}
function searchFrom() {	
	var qq = $('#ttl').tabs('getSelected');		
	var tab = qq.panel('options');
	if(tab.title=='未摆药患者'){
		var searchText = $('#medicalrecordIdSerc').textbox('getText');
		$('#tDt1').tree('search', searchText);

		
	}else if(tab.title=='已摆药患者'){
		var searchText = $('#medicalrecordIdSercs').textbox('getText');
		 $('#tDt2').tree('search', searchText);
	}
	
   
}

//查询信息
function searchData(flag){
	var startTime;//开始时间
	var endTime;//结束时间
	var year;
	var month;
	var day;
 if(flag==1){
	 startTime = $('#startTime').val();
		endTime = $('#endTime').val();
		if(startTime!=null&&startTime!=""){
			startTime=startTime+" 00:00:00";
		  }
	    if(endTime!=null&&endTime!=""){
	      endTime=endTime+" 23:59:59";
	      }
 }else{
	  var date=new Date();
	  year=date.getFullYear();
	  month=date.getMonth()+1;
	  day=date.getDate();
	  month=month<10?"0"+month:month;
	  day=day<10?"0"+day:day;
	  endTime=year+'-'+month+'-'+day;
	  if(flag==2){
		  var lon=date.getTime()-1000*3600*24*3;
		  startTime=new Date(lon);
		  year=startTime.getFullYear();
		  month=startTime.getMonth()+1;
		  day=startTime.getDate();
		  month=month<10?"0"+month:month;
		  day=day<10?"0"+day:day;
		  startTime=year+'-'+month+'-'+day; 
		  lon=date.getTime()-1000*3600*24*1;
		  endTime=new Date(lon);
		  year=endTime.getFullYear();
		  month=endTime.getMonth()+1;
		  day=endTime.getDate();
		  month=month<10?"0"+month:month;
		  day=day<10?"0"+day:day;
		  endTime=year+'-'+month+'-'+day; 
	  }else if(flag==3){
		  var lon=date.getTime()-1000*3600*24*7;
		  startTime=new Date(lon);
		  year=startTime.getFullYear();
		  month=startTime.getMonth()+1;
		  day=startTime.getDate();
		  month=month<10?"0"+month:month;
		  day=day<10?"0"+day:day;
		  startTime=year+'-'+month+'-'+day; 
		  lon=date.getTime()-1000*3600*24*1;
		  endTime=new Date(lon);
		  year=endTime.getFullYear();
		  month=endTime.getMonth()+1;
		  day=endTime.getDate();
		  month=month<10?"0"+month:month;
		  day=day<10?"0"+day:day;
		  endTime=year+'-'+month+'-'+day;
	  }else if(flag==4){
		  startTime=(date.getFullYear())+'-01-01';
	  }else if(flag==5){
		  startTime=year+'-'+month+'-01'
	  }else if(flag==7){
			  var lon=date.getTime()-1000*3600*24*15;
			  startTime=new Date(lon);
			  year=startTime.getFullYear();
			  month=startTime.getMonth()+1;
			  day=startTime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  startTime=year+'-'+month+'-'+day; 
			  lon=date.getTime()-1000*3600*24*1;
			  endTime=new Date(lon);
			  year=endTime.getFullYear();
			  month=endTime.getMonth()+1;
			  day=endTime.getDate();
			  month=month<10?"0"+month:month;
			  day=day<10?"0"+day:day;
			  endTime=year+'-'+month+'-'+day;
	  }else if(flag==8){
			var date=new Date();
			var year=date.getFullYear();
			var month=date.getMonth();
			if(month==0){
				year=year-1;
				month=12;
			}
			var startTime=year+'-'+month+'-01';
			$('#startTime').val(startTime);
			Stime= startTime;
			var date=new Date(year,month,0);
			var endTime=year+'-'+month+'-'+date.getDate();
			$('#endTime').val(endTime);
			Etime=endTime;
		}else if(flag==6){
		  startTime=endTime;
	  }else if(flag==null||flag==""){
		   startTime = $('#startTime').val();
			endTime = $('#endTime').val();
	  }
	  $('#startTime').val(startTime);
	  $('#endTime').val(endTime);
	  if(startTime!=null&&startTime!=""){
		  startTime=startTime+" 00:00:00";
		  }
	  if(endTime!=null&&endTime!=""){
		  endTime=endTime+" 23:59:59";
	      }
		 }
	var drugName=$('#medicalSerc').val();
	if(startTime=='' && endTime!=''){
		$.messager.alert('操作提示', '开始时间不能为空！');
		return;
	}else if(endTime=='' && startTime!=''){
		$.messager.alert('操作提示', '结束时间不能为空！');
		return;
	}else if(endTime!='' && startTime!=''){
		var sDate = new Date(startTime.replace(/\-/g, "\/"));  
		var eDate = new Date(endTime.replace(/\-/g, "\/"));  
		if(sDate>eDate){						
			$.messager.alert('操作提示', '开始时间不能大于结束时间！');
			return;
		}
	}
	var qq = $('#ttl').tabs('getSelected');				
	var tab = qq.panel('options');
	if(tab.title=='未摆药患者'){
		var nodes = $('#tDt1').tree('getChecked');
		if(nodes.length==0){
			$.messager.alert('操作提示', '请先选择患者！');
			return;
		}
		var inpatientNo='';
		var inpatientno='';
		for(var i=0;i<nodes.length;i++){
			if(nodes[0].text=='本区患者'){
				var node=nodes[0];
				if(i<nodes.length-1){
					inpatientno=node.children[i].attributes.no;
				}				
			}else{
				inpatientno=nodes[i].attributes.no;
			}
			if(i==0 && nodes.length>1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i<nodes.length-1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i==nodes.length-1){
				inpatientNo += inpatientno;
			}else if(i==0 && nodes.length==1){
				inpatientNo += inpatientno;
			}					
		}
		var qq = $('#tt').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='摆药汇总'){
			$('#drugDispensSumList').datagrid({			
				url:'<%=basePath%>statistics/NurseDrugDispens/queryDrugDispensSum.action',	
				queryParams: {
					'inpatientInfo.inpatientNo':inpatientNo,
					drugName:drugName,
					startTime:startTime,
					endTime:endTime,
					type:"0"
				},
			});
		}else if(tab.title=='摆药明细'){
			$('#drugDispensDetailList').datagrid({			
				url:'<%=basePath%>statistics/NurseDrugDispens/queryDrugDispensDetail.action',
				pagination:true,
				fitColumns:true,
				fit:true,
				queryParams: {
					'inpatientInfo.inpatientNo':inpatientNo,
					drugName:drugName,
					startTime:startTime,
					endTime:endTime,
					type:"0"
				},
			});
		}		
	}else if(tab.title=='已摆药患者'){
		var nodes = $('#tDt2').tree('getChecked');
		if(nodes.length==0){
			$.messager.alert('操作提示', '请先选择患者！');
			return;
		}
		
		var inpatientNo='';
		var inpatientno='';
		for(var i=0;i<nodes.length;i++){
			if(nodes[0].text=='本区患者'){
				var node=nodes[0];
				if(i<nodes.length-1){
					inpatientno=node.children[i].attributes.no;
				}				
			}else{
				inpatientno=nodes[i].attributes.no;
			}
			if(i==0 && nodes.length>1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i<nodes.length-1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i==nodes.length-1){
				inpatientNo += inpatientno;
			}else if(i==0 && nodes.length==1){
				inpatientNo += inpatientno;
			}					
		}
		var qq = $('#tt').tabs('getSelected');	
		var tab = qq.panel('options');
		if(tab.title=='摆药汇总'){
			$('#drugDispensSumList').datagrid({			
				url:'<%=basePath%>statistics/NurseDrugDispens/queryDrugDispensSum.action',	
				queryParams: {
					'inpatientInfo.inpatientNo':inpatientNo,
					drugName:drugName,
					startTime:startTime,
					endTime:endTime,
					type:"1"
				},
			});
		}else if(tab.title=='摆药明细'){
			$('#drugDispensDetailList').datagrid({			
				url:'<%=basePath%>statistics/NurseDrugDispens/queryDrugDispensDetail.action',	
				pagination:true,
				fitColumns:true,
				fit:true,
				queryParams: {
					'inpatientInfo.inpatientNo':inpatientNo,
					drugName:drugName,
					startTime:startTime,
					endTime:endTime,
					type:"1"
					
				},
			});
		}
	}
}


/**
 * 打印
 */
function adPrintAdvice(){
	var qq = $('#ttl').tabs('getSelected');				
	var tab = qq.panel('options');
	var startTime = $('#startTime').val();
    var	endTime = $('#endTime').val();
    var drugName=$('#medicalSerc').val();
	if(tab.title=='未摆药患者'){
		var nodes = $('#tDt1').tree('getChecked');
		if(nodes.length==0){
			$.messager.alert('操作提示', '请先选择患者！');
			return;
		}
		var inpatientNo='';
		var inpatientno='';
		for(var i=0;i<nodes.length;i++){
			if(nodes[0].text=='本区患者'){
				var node=nodes[0];
				if(i<nodes.length-1){
					inpatientno=node.children[i].attributes.no;
				}				
			}else{
				inpatientno=nodes[i].attributes.no;
			}
			if(i==0 && nodes.length>1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i<nodes.length-1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i==nodes.length-1){
				inpatientNo += inpatientno;
			}else if(i==0 && nodes.length==1){
				inpatientNo += inpatientno;
			}					
		}
		var qq = $('#tt').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='摆药汇总'){
			var timerStr = Math.random();
			window.open ("<c:url value='/statistics/NurseDrugDispens/printDrugDispensSum.action?randomId='/>"+timerStr+"&inpatientInfo.inpatientNo="+inpatientNo+"&startTime="+startTime+"&endTime="+endTime+"&drugName="+drugName+"&type=0",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}else if(tab.title=='摆药明细'){
			var timerStr = Math.random();
			window.open ("<c:url value='/statistics/NurseDrugDispens/printDrugDispensDetail.action?randomId='/>"+timerStr+"&inpatientInfo.inpatientNo="+inpatientNo+"&startTime="+startTime+"&endTime="+endTime+"&drugName="+drugName+"&type=0",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}		
	}else if(tab.title=='已摆药患者'){
		var nodes = $('#tDt2').tree('getChecked');
		if(nodes.length==0){
			$.messager.alert('操作提示', '请先选择患者！');
			return;
		}
		
		var inpatientNo='';
		var inpatientno='';
		for(var i=0;i<nodes.length;i++){
			if(nodes[0].text=='本区患者'){
				var node=nodes[0];
				if(i<nodes.length-1){
					inpatientno=node.children[i].attributes.no;
				}				
			}else{
				inpatientno=nodes[i].attributes.no;
			}
			if(i==0 && nodes.length>1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i<nodes.length-1){
				inpatientNo += inpatientno+",";
			}else if(i>0 && i==nodes.length-1){
				inpatientNo += inpatientno;
			}else if(i==0 && nodes.length==1){
				inpatientNo += inpatientno;
			}					
		}
		var qq = $('#tt').tabs('getSelected');				
		var tab = qq.panel('options');
		if(tab.title=='摆药汇总'){
			var timerStr = Math.random();
			window.open ("<c:url value='/statistics/NurseDrugDispens/printDrugDispensSum.action?randomId='/>"+timerStr+"&inpatientInfo.inpatientNo="+inpatientNo+"&startTime="+startTime+"&endTime="+endTime+"&drugName="+drugName+"&type=1",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}else if(tab.title=='摆药明细'){
			var timerStr = Math.random();
			window.open ("<c:url value='/statistics/NurseDrugDispens/printDrugDispensDetail.action?randomId='/>"+timerStr+"&inpatientInfo.inpatientNo="+inpatientNo+"&startTime="+startTime+"&endTime="+endTime+"&drugName="+drugName+"&type=1",'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}
	}
}
//剂型渲染
function jixingfunction(value,row,index){
	if(''!=value){
		return jixing[value];
	}
}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>