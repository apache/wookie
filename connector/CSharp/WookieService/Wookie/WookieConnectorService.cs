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
using System.IO;
using System.Net;
using System.Xml;

namespace WookieService.Wookie
{
    class WookieConnectorService
    {
        private WookieServerConnection wookieConn;

        public WookieConnectorService(String url, String api_key, String sharedDataKey)
        {
            this.wookieConn = new WookieServerConnection(url, api_key, sharedDataKey);
        }

        public WookieServerConnection getConnection()
        {
            return this.wookieConn;
        }

        public String getCurrentUser() {
            return "csharp_user";
        }

        public WidgetInstance getOrCreateInstance(String guid)
        {
            WebRequest postReq = WebRequest.Create(this.getConnection().getUrl()+"/widgetinstances");
            postReq.Proxy = null;

            //Add these, as we're doing a POST
            postReq.ContentType = "application/x-www-form-urlencoded";
            postReq.Method = "POST";

            StringBuilder postData = new StringBuilder();
            postData.Append("api_key=" + this.getConnection().getApiKey());
            postData.Append("&userid=" + this.getCurrentUser());
            postData.Append("shareddatakey=" + this.getConnection().getSharedDataKey());
            postData.Append("&widgetid=" + guid);

            //We need to count how many bytes we're sending. Post'ed Faked Forms should be name=value&
            byte[] bytes = Encoding.UTF8.GetBytes(postData.ToString());
            postReq.ContentLength = bytes.Length;
            Stream os = postReq.GetRequestStream();
            os.Write(bytes, 0, bytes.Length); //Push it out there
            os.Close();
            WebResponse resp = postReq.GetResponse();
            if (resp == null) return null;
            StreamReader sr = new StreamReader(resp.GetResponseStream());

            //let's read xml
            XmlDocument doc = new XmlDocument();
            String response = sr.ReadToEnd().Trim();
            doc.LoadXml(response);
            String widgetInstanceUrl = doc.GetElementsByTagName("url")[0].InnerText;;
            String widgetTitle = doc.GetElementsByTagName("title")[0].InnerText;;
            Int32 widgetHeight = Int32.Parse(doc.GetElementsByTagName("height")[0].InnerText);
            Int32 widgetWidth = Int32.Parse(doc.GetElementsByTagName("width")[0].InnerText);
            String widgetMaximize = doc.GetElementsByTagName("maximize")[0].InnerText;;

          
            WidgetInstance newInstance = new WidgetInstance(widgetInstanceUrl, guid, widgetTitle, 
                                                            widgetHeight, widgetWidth, widgetMaximize);
            return newInstance;            
        }

        public List<Widget> getAvailableWidgets()
        {
            // Create a request for the URL.         
            WebRequest request = WebRequest.Create(this.getConnection().getUrl() + "/widgets?all=true");

            request.Proxy = null;

            // Get the response.
            HttpWebResponse response = (HttpWebResponse)request.GetResponse();

            // Display the status.
            // Console.WriteLine (response.StatusDescription);

            // Get the stream containing content returned by the server.
            Stream dataStream = response.GetResponseStream();

            // Open the stream using a StreamReader for easy access.
            StreamReader reader = new StreamReader(dataStream);

            string responseFromServer = reader.ReadToEnd();

            // Cleanup the streams and the response.
            reader.Close();
            dataStream.Close();
            response.Close();

            XmlDocument doc = new XmlDocument();
            doc.LoadXml(responseFromServer);
            XmlNodeList elements = doc.GetElementsByTagName("widget");

            List<Widget> widgets = new List<Widget>();

            foreach (XmlElement widget in elements) {
                String guid = widget.GetAttribute("identifier");
                String title = "";
                String description = "";
                String iconUrl = "";

                XmlNodeList children = widget.ChildNodes;
                foreach (XmlElement child in children)
                {
                    switch(child.Name) {
                        case "title":
                            title = child.InnerText;
                            break;
                        case "description":
                            description = child.InnerText;
                            break;
                        case "icon":
                            iconUrl = child.InnerText;
                            break;
                    }
                }
                Widget newWidget = new Widget(guid, title, description, iconUrl);
                widgets.Add(newWidget);
            }

            return widgets;
        }
    }
}
