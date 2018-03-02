<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>病区患者费用及明细查询</title>
<%@ include file="/common/metas.jsp"%>
</head>
<body>
<div id="divLayout" class="easyui-layout" data-options="fit:true">
	<div data-options="region:'west',border:false" style="height: 93%;width:300px">
		<div id="cc" class="easyui-layout" data-options="fit:true">   
		    <div data-options="region:'north'" align="center" style="height:85px;padding-top: 10px;padding:10px;border-top:0 ">
		    	<table>
		    		<tr>
		    			<td>
		    			    <input class="easyui-combobox" id="type" style="width: 80px">
		    				<input type="text"  id="medicalrecordIdSerc"  class="easyui-textbox" data-options="prompt: '床位号、病历号、姓名查询'"/>
		    			</td>
		    		</tr>
		    		<tr style="height: 5px;"></tr>
		    		<tr style="">
		    			<td>
		    			<shiro:hasPermission name="${menuAlias}:function:query"> 
		    			<a  id="search" href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		    			</shiro:hasPermission>
		    			<shiro:hasPermission name="${menuAlias}:function:print"> 
		    			<a  id="savePDF" href="javascript:void(0)" onclick="adPrintAdvice()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
		    			</shiro:hasPermission>
		    			</td>
		    		</tr>
		    	</table>
		    </div>   
		    <div data-options="region:'center'">
		    	<ul id="tDt1" style="width:100%;height:100%;">正在加载，请稍后。。。</ul>
		    </div>   
		</div>	
	</div>   
	<div data-options="region:'center',border:false" style="height:93%;width: 83%">
		<div id="tt" class="easyui-tabs" data-options="fit:true,border:false">   
			<div title="费用汇总" style="padding:0px"> 
				<jsp:include page="detailFeeInfo.jsp"></jsp:include> 											
		    </div>
		    <div title="费用明细" style="padding:0px">
				<jsp:include page="costDetails.jsp"></jsp:include>
			</div>					
	 	</div>
	</div>
</div>
<input id="deptId" type="hidden" value="${deptId}">
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-tree-method-search.js"></script>
<script type="text/javascript">
var nodeid=null;
var zy1=$('#zy1').val();
var cy1=$('#cy1').val();
/**
 * 回车键查询
 * @author  yeguanqun
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2015-12-9
 * @version 1.0
 */		
$(function(){
	//选项卡选中事件
	$('#tt').tabs({    
    border:false,    
    onSelect:function(title){ 
    	var node = $('#tDt1').tree('getSelected');
    	if(node==''||node==null){
    		return;
    	}else{
    	if(title=='费用汇总'){
   		 $.post("<%=basePath%>statistics/InpatientFeeDetail/queryFeeInpatientInfo.action",{inpatientNo:nodeid},function(inpatientInfo){
   			 $("#name").html("");
   			 $("#inDate").html("");
   			 $("#freeCost").html("");
   			 
   			 $("#name").html(inpatientInfo.patientName);
   			 $("#inDate").html(inpatientInfo.inDate);
   			 $("#freeCost").html(inpatientInfo.freeCost);
   			  });
   		$('#listDetailFeeInfo').datagrid({			
   			url:'<%=basePath%>statistics/InpatientFeeDetail/queryFeeDetailInfo.action?inpatientInfo.inpatientNo='+node.id,				
   		});
	   	}
	   	else if(title=='费用明细'){					
	   		$('#listDetailsCost').datagrid({
	   			url:'<%=basePath%>statistics/InpatientFeeDetail/queryCostDetails.action?inpatientInfo.inpatientNo='+node.id,
	   		});
	   	}  
    	}
    }    
});
	
	$('#type').combobox({
	    data:[{"id":1,"text":"在院"},{"id":2,"text":"出院"},{"id":'12',"text":"全部"}],  
	    valueField:'id',    
	    textField:'text',
	    required:true,    
	    editable:true,
	    onLoadSuccess:function(none){
	    	$('#type').combobox('select','1');
	    },
	    onChange:function(newValue,oldValue){
	    	if(newValue=="1"){
	    		loadtree(1);
	    	}else if(newValue=="2"){
	    		loadtree(2);
	    	}else if(newValue=="12"){
	    		loadtree(12);
	    	}
	    }
	});
 	bindEnterEvent('medicalrecordIdSerc',searchFrom,'easyui');
});
function searchFromDate(){
	var rows = $('#tDt1').tree('getSelected');
	if(rows!=null){
		if(rows.id=="1"){
			$.messager.alert("提示","请先选择患者！");
		}else{
			querydatagrid(rows);
		}
	}else{
		$.messager.alert("提示","请先选择患者！");
	}
}
/**
 * 根据病历号查询患者
 * @author  yeguanqun
 * @param title 标签名称
 * @param title 跳转路径
 * @date 2016-6-2
 * @version 1.0
 */
