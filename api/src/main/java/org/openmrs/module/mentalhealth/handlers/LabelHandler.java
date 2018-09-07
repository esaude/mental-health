package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;

import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.LabelElement;
import org.openmrs.module.mentalhealth.elements.PassthroughElement;
import org.w3c.dom.Node;

public class LabelHandler extends AbstractTagHandler {
	
	public LabelHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doStartTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		FormEntryContext context = session.getContext();
		
		//instantiate a new instance of the HTML generator for this element
		LabelElement elem = new LabelElement(context, getAttributes(node), node);
		
		//Also adds any necessary FormSubmissionControllerActions to the FormSubmissionController associated with the session.
		
		//labels have nothing to do with form submission
		//element implements FormSubmissionController interface
		//session.getSubmissionController().addAction(elem);
		
		//Generates the appropriate HTML and adds it to the associated PrintWriter
		//output
		out.print(elem.generateHtml(context));
		
		//element implements HTMLGeneratorElement interface
		context.pushToStack(elem);
		
		//Returns whether or not to handle the body also. (True = Yes)
		//the child elements should also be processed individually by their respective handlers
		return true;
	}

	@Override
	public void doEndTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		FormEntryContext context = session.getContext();
		
		Object baseObj = context.popFromStack();
		//node.compareDocumentPosition(node.getOwnerDocument());
		if(!(baseObj instanceof LabelElement)) {
			throw new IllegalStateException("Element on top of stack wasn't a label tag." + " Parameters: " + getAttributes(node));
		}
		
		PassthroughElement elem = (PassthroughElement) baseObj;
		
		try {
			out.print(elem.closeTag());
		} catch (Exception e) {
			throw new IllegalArgumentException("Error when writing closing <label> tag. element was "+ elem + " Parameters: " + getAttributes(node));
		}
	}

}
