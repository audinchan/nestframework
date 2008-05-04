String.prototype.trim=function() {return this.replace(/(^\s*)|(\s*$)/g,"");}
String.prototype.ltrim=function() {return this.replace(/(^\s*)/g,"");}
String.prototype.rtrim=function() {return this.replace(/(\s*$)/g,"");}
String.prototype.isInteger=function() {return /^(-|\+)?\d+$/.test(this);}
String.prototype.isPositiveInteger=function() {return /^\d+$/.test(this);}
String.prototype.isNegativeInteger=function() {return /^-\d+$/.test(this);}
//   date (13:04:06)
String.prototype.isTime=function() {
	var a = this.match(/^(\d{1,2})(:)?(\d{1,2})\2(\d{1,2})$/);
	if (a == null) return false;
	if (a[1]>24 || a[3]>60 || a[4]>60) return false;
	return true;
}
//   short date (13:04)
String.prototype.isShortTime=function() {
	var a = this.match(/^(\d{1,2})(:)?(\d{1,2})$/);
	if (a == null) return false;
	if (a[1]>24 || a[3]>60) return false;
	return true;
}
//   date (2003-12-05)
String.prototype.isDate=function() {
         var r = this.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
         if(r==null)return false;
         var d= new Date(r[1], r[3]-1, r[4]);
         return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}
//   short date (2003-12)
String.prototype.isShortDate=function() {
         var r = this.match(/^(\d{1,4})(-|\/)(\d{1,2})$/);
         if(r==null)return false;
         var d= new Date(r[1], r[3]-1, 1);
         return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]);
}
//   date (2003-12-05 13:04:06)
String.prototype.isDateTime=function() {
        var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
        var r = this.match(reg);
        if(r==null)return false;
        var d= new Date(r[1], r[3]-1,r[4],r[5],r[6],r[7]);
        return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]&&d.getHours()==r[5]&&d.getMinutes()==r[6]&&d.getSeconds()==r[7]);
}

String.prototype.onlyChar=function() {
	return /[^a-zA-Z]/g.test(this);
}

String.prototype.onlyCharNumber=function() {
	return /[^0-9a-zA-Z]/g.test(this);
}

//   char, number, underline dot CharNumberUnderlineDot
String.prototype.onlyCNUD=function() {
	return /^([a-zA-z_]{1})([\w]*)$/g.test(this);
}

//   mail
String.prototype.isEmail=function() {
	return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this);
}

//zip
String.prototype.isZipCode = function()
{
return /^\d{6}$/.test(this);
}

//   hanzi
String.prototype.existChinese = function() {
	return /^[\x00-\xff]*$/.test(this);
}

//   to int
String.prototype.toInt = function() {
	return parseInt(this);
}

//   char length
String.prototype.charLen = function() {
	var length = 0;
	for (var i = 0; i < this.length; i++) {
		if (this.charCodeAt(i) > 10000) {
			length++;
		}
		length++;
	}
	return length;
}

String.prototype.isEmpty = function() {
	return this.trim().length == 0;
}

String.prototype.isNotEmpty = function() {
	return !this.isEmpty();
}


$ = function (id) {
	return document.getElementById(id);
}

function isIE() {
	return (navigator.userAgent.indexOf("MSIE") != -1);
}
