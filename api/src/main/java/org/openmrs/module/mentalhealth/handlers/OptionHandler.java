package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;

import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.SelectElement;
import org.openmrs.module.mentalhealth.elements.OptionElement;
import org.openmrs.module.mentalhealth.elements.PassthroughElement;
import org.w3c.dom.Node;

public class OptionHandler extends AbstractTagHandler {

	public OptionHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doStartTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		//make sure the last element pushed onto the stack was select, we'll need
		//it to continue
		
		FormEntryContext context = session.getContext();
		
		//no peeking
		Object parentElement = context.popFromStack();
		
		//just needed to peek
		context.pushToStack(parentElement);
		
		if(parentElement instanceof SelectElement) {
			SelectElement parentSelectElement = (SelectElement) parentElement;
			
			OptionElement elem = new OptionElement(context, getAttributes(node), node, parentSelectElement);
			
			context.pushToStack(elem);
			
			out.print(elem.generateHtml(context));
			
		}else {
			throw new IllegalArgumentException("Options immediate ancestor should be a select tag! Option with attrs: " + getAttributes(node));
		}
		
		//handles text node children
		return true;
	}

	@Override
	public void doEndTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		
		FormEntryContext context = session.getContext();
		
		Object baseObj = context.popFromStack();
		//node.compareDocumentPosition(node.getOwnerDocument());
		if(!(baseObj instanceof OptionElement)) {
			throw new IllegalStateException("Element on top of stack wasn't an option tag. Option with attrs: " + getAttributes(node));
		}
		
		PassthroughElement elem = (PassthroughElement) baseObj;
		
		try {
			out.print(elem.closeTag());
		} catch (Exception e) {
			throw new IllegalArgumentException("Error when writing closing <option> tag. element was "+ elem + " Option with attrs: " + getAttributes(node));
		}
	}

}
