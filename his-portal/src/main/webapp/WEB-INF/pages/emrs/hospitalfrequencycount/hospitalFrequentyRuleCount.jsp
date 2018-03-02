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
<title>科室质控书写频率统计</title>
<style type="text/css">
		.panel-body,.panel{
			overflow:visible;
		}
</style>
<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript">
var medicTypeMap = new Map();
	$(function(){
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
		$('#tt').tree({
			url:'<%=basePath%>emrs/hospitalCount/getFrequencyPat.action',
			dnd:true,
			onDblClick:function(node){
				var dept = $('#ksnew').getMenuIds();
				$('#list').datagrid('load',{inpatientNo:node.id,deptCode:dept});
			}
		});
		$('#list').datagrid({
			url:'<%=basePath%>emrs/hospitalCount/getHospitalFreRuleCount.action'
		});
		$.ajax({
			url:"<%=basePath%>emrs/medcialRecord/getemrtypeCombox.action",
			success: function(data){
				for(var i=0;i<data.length;i++){
					medicTypeMap.put(data[i].encode,data[i].name);
				}
			}
		});
	});
	/**
	 * 查询
	 */
	function searchFrom() {
		var dept = $('#ksnew').getMenuIds();
		$('#list').datagrid('load',{});
		$('#tt').tree({
			queryParams:{deptCode:dept}
		});
	}
	//超时渲染
	function formatterTimeOut(value,row,index){
		if(value==1){
			return "√";
		}
	}
	//剩余时间装换成时分秒
	function formatterLeastT(value,row,index){
		return formatSeconds(value);
	}
	//渲染病历分类
	function formtterType(value,row,index){
		return medicTypeMap.get(value);
	}
	//将秒装换成时分秒
	function formatSeconds(value) {
		var flag = true;//true 正数  false负数
	    var theTime = parseInt(value);// 秒
	    if(theTime<0){
	    	flag = false;
	    	theTime = -parseInt(value);
	    }
	    var theTime1 = 0;// 分
	    var theTime2 = 0;// 小时
	    if(theTime > 60) {
	        theTime1 = parseInt(theTime/60);
	        theTime = parseInt(theTime%60);
	            if(theTime1 > 60) {
	            theTime2 = parseInt(theTime1/60);
	            theTime1 = parseInt(theTime1%60);
	            }
	    }
	        var result = ""+parseInt(theTime)+"秒";
	        if(theTime1 > 0) {
	        result = ""+parseInt(theTime1)+"分"+result;
	        }
	        if(theTime2 > 0) {
	        result = ""+parseInt(theTime2)+"小时"+result;
	        }
	        if(!flag){
	        	result = "-"+result;
	        }
	    return result;
	}
</script>
</head>
<body>
	<div id="cc" class="easyui-layout" style="width:100%;height:100%;">
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
    <div data-options="region:'west',title:'出院患者',split:true" style="width:200px;overflow: hidden;">
    	<ul id="tt"></ul>  
    </div>   
    <div data-options="region:'center'" style="padding:5px;">
    	<table id="list" class="easyui-datagrid" style="width:auto;height:auto" data-options="fitColumns:true,singleSelect:true">   
		    <thead>   
		        <tr>   
		            <th data-options="field:'type',width:100,formatter:formtterType">项目名称</th>   
		            <th data-options="field:'inTime',width:100">起始时间</th>   
		            <th data-options="field:'outTime',width:100">结束时间</th>   
		            <th data-options="field:'shouldTime',width:100">应写次数</th>   
		            <th data-options="field:'doneTime',width:100">完成次数</th>   
		            <th data-options="field:'undoneTime',width:100">未完成次数</th>
		        </tr>   
		    </thead>   
		</table>  
    </div>   
</div>  
</body>
</html>