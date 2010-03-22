/*
* Copyright (c) 2007, Opera Software ASA
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of Opera Software ASA nor the
*       names of its contributors may be used to endorse or promote products
*       derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY OPERA SOFTWARE ASA ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL OPERA SOFTWARE ASA BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
 *  $ alias of document.getElementById
 */
function $(id)
{
    return document.getElementById(id);
}
/*
 *  Function bind
 */
Function.prototype.bind = function( object )
{
    var method = this;
    return function ()
    {
        return method.apply( object, arguments );
    }
}

/*
 *  on load, instanciate a new BubbleGame
 */
if(window.addEventListener){
	window.addEventListener
	(
	    'load',
	    function(/* load callback */)
	    {
	        if (screen.availHeight + screen.availWidth <= 800)
	        {
	            window.moveTo(0, 0);
	            window.resizeTo(screen.availWidth, screen.availHeight);
	            window.scrollTo(0,0);
	        }
	        new BubbleGame( 'gameArea', 'newGame', 'score', 'hiScore' );
	    },
	    false
	)
}
else{
	window.attachEvent
	(
	    'onload',
	    function(/* load callback */)
	    {	 
	        if (screen.availHeight + screen.availWidth <= 800)
	        {	 
	            window.moveTo(0, 0);
	            window.resizeTo(screen.availWidth, screen.availHeight);
	            window.scrollTo(0,0);
	        }
	        new BubbleGame( 'gameArea', 'newGame', 'score', 'hiScore' );
	    }
	)	
}

/*
 *  BubbleGame
 */
