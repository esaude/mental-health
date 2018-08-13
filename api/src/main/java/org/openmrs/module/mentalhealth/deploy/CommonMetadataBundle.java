package org.openmrs.module.mentalhealth.deploy;

import org.openmrs.module.mentalhealth.metadata.EncounterTypes;
import org.openmrs.module.mentalhealth.metadata.PatientIdentifierTypes;
import org.openmrs.module.mentalhealth.metadata.PersonAttributeTypes;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

@Component
public class CommonMetadataBundle extends AbstractMetadataBundle {

    /**
     * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
     */
    public void install() throws Exception {

        // install the patient identifier types


        // install person attribute types
        log.info("Installing PatientAttributeTypes");


        log.info("Person AttributeTypes installed");

        //Install Encounter Type
        log.info("Installing EncounterTypes");
        install(EncounterTypes.INITIAL_ENCOUNTER_TYPE);
        install(EncounterTypes.FOLLOW_UP_ENCOUNTER_TYPE);


        //installing programs metadata
        log.info("Installing Programs");

        //install Locations

    }
}
