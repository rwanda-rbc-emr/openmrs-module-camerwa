var gAutoPrint = true; // Tells whether to automatically call the print
						// function
function printSpecial() {
	if (document.getElementById != null) {
		var html = '<HTML>\n<HEAD>\n';
		if (document.getElementsByTagName != null) {
			var headTags = document.getElementsByTagName("head");
			if (headTags.length > 0)
				html += headTags[0].innerHTML;
		}

		html += '\n</HE>\n<BODY>\n';

		var printReadyElem = document.getElementById("display");

		if (printReadyElem != null) {
			html += printReadyElem.innerHTML;
		} else {
			alert("Could not find the printReady function");
			return;
		}

		html += '\n</BO>\n</HT>';

		var printWin = window.open("", "printSpecial");
		printWin.document.open();
		printWin.document.write(html);
		printWin.document.close();
		if (gAutoPrint)
			printWin.print();
	} else {
		alert("The print ready feature is only available if you are using an browser. Please update your browswer.");
	}
}

function validateNumber() {
	if (document.Form.monthReported.value == "Choose Month") {
		var answer = alert("You have to choose one criteria")
		if (answer)
			return false;
		else
			return false;
	}
	if (isNaN(document.Form.yearReported.value)) {
		var answer = alert("You gave an invalid value")
		if (answer)
			return false;
		else
			return false;
	} else if (document.Form.yearReported.value == ""
			|| document.Form.yearReported.value == null
			|| document.Form.yearReported.value < 0) {
		var answer = alert("You are not allowed to give an empty value")
		if (answer)
			return false;
		else
			return false;
	}
}

//-------------------------------------------------------------------------------------

function createDeleteButton(baseName){
	var deleteButton = $(document.createElement("span")).attr("id","delete_"+fieldGroupCount).attr("class","redbox").text("X");
	deleteButton.click(
		function()
		{
			var idString = $(this).attr("id");
			var selectorText = "#"+baseName+"tableid_"+idString.substring(idString.indexOf("_")+1);
			
			$(selectorText).hide(200,
					function()
					{
						$(selectorText).remove();
					}
			);
		}
	);
return deleteButton;
}

function addOptionsToSelect(selectElement, drugDispArray){
//selectElement.append($(document.createElement("option")).attr("value","").text("---"));
for( var j=0; j < drugDispArray.length; j++){
	selectElement.append($(document.createElement("option")).attr("value",drugDispArray[j]).text(drugDispArray[j]));
}
}

function createNakedOptionSelect(nameValue, drugDispArray,classAttr){
var selectElement = $(document.createElement("select")).attr("id",fieldGroupCount).attr("class", classAttr).attr("name",nameValue);
addOptionsToSelect(selectElement,drugDispArray);
var tableRow = $(document.createElement("tr"))
		.append($(document.createElement("td")).append(selectElement));	

return tableRow;
}


function addDrug(baseName, drugDispArray, classAttr, drugLabel){	
// the containing table
var table = $(document.createElement("table")).attr("id",baseName+"tableid_" + ++fieldGroupCount).attr("width","90%");

var drugs = createNakedOptionSelect("drugs_"+fieldGroupCount,drugDispArray, classAttr);

	
// delete button
var deleteButton = createDeleteButton(baseName);

	
// adding row fields
table.append($(document.createElement("tr")).attr("align", "left")
		.append($(document.createElement("td")).attr("width","20").append(drugLabel))			
		.append($(document.createElement("td")).attr("width","12").append(drugs))
		.append($(document.createElement("td")).append(deleteButton))
		);



// add the line separator between tables
table.append($(document.createElement("tr"))
		.append($(document.createElement("td")).attr("colspan","5")
				.append($(document.createElement("hr")).attr("color","#C9C9C9"))
				));

// add the entire set of elements to the div
table.hide();
$("#"+baseName).append(table);
table.show(200);
}





