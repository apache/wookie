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

class WookieServerConnection
    attr_accessor :host, :apiKey, :sharedDataKey

    def initialize(host, apiKey, sharedDataKey)
        @host = host
        @apiKey = apiKey
        @sharedDataKey = sharedDataKey
    end

    def Test()
        require 'net/http'
        require 'rexml/document'
        xmlData = Net::HTTP.get_response(URI.parse(@host+"/widgets?all=true")).body
        # widgets node, if not found return false
        begin
            doc = REXML::Document.new(xmlData)
            anyWidgetsFound = doc.elements['widgets']
            if anyWidgetsFound:
                return true
            else
                return false
            end
        rescue REXML::ParseException=>e
            return false
        end

    end
end

