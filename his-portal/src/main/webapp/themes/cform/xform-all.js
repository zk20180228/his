/*
 * Compressed by JSA(www.xidea.org)
 */

xf = {};

xf.field = {};

xf.$ = function(id) {
	return document.getElementById(id);
}

xf.id = function() {
	if(typeof xf.sed == 'undefined') {
		xf.sed = 1;
	}
	return 'xf-' + xf.sed++;
}

xf.addClass = function(el, className) {
	if(el){
		if((' ' + el.className).indexOf(' ' + className) == -1) {
			el.className += ' ' + className;
		}
	}
}

xf.removeClass = function(el, className) {
	if(el){
		var cls = el.className;
		while(cls.indexOf(' ' + className) != -1) {
			var index = cls.indexOf(' ' + className);
			cls = cls.substring(0, index) + cls.substring(index + className.length + 1);
		}
		while(cls.indexOf(className + ' ') != -1) {
			var index = cls.indexOf(className + ' ');
			cls = cls.substring(0, index) + cls.substring(index + className.length + 1);
		}
		el.className = cls;
	}
}

xf.getTarget = function(e) {
	var ev = window.event ? window.event : e;
	var x = ev.clientX;
	var y = ev.clientY;
	var target = ev.srcElement ? ev.srcElement : ev.target;
	if(target.tagName == 'IMG' && target.parentNode.className == 'xf-pallete') {
		target = target.parentNode;
	}
	return target;
}

xf.getHandler = function(el) {
	while(el) {
		if(el.className && el.className == 'xf-handler') {
			return el;
		}
		el = el.parentNode;
	}
	return null;
}

xf.getPosition = function(e) {
	var ev = window.event ? window.event : e;
	var x = ev.pageX;
	var y = ev.pageY;
	return {
		x: x,
		y: y
	};
}

xf.insertAfter = function(newElement, targetElement) {
	var parent = targetElement.parentNode;
	if(parent.lastChild == targetElement) {
		parent.appendChild(newElement);
	} else {
		parent.insertBefore(newElement, targetElement.nextSibling);
	}
}

xf.createField = function(label, value, callback, self, formNode,isReadOnly,id) {
	var labelNode = document.createElement('label');
	labelNode.innerHTML = label + ':';
	labelNode.className = 'control-label';

	var input = document.createElement('input');
	input.type = 'text';
	input.className = "form-control"
	id && (input.id = id)
	input.value = value;
	if(isReadOnly){
		input.readOnly = "readOnly"
	}
	input.onblur = function() {
		callback.call(self, this.value);
	}

	formNode.appendChild(labelNode);
	formNode.appendChild(input);
}
xf.createTimeInput = function(label, value, callback, self, formNode,isReadOnly,id) {
	var labelNode = document.createElement('label');
	labelNode.innerHTML = label + ':';
	labelNode.className = 'control-label';

	var input = document.createElement('input');
	input.type = 'text';
	input.className = "form-control rili"
	input.value = value;
	
	if(isReadOnly){
		input.readOnly = "readOnly"
	}
	if(label == "startTime"){
		input.id = "d4311"
		input.onclick = function(){
			WdatePicker({
				startDate:'%y-%M-01 00:00:00',
				dateFmt:'yyyy-MM-dd HH:mm:ss',
				maxDate:'#F{$dp.$D(\'d4312\')||\'2099-10-01\'}',
				onpicking:function(dp){
					callback.call(self, dp.cal.getNewDateStr());
				}
			})
		}
	}else if (label== "endTime"){
		input.id = "d4312"
		input.onclick = function(){
			WdatePicker({
				startDate:'%y-%M-01 00:00:00',
				dateFmt:'yyyy-MM-dd HH:mm:ss',
				minDate:'#F{$dp.$D(\'d4311\')}',
				maxDate:'2099-10-01',
				onpicking:function(dp){
					callback.call(self, dp.cal.getNewDateStr());
				}
			})
		}
	}else{
		input.onclick = function(){
			WdatePicker({
				startDate:'%y-%M-01 00:00:00',
				dateFmt:'yyyy-MM-dd HH:mm:ss',
				maxDate:'#F{\'2099-10-01\'}',
				onpicking:function(dp){
					callback.call(self, dp.cal.getNewDateStr());
				}
			})
		}
	}
	input.onblur = function() {
		callback.call(self, this.value);
	}
	formNode.appendChild(labelNode);
	formNode.appendChild(input);
}
xf.createBooleanField = function(label, value, callback, self, formNode) {
	var labelNode = document.createElement('label');
	labelNode.className = 'control-group';

	var input = document.createElement('input');
	input.type = 'checkbox';
	input.value = 'true';
	if(value) {
		input.checked = true;
	}
	input.onclick = function() {
		callback.call(self, this.checked);
	}

	labelNode.appendChild(input);
	labelNode.appendChild(document.createTextNode(' ' + label));
	formNode.appendChild(labelNode);
}

xf.createSelectField = function(label, value,textValue, callback, self, formNode,multiple,id) {
	var labelNode = document.createElement('label');
	labelNode.innerHTML = label + ':';
	labelNode.className = 'control-label';

	var input = document.createElement('select');
	id && (input.id = id) 
	input.className = "form-control"
	
	var str = ""
	var index = 0
	if(value){
		for (var key in value) {
			str+="<option value = "+ key +">"+ value[key] +"</option>"
			index ++
		}
		if(multiple){
			input.setAttribute("multiple","true") 
			input.style = "height: "+ ((index*20)+14) +"px;overflow: hidden;"   
		}
	}
	input.innerHTML = str;
	var v = ","+textValue+","
	$(input).children("option").each(function(){
	  if(v.indexOf(','+this.value+',')!=-1)this.selected=true;
	})
	$(input).on("change",function(){
		var val = $(this).val()
		if(Array.isArray(val)){
			if(val.length == 1){
				callback.call(self, val[0]);
			}else{
				callback.call(self, val.join(","));
			}
		}else{
			callback.call(self, val);
		}
	})

	formNode.appendChild(labelNode);
	formNode.appendChild(input);
}

xf.createCheckedBox = function(label, value,textValue, callback, self, formNode,multiple,id) {
	var labelNode = document.createElement('label');
	labelNode.innerHTML = label + ':';
	labelNode.className = 'control-label';
	var div = document.createElement('div');
	id && (input.id = id) 
	div.className = "checkbox checkbox-theme"
	var str = ""
	if(value){
		for (var key in value) {
			str+=  '<input id = "_check'+ key +'" item="text" type="checkbox" value="'+ key +'" style="margin:1px;"><label for="_check'+ key +'" class="checkbox inline">'+ value[key]+'</label>' 
		}
	}
	div.innerHTML = str;
	var v = ","+textValue+","
	$(div).children("input").each(function(){
	  if(v.indexOf(','+this.value+',')!=-1) $(this).prop("checked","checked")
	})
	$(div).find("input[type=checkbox]").on("change",function(){
		callback.call(self, $(this).val(),$(this).is(':checked'));
	})
	formNode.appendChild(labelNode);
	formNode.appendChild(div);
}

xf.createChilentSelectField = function(label, value,textValue, callback, self, formNode) {
	var labelNode = document.createElement('label');
	labelNode.innerHTML = label + ':';
	labelNode.className = 'control-label';

	var input = document.createElement('select');
	input.className = "form-control"
	input.id = value
	//111%20|1,222%20|2,333%60
	
	var str = ""
	var optionArr = textValue.split(",")
	
	for(var i = 0 ;i<optionArr.length;i++){
		if(optionArr[i] != ""){
			str+="<option value = "+ i +">"+ optionArr[i].replace(/%\d+|\|\d+|~\d+/g,"")+"</option>";
		}
	}
	input.innerHTML = str;
	$(input).on("change",function(){
		var val = $(this).val()
		if(Array.isArray(val)){
			if(val.length == 1){
				callback.call(self, val[0]);
			}else{
				callback.call(self, val.join(","));
			}
		}else{
			callback.call(self, val);
		}
	})

	formNode.appendChild(labelNode);
	formNode.appendChild(input);
}

;

xf.Xform = function(id) {
	this.id = id;

	// 默认模式是拖拽编辑添加组件
	// 可以切换成合并单元格的模式
	this.mode = 'EDIT';

	this.sections = [];

	this.sed = 0;
	this.proxy = new xf.Proxy();
	this.fieldFactory = new xf.field.FieldFactory();
}

xf.Xform.prototype.addSection = function(section) {
	section.id = xf.id();
	section.xform = this;
	this.sections.push(section);
}

xf.Xform.prototype.initEvents = function() {
	var self = this;
	document.onmousedown = function(e) {
		self.mouseDown(e);
	}
	document.onmousemove = function(e) {
		self.mouseMove(e);
	}
	document.onmouseup = function(e) {
		self.mouseUp(e);
	}
}

xf.Xform.prototype.mouseDown = function(e) {
	var target = xf.getTarget(e);
	var handler = xf.getHandler(target);
	if(handler || target.className == 'xf-pallete') {
		e.preventDefault();
	}

	if(this.mode == 'EDIT') {
		if(target.className == 'xf-pallete') {
			this.request = {
				type: 'add',
				fieldType: target.title
			}
		} else if(handler) {
			var section = this.findSection(e);
			if(section) {
				var field = section.findField(target);
				if(field) {
					this.request = {
						type: 'move',
						section: section,
						field: field
					};
				}
			}
		}
	} else if(this.mode == 'MERGE') {
		var section = this.findSection(e);
		if(section) {
			section.mergeStart(e);
			this.request = {
				status: 'merge'
			};
		}
	}

	var section = this.findSection(e);
	if(section) {
		section.selectSomething(e);
	}
}

xf.Xform.prototype.mouseMove = function(e) {
	if(!this.request) {
		return;
	}

	if(this.mode == 'EDIT') {
		var position = xf.getPosition(e);
		this.proxy.move(position.x + 5, position.y + 5);
	} else if(this.mode == 'MERGE') {
		var section = this.findSection(e);
		if(section) {
			section.mergeMove(e);
		}
	}
}

xf.Xform.prototype.mouseUp = function(e) {
	if(!this.request) {
		return;
	}

	if(this.mode == 'EDIT') {
		this.proxy.hide();

		var target = xf.getTarget(e);
		var section = this.findSection(e);
		if(section) {
			if(this.request.type == 'add') {
				section.addField(this.request, target);
			} else if(this.request.type == 'move') {
				section.moveTo(this.request.field, target);
			}
		}
		this.request = null;
	} else if(this.mode == 'MERGE') {
		var section = this.findSection(e);
		if(section) {
			section.mergeEnd(e);
		}
		this.request = null;
	}
}

xf.Xform.prototype.findSection = function(e) {
	var target = xf.getTarget(e);
	var parent = target;
	while(true) {
		if(parent.className != null && parent.className.indexOf('xf-section') != -1) {
			for(var i = 0; i < this.sections.length; i++) {
				var section = this.sections[i];
				if(section.id == parent.id) {
					return section;
				}
			}
		}

		parent = parent.parentNode;
		if(!parent) {
			return null;
		}
	}
}

xf.Xform.prototype.addRow = function() {
	var section = this.sections[1];
	section.addRow();
}

xf.Xform.prototype.removeRow = function() {
	var section = this.sections[1];
	section.removeRow();
}
xf.Xform.prototype.addCol = function() {
	var section = this.sections[1];
	section.addCol();
}
xf.Xform.prototype.removeCol = function() {
	var section = this.sections[1];
	section.removeCol();
}
xf.Xform.prototype.render = function() {
	for(var i = 0; i < this.sections.length; i++) {
		var section = this.sections[i];
		section.render();
	}
	if(this.mula){
		setFormula(this.mula)
	}
}

