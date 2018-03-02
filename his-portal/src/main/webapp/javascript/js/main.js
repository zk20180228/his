Edo.Main = function() {
	Edo.Main.superclass.constructor.call(this);
};
Edo.Main
		.extend(
				Edo.containers.Box,
				{
					width : '100%',
					height : '100%',
					verticalGap : 0,
					mainContent : '',
					load : function(url) {
						if (!url)
							return;
						this.mask();
					},
					onLoad : function(text) {
						this.componentMap = {};
						this.fireEvent('mainready', {
							type : 'mainready',
							source : this
						});

						this.unmask();
					},
					init : function() {
						var main = this;

						this.set(
							'children',
							[
								{
									type : "html",
									width : '100%',
									height : "74",
									html : '<div class="top"><img src="'+imagePath+'title.png" /><div class="rightArea"> <span class="loginUser"> <a href="javascript:void(0)" class="userLinks">'
											+ sDisplayUser
											+ '</a><font style=" color: #553000; margin: 0px 5px;">您好！</font> <a href="#" id="exitBtn" class="exitBtn">退出</a><a href="#" class="exitBtn" data-reveal-id="myModal">修改密码</a> </span> </div></div>'
								},
								{
									type : "html",
									width : '100%',
									height : "32",
									html : '<div class="nav">您当前所在的位置：<a id="fatherNode">首页</a> <span id="perch"></span> <a href ="javascript:void(0)" id="childNode"></a></div>'
								},
								{
									type : 'box',
									width : '100%',
									height : '100%',
									verticalGap : 0,
									padding : 0,
									border : [ 0, 0, 0, 0 ],
									children : [ {
										type : 'ct',
										width : '100%',
										height : '100%',
										layout : 'horizontal',
										verticalGap : 0,
										padding : 0,
										border : [ 0, 0, 0, 0 ],
										enableSplit : true,
										children : [
												{
													id:"leftMenu",
													type : "html",
													width : 209,
													border : [ 0, 0, 0, 0 ],
													padding : 0,
													height : "100%",
													html : '<div class="left"><span style=" display: inline-block; width: 66px; height: 30px; margin: 4px 0px; background:url('+imagePath+'financePic.png) no-repeat -88px 0px; position: absolute; right: 0px; top: 0px;"></span><div class="leftNav" id="nav">  </div><div id="stretchBtn" class="stretchBtn"></div></div>'
												},
												{
													type : 'box',
													width : '100%',
													height : '100%',
													verticalGap : 0,
													padding : 0,
													border : 0,
													cls : "right",
													children : [ {
														id : 'viewBox',
														type : 'box',
														width : '100%',
														height : '100%',
														layout : 'viewstack',
														border : [ 0, 0, 0, 0 ],
														cls : "right",
														children : [ {
															id : 'moduleViewer',
															type : 'module',
															border : [ 0, 0, 0, 0 ],
															layout : 'viewstack',
															style : 'background-color:white;border:0',
															minWidth : 300,
															width : '100%',
															height : '100%'
														} 
													]
												} 
											]
										} 
									]
								} 
							]
						},
						{
							type : "html",
							width : '100%',
							height : "32",
							html : '<div class="bottom" style=" background: url('+imagePath+'navBg.jpg) repeat-x;"><div style="  height: 32px; line-height: 32px; color: #fefefe; text-align: center;">博古海网络科技（北京）有限责任公司版权所有</div></div>'
						}
					]);

					Edo.Main.superclass.init.call(this);
				}
			});
	Edo.Main.regType('main');
	
	function openMenu(type,id,src,srcName,_src,_srcName){
//		if(type==1){
//			$(".mMenu.down").attr('class','mMenu');
//			$("#menu_"+id).attr('class','mMenu down');
//		}else{
//			$(".sMenu.on").attr('class','sMenu');
//			$("#menu_"+id).attr('class','sMenu on');
//		}
		moduleViewer.set('src', src);
		if(type==1){
			$('#fatherNode').attr('href',"javascript:openLoad(1,'"+id+"','"+src+"')");
			$('#fatherNode').html(srcName);
			$('#childNode').html(null);
			$('#perch').html(null);
		}
		if(type==2){
			$('#fatherNode').attr('href',"javascript:openLoad(1,'"+id+"','"+_src+"')");
			$('#fatherNode').html(srcName);
			$('#perch').html(">");
			$('#childNode').attr('href',"javascript:openLoad(2,'"+id+"','"+src+"')");
			$('#childNode').html(_srcName);
		}
	}
	function openLoad(type,id,src){
		if(type==1){
			$(".mMenu.down").attr('class','mMenu');
			$("#menu_"+id).attr('class','mMenu down');
			$('#childNode').html(null);
			$('#perch').html(null);
		}else{
			$(".sMenu.on").attr('class','sMenu');
			$("#menu_"+id).attr('class','sMenu on');
		}
		moduleViewer.set('src', src);
	}
	function refreshSession() {
		Edo.util.Ajax.request({
			url : 'mainAction!refreshSession',
			type : 'post',
			onSuccess : function(text) {
				setTimeout('refreshSession()', 600000); // 刷新
			},
			onFail : function(code) {
				alert("加载错误," + code);
			}
		});
	}
