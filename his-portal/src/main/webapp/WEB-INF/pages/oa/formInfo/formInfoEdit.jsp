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
		<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/themes/cform/styles/xform.css" />
		<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/themes/cform/xform-all.js"></script>
		<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/themes/cform/adaptor.js"></script>
		<style type="text/css">
			* {
				box-sizing: border-box;
			}
			.xf-pallete {
				border: dotted 2px gray;
				width: 45%;
				margin: 5px;
				padding: 5px 15px 5px 5px;
				text-align: left;
				background-color: #F8F8F8;
				float: left;
				position: relative;
			}
			.xf-pallete img {
				position: absolute;
				right: 5px;
			}
			.xf-table td {
				height: 45px;
			}
			.tab-pane {
				margin-right: 20px;
			}
			.panel-body {
				padding: 15px 10px;
			}
			body,html{
				width: 100%;
				height: 100%;
				background-color: #ffffff;
			}
			.nav-tabs>li.active>a, .nav-tabs>li.active>a:focus, .nav-tabs>li.active>a:hover{
				background-color: #f3f3f3;
			}
			#xf-form {
			    margin-bottom: 50px;
			}
			#__gef_palette__ .control-label {
			    width: 100%;
			    overflow: hidden;
			    text-align: left;
			    margin: 0;
			}
		</style>
	</head>
	<body>
		<div class="row-fluid">
			<!-- start of main -->
			<div id="m-main" class="col-md-12" style="padding-top:20px;">
				<div id="__gef_container__" style="padding-left:5px;">
					<div id="__gef_palette__" style="width: 260px;position: fixed; left: 15px;top: 20px;">
						<ul class="nav nav-tabs" id="myTab">
							<li class="active">
								<a href="#operation" data-toggle="tab">控件</a>
							</li>
							<li>
								<a href="#form" data-toggle="tab">控件属性</a>
							</li>
							<li>
								<a href="#prop" data-toggle="tab">表单属性</a>
							</li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="operation">
								<div style="padding-top:5px;">
									<div class="xf-pallete" title="label">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_label.png"> 标题文本
									</div>
									<div class="xf-pallete" title="textfield">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_input.png"> 输入框
									</div>
									<div class="xf-pallete" title="password">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_secret.png"> 密码框
									</div>
									<div class="xf-pallete" title="textarea">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_textarea.png"> 签章组件
									</div>
									<div class="xf-pallete" title="select">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_select.png"> 下拉框
									</div>
									<div class="xf-pallete" title="radio">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_item.png"> 单选框
									</div>
									<div class="xf-pallete" title="checkbox">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_itemset.png"> 多选框
									</div>
									<div class="xf-pallete" title="fileupload">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_upload.png"> 文件上传
									</div>
									<div class="xf-pallete" title="datepicker">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/new_range.png"> 日期输入框
									</div>
									<div class="xf-pallete" title="userpicker">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/userpicker.png"> 文本域
									</div>
									<div class="xf-pallete" title="chilentTable">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/userpicker.png"> 子表组件
									</div>
									<div class="xf-pallete" title="userModlue">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/userpicker.png"> 人员选择
									</div>
									<div class="xf-pallete" title="departmentModlue">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/userpicker.png"> 科室选择
									</div>
									<div class="xf-pallete" title="LabelText">
										<img src="${pageContext.request.contextPath}/themes/cform/images/xform/userpicker.png"> 纯文本
									</div>
								</div>
							</div>
							<div class="tab-pane" id="form">
								<div class="panel panel-theme" style="display:block;position:relative;">
									<div class="panel-heading">控件属性</div>
									<div class="panel-body">
										<div id="xf-form-attribute" class="controls"></div>
									</div>
								</div>
							</div>
							<div class="tab-pane" id="prop">
								<div class="panel panel-theme" style="display:block;position:relative;">
									<div class="panel-heading">表单属性</div>
									<div class="panel-body">
										<div id="xf-form-attribute" class="controls">
											<label>
											  名称
										      <input id="xFormName" type="text" class="form-control">
											</label>
											<label>
											  标识
										      <input id="xFormCode" type="text" class="form-control">
						                    </label>
						                    <label>
											  公式
										      <input id="xFormmula" type="text" class="form-control">
						                    </label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="__gef_center__" style="margin-left: 260px;">
						<div id="__gef_toolbar__" style = "position: fixed;top: 20px;">
							<div class="btn-group">
								<button class="btn btn-default" onclick="doSave('<%=basePath%>oa/formInfo/saveFormInfo.action')">保存</button>
								<button class="btn btn-default hidBtn" onclick="addRow()" id="addRow">添加行</button>
								<button class="btn btn-default hidBtn" onclick="doMerge()" id="mergeCell">合并单元格</button>
								<button class="btn btn-default hidBtn" onclick="doSplit()" id="splitCell">拆分单元格</button>
								<button class="btn btn-default hidBtn" onclick="addCol()" id="addCol">添加列</button>
								<button class="btn btn-default hidBtn" onclick="removeRow()" id="removeRow">删除行</button>
								<button class="btn btn-default hidBtn" onclick="removeCol()" id="removeCol">删除列</button>
<!-- 								<button class="btn btn-default hidBtn" onclick="closeWin()" id="closeWin">关闭</button> -->
							</div>
						</div>

						<div id="__gef_canvas__" style="overflow:auto;padding-top: 36px">
							<div id="xf-center" class="xf-center" unselectable="on">
								<!--<div id="xf-layer-form" class="xf-layer-form">-->
								<form id="xf-form" action="#" method="post" class="controls radio-theme"></form>
								<!--</div>-->
								<div id="xf-layer-mask" class="xf-layer-mask"></div>
							</div>
						</div>
					</div>

				</div>
			</div>
			<!-- end of main -->
			<form id="f" action="form-template-save.do" method="post" style="display:none;">
				<c:if test="${formInfo != null}">
					<input id="__gef_id__" name="formInfo.id" type="text" value="${formInfo.id}">
				</c:if>
				<input id="__gef_name__" name="formInfo.formName" value="${formInfo.formName}">
				<input id="__gef_code__" name="formInfo.formCode" value="${formInfo.formCode}">
				<input id="__gef_mula__" name="formInfo.formMula" value="${formInfo.formMula}">
				<textarea id="__gef_content__" name="formInfo.formInfoStr">${formInfo.formInfoStr}</textarea>
			</form>
		</div>
	</body>
</html>