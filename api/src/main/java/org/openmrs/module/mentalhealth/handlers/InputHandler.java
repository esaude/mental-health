/**
 * 
 */
package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;
import java.util.Map;

import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.FieldsetElement;
import org.openmrs.module.mentalhealth.elements.InputElement;
import org.openmrs.module.mentalhealth.elements.PassthroughElement;
import org.openmrs.module.mentalhealth.elements.RadioElement;

import org.w3c.dom.Node;

/**
 * @author longk
 *
 */
public class InputHandler extends AbstractTagHandler {

	/**
	 * 
	 */
	public InputHandler() {
		// TODO Auto-generated constructor stub
	}


	//this tag handler just creates the controller/htmlGenerator and returns
	//the original HTML, it acts as a mapping between the internal concept and
	//the provided input name 
	@Override
	public boolean doStartTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		FormEntryContext context = session.getContext();
		
		Map<String, String> attrs = getAttributes(node);
		
		InputElement elem = (InputElement)createElement(session, context, attrs, node);
		
		//output
		out.print(elem.generateHtml(context));
		
		//should be self closing
		//context.pushToStack(elem);
		
		//Returns whether or not to handle the body also. (True = Yes)
		//inputs should have no child elements
		return false;
	}

	@Override
	public void doEndTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		//inputs are self closing, do nothing
		
	}


	public PassthroughElement createElement(FormEntrySession session, FormEntryContext context, Map<String, String> attrs, Node node) {
		
		InputElement elem = null;
			
		String inputType = attrs.get("type");
		
		//throw new IllegalArgumentException("input type " + inputType + " type.equals(radio)" + (inputType.equals("radio")?"true":"false") );
		
		if(inputType.equals("radio")) {
			
			FieldsetElement parentFieldset = context.getHighestOnStack(FieldsetElement.class);
			
			elem = new RadioElement(context, attrs, node, parentFieldset);
		}
		//instantiate a new instance of our observation controller adapter
		else {
			elem = new InputElement(context, attrs, node);
			session.getSubmissionController().addAction(elem);
		}
		
		return elem;
	}

}
