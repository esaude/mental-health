/**
 * 
 */
package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.CheckboxElement;
import org.openmrs.module.mentalhealth.elements.DateElement;
import org.openmrs.module.mentalhealth.elements.FieldsetElement;
import org.openmrs.module.mentalhealth.elements.InputElement;
import org.openmrs.module.mentalhealth.elements.NumberInputElement;
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
		try {
		
		FormEntryContext context = session.getContext();
		
		Map<String, String> attrs = getAttributes(node);
		
		InputElement elem = (InputElement)createElement(session, context, attrs, node);
		
		//output
		out.print(elem.generateHtml(context));
		
		//should be self closing
		//context.pushToStack(elem);
		
	}catch(Exception e) {
		throw new IllegalStateException("Exception in InputHandler.doStartTag(): " + ExceptionUtils.getStackTrace(e));
	}

		
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
		
		
		//HTML specs (inputs default to type text if blank or unknown)
		if(inputType == null || inputType.isEmpty()) {
			inputType = "text";
			attrs.put("type", "text");
		}
		//throw new IllegalArgumentException("input type " + inputType + " type.equals(radio)" + (inputType.equals("radio")?"true":"false") );
		
		FieldsetElement parentFieldset = context.getHighestOnStack(FieldsetElement.class);
		
		if(inputType.equals("radio")) {

			elem = new RadioElement(session, attrs, node, parentFieldset);
			
		} else if (inputType.equals("date")) {
			
			elem = new DateElement(session, attrs, node, parentFieldset);
			
			if( elem.handlesSubmission() ) {
				session.getSubmissionController().addAction(elem);
			}
			
		}else if(inputType.equals("checkbox")) {
			
			elem = new CheckboxElement(session, attrs, node, parentFieldset);
			
			if( elem.handlesSubmission()) {
				session.getSubmissionController().addAction(elem);
			}
		}else if(inputType.equals("number")) {
			
			elem = new NumberInputElement(session, attrs, node);
			session.getSubmissionController().addAction(elem);
			
		}
		//instantiate a new instance of our observation controller adapter
		else {
			elem = new InputElement(session, attrs, node);
			session.getSubmissionController().addAction(elem);
		}
		
		
		return elem;
	}

}
