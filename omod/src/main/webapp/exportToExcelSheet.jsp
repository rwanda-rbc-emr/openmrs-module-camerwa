<%@ include file="/WEB-INF/template/include.jsp"%>


<openmrs:htmlInclude
file="/moduleResources/camerwa/javaScriptPrint.js" />
<openmrs:htmlInclude file="/moduleResources/camerwa/style.css" />
<openmrs:require privilege="Export ARV regimens report" redirect="camerwaLink.form" otherwise="/login.htm"/>





<%@page contentType="application/vnd.ms-excel" pageEncoding="UTF-8"%>
<h3 class='boxHeader'>ARV REGIMENS CONSUMPTION MONTHLY REPORT</h3> 

<br />     <c:if test="${fn:length(patientsUnderDrug)>0}">
  <h2>${selectedLocaion}</h2><br/>
<h4>MOIS RAPPORTE :
<b> ${month}/${year}</b> </h4>  
<br /> 
<br />

<div>
<table border="1" align="left" height="15%" width="50%" >
	<tr class="tableRaw" > 
		<td>INFORAMTION GENERAL</td>
		<td>Anciens</td>
		<td>Nouveau</td>
		<td>Total</td>
	</tr>
	<tr>
		<td>Nombre de patients sous ARV</td>
		<td><a href="patientList.form?year=${year}&amp;month=${month}&amp;KeyPatinents=LastPatientsUnderARV">
		${LastPatientsUnderARV}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsUnderArvNew">${patientsUnderARVNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPatientsUnderARV">${allPatientsUnderARV}</a></td>
	</tr>
	<tr>
		<td>Nombre d'adultes sous ARV</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=LastAdultsUnderARV">${LastAdultsUnderARV}
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultsUnderARVNew">${adultsUnderARVNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdultsUnderARV">${allAdultDOBQuery}</a></td>
	</tr>
	<tr>
		<td>Nombre d'enfants(<=15) sous ARV</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=LastkidsDOBQuery">${LastkidsDOBQuery}</a>
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=kidsDOBQueryNew">${kidsDOBQueryNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalKidsDOBQuery">${allKidsDOBQuery}</a></td>
	</tr>

	<tr>
		<td>Enfants sous comprimes</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=allActiveKidsUnderArvComprime">${allActiveKidsUnderArvComprime} </a>
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=activekidsUnderArvComprimeNew">${activekidsUnderArvComprimeNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalKidsUnderArvComprime">${TotalKidsUnderArvComprime}</a></td>
	</tr>
	<tr>
		<td>Enfants sous Sirops</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=allActiveKidsUnderArvSiropsLast">${allActiveKidsUnderArvSiropsLast}</a>
		</td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=activekidsUnderArvSiropsNew">${activekidsUnderArvSiropsNew}</a></td>
		<td><a
			href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalKidsUnderArvSirops">${TotalKidsUnderArvSirops}</a></td>
	</tr>


</table> 


<table border="1" height="100%"  align="right" height="15%" width="50%">

<tr class="tableRaw"> <td >No </td><td >Informations supplementaires </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<tr><td>1</td> <td>Nombre d'abandons   </td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast1">${patientsExitedLast1}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew1">${patientsExitedNew1} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare1">${TotalpatientsExitedFromCare1}</a></td> </tr>
<tr><td>2</td> <td>Nombre de deces  </td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast2">${patientsExitedLast2}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew2">${patientsExitedNew2} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare2">${TotalpatientsExitedFromCare2}</a></td> </tr>
<tr> <td>3</td><td>Nombre de patients transferes dans une autre FOSA</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast3">${patientsExitedLast3}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew3">${patientsExitedNew3} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare3">${TotalpatientsExitedFromCare3}</a></td> </tr>
<tr> <td>4</td><td>Nombre de patients transferes d'une aure FOSA</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedLast4pediatricRegimenLast4">${patientsExitedLast4}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=patientsExitedNew4">${patientsExitedNew4} </a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalpatientsExitedFromCare4">${TotalpatientsExitedFromCare4}</a></td> </tr>
</table>

</div>


<div style=" float: left; width: 100%;">

<table border="1" height="20%" align="left"  width="50%">

