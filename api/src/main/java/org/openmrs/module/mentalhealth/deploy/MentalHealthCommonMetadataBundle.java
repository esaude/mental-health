package org.openmrs.module.mentalhealth.deploy;

import org.openmrs.module.mentalhealth.metadata.MentalHealthEncounterTypes;
import org.openmrs.module.mentalhealth.metadata.MentalHealthPatientIdentifierTypes;
import org.openmrs.module.mentalhealth.metadata.MentalHealthPersonAttributeTypes;
import org.openmrs.module.mentalhealth.metadata.MentalHealthProgramtypes;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

@Component
public class MentalHealthCommonMetadataBundle extends AbstractMetadataBundle {

    /**
     * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
     */
    public void install() throws Exception {

        // install the patient identifier types


        //instal person attribute types here
        log.info("Person AttributeTypes installed");
        install(MentalHealthPersonAttributeTypes.TELEPHONE_NUMBER);

        //Install Encounter Type
        log.info("Installing MentalHealthEncounterTypes");
        install(MentalHealthEncounterTypes.INITIAL_ENCOUNTER_TYPE);
        install(MentalHealthEncounterTypes.FOLLOW_UP_ENCOUNTER_TYPE);


        //installing programs metadata
        log.info("Installing Programs");
        install(MentalHealthProgramtypes.MH_PROGRAM);

        //install identifier types
        log.info("Installing patient identifier types");
        install(MentalHealthPatientIdentifierTypes.MH_NID);

    }
}
