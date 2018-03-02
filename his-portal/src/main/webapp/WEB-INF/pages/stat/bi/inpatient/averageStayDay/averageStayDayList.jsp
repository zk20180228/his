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
<script type="text/javascript">
	
	$(function(){
		$('#dept').combobox({
			url:'<%=basePath%>statistics/averageStayDay/queryDeptload.action',
			valueField: 'deptCode',    
		    textField: 'deptName',  
		    multiple:true,
			select:'1',
			onSelect:function(data){
			},
			keyHandler:{
				enter: function(e){
					$('#diaInpatient').window('open');
				}
			}
		});
		$('#dept').combobox('select','1');
	
		$("input",$("#year").next("span")).blur(function() {
			if($("#year").textbox('getValue')){
				  var year=$('#year').textbox('getValue');
				  var reg = new RegExp("^([1-9][0-9]{3})$");//四位整数  
			    	if(!reg.test(year)){
			    		$('#year').textbox('setValue','');
			    		$.messager.alert('警告','年度只能为非0开头的4位有效整数！！');  
			    		 $("#year").next("span").children().first().focus();
			    	} 
				}
		
		} )
		
		$('#table1').datagrid({
			url:'<%=basePath%>statistics/averageStayDay/queryAverageStayDay.action',
			columns:[[
			          {field:'deptName',title:'科室',width:'60%',align:'center'},
			          {field:'aveStayDay',title:'平均住院日',width:'40%',align:'center'}
			          ]],
			  singleSelect:true,
			  onLoadSuccess:compute
		});			  
});
	
	/**
	 * 在列表最后添加一行总计
	 * @Author: zhuxiaolu
	 * @CreateDate: 2016年7月29日
	 * @version: 1.0
	 */
	function compute() {
        var rows = $('#table1').datagrid('getRows')//获取当前的数据行
        var aveStayDay = 0
       
        for (var i = 0; i < rows.length; i++) {
        	aveStayDay += rows[i]['aveStayDay'];
      	 
        }
       
        //新增一行显示统计信息
        $('#table1').datagrid('appendRow',{ deptName: '<b>统计：</b>', aveStayDay: aveStayDay});
    }

	function queryList(){
		var nameArray=$('#dept').combobox('getValues');
		var columnsArray=new Array();
		var nameString='';
		var years=$('#year').textbox('getValue');
		if(nameArray==''){
			$.messager.alert('提示', '请选择科室');
			return;
		}
		if(nameArray){
			//循环 将部门ID转换格式成String
			for(var i=0;i<nameArray.length;i++){
				if(nameString!=null&&nameString!=''){
					nameString +='\',\'';
				}else{
					nameString+='\'';
				}
				nameString +=nameArray[i];
				if(i==nameArray.length-1){
					nameString +='\'';
				}
			}	
		}
		$('#table1').datagrid({
			url:'<%=basePath%>statistics/averageStayDay/queryAverageStayDay.action',
			queryParams:{'years':years,'deptCode':nameString},
			onLoadSuccess:compute
		});
	}
	
</script>
</head>

<body  class="easyui-layout" style="width: 30%;">
<div data-options="region:'center',border:false" style="width: 30%;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 30%;height: 50px;padding:8px;">
					科室：<input class="easyui-combobox" id="dept" style="margin:15px 15px 0px 0px;">
				年度：<input class="easyui-textbox" id="year" style="margin:15px 15px 0px 15px;">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:2px 0px 0px 15px" >查 询 </a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;">
				<table id="table1" data-options="fit:true"></table>
			</div>
		</div>
	</div>

</div>
</body>

</html>