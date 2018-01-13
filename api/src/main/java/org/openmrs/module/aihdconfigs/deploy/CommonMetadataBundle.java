package org.openmrs.module.aihdconfigs.deploy;

import org.openmrs.module.aihdconfigs.metadata.EncounterTypes;
import org.openmrs.module.aihdconfigs.metadata.PatientIdentifierTypes;
import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

@Component
public class CommonMetadataBundle extends AbstractMetadataBundle {

    /**
     * @see org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle#install()
     */
    public void install() throws Exception {

        // install the patient identifier types
        log.info("Installing PatientIdentifierTypes");
        install(PatientIdentifierTypes.AIHD_PATIENT_NUMBER);
        install(PatientIdentifierTypes.MOBILE_NUMBER);
        install(PatientIdentifierTypes.DOPC_MOPC_NUMBER);
        log.info("Patient IdentifierTypes installed");

        // install person attribute types
        log.info("Installing PatientAttributeTypes");

        log.info("Person AttributeTypes installed");

        //Install Encounter Type
        log.info("Installing EncounterTypes");
        install(EncounterTypes.DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE);
        install(EncounterTypes.DM_HTN_INITIAL_ENCOUNTER_TYPE);

        //installing programs metadata
        log.info("Installing Programs");

        //install Locations

    }
}
