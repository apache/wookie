/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This provides the code for managing assets and collections of assets.
 * It provides generic functionality that is applicable to all asset types
 * and is therefore intended to be reused across all asset players.
 */
var ${widget.shortname}_asset_controller = { 
    idx: 0, // index of current asset being displayed,
    auto: false, // indicates if we are auto playing assets
    staticDuration: 5000, // interval between auto play of static assets (e.g. photo's)
    interval: null, // the interval object that controls the auto player

    init:function() { 
	// register button events
	$('#next').click(function() {
	    ${widget.shortname}_asset_controller.nextAsset();
	});

	$('#prev').click(function() {
	    ${widget.shortname}_asset_controller.prevAsset();
	});

	$('#gotoAlbum').click(function() {
	    alert("gotoAlbum not implemented yet");
	});

	$('#playPause').click(function() {
	    if (!${widget.shortname}_asset_controller.auto) {
		${widget.shortname}_asset_controller.startAuto();
		// FIXME: this breaks layout - as it overwrides JQuery markup
		this.innerText = "Pause";
	    } else {
		${widget.shortname}_asset_controller.stopAuto();
		// FIXME: this breaks layout - as it overwrides JQuery markup
		this.innerText = "Play";
	    }
	});
    },
    
    setCollection:function(album){
	sessionStorage.setItem('collection', JSON.stringify(album));
        // FIXME: cache images for faster slideshow. e.g. http://www.anthonymclin.com/code/7-miscellaneous/98-on-demand-image-loading-with-jquery
	${widget.shortname}_asset_controller.displayAsset(0);
    },

    getCollection:function() {
	return JSON.parse(sessionStorage.getItem('collection'));
    },
    
    displayAsset:function(idx) {
	var album = ${widget.shortname}_asset_controller.getCollection();
	$('#asset').attr("src", album[idx].src);
    },

    nextAsset:function() {
	var album = ${widget.shortname}_asset_controller.getCollection();
	${widget.shortname}_asset_controller.idx += 1;
        if (${widget.shortname}_asset_controller.idx >= album.length) { 
	    ${widget.shortname}_asset_controller.idx = 0;
	};
	${widget.shortname}_asset_controller.displayAsset(${widget.shortname}_asset_controller.idx);
    },

    prevAsset:function() {
	var album = ${widget.shortname}_asset_controller.getCollection();
	${widget.shortname}_asset_controller.idx -= 1;
        if (${widget.shortname}_asset_controller.idx <0) { 
	    ${widget.shortname}_asset_controller.idx = album.length - 1;
	};
	${widget.shortname}_asset_controller.displayAsset(${widget.shortname}_asset_controller.idx);
    },

    startAuto:function() {
	${widget.shortname}_asset_controller.auto = true;
	${widget.shortname}_asset_controller.interval = 
	    window.setInterval(${widget.shortname}_asset_controller.nextAsset, 
			       ${widget.shortname}_asset_controller.staticDuration);
    },

    stopAuto:function() {
	${widget.shortname}_asset_controller.auto = false;
	window.clearInterval(${widget.shortname}_asset_controller.interval);
    }
};

$('#home').live('pageshow',function(event) { 
  ${widget.shortname}_asset_controller.init(); 
});
