/** easyui-combobox-filterLocal.js  aizhonghua 2016-12-06 **/
function filterLocalCombobox(q, row, keys){
	if(keys!=null && keys.length > 0){
		for(var i=0;i<keys.length;i++){
			if(row[keys[i]]!=null&&row[keys[i]]!=''){
				var istrue = row[keys[i]].indexOf(q.toUpperCase()) > -1||row[keys[i]].indexOf(q) > -1;
				if(istrue==true){
					return true;
				}
			}
		}
	}else{
		var opts = $(this).combobox('options');
		return row[opts.textField].indexOf(q.toUpperCase()) > -1||row[opts.textField].indexOf(q) > -1;
	}
}