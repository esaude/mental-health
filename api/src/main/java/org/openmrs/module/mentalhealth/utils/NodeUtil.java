package org.openmrs.module.mentalhealth.utils;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeUtil {

	static public String stringify(Node node) {
		StringWriter sw = new StringWriter();
		
		if(node == null)
			return "";
		
		try {
			//state wont persist, but the performance impact of creating this here,
			//rather than in the ctor may lead to poor overall performance,
			//TransformerConfigurationException can be thrown during creation
			Transformer t = TransformerFactory.newInstance().newTransformer();
		
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//t.setOutputProperty(OutputKeys.INDENT, "yes");
			
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException e) {
			System.out.printf("MentalHealth Module Transformer Exception: %s", e.getMessage());
		}
		
		return sw.toString();
	}
	
	static public String[] generateSeparateTags(Node originalNode) {
		
		Node clonedNode = originalNode.cloneNode(true);
	    
	    NodeList allChildren = clonedNode.getChildNodes();
	    
		int originalChildCount = allChildren.getLength();
	    
	    for(int i=0; i<originalChildCount; i++) {
	        Node child = allChildren.item(0);
	        
	        clonedNode.removeChild(child);
	    }
	    
	    Node test = clonedNode.getOwnerDocument().createTextNode("\n");
	    
	    clonedNode.appendChild(test);
	    
		return NodeUtil.stringify(clonedNode).split("\n");
	}
	
	//do not allow construction of this class
	private NodeUtil() {
		// TODO Auto-generated constructor stub
	}

}
