	var qikoo = window.qikoo || {};
	qikoo.dialog = function() {
	    var e, t, n = {}, r = function() {
	        var n = ['<div class="mod-dialog-bg"></div>', '<div class="mod-dialog">', '<div class="dialog-nav">', '<span class="dialog-title"></span>', '<a href="#" onclick="return false" class="dialog-close"></a>', "</div>", '<div class="dialog-main"></div>', "</div>"].join(""),
	        r = $(n).hide().appendTo("body");
	        e = r.filter(".mod-dialog-bg"), t = r.filter(".mod-dialog"), t.find(".dialog-close").click(function() {
	            u()
	        })
	    }, i = function() {
	        t.css("width", n.width || ""), t.find(".dialog-title").html(n.title), t.find(".dialog-main").html(n.html), t.show(), e.show(), s()
	    }, s = function() {
	        var e = ($(window).width() - t.width()) / 2,
	        n = ($(window).height() - t.height()) / 2;
	        n = n > 0 ? n + $(window).scrollTop() : 0, t.css({
	            left: e,
	            top: n
	        })
	    }, o = function(e) {
	        return typeof e != "object" && (e = {
	            html: e || ""
	        }), n = $.extend({
	            title: "提示",
	            html: "",
	            closeFn: null
	        }, e), t || r(), i(), t
	    }, u = function() {
	        e && e.hide(), t && t.hide(), n.closeFn && n.closeFn.call(this)
	    };
	    return {
	        show: o,
	        hide: u
	    }
	}(), qikoo.dialog.alert = function(e, t) {
	    var n = ['<div class="dialog-content">', "<p>" + e + "</p>", "</div>", '<div class="dialog-console clearfix_new">', '<a class="console-btn-confirm" href="#" onclick="return false;">确定</a>', "</div>"].join(""),
	    r = qikoo.dialog.show({
	        html: n
	    });
	    return r.find(".console-btn-confirm").click(function() {
	        var e = t && t.call(r);
	        e !== !1 && qikoo.dialog.hide()
	    }), r
	}, qikoo.popConfirm = function() {	   
	    
	}();