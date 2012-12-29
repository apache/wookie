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
    albums: null, // the albums of colelctions that can be played
    collection: null, // the collection of assets currently playing

    init:function() { 
	// register button events
	$('#next').click(function() {
	    ${widget.shortname}_asset_controller.nextAsset();
	});

	$('#prev').click(function() {
	    ${widget.shortname}_asset_controller.prevAsset();
	});

	$('#selectAlbum').click(function() {
            $.mobile.changePage($("#home"));
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

    initAlbums:function() { 
        var list = $("#albumList");
        var albums = ${widget.shortname}_asset_controller.albums;
        if (albums) {
            list.empty();
            $.each(
                albums,
                function( intIndex, album ){
                    list.append(${widget.shortname}_asset_controller.getAlbumCover(album));
                    $("#" + album.title).click(function() {
                        ${widget.shortname}_asset_controller.showAlbum(album);
                    });
                }
            );
        } else {
            list.append("Loading Albums...");
        }
    },

    getAlbumCover:function(album) {
      var html = "<li id='" + album.title + "'>";
      html = html + "<img src='" + album.preview + "' width='120' height='120'/> ";
      html = html + album.title;
      html = html + "</a></li>";
      return $(html);
    },

    showAlbum:function(album) {
	${widget.shortname}_asset_controller.setCollection(album);
        $.mobile.changePage("#picture");
    },

    setAlbums:function(albums) {
	${widget.shortname}_asset_controller.albums = albums;
        ${widget.shortname}_asset_controller.initAlbums();
    },

    
    // Set the collection, that is the collection being shown at this time
    // to an array of assets. The "collection" should be either an array or an
    // object with an assets property which is the required array.
    //
    // The array will contain, at least, a src property which contains the src to
    // use for the HTML image.
    setCollection:function(collection){
        if (collection.assets) {
	    ${widget.shortname}_asset_controller.collection = collection.assets;
        } else {
	    ${widget.shortname}_asset_controller.collection = collection;
        }
        // TODO: cache images for faster slideshow. e.g. http://www.anthonymclin.com/code/7-miscellaneous/98-on-demand-image-loading-with-jquery
	${widget.shortname}_asset_controller.displayAsset(0);
    },

    getCollection:function() {
	return 	${widget.shortname}_asset_controller.collection;
    },
    
    displayAsset:function(idx) {
	var collection = ${widget.shortname}_asset_controller.getCollection();
	$('#asset').attr("src", collection[idx].src);
    },

    nextAsset:function() {
	var collection = ${widget.shortname}_asset_controller.getCollection();
	${widget.shortname}_asset_controller.idx += 1;
        if (${widget.shortname}_asset_controller.idx >= collection.length) { 
	    ${widget.shortname}_asset_controller.idx = 0;
	};
	${widget.shortname}_asset_controller.displayAsset(${widget.shortname}_asset_controller.idx);
    },

    prevAsset:function() {
	var collection = ${widget.shortname}_asset_controller.getCollection();
	${widget.shortname}_asset_controller.idx -= 1;
        if (${widget.shortname}_asset_controller.idx <0) { 
	    ${widget.shortname}_asset_controller.idx = collection.length - 1;
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

$('#picture').live('pageinit',function(event) { 
  ${widget.shortname}_asset_controller.init(); 
});

$('#home').live('pageinit',function(event) { 
  ${widget.shortname}_asset_controller.initAlbums(); 
});
