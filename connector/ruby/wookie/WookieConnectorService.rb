#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
# limitations under the License.
#

require 'wookie/WookieServerConnection.rb'
require 'wookie/widget/User.rb'
require 'wookie/widget/Widget.rb'
require 'wookie/widget/WidgetInstance.rb'

require 'net/http'
require 'uri'
require 'rexml/document'

class WookieConnectorService

    def initialize(host, apiKey, sharedDataKey, userName)
        @wookieConnection = WookieServerConnection.new(host, apiKey, sharedDataKey)
        @currentUser = User.new(userName);
    end

    def getConnection()
        return @wookieConnection
    end

    def getCurrentUser()
        return @currentUser
    end

    def getAvailableWidgets()
        widgets = Array.new
        xmlData = Net::HTTP.get_response(URI.parse(@wookieConnection.host+"/widgets?all=true")).body
        # widgets node, if not found return false
        begin
            doc = REXML::Document.new(xmlData)
            doc.elements.each('widgets/widget') do |widget|
                height = widget.attributes['height']
                width = widget.attributes['width']
                title = widget.elements['title'].text
                guid = widget.attributes['identifier']
                newWidget = Widget.new(title, guid, width, height)
                widgets.push(newWidget)
            end
        rescue REXML::ParseException=>e
            return widgets
        end
        return widgets
    end

    def getOrCreateWidgetInstance(guid)
        xmlData = Net::HTTP.post_form(URI.parse(@wookieConnection.host+'/widgetinstances'),
                              {'api_key'=>@wookieConnection.apiKey,
                               'shareddatakey'=> @wookieConnection.sharedDataKey,
                               'userid'=> self.getCurrentUser().loginName,
                               'widgetid'=> guid }).body
        begin
            doc = REXML::Document.new(xmlData)
            widgetNode = doc.elements['widgetdata']
            height = widgetNode.elements['height'].text
            width = widgetNode.elements['width'].text
            title = widgetNode.elements['title'].text
            url = widgetNode.elements['url'].text
            newInstance = WidgetInstance.new(title, url, width, height)
            return newInstance
        rescue REXML::ParseException=>e
            return nil
        end
        return nil
    end
end
