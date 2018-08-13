package org.openmrs.module.mentalhealth.metadata;

import org.openmrs.module.metadatadeploy.descriptor.EncounterTypeDescriptor;

public class EncounterTypes {

    public static EncounterTypeDescriptor DM_HTN_INITIAL_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "DM HTN Initial";
        }

        @Override
        public String description() {
            return "Used for collecting Initial encounter information";
        }

        public String uuid() {
            return "bf3f3108-f87c-11e7-913d-5f679b8fdacb";
        }
    };

    public static EncounterTypeDescriptor DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Diabetic Clinical Follow up";
        }

        @Override
        public String description() {
            return "Follow up information";
        }

        public String uuid() {
            return "2da542a4-f87d-11e7-8eb4-37dc291c1b12";
        }
    };

    public static EncounterTypeDescriptor DISCONTINUE_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Discontinue patient";
        }

        @Override
        public String description() {
            return "Discontinue patient from care from this facility";
        }

        public String uuid() {
            return "d67972c4-75f5-11e8-9e72-9738ea3173f2";
        }
    };
    
    public static EncounterTypeDescriptor ADMISSION_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Admit patient";
        }

        @Override
        public String description() {
            return "Admit patient into this facility";
        }

        public String uuid() {
            return "e22e39fd-7db2-45e7-80f1-60fa0d5a4378";
        }
    };

    public static EncounterTypeDescriptor FOOT_CLINIC_ENCOUNTER_TYPE = new EncounterTypeDescriptor() {
        @Override
        public String name() {
            return "Foot clinic";
        }

        @Override
        public String description() {
            return "Patient foot examination and care";
        }

        public String uuid() {
            return "3b1f9415-8e00-46b7-b52b-f6c5075d6e30";
        }
    };
}
