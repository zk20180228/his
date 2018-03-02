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
})
 /**
* 重置方法
*/
function clear(){
	$('#startTime').val("${startTime}");
	$('#endTime').val("${endTime}");
	$('#deptName').val('');
	$('#deptName').attr('name','');
	$('#team').textbox('setValue',"");
	$("#tableList").datagrid('loadData', { total: 0, rows: [] });
}
</script>
<body>
	<div class="easyui-layout" data-options="fit:true" style="margin-left:auto;margin-right:auto;">
				<form action="" style="padding:5px 5px 0px 5px;width:100%;height:35px;">
					<table style="width: 100%" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td class="chargeBillistSize" style="width: 280px;">
						      	日期
								<input id="startTime"  name="startTime" class="Wdate" type="text" value="${startTime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="endTime"  name="endTime"  class="Wdate" type="text" value="${endTime }" onClick="WdatePicker()"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width:45px;">科室:</td>
						    <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew" name="t1.id" readonly/>
								<span></span>
							</div>
								<div id="m3" class="xmenu" style="display: none;">
									<div class="searchDept">
										<input type="text" name="searchByDeptName" placeholder="回车查询"/>
										<span class="searchMenu"><i></i>查询</span>
										<a name="menu-confirm-cancel" href="javascript:void(0);" class="a-btn"><span class="a-btn-text">取消</span></a>						
										<a name="menu-confirm-clear" href="javascript:void(0);" class="a-btn"><span class="a-btn-text">清空</span></a>
										<a name="menu-confirm" href="javascript:void(0);" class="a-btn"><span class="a-btn-text">确定</span></a>
									</div>
									<div class="select-info" style="display:none">
										<label class="top-label">已选部门：</label>
										<ul class="addDept">
										</ul>
									</div>	
									<div class="depts-dl">
										<div class="addList"></div>
										<div class="tip" style="display:none">没有检索到数据</div>
									</div>	
											
								</div>
		    				</td>
						     <td>
						     	医疗组:<input id="team" class="easyui-combobox" style="width:110px;">
								<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								<a href="javascript:clear();void(0)" onclick="clear()" class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
							    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a>
							    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a>
							</td>
						</tr>
					</table>
				</form>
		<div data-options="region:'center',border:false" style="width: 100%;padding:0px 0px 30px 0px;margin-left:auto;margin-right:auto;height: 100%;">
			<table id="tableList" class="easyui-datagrid" style="width:100%;height:90%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
				<thead>
					<tr>
						<th data-options="field:'deptName',width:'12%'" align="center" halign="center">科室名称</th>
						<th data-options="field:'num',width:'5%'" align="center" halign="center">序号</th>
						<th data-options="field:'ICD',width:'5%'" align="center" halign="center">ICD编码</th>
						<th data-options="field:'illName',width:'12%'" align="center" halign="center">病种名称</th>
						<th data-options="field:'nicePeople',width:'8%'" align="right" halign="center">治愈好转人数</th>
						<th data-options="field:'niceProportion',width:'8%'" align="right" halign="center">治愈好转率</th>
						<th data-options="field:'dead',width:'8%'" align="right" halign="center">死亡</th>
						<th data-options="field:'deadProportion',width:'8%'" align="right" halign="center">死亡率</th>
						<th data-options="field:'total',width:'8%'" align="right" halign="center">总数</th>
						<th data-options="field:'outpatient',width:'8%'" align="right" halign="center">出院人数</th>
						<th data-options="field:'proportion',width:'8%'" align="right" halign="center">占例</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>
</html>