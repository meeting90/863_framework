<html>
	<body>
		<form action="api.php?apiname=addservice" method = "post" enctype="multipart/form-data">
		<label for="service">service name:</label>
		<input type="text" name="service_name"/><br/>
		<input type="file" name="wsdl" id="wsdl"/>
		<br/>
		<input type="submit" name="submit" value="Submit"/>
	</form>
	</body>
</html>