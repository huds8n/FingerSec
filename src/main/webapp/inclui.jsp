<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Oracle RMS Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<h3>Please Log In</h3>
<body>
	<script>

    var _app = navigator.appName;
    var index = 0;

readyimg=new Image();
readyimg.src = "resources/images/waitingleftindex.gif";

disconnectedimg=new Image();
disconnectedimg.src = "resources/images/uodisconnected.gif";

fingertouchimg=new Image();
fingertouchimg.src = "resources/images/touched.gif";

datacapturedimg = new Image();
datacapturedimg.src = "resources/images/datacaptured2.gif";

fingergoneimg=new Image();
fingergoneimg.src="resources/images/waitingleftindex.gif";

function onFeaturesAcquiredHandler() {
    document.uareulogo.src = datacapturedimg.src;
    //document.registerForm.submit();
	}

	function onTemplateHandler() {
	    if (_app == "Microsoft Internet Explorer") {
	        if (index == 0) {
	            document.registerForm.htbTemplate.value = document.applets["regAppletIE"].getData();
	            readyimg.src = "resources/images/waitingrightindex.gif";
	            fingergoneimg.src = readyimg.src;
	            document.uareulogo.src = readyimg.src;
	            document.getElementById("instruction").innerHTML = "Press your right index finger 4 times firmly to the sensor.<br>";
	            ++index;
	            document.registerForm.htbTemplateIndex.value = "4";
	        }
	        else {
	            document.registerForm.htbTemplate2.value = document.applets["regAppletIE"].getData();
	            document.applets["regAppletIE"].stopCapture();
	            document.getElementById("instruction").innerHTML = "";
	            document.getElementById("imgHolder").innerHTML = "";
	            index = 0;
	            document.registerForm.htbTemplateIndex2.value = "7";
	        }
	        
	    }
	    else if (_app == "Netscape") {
	        if (index == 0) {
	            document.registerForm.htbTemplate.value = document.regAppletFF.getData();
	            readyimg.src = "resources/images/waitingrightindex.gif";
	            fingergoneimg.src = readyimg.src;
	            document.uareulogo.src = readyimg.src;
	            document.getElementById("instruction").innerHTML = "Press your right index finger 4 times firmly to the sensor.<br>";
	            ++index;
	            document.registerForm.htbTemplateIndex.value = "4";
	        }
	        else {
	            document.registerForm.htbTemplate2.value = document.regAppletFF.getData();
	            document.regAppletFF.stopCapture();
	            document.getElementById("instruction").innerHTML = "";
	            document.getElementById("imgHolder").innerHTML = "";
	            index = 0;
	            document.registerForm.htbTemplateIndex2.value = "7";
	        }
	    }
	}

function onDevConnectedHandler() {
	document.uareulogo.src=readyimg.src;
}

function onDevDisconnectedHandler()
{
	document.uareulogo.src=disconnectedimg.src;
}

function onFingerTouchedHandler()
{
	document.uareulogo.src=fingertouchimg.src;
}

function onFingerGoneHandler() {
 	document.uareulogo.src=fingergoneimg.src;
}

function onBadPressHandler() {
    alert("Bad press, try again.");
}

function onErrorHandler()
{
	alert("Error reported from applet");
}

</script>

	<form action="${pageContext.request.contextPath}/Inclui" method="post"
		name="registerForm" id="registerForm">

		<table width="440" height="120" border="1" rules=NONE frame=box>
			<tr>
				<td width="60%"><br>&nbsp;User Name:</td>
				<td>&nbsp;<input name="UserName" type="text" value=""
					maxlength="32"></td>
				<td></td>
				<td></td>
			</tr>

			<tr>
				<td>&nbsp;Password:</td>
				<td>&nbsp;<input name="Password" type="text" value=""
					maxlength="16"></td>
			</tr>
			<tr>

				<td><input type="checkbox" name="startreg" id="startreg"
					value="ON" onclick="handleClick()"> Register Fingerprint</td>
			</tr>
		</table>

		<br>
		<div id="instruction"></div>
		<div id="imgHolder"></div>
		<div id="bioHolder"></div>
		<br> <input name="btnSubmit" type="Submit" value="Submit">
		<input name="htbTemplate" type="text" readonly="true"> <input
			name="htbTemplateIndex" id="htbTemplateIndex" type="text" value=""
			style="width: 20px" /> <input name="htbTemplate2" type="text"
			readonly="true"> <input name="htbTemplateIndex2"
			id="htbTemplateIndex2" type="text" value="" style="width: 20px" />

	</form>
	<script>
