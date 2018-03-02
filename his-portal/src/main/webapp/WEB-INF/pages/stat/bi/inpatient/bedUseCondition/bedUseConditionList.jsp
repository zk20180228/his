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
			url:'<%=basePath%>statistics/bedUseCondition/queryBedWardload.action',
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
			url:'<%=basePath%>statistics/bedUseCondition/queryBedUseCondition.action',
			columns:[[
						{field:'name',title:'<b>医疗机构运营情况床位利用</b>',width:'10%',colspan:10,align:'center'}
			          ],
			         [
			          {field:'deptName',title:'病区',width:'15%',align:'center'},
			          {field:'actualBed',title:'实有床位数（张）',width:'8%',align:'center'},
			          {field:'actOpenBedDay',title:'实际开放总床日数',width:'10%',align:'center'},
			          {field:'aveOpenBed',title:'平均开放病床（张）',width:'10%',align:'center'},
			          {field:'actOccBedDay',title:'实际占用总床日数',width:'10%',align:'center'},
			          {field:'disPatOccBedDay',title:'出院者占用总床日数',width:'10%',align:'center'},
			          {field:'bedTurnTimes',title:'病床周转次数',width:'10%',align:'center'},
			          {field:'bedWorkDay',title:'病床工作日',width:'8%',align:'center'},
			          {field:'disPatAveDay',title:'出院者平均住院日',width:'10%',align:'center'},
			          {field:'beddoorEmergency',title:'每床与每日门急诊诊次之比',width:'10%',align:'center'}
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
        var actualBed = 0
        var actOpenBedDay=0;
        var aveOpenBed=0.00;
        var actOccBedDay = 0
        var disPatOccBedDay=0;
        var bedTurnTimes=0.00;
        var bedWorkDay = 0
        var disPatAveDay=0;
        var disPatAveDay=0.00;
        var beddoorEmergency=0;
        for (var i = 0; i < rows.length; i++) {
      	  actualBed += rows[i]['actualBed'];
      	  actOpenBedDay += rows[i]['actOpenBedDay'];
      	  aveOpenBed += rows[i]['aveOpenBed'];
      	  actOccBedDay += rows[i]['actOccBedDay'];
      	  disPatOccBedDay += rows[i]['disPatOccBedDay'];
      	  bedTurnTimes += rows[i]['bedTurnTimes'];
      	  bedWorkDay += rows[i]['bedWorkDay'];
      	  disPatAveDay += rows[i]['disPatAveDay'];
      	  disPatAveDay += rows[i]['disPatAveDay'];
      		beddoorEmergency += rows[i]['beddoorEmergency'];
        }
        if(beddoorEmergency!=0){
        	beddoorEmergency=beddoorEmergency.toFixed(2)
        }
        
        //新增一行显示统计信息
        $('#table1').datagrid('insertRow',{index: 0, row:{ deptName: '<b>统计：</b>', actualBed: actualBed, actOpenBedDay: actOpenBedDay,aveOpenBed: aveOpenBed,actOccBedDay: actOccBedDay, disPatOccBedDay: disPatOccBedDay,bedTurnTimes: bedTurnTimes,bedWorkDay: bedWorkDay,disPatAveDay: disPatAveDay,disPatAveDay: disPatAveDay,beddoorEmergency: beddoorEmergency}});
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
			url:'<%=basePath%>statistics/bedUseCondition/queryBedUseCondition.action',
			queryParams:{'years':years,'deptCode':nameString},
			onLoadSuccess:compute
		});
	}
	
</script>
</head>
<body  class="easyui-layout" data-options="fit:true">
<div data-options="region:'center',border:false" style="width: 100%;" class="bedUseConditionList">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'north',border:false" style="width: 100%;height: 50px;padding:8px;">
				病区：<input class="easyui-combobox" id="dept" style="margin:15px 15px 0px 0px;">
				年度：<input class="easyui-textbox" id="year" style="margin:15px 15px 0px 15px;">
				<a href="javascript:void(0)"  class="easyui-linkbutton" onclick="queryList()" data-options="iconCls:'icon-search'" style="margin:2px 0px 0px 15px" >查&nbsp;询&nbsp;</a>
			</div>
			<div data-options="region:'center',border:false" style="width: 100%;">
				<table id="table1" data-options="fit:true"></table>
			</div>
		</div>
	</div>

</div>
</body>
</html>