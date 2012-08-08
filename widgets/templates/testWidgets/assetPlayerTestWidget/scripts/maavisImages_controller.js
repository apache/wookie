var ${widget.shortname}_images_controller = { 
    idx: 0, // index of current asset being displayed,
    auto: false, // indicates if we are auto playing assets
    staticDuration: 5000, // interval between auto play of static assets (e.g. photo's)
    interval: null, // the interval object that controls the auto player

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
    },

};

$('#home').live('pageshow',function(event) { 
  ${widget.shortname}_images_controller.init(); 
});
