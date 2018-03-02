<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/metas.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript">
	var menuAlias = '${menuAlias}';
	var cames=[];//院区
	$(function(){
		document.getElementById('hh').innerHTML = '医疗质量运行监管指标统计';
		$.ajax({
			url: "<c:url value='/baseinfo/department/getAuthorArea.action'/>?menuAlias="+menuAlias,
			type:"post",
			async:false,
			success: function(date){
				cames=date;
			}
		});
		var sign=cames.length;
		if(sign==undefined||sign==0){
			$("#list").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
			$('#campus').combobox({
				valueField : 'code',
				textField : 'name',
				multiple : true,
				data:cames
			});
	}
		$("#list").datagrid({
			fit:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			rownumbers:true,
			striped:true,
			pagination:true,
			data:{total:0,rows:[]}
			});
	});
	function searchFrom(){
		var begin=$('#begin').val();
		var end=$('#end').val();
		if(begin==''||end==''){
			$.messager.alert('提示','时间不能为空');
			return false;
		}
		var came=$('#campus').combobox('getValues');
		if(came.length==0){
			for(var i=0;i<cames.length;i++){
				came.push(cames[i].code);
			}
		}
		//加载数据表格
		$("#list").datagrid({
			fit:true,
			checkOnSelect:true,
			selectOnCheck:false,
			singleSelect:true,
			rownumbers:true,
			striped:true,
			pagination:true,
			url:'<%=basePath%>/dept/DeptSupervision/querySupervisionList.action',
			queryParams:{begin:begin,end:end,menuAlias:menuAlias,campus:came},
			onLoadSuccess:function(data){
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
		});
	}
	/**
	* 重置方法
	*/
	function clear(){
		$('#begin').val("${begin}");
		$('#end').val("${end}");
		$('#campus').combobox('clear');
		$("#list").datagrid('loadData', { total: 0, rows: [] });
	}
	</script>
	<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> 
<body>
	<div id="layoutId" class="easyui-layout" data-options="fit:true">   
	    <div data-options="region:'north',split:false,border:false" style="height:100px;">
	    	<form id="searchForm" style="padding-top:5px;padding-left:5px;padding-right:5px;">
				<table>
					<tr align="left">
						<td style="width: 400px;" >
							开始时间:
							<input id="begin" class="Wdate" type="text" value="${begin}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'end\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
							结束时间:
							<input id="end" class="Wdate" type="text" value="${end}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',minDate:'#F{$dp.$D(\'begin\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;height: 24px;"/>
						</td>
						<td style="width:240px;">&nbsp;院区:
								<input id="campus" class="easyui-combobox" name="campus"  /> 
						</td>
						<td>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${menuAlias}:function:query">
								<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							</shiro:hasPermission>
						</td>
						</tr>
				</table>
			</form>
			<h5 id="hh" style="font-size: 30;font: bold;text-align: center;padding-top: 15px"></h5>
	    </div>   
	    <div data-options="region:'center'" >
	    	<table id="list" data-options="pagination:true,fit:true" >
				<thead>
					 <tr>
						<th data-options="field:'deptName',rowspan:2,width:100" align="center">院区</th>
						<th data-options="field:'oldNum',colspan:3" align="center">门诊人次</th>
						<th data-options="field:'inNum',colspan:3" align="center" halign="center">门诊人均诊察人次</th>
						<th data-options="field:'exInNum',colspan:3" align="center">入院患者例数</th>
						<th data-options="field:'exOutNum',colspan:3" align="center" halign="center">出院患者例数</th>
						<th data-options="field:'outNum',colspan:3" align="center">住院患者及危重患者例数</th>
						<th data-options="field:'nowNum',colspan:3" align="center">住院患者抢救成功例数</th>
						<th data-options="field:'realBedNum',colspan:3" align="center">入院患者手术例数</th>
						<th data-options="field:'hangBedDays',colspan:3" align="center">平均住院天数</th>
						<th data-options="field:'hangBedDays',colspan:3" align="center">住院患者治愈率（%）</th>
						<th data-options="field:'openBedNum',colspan:3" align="center">住院患者未治愈率（%）</th>
						<th data-options="field:'rateOfBed',colspan:3" align="center">住院患者好转率（%）</th>
						<th data-options="field:'criticallyNum',colspan:3" align="center">床位使用率（%）</th>
						<th data-options="field:'grateOneNum',colspan:3" align="center">床位周转次数</th>
						<th data-options="field:'extraBedNum',colspan:3" align="center">平均床位工作日</th>
						<th data-options="field:'zhuyuanfeiyong',colspan:3" align="center">平均住院费用</th>
						<th data-options="field:'chuyuansiwanglv',colspan:3" align="center">出院死亡例数</th>
						<th data-options="field:'chongfanlv',rowspan:2,width:100" align="center">重返率</th>
						
					</tr> 
					<tr>
						
						<th data-options="field:'emptyBedNum',width:100"  align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
						<th data-options="field:'emptyBedNum',width:100"   align="center">本期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">上期</th>
						<th data-options="field:'emptyBedNum',width:100"   align="center">增减</th>
						
					</tr>
				</thead>
			</table>
	    </div>
	</div> 
</body>
</html>