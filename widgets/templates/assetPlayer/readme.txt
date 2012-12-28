* Asset Player Template
The Asset Player Template allows new player widgets to be quickly and
easily built. It can, by default, handle images, video and
audio. However, at present there is no way for the template to
differentiate between these. The appropriate tags, if not <img..> must
be provide in the content_primary or content_secondary files when
building a widget from this template.

By defaul the template expects to find an <img...> tag with the ID
"asset". This can be changed to an audio or video tag in the widgets
that build on this template (see content_primary.html).

** Functions

  - play a collection of assets in a rolling "slideshow"
  - move to the next asset
  - move back tot he previous asset
  - scanning of buttons controlling the player (via the scanning template)

** Use

By default the asset player assumes all assets are images it is
therefore easiest to display images using this widget. To do this all
you need to do is set the collection that should be displayed. This is
done by calling the setCollection(assets) method, where assets is
either an array or an object with an "assets" property which is the
required array.  This array will contain, at least, a "src"
property which contains the src to use for the HTML image.

For example:

var ${widget.shortname}_images_controller = { 
    init:function() { 
        var assets = {
            "assets": [
                {
	            "src":"images/places/032.jpg"
	        },
                {
	            "src":"images/places/042.jpg"
	        },
                {
	            "src":"images/places/eden project.jpg"
	        },
                {
	            "src":"images/places/edenProject.jpg"
	        },

            ]
        };
/*
	var assets = [];
	assets[0] = {
	    "src":"images/places/032.jpg"
	};
	assets[1] = {
	    "src":"images/places/042.jpg"
	};
*/

	${widget.shortname}_asset_controller.setCollection(assets);
	${widget.shortname}_scanning_controller.scanElements = $('[data-scanOrder]');
    },

};

$('#home').live('pageshow',function(event) { 
  ${widget.shortname}_images_controller.init(); 
});

*** Assets other than images

To play assets that are not images you will also need to overwrite the
content_primary.html file in order to provide the appropriate HTML to
render the asset. For example, to play audio files you will use
something like:

<div>
  <div id="player">
    <audio id="asset" autoplay="autoplay">
            Your browser does not support the audio tag.
    </audio>
  </div>
</div>

FIXME: Note that a future version of the player is likely to automaically
create the relevant HTML tag for a chosen asset type.

