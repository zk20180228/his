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
<title>病症与病种诊断</title>
</head>
<body >

<div id="layout" class="easyui-layout" style="width:100%;height:100%;">   
    <div style="width:100%;height:4%;">
	   <div style="margin-top:8px;margin-left:5px;">
	    	<table id="searchTab" style="width: 100%;">
							<tr>
								<td style="width:80px;" align="center">病情特征:</td>
				    			<td style="width:150px;" class="newMenu">
					    	       	<div class="deptInput menuInput" style="width:150px"><input style="width:120px" class="ksnew" id="ksnew"  style="height:25px;" readonly="readonly"/><span></span></div> 
					    	       	<div id="m2" class="xmenu" style="display: none;">
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
											<label class="top-label">已选病情特征：</label>																						
											<ul class="addDept">
											
											</ul>											
										</div>	
										<div class="depts-dl">
											<div class="addList"></div>
											<div class="tip" style="display:none">没有检索到数据</div>
										</div>													 
									</div>
								</td>
								<td style="padding-left: 10px;">
									<a href="javascript:void(0)"  id="search" onclick="searchList()" class="easyui-linkbutton"  iconCls="icon-search" style="height:25px;">查询</a>
									<a href="javascript:void(0)"  id="reset" onclick="resetList()" class="easyui-linkbutton" iconCls="reset" style="height:25px;">重置</a>
								</td>
							</tr>
						</table>
    	</div>
    </div>   
    
    <div style="width:100%;height:95%">
   		<table class="easyui-datagrid" id="list" style="width:100%;height:100%">   
		    <thead>   
		        <tr>   
		            <th data-options="field:'feature',width:120,align:'center'">病情特征</th>   
		            <th data-options="field:'result',width:80">病种诊断</th>   
		            <th data-options="field:'matchingDegree',width:50">匹配度</th>   
		        </tr>   
		    </thead>   
		</table>  
    </div>  
</div>  

</body>
</html>
<script type="text/javascript">
//合并单元格
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



$(function(){
	
	$("#list").datagrid({
		method: 'post',
		url:"${pageContext.request.contextPath}/statistics/diagnoseCount/diagnoseList.action",
		fit: true,
		singleSelect: true,
		remoteSort: false,
		pagination: true,
		pageSize: 20,
		fitColumns:true,
		rownumbers:true,
		pageList: [20, 30, 50, 100],
		onLoadSuccess: function(data) {
			//分页工具栏作用提示
			var pager = $(this).datagrid('getPager');
			var aArr = $(pager).find('a');
			var iArr = $(pager).find('input');
			$(iArr[0]).tooltip({
				content: '回车跳转',
				showEvent: 'focus',
				hideEvent: 'blur',
				hideDelay: 1
			});
			for(var i = 0; i < aArr.length; i++) {
				$(aArr[i]).tooltip({
					content: toolArr[i],
					hideDelay: 1
				});
				$(aArr[i]).tooltip('hide');
			}
			
			//加载完成，合并单元个
			$('#list').datagrid("autoMergeCells", ['feature']);
		}
	
	});
	
	//选择科室
	 $(".deptInput").MenuList({
			width :530, //设置宽度，不写默认为530，不要加单位
			height :400, //设置高度，不写默认为400，不要加单位
			dropmenu:"#m2",//弹出层id，必须要写
			isSecond:false,	//是否是二级联动的第二级，false为第一级，true为第二级，不写默认为false
			para:"",//要传的参数，科室类型，多个参数逗号分开，如果不写，查询全部
			firsturl:"${pageContext.request.contextPath}/statistics/diagnoseCount/featureList.action", //获取列表的url，必须要写
			relativeInput:".doctorInput",	//与其级联的文本框，必须要写
			relativeDropmenu:"#m3"			//与其级联的弹出层，如果是第一级必须要写，第二级不需要写
	});
	
})

	//查询
	function searchList(){
		var features=$("#ksnew").val();	
		$('#list').datagrid('load',{
			feature:features
		});
		
	}
	
	
	
	//重置
	function resetList(){
		$("#ksnew").val("");	
		var features=$("#ksnew").val();	
		$('#list').datagrid('load',{
			feature:features
		});	
	}

</script>

<script type="text/javascript" src="<%=basePath%>javascript/js/jquery.deptMenu.js"></script>
<script type="text/javascript" src="<%=basePath%>javascript/js/easyui-combobox-showPanel.js"></script>