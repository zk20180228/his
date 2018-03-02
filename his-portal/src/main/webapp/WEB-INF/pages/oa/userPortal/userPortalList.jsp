<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

	<head>
		<title>OA主页</title>
		<%@ include file="/common/metas.jsp"%>
		<style type="text/css">
			* {
				box-sizing: border-box;
			}
			
			body {
				overflow-x: hidden;
				-webkit-touch-callout: none;
				-webkit-user-select: none;
				-khtml-user-select: none;
				-moz-user-select: none;
				-ms-user-select: none;
				user-select: none;
			}
			
			a {
				color: #000000;
			}
			
			a:hover {
				text-decoration: none;
			}
			
			.panelBox {
				margin: 15px 0;
				height: 360px;
			}
			
			.panelBox a {
				display: block;
			}
			
			.panel {
				background: transparent;
				overflow: visible;
			}
			
			.panel-heading {
				border: 1px solid #D5DCDF;
				position: relative;
			}
			.panel-header,
			.panel-body{
				border-width: 0 !important;
			}
			.panTitle {
				font-family: "微软雅黑";
			}
			
			#modelUl .panel .panel-body {
				height: 326px;
			}
			
			#modelUl .panel .panel-heading {
				height: 34px;
				line-height: 34px;
				padding: 0 10px 0 17px;
			}
			
			.btnBox {
				float: right;
				height: 100%;
				position: relative;
				width: 60px;
				line-height: 25px;
				margin: 0;
			}
			
			.btnBox .Abtn {
				height: 25px;
				width: 25px;
				display: block;
				padding: 0;
				background-color: #E0E3E3;
				border-width: 0;
				line-height: 25px;
				text-align: center;
				float: left;
				margin-left: 5px;
				margin-top: 5px;
			}
			
			.btnBox .Abtn i {
				width: 100%;
				height: 100%;
				margin: 0;
				padding: 0;
			}
			
			.panel-body ul {
				width: 100%;
				padding: 0;
				margin: 0;
			}
			
			.panel-body ul>li {
				height: 36px;
				line-height: 36px;
				border-top: 1px solid #D5DCE1;
				position: relative;
			}
			
			.panel-body ul>li:first-child {
				border-top: none;
			}
			
			.panel-body .listinfo {
				display: block;
				width: 100%;
				padding-left: 17px;
				padding-right: 110px;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.panel-body .listinfo2 {
				display: block;
				width: 100%;
				padding-left: 17px;
				padding-right: 220px;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.panel-body .toolInfo {
				display: block;
				width: 100%;
				padding-left: 17px;
				padding-right: 150px;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			.panel-body .listTime {
				display: block;
				width: 95px;
				padding-right: 7px;
				text-align: center;
				position: absolute;
				right: 0;
				top: 0;
			}
			
			.panel-body .listTime2 {
				display: block;
				width: 95px;
				padding-right: 7px;
				text-align: center;
				position: absolute;
				right: 110px;
				top: 0;
			}
			
			.panel-body .listCheck {
				display: block;
				width: 65px;
				padding-right: 7px;
				text-align: center;
				position: absolute;
				right: 0;
				top: 0;
			}
			
			.panel-body .listCheck2 {
				display: block;
				width: 65px;
				height: 100%;
				padding: 5px 0;
				padding-right: 7px;
				text-align: center;
				position: absolute;
				right: 15px;
				top: 0;
			}
			
			.panel-body .listCheck2 a {
				width: 100%;
				height: 100%;
				line-height: 14px;
			}
			
			
			.panel-body .listCheck3 {
				display: block;
				width: 65px;
				height: 100%;
				padding-right: 7px;
				text-align: center;
				position: absolute;
				right: 15px;
				top: 0;
			}
			.panel-body li:hover .infolist {
				color: #FF9326;
			}
			#modelUl>li .panel {
				box-shadow: 0px 5px 20px 0px #CCCCCC;
			}
			
			.panelBox.line {
				border: dashed 1px gray !important;
			}
			
			.tableTitle:hover {
				color: #000000;
			}
			
			.addBox {
				width: 50px;
				height: 50px;
				font-size: 50px;
				position: fixed;
				right: 30px;
				bottom: 30px;
				z-index: 1;
				top: auto;
				line-height: 50px;
				text-align: center;
				cursor: pointer;
			}
			
			.lodingIndex {
				width: 50px;
				height: 50px;
				text-align: center;
				margin: 135px auto 0 auto;
				display: block;
			}
			.timeCanvae {
				margin: 0 auto;
				width: 320px;
				height: 320px;
			}
			.dwo img{
			    text-align: center;
    			margin: 0 auto;
    			display: block;
			}
		</style>

		<script type="text/html" id="indexTemplate1">
			<div class="panel panel-default" moduleId="{{data.widget}}" name="{{data.name}}" order="{{data.order}}" dataId="{{data.id}}">
				<div class="panel-heading">
					<i class="glyphicon glyphicon-list"></i>
					<span class="fontM panTitle">{{data.name}}</span>
					<div class="btnBox">
						<a class="Abtn resBtn"><i class="honryicon icon-spinner11"></i></a>
						<a class="Abtn" aria-expanded="true" data-toggle="dropdown"><i class="dataBtn honryicon icon-zhuti"></i></a>
						<ul style="min-width: 70px;" aria-labelledby="dropdownMenu1" role="menu" class="dropdown-menu">
							<li>
								<a href="javascript:void(0);updateWidget('{{data.widget}}', '{{data.order}}', '{{data.name}}','{{data.id}}')"><i class="glyphicon glyphicon-pencil"></i> 编辑</a>
							</li>
							<li>
								<a href="javascript:void(0);delMoudel('{{data.id}}')" class="remove-panel" ><i class="glyphicon glyphicon-remove"></i> 移除</a>
							</li>
							{{if data.moreUrl}} 
								<li>
									<a href="javascript:openp('{{data.moreUrl}}','{{data.name}}');" class="remove-panel" ><i style="font-size: 95%;" class="honryicon icon-gengduo"></i> 更多</a>
								</li>
							{{/if}}
						</ul>
					</div>
				</div>
				<div class="panel-body module{{data.widget}}">

				</div>
			</div>
		</script>
		<script type="text/html" id="indexTemplate1Data">
			{{if data.length != 0}}
			<ul>
				<li>
					<a class="tableTitle">
						<span style="font-weight: 900;font-size: 14px;" class="listinfo">标题</span>
						<span style="font-weight: 900;font-size: 14px;" class="listTime">时间</span>
					</a>
				</li>
				{{each data as value}}
				<li>
					<a class="infolist" href="javascript:openp('<%=basePath%>sys/noticeManage/contentView.action?info.id={{value.id}}','{{value.NavTitle}}');">
						<span class="listinfo">{{value.infoTitle}}</span>
						<span class="listTime">{{value.infoPubtime}}</span>
					</a>
				</li>
				{{/each}}
			</ul>
			{{/if}}
		</script>
		<script type="text/html" id="indexTemplate7Data">
			<iframe name="weather_inc" src="http://i.tianqi.com/index.php?c=code&id=35&icon=1&num=3" width="100%" height="70" frameborder="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>
		</script>
		<script type="text/html" id="indexTemplate8Data">
				<ul>
					<li>
						<span style="font-weight: 900;font-size: 14px;" class="toolInfo">工具名称</span>
						<span style="font-weight: 900;padding: 0 7px 0 0;font-size: 14px;" class="listCheck2">操作</span>
					</li>
					<li>
						<span class="toolInfo"><a href="javascript:openp('<%=basePath%>oa/Wages/toEmployee.action','工资查询')">工资查询</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href="javascript:openp('<%=basePath%>oa/Wages/toEmployee.action','工资查询')">打开</a>
						</span>
					</li>
					<li>
						<span class="toolInfo"><a href="javascript:openp('<%=basePath%>oa/agenda/agendaAction/agendaActionToView.action','日程安排')">日程安排</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href="javascript:openp('<%=basePath%>oa/agenda/agendaAction/agendaActionToView.action','日程安排')">打开</a>
						</span>
					</li>
					<li>
						<span class="toolInfo"><a href="javascript:openp('<%=basePath%>oa/publicAddressBook/publicAddressBookList.action','公共通讯录')" >公共通讯录</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href="javascript:openp('<%=basePath%>oa/publicAddressBook/publicAddressBookList.action','公共通讯录')" >打开</a>
						</span>
					</li>
					<li>
						<span class="toolInfo"><a href="javascript:openp('<%=basePath%>oa/personalAddressList/listPersonalAddress.action','个人通讯录')">个人通讯录</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href="javascript:openp('<%=basePath%>oa/personalAddressList/listPersonalAddress.action','个人通讯录')">打开</a>
						</span>
					</li>		
					
					<li>
						<span class="toolInfo"><a href="javascript:openp('<%=basePath%>activiti/queryFlow/myselfFlowView.action','业务申请')">业务申请</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href="javascript:openp('<%=basePath%>activiti/queryFlow/myselfFlowView.action','业务申请')">打开</a>
						</span>
					</li>	
					<li>
						{{if data.fileurl}}
						<span class="toolInfo"><a href={{data.fileurl}} download="移动客户端{{data.fileName}}.apk">移动客户端{{data.fileName}}</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href={{data.fileurl}} download="移动客户端{{data.fileName}}.apk">下载</a>
						</span>
						{{else}}
						<span class="toolInfo"><a href="" download="移动客户端{{data.fileName}}.apk">移动客户端{{data.fileName}}</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" href="" download="移动客户端{{data.fileName}}.apk">下载</a>
						</span>
						{{/if}}
					</li>	
					<li>
						<span class="toolInfo"><a onclick="versionModalclick()" >扫码下载移动客户端</a></span>
						<span class="listCheck2">
							<a class="btn btn-theme" onclick="versionModalclick()">打开</a>
						</span>
					</li>	
				</ul>
		</script>
		<script type="text/html" id="indexTemplate9Data">
			{{if data.length != 0}}
			<ul>
				<li>
					<a class="tableTitle">
						<span style="font-weight: 900;font-size: 14px;" class="listinfo">标题</span>
						<span style="font-weight: 900;font-size: 14px;" class="listTime">时间</span>
					</a>
				</li>
				{{each data as value}}
				<li>
					<a class="infolist"  href="javascript:openp('<%=basePath%>oa/information/view.action?infoid={{value.id}}&menuId={{value.infoMenuid}}','{{value.NavTitle}}');">
						<span class="listinfo">{{value.infoTitle}}</span>
						<span class="listTime">{{value.infoPubtime}}</span>
					</a>
				</li>
				{{/each}}
			</ul>
			{{/if}}
		</script>
		<script type="text/html" id="indexTemplate10Data">
			{{if data.length != 0}}
			<ul>
				<li>
					<a class="tableTitle">
						<span style="font-weight: 900;font-size: 14px;" class="listinfo2">标题</span>
						<span style="font-weight: 900;font-size: 14px;" class="listTime2">时间</span>
						<span style="font-weight: 900;padding: 0 7px 0 0;font-size: 14px;" class="listCheck2">审批</span>
					</a>
				</li>
				{{each data as value}}
				<li>
					<span class="listinfo2">{{value.infoTitle}}</span>
					<span class="listTime2">{{value.infoPubtime}}</span>
					<span class="listCheck2">
						<a class="btn btn-theme" href="javascript:openp('<%=basePath%>oa/information/toAudit.action?infoid={{value.id}}','文章审核');">审批</a>
					</span>
				</li>
				{{/each}}
			</ul>
			{{/if}}
		</script>
		<script type="text/html" id="indexTemplate14Data">
			{{if data.length != 0}}
			<ul>
				<li>
					<a class="tableTitle">
						<span style="font-weight: 900;font-size: 14px;" class="listinfo2">标题</span>
						<span style="font-weight: 900;font-size: 14px;" class="listTime2">时间</span>
						<span style="font-weight: 900;padding: 0 7px 0 0;font-size: 14px;" class="listCheck2">办理</span>
					</a>
				</li>
				{{each data as value}}
				<li>
					<span class="listinfo2">{{value.attr2}}</span>
					<span class="listTime2">{{value.createTime}}</span>
					<span class="listCheck2">
						<a class="btn btn-theme" href="javascript:AddOrShowEast('{{value.id}}','<%=basePath%>activiti/humanTask/viewTaskForm.action');">办理</a>
					</span>
				</li>
				{{/each}}
			</ul>
			{{/if}}
		</script>
		<script type="text/html" id="indexTemplate15Data">
			{{if data.length != 0}}
			<ul>
				<li>
					<a class="tableTitle">
						<span style="font-weight: 900;font-size: 14px;" class="listinfo2">标题</span>
						<span style="font-weight: 900;font-size: 14px;" class="listTime2">时间</span>
						<span style="font-weight: 900;padding: 0 7px 0 0;font-size: 14px;" class="listCheck2">状态</span>
					</a>
				</li>
				{{each data as value}}
				<li>
					<span class="listinfo2">{{value.title}}</span>
					<span class="listTime2">{{value.time}}</span>
					<span class="listCheck3">
						{{if value.isFinish == 1}}
						完成
						{{else}}
						未完成
						{{/if}}
					</span>
				</li>
				{{/each}}
			</ul>
			{{/if}}
		</script>
		<script type="text/html" id="tableInfo">
			<table class="table table-hover tableStatus">
				<thead>
					<tr>
						<th>组件名称</th>
						<th>个人状态</th>
						<th>全局状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					{{each data as value}}
					<tr>
						<td>{{value.name}}</td>
						{{if value.local == 0}}
						<td>正常</td>
						{{else}}
						<td>停用</td>
						{{/if}} {{if value.global == 0}}
						<td>正常</td>
						{{else}}
						<td>停用</td>
						{{/if}} {{if value.global == 0 && value.local == 1}}
						<td>
							<buttom class="btn btn-theme" onclick="start('{{value.name}}','{{value.id}}','{{value.widget}}')">启&nbsp;用</buttom>
						</td>
						{{else}}
						<td><buttom class="btn btn-default disabled">已启用</buttom></td>
						{{/if}}
					</tr>
					{{/each}}
				</tbody>
			</table>
		</script>
		<script type="text/html" id = "versionModalTemplate">
			<div class="modal fade" id="versionModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title">App下载</h4>
			      </div>
			      <div class="dwo modal-body clearfix">
			      		<img src="{{data.fileurl}}" alt="安卓APP下载" />
			      </div>
			    </div>
			  </div>
			</div>
		</script>
	</head>

	<body>
		<ul id="modelUl" class="clearfix"></ul>
		<i onclick="addFn()" class="addBox fontBg glyphicon glyphicon-plus"></i>
		<div class="modal fade" id="ModalAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<ul class="nav nav-tabs" role="tablist">
							<li role="presentation" class="active">
								<a href="#home" aria-controls="home" role="tab" data-toggle="tab">
									<h4 class="modal-title" id="myModalLabel">新增/修改</h4></a>
							</li>
							<li role="presentation">
								<a href="#oldWidget" aria-controls="profile" role="tab" data-toggle="tab">
									<h4 class="modal-title" id="myModalLabel">已有组件</h4></a>
							</li>
						</ul>
					</div>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="home">
							<div class="form-horizontal">
								<div class="modal-body">
									<div class="form-group">
										<label class="col-lg-3 control-label">组件标题</label>
										<div class="col-lg-7">
											<input type="text" class="form-control" name="moudelTitle" id="moudelTitle" value="" />
										</div>
									</div>
									<div class="form-group">
										<label class="col-lg-3 control-label">组件名称</label>
										<div class="col-lg-7">
											<select class="form-control" id="moudelName" name="moudelName"></select>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button id="isAjax" type="button" class="btn btn-theme"><span class="ladda-label">确定</span></button>
									<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="oldWidget"></div>
					</div>
				</div>
			</div>
		</div>

	</body>
	<script src="<%=basePath%>javascript/js/jquery.OAindexDragsort.js" type="text/javascript" charset="utf-8"></script>
	<script src="<%=basePath%>javascript/js/canvasTime.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		// 模板策略函数
		var Fn = {
			"1": function(data, isData) { //渲染盒子和数据 2是组件id
				if(isData) {
					return template("indexTemplate1Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"2": function(data, isData) { //渲染盒子和数据 2是组件id
				if(isData) {
					return template("indexTemplate1Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"3": function(data, isData) { //渲染盒子和数据 2是组件id
				if(isData) {
					return template("indexTemplate1Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"4": function(data, isData) { //渲染盒子和数据 2是组件id
				if(isData) {
					return template("indexTemplate1Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"5": function(data, isData) { //渲染盒子和数据 2是组件id
				if(isData) {
					return template("indexTemplate1Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"6": function(data, isData, callBack) { //时间
				if(isData) {
					return "<div class = 'timeCanvae'><canvas id='canvasTime' width='320px' height='320px' style='margin: 0 auto;'></canvas></div>"
				} else {
					return template("indexTemplate1", data)
				}
			},
			"7": function(data, isData) { //渲染盒子和数据 id7为天气组件
				if(isData) {
					return template("indexTemplate7Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"8": function(data, isData) { //渲染盒子和数据 id8为常用工具组件
				if(isData) {
					return template("indexTemplate8Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"9": function(data, isData) { //渲染盒子和数据
				if(isData) {
					return template("indexTemplate9Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"10": function(data, isData) { //作为审批组件使用
				if(isData) {
					return template("indexTemplate10Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"11": function(data, isData) { //渲染盒子和数据
				if(isData) {
					return template("indexTemplate9Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"12": function(data, isData) { //渲染盒子和数据
				if(isData) {
					return template("indexTemplate9Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"13": function(data, isData) { //渲染盒子和数据
				if(isData) {
					return template("indexTemplate9Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"14": function(data, isData) { //我的待办
				if(isData) {
					return template("indexTemplate14Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			},
			"15": function(data, isData) { //我的提醒
				if(isData) {
					return template("indexTemplate15Data", data)
				} else {
					return template("indexTemplate1", data)
				}
			}
		}
		//模板执行函数
		FnCallBack = {
			"6": function() {
				new Time({
					canvas: $("#canvasTime")[0],
					bigRoundX: 150,
					bigRoundY: 150,
					bigRoundR: 120
				});
			}
		}
		//组件数据预处理
		var List = eval('${portalWidgetJson}');
		if(List == null) {
			List = []
		}
		var numeList = (function($) {
			//全部的组件数据
			var newNumeList = {}
			for(var i = 0; i < List.length; i++) {
				newNumeList[List[i]["id"]] = {
					url: List[i]["url"],
					name: List[i]["name"],
					status: List[i]["status"],
					moreUrl:List[i]["moreUrl"]
				}
			}
			return newNumeList;
		})($)
		
		//我有的组件
		var myMoudel = eval('${userPortalJson}');
		if(myMoudel == null) {
			myMoudel = []
		}
		//加入跟多按钮
		for (var i = 0 ;i<myMoudel.length;i++) {
			if(numeList[myMoudel[i]['widget']]){
				myMoudel[i]['moreUrl'] =  numeList[myMoudel[i]['widget']]['moreUrl']
			}
		}
		//状态查看
		$("#oldWidget").append(template("tableInfo", {
			data: myMoudel
		}))
		var data = myMoudel
		var tmpArr = []
		var noOaderAll = []
		for(var i = 0; i < data.length; i++) {
			if(data[i]["local"] == 0 && data[i]["global"] == 0) {
				if(data[i]["order"] == -1) {
					noOaderAll.push(Fn[data[i]["widget"]]({
						"data": data[i]
					}))
				} else {
					tmpArr[data[i]["order"]] = Fn[data[i]["widget"]]({
						"data": data[i]
					})
				}
			}
		}
		var newTmpArr = tmpArr.concat(noOaderAll)
		for(i = 0; i < newTmpArr.length; i++) {
			if(newTmpArr[i]) {
				$("#modelUl").append("<li id = 'moduleBox" + i + "' index = " + i + " class='panelBox col-md-4 col-sm-6'></li>")
				$("#moduleBox" + i).html(null).append(newTmpArr[i])
				setModle(i)
			}
		}
		if(List.length == $(".panel").length && List.length != 0) {
			$(".addBox").css("display", "none")
		}
		$(".btnBox").on("mousedown", function() {
			return false
		})
		$(".resBtn").off("click").on("click", function() {
			var box = $(this).parents(".panelBox")
			var index = box.attr("index")
			setModle(index)
			return false
		})
		if('${portalRefresh}'=='0'){
			setInterval(function(){
				timeRes()
			},600000)
		}
		function setModle(i) {
			var module = $("#moduleBox" + i + " .panel").attr("moduleId")
			$("#moduleBox" + i + " .module" + module).html(null).html("<img  class = 'lodingIndex'  src = '<%=basePath%>baseframe/images/loadingText.gif'>")
			$.ajax({
				type: "get",
				url: "oa/userPortal/"+numeList[module]["url"]+"?random="+new Date().getTime(),
				async: true,
				success: function(data) {
					for(var j = 0; j < data.length; j++) {
						if(data[j].infoTitle){
							if(data[j].infoTitle.length >= 9) {
								data[j].NavTitle = data[j].infoTitle.substr(0, 7) + "..."
							} else {
								data[j].NavTitle = data[j].infoTitle
							}
						}
					}
					$("#moduleBox" + i + " .module" + module).html(null).append(Fn[module]({
						"data": data
					}, true))
					if(FnCallBack[module] != null){
						FnCallBack[module]()
					}
				},
				error: function() {
					$.messager.alert("提示信息", "" + $("#moduleBox" + i + " .panel").attr("name") + "请求超时")
				}
			});
		}

		function updateWidget(moduleID, oder, name, dataId) {
			if(name == "&nbsp;") {
				var name = ""
			}
			$('#ModalAdd').modal()
			$("#moudelTitle").val(name)
			var moudelArr = []
			var str = "<option value=" + moduleID + ">" + numeList[moduleID]["name"] + "</option>"
			var tmp = true
			$(".panel").each(function(i, v) {
				moudelArr.push(v.getAttribute("moduleId"))
			})
			for(var key in numeList) {
				if(numeList[key]["status"] == 0) {
					tmp = true
					for(var sel in myMoudel) {
						if(myMoudel[sel]["local"] == 1 || myMoudel[sel]["widget"] == key) {
							tmp = false
							break
						}
					}
					if(tmp) {
						str += "<option value=" + key + ">" + numeList[key]["name"] + "</option>"
					}
				}
			}
			$("#moudelName").append(str)
			//获取当前容器
			var box;
			$(".panel").each(function(i, v) {
				if(this.getAttribute("moduleid") == moduleID) {
					box = $(this)
					return false
				}
			})
			$("#isAjax").off("click").on("click", function() {
				if(name != $("#moudelTitle").val()){
					var newTitle = $("#moudelTitle").val() ? $("#moudelTitle").val() : "&nbsp;"
					var newMoudelId = $("#moudelName").val()
					$.ajax({
						type: "post",
						url: "<%=basePath%>oa/userPortal/saveUserPortal.action",
						async: true,
						data: {
							title: newTitle,
							moudelId: newMoudelId,
							oder: oder,
							dataId: dataId
						},
						success: function(data) {
							location.reload()
						},
						error: function() {
							$.messager.alert("请求信息", "请求失败")
						}
					});
				}else{
					$('#ModalAdd').modal('hide')
				}
			})
			return false
		}
		function AddOrShowEast(id,url) {
			var w = $("body").width()*0.9
			var h = $("body").height()*0.9
			var id = id;
			var url = url;
			var name = '查看';
			var width = w;
			var height = h;
			var top = (window.screen.availHeight-30-height)/2;
			var left = (window.screen.availWidth-10-width)/2;
			if($("#winOpenFrom").length<=0){  
				var form = "<form id='winOpenFrom' action='"+url+"' method='post' target='"+name+"'>" +
						"<input type='hidden' id='winOpenFromInpId' name='humanTaskId'/></form>";
				$("body").append(form);
			}
			$('#winOpenFromInpId').val(id);
			openWindow('about:blank',name,width,height,top,left);
			$('#winOpenFrom').prop('action',url);
			$("#winOpenFrom").submit();
		}
		//打开窗口
		function openWindow(url,name,width,height,top,left){
			window.open(url, name, 'height=' + height + ',innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + top + ',left=' + left + ',status=no,toolbar=no,menubar=no,location=no,resizable=no,scrollbars=0,titlebar=no'); 
		}
		function addFn() {
			var str = ""
			var tmp = true
			for(var key in numeList) {
				if(numeList[key]["status"] == 0) {
					tmp = true
					for(var sel in myMoudel) {
						if(myMoudel[sel]["widget"] == key) {
							tmp = false
							break
						}
					}
					if(tmp) {
						str += "<option value=" + key + ">" + numeList[key]["name"] + "</option>"
					}
				}
			}
			$('#ModalAdd').modal()
			$("#moudelName").html(null).append(str)
			if(str == "") {
				$("#isAjax").attr("disabled", "disabled")
				$("#moudelName").attr("disabled", "disabled")
			} else {
				$("#isAjax").attr("disabled", false)
				$("#isAjax").attr("disabled", false)
			}
			//获取最后的一个顺序
			var panel = $(".panel"),
				oder;
			if(panel.length > 0) {
				oder = $(panel[panel.length - 1]).attr("order") - 0 + 1
			} else {
				oder = 0
			}
			$("#moudelTitle").val($("#moudelName option:selected").text())
			$("#moudelName").off("change").on("change", function() {
				$("#moudelTitle").val($("#moudelName option:selected").text())
			})
			$("#isAjax").off("click").on("click", function() {
				$("#isAjax").attr("disabled",true)
				var newTitle = $("#moudelTitle").val() ? $("#moudelTitle").val() : "&nbsp;"
				var newMoudelId = $("#moudelName").val()
				$.ajax({
					type: "post",
					url: "<%=basePath%>oa/userPortal/saveUserPortal.action",
					async: true,
					data: {
						title: newTitle,
						moudelId: newMoudelId,
						oder: -1
					},
					success: function(data) {
						location.reload()
						//$('#ModalAdd').modal('hide')
					},
					error: function(){
						alert("提示信息", "请求超时，请重试")
					}
				});
			})
			return false
		}

		function delMoudel(id) {
			$.ajax({
				type: "post",
				url: "<%=basePath%>oa/userPortal/delUserPortal.action",
				async: true,
				data: {
					dataId: id
				},
				success: function(data) {
					location.reload()
				},
				error: function() {
					alert("请求超时，请重试")
				}
			});
			return false
		}

		function start(newTitle, dataId, moudelId) {
			var panel = $(".panel"),
				oder;
			if(panel.length > 0) {
				oder = $(panel[panel.length - 1]).attr("order") - 0 + 1
			} else {
				oder = 0
			}
			$.ajax({
				type: "post",
				url: "<%=basePath%>oa/userPortal/enableUserWidget.action",
				async: true,
				data: {
					title: newTitle,
					moudelId: moudelId,
					oder: oder,
					dataId: dataId
				},
				success: function(data) {
					$("#oldWidget").html(null).append(template("tableInfo", {
						data: data
					}))
					//关闭模态框触发事件
					$('#ModalAdd').off("hide.bs.modal").on("hide.bs.modal", function() {
						location.reload()
					})
				},
				error: function() {
					alert("请求超时，请重试")
				}
			});
		}

		function openp(url, title) {
			openNav(url, title, "homeInfoList")
		}
		$("#modelUl").dragsort({
			dragSelector: ".panel-heading",
			dragBetween: true,
			dragEnd: saveOrder,
			placeHolderTemplate: "<li class='panelBox line col-md-4 col-sm-6'><div></div></li>"
		});

		function saveOrder() {
			var data = $("#modelUl>li").map(function(index) {
				var tmp = $(this).children(".panel").attr("moduleid")
				if(tmp) {
					return tmp + "|" + index
				}
			}).get();
			$.ajax({
				type: "get",
				url: "<%=basePath%>oa/userPortal/moveWidget.action",
				async: true,
				data: {
					dataJson: data.join(",")
				},
				success: function() {}
			});
		}
		function timeRes (){
			$(".panelBox .panel").each(function(i,v){
				var moduleid = $(v).attr("moduleid")
				if(moduleid != "6" && moduleid != "8"){ //只刷新审核数据
					$.ajax({
						type:"post",
						url:"oa/userPortal/"+numeList[moduleid]["url"]+"?random="+new Date().getTime()+"&_reload=1",
						async:true,
						success:function(data){
							for(var j = 0; j < data.length; j++) {
								if(data[j].infoTitle){
									if(data[j].infoTitle.length >= 9) {
										data[j].NavTitle = data[j].infoTitle.substr(0, 7) + "..."
									} else {
										data[j].NavTitle = data[j].infoTitle
									}
								}
							}
							$(v).find(".panel-body").html(null).append(Fn[moduleid]({
								"data": data
							}, true))
							if(FnCallBack[moduleid] != null){
								FnCallBack[moduleid]()
							}
						},
						error:function(){
						
						}
					});
				}
				    
			})
		}
		function daibanRes (){
			$(".panelBox .panel").each(function(i,v){
				var moduleid = $(v).attr("moduleid")
				if(moduleid == 14){ //只刷代办数据
					$.ajax({
						type:"post",
						url:numeList[moduleid]["url"],
						async:true,
						success:function(data){
							for(var j = 0; j < data.length; j++) {
								if(data[j].infoTitle){
									if(data[j].infoTitle.length >= 9) {
										data[j].NavTitle = data[j].infoTitle.substr(0, 7) + "..."
									} else {
										data[j].NavTitle = data[j].infoTitle
									}
								}
							}
							$(v).find(".panel-body").html(null).append(Fn[moduleid]({
								"data": data
							}, true))
							if(FnCallBack[moduleid] != null){
								FnCallBack[moduleid]()
							}
						},
						error:function(){
						
						}
					});
				}
				    
			})
		}
		
		
		//app下载
		function versionModalclick (){
			$.ajax({
				type:"post",
				url:"<%=basePath%>oa/userPortal/downLoadApk.action",
				async:true,
				success:function(data){
					var tmp =  template("versionModalTemplate", {data:data})
					$("body").append(tmp)
					$("#versionModal").modal()
				}
			});
		}
		
	</script>

</html>