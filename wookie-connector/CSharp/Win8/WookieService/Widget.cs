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

namespace WookieService
{
    public sealed class Widget
    {
        private String title;
        private String icon;
        private String desc;
        private String guid;

        public Widget(String guid, String title, String desc, String icon, String path, String contentFile)
        {
            this.Guid = guid;
            this.Title = title;
            this.Description = desc;
            this.Icon = icon;
            this.Path = path;
            this.ContentFile = contentFile;
        }

        public String Guid { get; set; }
        public String Title { get; set; }
        public String Description { get; set; }
        public String Icon { get; set; }
        public String Path { get; set; } // the path to the widget files
        public String ContentFile { get; set; } // the filename for the content file

        /// <summary>
        /// Get the source location of this widget. This can be used as a URI to retrieve the widget.
        /// </summary>
        public String Source 
        {
            get
            {
                return "ms-appx-web:///widget/" + this.Path + "/" + this.ContentFile;
            }
        }
    }
}
