/*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
* limitations under the License.
*/
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace WookieService.Wookie
{
    class Widget
    {
        private String title;
        private String icon;
        private String desc;
        private String guid;

        public Widget(String guid, String title, String desc, String icon)
        {
            this.setGuid(guid);
            this.setTitle(title);
            this.setDescription(desc);
            this.setIcon(icon);
        }

        public void setGuid(String guid) 
        {
            this.guid = guid;
        }

        public String getGuid() 
        {
            return this.guid;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }

        public void setDescription(String desc)
        {
            this.desc = desc;
        }

        public String getDescription()
        {
            return this.desc;
        }

        public void setIcon(String icon)
        {
            this.icon = icon;
        }

        public String getIcon()
        {
            return this.icon;
        }

    }
}
