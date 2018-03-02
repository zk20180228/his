<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出院患者医嘱执行单查询</title>
</head> 	
<body style="margin: 0px;padding: 1px;">
<div class="easyui-layout" style="width:100%;height:100%;" data-options="fit:true">
    <div style="width:15%;height:100%;padding-top: 10px;" data-options="region:'west',border:true">
       <table>
		    <tr>
		    	<td>
		    		&nbsp;患者姓名:<input type="text"  id="findByName"  class="easyui-textbox" data-options="prompt:'根据姓名查询'"/>
				</td>
		   	</tr>
		   	<tr style="height: 17px;"></tr>
	  </table>
       <ul id="inpatientTree" style="width:100%;">正在加载...</ul>
   </div>
	<div style="width:85%;height:32px;padding:4px 1px 1px 1px;" data-options="region:'north',border:false">
	    &nbsp;病区：<select id="dept" class="easyui-combobox" data-options="required:true,valueField:'id',textField:'text',width:180" >
	              </select>
	              <a href="javascript:clear();"  class="easyui-linkbutton" data-options="iconCls:'icon-opera_clear',plain:true"></a>
		&nbsp;时间:
		<input id="start" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:onclickDate})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
		 至
		<input id="end" class="Wdate" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'start\')}',onpicked:onclickDate})" style="width:170px;border: 1px solid #95b8e7;border-radius: 5px;"/>
	</div>
	<div data-options="region:'center',border:false,title:'医嘱执行单明细列表',iconCls:'icon-book',split:true">
			<div id="ttt" class="easyui-tabs" data-options="fit:true,tabPosition:'bottom'">
			</div>
	</div>   
