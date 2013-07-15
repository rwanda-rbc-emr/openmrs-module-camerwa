<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude
file="/moduleResources/camerwa/javaScriptPrint.js" />
<openmrs:htmlInclude file="/moduleResources/camerwa/style.css" />

  
  <openmrs:require privilege="View ARV Regimens monthly report" redirect="/module/camerwa/camerwaLink.form" otherwise="/login.htm"/>  
 



<h3 class='boxHeader'><spring:message code="camerwa.title" /></h3>     
<div class="box">
	<form method="post" name="Form" action="camerwaLink.form" onSubmit="return validateNumber()">
		<spring:message code="camerwa.Year" /> <input type="text" name="year" value=${year}> <br/>
	<spring:message code="camerwa.Month" />
	<select name="month">
		<c:forEach items="1,2,3,4,5,6,7,8,9,10,11,12" var="mon">
			<option value="${mon}" ${month == mon ? 'selected="selected"' : ''}>${mon}</option>
		</c:forEach>
	</select> <br/>
	<spring:message code="camerwa.Location" /> : 
	<select name="oneLocation">
		<c:forEach items="${locations}" var="location">
			<option value="${location}" ${oneLocation == location ? 'selected="selected"' : 'TRAC Plus'} >${location}</option>
		</c:forEach>
	</select>
<br />  

<input type="submit" value="submit" /></form>
 
</div> 
<div id="display" > 
<br />    <c:if test="${fn:length(patientsUnderDrug)>0}">  
  <h2>${selectedLocaion}</h2><br/>
<h4><spring:message code="camerwa.moisRaporter" /> :
<b> ${month}/${year}</b> </h4>  <a href="javascript:void(printSpecial())"><spring:message code="camerwa.print" /></a> | <a href="camerwaLink.form?exportInToExcel=export&year=${year}&month=${month}&oneLocation=${selectedLocaion}" ><spring:message code="camerwa.exportToExcel" /></a> &nbsp;&nbsp;&nbsp;
<a href="patientWithoutDrug.form?exportInToExcel=export&year=${year}&month=${month}&oneLocation=${selectedLocaion}" ><spring:message code="camerwa.patientsWithoutRegimens" /></a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="patientList.form?year=${year}&amp;month=${month}&amp;KeyPatinents=patientsWithoutRegimens" > <b style="color: red;"> ${patientsWithoutRegimens} </b></a> <openmrs:hasPrivilege privilege="Create Regimen">| &nbsp;&nbsp;&nbsp;

<a href="createRegimen.form" ><spring:message code="camerwa.creationRegime" /></a></openmrs:hasPrivilege>&nbsp;&nbsp;&nbsp;&nbsp; <openmrs:hasPrivilege privilege="Delete Regimen">| &nbsp;&nbsp;&nbsp;&nbsp; <a href="deleteRegimen.form" ><spring:message code="camerwa.deleteRegime" /></a></openmrs:hasPrivilege>&nbsp;&nbsp;&nbsp;&nbsp;

<br /> 
<br /> 


<div>
<table border="1" align="left" height="15%" width="48%">
	<tr class="tableRaw" > 
		<td >INFORAMTION GENERAL</td>
		<td >Anciens</td>
		<td >Nouveau</td>
		<td >Total</td>
	</tr>
	<tr>
		<td class="categories">Nombre de patients sous ARV</td>
		<td><a href="patientList.form?year=${year}&amp;month=${month}&amp;KeyPatinents=LastPatientsUnderARV">
		${LastPatientsUnderARV}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsUnderArvNew">${patientsUnderARVNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPatientsUnderARV">${allPatientsUnderARV}</a></td>
	</tr>
	<tr>
		<td class="categories">Nombre d'adultes sous ARV</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=LastAdultsUnderARV">${LastAdultsUnderARV}
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultsUnderARVNew">${adultsUnderARVNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdultsUnderARV">${allAdultDOBQuery}</a></td>
	</tr>
	<tr>
		<td class="categories">Nombre d'enfants(<=15) sous ARV</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=LastkidsDOBQuery">${LastkidsDOBQuery}</a>
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=kidsDOBQueryNew">${kidsDOBQueryNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalKidsDOBQuery">${allKidsDOBQuery}</a></td>
	</tr>

	<tr>
		<td class="categories">Enfants sous comprimes</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=allActiveKidsUnderArvComprime">${allActiveKidsUnderArvComprime} </a>
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=activekidsUnderArvComprimeNew">${activekidsUnderArvComprimeNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalKidsUnderArvComprime">${TotalKidsUnderArvComprime}</a></td>
	</tr>
	<tr>
		<td class="categories">Enfants sous Sirops</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=allActiveKidsUnderArvSiropsLast">${allActiveKidsUnderArvSiropsLast}</a>
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=activekidsUnderArvSiropsNew">${activekidsUnderArvSiropsNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalKidsUnderArvSirops">${TotalKidsUnderArvSirops}</a></td>
	</tr>


