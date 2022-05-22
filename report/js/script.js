function toggleShowHide(id) {
	if(document.getElementById(id).style.display == "none")
        document.getElementById(id).style.display = "block";
    else
        document.getElementById(id).style.display = "none";
}

function toggleButtonText(id) {
	if(document.getElementById(id).innerHTML == "Show")
        document.getElementById(id).innerHTML = "Hide";
    else
        document.getElementById(id).innerHTML = "Show";
}

