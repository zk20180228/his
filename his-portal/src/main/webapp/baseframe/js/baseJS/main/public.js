function resetPageAll(t){t.each(function(t,a){var i=$(a).children("iframe")[0];i.src=i.src})}function openNav(t,a,i){if(window.parent.$("#tabs").tabs("exists",a)){window.parent.$("#tabs").tabs("select",a);var e=window.parent.$("#tabs").tabs("getSelected");void 0!=t&&window.parent.$("#tabs").tabs("update",{tab:e,options:{content:'<iframe scrolling="auto" frameborder="0"  src="'+t+'" style="width:100%;height:100%;"></iframe>'}})}else window.parent.$("#tabs").tabs("add",{title:a,content:'<iframe scrolling="auto" frameborder="0"  src="'+t+'" style="width:100%;height:100%;"></iframe>',closable:!0}),window.parent.tabClose()}function clearNav(t,a){window.parent.$("#tabs").tabs("close",a)}function replaceNav(t,a){window.parent.$("#tabs").tabs("select",a);var i=window.parent.$("#tabs").tabs("getSelected");void 0!=t&&window.parent.$("#tabs").tabs("update",{tab:i,options:{content:'<iframe scrolling="auto" frameborder="0"  src="'+t+'" style="width:100%;height:100%;"></iframe>'}})}function upPage(t,a){window.parent.$("#tabs").tabs("select",a);var i=window.parent.$("#tabs").tabs("getSelected");window.parent.$("#tabs").tabs("update",{tab:i,options:{content:'<iframe scrolling="auto" frameborder="0"  src="'+t+'" style="width:100%;height:100%;"></iframe>'}})}function upPagecallback(t,a,i){window.parent.$("#tabs").tabs("select",a),window.parent.$("#tabs .tabs-panels .panel").each(function(t,a){if("block"==$(a).css("display"))return i(window.parent.$(a).children().children("iframe")[0].contentWindow),!1})}$.alert=function(t,a,i,e){var a=a||"提示",t=t||"提示",e=e,i=i;0==$("#messageWindows").length?($("body").append('<div class="modal fade" id="messageWindows" tabindex="-1" role="dialog" aria-labelledby="messageWindowsLabel">  <div class="modal-dialog" role="document">    <div class="modal-content">      <div class="modal-header">        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>        <h4 class="modal-title" id="messageWindowsLabel">'+t+'</h4>      </div>      <div class="modal-body clearfix">'+a+'</div>      <div class="modal-footer">\t\t\t<button type="button" class="btn btn-theme" data-dismiss="modal">关闭</button>\t  </div>  </div></div>'),$("#messageWindows").on("hide.bs.modal",function(){i&&i()}).on("show.bs.modal",function(){e&&e()})):($("#messageWindows .modal-title").html(t),$("#messageWindows .modal-body").html(a)),$("#messageWindows").modal()},$.confirm=function(t,a,i,e){var a=a||"提示",t=t||"提示",i=i,e=e;0==$("#confirmWindows").length?$("body").append('<div class="modal fade" id="confirmWindows" tabindex="-1" role="dialog" aria-labelledby="messageWindowsLabel">  <div class="modal-dialog" role="document">    <div class="modal-content">      <div class="modal-header">        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>        <h4 class="modal-title" id="confirmWindowsLabel">'+t+'</h4>      </div>      <div class="modal-body clearfix">'+a+'</div>      <div class="modal-footer">\t\t\t<button type="button" id = "yesConfirm" class="btn btn-theme" >确定</button>\t\t\t<button type="button" id = "noConfirm" class="btn btn-default" data-dismiss="modal">关闭</button>\t  </div>  </div></div>'):($("#messageWindows .modal-title").html(t),$("#messageWindows .modal-body").html(a)),$("#yesConfirm").attr("disabled",!1).off("click").on("click",function(){$(this).attr("disabled","disabled"),i&&i(),$("#confirmWindows").modal("hide")}),$("#noConfirm").attr("disabled",!1).off("click").on("click",function(){$(this).attr("disabled","disabled"),e&&e(),$("#confirmWindows").modal("hide")}),$("#confirmWindows").modal()},$.dialog=function(t){var a={title:"窗口",width:"50%",height:"50%",afterFn:null,beforFn:null,window:!1,border:!0},i=$.extend(a,t);"Number"==typeof i.width&&(i.width=i.width+"px"),"Number"==typeof i.height&&(i.height=i.height+"px");var e=i.width?"width:"+i.width+";":"",d=i.height?"height:"+i.height+";":"",o=e+d,s="",n="";i.window&&(o=i.border?"height: calc(100% - 85px);width:100%;padding: 0;margin: 0;":"height: calc(100% - 55px);width:100%;padding: 0;margin: 0;",n="width: 100%;height: 100%;padding: 0;margin: 0;"),i.border||(s="padding: 0;"),0==$("#dialogWindows").length?($("body").append('<div class="modal fade" id="dialogWindows" tabindex="-1"  style="'+n+'" role="dialog" aria-labelledby="dialogWindowsLabel">  <div id = "dialogWindowsStyle" class="modal-dialog" role="document" style="'+o+'">    <div class="modal-content" >      <div class="modal-header">        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>        <h4 class="modal-title" id="dialogWindowsLabel">'+i.title+'</h4>      </div>      <div class="modal-body clearfix" style="'+s+'">\t\t<iframe id = "dialogWindowsiframe" src="'+i.href+'"width="100%" height="100%" style="border-width: 0;" ></iframe>\t\t</div>  </div></div>'),$("#dialogWindows").on("hide.bs.modal",function(){i.afterFn&&i.afterFn()}).on("show.bs.modal",function(){i.beforFn&&i.beforFn()})):($("#dialogWindowsStyle").attr("style",o),$("#dialogWindows .modal-title").html(i.title),$("#dialogWindowsiframe").attr("src",i.href)),$("#dialogWindows").modal()};