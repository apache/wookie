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
package org.apache.wookie.services.redis;

import java.io.IOException;
import java.util.Properties;

import org.apache.wookie.services.AbstractSharedContextServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import redis.clients.jedis.Jedis;
import uk.co.datumedge.redislauncher.LocalRedisServer;
import uk.co.datumedge.redislauncher.RedisServer;

public class RedisSharedContextServiceTest extends AbstractSharedContextServiceTest {
	
	private static RedisServer redisServer;
	
	@BeforeClass
	public static void setup() throws IOException, InterruptedException{
		//
		// Set the path to the Redis executable so we can start a test instance
		//
		Properties props = System.getProperties();
		props.setProperty("redislauncher.command", "/usr/local/bin/redis-server");
		redisServer = LocalRedisServer.newInstance();
		redisServer.start();

		svc = new RedisSharedContextService();
		Jedis jedis = new Jedis("localhost");
		jedis.flushDB();
	}
	
	@AfterClass
	public static void tearDown() throws IOException, InterruptedException{
		svc = null;
		redisServer.stop();
	}
	
}
