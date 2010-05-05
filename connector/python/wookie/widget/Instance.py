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
class Instance:
    __url = ''
    __guid = ''
    __title = ''
    __height = ''
    __width = ''

    def __init__(self, newUrl, newGuid, newTitle, newHeight, newWidth):
        self.setUrl(newUrl)
        self.setGuid(newGuid)
        self.setTitle(newTitle)
        self.setHeight(newHeight)
        self.setWidth(newWidth)

    def setUrl(self, newUrl):
        self.__url = newUrl

    def setGuid(self, newGuid):
        self.__guid = newGuid

    def setTitle(self, newTitle):
        self.__title = newTitle

    def setHeight(self, newHeight):
        self.__height = newHeight

    def setWidth(self, newWidth):
        self.__width = newWidth

    def getUrl(self):
        return self.__url

    def getGuid(self):
        return self.__guid

    def getTitle(self):
        return self.__title

    def getHeight(self):
        return self.__height

    def getWidth(self):
        return self.__width
