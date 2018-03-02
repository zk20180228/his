/*
* 设计器私有的配置说明 
* 一
* UE.leipiFormDesignUrl  插件路径
* 
* 二
*UE.getEditor('myFormDesign',{
*          toolleipi:true,//是否显示，设计器的清单 tool
*/
UE.leipiFormDesignUrl = 'formdesign/edit';
/**
 * 文本框
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
UE.plugins['text'] = function () {
	var me = this,thePlugins = 'text';
	me.commands[thePlugins] = {
		execCommand:function () {
			var dialog = new UE.ui.Dialog({
				iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/text.jsp',
				name:thePlugins,
				editor:this,
				title: '文本框',
				cssRules:"width:300px;height:155px;",
				buttons:[
				{
					className:'edui-okbutton',
					label:'确定',
					onclick:function () {
						dialog.close(true);
					}
				},
				{
					className:'edui-cancelbutton',
					label:'取消',
					onclick:function () {
						dialog.close(false);
					}
				}]
			});
			dialog.render();
			dialog.open();
		}
	};
	var popup = new baidu.editor.ui.Popup( {
		editor:this,
		content: '',
		className: 'edui-bubble',
		_edittext: function () {
			  baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
			  me.execCommand(thePlugins);
			  this.hide();
		},
		_delete:function(){
			if( window.confirm('确认删除该控件吗？') ) {
				baidu.editor.dom.domUtils.remove(this.anchorEl,false);
			}
			this.hide();
		}
	} );
	popup.render();
	 me.addListener( 'mouseover', function( t, evt ) {
	        evt = evt || window.event;
	        var el = evt.target || evt.srcElement;
	        if(el.innerHTML.substring(0,1)=="{"){
	        	var hidEl=el.getElementsByTagName("span")[0];
	        	var leipiPlugins = hidEl.getAttribute('leipiplugins');
	        	var attrKind = hidEl.getAttribute('attrkind');
		        if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
		        	var html = popup.formatHtml(
	                '<nobr>设置文本框控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
	            if ( html ) {
	                var rEl = el;
	                popup.getDom( 'content' ).innerHTML = html;
	                popup.anchorEl = el;
	                popup.showAnchor( rEl);
	            } else {
	                popup.hide();
	            }
		        }
	    	}
	    });
	};
	
	/**
	 * 数字框
	 * @command number
	 * @method execCommand
	 * @param { String } cmd 命令字符串
	 * @example
	 * ```javascript
	 * editor.execCommand( 'number');
	 * ```
	 */
	UE.plugins['number'] = function () {
		var me = this,thePlugins = 'number';
		me.commands[thePlugins] = {
			execCommand:function () {
				var dialog = new UE.ui.Dialog({
					iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/number.jsp',
					name:thePlugins,
					editor:this,
					title: '数字',
					cssRules:"width:600px;height:310px;",
					buttons:[
					{
						className:'edui-okbutton',
						label:'确定',
						onclick:function () {
							dialog.close(true);
						}
					},
					{
						className:'edui-cancelbutton',
						label:'取消',
						onclick:function () {
							dialog.close(false);
						}
					}]
				});
				dialog.render();
				dialog.open();
			}
		};
		var popup = new baidu.editor.ui.Popup( {
			editor:this,
			content: '',
			className: 'edui-bubble',
			_edittext: function () {
				  baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
				  me.execCommand(thePlugins);
				  this.hide();
			},
			_delete:function(){
				if( window.confirm('确认删除该控件吗？') ) {
					baidu.editor.dom.domUtils.remove(this.anchorEl,false);
				}
				this.hide();
			}
		} );
		popup.render();
		 me.addListener( 'mouseover', function( t, evt ) {
		        evt = evt || window.event;
		        var el = evt.target || evt.srcElement;
		        if(el.innerHTML.substring(0,1)=="{"){
		        	var hidEl=el.getElementsByTagName("span")[0];
		        	var leipiPlugins = hidEl.getAttribute('leipiplugins');
		        	var attrKind = hidEl.getAttribute('attrkind');
			        if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
			            var html = popup.formatHtml(
			                '<nobr>设置数字控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
			            if ( html ) {
			                var rEl = el;
			                popup.getDom( 'content' ).innerHTML = html;
			                popup.anchorEl = el;
			                popup.showAnchor( rEl);
			            } else {
			                popup.hide();
			            }
			        }
		    	}
		    });
		};
	
		/**
		 * 时间框
		 * @command date
		 * @method execCommand
		 * @param { String } cmd 命令字符串
		 * @example
		 * ```javascript
		 * editor.execCommand( 'date');
		 * ```
		 */
		UE.plugins['date'] = function () {
			var me = this,thePlugins = 'date';
			me.commands[thePlugins] = {
					execCommand:function () {
						var dialog = new UE.ui.Dialog({
							iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/date.jsp',
							name:thePlugins,
							editor:this,
							title: '时间',
							cssRules:"width:600px;height:310px;",
							buttons:[
							         {
							        	 className:'edui-okbutton',
							        	 label:'确定',
							        	 onclick:function () {
							        		 dialog.close(true);
							        	 }
							         },
							         {
							        	 className:'edui-cancelbutton',
							        	 label:'取消',
							        	 onclick:function () {
							        		 dialog.close(false);
							        	 }
							         }]
						});
						dialog.render();
						dialog.open();
					}
			};
			var popup = new baidu.editor.ui.Popup( {
				editor:this,
				content: '',
				className: 'edui-bubble',
				_edittext: function () {
					baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
					me.execCommand(thePlugins);
					this.hide();
				},
				_delete:function(){
					if( window.confirm('确认删除该控件吗？') ) {
						baidu.editor.dom.domUtils.remove(this.anchorEl,false);
					}
					this.hide();
				}
			} );
			popup.render();
			me.addListener( 'mouseover', function( t, evt ) {
				evt = evt || window.event;
				var el = evt.target || evt.srcElement;
				if(el.innerHTML.substring(0,1)=="{"){
					var hidEl=el.getElementsByTagName("span")[0];
					var leipiPlugins = hidEl.getAttribute('leipiplugins');
					var attrKind = hidEl.getAttribute('attrkind');
					if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
						var html = popup.formatHtml(
						'<nobr>设置时间控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
						if ( html ) {
							var rEl = el;
							popup.getDom( 'content' ).innerHTML = html;
							popup.anchorEl = el;
							popup.showAnchor( rEl);
						} else {
							popup.hide();
						}
					}
				}
			});
		};
		
