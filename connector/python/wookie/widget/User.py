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
class User:

    __loginName = "UNKOWN"
    __screenName = "UNKOWN"
    __thumbnailURL = ""
    
    def __init__(self, loginName, screenName = ""):
        self.setLoginName(loginName)
        if screenName == "":
            screenName = loginName
        self.setScreenName(screenName)

    def setLoginName(self, loginName):
        self.__loginName = loginName

    def setScreenName(self, screenName):
        self.__screenName = screenName

    def setThumbnail(self, newUrl):
        self.__thumbnailURL = newUrl;

    def getLoginName(self):
        return self.__loginName

    def getScreenName(self):
        return self.__screenName

    def getThumbnail(self):
        return self.__thumbnailURL