xf.Xform.prototype.doExport = function() {
	var text = '{"name":"' + this.name + '","code":"' + this.code + '","mula":"'+ this.mula +'","sections":[';
	for(var i = 0; i < this.sections.length; i++) {
		var sectionText = this.sections[i].doExport();
		text += sectionText;
		if(i != this.sections.length - 1) {
			text += ',';
		}
	}
	text += ']}';
	return text;
}

xf.Xform.prototype.doImport = function(text,callback) {
	var o = eval('(' + text + ')');

	this.name = o.name;
	this.code = o.code;
	this.mula = o.mula || ""

	xf.$(this.id).innerHTML = '';
	this.sections = [];

	for(var i = 0; i < o.sections.length; i++) {
		var sectionData = o.sections[i];
		switch(sectionData.type) {
			case 'text':
				var section = new xf.TextSection(sectionData.tag, sectionData.text,sectionData.editPosition);
				xform.addSection(section);
				break;
			case 'grid':
				var section = new xf.GridSection(sectionData.row, sectionData.col);
				xform.addSection(section);
				section.doImport(sectionData);
				break;
		}
	}

	this.render();
	if (arguments.length == 2){
		callback()
	}
}

xf.Xform.prototype.doMerge = function() {
	for(var i = 0; i < this.sections.length; i++) {
		this.sections[i].merge();
	}
}

xf.Xform.prototype.doSplit = function() {
	for(var i = 0; i < this.sections.length; i++) {
		this.sections[i].split();
	}
}

xf.Xform.prototype.setValue = function(data) {
	for(var i = 0; i < this.sections.length; i++) {
		var section = this.sections[i];
		section.setValue(data);
	}
}

;

xf.TextSection = function(tag, text,editPosition) {
	this.tag = tag;
	this.text = text;
	this.editPosition = editPosition || ""
}

xf.TextSection.prototype.render = function() {
	var el = document.createElement('div');
	el.id = this.id;
	el.className = 'xf-section';
	el.innerHTML = '<input type="hidden" name = "formTitle"/><' + this.tag + ' style="text-align:center;">' +
		this.text +
		'</' + this.tag + '>';

	var xformEl = xf.$(this.xform.id);

	xformEl.appendChild(el);
}

xf.TextSection.prototype.addField = function(request, target) {}

xf.TextSection.prototype.doExport = function() {
	return '{"type":"text","editPosition":"'+ this.editPosition +'","tag":"' + this.tag + '","text":"' + this.text + '"}';
}

xf.TextSection.prototype.selectSomething = function(e) {
	this.xform.selectionListener.select(this);
}

xf.TextSection.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('表单标题', this.text, this.updateText, this, formNode);
	xf.createField('可编辑表头(下拉框)', this.editPosition, this.updateEditPosition, this, formNode);
}

xf.TextSection.prototype.updateText = function(text) {
	this.text = text;
	var el = xf.$(this.id);
	el.innerHTML = '<input type="hidden" name = "formTitle"/><' + this.tag + ' style="text-align:center;">' +
		this.text +
		'</' + this.tag + '>';
}
xf.TextSection.prototype.updateEditPosition = function(diyTitle) {
	if(diyTitle){
		var titleItem = diyTitle.split("|")
		if(titleItem.length == 1){
			titleItem = [0,titleItem.join(",")]
		}
		if(isNaN(titleItem[0]-0)){
			alert("请输入可编辑文字的位置！（只能为数字）")
		}else{
			if(titleItem[0]-0 <= this.text.length){
				this.editPosition = diyTitle
				this.text = this.text.replace(/<select(.*?)<\/select>/g,"")
				var str1 =  this.text.substr(0,titleItem[0]-0)
				var str2 = this.text.substr(titleItem[0]-0)
				var selectItem = titleItem[1].split(",")
				var selectStr = "<select onchange=serTitleInfo(this)>"
				for(var i = 0 ;i<selectItem.length;i++){
					selectStr += "<option value="+selectItem[i]+">"+selectItem[i]+"</option>"
				}
				selectStr+="</select>"
				this.text = (str1+selectStr+str2).toString()
			}else{
				alert("文字的位置大于标题长度！")
			}
		}
	}else{
		this.editPosition = ""
		this.text = this.text.replace(/<select(.*?)<\/select>/g,"")
	}
	this.updateText(this.text)
}
function serTitleInfo (self){
	var selectStr =  $(self).val()
	if(selectStr){
		var titleStr = $(self).parent().html().replace(/<select(.*?)<\/select>/g,"&").split("&")
		$("[name=formTitle]").val(titleStr[0]+selectStr+titleStr[1])
		$(self).removeClass('requi')
	}else{
		$(self).addClass('requi')
	}
}
xf.TextSection.prototype.mergeStart = function(e) {}

xf.TextSection.prototype.mergeMove = function(e) {}

xf.TextSection.prototype.mergeEnd = function(e) {}

xf.TextSection.prototype.merge = function() {}

xf.TextSection.prototype.split = function() {}

xf.TextSection.prototype.setValue = function(data) {}

;

xf.GridSection = function(row, col) {
	this.row = row;
	this.col = col;
	this.fieldMap = {};
	this.mergeMap = {};

	this.selectedItems = [];
	this.startCell = [];
	this.endCell = [];
}

xf.GridSection.prototype.findCell = function(el) {
	while(el) {
		if(el.className && el.className.indexOf('xf-cell') != -1) {
			return el;
		}
		el = el.parentNode;
	}
	return false;
}

xf.GridSection.prototype.merge = function() {
	if(this.selectedItems.length == 0) {
		return false;
	}
	var minRow = this.minRow;
	var minCol = this.minCol;
	var maxRow = this.maxRow;
	var maxCol = this.maxCol;

	var startId = this.id + '-' + minRow + '-' + minCol;
	var el = xf.$(startId);
	if(!el) {
		return false;
	}

	var colSpan = el.colSpan + maxCol - minCol;
	var rowSpan = el.rowSpan + maxRow - minRow;
	el.setAttribute("colspan", colSpan);
	el.setAttribute("rowspan", rowSpan);
	el.setAttribute("width", (100 * colSpan / this.col) + '%');
	var els = this.selectedItems.slice(1);
	for(var i = 0; i < els.length; i++) {
		var el = els[i];
		if(el){
			el.parentNode.removeChild(el);
		}
	}

	this.mergeMap[minRow + '-' + minCol] = {
		minRow: minRow,
		minCol: minCol,
		maxRow: maxRow,
		maxCol: maxCol
	};

	this.selectedItems = [];
	$("#xf-form td").removeClass("active")
}

xf.GridSection.prototype.split = function() {
	if(this.selectedItems.length == 0) {
		return false;
	}
	var el = this.selectedItems[0];
	var position = this.getPosition(el);
	var minRow = position.row;
	var minCol = position.col;

	if(position.width == 1 && position.height == 1) {
		return false;
	}
	el.setAttribute("colspan", 1);
	el.setAttribute("rowspan", 1);
	el.setAttribute("width", (100 / this.col) + '%');

	for(var i = 0; i < position.width; i++) {

		for(var j = 0; j < position.height; j++) {
			if(i == 0 && j == 0) {
				continue;
			}
			var targetRow = position.row + j;
			var targetCol = position.col + i;

			var td = document.createElement('td');
			td.setAttribute("width", (100 / this.col) + '%');
			td.innerHTML = '<div>&nbsp;</div>';
			td.id = this.id + '-' + targetRow + '-' + targetCol;

			td.className = 'xf-cell';

			var preTd = xf.$(this.id + '-' + targetRow + '-' + (targetCol - 1));

			if(preTd == null) {
				var tr = xf.$(this.id + '-' + targetRow);
				if(tr.children.length == 0) {
					tr.appendChild(td);
				} else {
					tr.insertBefore(td, tr.firstChild);
				}
			} else {
				xf.insertAfter(td, preTd);
			}
		}
	}

	delete this.mergeMap[minRow + '-' + minCol];

	this.selectedItems = [];
	var tds = document.getElementsByTagName('TD');
	for(var i = 0; i < tds.length; i++) {
		xf.removeClass(tds[i], 'active');
	}
}

xf.GridSection.prototype.render = function() {
	// table
	var el = document.createElement('div');
	el.id = this.id;
	el.className = 'xf-section';

	var html = '<table class="xf-table table-bordered" cellspacing="0" cellpadding="0" width="100%" align="center" border="0">' +
		'<tbody>';

	for(var i = 0; i < this.row; i++) {
		var rowId = this.id + '-' + i;

		html += '<tr id="' + rowId + '">'
		for(var j = 0; j < this.col; j++) {
			var cellId = rowId + '-' + j;
			var cellClassName = 'xf-cell';
			html += '<td id="' + cellId + '" class="' + cellClassName + '" width="' + (100 / this.col) + '%">&nbsp;' +
				'</td>';
		}
		html += '</tr>';
	}

	html += '</tbody>' +
		'</table>';

	el.innerHTML = html;

	var xformEl = xf.$(this.xform.id);
	xformEl.appendChild(el);

	// field
	for(var key in this.fieldMap) {
		var field = this.fieldMap[key];
		field.render();
	}

	// merge
	for(var key in this.mergeMap) {
		var info = this.mergeMap[key];
		this.minRow = info.minRow;
		this.minCol = info.minCol;
		this.maxRow = info.maxRow;
		this.maxCol = info.maxCol;
		for(var i = this.minRow; i <= this.maxRow; i++) {
			for(var j = this.minCol; j <= this.maxCol; j++) {
				var node = xf.$(this.id + '-' + i + '-' + j);
				this.selectedItems.push(node);
			}
		}
		this.merge();
	}
	var self = this
	$("#xf-form").on("click","td",function(e){
		self.selectedItems = [];
		if($(e.target).hasClass("xf-cell")){
			self.selectedItems.push(e.target)
		}else{
			self.selectedItems.push($(e.target).parents(".xf-cell")[0])
		}
		$("#xf-form td").removeClass("active")
		$(this).addClass("active")
	})
}

xf.GridSection.prototype.addField = function(request, target) {
	var el = this.findCell(target);
	if(el) {
		var fieldFactory = this.xform.fieldFactory;
		var field = fieldFactory.create(request.fieldType, el);
		field.render();
		this.fieldMap[el.id] = field;
	}
}

xf.GridSection.prototype.doExport = function() {
	var text = '{"type":"grid","row":"' + this.row + '","col":"' + this.col + '","merge":[';

	var mergeExists = false;
	for(var key in this.mergeMap) {
		mergeExists = true;

		var startId = key;
		var mergeInfo = this.mergeMap[startId];
		text += '{"startId":"' + startId +
			'","minRow":' + mergeInfo.minRow +
			',"minCol":' + mergeInfo.minCol +
			',"maxRow":' + mergeInfo.maxRow +
			',"maxCol":' + mergeInfo.maxCol +
			'},';
	}
	if(mergeExists) {
		text = text.substring(0, text.length - 1);
	}

	var fieldExists = false;
	text += '],"fields":[';
	for(var key in this.fieldMap) {
		fieldExists = true;

		var fieldId = key;
		var fieldValue = this.fieldMap[fieldId];
		text += fieldValue.doExport() + ',';
	}
	if(fieldExists) {
		text = text.substring(0, text.length - 1);
	}

	text += ']}';
	return text;
}

