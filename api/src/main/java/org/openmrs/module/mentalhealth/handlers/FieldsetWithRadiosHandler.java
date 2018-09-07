package org.openmrs.module.mentalhealth.handlers;

import java.io.PrintWriter;

import org.openmrs.module.htmlformentry.BadFormDesignException;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.handler.AbstractTagHandler;
import org.openmrs.module.mentalhealth.elements.FieldsetElement;
import org.openmrs.module.mentalhealth.elements.PassthroughElement;
import org.w3c.dom.Node;

public class FieldsetWithRadiosHandler extends AbstractTagHandler {
	
	public FieldsetWithRadiosHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean doStartTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		FormEntryContext context = session.getContext();
		
		//instantiate a new instance of our observation controller adapter
		FieldsetElement elem = new FieldsetElement(context, getAttributes(node), node);
		
		//element implements FormSubmissionController interface
		session.getSubmissionController().addAction(elem);
		
		//if this is a fieldset with radios, it will output all it's child nodes,
		//and those child nodes should not render themselves
		//output
		out.print(elem.generateHtml(context));
		
		//should be self closing
		//element implements HTMLGeneratorElement interface
		context.pushToStack(elem);
		
		//Returns whether or not to handle the body also. (True = Yes)
		//we want the htmlformentrygenerator to call applyTagsHelper for child nodes
		return true;
	}

	@Override
	public void doEndTag(FormEntrySession session, PrintWriter out, Node parent, Node node)
			throws BadFormDesignException {
		FormEntryContext context = session.getContext();
		
		Object baseObj = context.popFromStack();
		//node.compareDocumentPosition(node.getOwnerDocument());
		if(!(baseObj instanceof FieldsetElement)) {
			throw new IllegalStateException("Element on top of stack wasn't a fieldset tag." + " Parameters: " + getAttributes(node));
		}
		
		PassthroughElement elem = (PassthroughElement) baseObj;
		
		try {
			out.print(elem.closeTag());
		} catch (Exception e) {
			throw new IllegalArgumentException("Error when writing closing <fieldset> tag. element was "+ elem + " Parameters: " + getAttributes(node));
		}

	}

}
