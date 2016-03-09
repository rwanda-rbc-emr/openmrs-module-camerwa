<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude
file="/moduleResources/camerwa/javaScriptPrint.js" />
<openmrs:htmlInclude
file="/moduleResources/camerwa/jquery.js" />
<openmrs:htmlInclude file="/moduleResources/camerwa/style.css" />

<openmrs:require privilege="Create Regimen" redirect="createRegimen.form" otherwise="/login.htm"/>



<script type="text/javascript">

var $ = jQuery.noConflict();

var fieldGroupCount = 0;

var drugArray = new Array();

<c:forEach var="drug" items="${arvDrugs}">
 drugArray.push("<c:out value="${drug}"/>");
</c:forEach> 

$(document).ready(function() {
	
	
	$("#createReg").click(function() {
		
		addDrug("tables",drugArray,"cssClass","<spring:message code='camerwa.regimenDrugComposition' />");

		
	});
});

</script>




<br/><br/><br/>
<a href="${pageContext.request.contextPath}/module/camerwa/camerwaLink.form" > <spring:message code="camerwa.makeAreport" /></a>

<br/>
<br/>

<h3 class='boxHeader'><spring:message code="camerwa.creationRegime" /></h3> 

<div class="box">
<form method="post" name="Form" action="createRegimen.form" onSubmit="return validateNumber()">
<table bordercolor="blue;">
  <tr>
<td> <spring:message code="camerwa.regimenCategory" /> </td>
<td> <select name="regimenCategory">
		<c:forEach items="${regiemenCategoryNames}" var="category">
			<option value="${category}" >${category}</option>
		</c:forEach>
	</select>  </td>
</tr>

<tr>
<td> <spring:message code="camerwa.regimenName" /></td>
<td> <input type="text" name="regimenName" > </td>
</tr>

<tr>
<td> <spring:message code="camerwa.regimenDrugComposition" /></td>
<td> <select name="arvDrug1">
	 <option value="nun" >-------</option>
		<c:forEach items="${arvDrugs}" var="drug">
			<option value="${drug}" >${drug}</option>
		</c:forEach>
	</select> </td>
</tr>


</table>

<div id="tables" style="cursor: pointer;">


</div>
<p id="createReg" style="cursor: pointer;">
</p>

<hr>





  
<input type="submit" value="Create" />	

</form>
<br/><br/><br/>

${regimenHasBeenCreatedMessage}
</div>

<center/>









<!-- <%@ include file="/WEB-INF/template/footer.jsp"%> -->  