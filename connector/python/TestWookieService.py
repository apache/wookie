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
from wookie import WookieConnectorService
from wookie.widget import Property
from wookie.widget import User

# @param host, path, api_key, shareddatakey
WookieConn = WookieConnectorService.WookieConnectorService('localhost:8080', '/wookie', 'TEST','localhost_python')
# set Widget user
WookieConn.setUser("demo_python", "demo_python_screenName")

print WookieConn.getCurrentUser().getLoginName()
print WookieConn.getCurrentUser().getScreenName()

# params: name, value, isPublic (default: false)
prop1 = Property.Property("test", "proov")
print prop1.getName()+":"+prop1.getValue()+":"+prop1.getIsPublic()

# get available widgets
widgetList = WookieConn.getAvailableWidgets()

a = widgetList[12]
b = widgetList[11]
#a = 'http://www.getwookie.org/widgets/weather'   

# create or get instances

retrievedInstance = WookieConn.getOrCreateInstance(a)
if retrievedInstance:
    print retrievedInstance.getTitle()+'\n'+retrievedInstance.getUrl()
    print retrievedInstance.getWidth()+'\n'+retrievedInstance.getHeight()

    # add participant, returns string "true" (if exists, or created) or "false"
    print WookieConn.addParticipant(retrievedInstance, User.User("ants_python", "ants_screenName"))
    
    # get participants, return list of participants
    users = WookieConn.getParticipants(retrievedInstance)
    print users[0].getLoginName()+":"+users[0].getScreenName()+":"+users[0].getThumbnail()

    # delete participant
    WookieConn.deleteParticipant(retrievedInstance, User.User("ants_python"));

    # add property
    # params: name, value, is_public
    prop2 = Property.Property("python_prop", "demo")
    WookieConn.setProperty(retrievedInstance, prop2)

    # get property, return property objec, if failed then "false"
    retrievedProp = WookieConn.getProperty(retrievedInstance, prop2)
    print retrievedProp.getValue()
    
    #delete property, returns "true" or "false"
    WookieConn.deleteProperty(retrievedInstance, prop2)

retrievedInstance2 = WookieConn.getOrCreateInstance(b)
if retrievedInstance2:
    print retrievedInstance2.getTitle()+'\n'+retrievedInstance2.getUrl()
    print retrievedInstance2.getWidth()+'\n'+retrievedInstance2.getHeight()


retrievedInstance3 = WookieConn.getOrCreateInstance('http://www.getwookie.org/widgets/weather')
if retrievedInstance3:
    print retrievedInstance3.getTitle()+'\n'+retrievedInstance3.getUrl()
    print retrievedInstance3.getWidth()+'\n'+retrievedInstance3.getHeight()
