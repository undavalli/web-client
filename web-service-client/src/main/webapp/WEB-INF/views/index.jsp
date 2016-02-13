<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Web Service Client</title>
<style type="text/css">
.simple-label {
	font-family: Arial, sans-serif;
	font-size: 12px;
	font-weight: normal;
	background-color: #f0f0f0;
}

.contentTextArea {
	width: 100%;
	height: 100%;
	resize: none;
}

.head-label {
	font-family: Arial, sans-serif;
	font-size: 12px;
	font-weight: normal;
	padding: 7px 5px;
	border-style: solid;
	border-width: 1px;
	overflow: hidden;
	word-break: normal;
	border-color: #ccc;
	background-color: #f0f0f0;
	background-image: linear-gradient(to bottom, #fafafa 2%, #eaeaea 98%);
}

.head-dropdown {
	font-family: Arial, sans-serif;
	font-size: 12px;
	font-weight: normal;
	padding: 6px 5px;
	border-style: solid;
	border-width: 1px;
	overflow: hidden;
	word-break: normal;
	border-color: #ccc;
}

.ready-only {
	background: #e7ebf2;
}
</style>
</head>
<body>
	<form:form action="${addAction}" commandName="WebServiceReq"
		method="post">
		<table>
			<tr>
				<td class='simple-label'>Select Environment &nbsp;</td>
				<td><form:select path="enviromentId"
						items="${WebServiceReq.enviromentsMap}" cssClass="head-dropdown"
						onchange="loadSrvsList()" /></td>
				<td class='simple-label'>Select Service&nbsp;</td>
				<td><form:select path="serviceId"
						items="${WebServiceReq.envServicesMap}" cssClass="head-dropdown" />&nbsp;&nbsp;
					<input type="button" value="Submit" class="head-label"
					onclick="handleSubmit()" /></td>
			</tr>
		</table>
		<table width="100%" height="90%">
			<tr height="25px">
				<td class="head-label">Request XML</td>
				<td class="head-label">Response XML</td>
			</tr>
			<tr>
				<td><form:textarea path="reqContent" cssClass="contentTextArea" />
				</td>
				<td><form:textarea path="respContent"
						cssClass="contentTextArea ready-only" readonly="true" /></td>
			</tr>
		</table>
	</form:form>
	<script type="text/javascript">
	function loadSrvsList(){
		var enviromentId = document.getElementById("enviromentId").value;
		if(enviromentId!=null && enviromentId!=""){
			document.forms[0].action = 'loadSrvs';
			document.forms[0].submit();
		}
	}
	function handleSubmit(){
		var enviromentId = document.getElementById("enviromentId").value;
		if(isEmpty(enviromentId)){
			alert("Please Select Environment");
			return ;
		}
		
		var serviceId = document.getElementById("serviceId").value;
		if(isEmpty(serviceId)){
			alert("Please Select Service");
			return ;
		}
		var reqContent = document.getElementById("reqContent").value;
		if(isEmpty(reqContent)){
			alert("Please Enter  Request Xml");
			return ;
		}
		document.forms[0].action = 'callService';
		document.forms[0].submit();
	}
	
	function isEmpty(str) {
	    return (!str || 0 === str.length);
	}
</script>
</body>
</html>