xf.GridSection.prototype.addRow = function() {
	var tbody = this.findTbody();
	var tr = document.createElement('tr');
	tr.id = this.id + '-' + this.row;
	tbody.appendChild(tr);
	for(var i = 0; i < this.col; i++) {
		var td = document.createElement('td');
		td.id = tr.id + '-' + i;
		td.className = 'xf-cell';
		td.width = (100 / this.col) + '%'
		td.innerHTML = '&nbsp;';
		tr.appendChild(td);
	}
	this.row++;
	this.selectedItems = [];
	$("#xf-form td").removeClass("active")
}

xf.GridSection.prototype.removeRow = function() {
	if(this.selectedItems.length == 0) {
		this.selectedItems = [];
		$("#xf-form td").removeClass("active")
		return false;
	}

	var td = this.selectedItems[0];
	var tr = td.parentNode;
	var tbody = tr.parentNode;

	var rowIndex = 0;

	for(var i = 0; i < tbody.children.length; i++) {
		var child = tbody.children[i];
		if(child == tr) {
			rowIndex = i;
			break;
		}
	}

	try {
		if(tbody.children[0] == tr) {
			for(var i = 0; i < tbody.children[1].children.length; i++) {
				var td = tbody.children[1].children[i];
				//				td.className += ' xf-cell-top';
			}
		}
	} catch(e) {}

	try {
		if(tbody.children[tbody.children.length - 1] == tr) {
			for(var i = 0; i < tbody.children[tbody.children.length - 2].children.length; i++) {
				var td = tbody.children[tbody.children.length - 2].children[i];
				//				td.className += ' xf-cell-bottom';
			}
		}
	} catch(e) {}

	tr.parentNode.removeChild(tr);

	for(var i = rowIndex; i < this.row - 1; i++) {
		var targetRowElement = tbody.children[i];
		var array = targetRowElement.getAttribute("id").split("-");
		var prefix = array[0] + '-' + array[1] + '-';
		targetRowElement.setAttribute("id", prefix + rowIndex);
		for(var j = 0; j < this.col; j++) {
			var targetColElement = targetRowElement.children[j];
			targetColElement.setAttribute("id", prefix + rowIndex + "-" + j);
		}
	}

	for(var key in this.fieldMap) {
		var field = this.fieldMap[key];
		if(field.row == rowIndex) {
			delete this.fieldMap[key];
			continue;
		}
		if(field.row > rowIndex) {
			field.row -= 1;
		}
	}

	this.row--;

	this.selectedItems = [];
	$("#xf-form td").removeClass("active")

};

xf.GridSection.prototype.addCol = function() {
	var tbody = this.findTbody();
	var trAll = document.querySelectorAll("#xf-form table tr")
	for(var i = 0; i < trAll.length; i++) {
		var createTd = document.createElement('td');
		var td = trAll[i].getElementsByTagName("td")
		if(td.length == 0) {
			createTd.id = this.id + "-0-0"
		} else {
			createTd.id = td[td.length - 1].id.replace(/xf-(\d+)-(\d+)-(\d+)/, function(a, b, c, d) {
				var da = ++d;
				return "xf-" + b + "-" + c + "-" + da;
			})
		}
		createTd.className = "xf-cell"
		createTd.innerHTML = "&nbsp;"
		trAll[i].appendChild(createTd)
	}
	var newtd = document.querySelectorAll("#xf-form table tr td")
	for(i = 0; i < newtd.length; i++) {
		var colNum
		if(newtd[i].colspan) {
			colNum = newtd[i].colspan
		} else {
			colNum = 1
		}
		newtd[i].setAttribute('width', 100 / (this.col + 1) * colNum + "%");
	}

	this.col++;
	this.selectedItems = [];
	$("#xf-form td").removeClass("active")
}

xf.GridSection.prototype.removeCol = function() {
	if(this.selectedItems.length == 0) {
		this.selectedItems = [];
		$("#xf-form td").removeClass("active")
		return false;
	}
	var td = this.selectedItems[0];
	if(td.getAttribute("colspan") > 1 || this.selectedItems.length > 1) {
		alert("删除失败,只能选择一列删除")
		this.selectedItems = [];
		$("#xf-form td").removeClass("active")
		return false
	}
	var tr = td.parentNode
	var tbody = tr.parentNode;
	var ColIndex = -1; //要删除的列
	for(var i = 0; i < tr.children.length; i++) {
		var trColspan = tr.children[i].getAttribute("colspan")
		if(trColspan) {
			ColIndex += parseInt(trColspan) 
		} else {
			ColIndex++
		}
		if(tr.children[i] == td) {
			break
		}
	}
	var delArr = [] //要删除id的数组
	var ColdelIndex = -1
	var isDel = true
	for(var i = 0; i < tbody.children.length; i++) {
		var child = tbody.children[i];
		ColdelIndex = -1
		var delArrLen = delArr.length
		for(var j = 0; j < child.children.length; j++) {
			var tmp = child.children[j].getAttribute("colspan")
			if(tmp) {
				ColdelIndex += parseInt(tmp)
			} else {
				ColdelIndex++
			}
			if(ColdelIndex == ColIndex) {
				if(tmp && tmp != 1) {

				} else {
					delArr.push(child.children[j].id)
				}
				break
			}

		}
		if(delArrLen == delArr.length) {
			alert("删除失败,所删除的列中有合并行")
			isDel = false
			this.selectedItems = [];
			$("#xf-form td").removeClass("active")
			return false
			break
		}
	}
	if(isDel) {
		//循环删除
		for(var i = 0; i < delArr.length; i++) {
			newid = delArr[i]
			$("#" + delArr[i]).nextAll().each(function(i, v) {
				var tmp = v.id
				v.id = newid
				newid = tmp
			})
			$("#" + delArr[i]).remove()
		}
	}
	this.col--;
	$("#xf-form td").removeClass("active")
	this.selectedItems = [];

};

xf.GridSection.prototype.findTbody = function() {
	var el = xf.$(this.id);

	for(var i = 0; i < el.childNodes.length; i++) {
		var childNode = el.childNodes[i];
		if(childNode.tagName == 'TABLE') {
			el = childNode;
		}
	}

	for(var i = 0; i < el.childNodes.length; i++) {
		var childNode = el.childNodes[i];
		if(childNode.tagName == 'TBODY') {
			el = childNode;
		}
	}
	return el;
}

xf.GridSection.prototype.selectSomething = function(e) {
	var target = xf.getTarget(e);
	var cell = this.findCell(target);
	var field = this.fieldMap[cell.id];
	this.xform.selectionListener.select(field);
}

xf.GridSection.prototype.doImport = function(sectionData) {
	this.fieldMap = {};

	for(var i = 0; i < sectionData.fields.length; i++) {
		var fieldData = sectionData.fields[i];
		var field = this.xform.fieldFactory.create(fieldData.type);
		field.parentId = this.id + '-' + fieldData.row + '-' + fieldData.col;
		for(var key in fieldData) {
			field[key] = fieldData[key];
		}
		this.fieldMap[field.parentId] = field;
	}

	this.mergeMap = {};
	if(sectionData.merge) {
		for(var i = 0; i < sectionData.merge.length; i++) {
			var mergeData = sectionData.merge[i];
			this.mergeMap[mergeData.startId] = mergeData;
		}
	}
}

xf.GridSection.prototype.getPosition = function(el) {
	if(!el.id) {
		return false;
	}
	var array = el.id.split('-');
	var p = {};
	p.row = parseInt(array[2]);
	p.col = parseInt(array[3]);
	p.width = parseInt(el.colSpan);
	if(p.width == 0) {
		p.width = 1;
	}
	p.height = parseInt(el.rowSpan);
	if(p.height == 0) {
		p.height = 1;
	}
	return p;
}

xf.GridSection.prototype.mergeStart = function(e) {
	var target = xf.getTarget(e);
	var cell = this.findCell(target);

	if(!cell) {
		return;
	}

	for(var i = 0; i < this.selectedItems.length; i++) {
		xf.removeClass(this.selectedItems[i], 'active');
	}
	xf.addClass(cell, 'active');

	this.status = 'DRAG';
	this.startCell = cell;
	this.selectedItems.push(cell);
}

xf.GridSection.prototype.mergeMove = function(e) {

	if(this.status == 'DRAG') {
		var target = xf.getTarget(e);
		var cell = this.findCell(target);

		if(!cell) {
			return;
		}
		var endCell = cell;
		var startCell = this.startCell;

		var startPosition = this.getPosition(startCell);
		var endPosition = this.getPosition(endCell);

		if(startCell.id == endCell.id) {
			return;
		}

		var minRow = Math.min(startPosition.row, endPosition.row);
		var minCol = Math.min(startPosition.col, endPosition.col);
		var maxRow = Math.max(startPosition.row, endPosition.row);
		var maxCol = Math.max(startPosition.col, endPosition.col);

		this.selectedItems = [];
		this.minRow = minRow;
		this.minCol = minCol;
		this.maxRow = maxRow;
		this.maxCol = maxCol;
		for(var i = minRow; i <= maxRow; i++) {
			for(var j = minCol; j <= maxCol; j++) {
				var el = xf.$(this.id + '-' + i + '-' + j);
				if(el){
					xf.addClass(el, 'active');
					this.selectedItems.push(el);
				}
			}
		}

	}
}

xf.GridSection.prototype.mergeEnd = function(e) {
	if(this.status == 'DRAG') {
		this.status = 'DROP';
	}
}

xf.GridSection.prototype.findField = function(target) {
	var cellEl = this.findCell(target);
	return this.fieldMap[cellEl.id];
}

xf.GridSection.prototype.moveTo = function(field, target) {
	var cellEl = this.findCell(target);
	var position = this.getPosition(cellEl);

	if(field.row == position.row && field.col == position.col) {
		return;
	}

	var fieldId = this.id + '-' + field.row + '-' + field.col;

	delete this.fieldMap[fieldId];
	xf.$(field.parentId).innerHTML = '';

	var row = position.row;
	var col = position.col;
	field.row = row;
	field.col = col;

	var fieldId = this.id + '-' + field.row + '-' + field.col;
	this.fieldMap[this.id + '-' + field.row + '-' + field.col] = field;

	field.parentId = cellEl.id;
	field.render();
}

xf.GridSection.prototype.setValue = function(data) {
	for(var key in this.fieldMap) {
		var field = this.fieldMap[key];
		var value = data[field.name];
		if(value) {
			field.setValue(value);
		}
	}
}

;

xf.Proxy = function() {
	this.id = 'xf-proxy';
	this.status = 'uninitialized';
	this.init();
}

xf.Proxy.prototype.init = function() {
	if(this.status == 'uninitialized') {
		var el = document.createElement('div');
		el.id = this.id;
		el.innerHTML = '&nbsp;';
		el.style.position = 'absolute';
		el.style.top = -100 + 'px';
		el.style.left = -100 + 'px';
		el.style.zIndex = 10000;
		el.style.width = '50px';
		el.style.backgroundColor = '#DDDDDD';
		el.style.border = 'dotted 1px gray';
		document.body.appendChild(el);
		this.status = 'initialized';
	}
}

xf.Proxy.prototype.move = function(x, y) {
	var el = xf.$(this.id);
	el.style.top = y + 'px';
	el.style.left = x + 'px';
}

xf.Proxy.prototype.hide = function() {
	this.move(-100, -100);
}

;

