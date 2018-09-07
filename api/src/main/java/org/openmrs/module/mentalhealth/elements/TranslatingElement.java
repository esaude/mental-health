package org.openmrs.module.mentalhealth.elements;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEnter;
import org.openmrs.module.mentalhealth.utils.NodeUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TranslatingElement extends PassthroughElement implements IHandleHTMLEnter {

	static final protected String m_serverLocale =  Context.getLocale().toString();

	protected Node m_originalNode;
	protected Node m_nodeClone;
	
	protected boolean m_translate = false;

	protected ArrayList<AbstractMap.SimpleEntry<String, Node>> m_toTranslate;
	
	public TranslatingElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);

		//m_translate = m_parameters.get("data-translate").equals("true");
		m_translate = false;
		
		if(m_translate) {
			findTranslationKeys();
		}
	}
	
	private void findTranslationKeys() {
		
		m_nodeClone = m_originalNode.cloneNode(true);
		
		NodeList list = m_nodeClone.getChildNodes();
		
		for( int i=0; i < list.getLength(); i++ )
		{
			Node childNode = list.item(i);
			 
			if(childNode.getNodeType()==Node.TEXT_NODE)
			{
				m_toTranslate.add(
						new AbstractMap.SimpleEntry<String, Node>( 
								childNode.getNodeValue(), 
								childNode)
						);
				
			}
		}
	}

	@Override
	public void takeActionForEnterMode(FormEntryContext context) {
		
		if(m_translate) 
		{
			try {
				
				for(AbstractMap.SimpleEntry<String, Node> translationPair : m_toTranslate) {
					//lookup translation of provided textContent
					String translatedString = context.getTranslator().translate(m_serverLocale, translationPair.getKey());
					
					Node originalChild = translationPair.getValue();
					
					Node changeling = originalChild.cloneNode(true);
					
					changeling.setNodeValue(translatedString);
					
					m_nodeClone.replaceChild(changeling, originalChild);
				}
				
				//return NodeUtil.stringify(m_nodeClone);
				
			}catch(Exception e) {
				System.out.printf("MentalHealth Module Translation Exception: %s", e.getMessage());
			}
		}

		//return super.generateHtml(context);
	}

}
