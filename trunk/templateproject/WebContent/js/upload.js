function getPath(obj){
	if(obj){  
		if (window.navigator.userAgent.indexOf("MSIE")>=1){  
	   		obj.select(); 
	   		path=document.selection.createRange().text;
	   		document.selection.empty();
	  		return path;
	   	} 
	   	else if(window.navigator.userAgent.indexOf("Firefox")>=1){ 
			if(obj.files){  
			    return obj.files.item(0).getAsDataURL();  
	   		}  
	     	return obj.value; 
	  	} 
	  	return obj.value;  
	}  
}  

function preview(my) { 	 
    var fileName = getPath(my);
 	var position = fileName.lastIndexOf(".");
	var extName = fileName.substring(position + 1);
	var photopattern = ".jpg$|.JPG$|.gif$|.GIF$|";
	if (photopattern.indexOf(extName) < 0) 
	{
	    alert("照片只能选择jpg|gif格式的图片");  	
		return false;	
	}
   try
   {
    if (navigator.userAgent.indexOf("Firefox")>-1)
    {
    	document.getElementById('picture').src=fileName;
    }
    else
    {
    	document.getElementById("pic").innerHTML="";  
    	document.getElementById("pic").filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src =fileName;
    }
   }catch(e)
   {
       // alert("照片文件不存在,请重新选择");  	   	   
		return false;	
   }
 } 	
function cleanPhoto()
{
	try{
	    obj = document.getElementById("fileField");
		if (navigator.userAgent.indexOf("Firefox")>-1)
	    {
	    	document.getElementById('picture').src="";
	    	obj.value="";
	    }
	    else
	    {
	    	obj.select(); 
	   		document.selection.clear();
	   		document.selection.empty();
	    	document.getElementById("pic").innerHTML="";  
	    	document.getElementById("pic").filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src ="../images/line_1.gif";
	    }
	}catch(e){}
	
	var pictureObj2 = document.getElementById("photoPath");
	pictureObj2.value = "";
}
