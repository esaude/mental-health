package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.PassthroughElement;
import org.openmrs.module.mentalhealth.elements.TextAreaElement;
import org.w3c.dom.Node;

public class TextAreaHandler extends AbstractTagHandler {
	
	protected Log log = LogFactory.getLog(getClass());
	
	public TextAreaHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doStartTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		log.info("In TextAreaHandler doStartTag");
		
		FormEntryContext context = session.getContext();
		
		//instantiate a new instance of our observation controller adapter
		TextAreaElement elem = new TextAreaElement(context, getAttributes(node), node);
		
		//element implements FormSubmissionController interface
		session.getSubmissionController().addAction(elem);
		
		//output
		out.print(elem.generateHtml(context));
		
		//keep track of nested tags
		context.pushToStack(elem);
		
		//if in view mode a child text node is added to the actual dom
		//so the generator will parse that node as normal
		//Returns whether or not to handle the body also. (True = Yes)
		return true;
	}

	@Override
	public void doEndTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		log.info("In TextAreaHandler doEndTag");

		FormEntryContext context = session.getContext();
		
		Object baseObj = context.popFromStack();
		//node.compareDocumentPosition(node.getOwnerDocument());
		if(!(baseObj instanceof TextAreaElement) ) {
			throw new IllegalStateException("Element on top of stack wasn't a textarea tag." + " Parameters: " + getAttributes(node));
		}
		
		PassthroughElement elem = (PassthroughElement) baseObj;
		
		try {
			log.info("In TextAreaHandler doEndTag, closeTag"+elem.closeTag());
			out.print(elem.closeTag());
		} catch (Exception e) {
			throw new IllegalArgumentException("Error when writing closing <textarea> tag. element was "+ elem + " Parameters: " + getAttributes(node));
		}
	}

}
