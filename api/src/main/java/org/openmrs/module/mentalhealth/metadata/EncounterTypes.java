package org.openmrs.module.mentalhealth.metadata;

import org.openmrs.module.metadatadeploy.descriptor.EncounterTypeDescriptor;

public class EncounterTypes {

    public static EncounterTypeDescriptor INITIAL_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Mental Health Initial";
        }

        @Override
        public String description() {
            return "Used for collecting Initial mental health information";
        }

        public String uuid() {
            return "e7c5643e-9efe-11e8-a4c3-2b65da6977a7";
        }
    };

    public static EncounterTypeDescriptor FOLLOW_UP_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Mental Health Clinical Follow up";
        }

        @Override
        public String description() {
            return "Mental Health Follow up information";
        }

        public String uuid() {
            return "0b3012b6-9eff-11e8-a0a6-cb6dac4515ee";
        }
    };
}
