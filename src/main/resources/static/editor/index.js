
var editor;
$(document).ready( function(){
	var textarea = document.getElementById("textarea");
	editor = CodeMirror.fromTextArea(textarea, {
			theme:"darcula",
			mode: {name: "javascript", json: true},
			lineNumbers: true,
			lineWrapping: true
	});
	//load current Json from Server
	jQuery.get('../tree/v1.json', function(data) {
		if( data != ""){
			$('#textarea').val(data);
		}
	});
	drawDiagram();
});

function drawDiagram(){	
	
	let jsonstring = JSON.parse(editor.getValue());
	let digraph = "digraph d {"
	
		
	for(var item in jsonstring['questions']) {
		question = jsonstring['questions'][item]
		digraph += question['id'] + " [label=\""+question['question']+"\"]; \n";
	}
	
	
	for(var qName in jsonstring['questions']) {
		question = jsonstring['questions'][qName]
		from = question['id'];
		for(var opName in question['options']) {
			option = question['options'][opName]
			next = option['next'] === "-1" ? "Summary" : option['next'];
			digraph += from + " -> "+next + " [label=\""+option['name']+"\"];";
		}
	}
	
	digraph += "}"
	
	console.log(digraph);
	
	
	// for svg
	let svgXml = Viz(digraph, { format: "svg"});
	$('#img').html(svgXml);
	// if png
	//let imgelement = Viz(digraph, { format: "png-image-element"});
	//$('#img').html(imgelement);
}


function downloadJson(){
	var jsonString = editor.getValue().replace("\t","");
	var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(jsonString);
	var dlAnchorElem = document.getElementById('downloadJson');
	dlAnchorElem.setAttribute("href",     dataStr     );
	dlAnchorElem.setAttribute("download", "OpenHealthCheck.json");
	dlAnchorElem.click();
	
}
