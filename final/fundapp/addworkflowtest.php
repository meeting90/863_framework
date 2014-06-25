<html>
	<body>
		<form action="api.php?apiname=addworkflow" method = "post" enctype="multipart/form-data">
		<label for="workflow">workflow name:</label>
		<input type="text" name="workflowname"/><br/>
		<label>bpel file</label>
		<input type="file" name="bpel" id="bpel"/><br/>
		<label>involved wsdl files</label>
		<input type="text" name="referedwsdl" id="wsdllist"/>
		<br/>
		<input type="submit" name="submit" value="Submit"/>
	</form>
	</body>
</html>