</table> 


<table border="1" height="15%"  align="right" height="15%" width="48%">

<tr class="tableRaw"> <td >No </td><td >Informations supplementaires </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<tr><td class="categories">1</td> <td class="categories">Nombre d'abandons   </td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast1">${patientsExitedLast1}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew1">${patientsExitedNew1} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare1">${TotalpatientsExitedFromCare1}</a></td> </tr>
<tr><td class="categories">2</td> <td class="categories">Nombre de deces  </td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast2">${patientsExitedLast2}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew2">${patientsExitedNew2} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare2">${TotalpatientsExitedFromCare2}</a></td> </tr>
<tr> <td class="categories">3</td><td class="categories">Nombre de patients transferes dans une autre FOSA</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast3">${patientsExitedLast3}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew3">${patientsExitedNew3} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare3">${TotalpatientsExitedFromCare3}</a></td> </tr>
<tr> <td class="categories">4</td><td class="categories"> Nombre de patients transferes d'une autre FOSA</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast4pediatricRegimenLast4">${patientsExitedLast4}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew4">${patientsExitedNew4} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare4">${TotalpatientsExitedFromCare4}</a></td> </tr>
</table>

</div>


<div style=" float: left; width: 100%;">

<table border="1" height="10" align="left"  width="48%">