//自动计算
function setFormula (Formula){
	if(Formula){
		var FormulaArr =  Formula.split(",")
		for(var i = 0 ;i<FormulaArr.length;i++){
			FormulaArr[i].replace(/(\w+)([+\-*/]{1})(\w+)[=]{1}(\w+)/g,function(all,a,b,c,d){
				$("[name="+ d +"]").attr("readonly","readonly")
				$("[name="+ a +"]").on("change",function(){
					var selfDom = $("[name="+ c +"]").val()
					if(selfDom && !isNaN(Number(selfDom))){
						$("[name="+ d +"]").val((eval(selfDom + b + this.value)-0).toFixed(2)).trigger("change").attr("readonly","readonly").attr("disabled",false)			 		
					}
				})
				$("[name="+ c +"]").on("change",function(){
					var selfDom = $("[name="+ a +"]").val()
					if(selfDom && !isNaN(Number(selfDom))){
						$("[name="+ d +"]").val((eval(selfDom + b + this.value)-0).toFixed(2)).trigger("change").attr("readonly","readonly").attr("disabled",false)		
					}
				})
			})
		}
	}
}

//自动计算(待修复)
//function setFormula (Formula){
//	if(Formula) {
//		var FormulaArr = Formula.split(",")
//		for(var i = 0; i < FormulaArr.length; i++) {
//			var indexEqual =FormulaArr[i].indexOf("=")
//			if(indexEqual != -1){
//				var resName =  FormulaArr[i].substr(indexEqual+1);
//				var setFormula = ""
//				var nameArr = []
//				 FormulaArr[i].replace(/(\w+)([=|+|-|*|\/]{1})/g,function(x,name,symbol){
//				 	setFormula += ('($("[name='+ name +']").val()?$("[name='+ name +']").val():0)' + (symbol=="="?"":symbol));
//				 	nameArr.push(name)
//				 })
//				 for (var j = 0 ;j<nameArr.length;j++) {
//				 	$("[name=" + nameArr[j] + "]").on("change",function(){
//				 		$("[name="+ resName +"]").val((eval(setFormula)-0).toFixed(2)).trigger("change").attr("readonly","readonly")
//					 })
//				 }
//			}
//		}
//	}
//}


xf.field.FieldFactory = function() {
	this.fieldTypeMap = {};
	this.register('label', xf.field.Label);
	this.register('textfield', xf.field.TextField);
	this.register('password', xf.field.Password);
	this.register('textarea', xf.field.TextArea);
	this.register('select', xf.field.Select);
	this.register('radio', xf.field.Radio);
	this.register('checkbox', xf.field.Checkbox);
	this.register('fileupload', xf.field.FileUpload);
	this.register('datepicker', xf.field.DatePicker);
	this.register('userpicker', xf.field.UserPicker);
	this.register('chilentTable', xf.field.chilentTable);
	this.register('userModlue', xf.field.userModlue);
	this.register('departmentModlue', xf.field.departmentModlue);
	this.register('LabelText', xf.field.LabelText);
}

function styleArr (styleStr){
	var styleArr = styleStr
	var str = ""
	if ( styleArr.indexOf(",") == -1){
		styleArr = [styleArr]
	}else{
		styleArr = styleArr.split(",")
	}
	for (var i = 0 ;i<styleArr.length;i++) {
		switch (styleArr[i]){
			case "0":
			str+="border-width: 1px !important;"
				break;
			case "1":
			str+="border-top-width: 0;"
				break;
			case "2":
			str+="border-left-width: 0;"
				break;
			case "3":
			str+="border-bottom-width: 0;"
				break;
			case "4":
			str+="border-right-width: 0;"
				break;
			case "5":
			str+="border-width: 0 !important;"
				break;
			default:
				break;
		}	
	}
	return str
}

xf.field.FieldFactory.prototype.create = function(type, parentNode) {
	var constructor = this.fieldTypeMap[type];
	var field = new constructor(parentNode);
	return field;
}

xf.field.FieldFactory.prototype.register = function(key, value) {
	this.fieldTypeMap[key] = value;
}

;

xf.field.Label = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'label-' + this.row + '-' + this.col;
	this.text = "text";
	this.align = "center"
	this.clearBorder = 0;
}

xf.field.Label.prototype.render = function() {
	this.updateText(this.text);
}

xf.field.Label.prototype.doExport = function() {
	return '{"type":"label","row":' + this.row +
		',"col":' + this.col +
		',"text":"' + this.text +
		'","align":"' + this.align +
		'","clearBorder":"' + this.clearBorder +
		'"}';
}

xf.field.Label.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('text', this.text, this.updateText, this, formNode);
	formNode.appendChild(document.createElement('br'));
	xf.createSelectField('value', {"center":"居中对齐","left":"左对齐","right":"右对齐"},this.align, this.alignFn, this, formNode)
	formNode.appendChild(document.createElement('br'));
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearborder, this, formNode,true);
}
xf.field.Label.prototype.clearborder = function(clearborder) {
	this.clearBorder = clearborder
	this.updateText(this.text)
}
xf.field.Label.prototype.alignFn = function(align) {
	this.align = align
	this.updateText(this.text)
}
xf.field.Label.prototype.updateText = function(text) {
	this.text = text;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">' +
		'<label style="display:block;text-align:'+ (this.align&&this.align!="undefined" ? this.align : 'center') +';margin-bottom:0px;">' + text + '</label>' +
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}
;

xf.field.TextField = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'textfield-' + this.row + '-' + this.col;
	this.clearBorder = 0;
	this.maxLength = "200";
	this.isType = "0"
	this.decimalPoint = "-1"
	this.item = "text"
}

xf.field.TextField.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.TextField.prototype.doExport = function() {
	return '{"type":"textfield","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","isType":"'+this.isType+
		'","maxLength":"'+ this.maxLength +
		'","decimalPoint":"'+ this.decimalPoint +
		'","item":"'+ this.item +
		'"}';
}

xf.field.TextField.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
	xf.createField('maxLength', this.maxLength, this.updateMaxLength, this, formNode);
	xf.createSelectField('isType',{"0":"无限制","1":"只能输入文字","2":"只能输入正整数","3":"只能输入数字(带小数)","4":"只能输入汉字或者字母","5":"只能输入数字或者字母","6":"金钱格式","7":"只能输入手机号码","8": "只能输入固定电话","9": "输入手机或者固定电话","10": "身份证","11": "邮箱","12": "ip地址","13": "网址","14": "年龄(周天年)","15": "纯数字"}, this.isType, this.isTypeFn, this, formNode,false);
	xf.createField('decimalPoint', this.decimalPoint, this.updateDecimalPoint, this, formNode);
}
xf.field.TextField.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">'+
		'<input item = "'+ this.item +'" class="form-control" type="text" name="' + this.name + '" '  +
		' value="' + (this.value ? this.value : '') + '"' +
		(this.isType == "0"?'':'onchange="'+ ChangeAll(this.isType,this.decimalPoint-0)+'"' ) + ' style="margin-bottom:0px;" maxlength="'+ this.maxLength +'">'+
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}
xf.field.TextField.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.TextField.prototype.updateDecimalPoint = function(value) {
	this.decimalPoint = value;
	this.updateName(this.name)
}
xf.field.TextField.prototype.updateisNumber = function(value) {
	this.isNumber = value;
	this.updateName(this.name)
}
xf.field.TextField.prototype.updateMaxLength = function(value) {
	this.maxLength = value;
	this.updateName(this.name)
}
xf.field.TextField.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}
xf.field.TextField.prototype.isTypeFn = function(value) {
	this.isType = value;
	this.updateName(this.name)
}
xf.field.TextField.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}
//change函数
function ChangeAll(number,decimalPoint) {
	var listObj = {
		"0": "",
		'1': "ChangeFn.textChange(this)",
		'2': "ChangeFn.numberChange(this)",
		"3": "ChangeFn.numberChangSmall(this,"+ decimalPoint +")",
		"4": "ChangeFn.textAndLetterChange(this)",
		"5": "ChangeFn.textAndNumber(this)",
		"6": "ChangeFn.moneyAtBig(this,"+ decimalPoint +")",
		"7": "ChangeFn.isMovePhone(this)",
		"8": "ChangeFn.isFixationPhone(this)",
		"9": "ChangeFn.isFixationOrMovephone(this)",
		"10": "ChangeFn.isIdCald(this)",
		"11": "ChangeFn.isEmail(this)",
		"12": "ChangeFn.isIp(this)",
		"13": "ChangeFn.isSrc(this)",
		"14": "ChangeFn.isAge(this)",
		"15":"ChangeFn.upnumberChange(this)"
	}
	return listObj[number]
}

