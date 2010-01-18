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
package org.apache.wookie.util.html;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentToken;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;

/**
 * This is a custom serializer for HtmlCleaner that does not escape the content of
 * event handler attributes such as "onClick". In other respects it is identical 
 * to SimpleXmlSerializer.
 */
public class HtmlSerializer extends XmlSerializer {
	
	/**
	 * The set of HTML event handler attributes
	 */
	private static final String[] EVENT_HANDLERS = {"onabort","onbeforeunload","onblur","onchange","oncontextmenu","onclick","ondblclick","ondragdrop","ondrag","ondragend","ondragenter","ondragleave","ondragover","ondragstart","ondrop","onerror","onfocus","onkeydown","onkeypress","onkeyup","onload","onmessage","onmousedown","onmouseup","onmousemove","onmouseout","onmouseover","onmouseup","onmousewheel","onmove","onreset","onresize","onscroll","onselect","onstorage","onsubmit","onunload" };

	public HtmlSerializer(CleanerProperties props){
		super(props);
	}
	
	/**
	 * Checks to see if an attribute should have its value escaped 
	 * @param attname the attribute name
	 * @return true if the attribute shouldn't be escaped, otherwise false
	 */
	protected boolean dontEscapeAttribute(String attname){
		for (String handler:EVENT_HANDLERS) if (handler.equalsIgnoreCase(attname)) return true;
		return false;
	}
	
	/**
	 * We only override two lines of this method - see below
	 */
	@Override
    protected void serializeOpenTag(TagNode tagNode, Writer writer, boolean newLine) throws IOException {
        String tagName = tagNode.getName();
        Map tagAtttributes = tagNode.getAttributes();
        
        writer.write("<" + tagName);
        Iterator it = tagAtttributes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String attName = (String) entry.getKey();
            String attValue = (String) entry.getValue();
            
            if ( !props.isNamespacesAware() && ("xmlns".equals(attName) || attName.startsWith("xmlns:")) ) {
            	continue;
            }
            // This is a line we've changed
            writer.write(" " + attName + "=\"" + (dontEscapeAttribute(attName)? attValue: escapeXml(attValue)) + "\"");
        }
        
        if ( isMinimizedTagSyntax(tagNode) ) {
        	writer.write(" />");
        	if (newLine) {
        		writer.write("\n");
        	}
        } else if (dontEscape(tagNode)) {
        	// And so is this
        	writer.write(">");
        } else {
        	writer.write(">");
        }
    }
	
	@Override
    protected void serializeEndTag(TagNode tagNode, Writer writer, boolean newLine) throws IOException {
    	String tagName = tagNode.getName();
    	
    	// Lets not bother with this shall we?
    	//if (dontEscape(tagNode)) {
    	//	writer.write("]]>");
    	//}
    	
    	writer.write( "</" + tagName + ">" );

        if (newLine) {
    		writer.write("\n");
    	}
    }

	/**
	 * This is exactly the same as SimpleXmlSerializer.serialize, however we have to include it here as it would
	 * inherit from XmlSerializer and miss out our custom dontEscapeAttribute method
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void serialize(TagNode tagNode, Writer writer) throws IOException {
        serializeOpenTag(tagNode, writer, false);

        List tagChildren = tagNode.getChildren();
        if ( !isMinimizedTagSyntax(tagNode) ) {
            Iterator childrenIt = tagChildren.iterator();
            while ( childrenIt.hasNext() ) {
                Object item = childrenIt.next();
                if (item != null) {
                    if ( item instanceof ContentToken ) {
                        String content = ((ContentToken) item).getContent();
                        writer.write( dontEscape(tagNode) ? content.replaceAll("]]>", "]]&gt;") : escapeXml(content) );
                    } else {
                        ((BaseToken)item).serialize(this, writer);
                    }
                }
            }

            serializeEndTag(tagNode, writer, false);
        }
    }

}