function searchFrom() {	
	  var searchText = $('#medicalrecordIdSerc').textbox('getText');
      $("#tDt1").tree("search", searchText);
}
/**  
 *  
 * @Description：加载本病患者树
 * @Author：yegaunqun
 * @CreateDate：2016-6-1
 * @version 1.0
 * @throws IOException 
 *
 */
 function loadtree(flag){
	 $('#tDt1').tree({ 
		   url:"<%=basePath%>statistics/InpatientFeeDetail/InfoTree.action",
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
		   },
		   onClick: function(node){//点击节点
			   querydatagrid(node);				
		   },onLoadSuccess:function(node, data){
			   console.info(data);
			   if(data.resCode=='error'){
				   $("body").setLoading({
						id:"body",
						isImg:false,
						text:data.resMsg
					});
			   }
		   }
	});
}
function querydatagrid(node){
	nodeid=node.id;
	var qq = $('#tt').tabs('getSelected');				
	var tab = qq.panel('options');
	if(tab.title=='费用汇总'){
		 $.post("<%=basePath%>statistics/InpatientFeeDetail/queryFeeInpatientInfo.action",{inpatientNo:nodeid},function(inpatientInfo){
			 $("#name").html("");
			 $("#inDate").html("");
			 $("#freeCost").html("");
			 
			 $("#name").html(inpatientInfo.patientName);
			 $("#inDate").html(inpatientInfo.inDate);
			 $("#freeCost").html(inpatientInfo.freeCost);
			  });
		$('#listDetailFeeInfo').datagrid({			
			url:'<%=basePath%>statistics/InpatientFeeDetail/queryFeeDetailInfo.action?inpatientInfo.inpatientNo='+node.id,				
		});
	}
	else if(tab.title=='费用明细'){					
		$('#listDetailsCost').datagrid({
			url:'<%=basePath%>statistics/InpatientFeeDetail/queryCostDetails.action?inpatientInfo.inpatientNo='+node.id,
		});
	}				
}
 /**  
  *  
  * @Description：打印
  * @Author：hanzurong
  * @CreateDate：2016-9-26
  * @version 1.0
  * @throws IOException 
  *
  */
function adPrintAdvice(){
		var rows=$('#listDetailFeeInfo').datagrid('getRows');
		var rows1=$('#listDetailsCost').datagrid('getRows');
		var qq = $('#tt').tabs('getSelected');
		var tab = qq.panel('options');
		if(tab.title=='费用汇总'&&rows!=null&&rows!=''){	
			   var nam= $("#name").html();
			   var indate= $("#inDate").html();
			   var freecost= $("#freeCost").html();
			var timerStr = Math.random();
			var name=encodeURIComponent(encodeURIComponent($('#name').val()))
		 	window.open ("<c:url value='queryFeeDetailInfoPDF.action?randomId='/>"+timerStr+"&inpatientInfo.inpatientNo="+nodeid+"&inpatientInfo.accountId="+name+"&inpatientInfo.profCode="+$('#inDate').val()+"&inpatientInfo.alterType="+$('#freeCost').val(),'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}else if(tab.title=='费用明细'&&rows1!=null&&rows1!=''){
			window.open ("<c:url value='queryCostDetailsPDF.action?randomId='/>"+timerStr+"&inpatientInfo.inpatientNo="+nodeid,'newwindow','height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		}else{
			$.messager.alert('提示','列表中没有数据，无法打印'); 
		}		
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>
</body>
</html>