var ChangeFn = {}
ChangeFn.textChange = function(self){
	if(!/^[\u4e00-\u9fa5]*$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入汉字');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//正整数
ChangeFn.numberChange = function (self){
	
	if(!/^([0]{1}|[1-9]{1})[0-9]*$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入正整数!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
// 数字
ChangeFn.upnumberChange = function (self){
	if(!/^[0-9]*$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入数字!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}

ChangeFn.numberChangSmall = function(self,decimalPoint){
	var reg = new RegExp("^\\d+(\\.\\d{0,"+ decimalPoint +"})?$");
	if(!reg.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','请输入正确的数字格式(小数点后最多能有'+ decimalPoint +'位!)');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
ChangeFn.textAndLetterChange = function(self){
	if(!/^([\u4e00-\u9fa5]|[a-zA-Z])*$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入汉字或者字母!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
ChangeFn.moneyAtBig = function(self,decimalPoint){
	setTimeout(function(){
		var reg = new RegExp("^([0]{1}|[1-9]{1}[0-9]*)(\\.\\d{0,2})?$");
		var dom = $("[name="+$(self).attr("name")+"TranBig"+"]") 
		if(!reg.test(self.value)) {
			if(self.value != ""){
				$.alert('提示','请输入正确的金额格式(小数点后最多能有2位!)');
			}
			self.value = "";
			if($(self).attr("required")){
				$(self).addClass("requi")
				if(dom.length > 0){
					dom.val("").addClass("requi")
				}
			}else{
				if(dom.length > 0){ 
					dom.val("")
				}
			}
		}else{
			$(self).removeClass("requi")
			if(dom.length > 0){
				dom.val(convertCurrency(self.value)).removeClass("requi").attr("readonly","readonly").attr("disabled",false)
			}
		}
	},50)
}
//手机
ChangeFn.isMovePhone = function(self){
	if(!/^1[3|4|5|8][0-9]\d{8}$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示',"只能输入手机号!");
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//固定电话
ChangeFn.isFixationPhone = function(self){
	if(!/^\d{3}-\d{8}|\d{4}-\{7,8}$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入国内固定电话!例010-88888888');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//手机或者固定电话
ChangeFn.isFixationOrMovephone = function(self){
	if(!/(^\d{3}-\d{8}$|^\d{4}-\d{7,8}$)|(^1[3|4|5|8][0-9]\d{8}$)/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入国内固定电话或者国内手机号码!例010-88888888|18811112222');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//身份证
ChangeFn.isIdCald = function(self){
	if(!/(^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$)|(^\d{15}$)/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','请输入合法的身份证号码!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//邮箱
ChangeFn.isEmail = function(self){
	if(!/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','请输入正确的邮箱格式!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//ip
ChangeFn.isIp = function(self){
	if(!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','请输入正确的ip地址!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//网址
ChangeFn.isSrc = function(self){
	if(!/[a-zA-z]+:\/\/[^\s]*/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','请输入正确的网址(例:http://......)');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
//年龄
ChangeFn.isAge = function(self){
	if(!/^\d{1,3}(岁|周|天){1}$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','请输入正确的年龄(例:xx岁,xx周,xx天)');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
ChangeFn.textAndNumber = function(self){
	if(!/^[0-9a-zA-Z]*$/.test(self.value)) {
		if(self.value != ""){
			$.alert('提示','只能输入数字或者字母!');
		}
		self.value = "";
		if($(self).attr("required")){
			$(self).addClass("requi")
		}
	}else{
		$(self).removeClass("requi")
	}
}
function convertCurrency(money) {
  //汉字的数字
  var cnNums = new Array('零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖');
  //基本单位
  var cnIntRadice = new Array('', '拾', '佰', '仟');
  //对应整数部分扩展单位
  var cnIntUnits = new Array('', '万', '亿', '兆');
  //对应小数部分单位
  var cnDecUnits = new Array('角', '分', '毫', '厘');
  //整数金额时后面跟的字符
  var cnInteger = '整';
  //整型完以后的单位
  var cnIntLast = '元';
  //最大处理的数字
  var maxNum = 999999999999999.9999;
  //金额整数部分
  var integerNum;
  //金额小数部分
  var decimalNum;
  //输出的中文金额字符串
  var chineseStr = '';
  //分离金额后用的数组，预定义
  var parts;
  if (money == '') { return ''; }
  money = parseFloat(money);
  if (money >= maxNum) {
    //超出最大处理数字
    return '';
  }
  if (money == 0) {
    chineseStr = cnNums[0] + cnIntLast + cnInteger;
    return chineseStr;
  }
  //转换为字符串
  money = money.toString();
  if (money.indexOf('.') == -1) {
    integerNum = money;
    decimalNum = '';
  } else {
    parts = money.split('.');
    integerNum = parts[0];
    decimalNum = parts[1].substr(0, 4);
  }
  //获取整型部分转换
  if (parseInt(integerNum, 10) > 0) {
    var zeroCount = 0;
    var IntLen = integerNum.length;
    for (var i = 0; i < IntLen; i++) {
      var n = integerNum.substr(i, 1);
      var p = IntLen - i - 1;
      var q = p / 4;
      var m = p % 4;
      if (n == '0') {
        zeroCount++;
      } else {
        if (zeroCount > 0) {
          chineseStr += cnNums[0];
        }
        //归零
        zeroCount = 0;
        chineseStr += cnNums[parseInt(n)] + cnIntRadice[m];
      }
      if (m == 0 && zeroCount < 4) {
        chineseStr += cnIntUnits[q];
      }
    }
    chineseStr += cnIntLast;
  }
  //小数部分
  if (decimalNum != '') {
    var decLen = decimalNum.length;
    for (var i = 0; i < decLen; i++) {
      var n = decimalNum.substr(i, 1);
      if (n != '0') {
        chineseStr += cnNums[Number(n)] + cnDecUnits[i];
      }
    }
  }
  if (chineseStr == '') {
    chineseStr += cnNums[0] + cnIntLast + cnInteger;
  } else if (decimalNum == '') {
    chineseStr += cnInteger;
  }
  return chineseStr;
}

xf.field.Password = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'password-' + this.row + '-' + this.col;
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.Password.prototype.render = function() {
	this.updateName(this.name);
}
xf.field.Password.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.Password.prototype.doExport = function() {
	return '{"type":"password","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}

xf.field.Password.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
}

xf.field.Password.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">' +
		'<input item = "'+ this.item +'" type="password" name="' + this.name + '" ' +
		(this.readOnly ? 'readOnly' : '') +
		(this.required ? ' required="true" class="required"' : '') +
		' style="margin-bottom:0px;" maxlength="200">' +
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}

xf.field.Password.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}


xf.field.Password.prototype.setValue = function(value) {}

;

xf.field.TextArea = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'textarea-' + this.row + '-' + this.col;
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.TextArea.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.TextArea.prototype.doExport = function() {
	return '{"type":"textarea","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}
xf.field.TextArea.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.TextArea.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
}

xf.field.TextArea.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler" >' +
			'<div class="input-group userExamineBoxAll" style="width:100%;height: 160px;">' +
				'<textarea item = "'+ this.item +'" style="width: 100%;height: 100%;" name="' + this.name + '" class="form-control spDom" maxlength="100" >' + (this.value ? this.value : '') + '</textarea>' +
				'<div class="userExamine clearfix">' +
					'<div class="zhangBox">' +
						'<div class="targetZhangBox" style="position: absolute;bottom: 0;width: 100%;">' +
							'<img class="imgZhangImg" src="" />' +
							'<span class = "imgZhangspan">盖章：</span>' +
							'<img class="imgnameImg" src="" />' +
							'<span class = "imgnamespan">签名：</span>' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</div>' +
		'</div>' ;
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}


xf.field.TextArea.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}

xf.field.TextArea.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}
;
function qianziFn (self){
	var $self = $(self)
	$self.parent().find(".userExamine").css("display","block")
	var imgZhangSelect = $self.next().find(".imgZhangSelect")
	if (imgZhangSelect.find("option").length>1){
		imgZhangSelect.css("display","block")
	}
}
function SelectZhang (self){
	var $self = $(self)
	$self.next().find(".imgZhangImg").attr("src",$self.val())
}
xf.field.Select = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'select-' + this.row + '-' + this.col;
	this.items = '';
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.Select.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.Select.prototype.doExport = function() {
	return '{"type":"select","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","items":"' + this.items +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}

xf.field.Select.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createField('content', this.items, this.updateItems, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
}

xf.field.Select.prototype.updateName = function(value) {
	this.name = value;
	this.updateItems(this.items);
}
xf.field.Select.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.Select.prototype.updateItems = function(value) {
	this.items = value;
	var parentNode = xf.$(this.parentId);
	var html =
		'<div class="xf-handler">' +
		'<select item = "'+ this.item +'" class="form-control" name="' + this.name + '" ' + (this.readOnly ? 'disabled' : '') +
		(this.required ? ' required="true" class="form-control required"' : '') + ' style="margin-bottom:0px;width:auto;">';
	var array = this.items.split(',');
	for(var i = 0; i < array.length; i++) {
		var item = array[i];
		html += '<option value="' + item + '" ' + (this.value == item ? 'selected' : '') + '>' + item + '</option>';
	}
	html += '</select>' +
		'</div>';
	parentNode.innerHTML = html;
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}

//xf.field.Select.prototype.updateRequired = function(value) {
//	this.required = value;
//}
//
//xf.field.Select.prototype.updateReadOnly = function(value) {
//	this.readOnly = value;
//}

xf.field.Select.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}

xf.field.Select.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}

;

xf.field.Radio = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'radio-' + this.row + '-' + this.col;
	this.items = '';
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.Radio.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.Radio.prototype.doExport = function() {
	return '{"type":"radio","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","items":"' + this.items +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}

xf.field.Radio.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createField('content', this.items, this.updateItems, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);

}
xf.field.Radio.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.Radio.prototype.updateName = function(value) {
	this.name = value;
	this.updateItems(this.items);
}

xf.field.Radio.prototype.updateItems = function(value) {
	this.items = value;
	var parentNode = xf.$(this.parentId);
	var html = '<div class="xf-handler radio radio-theme">';
	var array = this.items.split(',');
	for(var i = 0; i < array.length; i++) {
		var item = array[i];
		html += '<input item = "'+ this.item +'" onchange = RadioChange("'+ this.name+'")  type="radio" id = "'+ this.name+ i + '" name="' + this.name + '" value="' + item + '"  >';
		html += '<label for="' + this.name + i + '" class="radio-inline" >';
		html += item;
		html += '</label>';
	}
	parentNode.innerHTML = html + '</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}
function  RadioChange (name) {
	$("[name='"+name+"']").removeClass("requi")
}

xf.field.Radio.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}


xf.field.Radio.prototype.setValue = function(value) {
	this.value = value;
	this.updateItems(this.items);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}

;

xf.field.Checkbox = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'checkbox-' + this.row + '-' + this.col;
	this.items = '';
	this.clearBorder = 0;
	this.item = "text"
	this.checkboxShow = ""
}

xf.field.Checkbox.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}
var checkboxOrderData = {}
xf.field.Checkbox.prototype.doExport = function() {
	return '{"type":"checkbox","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","items":"' + this.items +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'","checkboxShow":"'+ this.checkboxShow +'"}';
}
xf.field.Checkbox.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.Checkbox.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createField('content', this.items, this.updateItems, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
	xf.createField('审批选择显示', this.checkboxShow, this.checkboxShowFn, this, formNode);
}

xf.field.Checkbox.prototype.checkboxShowFn = function(value) {
	this.checkboxShow = value;
	//this.updateItems(this.items);
}

xf.field.Checkbox.prototype.updateName = function(value) {
	this.name = value;
	this.updateItems(this.items);
}

xf.field.Checkbox.prototype.updateItems = function(value) {
	this.items = value;
	var parentNode = xf.$(this.parentId);
	var html = '<div class="xf-handler checkbox checkbox-theme">';
	var array = this.items.split(',');
	if(this.checkboxShow === undefined){
		this.checkboxShow = ""
	}
	var targetCheBoxArr = this.checkboxShow.split(',');
	var valueArray = [];
	if(this.value != null) {
		valueArray = this.value.split(',');
	}

	for(var i = 0; i < array.length; i++) {
		var item = array[i];
		html += '<input orderData = "'+ (targetCheBoxArr[i]?targetCheBoxArr[i]:"") +'" item = "'+ this.item +'" type="checkbox" id = "'+ this.name + i +'" name="' + this.name + '" value="' + item + '" ' + ' style="margin:1px;">';
		html += '<label for = "' + this.name+ i + '" class="checkbox inline" >';
		html += item;
		html += '</label>';
	}
	parentNode.innerHTML = html + '</div>';
	if(this.checkboxShow && this.checkboxShow!="undefined"){
		checkboxOrderData[this.name]  = {}
		for (var i = 0 ;i<targetCheBoxArr.length;i++) {
			checkboxOrderData[this.name][targetCheBoxArr[i]] = false
		}
	}
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}

xf.field.Checkbox.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}

xf.field.Checkbox.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}

;
xf.field.FileUpload = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'fileupload-' + this.row + '-' + this.col;
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.FileUpload.prototype.render = function() {
	this.updateName(this.name);
}

xf.field.FileUpload.prototype.doExport = function() {
	return '{"type":"fileupload","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}
xf.field.FileUpload.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.FileUpload.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
}

xf.field.FileUpload.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">' +
		'<button  onclick="uploadFileClick(this)" style = "cursor: pointer;" type="button" class="btn btn-theme" ><span class = "glyphicon glyphicon-plus"></span><span>选择文件</span></button>'+
		'<input item = "'+ this.item +'" style="display: none;" type="file" onchange = "fileChange(this)" multiple="multiple" name="' + this.name + '" >' +
		'<ul class = "fileList" ></ul>'+
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}

xf.field.FileUpload.prototype.updateRequired = function(value) {
	this.required = value;
}

xf.field.FileUpload.prototype.updateReadOnly = function(value) {
	this.readOnly = value;
}

xf.field.FileUpload.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}
xf.field.FileUpload.prototype.setValue = function(value) {
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler"><a href="../rs/store/view?model=form&key=' + value.key + '">' + value.label + '</a></div>';
	} else {
		var parentNode = xf.$(this.parentId);
		if(parentNode.children.length == 1) {
			var span = document.createElement('span');
			span.innerHTML = '<div class="xf-handler"><a href="../rs/store/view?model=form&key=' + value.key + '">' + value.label + '</a></div>';
			parentNode.appendChild(span);
		} else {
			var span = parentNode.children[1];
			span.innerHTML = '<div class="xf-handler"><a href="../rs/store/view?model=form&key=' + value.key + '">' + value.label + '</a></div>';
		}
	}
}
function uploadFileClick (self){
	$(self).next("input").click()
}
function fileChange(self) {
	if(self.files.length > 5){
		$.alert('提示',"每个上传控件最多只能上传5个文件!");
		self.value = ""
	}else{
		var str = "";
		var fileslist = self.files
		for(var i = 0; i < fileslist.length; i++) {
			str += "<li>"+ fileslist[i].name + "</li>"
		}
		$(self).next(".fileList").html(str)
		$(self).parent().children("a").remove()
	}
}
;

xf.field.DatePicker = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'datepicker-' + this.row + '-' + this.col;
	this.clearBorder = 0;
	this.order = -1;
	this.startTime = ""
	this.endTime = ""
	this.dateType = "0"
	this.item = "text"
}

xf.field.DatePicker.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.DatePicker.prototype.doExport = function() {
	return '{"type":"datepicker","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","dateType":"'+ this.dateType +
		'","order":"'+ this.order +
		'","startTime":"'+ this.startTime+
		'","endTime":"'+this.endTime+
		'","startTimeAtDay":'+this.startTimeAtDay+
		',"endTimeAtDay":'+ this.endTimeAtDay +
		',"item":"'+ this.item +
		'"}';
}
xf.field.DatePicker.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.DatePicker.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
	xf.createSelectField('dateType',{"0":"yyyy-MM-dd","1":"yyyy","2":"yyyy-MM","3":"yyyy-MM-dd HH","4":"yyyy-MM-dd HH:mm","5":"yyyy-MM-dd HH:mm:ss"}, this.dateType, this.dateTypeFn, this, formNode);
	xf.createField('order', this.order, this.updateOrder , this, formNode);
	xf.createTimeInput('startTime', this.startTime, this.updateStartTime, this,formNode);
	xf.createBooleanField('startTimeAtDay', this.startTimeAtDay, this.updateStartTimeAtDay, this, formNode);
	xf.createTimeInput('endTime', this.endTime, this.updateEndTime, this, formNode);
	xf.createBooleanField('endTimeAtDay', this.endTimeAtDay, this.updateEndTimeAtDay, this, formNode);
}

xf.field.DatePicker.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	var timeData ={
		"startTime":this.startTime,
		"endTime":this.endTime,
		"startTimeAtDay":this.startTimeAtDay,
		"endTimeAtDay":this.endTimeAtDay,
		"dateType":this.dateType
	} 
	parentNode.innerHTML =
		'<div class=" input-group xf-handler datepicker date">' +
		'  <input item = "'+ this.item +'" type="text" data = \''+ JSON.stringify(timeData) +'\' order = "'+ this.order +'"  readonly= "readonly" name="' + this.name + '" value="' + (this.value ? this.value : '') + '" ' +
		' style="background-color:white;cursor:default;" class="form-control rili required" onclick = selectTiem(this) "></div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}
function selectTiem (self,statime){
	var startTime,endTime,dateType;
	var data =  JSON.parse($(self).attr('data')) 
	var d = new Date();
	var dataTypeObj = {"0":"yyyy-MM-dd","1":"yyyy","2":"yyyy-MM","3":"yyyy-MM-dd HH","4":"yyyy-MM-dd HH:mm","5":"yyyy-MM-dd HH:mm:ss"}
	if(data.startTimeAtDay){
		startTime = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()
	}else if (!data.startTime){
		startTime = '1370-12-30'
	}else{
		startTime = data.startTime
	}
	if(data.endTimeAtDay){
		endTime = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()
	}else if (!data.endTime){
		endTime = '2099-12-30'
	}else{
		endTime = data.endTime
	}
	if(!data.dateType || data.dateType == "undefind"){
		dateType = dataTypeObj["0"]
	}else{
		dateType = dataTypeObj[data.dateType]
	};
	WdatePicker({
		isShowClear:false,
		maxDate:endTime,
		minDate:startTime,
		dateFmt:dateType,
		onpicking:function(db){
			calc(self,db);
			calcOrder(self,db);
			$(self).removeClass('requi')
		}
	})
}

function calcOrder (self,db){
	var stTimetmp = db.cal.newdate
	var stTimetmpM = stTimetmp["M"]>=10?stTimetmp["M"]:"0"+stTimetmp["M"]
	var stTimetmpD = stTimetmp["d"]>=10?stTimetmp["d"]:"0"+stTimetmp["d"]
	var Timevalue =  stTime = stTimetmp["y"]+"-"+stTimetmpM+"-"+stTimetmpD;
	var order = $(self).removeClass("requi").attr("order")
	var selfTitle = $(self).attr("item")
	if(order != -1){
		var thisTime = new Date(Timevalue).getTime()
		var beforeDom = $('.rili[order='+ (order-0-1) +']')
		if(beforeDom.length>0){
			if(beforeDom.val()){
				var before = new Date(beforeDom.val()).getTime() 
				if( thisTime<before){
					$.alert('提示',selfTitle + "必须大于" + beforeDom.attr("item"))
					setTimeout(function(){
						$(self).addClass("requi").val(null)
					},100)
					return false
				}
			}else{
				for (var i = (order-0-1) ; i>0 ; i--) {
					var dom =$('.rili[order='+ i +']') 
					if(dom.val()){
						var before = new Date(dom.val()).getTime() 
						if(thisTime < before){
							$.alert('提示',selfTitle+"必须大于"+dom.attr("item"))	
							setTimeout(function(){
								$(self).addClass("requi").val(null)
							},100)
							break;
						}
					}
				}
			}
		}
		var afterDom = $('.rili[order='+ (order-0+1) +']')
		if(afterDom.length>0){
			if(afterDom.val()){
				var after =  new Date(afterDom.val()).getTime()  
				if(thisTime>after){
					$.alert('提示',selfTitle +"必选小于"+afterDom.attr("item"))	
					setTimeout(function(){
						$(self).addClass("requi").val(null)
					},100)
					return false
				}
			}else{
				for (var i = (order-0+1) ; true ; i++) {
					var dom = $('.rili[order='+ i +']')
					if(dom.length == 0){
						break
					}else{
						if(dom.val()){
							var before = new Date(dom.val()).getTime() 
							if(thisTime > before){
								$.alert('提示',selfTitle +"必须小于"+dom.attr("item"))	
								setTimeout(function(){
									$(self).addClass("requi").val(null)
								},100)
								break;
							}
						}
					}
				}
			}
		}
	}else {
		return false
	}
};



function calc (self,db){
	var stTime = $("[name=startDate]").val();
	var entTime = $("[name=endDate]").val();
	if($(self).attr("name") == "startDate" ){
		var stTimetmp = db.cal.newdate
		var stTimetmpM = stTimetmp["M"]>=10?stTimetmp["M"]:"0"+stTimetmp["M"]
		var stTimetmpD = stTimetmp["d"]>=10?stTimetmp["d"]:"0"+stTimetmp["d"]
		stTime = stTimetmp["y"]+"-"+stTimetmpM+"-"+stTimetmpD;
	}
	if($(self).attr("name") == "endDate" ){
		var entTimeTmp = db.cal.newdate
		var entTimeTmpM = entTimeTmp["M"]>=10?entTimeTmp["M"]:"0"+entTimeTmp["M"]
		var entTimeTmpD = entTimeTmp["d"]>=10?entTimeTmp["d"]:"0"+entTimeTmp["d"]
		entTime = entTimeTmp["y"]+"-"+entTimeTmpM+"-"+entTimeTmpD;
	}
	if(stTime && entTime) {
		var s = new Date(stTime).getTime()
		var e = new Date(entTime).getTime()
		var res = Math.ceil(parseInt(e - s) / 1000 / 3600 / 24)
		if(res >= 0) {
			var dom = $("[name=num],[name=sum]")
			dom.val((res - 0 + 1)+"天").removeClass("requi").attr("readOnly","readOnly")
		}
	}
}


xf.field.DatePicker.prototype.updateStartTimeAtDay = function(value) {
	this.startTimeAtDay = value
	if(value){
		this.updateStartTime("")
	}
	this.updateName(this.name)
}

xf.field.DatePicker.prototype.updateEndTimeAtDay = function(value) {
	this.endTimeAtDay = value
	if(value){
		this.updateEndTime("")
	}
	this.updateName(this.name)
}

xf.field.DatePicker.prototype.updateStartTime = function(value) {
	this.startTime = value
	this.updateName(this.name)
}

xf.field.DatePicker.prototype.updateEndTime = function(value) {
	this.endTime = value
	this.updateName(this.name)
}

xf.field.DatePicker.prototype.updateOrder = function(value) {
	this.order = value
	this.updateName(this.name)
}

xf.field.DatePicker.prototype.dateTypeFn = function(dateType) {
	this.dateType = dateType;
	this.updateName(this.name)
}
xf.field.DatePicker.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}

xf.field.DatePicker.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}

;

xf.field.UserPicker = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'textarea-' + this.row + '-' + this.col;
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.UserPicker.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}
xf.field.UserPicker.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.UserPicker.prototype.doExport = function() {
	return '{"type":"userpicker","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}

xf.field.UserPicker.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
}

xf.field.UserPicker.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">' +
		'<textarea item = "'+ this.item +'" name="' + this.name + '" ' + (this.readOnly ? 'readOnly' : '') +
		(this.required ? ' required="true" class="form-control required"' : ' class="form-control"') +
		' style="margin-bottom:0px;" maxlength="200">' + (this.value ? this.value : '') + '</textarea>' +
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}

xf.field.UserPicker.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}
xf.field.UserPicker.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
};

xf.field.chilentTable = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'chilentTable-' + this.row + '-' + this.col;
	this.items =  "";
	this.clearBorder = 0;
	this.item = "text"
	this.itemType = ""
	this.isChoice = true
	this.chilentOrder = ""
	this.linkItem = ""
}

xf.field.chilentTable.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.chilentTable.prototype.doExport = function() {
	return '{"type":"chilentTable","row":' + this.row +
		',"col":' + this.col +
		',"name":"' + this.name +
		'","items":"' + this.items +
		'","clearBorder":"' + this.clearBorder + 
		'","chilentOrder":"'+ this.chilentOrder +
		'","item":"'+ this.item +
		'","linkItem":"'+ this.linkItem +
		'","itemType":"'+ this.itemType +
		'",isChoice:'+ this.isChoice +'}';
}
xf.field.chilentTable.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.chilentTable.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createField('content', this.items, this.updateItems, this, formNode,null,"_itemInput");
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
	xf.createChilentSelectField('col',"_selectOptionInfo", this.items, this.itemsChange, this, formNode);
	var selectChangeText ;
	var itemsStr = this.items.split(",")[0]
	if(/\|\d+/.test(itemsStr)){
		itemsStr.replace(/\|(\d+)/g,function(a,b){
			 selectChangeText = b
		})
	}else{
		selectChangeText = 0
	}
	if(this.itemType){
		var itemTypePos = this.itemType.indexOf(",") 
	}else{
		var itemTypePos = -1;
	}
	if(itemTypePos != -1){
		tdType  = this.itemType.substr(0,itemTypePos)
	}else{
		tdType  = "0"
	}
	xf.createSelectField('TdType',{"0":"输入框","1":"时间","2":"人员选择","3":"科室选择"}, tdType, this.tdTypeupFn, this, formNode,false,"_TdType");
	xf.createSelectField('colIsType',{"0":"无限制","1":"只能输入文字","2":"只能输入正整数","3":"只能输入数字(带小数)","4":"只能输入汉字或者字母","5":"只能输入数字或者字母","6":"金钱格式","7":"只能输入手机号码","8": "只能输入固定电话","9": "输入手机或者固定电话","10": "身份证","11": "邮箱","12": "ip地址","13": "网址","14": "年龄(周天年)","15": "纯数字"}, selectChangeText, this.itemsChangeInput, this, formNode,false,"_colIsType");
	xf.createSelectField('联动选择',{"0":"无","1":"手机号","2":"科室(科室选择)","3":"身份证","4":"性别","5":"年龄","6":"工号","7":"职务","8":"职称","9":"人员(人员选择)","10":"员工类型","11":"生日","12":"人员(输入框)","13":"科室(输入框)"}, this.chilentOrder, this.chilentOrderFn, this, formNode,false,"_chilentOrder");
	if(!this.linkItem){
		this.linkItem = ""
	}
	xf.createField('联动标识', this.linkItem.split(",")[0], this.linkItemFn, this, formNode,null,"_linkItem");
	var selectPointText = "0"
	if(/\~\d+/.test(itemsStr)){
		itemsStr.replace(/\~(\d+)/g,function(a,b){
			 selectPointText = b
		})
	}
	xf.createField('decimalPoint', selectPointText, this.PointText, this, formNode,null,"_itemPoint");
	var selectWidthText = "0"
	if(/\%\d+/.test(itemsStr)){
		itemsStr.replace(/\%(\d+)/g,function(a,b){
			 selectWidthText = b
		})
	}
	xf.createField('width', selectWidthText, this.updateWidth, this, formNode,null,"_itemWidth");
}

xf.field.chilentTable.prototype.updateName = function(value) {
	this.name = value;
	this.updateItems(this.items);
}
xf.field.chilentTable.prototype.clearBorderFn  = function(value) {
	this.clearBorder = value;
	this.updateItems(this.items);
}
xf.field.chilentTable.prototype.updateRequired = function(value) {
	this.required = value;
}

xf.field.chilentTable.prototype.updateReadOnly = function(value) {
	this.readOnly = value;
}
//xf.field.chilentTable.prototype.chilentTypeFn = function(value) {
//	this.chilentType = value
//	if(value == "0"){
//		$("#_colIsType").css("display",'block').prev("label").css("display",'block')
//		$("#_selectOptionInfo").css("display",'block').prev("label").css("display",'block')
//		$("#_itemPoint").css("display",'block').prev("label").css("display",'block')
//		$("#_TdType").css("display",'block').prev("label").css("display",'block')
//		$("#_chilentOrder").css("display","none").prev("label").css("display",'none')
//	}else{
//		$("#_colIsType").css("display",'none').prev("label").css("display",'none')
//		$("#_selectOptionInfo").css("display",'none').prev("label").css("display",'none')
//		$("#_itemPoint").css("display",'none').prev("label").css("display",'none')
//		$("#_TdType").css("display",'none').prev("label").css("display",'none')
//		$("#_chilentOrder").css("display","block").prev("label").css("display",'block')
//	}
//}
xf.field.chilentTable.prototype.chilentOrderFn = function(value) {
	var typeArr = this.chilentOrder.split(",")
	typeArr[($("#_selectOptionInfo").val()-0)] = value
	this.chilentOrder = typeArr.join(',')
}

xf.field.chilentTable.prototype.itemsChange = function(value) {
	//选择行
	var itemsStr = this.items.split(",")[(value-0)]
	if(/\|\d+/g.test(itemsStr)){
		itemsStr.replace(/\|(\d+)/g,function(a,b){
			$("#_colIsType").val(b)
		})
	}else{
		$("#_colIsType").val("0")
	}
	if(/%\d+/g.test(itemsStr)){
		itemsStr.replace(/%(\d+)/g,function(a,b){
			$("#_itemWidth").val(b)
		})
	}else{
		$("#_itemWidth").val("0")
	}
	if(/~\d+/g.test(itemsStr)){
		itemsStr.replace(/~(\d+)/g,function(a,b){
			$("#_itemPoint").val(b)
		})
	}else{
		$("#_itemPoint").val("0")
	}
	if(this.itemType == null){
		this.itemType = ""
	}
	if(this.chilentOrder == null){
		this.chilentOrder = ""
	}
	if(this.linkItem == null){
		this.linkItem = ""
	}
	$("#_TdType").val(this.itemType.split(",")[(value-0)] || 0)
	$("#_chilentOrder").val(this.chilentOrder.split(",")[(value-0)] || 0)
	$("#_linkItem").val(this.linkItem.split(",")[(value-0)] || "")
}
xf.field.chilentTable.prototype.linkItemFn = function(value) {
	if(!this.linkItem){
		this.linkItem = ""
	}
	var typeArr = this.linkItem.split(",")
	typeArr[($("#_selectOptionInfo").val()-0)] = value
	this.linkItem = typeArr.join(',')
}

xf.field.chilentTable.prototype.itemsChangeInput = function(value) {
	var itemsArr = this.items.split(",")
	var itemsStr = itemsArr[($("#_selectOptionInfo").val()-0)]
	if(/\|\d+/g.test(itemsStr)){
		itemsArr[($("#_selectOptionInfo").val()-0)] = itemsStr.replace(/\|\d+/g,"") + "|" +value
	}else{
		itemsArr[($("#_selectOptionInfo").val()-0)] = itemsStr + "|" +value
	}
	this.items = itemsArr.join(',')
	$("#_itemInput").val(this.items)
}
xf.field.chilentTable.prototype.updateWidth = function(value) {
	var itemsArr = this.items.split(",")
	var itemsStr = itemsArr[($("#_selectOptionInfo").val()-0)]
	if(/%\d+/g.test(itemsStr)){
		itemsArr[($("#_selectOptionInfo").val()-0)] = itemsStr.replace(/%\d+/g,"") + "%" +value
	}else{
		itemsArr[($("#_selectOptionInfo").val()-0)] = itemsStr + "%" +value
	}
	this.items = itemsArr.join(',')
//	$("#_itemInput").val(this.items)
//	this.updateItems(this.items);
}
xf.field.chilentTable.prototype.PointText = function(value) {
	var itemsArr = this.items.split(",")
	var itemsStr = itemsArr[($("#_selectOptionInfo").val()-0)]
	if(/~\d+/g.test(itemsStr)){
		itemsArr[($("#_selectOptionInfo").val()-0)] = itemsStr.replace(/~\d+/g,"") + "~" +value
	}else{
		itemsArr[($("#_selectOptionInfo").val()-0)] = itemsStr + "~" +value
	}
	this.items = itemsArr.join(',')
	$("#_itemInput").val(this.items)
	//this.updateItems(this.items);
}
xf.field.chilentTable.prototype.tdTypeupFn = function(value) {
	var typeArr = this.itemType.split(",")
	typeArr[($("#_selectOptionInfo").val()-0)] = value
	this.itemType = typeArr.join(',')
//	console.log(this.itemType)	
	//this.updateItems(this.items);
}


xf.field.chilentTable.prototype.updateItems = function(value) {
	if(this.isChoice == null){
		this.isChoice = true
	}
	var optionArr = value.split(",")
	var str = ""
	var typeArr = []
	for(var i = 0 ;i<optionArr.length;i++){
		if(optionArr[i] != ""){
			str+="<option value = "+ i +">"+ optionArr[i].split("|")[0]+"</option>";
		}
	}
	if(this.itemType && this.itemType.replace(/,|0/g,"") == ""){
		for(var i = 0 ;i<optionArr.length;i++){
			typeArr.push(0)
		}
		this.itemType = typeArr.join(",")
	}
	if(this.itemType){
		var itemTypeNum = this.itemType.split(",")
	}else{
		var itemTypeNum = []
	}
	$("#_selectOptionInfo").html(str)
	this.items = value;
	var parentNode = xf.$(this.parentId);
	var array = this.items.split(',');
	if(!this.chilentOrder){
		this.chilentOrder = ""
	}
	var linkage = this.chilentOrder.split(',')
	if(!this.linkItem){
		this.linkItem = ""
	}
	var linkItem = this.linkItem.split(',')
	var html =
		'<div class="xf-handler chilentTableBox">' +
		'<table class="chilentTableItem" ><tr class = "falstTr">';
			for(var i = 0; i < array.length; i++) {
				var itemWidth = "";
				array[i].replace(/%(\d+)/g,function(a,b){
					itemWidth = b
				})
				html += '<td width='+itemWidth+"%"+'>'+ array[i].replace(/%\d+|\|\d+|~\d+/g,"") +'</td>';
			}
		html += '</tr><tr class = "chilentTr">'	;
			for (i = 0; i < array.length; i++) {
				if(itemTypeNum[i] == "0" || itemTypeNum[i] == null || itemTypeNum[i] == ""  ){ // 输入框
					if(/\|\d+/g.test(array[i])){
						array[i].replace(/\|(\d+)/g,function(a,b){
							html += '<td><input '+ (linkItem[i]?'linkItem='+linkItem[i]:"") +' linkage = '+ (linkage[i] || "0") +'  onchange = "'+ ChangeAll(b)+'" class= "form-control tablechange chilentDataInput" type="text"/></td>';
						})
					}else{
						html += '<td><input '+ (linkItem[i]?'linkItem='+linkItem[i]:"") +' linkage = '+ (linkage[i] || "0") +' class= "form-control tablechange chilentDataInput" type="text"/></td>';
					}
				}else if(itemTypeNum[i] == "1"){//日期
					html += '<td>'+
					'<div class=" input-group xf-handler datepicker date">' +
					'<input '+ (linkItem[i]?'linkItem='+linkItem[i]:"") +' linkage = '+ (linkage[i] || "0") +' type="text" data = {"dateType":"0"} readonly= "readonly"' +
					'style="background-color:white;cursor:default;" class="form-control rili chilentDataInput" onclick = selectTiem(this) "></div>'
					+'</td>';
				}else if(itemTypeNum[i] == "2"){//人员
					html += '<td>'+
					'<div class=" input-group xf-handler">' +
					'<input '+ (linkItem[i]?'linkItem='+linkItem[i]:"") +' linkage = '+ (linkage[i] || "0") +' id ="ChilentUser'+ this.name + i +'InputData" class = "objDataType chilentDataInput" type="hidden">'+
					'<input id = "ChilentUser'+ this.name + i +'Input" type="text" readOnly = "readOnly"  ' +
					'style="background-color:white;cursor:default;" class="form-control  userImg" onclick = "userModlueOpen(this.id,'+ this.isChoice +',\''+ (linkItem[i] || '') +'\')">' +
					'</div>'+
					'</td>';
				}else if (itemTypeNum[i] == "3"){//科室
					html += '<td>'+
					'<div class=" input-group xf-handler">' +
					'<input '+ (linkItem[i]?'linkItem='+linkItem[i]:"") +' linkage = '+ (linkage[i] || "0") +' id ="ChilentDept'+ this.name + i +'InputData" class = "objDataType chilentDataInput" type="hidden">'+
					'<input id = "ChilentDept'+ this.name + i +'Input" type="text" readOnly = "readOnly"' +
					'style="background-color:white;cursor:default;" class="form-control  DepartmentImg" onclick = "departmentModlueOpen(this.id,'+ this.isChoice +')">' +
					'</div>'+
					'</td>';
				}
//				else if(itemTypeNum[i] == "4"){//性别
//					html += '<div class="xf-handler"><select linkage = '+ (linkage[i] || "0") +' item="性别" class="form-control" style="margin-bottom:0px;width:auto;"><option value="3">未知</option><option value="1">男</option><option value="2">女</option></select></div>'
//				}
			}
		html += '</tr>';
		html += '</table>';
		html += '<a class = "addtableTrBtn" onclick="addchilentTableTr(this)" ><i class = "glyphicon glyphicon-plus"></i></a>'
		html += '<a style="display:none" class = "deltableTrBtn" onclick="delchilentTableTr(this)" ><i class = "glyphicon glyphicon-minus"></i></a>'
		html += '<input item = "'+ this.item +'" itemType = "'+ this.itemType +'"  type="hidden" class = "chilentTabletext" name = '+ this.name +'>'
		html += '</div>';
	parentNode.innerHTML = html;
	var $table = $("[name="+this.name+"]").parents(".chilentTableBox").find(".chilentTableItem")
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
	bindevenChilentTableTr($table) 
}

xf.field.chilentTable.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
}
function addchilentTableTr(el){
	var table = $(el).parents(".chilentTableBox").find(".chilentTableItem");
	var clonetr = table.find("tr:nth-child(2)").clone()
	table.append(clonetr)
	var appendlength = table.find("tr").length
	table.find("tr:last-child td input").each(function(i,v){
		v.value = ""
		v.id && (v.id = appendlength + v.id)	
	})
	bindevenChilentTableTr(table)
	ChilentTableEven(table)
	$(el).next("a").css("display","block")
}
function delchilentTableTr(el){
	var tmp = $(el).parents(".chilentTableBox").find(".chilentTableItem")
  	tmp.find("tr:last-child").remove()
  	if(tmp.find("tr").length<=2){
  		$(el).css("display","none")
  	}else{
  		$(el).css("display","block")
  	}
  	ChilentTableEven(tmp)
}
function bindevenChilentTableTr(el){
	el.find(".tablechange").off("change").on("change",function(){
		ChilentTableEven(el)
	}) 
}

