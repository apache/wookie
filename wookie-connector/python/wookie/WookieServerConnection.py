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

import httplib;
from xml.dom import minidom

class WookieServerConnection:
    __url = ''
    __api_key = ''
    __shareddatakey = ''
    __wookiePath = '/wookie'

    def __init__(self, wookieUrl, api_key, shareddatakey, wookiePath = ''):
        self.__url = wookieUrl
        self.__api_key = api_key
        self.__shareddatakey = shareddatakey
        if wookiePath != '':
            self.__wookiePath = wookiePath

    def getUrl(self):
        return self.__url

    def getPath(self):
        return self.__wookiePath

    def getApiKey(self):
        return self.__api_key

    def getSharedDataKey(self):
        return self.__shareddatakey

    def Test(self):
        socket = httplib.HTTPConnection(self.getUrl())
        socket.request('GET', self.getPath()+'/widgets?all=true')
        response = socket.getresponse()
        try:
            xmlDoc = minidom.parseString(response.read())
            anyWidgetsFound = xmlDoc.getElementsByTagName('widget')
            if anyWidgetsFound:
                return "1"
            else:
                return
        except Exception:
            return
        socket.close()
