//window.history.go(1);
        history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
		if(window != top) {
			window.top.location = 'login.jsp';
		}

		var brigIndex = -1;
		var roles;
		var depts;

		function getDeptAndRoleInfo(txtAccount, password) {
			$.ajax({
				url: "userLoginAction!getDeptAndRoleInfo",
				async: false,
				cache: false,
				data: {
					'txtAccount': txtAccount,
					'txtPassword': password
				},
				dataType: "json",
				type: "POST",
				success: function(o) {
					var status = o.status;
					if(status == '1') {
						if(password == '') {
							if(o.roles != '') {
								roles = eval(o.roles);
							} else {
								addRoles('');
								roles = '';
							}
							if(o.depts != '') {
								depts = eval(o.depts);
							} else {
								addDepts('');
								depts = '';
							}
						} else {
							if(roles != null && roles.length > 0) {
								addRoles(roles);
							}
							if(depts != null && depts.length > 0) {
								addDepts(depts);
							}
						}
					} else {
						if(password != '') {
							qikoo.dialog.alert(o.message);
							$(".slide div#roleDiv").css("display", "none");
						}
						addRoles(null);
						addDepts(null);
					}
				}
			});
		}

		function autoSearch(divId, textId, data) {
			var textVal = $("#" + textId).val();
			var autoDiv = $("#" + divId);
			var seaArr = new Array();
			var n = 0;
			for(var i = 0; i < data.length; i++) {
				if(data[i].indexOf(textVal) != -1) {
					seaArr[seaArr.length] = data[i];
				}
			}
			if(seaArr.length == 0) {
				autoDiv.hide();
				return;
			}
			autoDiv.empty();
			for(var i = 0; i < seaArr.length; i++) {
				var seaText = seaArr[i];
				var newDiv = $("<div>").attr("id", i);
				newDiv.attr("style", "font:14px/25px arial;height:25px;padding:0 8px;cursor: pointer;");
				newDiv.html(seaText).appendTo(autoDiv);

				newDiv.mouseover(function() {
					if(brigIndex != -1) {
						autoDiv.children("div").eq(brigIndex).css("background-color", "white");
					}
					brigIndex = $(this).attr("id");
					$(this).css("background-color", "#ebebeb");
				});

				newDiv.mouseout(function() {
					$(this).css("background-color", "white");
				});

				newDiv.click(function() {
					var comText = autoDiv.hide().children("div").eq(brigIndex).text();
					brigIndex = -1;
					$("#" + textId).val(comText);
					$("#txtPassword").focus();
				})
				if(seaArr.length > 0) {
					autoDiv.show();
				} else {
					autoDiv.hide();
					brigIndex = -1;
				}
			}
			document.onclick = function(e) {
				var e = e ? e : window.event;
				var tar = e.srcElement || e.target;
				if(tar.id != textId) {
					if($("#" + divId).is(":visible")) {
						$("#" + divId).css("display", "none")
					}
				}
				brigIndex = -1;
			}
			return seaArr.length;
		}

		function addRoles(roles) {
			var parent = document.getElementById('roleDiv');
			$('#roleDiv').html('');
			if(roles != null) {
				if(roles != '') {
					roles.forEach(function(value, index, array) {
						var p = document.createElement("p");
						p.setAttribute("id", value.id + '_' + value.isAdmin);
						p.setAttribute("class", value.icon);
						p.innerHTML = value.name + (value.isAdmin ? "(可不选择部门)" : "");
						parent.appendChild(p);
					});
				} else {
					var p = document.createElement("p");
					p.setAttribute("id", '');
					p.innerHTML = '该用户尚未关联角色！';
					parent.appendChild(p);
				}
			}
		}

		function addDepts(depts) {
			var parent = document.getElementById('deptDiv');
			$('#deptDiv').html('');
			if(depts != null) {
				if(depts != '') {
					depts.forEach(function(value, index, array) {
						var p = document.createElement("p");
						p.setAttribute("id", value.id);
						p.setAttribute("class", value.icon);
						p.innerHTML = value.name;
						parent.appendChild(p);
					});
				} else {
					var p = document.createElement("p");
					p.setAttribute("id", '');
					p.innerHTML = '该用户尚未关联部门！';
					parent.appendChild(p);
				}
			}
		}

		$(function() {
			if(navigator.userAgent.toLowerCase().match(/msie ([\d.]+)/) != null || !window.addEventListener) {
				document.body.innerHTML = ""
				var str = "<div class='alert-box'>" +
					"<p class='alert-title'>消息提醒</p>" +
					"<p class='alert-content'>您使用IE浏览器版本过低，请使用IE11浏览器或者更换其他浏览器！</p></div>"
				$("body").append(str)
				return false
			}
			var userStrCookie = $.cookie("username");
			if(userStrCookie != null && userStrCookie != '') {
				var userArrCookie = userStrCookie.split(',');
				$("#txtAccount").val(userArrCookie[0]);
				$("#txtPassword").focus();
				var old_value = $("#txtAccount").val();
				if(old_value != null && old_value != '') {
					getDeptAndRoleInfo(old_value, '');
				}
				$("#txtAccount").focus(function() {
					if($("#txtAccount").val() == "") {
						autoSearch("auto_div", "txtAccount", userArrCookie);
					}
				});
				$("#txtAccount").keyup(function(event) {
					$("#txtPassword").val('');
					$('#selectRole').val('');
					$('#selectRole').attr('name', '');
					$('#selectDept').val('');
					$('#selectDept').attr('name', '');
					var maxNum = autoSearch("auto_div", "txtAccount", userArrCookie);
					var max = parseInt(maxNum);
					var index = parseInt(brigIndex);
					if(event.keyCode == 38) {
						if(index == -1) {
							brigIndex = max - 1;
						} else {
							if(brigIndex > 0) {
								$('#' + index).css("background-color", "#white");
								brigIndex = index - 1;
							}
						}
						$('#' + brigIndex).css("background-color", "#ebebeb");
					}
					if(event.keyCode == 40) {
						if(index == -1) {
							brigIndex = 0;
						} else {
							if(index < max - 1) {
								$('#' + index).css("background-color", "#white");
								brigIndex = index + 1;
							}
						}
						$('#' + brigIndex).css("background-color", "#ebebeb");
					}
					if(event.keyCode == 13) {
						var _account = $("#txtAccount").val();
						var _password = $("#txtPassword").val();
						$("#txtAccount").val($('#auto_div').hide().children("div").eq(brigIndex).text());
						$("#txtPassword").focus();
					}
				});
			}

			$(".addshadow input").focus(function() {
				$(this).parent().addClass("shadow").siblings(".addshadow").removeClass("shadow");
			});
			$(".addshadow input").blur(function() {
				$(this).parent().removeClass("shadow");
			});
			$(".slide input").focus(function() {
				if($(this).parent().find("div").html().indexOf("p") == -1) {
					$(this).parent().find("div").css("display", "none");
				} else {
					$(this).parent().find("div").css("display", "block").parent().siblings("li").find("div").css("display", "none");
					$(this).parent().find("div").animate({
						'scrollTop': 0
					}, 10);
				}
			});
			$("body").on("click", ".slide div p", function() {
				$(this).parent().css("display", "none");
				var inp = $(this).parent().parent().find("input");
				$(inp).val($(this).html().split('(')[0]);
				$(inp).attr('name', $(this).attr('id'));
			});
			$("body").on("mouseover mouseout", ".slide div p", function() {
				if(event.type == "mouseover") {
					$(this).addClass("hoverp");
				} else if(event.type == "mouseout") {
					$(this).removeClass("hoverp");
				}
			});
			$(".form .slide i").click(function() {
				var slidewindow = $(this).parent().find("div");
				var slideinput = $(this).parent().find("input");
				if(slidewindow.css("display") == "block") {
					slidewindow.css("display", "none");
				} else {
					if(slidewindow.html().indexOf("p") == -1) {
						slidewindow.css("display", "none");
					} else {
						slidewindow.css("display", "block");
						slideinput.focus();
					}
				}
			});
			$(document).click(function(e) {
				var _con = $(".slide");
				var _con1 = $(".slide div");
				var _con2 = $(".mod-dialog");
				if(!_con.is(e.target) && _con.has(e.target).length == 0 && !_con1.is(e.target) && _con1.has(e.target).length == 0 && !_con2.is(e.target) && _con2.has(e.target).length == 0) {
					$(".slide div").css("display", "none");
					$(".slide div p").removeClass("hoverp");
				}
			});
			if($.cookie("isCheck") == 'true') {
				$('#checkId').attr("checked", true);
			}

			$("#submitBtn").click(function() {
				var _account = $("#txtAccount").val();
				if(_account == '') {
					qikoo.dialog.alert('账号不能为空!');
					return;
				}
				var _password = $("#txtPassword").val();
				if(_password == '') {
					qikoo.dialog.alert('密码不能为空!');
					return;
				}
				var _role = $('#selectRole').attr('name');
				if(_role == null || _role == '') {
					qikoo.dialog.alert('请选择角色!');
					return;
				}
				var roleArr = _role.split('_');
				var _dept = $('#selectDept').attr('name');
				if(roleArr[1] == 'false') {
					if(_dept == null || _dept == '') {
						qikoo.dialog.alert('请选择部门!');
						return;
					}
				}
				$.ajax({
					url: "userLoginAction!outOrIn",
					async: false,
					cache: false,
					data: {
						'txtAccount': _account
					},
					success: function(data){
						if(data.status == 'out'){
							$('#mobile').val(data.mobile).attr("readonly","readonly")
							$('#authcode').val('');
							$("#ManageInfoModal").modal({backdrop: 'static', keyboard: false})
							winBtnClose();
							centerModals()
							if(data.mobile==null||data.mobile==""){
								document.getElementById("codebtn").style = "width: 130px; float: left; border-radius: 4px; background-color: rgb(192, 192, 192);"
								sendOk("请通过内网登录,及完善个人手机号等信息！",false)
								return ;
							}
						}else{
							$.ajax({
								url: "userLoginAction!getUserInfo",
								async: false,
								cache: false,
								data: {
									'txtAccount': _account,
									'txtPassword': _password,
									'lRole': roleArr[0],
									'lDept': _dept.split('_')[0]
								},
								dataType: "json",
								type: "POST",
								success: function(o) {
									if(o.status) {
										var userCookie = $.cookie("username");
										if($('#checkId').is(":checked")) {
											if(userCookie != null && userCookie != '') {
												var userArr = userCookie.split(',');
												var index = $.inArray(_account, userArr);
												if(index == -1) {
													userCookie = _account + ',' + userCookie;
													$.cookie("username", userCookie, {
														expires: 1
													});
												} else {
													if(index != 0) {
														userArr.splice(index, 1);
														var userNowCookie = userArr.join(',');
														userNowCookie = _account + ',' + userNowCookie;
														$.cookie("username", userNowCookie, {
															expires: 1
														});
													}
												}
											} else {
												$.cookie("username", _account, {
													expires: 1
												});
											}
											$.cookie("isCheck", true, {
												expires: 1
											});
										} else {
											if(userCookie != null && userCookie != '') {
												var userArr = userCookie.split(',');
												var index = $.inArray(_account, userArr);
												userArr.splice(index, (index >= 0) ? 1 : 0);
												var userNowCookie = userArr.join(',');
												$.cookie("username", userNowCookie, {
													expires: 1
												});
											}
											$.cookie("isCheck", false, {
												expires: 1
											});
										}
										document.location = "mainAction!getMenu";
									} else {
										qikoo.dialog.alert(o.message);
										$("#txtPassword").val("");
									}
								}
							});
						}
					}
				});
				
			});
			function centerModals(){
			  $('.modal').each(function(i){
			    var $clone = $(this).clone().css('display', 'block').appendTo('body');
			    var top = Math.round(($clone.height() - $clone.find('.modal-content').height()) / 2);
			    top = top > 50 ? top : 0;
			    $clone.remove();
			    $(this).find('.modal-content').css("margin-top", top-50);
			  });
			}
			//点击键盘事件
			$(document).keydown(function(e) {
				var _account = $("#txtAccount").val();
				var _password = $("#txtPassword").val();
				var _role = $('#selectRole').val();
				var _dept = $('#selectDept').val();
				var _role1 = $('#selectRole').attr('name');
				var _dept1 = $('#selectDept').attr('name');

				//回车
				if(e.keyCode == 13) {
					if($(".mod-dialog").css("display") == "block") {
						$(".mod-dialog").css("display", "none");
						$(".mod-dialog-bg").css("display", "none");
						var tip = $(".mod-dialog .dialog-main .dialog-content p").html();
						if(tip == "用户名不能为空！") {
							$("#txtAccount").focus();
						} else if(tip == "密码不能为空！") {
							$("#txtPassword").focus();
						} else if(tip == "请选择角色！") {
							$("#selectRole").focus();
							$(".slide div#roleDiv").css("display", "block");
						} else if(tip == "请选择部门！") {
							$("#selectDept").focus();
							$(".slide div#deptDiv").css("display", "block");
						} else if(tip == "您输入的密码不正确！") {
							$("#txtPassword").focus();
						}
					} else if($("#txtAccount").is(":focus")) {
						if(_account != "") {
							$("#txtPassword").focus();
							$("#txtAccount").val(_account);
						} else {
							qikoo.dialog.alert("用户名不能为空！");
							$("#txtAccount").focus();
						}

					} else if($("#txtPassword").is(":focus")) {
						if(_password != "") {
							$("#selectRole").focus();
							$(".slide div#roleDiv").css("display", "block");
							$("#txtPassword").val(_password);
						} else {
							qikoo.dialog.alert("密码不能为空！");
							$("#txtPassword").focus();
						}
					} else if($("#selectRole").is(":focus")) {
						if($("#roleDiv p.hoverp").index() != -1) {
							var valueR = $("#roleDiv p.hoverp").attr("id");
							var rolename = $("#roleDiv p.hoverp").html();
							$("#selectRole").val(rolename.split('(')[0]);
							$("#selectRole").attr("name", valueR);
							_role = $("#selectRole").val();
						}
						if(_role != "") {
							$("#selectDept").focus();
							$("#selectRole").val(_role);
							$("#roleDiv p").removeClass("hoverp");
						} else {
							qikoo.dialog.alert("请选择角色！");
							$("#selectRole").focus();
							$(".slide div#roleDiv").css("display", "block");
						}
					} else if($("#selectDept").is(":focus")) {
						if($("#deptDiv p.hoverp").index() != -1) {
							var valueD = $("#deptDiv p.hoverp").attr("id");
							var deptname = $("#deptDiv p.hoverp").html();
							$("#selectDept").val(deptname);
							$("#selectDept").attr("name", valueD);
							_dept = $("#selectDept").val();
							_dept1 = $("#selectDept").attr("name");
						}
						var roleArr = _role1.split('_');
						if(roleArr[1] == 'false') {
							if(_dept == null || _dept == '') {
								qikoo.dialog.alert('请选择部门!');
								$("#selectDept").focus();
								$(".slide div#deptDiv").css("display", "block");
							} else {
								$("#selectDept").blur();
								$("#deptDiv").css("display", "none");
							}
						} else {
							$("#selectDept").blur();
							$("#deptDiv").css("display", "none");
							$("#roleDiv p").removeClass("hoverp");
						}

					}else if ($("#ManageInfoModal").css("display") == "block" ){
						if($("#authcode").val().length == 6){
							checkAuthCode()
						}
					}else {
						$("#submitBtn").click();
					}
				} else if(e.keyCode == 40) {
					if($("#selectRole").is(":focus")) {
						var flag = 0;
						var selectRole = $("#roleDiv p.hoverp").index();
						if(selectRole == -1) {
							flag = 0;
						} else if(selectRole == $("#roleDiv p").length - 1) {
							flag = 0;
						} else {
							flag = selectRole + 1;
						}
						var t = $("#roleDiv").scrollTop();
						if(selectRole >= 4) {
							$("#roleDiv").animate({
								'scrollTop': t + 34
							}, 100);
						}
						if(selectRole == $("#roleDiv p").length - 1) {
							$("#roleDiv").animate({
								'scrollTop': 0
							}, 100);
						}
						$("#roleDiv p").each(function(index, value) {
							if(flag == index) {
								$(this).addClass("hoverp").siblings().removeClass("hoverp");
							}
						});

					} else if($("#selectDept").is(":focus")) {
						var flag = 0;
						var selectDept = $("#deptDiv p.hoverp").index();
						if(selectDept == -1) {
							flag = 0;
						} else if(selectDept == $("#deptDiv p").length - 1) {
							flag = 0;
						} else {
							flag = selectDept + 1;
						}
						var t = $("#deptDiv").scrollTop();
						if(selectDept >= 4) {
							$("#deptDiv").animate({
								'scrollTop': t + 34
							}, 100);
						}
						if(selectDept == $("#deptDiv p").length - 1) {
							$("#deptDiv").animate({
								'scrollTop': 0
							}, 100);
						}
						$("#deptDiv p").each(function(index, value) {
							if(flag == index) {
								$(this).addClass("hoverp").siblings().removeClass("hoverp");
							}
						});
					}
				} else if(e.keyCode == 38) {
					if($("#selectRole").is(":focus")) {
						var flag = 0;
						var selectRole = $("#roleDiv p.hoverp").index();
						if(selectRole == -1) {
							flag = -2;
						} else if(selectRole == 0) {
							flag = $("#roleDiv p").length - 1;
						} else {
							flag = selectRole - 1;
						}
						var t = $("#roleDiv").scrollTop();
						var maintop = ($("#roleDiv p").length - 5) * 34;
						if(selectRole >= 4) {
							$("#roleDiv").animate({
								'scrollTop': t - 34
							}, 100);
						}
						if(selectRole == 0) {
							$("#roleDiv").animate({
								'scrollTop': maintop
							}, 100);
						}
						$("#roleDiv p").each(function(index, value) {
							if(flag != -2 && flag == index) {
								$(this).addClass("hoverp").siblings().removeClass("hoverp");
							}
						});
					} else if($("#selectDept").is(":focus")) {
						var flag = 0;
						var selectDept = $("#deptDiv p.hoverp").index();
						if(selectDept == -1) {
							flag = -2;
						} else if(selectDept == 0) {
							flag = $("#deptDiv p").length - 1;
						} else {
							flag = selectDept - 1;
						}
						var t = $("#deptDiv").scrollTop();
						var maintop = ($("#deptDiv p").length - 5) * 34;
						if(selectDept >= 4) {
							$("#deptDiv").animate({
								'scrollTop': t - 34
							}, 100);
						}
						if(selectDept == 0) {
							$("#deptDiv").animate({
								'scrollTop': maintop
							}, 100);
						}
						$("#deptDiv p").each(function(index, value) {
							if(flag != -2 && flag == index) {
								$(this).addClass("hoverp").siblings().removeClass("hoverp");
							}
						});
					}
				}
			});
			var prevUserAccount = "";
			$("#txtAccount").blur(function() {
				var _account = $.trim($("#txtAccount").val());
				if(prevUserAccount == "") {
					prevUserAccount = _account;
				}
				if(_account != "") {
					//getDeptAndRoleInfo(_account,'');
				}
				if(prevUserAccount != _account) {
					$("#txtPassword").val("");
					$("#selectRole").val("");
					$("#roleDiv").html("");
					$("#selectDept").val("");
					$("#deptDiv").html("");

					prevUserAccount = _account;
				}
			});
			$("#txtPassword").blur(function() {
				var _account = $.trim($("#txtAccount").val());
				var _password = $.trim($("#txtPassword").val());
				if(_account != "" && _password != "") {
					getDeptAndRoleInfo(_account, '');
					getDeptAndRoleInfo(_account, _password);
				}
			});

			$(document).keydown(function(event) {
				if(event.keyCode == 32) {
					var mesObj = $(".messager-body");
					if($(".messager-body").length > 0) {
						event.preventDefault();
						$(".messager-body").window('close');
						if($("#txtAccount").val() == null || $("#txtAccount").val() == '') {
							$("#txtAccount").focus();
						} else {
							$("#txtPassword").focus();
						}
					}
				}
			})

			var setObj = {
				main: $("#main-mid"),
				logo: $("#index-logo"),
				content: $("#index-content"),
				bottom_text: $("#index-bottom_text"),
				left_img: $("#index-left-img"),
				from_box: $("#from"),
				input_li: $("#from .login ul li"),
				content_left: $("#content_left"),
				h2Title: $("#h2Title")
			}

			function size(setObj) {
				setObj.input_li.children("span").each(function(i, v) {
					if(i < 4) {
						$(this).css("line-height", setObj.input_li.height() + "px")
					}
				})
				var h = setObj.main.height()
				setObj.logo.css("margin-top", h * 0.04)
				setObj.content.css("margin-top", h * 0.06)
				setObj.bottom_text.css({
					"margin-top": h * 0.05,
					"margin-bottom": h * 0.02
				})
				setObj.h2Title.css("lineHeight", setObj.h2Title.height() + "px")
				var diff = setObj.content_left.height() - setObj.content_left.width()
				if(diff > 0) {
					setObj.left_img.css("margin-top", diff / 2)
				} else {
					setObj.left_img.css("margin-top", 0)
				}

				if($("body").width() <= 800) {
					setObj.from_box.css("margin-top", 0)
				} else {
					setObj.from_box.css("margin-top", (setObj.content.height() - setObj.from_box.height()) / 2)
				}
			}
			if(window.addEventListener) {
				size(setObj)
				window.onresize = function() {
					size(setObj)
				}
			}

			/* 		//屏幕自适应
					var wheight = $(window).height();
					var divHeight = 757;
					var mainHeight = parseInt($(".logo").css("margin-top").substring(0,2))+parseInt($(".content").css("margin-top").substring(0,2))+parseInt($(".bottom_text").css("margin-top").substring(0,2))+757;
					if(mainHeight > wheight){
						var a = wheight - divHeight;
						var n = Math.floor(a/3);
						if(n < 0){
							n=0;
						}
						$(".logo").css("margin-top",n+"px");
						$(".content").css("margin-top",n+"px");
						$(".bottom_text").css("margin-top",n+"px");
					}*/
		});
		var t1;
		function openCountZero(){
			$('#codebtn').attr('disabled','disabled');
			//定时执行方法
			countDown()
			t1 = window.setInterval("countDown()",1000);
		}
		function closeCountZero(){
			clearTimeout(t1)
			sec = 60
			$('#codebtn').attr('disabled',false).css("background-color", "#017571").html('获取验证码').attr('href','javascript:createAuthCode();void(0)')
		}
		var sec = 60;
		function countDown(){
			sec--;
			$('#codebtn').html(sec+"秒后可重发");
			$("#codebtn").css("background-color", "#C0C0C0");
			$('#codebtn').removeAttr('href');
			if(sec<1){
				window.clearTimeout(t1);//去掉定时器 
				$('#codebtn').html("重获验证码");
				$('#codebtn').attr('href','javascript:createAuthCode();void(0)');
				$("#codebtn").css("background-color", "#017571");
				sec = 60;
				$("#infoTitleImg").css("display","none")
				$("#infoTitleInof").html("")
				$(".infoTitleBox").css("height","0")
			}
		}
		function sendOk (info,isOk){
			if(isOk){
				$("#infoTitleImg").css("display","none")
				$("#infoTitleInof").html(info)
				$(".infoTitleBox").css("height","14px")
			}else{
				$("#infoTitleImg").css("display","block")
				$("#infoTitleInof").html(info)
				$(".infoTitleBox").css("height","14px")
			}
		}
		
		function createAuthCode(){
			if(sec<60){
				return ;
			}
			var mobile = $('#mobile').val();
			if(mobile==null||mobile==""){
				sendOk('手机号不能为空！',false)
				return ;
			}
			openCountZero();
			$.ajax({
				url : "userLoginAction!createAuthCode",
				data :{"mobile" : mobile} ,
				type : "POST",
				success : function(r){
					if(r.resCode != "success"){
						sendOk("验证码发送失败，请重新发送！",false)
						closeCountZero();
					}else{
						sendOk("验证码发送成功，请注意查收！",true)
					}
//					$.messager.alert('提示',r.resMsg,'info');
				},
				error : function(){
					sendOk("验证码发送失败，请重新发送！",false)
					closeCountZero();
//					$.messager.alert('提示','未知错误','info');
				}
			});
		}
		function checkAuthCode(){
			var mobile = $('#mobile').val();
			var authcode = $('#authcode').val();
			if(mobile==null||mobile==""){
				sendOk('手机号不能为空！',false)
//				$.messager.alert('提示','手机号不能为空','info');
				return ;
			}
			if(authcode==null||authcode==""){
				sendOk('验证码不能为空！',false)
//				$.messager.aler('提示','...','info');
			}
			$.ajax({
				url : "userLoginAction!checkAuthCode",
				data :{"mobile" : mobile,msgcode : authcode} ,
				type : "POST",
				success : function(r){
					if(r.resCode=="success"){
						var _account = $("#txtAccount").val();
						var _password = $("#txtPassword").val();
						var _role = $('#selectRole').attr('name');
						var roleArr = _role.split('_');
						var _dept = $('#selectDept').attr('name');
						$.ajax({
							url: "userLoginAction!getUserInfo",
							async: false,
							cache: false,
							data: {
								'txtAccount': _account,
								'txtPassword': _password,
								'lRole': roleArr[0],
								'lDept': _dept.split('_')[0]
							},
							dataType: "json",
							type: "POST",
							success: function(o) {
								if(o.status) {
									var userCookie = $.cookie("username");
									if($('#checkId').is(":checked")) {
										if(userCookie != null && userCookie != '') {
											var userArr = userCookie.split(',');
											var index = $.inArray(_account, userArr);
											if(index == -1) {
												userCookie = _account + ',' + userCookie;
												$.cookie("username", userCookie, {
													expires: 1
												});
											} else {
												if(index != 0) {
													userArr.splice(index, 1);
													var userNowCookie = userArr.join(',');
													userNowCookie = _account + ',' + userNowCookie;
													$.cookie("username", userNowCookie, {
														expires: 1
													});
												}
											}
										} else {
											$.cookie("username", _account, {
												expires: 1
											});
										}
										$.cookie("isCheck", true, {
											expires: 1
										});
									} else {
										if(userCookie != null && userCookie != '') {
											var userArr = userCookie.split(',');
											var index = $.inArray(_account, userArr);
											userArr.splice(index, (index >= 0) ? 1 : 0);
											var userNowCookie = userArr.join(',');
											$.cookie("username", userNowCookie, {
												expires: 1
											});
										}
										$.cookie("isCheck", false, {
											expires: 1
										});
									}
									document.location = "mainAction!getMenu";
								} else {
									qikoo.dialog.alert(o.message);
									$("#txtPassword").val("");
								}
							}
						});
					}else{
						sendOk('验证失败,请重新验证！',false)
					}
				},
				error : function(){
					sendOk('未知错误',false)
				}
			});
		}
		function winBtnClose(){
			$("#winBtn").css("background-color", "#C0C0C0");
			$('#winBtn').removeAttr('href');
		}
		function winBtnOpen(){
			$('#winBtn').attr('href','javascript:checkAuthCode();void(0)');
			$("#winBtn").css("background-color", "#017571");
		}
		$(function(){
			$('#authcode').on('input',function(){
				var authcode = $('#authcode').val();
				if(authcode!=null&&authcode!=''&&authcode.length==6){
					winBtnOpen();
				}
			});
		});