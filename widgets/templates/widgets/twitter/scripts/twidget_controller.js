<%
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
 %>

/*
 * Change created times into friendly strings like "2 minutes ago"
 * using the TimeAgo JQuery plugin. Also "linkify" any @screenname
 * found in tweets
 *
 * We trigger this on document
 * load and whenever the results are updated.
 */
$(document).ready(function() {
 $('abbr.timeago').timeago();
 linkify();
 $('body').bind('results_updated', function() {
   $('abbr.timeago').timeago();
   linkify();
 });

});


/*
 * Replace @screen_name in tweets with links that update the results with that user's timeline
 */
function linkify(){
  $('.tweet_text').each(function(){
     var html = $(this).text().replace(/(^|)@(\w+)/gi, function (s) {
        return '<a href="#" onclick="${widget.shortname}_browse_controller.search(\''+s+'\')">'+s+'</a>';
     });
     $(this).html(html);
  });
}