<tr class="tableRaw"> <td >No </td><td >REGIMEN ADULT </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr> 
 <c:forEach var="adultRegimenResult" items="${adultRegimenResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${adultRegimenResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultRegimenLast&regimenName=${adultRegimenResult.regimenName}">${adultRegimenResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultRegimenNew&regimenName=${adultRegimenResult.regimenName}">${adultRegimenResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdulRegimen&regimenName=${adultRegimenResult.regimenName}">${adultRegimenResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td class="categories">Total</td><td><font color="green" > ${sumOfAdultRegimenLast}</font></td><td><font color="green">${sumOfAdultRegimenNew}</font></td><td><font color="green">${sumOfTatals}</font></td></tr>
</table>
 

 
 <table border="1"  align="right" width="48%">

<tr class="tableRaw"> <td >No </td><td >REGIMEN PEDIATRIC(COMPRIMES) </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr> 
 <c:forEach var="pediatricRegimenResult" items="${pediatricRegimenResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${pediatricRegimenResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricRegimenLast&regimenName=${pediatricRegimenResult.regimenName}">${pediatricRegimenResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricRegimenNew&regimenName=${pediatricRegimenResult.regimenName}">${pediatricRegimenResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPediatricRegimen&regimenName=${pediatricRegimenResult.regimenName}">${pediatricRegimenResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td class="categories">Total</td><td><font color="green" > ${sumOfPediatricRegimenLast}</font></td><td><font color="green">${sumOfPediatricRegimenNew}</font></td><td><font color="green">${sumOfPediatricTatals}</font></td></tr>
</table>
 

</div>
 
<div style=" float: left; width: 100%;">
<table border="1" align="left" width="48%">

<tr class="tableRaw"> <td >No </td><td >ENFANTS: REGIMEN SECOND LINE </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>

<c:forEach var="pediatricRegimenSecondLineResult" items="${pediatricRegimenSecondLineResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${pediatricRegimenSecondLineResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricSecondLineLast&regimenName=${pediatricRegimenSecondLineResult.regimenName}">${pediatricRegimenSecondLineResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricSecondLineNew&regimenName=${pediatricRegimenSecondLineResult.regimenName}">${pediatricRegimenSecondLineResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPediatricSecondLine&regimenName=${pediatricRegimenSecondLineResult.regimenName}">${pediatricRegimenSecondLineResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td class="categories">Total</td><td><font color="green" > ${sumOfSecondLineLast}</font></td><td><font color="green">${sumOfSecondLineNew}</font></td><td><font color="green">${sumOfSecondLineTatals}</font></td></tr>
</table>




<table border="1" align="right" width="48%">

<tr class="tableRaw"> <td >No </td><td >ADULTS: REGIMEN SECOND LINE </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>


<c:forEach var="adultRegimenSecondLineResult" items="${adultRegimenSecondLineResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${adultRegimenSecondLineResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultSecondLineLast&regimenName=${adultRegimenSecondLineResult.regimenName}">${adultRegimenSecondLineResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultSecondLineNew&regimenName=${adultRegimenSecondLineResult.regimenName}">${adultRegimenSecondLineResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdultSecondLine&regimenName=${adultRegimenSecondLineResult.regimenName}">${adultRegimenSecondLineResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td class="categories">Total</td><td><font color="green"> ${sumOfAdultSecondLineLast}</font></td><td><font color="green">${sumOfAdultSecondLineNew}</font></td><td><font color="green">${sumOfAdultSecondLineTatals}</font></td></tr>
</table>
  
 </div> 
<div style=" float: left; width: 100%;">

<table border="1" align="left" width="48%">

<tr class="tableRaw"> <td >No </td><td >PROPHYLAXIE POST EXPOSITION </td> <td > ANCIEN </td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<c:forEach var="prophylaxiePostExpositionResult" items="${prophylaxiePostExpositionResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${prophylaxiePostExpositionResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=prophylaxiePostExpositionLast&regimenName=${prophylaxiePostExpositionResult.regimenName}">${prophylaxiePostExpositionResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=prophylaxiePostExpositionNew&regimenName=${prophylaxiePostExpositionResult.regimenName}">${prophylaxiePostExpositionResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalProphylaxiePostExposition&regimenName=${prophylaxiePostExpositionResult.regimenName}">${prophylaxiePostExpositionResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td class="categories">Total</td><td><font color="green" > ${sumOfProphylaxiePostExpositionLast}</font></td><td><font color="green">${sumOfProphylaxiePostExpositionNew}</font></td><td><font color="green">${sumOfProphylaxiePostExpositionTatals}</font></td></tr>
</table> 

<table border="1" align="right" width="48%">

<tr class="tableRaw"> <td >No </td><td >PMTCT </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<c:forEach var="pmtctResult" items="${pmtctResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${pmtctResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pmtctLast&regimenName=${pmtctResult.regimenName}">${pmtctResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pmtctNew&regimenName=${pmtctResult.regimenName}">${pmtctResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPmtct&regimenName=${pmtctResult.regimenName}">${pmtctResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>

<tr><td> </td><td class="categories">Total</td><td><font color="green"> ${sumOfPmtctLast}</font></td><td><font color="green">${sumOfPmtctNew}</font></td><td><font color="green">${sumOfPmtctTotals}</font></td></tr>
 </table>



</div> 
<div>
<table border="1" align="left" width="48%">

<tr class="tableRaw"> <td >No </td><td >ENFANT:REGIMES PEDIATRIQUES(SIROPS)</td> <td > ANCIEN </td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>

<c:forEach var="pediatricSiropResult" items="${pediatricSiropResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${pediatricSiropResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=PediatricSiropLast&regimenName=${pediatricSiropResult.regimenName}">${pediatricSiropResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=PediatricSiropNew&regimenName=${pediatricSiropResult.regimenName}">${pediatricSiropResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPediatricSirop&regimenName=${pediatricSiropResult.regimenName}">${pediatricSiropResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td class="categories">Total</td><td><font color="green" > ${sumOfPediatricSiropLast}</font></td><td><font color="green">${sumOfPediatricSiropNew}</font></td><td><font color="green">${sumOfPediatricSiropTotals}</font></td></tr>
</table>

<table border="1" align="right" width="48%">

<tr class="tableRaw"> <td >No </td><td >  ADULT THIRD LINE REGIMEN </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<c:forEach var="adultRegimenAdultThirdLineResult" items="${adultRegimenAdultThirdLineResults}" varStatus="number">
 
 <tr><td class="categories">${number.count}</td> <td class="categories">${adultRegimenAdultThirdLineResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=AdultThirdLineLast&regimenName=${adultRegimenAdultThirdLineResult.regimenName}">${adultRegimenAdultThirdLineResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=AdultThirdLineNew&regimenName=${adultRegimenAdultThirdLineResult.regimenName}">${adultRegimenAdultThirdLineResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdultThirdLine&regimenName=${adultRegimenAdultThirdLineResult.regimenName}">${adultRegimenAdultThirdLineResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>

<tr><td> </td><td class="categories">Total</td><td><font color="green"> ${sumOfRegimenAdultThirdLineLast}</font></td><td><font color="green">${sumOfRegimenAdultThirdLineNew}</font></td><td><font color="green">${sumOfRegimenAdultThirdLineTatals}</font></td></tr>
 </table>


</div> 
 
</div>

<!-- 
 </td>
</tr>
</table> -->
 

</c:if>
</div>

 
<!-- <%@ include file="/WEB-INF/template/footer.jsp"%> -->  