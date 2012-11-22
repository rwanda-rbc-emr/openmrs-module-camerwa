<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>



<openmrs:htmlInclude file="/moduleResources/camerwa/jquery.js" />
<openmrs:htmlInclude
	file="/moduleResources/camerwa/jquery.dataTables.js" />
<openmrs:htmlInclude file="/moduleResoures/camerwa/demo_page.css" />
<openmrs:htmlInclude file="/moduleResources/camerwa/demo_table.css" />

<openmrs:require privilege="View ARV Regimens monthly report" redirect="patientList.form" otherwise="/login.htm"/>

<script type="text/javascript" charset="utf-8">
	$(document).ready( function() {
		$('#example').dataTable( {
			"sPaginationType" :"full_numbers"
		});
	});
</script>



<a href="${pageContext.request.contextPath}/admin/index.htm">Admin</a><openmrs:hasPrivilege privilege="Export Collective Patient Data">
|
<a
	href="downloadController.form?id=2&KeyPatinents=${KeyPatinents}&regimenName=${regimenName}&year=${year}&amp;month=${month}">Export</a> </openmrs:hasPrivilege> <!--  <font size="2" face="arial" color="red">   ${exportPrivilegeMessage} </font> -->

<br />
<b><spring:message code="PATIENT LIST" arguments="65" /></b>

<div><c:choose>

	<c:when test="${empty thePatientAndRegimenList}">
		<h2>There is no patient matching your createria !</h2>
	</c:when>
	<c:otherwise>


		<div id="dt_example">
		<div id="container">
		
		 <table cellpadding="0" cellspacing="0" border="0" class="display"
			id="example">
			<thead>
				<tr>
					<th>Identifier</th>
					<openmrs:hasPrivilege privilege="View Patient Names">
					<th>Given Name</th>
					<th>Family Name</th>
					</openmrs:hasPrivilege>
					<th>Age</th>
					<th>Gender</th>					
					<th>Last Encouter Date</th>
					<th>Return Visit Date</th>
					<th>Drugs</th>
					<th>Arv start Date</th>
					<th>Edit</th>
				</tr>
			</thead>
			
			<tbody>
			<c:forEach var="patientAndRegimen"
				items="${thePatientAndRegimenList}" varStatus="status">

				<tr>
					<td>${patientAndRegimen[0].patientIdentifier}</td>
					<openmrs:hasPrivilege privilege="View Patient Names">
					<td>${patientAndRegimen[0].givenName}</td>
					<td>${patientAndRegimen[0].familyName}</td>
					</openmrs:hasPrivilege>
					<td>${patientAndRegimen[0].age}</td>
					<td><img
						src="${pageContext.request.contextPath}/images/${patientAndRegimen[0].gender == 'M' ? 'male' : 'female'}.gif" /></td>			
					<td>${patientAndRegimen[2]}</td>
					<td>${patientAndRegimen[3]}</td>	
					<td>${patientAndRegimen[1]}</td>
					<td>${patientAndRegimen[4]}</td>
					<td><a
						href="${pageContext.request.contextPath}/patientDashboard.form?patientId=${patientAndRegimen[0].patientId}"><img
						src="${pageContext.request.contextPath}/images/edit.gif"
						title="Edit" border="0" align="top" /></a></td>
				</tr>
				<c:if test="${status.last}">
					<h4>There is <b style="color: blue;">${status.count }</b>
					patients</h4>
				</c:if>

			</c:forEach>
			</tbody>
			
			

		</table>
		
		</div>
		</div>

		
		</div>
	</c:otherwise>
</c:choose> <%@ include file="/WEB-INF/template/footer.jsp"%>