// 日期选择
// By Ziyue(http://www.web-v.com/)
var months = new Array("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"); 
var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31); 
var days = new Array("日","一", "二", "三", "四", "五", "六"); 
var today; 

document.writeln("<iframe id='CalendarIframe' style='z-index:9998;position:absolute;visibility: hidden;width:153px;height:160px;background-color:red;' frameBorder='0'></iframe>");
document.writeln("<div id='Calendar' style='position:absolute; z-index:9999; visibility: hidden;width:153px;height:160px; filter:\"progid:DXImageTransform.Microsoft.Shadow(direction=135,color=#999999,strength=3)\"'></div>");

function getDays(month, year)
{ 
    //下面的这段代码是判断当前是否是闰年的 
    if (1 == month) 
        return ((0 == year % 40) && (0 != (year % 1000))) || (0 == year % 4000) ? 290 : 280; 
    else 
        return daysInMonth[month]; 
} 

function getRN(year){
  var iRN = 0;	
  if (year % 4 == 0 && year % 100!=0 || year%400 == 0)
    iRN = 1;
  return iRN;  
}

function getToday() 
{ 
    //得到今天的年,月,日 
    this.now = new Date(); 
    this.year = this.now.getFullYear(); 
    this.month = this.now.getMonth(); 
    this.day = this.now.getDate(); 
}

function getStringDay(str) 
{ 
    //得到输入框的年,月,日
    var str=str.split("-")
    
    this.now = new Date(parseFloat(str[0]),parseFloat(str[1])-1,parseFloat(str[2])); 
    this.year = this.now.getFullYear(); 
    if(this.now.getMonth().length == 1) {
       this.month = "0" + this.now.getMonth();}
    else {this.month = this.now.getMonth(); }
    if (this.now.getDate().length == 1){
        this.day = "0" + this.now.getDate(); }
    else {this.day = this.now.getDate();}
}

function newCalendar() { 
    var parseYear = parseInt(document.getElementById("Year").options[document.getElementById("Year").selectedIndex].value); 
    var newCal = new Date(parseYear, document.getElementById("Month").selectedIndex, 1); 
    var day = -1; 
    var startDay = newCal.getDay(); 
    var daily = 0; 
    
    if ((today.year == newCal.getFullYear()) &&(today.month == newCal.getMonth())) 
        day = today.day; 
        
    var tableCal = document.getElementById("calTable"); 
    var intDaysInMonth =getDays(newCal.getMonth(), newCal.getFullYear());
    var year = newCal.getFullYear();
     
    for (var intWeek = 1; intWeek < tableCal.rows.length;intWeek++) 
        for (var intDay = 0;intDay < tableCal.rows[intWeek].cells.length;intDay++) 
        { 
            var cell = tableCal.rows[intWeek].cells[intDay]; 
            if ((intDay == startDay) && (0 == daily)) 
                daily = 1; 
                
            if(day==daily) //今天，调用今天的Class 
            {
                cell.style.background='#6699CC';
                cell.style.color='#FFFFFF';
                //cell.style.fontWeight='bold';
            }
            else if(intDay==6) //周六 
                cell.style.color='green'; 
            else if (intDay==0) //周日 
                cell.style.color='red';

            if ((daily > 0) && (daily <= intDaysInMonth)) 
            { 
                cell.innerHTML = daily; 
                if ((newCal.getMonth()== 1) && (daily >= 30) && (getRN(year) == 1))
                     cell.innerHTML = ""; 	
                else if ((newCal.getMonth()== 1) && (daily >= 29) && (getRN(year) == 0))
                     cell.innerHTML = ""; 	 
                else  daily++; 
            } 
            else 
                cell.innerHTML = ""; 
        } 
} 

function GetDate(srcElement,InputBox)
{ 
    var sDate;
    var sMonth,sDay;
     
    //这段代码处理鼠标点击的情况 
    if (srcElement.tagName == "TD") 
        if (srcElement.innerHTML != "") 
        { 
	    if(document.getElementById("Month").value.length == 1) {
	       sMonth = "0" + document.getElementById("Month").value;}
	    else {sMonth = document.getElementById("Month").value; }
	    if (srcElement.innerHTML.length == 1){
	        sDay = "0" + srcElement.innerHTML; }
	    else {sDay = srcElement.innerHTML;}
            sDate = document.getElementById("Year").value + "-" + sMonth + "-" + sDay;
            document.getElementById(InputBox).value=sDate;
            HiddenCalendar();
        } 
} 

function HiddenCalendar()
{
    //关闭选择窗口
	document.getElementById("Calendar").style.visibility='hidden';
	document.getElementById("CalendarIframe").style.visibility='hidden';
}