<tr class="tableRaw"> <td >No </td><td >REGIMEN ADULT </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr> 
 <c:forEach var="adultRegimenResult" items="${adultRegimenResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${adultRegimenResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultRegimenLast&regimenName=${adultRegimenResult.regimenName}">${adultRegimenResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultRegimenNew&regimenName=${adultRegimenResult.regimenName}">${adultRegimenResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdulRegimen&regimenName=${adultRegimenResult.regimenName}">${adultRegimenResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td>Total</td><td><font color="green" > ${sumOfAdultRegimenLast}</font></td><td><font color="green">${sumOfAdultRegimenNew}</font></td><td><font color="green">${sumOfTatals}</font></td></tr>
</table>
 

 
 <table border="1"  align="right" width="50%">

<tr class="tableRaw"> <td >No </td><td >REGIMEN PEDIATRIC(COMPRIMES) </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr> 
 <c:forEach var="pediatricRegimenResult" items="${pediatricRegimenResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${pediatricRegimenResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricRegimenLast&regimenName=${pediatricRegimenResult.regimenName}">${pediatricRegimenResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricRegimenNew&regimenName=${pediatricRegimenResult.regimenName}">${pediatricRegimenResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPediatricRegimen&regimenName=${pediatricRegimenResult.regimenName}">${pediatricRegimenResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td>Total</td><td><font color="green" > ${sumOfPediatricRegimenLast}</font></td><td><font color="green">${sumOfPediatricRegimenNew}</font></td><td><font color="green">${sumOfPediatricTatals}</font></td></tr>
</table>
 

</div>
 
<div style=" float: left; width: 100%;">
<table border="1" align="left" width="50%">

<tr class="tableRaw"> <td >No </td><td >ENFANTS: REGIMEN SECOND LINE </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>

<c:forEach var="pediatricRegimenSecondLineResult" items="${pediatricRegimenSecondLineResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${pediatricRegimenSecondLineResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricSecondLineLast&regimenName=${pediatricRegimenSecondLineResult.regimenName}">${pediatricRegimenSecondLineResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pediatricSecondLineNew&regimenName=${pediatricRegimenSecondLineResult.regimenName}">${pediatricRegimenSecondLineResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPediatricSecondLine&regimenName=${pediatricRegimenSecondLineResult.regimenName}">${pediatricRegimenSecondLineResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td>Total</td><td><font color="green" > ${sumOfSecondLineLast}</font></td><td><font color="green">${sumOfSecondLineNew}</font></td><td><font color="green">${sumOfSecondLineTatals}</font></td></tr>
</table>




<table border="1" align="right" width="50%">

<tr class="tableRaw"> <td >No </td><td >ADULTS: REGIMEN SECOND LINE </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>


<c:forEach var="adultRegimenSecondLineResult" items="${adultRegimenSecondLineResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${adultRegimenSecondLineResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultSecondLineLast&regimenName=${adultRegimenSecondLineResult.regimenName}">${adultRegimenSecondLineResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=adultSecondLineNew&regimenName=${adultRegimenSecondLineResult.regimenName}">${adultRegimenSecondLineResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalAdultSecondLine&regimenName=${adultRegimenSecondLineResult.regimenName}">${adultRegimenSecondLineResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td>Total</td><td><font color="green"> ${sumOfAdultSecondLineLast}</font></td><td><font color="green">${sumOfAdultSecondLineNew}</font></td><td><font color="green">${sumOfAdultSecondLineTatals}</font></td></tr>
</table>
  
 </div> 
<div style=" float: left; width: 100%;">

<table border="1" align="left" width="50%">

<tr class="tableRaw"> <td >No </td><td >PROPHYLAXIE POST EXPOSITION </td> <td > ANCIEN </td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<c:forEach var="prophylaxiePostExpositionResult" items="${prophylaxiePostExpositionResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${prophylaxiePostExpositionResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=prophylaxiePostExpositionLast&regimenName=${prophylaxiePostExpositionResult.regimenName}">${prophylaxiePostExpositionResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=prophylaxiePostExpositionNew&regimenName=${prophylaxiePostExpositionResult.regimenName}">${prophylaxiePostExpositionResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalProphylaxiePostExposition&regimenName=${prophylaxiePostExpositionResult.regimenName}">${prophylaxiePostExpositionResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td>Total</td><td><font color="green" > ${sumOfProphylaxiePostExpositionLast}</font></td><td><font color="green">${sumOfProphylaxiePostExpositionNew}</font></td><td><font color="green">${sumOfProphylaxiePostExpositionTatals}</font></td></tr>
</table> 

<table border="1" align="right" width="50%">

<tr class="tableRaw"> <td >No </td><td >PMTCT </td> <td > ANCIEN</td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>
<c:forEach var="pmtctResult" items="${pmtctResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${pmtctResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pmtctLast&regimenName=${pmtctResult.regimenName}">${pmtctResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=pmtctNew&regimenName=${pmtctResult.regimenName}">${pmtctResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPmtct&regimenName=${pmtctResult.regimenName}">${pmtctResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>

<tr><td> </td><td>Total</td><td><font color="green"> ${sumOfPmtctLast}</font></td><td><font color="green">${sumOfPmtctNew}</font></td><td><font color="green">${sumOfPmtctTotals}</font></td></tr>
 </table>



</div> 
<div>
<table border="1" align="left" width="50%">

<tr class="tableRaw"> <td >No </td><td >ENFANT:REGIMES PEDIATRIQUES(SIROPS)</td> <td > ANCIEN </td> <td >NOUVEAUX </td><td >TOTAL</td> </tr>

<c:forEach var="pediatricSiropResult" items="${pediatricSiropResults}" varStatus="number">
 
 <tr><td>${number.count}</td> <td>${pediatricSiropResult.regimenName}</td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=PediatricSiropLast&regimenName=${pediatricSiropResult.regimenName}">${pediatricSiropResult.regimenLast}</a></td> <td><a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=PediatricSiropNew&regimenName=${pediatricSiropResult.regimenName}">${pediatricSiropResult.regimenNew}</a></td> <td> <a href="patientList.form?year=${year}&amp;month=${month}&KeyPatinents=TotalPediatricSirop&regimenName=${pediatricSiropResult.regimenName}">${pediatricSiropResult.totalRegimen}</a></td> </tr>
	   
  
</c:forEach>
<tr><td> </td><td>Total</td><td><font color="green" > ${sumOfPediatricSiropLast}</font></td><td><font color="green">${sumOfPediatricSiropNew}</font></td><td><font color="green">${sumOfPediatricSiropTotals}</font></td></tr>
</table>


</div> 
 
</div>

<!-- 
 </td>
</tr>
</table> -->
 

</c:if>
</div>



 


<!--   <%@ include file="/WEB-INF/template/footer.jsp"%> --> 