/**
 * 单选框组
 * @command radios
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'radio');
 * ```
 */
UE.plugins['radios'] = function () {
    var me = this,thePlugins = 'radios';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/radios.jsp',
                name:thePlugins,
                editor:this,
                title: '设置单选控件',
                cssRules:"width:600px;height:400px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                },
                {
                    className:'edui-cancelbutton',
                    label:'取消',
                    onclick:function () {
                        dialog.close(false);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
              baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
              me.execCommand(thePlugins);
              this.hide();
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        if(el.innerHTML.substring(0,1)=="{"){
        	var hidEl=el.getElementsByTagName("span")[0];
        	var leipiPlugins = hidEl.getAttribute('leipiplugins');
        	var attrKind = hidEl.getAttribute('attrkind');
	        if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
	            var html = popup.formatHtml(
	                '<nobr>设置单选控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
	            if ( html ) {
	                var rEl = el;
	                popup.getDom( 'content' ).innerHTML = html;
	                popup.anchorEl = el;
	                popup.showAnchor( rEl);
	            } else {
	                popup.hide();
	            }
	        }
    	}
    });
};
/**
 * 复选框组
 * @command checkboxs
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'checkboxs');
 * ```
 */
UE.plugins['checkboxs'] = function () {
    var me = this,thePlugins = 'checkboxs';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/checkboxs.jsp',
                name:thePlugins,
                editor:this,
                title: '复选框组',
                cssRules:"width:600px;height:400px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                },
                {
                    className:'edui-cancelbutton',
                    label:'取消',
                    onclick:function () {
                        dialog.close(false);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
              baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
              me.execCommand(thePlugins);
              this.hide();
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        if(el.innerHTML.substring(0,1)=="{"){
        	var hidEl=el.getElementsByTagName("span")[0];
        	var leipiPlugins = hidEl.getAttribute('leipiplugins');
        	var attrKind = hidEl.getAttribute('attrkind');
	        if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
	            var html = popup.formatHtml(
	                '<nobr>设置多选控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
	            if ( html ) {
	                var rEl = el;
	                popup.getDom( 'content' ).innerHTML = html;
	                popup.anchorEl = el;
	                popup.showAnchor( rEl);
	            } else {
	                popup.hide();
	            }
	        }
    	}
    });
};
/**
 * 有无组
 * @command yesornoselect
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'checkboxs');
 * ```
 */
UE.plugins['yesornoselect'] = function () {
    var me = this,thePlugins = 'yesornoselect';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/yesornoselect.jsp',
                name:thePlugins,
                editor:this,
                title: '有无选择组',
                cssRules:"width:600px;height:400px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                },
                {
                    className:'edui-cancelbutton',
                    label:'取消',
                    onclick:function () {
                        dialog.close(false);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
              baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
              me.execCommand(thePlugins);
              this.hide();
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        if(el.innerHTML.substring(0,1)=="{"){
        	var hidEl=el.getElementsByTagName("span")[0];
        	var leipiPlugins = hidEl.getAttribute('leipiplugins');
        	var attrKind = hidEl.getAttribute('attrkind');
	        if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
	            var html = popup.formatHtml(
	                '<nobr>设置有无选控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
	            if ( html ) {
	                var rEl = el;
	                popup.getDom( 'content' ).innerHTML = html;
	                popup.anchorEl = el;
	                popup.showAnchor( rEl);
	            } else {
	                popup.hide();
	            }
	        }
    	}
    });
};
/**
 * 多行文本框
 * @command textarea
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textarea');
 * ```
 */