function ChilentTableEven (el){
	var data = []
	var text = []
	el.find("tr").each(function(i,tr){
		if(i==0){
			$(this).find("td").each(function(i,td){
				text.push(td.innerHTML)
			}) 
		}else if (i>0){
			var tmparr = []
			$(this).find("td input.chilentDataInput").each(function(i,td){
				tmparr.push(td.value)
			})
			data.push(tmparr)
			tmparr = []
		}
	})
	var dataObj = {
		text:text,
		data:data
	}
	var strData =JSON.stringify(dataObj) 
	var dataDom = el.parents(".chilentTableBox").find(".chilentTabletext")
	dataDom.val(strData).removeClass("requi")
	if(dataObj.data.length == 1 && !dataDom.attr("disabled")) {
		var dataTmp = true
		for(var i = 0; i < dataObj.data[0].length; i++) {
			if(dataObj.data[0][i] != "") {
				dataTmp = false
				break;
			}
		}
		if(dataTmp) {
			dataDom.addClass("requi")
		}
	}
}

xf.field.userModlue = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	var userDom = $("[id^=userModlue-]") 
	if(userDom.length>0){
		var arr = []
		userDom.each(function(i,v){
			arr.push(v.id.replace(/userModlue-|InputData|Input/g,""))
		})
		this.name = 'userModlue-' + (Math.max.apply(null,arr)+1)
	}else{
		this.name = 'userModlue-0'
	}
	this.isChoice = false;
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.userModlue.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}
xf.field.userModlue.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.userModlue.prototype.doExport = function() {
	return '{"type":"userModlue","row":' + this.row +
		',"col":' + this.col +
		',"isChoice":' + this.isChoice +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}

