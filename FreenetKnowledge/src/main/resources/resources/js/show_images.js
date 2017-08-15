var imagesExtensions = ['.gif', '.jpg', '.jpeg', '.jpe', '.jfif', '.png', '.bmp'];

jQuery().ready(function () {
	// Activate the click
	jQuery('#options_show_images').click(show_images);
});

function show_images(eventObject) {
	// Hide the link
	jQuery('#options_show_images').hide();
	
	// Change the result visual
	jQuery('#resultLinks').addClass('imagesResult');
	
	// Transform all the results
	jQuery('#resultLinks li a').each(function (index, Element) {
		var el = jQuery(Element);
		
		// Get the link and name
		var link = el.attr('href');
		var name = link.substr(link.lastIndexOf('/') + 1);
		
		// Check the extension and hide the result if not an image
		var dotPosition = name.lastIndexOf('.');
		var isAnImage = false;
		if (dotPosition != -1) {
			var extension = name.substr(dotPosition).toLowerCase();
			
			if (jQuery.inArray(extension, imagesExtensions) != -1) {
				isAnImage = true;
			}
		}
		
		if (isAnImage) {
			// Replace the inside with an image of the link + the name of the file
			el.html('<img src="' + link + '" /><br>' + name);
		} else {
			// Hide the <li>
			el.parent().hide();
		}
	});
}