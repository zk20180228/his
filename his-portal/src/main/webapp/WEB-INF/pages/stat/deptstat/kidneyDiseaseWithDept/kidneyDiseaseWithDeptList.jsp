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
<title>科室对比表</title>
</head>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var menuAlias = '${menuAlias}'
var acount='${acount}';
var flag;
// var acountName='${acountName}'
$(function(){
	$(".deptInput").MenuList({
		width :530, //设置宽度，不写默认为530，不要加单位
		height :400, //设置高度，不写默认为400，不要加单位
		menulines:2, //设置菜单每行显示几列（1-5），默认为2
		dropmenu:"#m3",//弹出层id，必须要写
		isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
//		haveThreeLevel:true,//是否有三级菜单
		para:"I",	//要传的参数，多个之间用逗号隔开，如果不写默认为查询全部
		chId:acount,
		firsturl:"<%=basePath%>baseinfo/department/getAuthorNoFic.action?menuAlias="+menuAlias+"&deptTypes=", //获取列表的url，必须要写
		relativeInput:".doctorInput",	//与其级联的文本框，必须要写
		relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
	});
	
	$('#m3 .addList h2 input').click();
	$('a[name=\'menu-confirm\']').click();
	flag=$('#deptName').getMenuIds();
	
	$('a[name=\'menu-confirm-clear\']').click();
	if(flag==''||flag==null){
		$('#illMedical').datagrid();
		pickedFunc();
		$("body").setLoading({
			id:"body",
			isImg:false,
			text:"无数据权限"
		});
	}else{
		
	$('#illMedical').datagrid({
		url:'<%=basePath%>statistics/kidneyDisease/quertItemVo.action',
		queryParams:{date:$('#time').val(),deptCode:$('#deptName').getMenuIds()},
		onLoadSuccess: function (data) {//默认选中
			$('#illMedical').datagrid("autoMergeCells", ['deptName']);
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
	/**
	 * 合并单元格
	 */
	 $.extend($.fn.datagrid.methods, {
	        autoMergeCells: function (jq, fields) {
	            return jq.each(function () {
	                var target = $(this);
	                if (!fields) {
	                    fields = target.datagrid("getColumnFields");
	                }
	                var rows = target.datagrid("getRows");
	                var i = 0,
	                j = 0,
	                temp = {};
	                for (i; i < rows.length; i++) {
	                    var row = rows[i];
	                    j = 0;
	                    for (j; j < fields.length; j++) {
	                        var field = fields[j];
	                        var tf = temp[field];
	                        if (!tf) {
	                            tf = temp[field] = {};
	                            tf[row[field]] = [i];
	                        } else {
	                            var tfv = tf[row[field]];
	                            if (tfv) {
	                                tfv.push(i);
	                            } else {
	                                tfv = tf[row[field]] = [i];
	                            }
	                        }
	                    }
	                }
	                $.each(temp, function (field, colunm) {
	                    $.each(colunm, function () {
	                        var group = this;
	                        if (group.length > 1) {
	                            var before,
	                            after,
	                            megerIndex = group[0];
	                            for (var i = 0; i < group.length; i++) {
	                                before = group[i];
	                                after = group[i + 1];
	                                if (after && (after - before) == 1) {
	                                    continue;
	                                }
	                                var rowspan = before - megerIndex + 1;
	                                if (rowspan > 1) {
	                                    target.datagrid('mergeCells', {
	                                        index: megerIndex,
	                                        field: field,
	                                        rowspan: rowspan
	                                    });
	                                }
	                                if (after && (after - before) != 1) {
	                                    megerIndex = after;
	                                }
	                            }
	                        }
	                    });
	                });
	            });
	        }
	    });
	pickedFunc();
	}
})
//查询
 function searchFrom(){
	
	var time = $('#time').val();
	if(time==null||time==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
	var deptCode = $('#deptName').getMenuIds();
	if(deptCode==''){
		$.messager.alert('提示','请选择科室!');
		return ;
	}
	if(deptCode.length!=deptCode.replace(',','').length){
		$.messager.alert('提示','请选择一个科室!');
		return ;
	}
	$('#illMedical').datagrid('load',{
		date:time,
		deptCode:$('#deptName').getMenuIds()
	});
	pickedFunc();
 }
 //清空
 function clear(){
	$('#time').val('${ETime}');
	$('#deptName').val('');
	$('#deptName').attr('name','');
 	var time=$('#time').val();
	var date=time.split('-');
	var viewCaption=date[0]+'年'+date[1]+'月对比表'
	$('#caption').html(viewCaption);
	$("#illMedical").datagrid('loadData', { total: 0, rows: [] });
	
 }
 //导出
 function exportList(){
	var time = $('#time').val();
	var date=time.split('-');
	var deptName = $('#deptName').val();
	var viewCaption=date[0]+'年'+date[1]+'月'+deptName+'对比表';
    if(time==null||time==''){
        $.messager.alert("提示","日期不能为空！");
        return;
	}
    var rows=$('#illMedical').datagrid('getRows');
	if(rows==null||rows.length==0){
		$.messager.alert("提示","没有数据，不能导出！");
		return;
	}else{
	$.messager.confirm('确认', '确定要导出吗?', function(res) {//提示是否导出
		if (res) {
			  //给表单的隐藏字段赋值
		    $("#rows").val(JSON.stringify(rows));
		    $("#exlToLastTime").val((parseInt(date[0])-1)+'年');
		    $("#exlToTime").val((parseInt(date[0]))+'年');
		    $("#viewTitle").val(viewCaption);
			$('#saveForm').form('submit', {
				url :'<%=basePath%>statistics/kidneyDisease/expKidneyDiseaseWithDept.action',
				onSubmit : function() {
					return $(this).form('validate');
				},
				success : function(data) {
					$.messager.alert("提示", "导出失败！", "success");
				},
				error : function(data) {
					$.messager.alert("提示", "导出失败！", "error");
				}
			});
		}
	});
	}
 }
 //打印
 function exportPDF(){
		var rows=$('#illMedical').datagrid('getRows');
		var time = $('#time').val();
	    var deptName = $('#deptName').val();
	    var date=time.split('-');
		var viewCaption=date[0]+'年'+date[1]+'月'+deptName+'对比表';
		if(time==null||time==''){
	          $.messager.alert("提示","日期不能为空！");
	          return;
		}
		if(rows!=null&&rows!=''){
			$.messager.confirm('确认', '确定要打印吗?', function(res) {//提示是否打印
	 			if (res) {
	 				//hedong 20170316  报表打印参数传递改造为表单POST提交的方式示例
				    //给表单的隐藏字段赋值
				    $("#toLastTime").val((parseInt(date[0])-1)+'年');
				    $("#toTime").val((parseInt(date[0]))+'年');
				    $("#viewCaption").val(viewCaption);
				    $("#reportToRows").val(JSON.stringify(rows));
				    $("#reportToFileName").val("KSDBB");
				
				    //表单提交 target
				    var formTarget="hiddenFormWin";
			        var tmpPath = "<%=basePath%>statistics/kidneyDisease/iReportKidneyDiseaseWithDept.action";
			        //设置表单target
			        $("#reportToHiddenForm").attr("target",formTarget);
			        //设置表单访问路径
					$("#reportToHiddenForm").attr("action",tmpPath); 
			        //表单提交时打开一个空的窗口
				    $("#reportToHiddenForm").submit(function(e){
				    	 var timerStr = Math.random();
						 window.open('about:blank',formTarget,'height=900,width=850,top=50,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no,status=no');   
					});  
				    //表单提交
				    $("#reportToHiddenForm").submit();
	 			}
	 		});
		}else{
			$.messager.alert("提示","没有数据，不能打印！"); 	
		} 
 }
 function pickedFunc(){
	 var time=$('#time').val();
	 var date=time.split('-');
	 var deptName=$('#deptName').val();
	 var viewCaption=date[0]+'年'+date[1]+'月'+deptName+'对比表'
	 $('#caption').html(viewCaption);
	 $('#yearOne').html((parseInt(date[0])-1)+'年');
	 $('#yearTwo').html((parseInt(date[0]))+'年');
 }
</script>
<body style="margin: 0px; padding: 0px;">
<div id="cc" class="easyui-layout" data-options="fit:true">   
	   <form action="" style="width: 100%;height: 4%;padding: 5px 5px 5px 5px;">
	    <table style="width: 100%;height: 100%;">
			<tr>
	  		 <td style="width:40px;" align="left">时间:</td>
	  		 <td style="width:110px;">
				<input id="time" class="Wdate" type="text" value="${ETime}" onClick="WdatePicker({dateFmt:'yyyy-MM',onpicked:pickedFunc})" />
			 </td>
 			<td style="width:40px;" align="center">科室:</td>
			<td id = "classA" class="newMenu" style="width:110px;z-index:1;position: relative;">
				<div class="deptInput menuInput">
					<input id="deptName" class="ksnew" readonly/><span></span></div>
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
						<ul class="addDept">
						</ul>											
					</div>	
					<div class="depts-dl">
					<div class="addList"></div>
					<div class="tip" style="display:none">没有检索到数据</div>
					</div>	
							
			</td>
		    <td>
		    	<!-- <input class="easyui-combobox" id="deptName" name="deptName" />  -->
	 			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchFrom()" iconCls="icon-search" style="margin-left: 8px">查询</a>
	 			<a href="javascript:clear();void(0)" class="easyui-linkbutton" onclick="clear()" iconCls="reset">重置</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportPDF()" iconCls="icon-printer">打印</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportList()" iconCls="icon-down">导出</a>
			</td>
			</tr>
			</table>
		</form>
			<div data-options="region:'north'" style="height:6%;width: 100%;padding:5px 5px 0px 5px;">
  			<table style="width:100%;z-index: 0">
		    		<tr>
		    			<td align="center"><span id="caption" style="font-size:28px !important;"></span></td>
		    		</tr>
		    	</table>
		</div>
	    <div data-options="region:'center',border:false" style="width: 100%;height: 88%;padding: 5px;">
	    	<table id="illMedical" class="easyui-datagrid" style="padding:5px 5px 5px 5px;" data-options="singleSelect:'true',striped:true,fitColumns:true,rownumbers:true,fit:true">
				<thead>
					<tr>
						<th data-options="field:'deptName',width:'15%',align:'center'">科室</th>
						<th data-options="field:'xiangmu',width:'15%',halign:'center'">项目</th>
						<th data-options="field:'lastDate',width:'15%',align:'center'" ><span id="yearOne"></span></th>
						<th data-options="field:'date',width:'15%',align:'center'"><span id="yearTwo"></span></th>
						<th data-options="field:'differ',width:'15%',align:'center'">增减数<font size="1">(备注2)</font></th>
						<th data-options="field:'differPer',width:'15%',align:'center'">增减率(%)<font size="1">(备注2)</th>
					</tr>
				</thead>
			</table>
			<form id="saveForm" method="post">
			    <input type="hidden" name="rows" id="rows" value=""/>
			    <input type="hidden" name="exlToLastTime" id="exlToLastTime" value=""/>
			    <input type="hidden" name="exlToTime" id="exlToTime" value=""/>
			    <input type="hidden" name="viewTitle" id="viewTitle" value=""/>
		    </form>
			<form method="post" id="reportToHiddenForm">
				<input type="hidden" name="exlToLastTime" id="toLastTime" value=""/>
			    <input type="hidden" name="exlToTime" id="toTime" value=""/>
				<input type="hidden" name="viewCaption" id="viewCaption" value=""/>
				<input type="hidden" name="reportToRows" id="reportToRows" value=""/>
				<input type="hidden" name="fileName" id="reportToFileName" value=""/>
			</form>
	    </div> 
	    </div>  
</body>
</html>
