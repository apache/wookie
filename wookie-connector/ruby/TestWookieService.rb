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

require 'wookie/WookieConnectorService.rb'

service = WookieConnectorService.new("http://localhost:8081/wookie/", "TEST", "ruby_localhost", "demo_ruby_user")

print service.getCurrentUser().screenName

alive = service.getConnection().Test()
if alive:
    puts service.getConnection().host
    service.getAvailableWidgets().each do |widget|
        widgetInstance = service.getOrCreateWidgetInstance(widget.guid)
        puts widgetInstance.url
        puts widgetInstance.width
        puts widgetInstance.height
        break
    end
else
    print "Connection failed!"
end