function BubbleGame( gameAreaId, newGameId, scoreId, hiScoreId )
{
    var that        = this,
        isGameOver  = false,
        width       = 0,
        height      = 0,
        tiles       = 0,
        score       = 0,
        map         = [],
        colors      = ['#e00','#fa0','#766','#0cf','#a0f','#3b0'];

    /*
     *  displayScore
     */
    function displayScore()
    {
        hScore.textContent      = score;
        hHiScore.textContent    = hiScore;
        $('dock').textContent   = "Score: " + score;
    }

    /*
     *  newGame
     */
    this.newGame = function()
    {
        score       = 0;
        width       = 0;
        height      = 0;
        map.length  = 0;
        tiles       = 0;

        displayScore();

        hGameArea.className = '';
        hGameArea.innerHTML = '';

        if(window.addEventListener){
        	hGameArea.addEventListener( 'mouseover',    hoverTile.bind(this), false );
        	hGameArea.addEventListener( 'mouseout',     hoverTile.bind(this), false );
        }
        else{
	        hGameArea.attachEvent( 'onmouseover',    hoverTile.bind(this));
	        hGameArea.attachEvent( 'onmouseout',     hoverTile.bind(this));        	
        }

        var isFull  = false;
        while( !isFull )
        {
            var newTile = document.createElement('span'),
                color   = colors[Math.random()*colors.length|0];

            map.push( color );
            newTile.setAttribute( 'style', 'background-color:'+ color );
            newTile.setAttribute( 'data-index', tiles );
            hGameArea.appendChild( newTile );

    	    if(window.addEventListener){
    	    	newTile.addEventListener( 'click', clickTile.bind(this), false );
    	    }
    	    else{
	            newTile.attachEvent( 'onclick', clickTile.bind(this));	        	    	
    	    }
    	    
            //newTile.onclick= clickTile.bind(this);
            if( !newTile.offsetTop )
            {
                width++;
            }

            isFull = !(++tiles%width) && (newTile.offsetTop+newTile.offsetHeight*2)>=gameArea.clientHeight;
        }
        height = hGameArea.childNodes.length/width;
    }

    /*
     *  floodFill
     */
    function floodFill( x,y )
    {
        var color       = map[ x+y*width ],
            floodMap    = new Array(map.length),
            n           = 0;

        if( color  )
        {
            n = (function( x, y )
            {
                var i = x+y*width;
                if( map[i]==color && !floodMap[i] )
                {
                    //  mark tile
                    floodMap[i] = 1;

                    //  recurse
                    var n = 1;
                    if( x>0 )       n += arguments.callee( x-1, y );
                    if( x<width-1 ) n += arguments.callee( x+1, y );
                    if( y>0 )       n += arguments.callee( x, y-1 );
                    if( y<height-1) n += arguments.callee( x, y+1 );
                    return n;
                }
                return 0;
            })( x, y );
        }

        return {'map':floodMap,'tiles':n}
    }

    /*
     *  hoverTile
     */
    function hoverTile(event)
    {
        var src = event.srcElement? event.srcElement : event.target;
        if( src==event.currentTarget || event.type=='mouseout' )
        {
            var hoverMap    = new Array(map.length);
        }
        else
        {
            var index   = src.getAttribute('data-index')|0,
                color   = map[index],
                x       = index%width,
                y       = index/width|0,
                hoverMap= floodFill( x, y ).map;
        }

        //  update tiles' display
        var node    = hGameArea.firstChild,
            i       = 0;
        while( node )
        {
            var className   = hoverMap[i++]?'hover':'';
            if( node.className != className )
            {
                node.className = className;
            }
            node = node.nextSibling;
        }
    }

    /*
     *  clickTile
     */
    function clickTile(event)
    {
        var src     = event.srcElement? event.srcElement : event.target,
            index   = src.getAttribute('data-index')|0,
            x       = index%width,
            y       = index/width|0,
            flood   = floodFill( x, y );


        //  a single|empty tile -> exit
        if( flood.tiles<2 )
        {
            return true;
        }

        //  clear tiles
        for( var i=flood.map.length;i--; )
        {
            if( flood.map[i] )
                map[i]  = 0;
        }

        //  decrease tiles count
        tiles -= flood.tiles;

        //  update score
        score+= (1+flood.tiles)*flood.tiles;


        if( tiles )
        {
            //  waterfall
            var cols=[]
            for( var x=0; x<width; x++ )
            {
                var i = map.length-width+x,
                    n = height-1;
                while( i>=width )
                {
                    var j = i-width;
                    if( !map[i] )
                    {
                        while( j>=0 && !map[j])j-=width;
                        if( j>=0 )
                        {
                            map[i] = map[j];
                            map[j] = 0;
                        }
                    }
                    if( !map[i] )
                    {
                        n--;
                    }
                    i -= width;
                }
                cols.push(n)
            }

            //  collapse empty columns
            for( var xx=0; xx<width*width; xx++ )
            {
                var x = xx%(width-1);
                if( cols[x] )
                    continue;
                var i = x;
                while( i<map.length )
                {
                    map[i]  = map[i+1];
                    map[i+1]= 0;
                    i       += width;
                }
                cols[x]     = cols[x+1];
                cols[x+1]   = 0;
            }

            //  FAIL ?
            var fail    = true,
                i       = map.length;
            while( fail && i-- )
            {
                if( color = map[i] )
                {
                    var x = i%width,
                        y = i-x;

                    if( fail && x>0 && color==map[i-1] )            fail = false;
                    if( fail && x<width-1 && color==map[i+1] )      fail = false;
                    if( fail && y>0 && color==map[i-width] )        fail = false;
                    if( fail && y<height-1 && color==map[i+width] ) fail = false;
                }
            }
            if( fail )
            {
                hGameArea.className = 'fail';
                isGameOver = true;
            }
        }

        //  update tiles' display
        var node    = hGameArea.firstChild,
            i       = 0;
        while( node )
        {
            var color = map[i++];
            if( color )
            {
                color = 'background-color:'+ color;
                if( node.getAttribute( 'style' )!=color )
                {
                    node.setAttribute( 'style', color );
                }
            }
            else if( node.hasAttribute( 'style' ) )
            {
                node.removeAttribute( 'style' );
            }
            node.className  = '';
            node = node.nextSibling;
        }

        //  WIN!?
        if( !tiles )
        {
            score   += 100; //  ULTRA WIN!
            hGameArea.className = 'win';
            isGameOver = true;
        }

        //  update score display and hiScore
        displayScore();

        if( isGameOver && score>hiScore )
        {
            hiScore = score;
            widget.preferences.setItem('hiScore', hiScore);
        }
        isGameOver = false;

        return true;
    }


    //  initialize
    var hScore      = $(scoreId),
        hGameArea   = $(gameAreaId),
        hHiScore    = $(hiScoreId);

    var hiScore;
    try{
	    hiScore = widget.preferences.getItem('hiScore');
	    if(hiScore == "undefined"){
	    	hiScore = 0;
	    }	    	
    }
    catch(err){
    	hiScore = 0;
    }

    if(window.addEventListener){
	    $(newGameId).addEventListener
	    (
	        'click',
	        this.newGame.bind(this),
	        false
	    );
    }
    else{
        $(newGameId).attachEvent
        (
            'onclick',
            this.newGame.bind(this)        
        );    	
    }

    this.newGame();
}
