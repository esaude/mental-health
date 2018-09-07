package org.openmrs.module.mentalhealth.elements.interfaces;

import org.openmrs.module.htmlformentry.FormEntryContext;

public interface IPassthrough {
	public boolean handlesSubmission();
	public boolean requiresClosingTag();
	public String getTagName();
}
