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
Accessibility = {

        stylesheet: '',

        defaultsize: 0,

        colour2: '',

        colour3: '',

        colour4: '',
        
        sizes: [77,85,93,77,85,93,100,108,116,123.1,131,138.5,146.5,153.9,161.6,167,174,182,189,197],

        watch: null,

	doc: '',

        init: function(Y, autoload_atbar) {
            this.Y = Y;
	    Y.one('#requiresjavascript').hide();
            //sheetnode = Y.one('link[href='+M.cfg.wwwroot+'/blocks/accessibility/userstyles.php]');
            //this.stylesheet = Y.StyleSheet(sheetnode);
	    this.doc = window.parent.document;
	    
            this.colour2 = Y.Node.create('<style id="a11ysheet" type="text/css">*{background-color: #ffc !important;background-image:none !important;}</style>');
            this.colour3 = Y.Node.create('<style id="a11ysheet" type="text/css">*{background-color: #9cf !important;background-image:none !important;}</style>');
            this.colour4 = Y.Node.create('<style id="a11ysheet" type="text/css">*{background-color: #000 !important;background-image:none !important;color: #ff0 !important;}a{color: #f00 !important;}.block_accessibility .outer{border-color: white;}</style>');
            this.defaultsize = widget.preferences.getItem('size');
            if (typeof(currentsize) == "undefined" || currentsize == null) {
                this.defaultsize = 100;
            }
	    console.log(Y.one(this.doc.body).getStyle('fontSize'));
            Y.one(this.doc.body).setStyle('fontSize', this.defaultsize+'%');
      
            // Attach the click handler
            Y.all('#block_accessibility_textresize a').on('click', function(e) {
                            Accessibility.changesize(e.target);
            });

	    
            Y.all('#block_accessibility_changecolour a').on('click', function(e) {
                            Accessibility.changecolour(e.target);
            });

            Y.one('#atbar_auto').on('click', function(e) {
                if (e.target.get('checked')) {
                    Accessibility.atbar_autoload('on');
                } else {
                    Accessibility.atbar_autoload('off');
                }
            });

            // Remove href attributes from anchors
            Y.all('.block_accessibility .outer').each(function(node){
                node.removeAttribute('href');
            });

            // Create Bookmarklet-style link using code from ATbar site
            // http://access.ecs.soton.ac.uk/StudyBar/versions
            var launchbutton = Y.one('#block_accessibility_launchtoolbar');
            launchbutton.on('click', function() {            
               /* (d = document;
                lf = d.createElement('script');
                lf.type = 'text/javascript';
                lf.id = 'ToolbarStarter';
                lf.text = 'var StudyBarNoSandbox = true';
                d.getElementsByTagName('head')[0].appendChild(lf);
                jf = d.createElement('script');
                jf.src = M.cfg.wwwroot+'/blocks/accessibility/toolbar/client/JTToolbar.user.js';
                jf.type = 'text/javascript';
                jf.id = 'ToolBar';
                d.getElementsByTagName('head')[0].appendChild(jf);
                // Hide block buttons until ATbar is closed
                Y.one('#block_accessibility_textresize').setStyle('display', 'none');
                Y.one('#block_accessibility_changecolour').setStyle('display', 'none');
                Accessibility.watch_atbar_for_close();*/
                alert('Load ATbar');
            });

            if (autoload_atbar) {            
                /*d = document;
                lf = d.createElement('script');
                lf.type = 'text/javascript';
                lf.id = 'ToolbarStarter';
                lf.text = 'var StudyBarNoSandbox = true';
                d.getElementsByTagName('head')[0].appendChild(lf);
                jf = d.createElement('script');
                jf.src = M.cfg.wwwroot+'/blocks/accessibility/toolbar/client/JTToolbar.user.js';
                jf.type = 'text/javascript';
                jf.id = 'ToolBar';
                d.getElementsByTagName('head')[0].appendChild(jf);
                // Hide block buttons until ATbar is closed
                Y.one('#block_accessibility_textresize').setStyle('display', 'none');
                Y.one('#block_accessibility_changecolour').setStyle('display', 'none');
                setTimeout("Accessibility.watch_atbar_for_close();", 1000); // Wait 1 second to give the bar a chance to load*/
                alert('Autoload ATBar');
            }
            
        },


        /**
         * Displays the specified message in the block's footer
         *
         * @param {String} msg the message to display
         */
        show_message: function(msg) {
            this.Y.one('#block_accessibility_message').setContent(msg);
        },

        /**
         * Calls the database script on the server to save the current setting to
         * the database. Displays a message on success, or an error on failure.
         *
         * @requires accessibility_show_message
         * @requires webroot
         *
         */
        savesize: function() {
            /*this.Y.io(M.cfg.wwwroot+'/blocks/accessibility/database.php', {
                data: 'op=save&size=true&scheme=true',
                method: 'get',
                on: {
                    success: function(id, o) {
                        Accessibility.show_message(M.util.get_string('saved', 'block_accessibility'));
                        setTimeout("Accessibility.show_message('')", 5000);
                    },
                    failure: function(id, o) {
                        alert(M.util.get_string('jsnosave', 'block_accessibility')+' '+o.status+' '+o.statusText);
                    }
                }
            });*/
            alert('Save size');
        },

        /**
         * Calls the database script on the server to clear the current size setting from
         * the database. Displays a message on success, or an error on failure. 404 doesn't
         * count as a failure, as this just means there's no setting to be cleared
         *
         * @requires show_message()
         *
         */
        resetsize: function() {
            /*this.Y.io(M.cfg.wwwroot+'/blocks/accessibility/database.php', {
                data: 'op=reset&size=true',
                method: 'get',
                on: {
                    success: function(id, o) {
                        Accessibility.show_message(M.util.get_string('reset', 'block_accessibility'));
                        setTimeout("Accessibility.show_message('')", 5000);
                    },
                    failure: function(id, o) {
                        if (o.status != '404') {
                            alert(M.util.get_string('jsnosizereset', 'block_accessibility')+' '+o.status+' '+o.statusText);
                        }
                    }
               }
            });*/
            alert('Reset Size');
        },

        /**
         * Calls the database script on the server to clear the current colour scheme setting from
         * the database. Displays a message on success, or an error on failure. 404 doesn't
         * count as a failure, as this just means there's no setting to be cleared
         *
         * @requires show_message()
         *
         */
         resetscheme: function() {
            /*this.Y.io(M.cfg.wwwroot+'/blocks/accessibility/database.php', {
                data: 'op=reset&scheme=true',
                method: 'get',
                on: {
                    success: function(id, o) {
                        Accessibility.show_message(M.util.get_string('reset', 'block_accessibility'));
                        setTimeout("Accessibility.show_message('')", 5000);
                    },
                    failure: function(id, o) {
                        if (o.status != '404') {
                            alert(M.util.get_string('jsnocolourreset', 'block_accessibility')+': '+o.status+' '+o.statusText);
                        }
                    }
               }
            });*/
        },

        /**
         * Enables or disables the buttons as specified
         *
         * @requires webroot
         *
         * @param {String} id the ID of the button to enable/disable
         * @param {String} op the operation we're doing, either 'on' or 'off'.
         *
         */
        toggle_textsizer: function(id, op) {
            var button = this.Y.one('#block_accessibility_'+id);
            if (op == 'on') {
                if (button.hasClass('disabled')) {
                    button.removeClass('disabled');
                }
                if (button.get('id') == 'block_accessibility_save') {
                    button.get('firstElementChild').get('firstElementChild').set('src', M.cfg.wwwroot+'/blocks/accessibility/pix/document-save.png');
                }
            } else if (op == 'off') {
                if(!button.hasClass('disabled')) {
                    button.addClass('disabled');
                }
                if (button.get('id') == 'block_accessibility_save') {
                    button.get('firstElementChild').get('firstElementChild').set('src', M.cfg.wwwroot+'/blocks/accessibility/pix/document-save-grey.png');
                }
            }
        },

        /**
         * This handles clicks from the buttons. If increasing, decreasing or
         * resetting size, it calls changesize.php via AJAX and sets the text
         * size to the number returned from the server. If saving the size, it
         * calls accessibility_savesize.
         * Also enables/disables buttons as required when sizes are changed.
         *
         * @requires accessibility_toggle_textsizer
         * @requires accessibility_savesize
         * @requires accessibility_resetsize
         * @requires stylesheet
         * @requires webroot
         *
         * @param {Node} button the button that was pushed
         *
         */
        changesize: function(button) {
            Y = this.Y;
            var currentsize = widget.preferences.getItem('size');
            if (typeof(currentsize) == "undefined" || currentsize == null) {
                currentsize = this.defaultsize;
            }
            
            switch (button.get('id')) {
                case "block_accessibility_inc":
		    if (currentsize < 197) {
			var newsize = this.sizes[this.sizes.indexOf(currentsize)+1];
			widget.preferences.setItem('size', newsize);
			Y.one(window.parent.document.body).setStyle('fontSize', newsize+'%')
		    }
                    break;
		    
                case "block_accessibility_dec":
		    if (currentsize > 77) {
			var newsize = this.sizes[this.sizes.indexOf(currentsize)-1];
			widget.preferences.setItem('size', newsize);
			Y.one(window.parent.document.body).setStyle('fontSize', newsize+'%');
		    }
                    break;
		    
                case "block_accessibility_reset":
                    var newsize = 100;                    
                    widget.preferences.setItem('size', null);
                    Y.one(window.parent.document.body).setStyle('fontSize', newsize+'%');
                    break;

                case "block_accessibility_save":
                    Accessibility.savesize();
                    break;
            
            }
        },

        /**
         * This handles clicks from the colour scheme buttons.
         * We start by getting the scheme number from the theme button's ID.
         * We then get the elements that need dynamically re-styling via their
         * CSS selectors and loop through the arrays to style them appropriately.
         *
         * @requires accessibility_toggle_textsizer
         * @requires accessibility_resetscheme
         * @requires stylesheet
         * @requires webroot
         *
         * @param {String} button - the button that was clicked.
         *
         */

        changecolour: function(button) {
            Y = this.Y;
            scheme = button.get('id').substring(26);

	    switch (scheme) {
		case '1':
		    var head = Y.one(this.doc.head);
		    head.removeChild(head.one('#a11ysheet')); 
		    break;
		    
		case '2':
		    Y.one(this.doc.head).appendChild(this.colour2);
		    break;

		case '3':
		    Y.one(this.doc.head).appendChild(this.colour3);
		    break;

		case '4':
		    Y.one(this.doc.head).appendChild(this.colour4);
		break;
	    }
        },

        atbar_autoload: function(op) {
            if (op == 'on') {
            } else if (op == 'off') {
            }
        },

        watch_atbar_for_close: function() {
            Y = this.Y;
            this.watch = setInterval(function() {
                if (!Y.one('#sbar')) {
                    Y.one('#block_accessibility_textresize').setStyle('display', 'block');
                    Y.one('#block_accessibility_changecolour').setStyle('display', 'block');
                    clearInterval(Accessibility.watch);
                }
            }, 1000);
        }
    }