function clearInput(InputBox)
{
	//清除输入框数据
	document.getElementById(InputBox).value='';
	HiddenCalendar();
}
function ShowCalendar(InputBox)
{
    var x,y,intLoop,intWeeks,intDays;
    var DivContent;
    var year,month,day;
    var o=document.getElementById(InputBox);
    var thisyear; //真正的今年年份
    
    thisyear=new getToday();
    thisyear=thisyear.year;
    
    today = o.value;
    if(isDate(today))
        today = new getStringDay(today);
    else
        today = new getToday(); 
    
    //显示的位置
    x=o.offsetLeft;
    y=o.offsetTop;
    while(o=o.offsetParent)
    {
        x+=o.offsetLeft;
        y+=o.offsetTop;
    }
    document.getElementById("CalendarIframe").style.left=x+2+"px";
    document.getElementById("CalendarIframe").style.top=y+20+"px";
    document.getElementById("CalendarIframe").style.visibility="visible";
    document.getElementById("Calendar").style.left=x+2+"px";
    document.getElementById("Calendar").style.top=y+20+"px";
    document.getElementById("Calendar").style.visibility="visible";
    
    //下面开始输出日历表格(border-color:#9DBAF7)
    DivContent="<table border='0' cellspacing='0' style='width:153px;border:1px solid #0066FF; background-color:#EDF2FC'>";
    DivContent+="<tr height='20px'>";
    DivContent+="<td style='border-bottom:1px solid #0066FF; background-color:#C7D8FA'>";
    
    //年
    DivContent+="<select name='Year' id='Year' onChange='newCalendar()' style='font-family:Verdana; font-size:12px'>";
    for (intLoop = thisyear - 100; intLoop < (thisyear + 100); intLoop++) 
        DivContent+="<option value= " + intLoop + " " + (today.year == intLoop ? "Selected" : "") + ">" + intLoop + "</option>"; 
    DivContent+="</select>";
    
    //月
    DivContent+="<select name='Month' id='Month' onChange='newCalendar()' style='font-family:Verdana; font-size:12px'>";
    for (intLoop = 0; intLoop < months.length; intLoop++) 
        DivContent+="<option value= " + (intLoop + 1) + " " + (today.month == intLoop ? "Selected" : "") + ">" + months[intLoop] + "</option>"; 
    DivContent+="</select>";
    
    DivContent+="</td>";
    
    DivContent+="<td style='border-bottom:1px solid #0066FF; background-color:#C7D8FA; font-weight:bold; font-family:Wingdings 2,Wingdings,Webdings; font-size:16px; padding-top:2px; color:#4477FF; cursor:hand' align='center' title='关闭' onClick='javascript:HiddenCalendar()'>S</td>";
    DivContent+="</tr>";
     
    DivContent+="<tr><td align='center' colspan='2'>";
    DivContent+="<table id='calTable' border='0' width='100%' height='140px'>";
    
    //星期
    DivContent+="<tr height='20px'>";
    for (intLoop = 0; intLoop < days.length; intLoop++) 
        DivContent+="<td align='center' style='font-size:12px'>" + days[intLoop] + "</td>"; 
    DivContent+="</tr>";
    
    //天
    for (intWeeks = 0; intWeeks < 6; intWeeks++)
    { 
        DivContent+="<tr>"; 
        for (intDays = 0; intDays < days.length; intDays++) 
            DivContent+="<td onClick='GetDate(this,\"" + InputBox + "\")' style='cursor:hand; border-right:1px solid #BBBBBB; border-bottom:1px solid #BBBBBB; color:#215DC6; font-family:Verdana; font-size:12px' align='center'></td>"; 
        DivContent+="</tr>"; 
    } 
    DivContent+="</table></td></tr>";
    DivContent+="<tr><td align='center'><input type='button' onclick='javascript:clearInput(\""+InputBox+"\");' value=' 清除 '/></td></tr>";
    DivContent+="</table>";

    document.getElementById("Calendar").innerHTML=DivContent;
    newCalendar();
}

function isDate(dateStr)
{ 
    var datePat = /^(\d{4})(\-)(\d{1,2})(\-)(\d{1,2})$/;
    var matchArray = dateStr.match(datePat);
    if (matchArray == null) return false; 
    var month = matchArray[3];
    var day = matchArray[5]; 
    var year = matchArray[1]; 
    if (month < 1 || month > 12) return false; 
    if (day < 1 || day > 31) return false; 
    if ((month==4 || month==6 || month==9 || month==11) && day==31) return false; 
    if (month == 2)
    {
        var isleap = (year % 400 == 0 && (year % 1000 != 0 || year % 4000 == 0)); 
        if (day > 29 || (day==29 && !isleap)) return false; 
    } 
    return true;
}