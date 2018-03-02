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
var flag;
$(function(){	
	//对比指定年的指定的一个月
	var endYear=$("#Stime").val();//时间
	var month = endYear.substring(5,7);
	if(month<10){
		month=month.substring(1,2);
	}	
	var numYear= parseInt(endYear.substring(0,4));
	var end=endYear.substring(0,4)+"年"+month+"月";
	var beginYear=(numYear-1)+"年"+month+"月";
	$("#beginYear").html(beginYear);
	$("#endYear").html(end);
	
	//科室下拉
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
		haveThreeLevel:false,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
	});
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#deptName').getMenuIds();
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''){
		$("#tableList").datagrid();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}
	
});

/**
* 重置方法
*/
function clear(){
	$('#Stime').val("${startTime}");
	$('#deptName').val('');
	$('#deptName').attr('name','');
	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
}
</script>

<body>
	<div id="divLayout" class="easyui-layout" data-options="fit:true" style="width: 100%; height: 100%;">
					<form action="" style="height:38px;padding:5px 5px 0px 5px;">
						<table id="searchTab" style="width: 100%;" border="0">
							<tr>
								<!-- 开始时间 --> 
								<td style="width:40px;" align="left">时间:</td>
								<td style="width:110px;">
									<input id="Stime" class="Wdate" type="text" name="Stime" value="${startTime }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="height:22px;width:110px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								</td>
								
								<td style="width:45px;" align="center">科室:</td>
								<td  class="newMenu" style="width:160px;z-index:1;position: relative;">
								<div class="deptInput menuInput" style="margin-top:0px;">
									<input id="deptName" class="ksnew" name="t1.id" readonly/>
									<span></span>
									</div>
										<div id="m3" class="xmenu" style="display: none;">
										<div class="searchDept">
									<input type="text" name="searchByDeptName" placeholder="回车查询" />
									<span class="searchMenu"><i></i>查询</span> <a
										name="menu-confirm-cancel" href="javascript:void(0);"
										class="a-btn"> <span class="a-btn-text">取消</span>
									</a> <a name="menu-confirm-clear" href="javascript:void(0);"
										class="a-btn"> <span class="a-btn-text">清空</span>
									</a> <a name="menu-confirm" href="javascript:void(0);" class="a-btn">
										<span class="a-btn-text">确定</span>
									</a>
								</div>
								<div class="select-info" style="display: none">
									<label class="top-label">已选部门：</label>
									<ul class="addDept">
		
									</ul>
								</div>
								<div class="depts-dl">
									<div class="addList"></div>
									<div class="tip" style="display: none">没有检索到数据</div>
								</div>
		    					</td>				    			
								<td>
									<shiro:hasPermission name="${menuAlias}:function:query">
										<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="height:23px;margin-top:-3px">查询</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="${menuAlias}:function:set">
										<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" iconCls="reset" style="height:23px;margin-top:-3px">重置</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="${menuAlias}:function:print">
										<a href="javascript:void(0)" onclick="exper()" class="easyui-linkbutton" iconCls="icon-printer">打印</a>
									</shiro:hasPermission>
									<shiro:hasPermission name="${menuAlias}:function:export">
										<a href="javascript:void(0)" onclick="exportList()" class="easyui-linkbutton" iconCls="icon-down">导出</a>
									</shiro:hasPermission>
								</td>
							</tr>
						</table>
					</form>
<!-- 					</div> -->
				 <div data-options="region:'center',noheader:true,border:false">
				 <div data-options="region:'north'" style="height:80px;width: 100%;">
  			<table style="width:100%;z-index: 0">
		    		<tr>
		    			<td align="center"><font style = "font-size: 32px !important;" size="6" class="empWorkTit">医技工作同期对比表</font></td>
		    		</tr>
		    	</table>
		</div>
	    <!-- <table id="dataGrid1"></table> -->	    
	    <table class="easyui-datagrid" data-options="fitColumns:true,singleSelect:true">   
	    <thead>   
	        <tr>   
	            <th data-options="field:'name',width:'20%'">项目</th>   
	            <th data-options="field:'time1',width:'20%'"><span id="beginYear"></span></th>   
	            <th data-options="field:'time2',width:'20%'"><span id="endYear"></span></th>  
	            <th data-options="field:'zengJian',width:'20%'">增减数</th> 
	            <th data-options="field:'zengJianPer',width:'20%'">增减%</th>  
	        </tr>   
	    </thead>   
	    <tbody>   
	        <tr>   
	            <td>放射科</td><td>187479</td><td>233325</td><td>45846</td><td>24.45</td>   
	        </tr>   
	        <tr>   
	            <td>超声波</td><td>343782</td><td>448161</td><td>104379</td><td>30.36</td>   
	        </tr>   
	        <tr>   
	            <td>核医学</td><td>455272</td><td>560975</td><td>105703</td><td>23.22</td>   
	        </tr>
	         <tr>   
	            <td>河医院区</td><td>455272</td><td>445173</td><td>105703</td><td>-2.22</td>   
	        </tr>
	         <tr>   
	            <td>郑东院区</td><td>--</td><td>115802</td><td>--</td><td>--</td>   
	        </tr> 
	    </tbody>  
	    </table> 
    </div>   
	</div>
</body>
</html>