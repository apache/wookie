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
    class WidgetInstance
    {
        private String url;
        private String guid;
        private String title;
        private Int32 height;
        private Int32 width;
        private String maximize;

        public WidgetInstance(String url, String guid, String title, Int32 height, Int32 width, String maximize) {
            this.setIdentifier(guid);
            this.setUrl(url);
            this.setTitle(title);
            this.setHeight(height);
            this.setWidth(width);
            this.setMaximize(maximize);
        }
      
        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIdentifier() {
            return this.guid;
        }

        public void setIdentifier(String guid) {
            this.guid = guid;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Int32 getHeight() {
            return this.height;
        }

        public void setHeight(Int32 height) {
            this.height = height;
        }

        public Int32 getWidth() {
            return this.width;
        }

        public void setWidth(Int32 width) {
            this.width = width;
        }

        public String getMaximize() {
            return this.maximize;
        }

        public void setMaximize(String maximize) {
            this.maximize = maximize;
        }
        public String isMaximizable() {
            return this.getMaximize();
        }
    }
}