function handleClick() {


    var _app = navigator.appName;
    readyimg.src = "resources/images/waitingleftindex.gif";


    if (document.getElementById("startreg").checked == false) {
        //clear innerHTML
        document.getElementById("bioHolder").innerHTML = "";
        document.getElementById("instruction").innerHTML = "";
        document.getElementById("imgHolder").innerHTML = "";
        return;
    }

    document.getElementById("instruction").innerHTML = "Press your left index finger 4 times firmly to the sensor.<br>";
    document.getElementById("imgHolder").innerHTML = '<img id="uareulogo" name="uareulogo" src="resources/images/uoenabled.gif" border="0" alt="PLEASE PLACE YOUR FINGER ON THE SENSOR!" align="left"><br><br><br>';

    if (_app == "Microsoft Internet Explorer") {

        
        
        var objectString = new String();
        
        objectString ='<object classid=clsid:8AD9C840-044E-11D1-B3E9-00805F499D93';
        objectString = objectString.concat('\ncodebase="http://java.sun.com/products/plugin/autodl/jinstall-1_6_0_20-windows-i586.cab#Version=1,4,0,0"',
        'width="0"',
        'height="0"',
        'name="regAppletIE">',
        '<param name="code" value="applettest.Main"/>',
        '<param name="scriptable" value="true"/>',
        '<param name="separate_jvm" value="true"/>',
        '<param name="archive" value="http://localhost/Teste1/RegisterNonGUIApplet.jar"/>',
        '<param name="onDevConnectedScript" value="onDevConnectedHandler"/>',
        '<param name="onDevDisconnectedScript" value="onDevDisconnectedHandler"/>',
        '<param name="onFeaturesAcquiredScript" value="onFeaturesAcquiredHandler"/>',
        '<param name="onFingerTouchedScript" value="onFingerTouchedHandler"/>',
        '<param name="onFingerGoneScript" value="onFingerGoneHandler"/>',
        '<param name="onErrorScript" value="onErrorHandler"/>',
        '<param name="onTemplateAcquiredScript" value="onTemplateHandler" />',
        '<param name="onBadPressScript" value="onBadPressHandler" />',
        '<param name="purpose" value="enrollment" />',
        '<param name="priority" value="normal" />',
        '</object>');
        
        document.getElementById("bioHolder").innerHTML = objectString;
    
    }
    else if (_app == "Netscape") {
 
        var objectString = new String();

        objectString = '<embed code="applettest.Main"';
        objectString = objectString.concat('name="regAppletFF"',            
                   'width="0"',
                   'height="0"',
                   'type="application/x-java-applet"',
                   'pluginspage="http://java.sun.com/javase/downloads"',
                   'code="applettest.Main"',
                   'archive="http://localhost/Teste1/RegisterNonGuiApplet.jar"',
                   'onDevConnectedScript="onDevConnectedHandler"',
                   'onDevDisconnectedScript="onDevDisconnectedHandler"',
                   'onFeaturesAcquiredScript="onFeaturesAcquiredHandler"',
                   'onFingerTouchedScript="onFingerTouchedHandler"',
                   'onFingerGoneScript="onFingerGoneHandler"',
                   'onErrorScript="onErrorHandler"',
                   'onTemplateAcquiredScript="onTemplateHandler"',
                   'onBadPressScript="onBadPressHandler"',
                   'purpose="enrollment"',
                   'priority="low"',
                   'scriptable="true"',
                   'mayscript="true"/>');

        document.getElementById("bioHolder").innerHTML = objectString;
    }
    else {
            //Browser not FF or IE, use Applet tag for highest compatibility
    }

    
}

</script>

</body>
</html>