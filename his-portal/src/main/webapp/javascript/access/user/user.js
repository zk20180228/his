function searchUsers(o, callback){
	//o=encodeJson(o);
    var o = {
        dataString: Edo.util.Json.encode(o),
        method: 0
    };
    Edo.util.Ajax.request({        
        url: 'userAction!commitAjax',
        type: 'post',
        params: o,
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o);                
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("加载错误,"+code);
        }
    });
}
function searchRoles(id,callback){
    var o = {
    	dataString:"{UserID:"+id+"}",
        method: 0
    };
    Edo.util.Ajax.request({        
        url: 'roleAction!commitAjax',
        type: 'post',
        params: o,
        onSuccess: function(text){   
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o);                
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("加载错误,"+code);
        }
    });
}
function addUser(o, callback){
	//o=encodeJson(o);
    var o = {
        dataString: Edo.util.Json.encode(o),
        method: 1
    };
    Edo.util.Ajax.request({
        url: 'userAction!commitAjax',
        type: 'post',
        params: o,
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function deleteUser(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'userAction!commitAjax',
        type: 'post',
        params: {
            method: 2,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o);
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function updateUser(o, callback){
	//o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'userAction!commitAjax',
        type: 'post',
        params: {
            method: 3,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

//授权用户
function authorizeUser(o, callback){
	//var o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'userAction!commitAjax',
        type: 'post',
        params: {
            method: 5,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){   
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

//启用和停用用户
function startAndStopUser(o, callback){
	//var o=encodeJson(o);
    Edo.util.Ajax.request({
    	url: 'userAction!commitAjax',
        type: 'post',
        params: {
            method: 6,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}

function setUserRole(o, callback){
    Edo.util.Ajax.request({
    	url: 'userAction!commitAjax',
        type: 'post',
        params: {
            method: 7,
            dataString: Edo.util.Json.encode(o)
        },
        onSuccess: function(text){        
            var o = Edo.util.Json.decode(text);
            if(o.error == 0){
                if(callback) callback(o)
                Info("设置成功！");
            }else{
                alert(o.msg);
            }
        },
        onFail: function(code){
            alert("新增错误,"+code);
        }
    });
}


//表单验证器函数
function noEmpty(v){
  if(v == "") return "不能为空";
}

function showUserForm(callback){
    if(!Edo.get('userForm')) {
        //创建用户面板
        Edo.create({
            id: 'userForm',            
            type: 'window',title: '用户录入',
            render: document.body,
            titlebar: [
                {
                    cls: 'e-titlebar-close',
                    onclick: function(e){
                        this.parent.owner.hide();
                    }
                }
            ],
            children: [
                {
                    type: 'formitem',label: '账号<span style="color:red;">*</span>:',
                    children:[{id: 'account', type: 'text', name: 'Account', valid: noEmpty}]
                },
                {
                    type: 'formitem',label: '密码<span style="color:red;">*</span>:',
                    children:[{id: 'password', type: 'password', name: 'Password', valid: noEmpty}]
                },
                {
                    type: 'formitem',label: '昵称:',
                    children:[{type: 'text', name: 'NickName'}]
                },
                {
                    type: 'formitem',label: '排序:',
                    children:[{type: 'spinner', name: 'Order',incrementValue: 10}]                
                },
                {
                    type: 'formitem',layout:'horizontal', padding: [8,0,8, 0],
                    children:[
                        {name: 'submitBtn', type: 'button', text: '提交表单', 
                            onclick: function(){
                                if(userForm.valid()){
                                    var o = userForm.getForm();
                                    if(userForm.callback) userForm.callback(o);
                                    userForm.hide();
                                }
                            }
                        }
                    ]
                }
            ]
        });
    }
    userForm.callback = callback;
    userForm.show('center', 'middle', true);
    return userForm;
}