</div>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/js/easyui-combobox-filterLocal.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
var date=new Date();
var endTime=date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();//医嘱开立时间
var startTime=date.getFullYear()+'-'+(date.getMonth()+1)+'-01 00:00:00';
endTime=endTime.replace(/-(\d{1}\b)/g,'-0$1');
startTime=startTime.replace(/-(\d{1}\b)/g,'-0$1');
	$(function(){
		$('#start').val(startTime);
		$('#end').val(endTime);
		$('#dept').combobox({    
		    url:'<%=basePath%>statistics/outExecution/queryDeptList.action',    
		    valueField:'deptCode',    
		     textField:'deptName',
		    onLoadSuccess: function () { //加载完成后,设置选中第一项  
                     var val = $(this).combobox('getData'); 
                     for (var item in val[0]) {  
                         if (item == 'deptCode') {  
                             $(this).combobox('select', val[0][item]);  
                         }  
                     }  
                },onHidePanel:function(none){
                	    var data = $(this).combobox('getData');
                	    var val = $(this).combobox('getValue');
                	    var result = true;
                	    for (var i = 0; i < data.length; i++) {
                	        if (val == data[i].deptCode) {
                	            result = false;
                	        }
                	    }
                	    if (result) {
                	        $(this).combobox("clear");
                	    }else{
//                 	        $(this).combobox('unselect',val);
//                 	        $(this).combobox('select',val);
                	    }
                	},
                	filter: function(q, row){
                	    var keys = new Array();
                	    keys[keys.length] = 'deptCode';
                	    keys[keys.length] = 'deptName';
                	    keys[keys.length] = 'pinyin';
                	    keys[keys.length] = 'wb';
                	    keys[keys.length] = 'inputCode';
                	    return filterLocalCombobox(q, row, keys);
                	},
                 onSelect:function(record){
             	    //获取选中值
               	    var deptCode =$('#dept').combobox('getValue');
               	 		treeReload(deptCode);//重新加载
              	       $.post("<%=basePath%>statistics/outExecution/list2.action",{deptCode:deptCode}, function (data) { 
             		        var tabs = $('#ttt').tabs('tabs');  
                      		for(var i=tabs.length-1;i>=0;i--){
                                $('#ttt').tabs('close',i);
                            } 
                            for(var i=0;i<data.length;i++){
                            	if(i==0){
                                	$('#ttt').tabs('add',{
                                         title:data[i].billName,
                                         id:"tab_"+data[i].billNo
                                    });   
                                }else{
                                    $('#ttt').tabs('add',{
	                  	    			title:data[i].billName,
	                                    id:"tab_"+data[i].billNo                        
                                	});  
       	  						}
                            	var tab = $('#ttt').tabs('getTab',i);
								$(tab).append('<table  id="dg_'+data[i].id+'"></table>');
								$('#ttt').tabs('select',i);
								var ww = $('#ttt').tabs('getSelected');
								var wwtab = ww.panel('options');				 
								var billNo1= wwtab.id;
								var tabbleId = "dg_"+billNo1.split("_")[1];
								loadNullDate(tabbleId)
             				}
                            $('#ttt').tabs('select',0);
                    });
               }
		});  
		bindEnterEvent('findByName',searchFrom,'easyui');
	});
	function loadNullDate(tabbleId){
		$("#"+tabbleId).datagrid({
			striped:true,
			fit:true,
			pagination:true,
			border:false,
			rownumbers:true,
			pageSize:20,
			data: {total:0,rows:[]},
			pageList:[20,30,50,80,100],
			columns: [  
						[  	{field:'ck',checkbox:true}, 
						   	{field:'id',title:'Id',hidden:true},
							{field:'drugName',title:'名称',width:200},  
							{field:'specs',title:'规格/样品类型',width:160},
							{field:'qtyTot',title:'用量',width:80},
							{field:'priceUnit',title:'单位',width:100},
							{field:'docName',title:'开立医生',width:100},
							{field:'validFlag',title:'有效',width:100},
							{field:'frequencyName',title:'频次',width:100},
							{field:'moDate',title:'医嘱时间',width:150},
							{field:'useTime',title:'应执行时间',width:150},
							{field:'decoDate',title:'分解时间',width:150},
							{field:'execDpcd',title:'执行科室',width:100},
							{field:'execFlag',title:'执行',width:50},
							{field:'execPrnflag',title:'打印',width:50}
						]  
					] 
		});
		
	}
	function searchFrom() {	
	  var searchText = $('#findByName').textbox('getText');
      $("#inpatientTree").tree("search", searchText);
    }
			function loadDatagrid(tabbleId,billNo,billType,inpatientNo,start,end){
				if(billType==1){
					$("#"+tabbleId).datagrid({
						striped:true,
						fit:true,
						pagination:true,
						border:false,
						rownumbers:true,
						pageSize:20,
						pageList:[20,30,50,80,100],
						url:'<%=basePath%>statistics/outExecution/queryExecdrugList.action',
						queryParams:{billNo:billNo,patNoData:inpatientNo,beginDate:start,endDate:end},
						columns: [  
									[  	{field:'ck',checkbox:true}, 
									   	{field:'id',title:'Id',hidden:true},
										{field:'drugName',title:'名称',width:200},  
										{field:'specs',title:'规格/样品类型',width:100},
										{field:'qtyTot',title:'用量',width:80},
										{field:'priceUnit',title:'单位',width:100},
										{field:'docName',title:'开立医生',width:100},
										{field:'validFlag',title:'有效',width:100},
										{field:'frequencyName',title:'频次',width:100},
										{field:'moDate',title:'医嘱时间',width:150},
										{field:'useTime',title:'应执行时间',width:150},
										{field:'decoDate',title:'分解时间',width:150},
										{field:'execDpcd',title:'执行科室',width:100},
										{field:'execFlag',title:'执行',width:50},
										{field:'execPrnflag',title:'打印',width:50}
									]  
								] 
					});
				}else{
					$("#"+tabbleId).datagrid({
						striped:true,
						fit:true,
						pagination:true,
						border:false,
						rownumbers:true,
						pageSize:20,
						pageList:[20,30,50,80,100],
						url:'<%=basePath%>statistics/outExecution/queryExecdrugList.action',
						queryParams:{billNo:billNo,patNoData:inpatientNo,beginDate:start,endDate:end},
						columns: [  
									[	{field:'ck',checkbox:true}, 
										{field:'id',title:'Id',hidden:true},
										{field:'drugName',title:'名称',width:200},  
										{field:'specs',title:'规格/样品类型',width:100},
										{field:'qtyTot',title:'用量',width:80},
										{field:'priceUnit',title:'单位',width:100},
										{field:'docName',title:'开立医生',width:100},
										{field:'validFlag',title:'有效',width:100},
										{field:'frequencyName',title:'频次',width:100},
										{field:'moDate',title:'医嘱时间',width:150},
										{field:'useTime',title:'应执行时间',width:150},
										{field:'decoDate',title:'分解时间',width:150},
										{field:'execDpcd',title:'执行科室',width:100},
										{field:'execFlag',title:'执行',width:50},
										{field:'execPrnflag',title:'打印',width:50,align:'left'}
									]  
								] ,
					});
				}
			}
			
	//树的重载	
	function treeReload(deptCode){
		   	var start = $('#start').val();  
		    var end = $('#end').val();
		    if(start==null||start==''||end==null||end==''){
		    	$.messager.alert('提示','时间不能为空');
		    	return false;
		    }
 			 $('#inpatientTree').html('正在加载....');
        	 $('#inpatientTree').tree({ 
        		   url:"<%=basePath%>statistics/outExecution/treeInpatient.action",
        		   queryParams:{deptCode:deptCode,beginDate:start,endDate:end},
        		   method:'POST',
        		   animate:true,  //点在展开或折叠的时候是否显示动画效果
        		   lines:true ,  //是否显示树控件上的虚线
        		   dnd:true,
        		   onClick:function(node){
                       var inpatientNo= node.id;
                       var ww = $('#ttt').tabs('getSelected');
       				   var wwtab = ww.panel('options');				 
       				   var billNo1= wwtab.id;
       				   var tabbleId = "dg_"+billNo1.split("_")[1];
       				   var billNo=billNo1.split("_")[1];  
       				$.ajax({
       						url: "<%=basePath%>statistics/outExecution/queryDrugBillDetail.action",
       						data:{billNo:billNo},	
       						type:'post',
       						success: function(data) {
       							var sign=data.total//药嘱类型
       							if(sign>0){
       								if(sign==1){
       									loadDatagrid(tabbleId,billNo,1,inpatientNo,start,end);
       								}else if(sign==2){
       									loadDatagrid(tabbleId,billNo,2,inpatientNo,start,end);
       								}
       							}else{
       								loadNullDate(tabbleId);
//        								loadDatagrid(tabbleId,billNo,2,inpatientNo,start,end);
       							}
       						}
       					});	
        		   },
        		   formatter:function(node){//统计节点总数
        				  var s = node.text;
        				  if (node.children){
        					  if(node.id==1){
        						  s += '<span style=\'color:blue\'>(' + node.children.length + ')</span>';
        					  }
        			      }  
        				  return s;
        			   }
        	 });
	}
	function onclickDate(){
		var deptCode=$('#dept').combobox('getValue');
		treeReload(deptCode);
	}
	function clear(){
		$('#dept').combobox('setValue','');
		$('#inpatientTree').html('暂无数据');
		var tabs = $('#ttt').tabs('tabs');  
  		for(var i=tabs.length-1;i>=0;i--){
            $('#ttt').tabs('close',i);
        } 
	}
</script>
</html>