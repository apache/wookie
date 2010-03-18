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

namespace WookieService.Wookie
{
    class WookieServerConnection
    {
        private String url;
        private String api_key;
        private String shareddatakey;

        public WookieServerConnection(String url, String api_key, String sharedDataKey) {
            this.setUrl(url);
            this.setApiKey(api_key);
            this.setSharedDataKey(sharedDataKey);
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl()
        {
            return this.url;
        }

        public void setApiKey(String apikey)
        {
            this.api_key = apikey;
        }

        public String getApiKey()
        {
            return this.api_key;
        }

        public void setSharedDataKey(String sharedDataKey)
        {
            this.shareddatakey = sharedDataKey;
        }

        public String getSharedDataKey()
        {
            return this.shareddatakey;
        }

    }
}
