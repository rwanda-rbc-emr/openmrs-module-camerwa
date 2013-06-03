<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude
file="/moduleResources/camerwa/javaScriptPrint.js" />
<openmrs:htmlInclude file="/moduleResources/camerwa/style.css" />

<openmrs:require privilege="Delete Regimen" redirect="deleteRegimen.form" otherwise="/login.htm"/>
<script LANGUAGE="JavaScript">
//<!--
// Nannette Thacker http://www.shiningstar.net
function confirmSubmit()
{
var agree=confirm("Are you sure you wish to delete selected Regimen?");
if (agree)
	return true ;
else
	return false ;
}
// -->
</script>
<br/><br/><br/>
<a href="camerwaLink.form" >Make a Report</a>

<center>
<br/><br/>
<h3 class='boxHeader'> Delete Regimen</h3>

<div class="box">

<form method="post" name="Form" action="deleteRegimen.form" onSubmit="return confirmSubmit()">
<table bordercolor="blue;">
<tr>
<td> Regimen Category Location : </td>
<td> <select name="regimenCategory">
		<c:forEach items="${regiemenCategoryNames}" var="category">
			<option value="${category}" >${category}</option>
		</c:forEach>
	</select>  </td>
</tr>

<tr>
<td> Regimen Name </td>
<td> <select name="regimenName">
	
		<c:forEach items="${regimenNames}" var="regimenName">
			<option value="${regimenName}" >${regimenName}</option>
		</c:forEach>
	</select> </td>
</tr>

</table>


  
<input type="submit" value="Delete" />	

</form>
<br/><br/><br/>

${deleteSuccessMsg}

</div>
<center/>

<!-- <%@ include file="/WEB-INF/template/footer.jsp"%> -->  