package org.apache.wookie.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.services.impl.DefaultParticipantImpl;
import org.apache.wookie.services.impl.DefaultSharedDataImpl;
import org.junit.After;
import org.junit.Test;


public class AbstractSharedContextServiceTest {
	
	protected static SharedContextService svc;
	
	protected static final String API_KEY = "testapikey";
	protected static final String CONTEXT_ID = "23";
	protected static final String WIDGET_ID = "http://test.widget/";

	@After
	public void cleanUp(){
		svc.removeSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name");
		assertNull(svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name"));
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertNull(svc.getParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice"));
	}
	
	@Test
	public void setAndGet(){
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name", "test-value", false);
		ISharedData data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name");
		assertEquals("test-value", data.getDvalue());
	}
	
	@Test
	public void setAndGetObject(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID,input, false);
		
		ISharedData data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name");
		assertEquals("test-value", data.getDvalue());
	}
	
	@Test
	public void setAppendAndGetObject(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, input, false);
		ISharedData data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name");
		assertEquals("test-value", data.getDvalue());
		
		//
		// Append
		//
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name", "-append", true);
		ISharedData out = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name");
		assertEquals("test-value-append", out.getDvalue());
	}
	
	@Test
	public void setAndGetObjectFromArray(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID,input, false);
		
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals("test-value", data[0].getDvalue());
	}
	
	@Test
	public void removeNonexistant(){
		boolean out = svc.removeSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "rubbish");
		assertFalse(out);
	}
	
	@Test
	public void removeEmptyString(){
		boolean out = svc.removeSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "");
		assertFalse(out);
	}
	
	@Test
	public void setNull(){
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, null, false);
		assertFalse(out);
		
	}
	
	@Test
	public void appendNullToNull(){
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, null, true);
		assertFalse(out);
	}
	
	
	@Test
	public void appendDataToNull(){
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name", "append", true);
		assertTrue(out);
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals("append", data[0].getDvalue());
	}
	
	@Test
	public void appendWithNull(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID,input, false);
		
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, null, true);
		assertFalse(out);
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals("test-value", data[0].getDvalue());
	}
	
	@Test
	public void appendWithNullValue(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID,input, false);
		
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name", null, true);
		assertTrue(out);
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(0, data.length);
	}
	
	@Test
	public void append(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID,input, false);
		
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name", "-append", true);
		assertTrue(out);
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals("test-value-append", data[0].getDvalue());
	}
	
	@Test
	public void overwrite(){
		ISharedData input = new DefaultSharedDataImpl("test-name", "test-value");
		svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID,input, false);
		
		boolean out = svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-name", "new-value", false);
		assertTrue(out);
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals("new-value", data[0].getDvalue());
		

	}
	
	@Test
	public void removeNull(){
		assertFalse(svc.removeSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, null));
	}
	
	@Test
	public void addGetParticipant(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url"));
		IParticipant participant = svc.getParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("Alice", participant.getParticipantDisplayName());
		assertEquals("http://some.url", participant.getParticipantThumbnailUrl());
	}
	
	@Test
	public void addGetParticipants(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url"));
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://some.url"));

		IParticipant[] participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(2, participants.length);
		
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, participants.length);
	}
	
	@Test
	public void addGetHosts(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url", IParticipant.HOST_ROLE));
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://some.url"));

		IParticipant[] participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(2, participants.length);
		
		IParticipant[] hosts = svc.getHosts(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, hosts.length);
		assertEquals("alice", hosts[0].getParticipantId());
		
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, participants.length);
	}

	@Test
	public void addGetHost(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url", IParticipant.HOST_ROLE));
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://some.url"));

		IParticipant[] participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(2, participants.length);
		
		IParticipant host = svc.getHost(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals("alice", host.getParticipantId());
		
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, participants.length);
	}
	
	@Test
	public void getHostNull(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://some.url", "Beekeeper"));
		IParticipant[] participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, participants.length);
		IParticipant host = svc.getHost(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertNull(host);
		
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
	}
	
	@Test
	public void removeByObject(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url", IParticipant.HOST_ROLE));
		IParticipant participant = svc.getParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals(1, svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID).length);
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, participant);
		assertEquals(0, svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID).length);
	}
	
	@Test
	public void addGetViewer(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url", IParticipant.HOST_ROLE));
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob", "Bob", "http://some.url"));

		
		IParticipant viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
		assertEquals("bob", viewer.getParticipantId());
		
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "bob");
	}
	
	@Test
	public void addGetIncompleteViewer(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", null, IParticipant.HOST_ROLE));

		
		IParticipant viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
		assertNull(viewer.getParticipantThumbnailUrl());		
	}
	
	@Test
	public void addIncompleteParticipant(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", null,null,null));


		IParticipant viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
		assertNull(viewer.getParticipantThumbnailUrl());	
		assertNull(viewer.getParticipantDisplayName());	
	}
	
	@Test
	public void addParticipantNoId(){
		assertFalse(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, null, "don",null,null));
		assertFalse(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "", "carol",null,null));
	}
	
	@Test
	public void mixedDataAndParticipants(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", null, IParticipant.HOST_ROLE));
		assertTrue(svc.updateSharedData(API_KEY, WIDGET_ID, CONTEXT_ID, "test-data", "test-value", false));
		IParticipant[] participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, participants.length);
		ISharedData[] data = svc.getSharedData(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, data.length);
	}
	
	@Test
	public void overwriteParticipant(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alice", "http://some.url", IParticipant.HOST_ROLE));
		assertFalse(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", "Alicia", "http://some.url", IParticipant.HOST_ROLE));
		IParticipant[] participants = svc.getParticipants(API_KEY, WIDGET_ID, CONTEXT_ID);
		assertEquals(1, participants.length);		
		assertEquals("Alice", participants[0].getParticipantDisplayName());
	}

	@Test
	public void removeNullParticipant(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", null,null,null));
		IParticipant viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
		assertFalse(svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, ""));
		viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
	}
	
	@Test
	public void removeNullParticipantObject(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", null,null,null));
		IParticipant viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
		IParticipant remove = null;
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, remove);
		viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
	}
	
	@Test
	public void removeNullParticipantId(){
		assertTrue(svc.addParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, "alice", null,null,null));
		IParticipant viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
		IParticipant remove = new DefaultParticipantImpl(null, null, null, null);
		svc.removeParticipant(API_KEY, WIDGET_ID, CONTEXT_ID, remove);
		viewer = svc.getViewer(API_KEY, WIDGET_ID, CONTEXT_ID, "alice");
		assertEquals("alice", viewer.getParticipantId());
	}

}
