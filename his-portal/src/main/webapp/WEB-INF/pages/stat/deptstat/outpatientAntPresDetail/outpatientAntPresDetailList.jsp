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
	var beginClear='${searchBegin }';
	var endClear="${searchEnd }";
	var deptMap=null;
	var menuAlias="${menuAlias}";
	var flag;//全局科室
	$(function(){
		$(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			menulines:2, //设置菜单每行显示几列（1-5），默认为2
			dropmenu:"#m3",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			haveThreeLevel:false,//是否有三级菜单
			para:"C",//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
			async:false,
			firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		});
		$('#m3 .addList h2 input').click();
		$('a[name=\'menu-confirm\']').click();
		flag=$('#deptName').getMenuIds();
		$('a[name=\'menu-confirm-clear\']').click();
		if(flag==''||flag==null){
			$('#tableList').datagrid();
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
			$('#tableList').datagrid({
				pagination:true,
				fitColumns:true,
				striped:true,
				rownumbers:true,
				pageSize:20,
				pageList:[20,30,50,80,100],
				data:{total:0,rows:[]}
			});
		}
	});
	function search(){
		var begin=$('#startTime').val();
		var end=$('#endTime').val();
		if(begin==''||end==''){
			$.messager.alert('提示','时间不能为空');
			return false;
		}
		var deptCodes=$('#deptName').getMenuIds();
		if(deptCodes==''){
			deptCodes=flag
		};
		$('#tableList').datagrid({
			fitColumns:true,
			striped:true,
			rownumbers:true,
			url:'<%=basePath%>/statistics/outpatientAnt/queryList.action',
			queryParams: {
				searchBegin: begin,
				searchEnd: end,
				deptCodes: deptCodes,
				menuAlias:menuAlias
			}
		});
	}
	//科室渲染
	function dept_codefunction(value,row,index){
		if(value!=null && value!=''&&value!='病区合计'){
			return deptMap[value];
		}
		return value;
	}
	function fatterfunc(value,row,index){
		if(value=='0.0'){
			return '';
		}
	}
	function clear(){
		$('a[name=\'menu-confirm-clear\']').click();
		$('#startTime').val(beginClear);
		$('#endTime').val(endClear);
		$("#tableList").datagrid('loadData',{total:0,rows:[]}); 
		
	}
</script>
<body>
	<div class="easyui-layout" data-options="fit:true" style="margin-left:auto;margin-right:auto;">
				<form action="" style="padding:5px 5px 0px 5px;width:100%;height:70px;">
					<table style="width: 100%" cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td class="chargeBillistSize" style="width: 280px;">
						      	日期
								<input id="startTime"  name="startTime" class="Wdate" type="text" value="${searchBegin }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
								至
								<input id="endTime"  name="endTime"  class="Wdate" type="text" value="${searchEnd }" onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})"  style="width:100px;border: 1px solid #95b8e7;border-radius: 5px;"/>
							</td>
							<td style="width:45px;">科室:</td>
						    <td  class="newMenu" style="width:160px;z-index:1;position: relative;">
							<div class="deptInput menuInput" style="margin-top:0px;">
								<input id="deptName" class="ksnew"  readonly/>
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
								<a href="javascript:search();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								<a href="javascript:clear();"  class="easyui-linkbutton" data-options="iconCls:'reset'" style="margin-left: 3px">重置</a>
<!-- 							    <a href="javascript:void(0)" onclick="print()" class="easyui-linkbutton" data-options="iconCls:'icon-printer'">打印</a> -->
<!-- 							    <a href="javascript:void(0)" onclick="explor()" class="easyui-linkbutton" data-options="iconCls:'icon-down'">导出</a> -->
							</td>
						</tr>
						<tr>
							<td colspan="4" align="center" ><font size="6">门诊抗菌药物处方比例</font></td>
						</tr>
					</table>
				</form>
		<div data-options="region:'center',border:false" style="width: 100%;padding:0px 0px 30px 0px;margin-left:auto;margin-right:auto;height: 90%;">
			<table id="tableList"  style="width:100%;height:90%;" data-options="fitColumns:true,singleSelect:true,fit:true,border:false" >
				<thead>
						<tr>
							<th data-options="field:'dept',formatter:dept_codefunction" width="10%" align="center">科室</th>
							<th data-options="field:'docName'" width="16%" align="center">医生姓名</th>
							<th data-options="field:'drugCfs'" width="16%" align="center">药品处方级</th>
							<th data-options="field:'drugKjcfs'" width="15%" align="center" halign="center">抗菌药物处方数</th>
							<th data-options="field:'drugBl'" width="15%" align="center">抗菌药物处方比例</th>
							<th data-options="field:'ygbl'" width="15%" align="center" halign="center">院规比例</th>
							<th data-options="field:'equel'" width="12%" align="center">对比</th>
						</tr>
					</thead>
			</table>
		</div>
	</div>
</body>
</html>