UE.plugins['textarea'] = function () {
    var me = this,thePlugins = 'textarea';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/textarea.jsp',
                name:thePlugins,
                editor:this,
                title: '多行文本框',
                cssRules:"width:600px;height:330px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                },
                {
                    className:'edui-cancelbutton',
                    label:'取消',
                    onclick:function () {
                        dialog.close(false);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
              baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
              me.execCommand(thePlugins);
              this.hide();
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        if(el.innerHTML.substring(0,1)=="{"){
        	var hidEl=el.getElementsByTagName("span")[0];
        	var leipiPlugins = hidEl.getAttribute('leipiplugins');
        	var attrKind = hidEl.getAttribute('attrkind');
	        if ( /span/ig.test( hidEl.tagName ) && leipiPlugins==thePlugins && attrKind == 0) {
	            var html = popup.formatHtml(
	                '<nobr>设置多行文本框控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
	            if ( html ) {
	                var rEl = el;
	                popup.getDom( 'content' ).innerHTML = html;
	                popup.anchorEl = el;
	                popup.showAnchor( rEl);
	            } else {
	                popup.hide();
	            }
	        }
    	}
    });
};
/**
 * 列表控件
 * @command listctrl
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'qrcode');
 * ```
 */
UE.plugins['listctrl'] = function () {
    var me = this,thePlugins = 'listctrl';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/listctrl.jsp',
                name:thePlugins,
                editor:this,
                title: '列表控件',
                cssRules:"width:800px;height:400px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                },
                {
                    className:'edui-cancelbutton',
                    label:'取消',
                    onclick:function () {
                        dialog.close(false);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup( {
        editor:this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
              baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
              me.execCommand(thePlugins);
              this.hide();
        },
        _delete:function(){
            if( window.confirm('确认删除该控件吗？') ) {
                baidu.editor.dom.domUtils.remove(this.anchorEl,false);
            }
            this.hide();
        }
    } );
    popup.render();
    me.addListener( 'mouseover', function( t, evt ) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var leipiPlugins = el.getAttribute('leipiplugins');
        if ( /input/ig.test( el.tagName ) && leipiPlugins==thePlugins) {
            var html = popup.formatHtml(
                '<nobr>列表控件: <span onclick=$$._edittext() class="edui-clickable">编辑</span></nobr>' );
            if ( html ) {
                popup.getDom( 'content' ).innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor( popup.anchorEl );
            } else {
                popup.hide();
            }
        }
    });
};

UE.plugins['error'] = function () {
    var me = this,thePlugins = 'error';
    me.commands[thePlugins] = {
        execCommand:function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl:this.options.UEDITOR_HOME_URL + UE.leipiFormDesignUrl+'/error.jsp',
                name:thePlugins,
                editor:this,
                title: '异常提示',
                cssRules:"width:400px;height:130px;",
                buttons:[
                {
                    className:'edui-okbutton',
                    label:'确定',
                    onclick:function () {
                        dialog.close(true);
                    }
                }]
            });
            dialog.render();
            dialog.open();
        }
    };
};
UE.registerUI('button_preview',function(editor,uiName){
    if(!this.options.toolleipi)
    {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName,{
        execCommand:function(){
            try {
            	templateDesign.fnReview();
            } catch ( e ) {
                alert('预览异常');
            }
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name:uiName,
        //提示
        title:"预览",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules :'background-position: -420px -19px;',
        //点击时执行的命令
        onclick:function () {
            //这里可以不用执行命令,做你自己的操作也可
           editor.execCommand(uiName);
        }
    });

    //因为你是添加button,所以需要返回这个button
    return btn;
});

UE.registerUI('button_save',function(editor,uiName){
    if(!this.options.toolleipi)
    {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName,{
        execCommand:function(){
            try {
            	templateDesign.fnCheckForm('save');
            } catch ( e ) {
                alert('保存异常');
            }
            
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name:uiName,
        //提示
        title:"保存模板",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules :'background-position: -481px -20px;',
        //点击时执行的命令
        onclick:function () {
            //这里可以不用执行命令,做你自己的操作也可
           editor.execCommand(uiName);
        }
    });

    //因为你是添加button,所以需要返回这个button
    return btn;
});
