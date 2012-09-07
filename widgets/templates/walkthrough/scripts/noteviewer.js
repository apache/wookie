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

var NoteViewer =
{
    // TODO factor out to base class
    
    initPage: function()
    {
        if (this.model === undefined)
        {
            var question_file = "xml/questions.xml";
            var self = this;
            $.get(question_file, function(xml){ // load XML - need error handling
                self.model = $( xml ); 
                self.setHeaderFooter();
                var id = window.location.hash;
                self.showNote(id, {changeHash: false});
            });
        }
    },

    setHeaderFooter: function()
    {
        var title = this.model.find("question_set > title").text();
        $("#title").html(title); // Also set page title
        var copyright = this.model.find("question_set > copyright").text();
        $('#copyright').html(copyright); 
    },

    parseNote: function(id, node)
    {   // TODO factor this so DRY - appears in 2 files
        var note = this.model.find('note[id="'+id.slice(1)+'"]').first();
        var text = (note.length != 0) ? note.text() : "Cannot find a note with an id of '"+id+"'";
        var creole = new Parse.Simple.Creole( {
            forIE: document.all,
            interwiki: {
                WikiCreole: 'http://www.wikicreole.org/wiki/',
                Wikipedia: 'http://en.wikipedia.org/wiki/'
            },
            linkFormat: ''
            } );
        creole.parse(node, text);
    },
    
    showNote: function(id, options)
    {
        var note = $("#note");
        note.html('');
        this.parseNote(id, note[0]);

        var page = $("#page");
        page.page();

        var urlObj = $.mobile.path.parseUrl(id);  
        options.dataUrl = urlObj.href; // not page id
        options.allowSamePageTransition = true; // so hash get's updated in browser
        $.mobile.changePage(page, options);
    }
};


$("#page").live('pagebeforecreate',function(event)
{ 
  // called for URL without hash or F5 on any page
  NoteViewer.initPage();
});

// update page contents
$(document).bind( "pagebeforechange", function( e, data ) {
	if ( typeof data.toPage === "string" ) {
        if ($.mobile.activePage === undefined)
            // edge condition if F5 used on URL with hash 
            // allow default processing so get pagecreatebefore event
            return;

        var hash = $.mobile.path.parseUrl( data.toPage ).hash;
        NoteViewer.showNote( hash, data.options );
        e.preventDefault();
    }
});

