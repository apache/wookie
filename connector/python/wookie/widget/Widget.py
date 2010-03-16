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
class Widget:
        __title = ''
        __guid = ''
        __desc = ''
        __icon = ''
        
        def __init__ (self, guid, title, desc, icon):
                self.setTitle(title)
                self.setGuid(guid)
                self.setDescription(desc)
                self.setIcon(icon)
                
        def getTitle(self):
            return self.__title
        
        def getGuid(self):
            return self.__guid
        
        def getDescription(self):
            return self.__desc
        
        def getIcon(self):
            return self.__icon

        def setTitle(self, title):
                self.__title = title

        def setGuid(self, url):
                self.__guid = url

        def setDescription(self, desc):
                self.__desc = desc

        def setIcon(self, iconUrl):
                self.__icon = iconUrl
