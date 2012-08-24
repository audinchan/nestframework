String.prototype.trim=function() {return this.replace(/(^\s*)|(\s*$)/g,"");}
String.prototype.ltrim=function() {return this.replace(/(^\s*)/g,"");}
String.prototype.rtrim=function() {return this.replace(/(\s*$)/g,"");}
String.prototype.isInteger=function() {return /^(-|\+)?\d+$/.test(this);}
String.prototype.isPositiveInteger=function() {return /^\d+$/.test(this);}
String.prototype.isNegativeInteger=function() {return /^-\d+$/.test(this);}
String.prototype.isFloat=function(){
	if(this.isInteger())return true;
	return /^(-|\+)?\d+\.\d*$/.test(this);
}
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

String.prototype.toFloat = function() {
	return parseFloat(this);
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

$$ = function (id) {
	return document.getElementById(id);
}

function isIE() {
	return (navigator.userAgent.indexOf("MSIE") != -1);
}

function _format(pattern,num,z){   
    var j = pattern.length >= num.length ? pattern.length : num.length ;   
    var p = pattern.split("");   
    var n = num.split("");   
    var bool = true,nn = "";   
    for(var i=0;i<j;i++){   
        var x = n[n.length-j+i];   
        var y = p[p.length-j+i];   
        if( z == 0){   
            if(bool){   
                if( ( x && y && (x != "0" || y == "0")) || ( x && x != "0" && !y ) || ( y && y == "0" && !x )  ){   
                    nn += x ? x : "0";   
                    bool = false;   
                }      
            } else {   
                nn +=  x ? x : "0" ;   
            }   
        } else {   
            if( y && ( y == "0" || ( y == "#" && x ) ))   
                nn += x ? x : "0" ;                                
        }   
    }   
    return nn;   
 
}   
function _formatNumber(numChar,pattern){   
    var patterns = pattern.split(".");   
    var numChars = numChar.split(".");   
    var z = patterns[0].indexOf(",") == -1 ? -1 : patterns[0].length - patterns[0].lastIndexOf(",") ;   
    var num1 = _format(patterns[0].replace(",",""),numChars[0],0);   
    var num2 = _format( patterns[1]?patterns[1].split('').reverse().join(''):"", numChars[1]?numChars[1].split('').reverse().join(''):"",1);   
    num1 =  num1.split("").reverse().join('');   
    var reCat = eval("/[0-9]{" + (z-1) + "," + (z-1) + "}/gi");   
    var arrdata = z > -1 ? num1.match(reCat) : undefined ;   
    if( arrdata && arrdata.length > 0 ){   
        var w = num1.replace(arrdata.join(''),'');   
        num1 = arrdata.join(',') + ( w == "" ? "" : "," ) + w ;   
    }   
    num1 = num1.split("").reverse().join("");   
    return (num1 == "" ? "0" : num1) + (num2 != "" ? "." + num2.split("").reverse().join('') : "" );                       
}    
function formatNumber(num,opt){   
    var reCat = /[0#,.]{1,}/gi;   
    var zeroExc = opt.zeroExc == undefined ? true : opt.zeroExc ;   
    var pattern = opt.pattern.match(reCat)[0];   
    var numChar = num.toString();   
    var n=_formatNumber(numChar,pattern);
    if(opt.maxLength!=undefined){
    	var j = opt.maxLength >= n.length ? opt.maxLength : n.length ;
    	for(var i=n.length;i<j;i++){
    		n="&nbsp;"+n;
    	}
    }
    return !(zeroExc && numChar == 0) ? opt.pattern.replace(pattern,n) : opt.pattern.replace(pattern,"0");   
}   

function selectAll(eleObj,idname)
{
  var inputObjes = document.getElementsByTagName("input");
  if (inputObjes)
  {
   for (var i = 0; i < inputObjes.length; i++)
   {
    if (inputObjes[i].type == 'checkbox' && inputObjes[i].disabled==false && inputObjes[i].name == idname)
    {
       inputObjes[i].checked = eleObj.checked;
    }
   }
  }
}