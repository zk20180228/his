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
		<div id="p" data-options="region:'west',split:true" style="width: 350px;min-width: 270px;border-top:0">
			<div id="cc" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north',border:false" align="center" style="height:40px;padding-top: 5px;">
		    	<table>
		    		<tr>
		    			<td>
		    				<input class="easyui-combobox" id="type" style="width: 100px">
		    				<input type="text"  id="medicalrecordIdSerc"  class="easyui-textbox" data-options="prompt: '床位号、病历号、姓名查询'"/>
		    			</td>
		    		</tr>
		    	</table>
		    </div>   
		    <div data-options="region:'center'" style="border-right:0;border-left:0">
		    	<ul id="tDt1" style="width:100%;height:100%;">正在加载，请稍后。。。</ul>
		    </div>   
		</div>
		</div>
		<div data-options="region:'center'" style="border-top:0">
			<div id="divLayout1" class="easyui-layout" data-options="fit:true">
			    <div data-options="region:'north',split:false,border:false" style="width:100%;height: 72px;padding:5px 5px 5px 5px">
			    	<table style="width: 100%" cellspacing="0" cellpadding="0">
			    		<tr>
			    			<td>
			    			<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchGrid()" class="easyui-linkbutton" iconCls="icon-search" style="margin-left:5px;margin-top:5px;">查询</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="${menuAlias}:function:print">
								<a href="javascript:void(0)" onclick="printPDf()" class="easyui-linkbutton" iconCls="icon-printer" style="margin-left:5px;margin-top:5px;">打印</a>
								</shiro:hasPermission>
							</td>
							<td style='text-align: right; padding-right: 5px'>
								<a href="javascript:void(0)" onclick="queryMidday(5)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">当年</a>
								<a href="javascript:void(0)" onclick="queryMidday(4)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">当月</a>
								<a href="javascript:void(0)" onclick="queryMidday(7)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">上月</a>
								<a href="javascript:void(0)" onclick="queryMidday(6)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">十五天</a>
								<a href="javascript:void(0)" onclick="queryMidday(3)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">七天</a>
								<a href="javascript:void(0)" onclick="queryMidday(2)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">三天</a>
			    				<a href="javascript:void(0)" onclick="queryMidday(1)" style="float: right;margin-left: 12px" class="easyui-linkbutton" iconCls="icon-date">当天</a>
			    			</td>
		    			</tr>
		    			<tr style="height:6px"></tr>
		    			<tr>
			    			<td style="text-align: right'; width:100%; height: 45px;padding:5px 5px 5px 5px ">
			    				日期：
								<input id="Stime"  name="Stime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="Etime"  name="Etime" class="Wdate" type="text" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								<input id="tradeName" style="width:170px;" class="easyui-textbox" data-options="prompt:'请输入药品名称回车查询'"/>
			    			</td>
			    		</tr>
			    	</table>
				</div>			
				<div data-options="region:'center',split:false,border:true" style="border-left:0">	
					<table id="billSearchHzList" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',pagination:true,pageSize:20,pageList:[20,40,60,80,100],rownumbers:true,fit:true,border:false">
						<thead>
							<tr>
								<th data-options="field:'patientName',width:'8%',align:'center' ">姓名</th>
								<th data-options="field:'drugCode',width:'12%',align:'center'">药品名称</th>
								<th data-options="field:'specs',width:'10%',align:'center'">规格</th>
								<th data-options="field:'dfqFreq',width:'10%',align:'center'">频次</th>
								<th data-options="field:'usageCode',width:'10%',align:'center'">用法</th>
								<th data-options="field:'applyNum',width:'5%',align:'center' ">总量</th>
								<th data-options="field:'doseUnit',width:'5%',align:'center',formatter:formattercodetype">单位</th>
								<th data-options="field:'drugDeptCode',formatter:functionDept,width:'8%',align:'center'">取药药房</th>
								<th data-options="field:'sendType',width:'6%',align:'center'">发送类型</th>
								<th data-options="field:'billclassCode',formatter:billClassFormatter,width:'8%',align:'center'">摆药单</th>
								<th data-options="field:'applyOpercode',formatter:functionEmp,width:'8%',align:'center'">发送人</th>
								<th data-options="field:'applyDate',width:'10%',align:'center'">发送时间</th>
								<th data-options="field:'printEmpl',formatter:functionEmp,width:'8%',align:'center' ">发药人</th>
								<th data-options="field:'printDate',width:'10%',align:'center'">发药时间</th>
								<th data-options="field:'validState',width:'5%',align:'center'">有效性</th>
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
	<input type="text" id="chexkBox">
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
	<script type="text/javascript">
	//单位渲染
	var codeTypeMap;
	var packUnitList;
	$(function(){
		$('#type').combobox({
		    data:[{"id":1,"text":"在院"},{"id":2,"text":"出院"},{"id":'12',"text":"全部"}],  
		    valueField:'id',    
		    textField:'text',
		    required:true,    
		    editable:true,
		    onLoadSuccess:function(none){
		    	$('#type').combobox('select','1');
		    },onSelect:function(record){
		    	queryTree();
		    }
		});
	  $('#Stime').val("${stime}");
	  $('#Etime').val("${etime}");
	  $('#billSearchHzList').datagrid({
		  onLoadSuccess : function(data){
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
	  })
	  $("#billSearchHzList").datagrid('loadData', { total: 0, rows: [] });
	  queryTree();
	})
		var inpa="";
		var billMap=new Map(); //摆药单
		var empMap = null;//人员
		var deptMap = new Map();  //部门
		
		//渲染科室
		$.ajax({
			url: "<%=basePath%>statistics/PatientDispensingOut/queryDeptMapPublic.action",				
			type:'post',
			success: function(deptData) {
				deptMap = deptData;
			}
		});	
		//渲染科室
		function functionDept(value,row,index){
			if(value!=null&&value!=''){
				return deptMap[value];
			}
		}
		$.ajax({
			url: "<%=basePath %>inpatient/deliveryDelivery/likeDrugPackagingunit.action",
			type:'post',
			success: function(packUnitdata) {
				packUnitList = packUnitdata ;
			}
		});
		//渲染人员
		$.ajax({
			url: "<%=basePath%>statistics/PatientDispensingOut/queryEmpMapPublic.action",				
			type:'post',
			success: function(empData) {
				empMap = empData;
			}
		});	
		//渲染人员
		function functionEmp(value,row,index){
			if(value!=null&&value!=''){
				return empMap[value];
			}
		}
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
		//摆药单的渲染
		function billClassFormatter(value,row,index){
			if(value!=null&&value!=""){	
				if(billMap[value]!=null&&billMap[value]!=""){
					return billMap[value];
				}
				return value;
			}
		}
		/**
		 *显示单位格式化
		 */
		function formattercodetype(value,row,index){
			if(value!=null){
				for(var i=0;i<packUnitList.length;i++){
					if(value==packUnitList[i].encode){
						return packUnitList[i].name;
					}
				}	
			}
		}
		$(function(){
			bindEnterEvent('medicalrecordIdSerc',searchFrom,'easyui');
			$('#tDt1').tree({
				onClick:function(node){
					var Stime = $('#Stime').val();
				    var Etime = $('#Etime').val();
				    var tradeName = $('#tradeName').textbox('getValue');
				    inpa = node.attributes.no;
				    $('#billSearchHzList').datagrid({
				    	url : "<%=basePath %>statistics/PatientDispensingOut/queryVinpatientApplyoutlist.action",
					    queryParams:{
						    inpatientNo: inpa,
							stime:Stime,
							etime:Etime,
							tradeName:tradeName,
							flag:node.attributes.pid
					   	}
					});
				}
			});
			bindEnterEvent('tradeName',searchGrid,'easyui');//绑定回车事件
		});
		function searchFrom() {	
			var searchText = $('#medicalrecordIdSerc').textbox('getText');
		    $("#tDt1").tree("search", searchText);
		}
		function queryTree(flag){
			var flag = $('#type').combobox('getValue');
			$('#tDt1').tree({ 
				   url:"<%=basePath%>statistics/PatientDispensingOut/InfoTree.action",
				   queryParams:{flag:flag},
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
					   var node = $('#tDt1').tree('find', 1);
					   if(node!=null){
						   $('#tDt1').tree('remove', node.target);
						   $('#tDt1').append("<span>正在加载，请稍后。。。</span>");
					   }
				   }
			});
		}
		function searchGrid(){
			var rows = $('#tDt1').tree('getSelected');
			if(rows == null){
				$.messager.alert('提示','请选择患者！');
				return false;
			}else{
				if(rows.id==1){
					$.messager.alert('提示','请选择患者！');
					return false;
				}
				var Stime = $('#Stime').val();
			    var Etime = $('#Etime').val();
			    if(Stime&&Etime){
				    if(Stime>Etime){
				      $.messager.alert("提示","开始时间不能大于结束时间！");
				      close_alert();
				      return ;
				    }
				  }
			    var tradeName = $('#tradeName').textbox('getText');
			    $('#billSearchHzList').datagrid({
			    	url : "<%=basePath %>statistics/PatientDispensingOut/queryVinpatientApplyoutlist.action",
					queryParams:{
						    inpatientNo: rows.id,
							stime:Stime,
							etime:Etime,
							tradeName:tradeName,
							flag:rows.attributes.pid
					}
				});
			}
		} 
		//按时间段查询
		function queryMidday(val){
			if(val==1){
				var myDate = new Date();
				var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				var date=myDate.getDate();
				month=month<10?"0"+month:month;
				date=date<10?"0"+date:date;
				var day=year+"-"+month+"-"+date;
				var Stime = $('#Stime').val(day);
			    var Etime = $('#Etime').val(day);
			    searchGrid();
			}else if(val==2){
				var nowd = new Date();
				var myDate=new Date(nowd.getTime() - 3 * 24 * 3600 * 1000);
				var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				var date=myDate.getDate();
				month=month<10?"0"+month:month;
				date=date<10?"0"+date:date;
				var day2=year+"-"+month+"-"+date;
				var Stime = $('#Stime').val(day2);
				 nowd = new Date();
				 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
			  	month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day2=year+"-"+month+"-"+date;
			    var Etime = $('#Etime').val(day2);
			    searchGrid();
			}else if(val==3){
				var nowd = new Date();
				var myDate=new Date(nowd.getTime() - 7 * 24 * 3600 * 1000);
				var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				var date=myDate.getDate();
				month=month<10?"0"+month:month;
				date=date<10?"0"+date:date;
				var day3=year+"-"+month+"-"+date;
				var Stime = $('#Stime').val(day3);
				 nowd = new Date();
				 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
			  	month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day3=year+"-"+month+"-"+date;
			    var Etime = $('#Etime').val(day3);
			    searchGrid();
			}else if(val==4){
				var myDate = new Date();
				var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				month=month<10?"0"+month:month;
				var day=year+"-"+month+"-"+"01";
				var Stime = $('#Stime').val(day);
				myDate = new Date();
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
				 month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day=year+"-"+month+"-"+date;
			    var Etime = $('#Etime').val(day);
			    searchGrid();
			}else if(val==5){
				var myDate = new Date();
				var year=myDate.getFullYear();
				var day=year+"-"+"01"+"-"+"01";
				var Stime = $('#Stime').val(day);
				myDate = new Date();
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
				 month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day=year+"-"+month+"-"+date;
			    var Etime = $('#Etime').val(day);
				searchGrid();
			}else if(val==6){
				var nowd = new Date();
				var myDate=new Date(nowd.getTime() - 15 * 24 * 3600 * 1000);
				var year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				var month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				var date=myDate.getDate();
				month=month<10?"0"+month:month;
				date=date<10?"0"+date:date;
				var day2=year+"-"+month+"-"+date;
				var Stime = $('#Stime').val(day2);
				 nowd = new Date();
				 myDate=new Date(nowd.getTime() - 1 * 24 * 3600 * 1000);
				 year=myDate.getFullYear(); //获取完整的年份(4位,1970-????)
				 month=myDate.getMonth()+1; //获取当前月份(0-11,0代表1月)
				 date=myDate.getDate();
			  	month=month<10?"0"+month:month;
				 date=date<10?"0"+date:date;
				 day2=year+"-"+month+"-"+date;
			    var Etime = $('#Etime').val(day2);
			    searchGrid();
			}else if(val==7){
				var date=new Date();
				var year=date.getFullYear();
				var month=date.getMonth();
				if(month==0){
					year=year-1;
					month=12;
				}
				var startTime=year+'-'+month+'-01';
				 $('#Stime').val(startTime);
				var date=new Date(year,month,0);
				var endTime=year+'-'+month+'-'+date.getDate();
				$('#Etime').val(endTime);
				 searchGrid();
			}
			
		}
		/**打印文档**/
		function printPDf(){
			var rowCount=$('#billSearchHzList').datagrid('getRows');
			if(''!=rowCount&&null!=rowCount){
				$.messager.confirm('确认','是否打印?',function(res){
					if(res){
						var rows = $('#tDt1').tree('getSelected');
						var Stime = $('#Stime').val();
					    var Etime = $('#Etime').val();
					    var tradeName = encodeURIComponent(encodeURIComponent($('#tradeName').textbox('getText')));
					    window.open ("<c:url value='queryVinpatientApplyoutlistPDf.action?inpatientNo='/>"+rows.id+"&stime="+Stime+"&etime="+Etime+"&tradeName="+tradeName+"&flag="+rows.attributes.pid,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
					}
				});
			}else{
				$.messager.alert('提示','列表中没有数据,不能提供打印功能！')
			}
		}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>