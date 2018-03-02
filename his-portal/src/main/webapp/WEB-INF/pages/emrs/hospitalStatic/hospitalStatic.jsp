<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>电子病历列表</title>
<base href="<%=basePath%>">
	<%@ include file="/common/metas.jsp" %>
	<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
	</style>
	 <script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
	<script type="text/javascript">
	//加载页面
	$(function(){
		//选择科室
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			dropmenu:"#m2",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			firsturl:'<%=basePath%>emrs/emrDeptStatic/getDeptList.action?deptTypes=', //获取列表的url，必须要写
			relativeInput:".doctorInput",	//与其级联的文本框，必须要写
			relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
		});
		$('#mainList').datagrid({
			url:'<%=basePath%>emrs/emrDeptStatic/deptStaticList.action',
			onBeforeLoad:function(param){
				$('#mainList').datagrid('clearSelections');
			},
			onSelect: function (rowIndex, rowData) {//双击查看
				window.open ('<%=basePath%>emrs/emrDeptStatic/toDeptStaticDetil.action?inpatientNo=' + rowData.inpatientNo + '&deptCode=' + rowData.deptCode,'newwindow',' left=400,top=200,width='+ (screen.availWidth -1000) +',height='+ (screen.availHeight-370) 
						+',scrollbars,resizable=yes,toolbar=yes');
			}
		});
	});
	/**
	 * 查询
	 */
	function searchFrom() {
		var dept = $('#ksnew').getMenuIds();
		$('#mainList').datagrid('load',{deptCode : dept});
	}
	
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:false,border:false" style="height: 35px;">
			<table  style="padding:7px 7px 0px 7px;width:100%;position: relative;z-index: 100" data-options="border:false" title="" cellspacing="0" cellpadding="0" border="0">
				<tr >
					<td style="width:55px;" align="center">科室:</td>
					<td width =160px class="newMenu">
						<div class="deptInput menuInput"  style="width:150px;"><input class="ksnew" id="ksnew" readonly="readonly"  style="width:95px" /><span></span></div>
						<div id="m2" class="xmenu" style="display: none;">
							<div class="searchDept">
								<input type="text" name="searchByDeptName" placeholder="回车查询" />
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
							<div class="select-info" style="display:none; ">
								<label class="top-label">已选科室：</label>
								<ul class="addDept">

								</ul>
							</div>
							<div class="depts-dl">
								<div class="addlist"></div>
								<div class="tip" style="display:none; ">没有检索到数据</div>
							</div>
						</div>
					</td>
					<td>
						<a href="javascript:void(0)" onclick="searchFrom()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						<a href="javascript:void(0)" onclick="clears()" class="easyui-linkbutton" iconCls="reset">重置</a>
					</td>
				</tr>
			</table>
		</div>
		<div id="divLhmMessage" data-options="region:'center',border:'false'" >
			<table id="mainList"  data-options="fit:true,method:'post',rownumbers:true,idField: 'id',striped:true,border:false,checkOnSelect:false,selectOnCheck:false,singleSelect:true">
				<thead>
					<tr>
						<th data-options="field:'inpatientNo',width:'13%',align:'center'">住院号</th>
						<th data-options="field:'deptName',width:'14%',align:'center'">科室</th>
						<th data-options="field:'patName',width:'14%',align:'center'">患者姓名</th>
						<th data-options="field:'patSex',width:'14%',align:'center'">性别</th>
						<th data-options="field:'patAge',width:'14%',align:'center'">年龄</th>
						<th data-options="field:'bedName',width:'13%',align:'center'">床号</th>
						<th data-options="field:'inDays',width:'14%',align:'center'">住院日</th>
						<th data-options="field:'inDate',width:'14%',align:'center'">入院时间</th>
						<th data-options="field:'overTime',width:'14%',align:'center'">超时项目数</th>
						<th data-options="field:'lostTime',width:'13%',align:'center'">缺写项目数</th>
						<th data-options="field:'times',width:'14%',align:'center'">质控环节扣分次数</th>
						<th data-options="field:'score',width:'14%',align:'center'">质控环节扣分数</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
</body>