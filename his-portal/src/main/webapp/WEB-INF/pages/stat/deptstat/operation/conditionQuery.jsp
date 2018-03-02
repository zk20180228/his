<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>手术情况统计</title>
<%@ include file="/common/metas.jsp"%>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<style type="text/css">
	.panel-body,.panel{
			overflow:visible;
		}
		.addList dl:first-child ul {
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix:after{
		    content:"";
		    display:table;
		    height:0;
		    visibility:hidden;
		    clear:both;
		}
		.xmenu dl dd ul{
			overflow:visible; !important; 
			clear:both;
		}
		.clearfix{
		*zoom:1;    /* IE/7/6*/
		}
</style>
<style type="text/css">
	.tableCss{
		border-collapse: collapse;border-spacing: 0;border: 1px solid #95b8e7;width:100%;
	}
	.tableCss .TDlabel{
		text-align: right;
		font-size:14px;
		width:200px;
	}
	.tableCss td{
		border: 1px solid #95b8e7;
		padding: 5px 5px;
		word-break: keep-all;
		white-space:nowrap;
	}
	.easyuiInput{
		width:200px;
	}
	.Input{
		width:200px;
	}
</style>
</head>
<script type="text/javascript">
	var menuAlias = '${menuAlias}';
	var cames=[];//院区
	var deptMap;//科室渲染
	var beginSearch="${startTime}";
	var endSearch="${endTime}";
	var flag;//全局科室
	$(function(){
		//科室下拉
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			menulines:2, //设置菜单每行显示几列（1-5），默认为2
			dropmenu:"#m3",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			haveThreeLevel:false,//是否有三级菜单
			para:"I",//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			async:false,
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		});
		
		$('#m3 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#deptName').getMenuIds();
		$('a[name=\'menu-confirm-clear\']').click();
		if(flag==''||flag==null){
			$("#list").datagrid();
			$("body").setLoading({
				id:"body",
				isImg:false,
				text:"无数据权限"
			});
		}else{
			$.ajax({
				url: "<%=basePath%>publics/consultation/querydeptComboboxs.action",
				async:false,
				success: function(deptData) {
					deptMap = deptData;
				}
			});
			$("#list").datagrid({
				fit:true,
				checkOnSelect:true,
				selectOnCheck:false,
				singleSelect:true,
				rownumbers:true,
				striped:true,
				pagination:true,
				pageSize:20,
				data:{total:0,rows:[]}
			});
		}
	});
	
	function searchFrom(){
		var begin=$('#begin').val();
		var end=$('#end').val();
		if(begin==''||end==''){
			$.messager.alert('提示','查询时间不能为空');
			return false;
		}
		var depts=$('#deptName').getMenuIds();
		if(depts==''){
			depts=flag;
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
			pageSize:20,
			queryParams: {startTime :begin,endTime:end,menuAlias:menuAlias,depts:depts},
			url:'<%=basePath %>/statistics/operaCondtion/queryOperaCondList.action',
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
	//科室渲染
	function deptformatter(value,row,index){
		if(value!=null && value!=''&&value!='病区合计'){
			return deptMap[value];
		}
		return value;
	}
	function clear(){
		$('#begin').val(beginSearch);
		$('#end').val(endSearch);
		$('a[name=\'menu-confirm-clear\']').click();
		$("#list").datagrid('loadData',{total:0,rows:[]}); 
	}
</script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script> 
<body>
<div class="easyui-layout" data-options="fit:true">   
    <div data-options="region:'north',border:false" style="height:42px;width:100%">
    	<table id="tab" style="width: 100%;padding: 5px 5px 5px 5px;">
			<tr>
			<!-- 开始时间 --> 
				<td style="width:80px;" align="left">开始时间:</td>
				<td style="width:110px;">
					<input id="begin" class="Wdate" type="text" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'end\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<!-- 结束时间 --> 
				<td style="width:80px;" align="center">结束时间:</td>
				<td style="width:110px" id="td1">
					<input id="end" class="Wdate" type="text" value="${endTime }"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'begin\')}'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
				</td>
				<td style="width: 50px">科室:</td>
							 <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew" name="t1.id" readonly/>
								<span></span>
							</div>
							<div id="m3" class="xmenu" style="display: none;">
								<div class="searchDept">
									<input type="text" name="searchByDeptName" placeholder="回车查询"/>
									<span class="searchMenu"><i></i>查询</span>
									<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">取消</span>
									</a>						
									<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">清空</span>
									</a>
									<a name="menu-confirm" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">确定</span>
									</a>
								</div>
								<div class="select-info" style="display:none">
									<label class="top-label">已选部门：</label>
									<ul class="addDept"></ul>
								</div>	
								<div class="depts-dl">
									<div class="addList">
									</div>
									<div class="tip" style="display:none">没有检索到数据</div>
								</div>	
							</div>
		    		</td>
				<td>
					<shiro:hasPermission name="${menuAlias}:function:query">
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="margin-left: 3px">查询</a>
					</shiro:hasPermission>
						<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset">重置</a>
				</td>
			</tr>
		</table>
    </div>
    <div data-options="region:'center',border:false" style="width:100%">
    	<div class="easyui-layout" data-options="fit:true">
    		<div data-options="region:'north',border:false" style=";width:100%">
    			<table style="width:100%;padding: 5px 5px 5px 0px;">
		    		<tr>
		    			<h5 align="center"style="font-size: 32;font: bold;text-align: center;padding-top: 15px">手术情况统计</h5>
		    		</tr>
		    	</table>
    		</div>
    		<div data-options="region:'center',border:false" style="width:100%">
    			<table class="easyui-datagrid"  id="list"  data-options="method:'post',rownumbers:true,striped:true,border:false,checkOnSelect:true,selectOnCheck:false,singleSelect:true,fitColumns:false,fit:true">   
				    <thead>   
				        <tr> 
				         <th data-options="field:'deptCode',width:'10%',formatter:deptformatter" halign="left" align="left">科室</th>
						<th data-options="field:'name',width:'10%'" align="center">患者姓名</th>
						<th data-options="field:'patientNo',width:'10%'" align="center">住院号(病历号)</th>
						<th data-options="field:'itemName',width:'25%'" halign="left" align="left">手术名称</th>
						<th data-options="field:'realDuation',width:'10%'" align="center">手术耗时</th>
						<th data-options="field:'execDept',width:'10%',formatter:deptformatter"align="center">手术室</th>
				</tr>   
				    </thead>   
				</table>
    		</div>
    	</div>
    </div>   
</div> 
</body>
</html>