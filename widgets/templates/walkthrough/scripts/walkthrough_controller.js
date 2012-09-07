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

var ${widget.shortname}_walkthrough_controller = 
{
    parseQueryString: function()
    {
        var nvpair = {};
        var qs = window.location.search.replace('?', '');
        var pairs = qs.split('&');
        $.each(pairs, function(i, v)
        {
            var pair = v.split('=');
            nvpair[pair[0]] = pair[1];
        });
        return nvpair;
    },

    initPage: function()
    {
        var question_file = this.parseQueryString().questions || "xml/questions.xml";
        var self = this;
        if (self.model === undefined)
        {
            $.get(question_file, function(xml){ // load XML - FIXME need error handling
                self.model = $( xml ); 
                self.setHeaderFooter();
                var page = window.location.hash; // might be F5 on a URI with hash
                $.mobile.changePage(page, {changeHash: false});
            });
        }
    },

    setHeaderFooter: function()
    {
        var title = this.model.find("question_set > title").text();
        $("#title").html(title); // Also set page title
                
        this.setupSave();
    },
    
    showQuestion: function(url, options)
    {
        if (url == '')
        {
            url = '#'+this.model.find("question:first").attr("id");
        }

        var urlObj = $.mobile.path.parseUrl(url);
        var options = options || {};

        // fill in question
        var idQ = urlObj.hash.slice(1);
        var question = this.model.find('question[id="'+idQ+'"]').first();
        if (question.length == 0)
        {
            alert("Cannot find a question with an id of '"+idQ+"'");
            return;
        }
        this.showQuestionText(question);
        this.showAnswerText(question);

        var page = $("#home");
        page.page();
        //var content = page.children( ":jqmData(role=content)" );
        
        //$("#question_body").find( ":jqmData(role=button)" )button();
        //answers.listview('refresh');

        options.dataUrl = urlObj.href;          // not current page id
        options.allowSamePageTransition = true; // so hash get's updated in browser
        options.changeHash = true;              // Needed so back works in IE from 1st page change
        $.mobile.changePage(page, options);     // As per JQM example - doesn't recusive bomb   
	},
   
    isNote: function(href)
    {   
        return href != '#' && (href.slice(0, 7).toLowerCase() != 'http://'.toLowerCase());
    },
      
    showQuestionText: function (question)
    {
	    $("#question_title").html(question.find("title").text());

        var qhtml = '';
        // create question with link to add actions
        question.find("body").contents().each( function() {
            var that = $(this);
            if (this.nodeType == 8) // comment
                ; // nothing
            else if (this.nodeType == 3) // text
                qhtml += that.text();
            else // todo_item
            {
                var type = that.attr('type');
                if (type == 'TODO')
                {
                    var href = ''; // made up protocol we can match against - won't degrade
                }
                else
                {
                    var href = that.attr('href');
                }
                var text = that.text();
                qhtml += that.attr('type')+": <a href='#' data-href='"+href+"'>"+text+"</a>";
            }
        });
	    $("#question_body").html(qhtml);

        var todo = $("#todo_list");
        var self = this;
        
        // hook up buttons to add items to actions list.
        var button_links = $("#question_body").find('a');
        button_links.bind('click', function(event) 
        {   
            var href = $(event.target).data('href');
            var text = $(event.currentTarget).text();
            if (href == '') 
            {
                var item = "<li>TODO: "+text;
            }
            else
            {
                var target = href;
                if (self.isNote(href))
                {
                    // is internal note
                    target = 'noteviewer.html#'+href; // arguable should be ?note= and not hash
                }
                var item = "<li>TOREAD: <a href='"+target+"' target='_blank'>"+text+"</a>";
            }
            item += " <a title='Delete this action' class='action_delete' href='#'>[X]</a></li>";
            todo.append(item);
            
            // add a remove link
            var a_new = todo.find('li > a').last(); 
            a_new.bind('click', function(event) {
                
                $(event.target.parentNode).remove();
                return false;
            });
            
            //todo.listview('refresh');
            return false;
        });
    },

    showAnswerText: function (question)
    {
        $("#answer_text").html(question.find("navigation > text").text());
        
        var answers = $("#answer_links");

        // update answers
        answers.find('li').remove();
        question.find("navigation > answer").each( function(i, el) {
           var next_id = $(el).attr("next");
           var next_text = $(el).text();
           var html = "<li><a href='#"+next_id+"'>"+next_text+"</a></li>";
           answers.append(html);
        });

        // hook up answers to show next question
        var answer_links = answers.find('a');
        answer_links.bind('click', function(event) 
        {
            $.mobile.changePage(event.target.href, {}); 
            return false;
        });
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
    
    setupSave: function()
    {
        var self = this;
        
        // save button action
        $("#save").bind('click', function(event) 
        {   
            var todo = $("#todo_list");
            var items = todo.find('li');
            var actions = [];
            items.each( function(i, el)
            {
                var jqthis = $(this);
                var action = {};
                
                action.text = jqthis.text().replace(/\[X\]?/g, ''); // lose those close links
                var href = jqthis.find("a").first().attr('href');
                if (href != undefined && href != '#' && !self.isNote(href))
                    action.href = href;
                else if (self.isNote(href))
                {
                    var id = $.mobile.path.parseUrl( href ).hash;
                    //var note = self.model.find('note[id="'+id.slice(1)+'"]').first();
                    //var notetext = (note.length != 0) ? note.text() : "Cannot find a note with an id of '"+id+"'";
                    var noteNode = $("<div/>");
                    self.parseNote(id, noteNode[0]);
                    action.notetext = noteNode.html(); 
                }
                actions.push(action); 
                i++;
            });
            if (actions.length)
			{
                self.actions = actions;                            // so actions.html can access them
                window.open("actions.html", "actions", "", false); // Use this so target page can use 'opener' to access this conext 
                                                                   // alternative is HTML5 session data with window.name hack fallback
            }
            return false;
        });
   }
};

$("#home").live('pagebeforecreate',function(event)
{ 
  // called for URL without hash or F5 on any page
  ${widget.shortname}_walkthrough_controller.initPage();
});


// update page contents
$(document).bind( "pagebeforechange", function( e, data ) {
	if ( typeof data.toPage === "string" ) {
        if ($.mobile.activePage === undefined)
            // edge condition if F5 used on URL with hash 
            // allow default processing so get pagecreatebefore event
            return;

        var hash = $.mobile.path.parseUrl( data.toPage ).hash;
        ${widget.shortname}_walkthrough_controller.showQuestion( hash, data.options );
        e.preventDefault();
    }
});