xf.field.userModlue.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode,true);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
	formNode.appendChild(document.createElement('br'));
	xf.createBooleanField('是否单选', this.isChoice, this.updateIsChoice, this, formNode);
}

xf.field.userModlue.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">' +
		'<div class=" input-group xf-handler">' +
		'<input item = "'+ this.item +'" id ="'+ this.name +'InputData" type="hidden" class = "objDataType" name="' + this.name + '" value="' + (this.value ? this.value : '') + '">'+
		'<input item = "'+ this.item +'" id = "'+ this.name +'Input" type="text" readOnly = "readOnly"   value="' + (this.value ? userModlueValue(this.value) : '') + '" ' +
		'style="background-color:white;cursor:default;" class="form-control userImg" onclick = "userModlueOpen(this.id,'+ this.isChoice +')">' +
		'</div>'+
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}
xf.field.userModlue.prototype.updateIsChoice = function(value) {
	this.isChoice = value;
	this.updateName(this.name)
}
xf.field.userModlue.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}

xf.field.userModlue.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
	if(this.readOnly) {
		var parentNode = xf.$(this.parentId);
		parentNode.innerHTML = '<div class="xf-handler">' + value + '</div>';
	}
};

function setIinkage (id,data,link){
	window.tempData = data
	$("#"+id).parents(".chilentTr").find("input").each(function(i,v){
		var linkage = $(v).attr("linkage")
		var domlink = $(v).attr("linkItem")
		if(linkage && linkage != "0" && link==domlink){
			if(typeof data[linkage] == "string"){
				$(v).val(data[linkage]).attr("value",data[linkage])
			}else{
				$(v).val(data[linkage]).attr("value",JSON.stringify(data[linkage]))
			}
			if($(v).hasClass("objDataType")){
				var objDataTypeData = data[linkage]
				var str = ""
				for (var key in objDataTypeData) {
					str += objDataTypeData[key]
				}
				$(v).next().val(str).attr("value",str)
			}
		}
	})
}

