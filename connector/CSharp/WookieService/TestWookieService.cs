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
using WookieService.Wookie;

namespace TestWookieService
{
    class TestWookieService
    {
        static void Main(string[] args)
        {
            WookieConnectorService conn = new WookieConnectorService("http://localhost:8080/wookie", "TEST", "csharp_localhost");
            List<Widget> widgets = conn.getAvailableWidgets();
            foreach (Widget widget in widgets) {
            	Console.WriteLine(widget.getTitle() + " - " + widget.getGuid()+ " - " + widget.getDescription());
            }

            WidgetInstance retrievedInstance_1 = conn.getOrCreateInstance("http://www.getwookie.org/widgets/weather");
            WidgetInstance retrievedInstance_2 = conn.getOrCreateInstance(widgets[3].getGuid());

            Console.WriteLine("Instance 1 - "+retrievedInstance_1.getTitle());
            Console.WriteLine("Instance 2 - " + retrievedInstance_2.getTitle());
			// pause to read
			Console.WriteLine("Press return to continue");
			Console.ReadLine();
        }
    }
}
