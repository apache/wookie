var ${widget.shortname}_images_controller = { 
    init:function() { 
	// FIXME: album should be created by reading a directory
	var assets = [];
	assets[0] = {
	    "src":"images/Places/Scottish Highlands.bmp"
	};
	assets[1] = {
	    "src":"images/Places/Tower Bridge.bmp"
	};
	${widget.shortname}_asset_controller.setCollection(assets);
	${widget.shortname}_scanning_controller.scanElements = $('[data-scanOrder]');
    },

};

$('#home').live('pageshow',function(event) { 
  ${widget.shortname}_images_controller.init(); 
});
