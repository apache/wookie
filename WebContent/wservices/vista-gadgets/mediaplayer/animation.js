// File:		animation.js
// Description: Provides animation
// Dependencies: none
// Author:      Jonathan Marston
// Copyright:   Please don't use this script without explicit consent from the original author!

var LINEAR = 1;
var SMOOTH = 2;
var EASE_IN = 3;
var EASE_OUT = 4;

function Animator( animElement )
{
	var me = this;
	var element = animElement;
	var animations = new Array();
	var interval = undefined;
	var startTime, duration;
	var type;
	var ondone;

	me.getOpacity = function()
	{
		if (element.style.visibility == "hidden") return 0;
		if (element.style.display == "none") return 0;
		try	{ return element.filters.item("alpha").opacity / 100; } catch (e) {}
		if (element.style.opacity) return parseFloat( element.style.opacity );
		return 1;
	}
		
	me.setOpacity = function( opacity )
	{
		opacity = Math.max( 0, Math.min( 1.0, opacity ) );
		element.style.visibility = (opacity > 0.0) ? "visible" : "hidden";

		if (element.filters)
		{
		    try {
		        element.filters.item("alpha");
		    } catch( e ) {
		        element.style.filter += " alpha(opacity=100)";
		    }
			//element.style.filter = "alpha(opacity=" + opacity * 100 + ")"
			element.filters.item("alpha").opacity = opacity * 100;
			element.filters.item("alpha").enabled = opacity < 1.0;
		}
		element.style.opacity = Math.min( 0.9999, opacity );
	}
	
	me.setWidth  = function( width )  { element.style.width  = Math.round( width )  + "px"; }
	me.setHeight = function( height )
	{ 
		if (height < 1)
			element.style.display = "none";
		else
		{
			element.style.display = "block";
			element.style.height = Math.round( height ) + "px";
		}
	}
	me.setLeft   = function( left )	  { element.style.left   = Math.round( left )   + "px"; }
	me.setTop    = function( top )    { element.style.top    = Math.round( top )    + "px"; }
	
	me.setScrollTop  = function( top )  { element.scrollTop = top; }
	me.setScrollLeft = function( left ) { element.scrollLeft = left; }
	
	me.setScrollTopToBottom = function() { element.scrollTop = element.scrollHeight - element.offsetHeight; }
	
	me.step = function()
	{
		var elapsed = (new Date()).getTime() - startTime;
		if (elapsed >= duration)
		{
			for (var i = 0; i < animations.length; i++)
				animations[i].setter( animations[i].endValue );
			
			window.clearInterval( interval );
			if (me.ondone) me.ondone( element );
		}
		else
		{
			var percent = elapsed / duration;
			if (type == SMOOTH)   percent = 0.5 - Math.cos(			   percent * Math.PI		) * 0.5;
			if (type == EASE_OUT) percent = 0.0 - Math.cos( (Math.PI + percent * Math.PI) * 0.5 );
			if (type == EASE_IN)  percent = 1.0 - Math.cos(			   percent * Math.PI  * 0.5 );

			for (var i = 0; i < animations.length; i++)
			{
				animations[i].setter( animations[i].beginValue + 
						(animations[i].endValue - animations[i].beginValue) * percent );
			}
		}
	}
	
	me.play = function()
	{
		startTime = (new Date()).getTime();
		if (!interval)
			interval = window.setInterval( me.step, 1 );
	}
	
	me.stop = function()
	{
		window.clearInterval( interval );
		interval = undefined;
		animations = new Array();
	}
	
	me.fadeIn = function( seconds, ondone )
	{
		me.fadeTo( 1.0, seconds, ondone );
	}
	
	me.fadeOut = function( seconds, ondone )
	{
		me.fadeTo( 0.0, seconds, ondone );
	}
	
	me.fadeTo = function( opacity, seconds, ondone )
	{
		me.animate( [ { setter: me.setOpacity, beginValue: me.getOpacity(), endValue: opacity } ],
					SMOOTH, seconds, ondone );
	}

	me.moveTo = function( left, top, seconds, ondone )
	{
		me.animate( [ { setter: me.setLeft, beginValue: parseInt( element.style.left ), endValue: left },
					  { setter: me.setTop,  beginValue: parseInt( element.style.top ),  endValue: top } ],
					SMOOTH, seconds, ondone );
	}
	
	me.scaleTo = function( left, top, width, height, seconds, ondone ) {
	    me.animate( [ { setter: me.setLeft, beginValue: parseInt( element.style.left ), endValue: left },
					  { setter: me.setTop,  beginValue: parseInt( element.style.top ),  endValue: top },
	                  { setter: me.setWidth, beginValue: parseInt( element.style.width ), endValue: width },
	                  { setter: me.setHeight, beginValue: parseInt( element.style.height ), endValue: height } ],
	                EASE_OUT, seconds, ondone );
	}
	
	me.scaleAndFadeTo = function( left, top, width, height, opacity, seconds, ondone ) {
	    me.animate( [ { setter: me.setLeft, beginValue: parseInt( element.style.left ), endValue: left },
					  { setter: me.setTop,  beginValue: parseInt( element.style.top ),  endValue: top },
	                  { setter: me.setWidth, beginValue: parseInt( element.style.width ), endValue: width },
	                  { setter: me.setHeight, beginValue: parseInt( element.style.height ), endValue: height },
	                  { setter: me.setOpacity, beginValue: me.getOpacity(), endValue: opacity } ],
	                SMOOTH, seconds, ondone );
	}
	
	me.expandIn = function( seconds, ondone )
	{
		if (element.style.display != "none") return;
		element.style.overflow = "hidden";
		element.style.display = "block";
		element.style.height = "1px";
		element.scrollTop = 0;
		
		me.animate( [ { setter: me.setOpacity,     beginValue: me.getOpacity(),	 endValue: 1 },
					  { setter: me.setHeight,      beginValue: 0,				 endValue: element.scrollHeight } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.contractOut = function( seconds, ondone )
	{
		if (element.style.display == "none") return;
		element.style.overflow = "hidden";
		element.scrollTop = 0;
		
		me.animate( [ { setter: me.setOpacity,	 beginValue: me.getOpacity(),		endValue: 0 },
					  { setter: me.setHeight,	 beginValue: element.offsetHeight,	endValue: 0 } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.slideIn = function( seconds, ondone )
	{
		if (element.style.display != "none") return;
		element.style.overflow = "hidden";
		element.style.display = "block";
		element.style.height = "1px";
		
		me.animate( [ { setter: me.setOpacity,			 beginValue: me.getOpacity(),	 endValue: 1 },
					  { setter: me.setHeight,			 beginValue: 0,					 endValue: element.scrollHeight },
					  { setter: me.setScrollTopToBottom, beginValue: 0,					 endValue: 0 } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.slideOut = function( seconds, ondone )
	{
		if (element.style.display == "none") return;
		element.style.overflow = "hidden";
		
		me.animate( [ { setter: me.setOpacity,			 beginValue: me.getOpacity(),		endValue: 0 },
					  { setter: me.setHeight,			 beginValue: element.style.height ? parseInt( element.style.height ) : element.scrollHeight,	endValue: 0 },
					  { setter: me.setScrollTopToBottom, beginValue: 0,						endValue: 0 } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.resizeToContent = function( seconds, ondone )
	{
		element.style.overflow = "hidden";
		element.scrollTop = 0;
		
		me.animate( [ { setter: me.setOpacity,			 beginValue: me.getOpacity(),		endValue: 1 },
					  { setter: me.setHeight,			 beginValue: parseInt( element.style.height ),	endValue: element.scrollHeight } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.riseIn = function( seconds, ondone )
	{
		if (!element.style.position) element.style.position = "relative";
		if (!element.style.top) element.style.top = "0px";
		
		me.animate( [ { setter: me.setOpacity,	beginValue: me.getOpacity(), endValue: 1 },
					  { setter: me.setTop,		beginValue: parseInt( element.style.top ) + element.offsetHeight,
												endValue:   parseInt( element.style.top ) } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.riseOut = function( seconds, ondone )
	{
		if (!element.style.position) element.style.position = "relative";
		if (!element.style.top) element.style.top = "0px";
		
		me.animate( [ { setter: me.setOpacity, beginValue: me.getOpacity(), endValue: 0 },
					  { setter: me.setTop,	   beginValue: parseInt( element.style.top ),
											   endValue:   parseInt( element.style.top ) - element.offsetHeight } ],
					EASE_IN, seconds, ondone );
	}
	
	me.dropOut = function( seconds, ondone )
	{
		if (!element.style.position) element.style.position = "relative";
		if (!element.style.top) element.style.top = "0px";
		
		me.animate( [ { setter: me.setOpacity, beginValue: me.getOpacity(), endValue: 0 },
					  { setter: me.setTop,	   beginValue: parseInt( element.style.top ),
											   endValue:   parseInt( element.style.top ) + element.offsetHeight } ],
					EASE_IN, seconds, ondone );
	}
	
	me.dropIn = function( seconds, ondone )
	{
		if (!element.style.position) element.style.position = "relative";
		if (!element.style.top) element.style.top = "0px";
		
		me.animate( [ { setter: me.setOpacity, beginValue: me.getOpacity(), endValue: 1 },
					  { setter: me.setTop,	   beginValue: parseInt( element.style.top ) - element.offsetHeight,
											   endValue:   parseInt( element.style.top ) } ],
					EASE_OUT, seconds, ondone );
	}
	
	me.scrollTo = function( top, left, seconds, ondone )
	{
		me.animate( [ { setter: me.setScrollTop,  beginValue: element.scrollTop,  endValue: top },
					  { setter: me.setScrollLeft, beginValue: element.scrollLeft, endValue: left } ],
					EASE_OUT, seconds, ondone );	
	}
	
	me.animate = function( anim, animType, seconds, ondone )
	{
		me.stop();
		me.ondone = ondone;
		duration = seconds * 1000;
		type = animType || SMOOTH;
		animations = new Array();
		
		for (var i = 0; i < anim.length; i++) {
		    if (anim[i].beginValue != anim[i].endValue) {
		        animations[animations.length] = anim[i];
		    }
		}
		
		me.play();
	}
}