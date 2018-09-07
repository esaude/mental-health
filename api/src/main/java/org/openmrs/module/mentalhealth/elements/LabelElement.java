package org.openmrs.module.mentalhealth.elements;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.openmrs.module.mentalhealth.utils.NodeUtil;
import org.openmrs.util.LocaleUtility;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.openmrs.api.context.Context;

public class LabelElement extends TranslatingElement implements IPassthrough {

	public LabelElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);
		
	}

	@Override
	public boolean handlesSubmission() {
		return false;
	}

	@Override
	public boolean requiresClosingTag() {
		return false;
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "label";
	}
	
}
