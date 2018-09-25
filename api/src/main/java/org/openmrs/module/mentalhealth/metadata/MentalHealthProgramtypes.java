package org.openmrs.module.mentalhealth.metadata;

import org.openmrs.module.metadatadeploy.descriptor.ProgramDescriptor;

public class MentalHealthProgramtypes {

    public static ProgramDescriptor MH_PROGRAM = new ProgramDescriptor() {
        @Override
        public String conceptUuid() {
            return "e1e68670-1d5f-11e0-b929-000c29ad1d07";
        }

        @Override
        public String name() {
            return "MENTAL HEALTH PROGRAM";
        }

        @Override
        public String description() {
            return "Mental health program management";
        }

        @Override
        public String uuid() {
            return "cb1e24be-aa03-11e8-a5b5-f34e18407f07";
        }
    };
}
