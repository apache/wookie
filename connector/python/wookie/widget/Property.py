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
class Property:
    __propertyName = ""
    __propertyValue = ""
    __isPublic = "false"

    def __init__(self, propertyName, propertyValue, isPublic = ""):
        self.setName(propertyName)
        self.setValue(propertyValue)
        if isPublic != "":
            self.setIsPublic(isPublic)

    def setName(self, name):
        self.__propertyName = name

    def setValue(self, value):
        self.__propertyValue = value

    def setIsPublic(self, isPublic):
        self.__isPublic = isPublic

    def getName(self):
        return self.__propertyName

    def getValue(self):
        return self.__propertyValue

    def getIsPublic(self):
        return self.__isPublic
