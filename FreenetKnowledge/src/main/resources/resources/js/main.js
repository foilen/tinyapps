// Fix the iframe height
jQuery().ready(fixIframeHeight);
jQuery().ready(function () {
	setTimeout(fixIframeHeight, 500);
});
jQuery(window).resize(fixIframeHeight);

function fixIframeHeight() {
	var windowHeight = jQuery(window).height();
	
	var frameElement = jQuery('#frame');
	var frameTop = frameElement.position().top;
	frameElement.height(windowHeight-frameTop-20);
}

// Update the DB status
jQuery().ready(updateDBStatus);

function updateDBStatus() {
	
	jQuery('#visitationState').load('knowledge/html/dbstate');
	
	setTimeout(updateDBStatus, 1000);
}