function userModlueOpen (id,isChoice,link){
	if(!link){
		window.tempData =$("#"+id+"Data").val() 
	}
	$.dialog({
		href:$("base").attr("href")+"oa/formInfo/userModlue.action?id="+id+"&isChoice="+isChoice+"&link="+link,
		width:"1050px",
	    height: "540px",
	    title:"人员选择"
	})
}

function userModlueValue (data){
	var data = JSON.parse(data),str="";
	for (var key in data){
		str+=(data[key]+",")
	}
	if(str.length>0){
		str=str.substring(0,str.Length-1)
	}
	return str
}


xf.field.departmentModlue = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	var userDom = $("[id^=departmentModlue-]") 
	if(userDom.length>0){
		var arr = []
		userDom.each(function(i,v){
			arr.push(v.id.replace(/departmentModlue-|InputData|Input/g,""))
		})
		this.name = 'departmentModlue-' + (Math.max.apply(null,arr)+1)
	}else{
		this.name = 'departmentModlue-0'
	}
	this.isChoice = false;
	this.clearBorder = 0;
	this.item = "text"
}

xf.field.departmentModlue.prototype.render = function() {
	this.updateName(this.name);
	this.setValue('');
}

xf.field.departmentModlue.prototype.doExport = function() {
	return '{"type":"departmentModlue","row":' + this.row +
		',"col":' + this.col +
		',"isChoice":' + this.isChoice +
		',"name":"' + this.name +
		'","clearBorder":"' + this.clearBorder +
		'","item":"'+ this.item +
		'"}';
}
xf.field.departmentModlue.prototype.updateItem = function(item) {
	this.item = item;
}
xf.field.departmentModlue.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('name', this.name, this.updateName, this, formNode,true);
	xf.createField('item', this.item, this.updateItem, this, formNode);
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearBorderFn, this, formNode,true);
	xf.createBooleanField('是否单选', this.isChoice, this.updateIsChoice, this, formNode);
}

xf.field.departmentModlue.prototype.updateName = function(value) {
	this.name = value;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		'<div class="xf-handler">' +
		'<div class=" input-group xf-handler">' +
		'<input item = "'+ this.item +'" id ="'+ this.name +'InputData" class = "objDataType" type="hidden" name="' + this.name + '" value="' + (this.value ? this.value : '') + '">'+
		'<input item = "'+ this.item +'" id = "'+ this.name +'Input" type="text" readOnly = "readOnly"   value="' + (this.value ? userModlueValue(this.value) : '') + '" ' +
		'style="background-color:white;cursor:default;" class="form-control  DepartmentImg" onclick = "departmentModlueOpen(this.id,'+ this.isChoice +')">' +
		'</div>'+
		'</div>';
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
}
xf.field.departmentModlue.prototype.updateIsChoice = function(value) {
	this.isChoice = value;
	this.updateName(this.name)
}
xf.field.departmentModlue.prototype.clearBorderFn = function(clearBorder) {
	this.clearBorder = clearBorder;
	this.updateName(this.name)
}

xf.field.departmentModlue.prototype.setValue = function(value) {
	this.value = value;
	this.updateName(this.name);
};
function departmentModlueOpen (id,isChoice){
//	var data = $("#"+id+"Data").val()
	window.tempData =$("#"+id+"Data").val() 
	$.dialog({
		href:$("base").attr("href")+"oa/formInfo/departmentModlue.action?id="+id+"&isChoice="+isChoice+"",
		width:"645px",
	    height: "540px",
	    title:"科室选择"
	})
}

xf.field.LabelText = function(parentNode) {
	if(!parentNode) {
		return;
	}
	var parentId = parentNode.id;
	var array = parentId.split('-');

	this.parentId = parentId;
	this.row = array[2];
	this.col = array[3];
	this.name = 'LabelText-' + this.row + '-' + this.col;
	this.text = "纯文本";
	this.align = "center"
	this.clearBorder = 0;
	this.tag = "lable";
	this.isTitle = false;
}

xf.field.LabelText.prototype.render = function() {
	this.updateText(this.text);
}
xf.field.LabelText.prototype.doExport = function() {
	return '{"type":"LabelText","row":' + this.row +
		',"col":' + this.col +
		',"text":"' + this.text +
		'","align":"' + this.align +
		'","tag":"' + this.tag +
		'","clearBorder":"' + this.clearBorder +
		'","isTitle":'+ this.isTitle +'}';
}

xf.field.LabelText.prototype.viewForm = function(formNode) {
	formNode.innerHTML = '';
	xf.createField('text', this.text, this.updateText, this, formNode);
	xf.createSelectField('value', {"center":"居中对齐","left":"左对齐","right":"右对齐"},this.align, this.alignFn, this, formNode)
	xf.createSelectField('clearBorder',{"0":"无","1":"清除上边框","2":"清除右边框","3":"清除下边框","4":"清除左边框","5":"清除全部边框"}, this.clearBorder, this.clearborder, this, formNode,true);
	xf.createField('tag', this.tag, this.tagFn, this, formNode);
	xf.createBooleanField('是否为标题', this.isTitle, this.updateIsTitle, this, formNode);
}
xf.field.LabelText.prototype.updateIsTitle = function(value) {
	this.isTitle = value;
	this.updateName(this.name)
}
xf.field.LabelText.prototype.clearborder = function(clearborder) {
	this.clearBorder = clearborder
	this.updateText(this.text)
}
xf.field.LabelText.prototype.alignFn = function(align) {
	this.align = align
	this.updateText(this.text)
}
xf.field.LabelText.prototype.tagFn = function(tag) {
	this.tag = tag
	this.updateText(this.text)
}
xf.field.LabelText.prototype.updateText = function(text) {
	this.text = text;
	var parentNode = xf.$(this.parentId);
	parentNode.innerHTML =
		(this.isTitle ? "" : '<div class="xf-handler">') +
		(this.tag?'<'+this.tag:'</label') +
		' style="display:block;text-align:'+
		(this.align ? this.align : 'center') +
		';margin-bottom:0px;">' +
		text  +
		(this.tag?'</'+this.tag+'>':'</label>')+
		(this.isTitle ? "" : '</div>')
	if(this.clearBorder){
		parentNode.style = styleArr(this.clearBorder)
	}else{
		parentNode.style = "border-width:1px;"
	}
};
