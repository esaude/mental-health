package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.PassthroughElement;
import org.openmrs.module.mentalhealth.elements.SelectElement;
import org.w3c.dom.Node;

//
//using IteratingTagHandler would allow this element to process all the node children
//but it may keep the logic simpler to leave select and option as separate classes

public class SelectHandler extends AbstractTagHandler {

	public SelectHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doStartTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		try {
		FormEntryContext context = session.getContext();
		
		//instantiate a new instance of our observation controller adapter
		SelectElement elem = new SelectElement(session, getAttributes(node), node);
		
		//element implements FormSubmissionController interface
		session.getSubmissionController().addAction(elem);
		
		//output
		out.print(elem.generateHtml(context));
		
		//keep track of nested tags
		context.pushToStack(elem);
		
		}catch(Exception e) {
			throw new IllegalStateException("Exception in SelectHandler.doStartTag(): " + ExceptionUtils.getStackTrace(e));
		}

		//all child option tags will add themselves using this tags element on the context stack
		return true;
	}

	@Override
	public void doEndTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		try {
		FormEntryContext context = session.getContext();
		
		Object baseObj = context.popFromStack();
		//node.compareDocumentPosition(node.getOwnerDocument());
		if(!(baseObj instanceof SelectElement)) {
			throw new IllegalStateException("Element on top of stack wasn't a select tag." + " Parameters: " + getAttributes(node));
		}
		
		PassthroughElement elem = (PassthroughElement) baseObj;
		
		try {
			out.print(elem.closeTag());
		} catch (Exception e) {
			throw new IllegalArgumentException("Error when writing closing <select> tag. element was "+ elem + " Parameters: " + getAttributes(node));
		}
		}catch(Exception e) {
			throw new IllegalStateException("Exception in SelectHandler.doEndTag(): " + ExceptionUtils.getStackTrace(e));
		}

	}

}
