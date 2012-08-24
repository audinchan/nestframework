//判断日期格式是否正确
function checkDate(dateStr,pattern){
	if(dateStr=="" || dateStr.trim()=="" || pattern=="")
		return true;
	CheckDateUtil.checkDateFormat(dateStr,pattern,function(obj){
		if(!obj){
			alert("日期格式不正确，应为"+pattern);
			return false;
		}
	});
	return true;
}
//判断是否有选项
function checkHaveSelect(frm,type,name)
{
	var flag = 0;
	for (i = 0; i < frm.elements.length; i++)
	{
		if (frm.elements[i].type == type
			&& frm.elements[i].name == name
			&& frm.elements[i].checked == true)
		{
			flag ++;
		}
	}
	return flag;
}

function checkFields(objs){
	for(var i=0;i<objs.length;i++){
		var obj=objs[i];
		if(obj.type=='checkbox'){
			//判断复选框
			if(checkHaveSelect(obj.frm,'checkbox',obj.name)==0){
				alert("请选择"+obj.text);
				o.focus();
				return false;
			}
			continue;
		}
		if(obj.type=="radio"){
			//判断单选框
			if(checkHaveSelect(obj.frm,'radio',obj.name)==0){
				alert("请选择"+obj.text);
				o.focus();
				return false;
			}
			continue;
		}
		
		var o=$$(obj.id);
		if(!o){
			continue;
		}
		if(o.value.isEmpty() && obj.allowBlank==true){
			continue;
		}
		//判断是否为空
		if(o.value.isEmpty() && (!obj.allowBlank || obj.allowBlank==false)){
			alert(obj.text+"不能为空");
			o.focus();
			return false;
		}
		//判断长度
		if(obj.maxLength && o.value.trim().charLen()>obj.maxLength){
			alert(obj.text+"长度不能大于"+obj.maxLength+"个字符");
			o.focus();
			return false;
		}
		if(obj.minLength && o.value.trim().charLen()<obj.minLength){
			alert(obj.text+"长度不能小于"+obj.minLength+"个字符");
			o.focus();
			return false;
		}
		if(obj.length && o.value.trim().charLen()!=obj.length){
			alert(obj.text+"应为"+obj.length+"个字符");
			o.focus();
			return false;
		}
		
		//判断类型
		if(obj.type=='date'){
			var p=obj.pattern;
			if(!checkDate(o.value.trim(),p)){
				o.focus();
				return false;
			}
		}else if(obj.type=='int'){
			if(!o.value.trim().isInteger()){
				alert(obj.text+"应为整数，请重新输入！");
				o.focus();
				return false;
			}
			if((obj.minValue || obj.minValue==0) && o.value.trim().toInt()<obj.minValue){
				alert(obj.text+"不能小于"+obj.minValue);
				o.focus();
				return false;
			}
			if((obj.maxValue || obj.maxValue==0) && o.value.trim().toInt()>obj.maxValue){
				alert(obj.text+"不能大于"+obj.maxValue);
				o.focus();
				return false;
			}
		}else if(obj.type=='float'){
			if(!o.value.trim().isFloat()){
				alert(obj.text+"应为数字，请重新输入！");
				o.focus();
				return false;
			}
			if((obj.minValue || obj.minValue==0) && o.value.trim().toFloat()<obj.minValue){
				alert(obj.text+"不能小于"+obj.minValue);
				o.focus();
				return false;
			}
			if((obj.maxValue || obj.maxValue==0) && o.value.trim().toFloat()>obj.maxValue){
				alert(obj.text+"不能大于"+obj.maxValue);
				o.focus();
				return false;
			}
		}else if(obj.type=="char_num" || obj.type=="num_char"){
			if(o.value.trim().onlyCharNumber()){
				alert(obj.text+"应为数字或字母，请重新输入！")
				o.focus();
				return false;
			}
		}else if(obj.type=="char"){
			if(o.value.trim().onlyChar()){
				alert(obj.text+"应为字母，请重新输入！")
				o.focus();
				return false;
			}
		}
		//和其他字段比较
		if(obj.compareTo){
			var otherObj=$$(obj.compareTo);
			//查找另一个字段的名称
			if(otherObj){
				if((obj.compareType=='eq'||obj.compareType=='==') && o.value.trim()!=otherObj.value.trim()){
					alert(obj.text+"与"+getTextById(objs,obj.compareTo)+"不相同，请重新输入");
					o.focus();
					return false;
				}
				if((obj.compareType=='ne'||obj.compareType=='!=') && o.value.trim()==otherObj.value.trim()){
					alert(obj.text+"不能与"+getTextById(objs,obj.compareTo)+"相同，请重新输入");
					o.focus();
					return false;
				}
				if((obj.compareType=='lt'||obj.compareType=='<') && o.value.trim()>=otherObj.value.trim()){
					alert(obj.text+"应小于"+getTextById(objs,obj.compareTo)+"，请重新输入");
					o.focus();
					return false;
				}
				if((obj.compareType=='gt'||obj.compareType=='>') && o.value.trim()<=otherObj.value.trim()){
					alert(obj.text+"应大于"+getTextById(objs,obj.compareTo)+"，请重新输入");
					o.focus();
					return false;
				}
				if((obj.compareType=='le'||obj.compareType=='<=') && o.value.trim()>otherObj.value.trim()){
					alert(obj.text+"应小于等于"+getTextById(objs,obj.compareTo)+"，请重新输入");
					o.focus();
					return false;
				}
				if((obj.compareType=='ge'||obj.compareType=='>=') && o.value.trim()<otherObj.value.trim()){
					alert(obj.text+"应大于等于"+getTextById(objs,obj.compareTo)+"，请重新输入");
					o.focus();
					return false;
				}
			}
		}
	}	
	return true;
}
//返回oId对应的名称
function getTextById(objs,oId){
	for(var i=0;i<objs.length;i++){
		var obj=objs[i];
		if(obj.id==oId){
			return obj.text;
		}
	}
	return "";
}
