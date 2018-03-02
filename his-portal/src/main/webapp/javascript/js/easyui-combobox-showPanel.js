$(document).ready(function(){  
	$(".combo").click(function(){
		var p = $(this).prev();
		if(!$(p).prop("disabled")){
			$(p).combobox("showPanel");
		}
	});
}); 
