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
from xml.dom import minidom
import httplib
import urllib
httplib.HTTPConnection.debugLevel = 1
from WookieServerConnection import WookieServerConnection
from wookie.widget import Widget
from wookie.widget import Instance
from wookie.widget import Instances
from wookie.widget import User
from wookie.widget import Property


class WookieConnectorService:
    __connection = ""
    __widgetInstances = Instances.Instances()
    __currentUser = ""

    def __init__(self, wookieUrl, wookiePath = "", api_key = "", shareddatakey = ""):
        self.__connection = WookieServerConnection(wookieUrl, api_key, shareddatakey, wookiePath)

    # set current user
    # @param username, screenname

    def setUser(self, loginName, screenName = ""):
        if screenName == "":
            screenName = loginName
        self.__currentUser = User.User(loginName, screenName)

    # get current use
    # @return User object

    def getCurrentUser(self):
        return self.__currentUser

    # get current connection object
    # @return WookieServerConnection
    
    def getConnection(self):
        return self.__connection
        
    # get all available widgets
    # @return list of widgets
    
    def getAvailableWidgets(self):
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('GET', self.getConnection().getPath()+'/widgets?all=true')
        response = socket.getresponse()
        xmldoc = ''
        try:
            xmldoc = minidom.parseString(response.read());
        except Exception:
            print 'Error getting widgets XML'
        socket.close()
        
        ##define widgetList
        widgetList = []
        if xmldoc:
            widgetsAvailable = xmldoc.getElementsByTagName('widget')
            if widgetsAvailable:
                for widget in widgetsAvailable:
                    
                    ##get widget title
                    widgetTitle = self.getText(widget.getElementsByTagName('title'));
                        
                     ##get widget url
                    widgetGuid = widget.getAttribute('identifier')
                        
                    ##get widget description
                    widgetDesc = self.getText(widget.getElementsByTagName('description'));
                        
                    ##get widget icon
                    widgetIcon = self.getText(widget.getElementsByTagName('icon'));
                    
                    if widgetTitle and widgetGuid:
                      widgetList.append(Widget.Widget(widgetGuid, widgetTitle, widgetDesc, widgetIcon))
            else:
                print 'No widgets found'
        return widgetList

    # Function to get Text value from XML node
    # @param parentElement
    # @return string

    def getText(self, parentElement):
        try:
            node = parentElement[0].childNodes[0]
            if node.nodeType == node.TEXT_NODE:
                return node.nodeValue
        except IndexError:
            pass
        return ''

    # Get or create widget instance
    # @param Widget instance or plain text GUID
    # @return new widget instance

    def getOrCreateInstance(self, widget_or_guid):
        if widget_or_guid != str(widget_or_guid):
            guid = widget_or_guid.getGuid()
        else:
            guid = widget_or_guid
        if self.getCurrentUser().getLoginName() == "":
            print "Current user loginName value empty (\"\")"
            return
        if guid:
            params = urllib.urlencode({'api_key': self.getConnection().getApiKey(),
                                       'userid': self.getCurrentUser().getLoginName(),
                                       'shareddatakey': self.getConnection().getSharedDataKey(),
                                       'widgetid': guid})
            headers = {"Content-type": "application/x-www-form-urlencoded",
                       "Accept": "text/xml"}
            socket = httplib.HTTPConnection(self.getConnection().getUrl())
            socket.request('POST', self.getConnection().getPath()+'/widgetinstances', params, headers)
            try:
                response = socket.getresponse()
            except ResponseNotReady:
                response = socket.getresponse()
                
            if response.status == 201:
                response = socket.getresponse()
            instanceXml = response.read()
            socket.close()
            newInstance = self.parseInstance(instanceXml, guid)
            if newInstance:
                self.__widgetInstances.put(newInstance)
                self.addParticipant(newInstance, self.getCurrentUser())
                return newInstance
            else:
                return

    # Parse widget instance XML
    # @param xml, guid
    # @return new widget instance
        
    def parseInstance(self, xml, guid):
        newInstance = ''
        xmlDoc = ''
        try:
            xmlDoc = minidom.parseString(xml)
        except Exception:
            print 'Could not parse instance xml'
        if xmlDoc:
            url = self.getText(xmlDoc.getElementsByTagName('url'))
            title = self.getText(xmlDoc.getElementsByTagName('title'))
            height = self.getText(xmlDoc.getElementsByTagName('height'))
            width = self.getText(xmlDoc.getElementsByTagName('width'))
            isMaximizable = self.getText(xmlDoc.getElementsByTagName('maximize'))
            newInstance = Instance.Instance(url, guid, title, height, width, isMaximizable)
        return newInstance

    # Get list of participants
    # @param widget instance
    # @return List participants

    def getParticipants(self, widgetInstance):
        participantsList = []
        if widgetInstance == "":
            print "No widget instance"
            return
        queryString = '?api_key='+self.getConnection().getApiKey()
        queryString += '&userid='+self.getCurrentUser().getLoginName()
        queryString += '&shareddatakey='+self.getConnection().getSharedDataKey()
        queryString += '&widgetid='+widgetInstance.getGuid()
        
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('GET', self.getConnection().getPath()+'/participants'+queryString)
        try:
            response = socket.getresponse()
        except ResponseNotReady:
            response = socket.getresponse()

        if response.status == 200:
            xmlDoc = ""
            try:
                xmlDoc = minidom.parseString(response.read())
                participants = xmlDoc.getElementsByTagName("participant")
                if participants:
                    for participant in participants:
                        participant_id = participant.getAttribute("id")
                        display_name = participant.getAttribute("display_name")
                        thumbnailURL = participant.getAttribute("thumbnail_url")
                        newUser = User.User(participant_id, display_name)
                        newUser.setThumbnail(thumbnailURL)
                        participantsList.append(newUser)
            except Exception:
                print "Could not parse participants XML"
            
            return participantsList
        if response.status != 200:
            print "HTTP Status: "+response.status+"\nResponseText: "+response.read()
            return
        socket.close()
        return participantsList

    # Add participant to current widget instance
    # @param widgetInstance, userInstance
    # @return true if added, false otherwise
    
    def addParticipant(self, widgetInstance, userInstance):
        params = urllib.urlencode({'api_key': self.getConnection().getApiKey(),
                                   'userid': self.getCurrentUser().getLoginName(),
                                   'shareddatakey': self.getConnection().getSharedDataKey(),
                                   'widgetid': widgetInstance.getGuid(),
                                   'participant_id': userInstance.getLoginName(),
                                   'participant_display_name': userInstance.getScreenName(),
                                   'participant_thumbnail_url': userInstance.getThumbnail()})
        
        headers = {"Content-type": "application/x-www-form-urlencoded",
                       "Accept": "text/xml"}
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('POST', self.getConnection().getPath()+'/participants', params, headers)
        try:
            response = socket.getresponse()
        except ResponseNotReady:
            response = socket.getresponse()
        if response.status == 201:
            return 'true'
        if response.status == 200:
            return 'true'
        return 'false'

    # Delete participant
    # @param widgetIntance, userInstance
    # @return true if done, false if failed

    def deleteParticipant(self, widgetInstance, userInstance):
        if widgetInstance == "":
            print "No widget instance"
            return
        if userInstance == "":
            print "No user instance"
            return
        queryString = '?api_key='+self.getConnection().getApiKey()
        queryString += '&userid='+self.getCurrentUser().getLoginName()
        queryString += '&shareddatakey='+self.getConnection().getSharedDataKey()
        queryString += '&widgetid='+widgetInstance.getGuid()
        queryString += '&participant_id='+userInstance.getLoginName()
        
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('DELETE', self.getConnection().getPath()+'/participants'+queryString)
        try:
            response = socket.getresponse()
        except ResponseNotReady:
            response = socket.getresponse()

        if response.status == 200:
            return 'true'
        if response.status == 404:
            return 'false'

        print "deleteParticipant error: \n"+response.status
        return

    # Set widget property
    # @param widgetInstance, property instance
    # @return true or false

    def setProperty(self, widgetInstance, propertyInstance):
        if widgetInstance == "":
            print "No widget instance"
            return
        if propertyInstance == "":
            print "No property instance"
            return
        params = urllib.urlencode({'api_key': self.getConnection().getApiKey(),
                                   'userid': self.getCurrentUser().getLoginName(),
                                   'shareddatakey': self.getConnection().getSharedDataKey(),
                                   'widgetid': widgetInstance.getGuid(),
                                   'propertyname': propertyInstance.getName(),
                                   'propertyvalue': propertyInstance.getValue(),
                                   'is_public': propertyInstance.getIsPublic()})
        
        headers = {"Content-type": "application/x-www-form-urlencoded",
                       "Accept": "text/plain"}
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('POST', self.getConnection().getPath()+'/properties', params, headers)
        try:
            response = socket.getresponse()
        except ResponseNotReady:
            response = socket.getresponse()
        if response.status == 200:
            return 'true'
        if response.status == 201:
            return 'true'
        if response.status > 201:
            print "setProperty error: "+response.status
        return 'false'

    # Delete property
    # @param widgetInstance, property instance
    # @return true or false

    def deleteProperty(self, widgetInstance, propertyInstance):
        if widgetInstance == "":
            print "No widget instance"
            return
        if propertyInstance == "":
            print "No property instance"
            return
        queryString = '?api_key='+self.getConnection().getApiKey()
        queryString += '&userid='+self.getCurrentUser().getLoginName()
        queryString += '&shareddatakey='+self.getConnection().getSharedDataKey()
        queryString += '&widgetid='+widgetInstance.getGuid()
        queryString += '&propertyname='+propertyInstance.getName()
        
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('DELETE', self.getConnection().getPath()+'/properties'+queryString)
        try:
            response = socket.getresponse()
        except ResponseNotReady:
            response = socket.getresponse()
        if response.status == 200:
            return 'true'
        if response.status == 404:
            return 'false'
        print "deleteProperty error: "+response.status
        return 'false'

    # Get property
    # @param widget instance, property instance
    # @return retrieved property instance

    def getProperty(self, widgetInstance, propertyInstance):
        if widgetInstance == "":
            print "No widget instance"
            return
        if propertyInstance == "":
            print "No property instance"
            return
        queryString = '?api_key='+self.getConnection().getApiKey()
        queryString += '&userid='+self.getCurrentUser().getLoginName()
        queryString += '&shareddatakey='+self.getConnection().getSharedDataKey()
        queryString += '&widgetid='+widgetInstance.getGuid()
        queryString += '&propertyname='+propertyInstance.getName()
        
        socket = httplib.HTTPConnection(self.getConnection().getUrl())
        socket.request('GET', self.getConnection().getPath()+'/properties'+queryString)
        try:
            response = socket.getresponse()
        except ResponseNotReady:
            response = socket.getresponse()
        propertyValue = response.read()
        if response.status == 200:
            return Property.Property(propertyInstance.getName(), propertyValue)
        print "getProperty error: "+response.status
        return 'false'
