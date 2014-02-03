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

package org.apache.wookie.server;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Stop {

  static final private Logger logger = Logger.getLogger(Stop.class);
  private static int shutdownPort = 8079;
  
	public static void main(String[] args) throws Exception {
	  for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      logger.info("Runtime argument: " + arg);
      if (arg.startsWith("shutdownport=")) {
        try {
          shutdownPort = new Integer(arg.substring(13));
        } catch (Exception e) {
          logger.error("Unable to parse shutdown port:" + arg.substring(13));
        }
        logger.info("The shutdown port is: "+shutdownPort);
        break;
      }
	  }
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), shutdownPort);
		OutputStream out = s.getOutputStream();
		logger.info("*** sending jetty stop request");
		out.write(("\r\n").getBytes());
		out.flush();
		s.close();
	}
}