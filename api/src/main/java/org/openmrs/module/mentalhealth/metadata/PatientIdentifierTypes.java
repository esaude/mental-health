package org.openmrs.module.mentalhealth.metadata;

import org.openmrs.module.metadatadeploy.descriptor.PatientIdentifierTypeDescriptor;

public class PatientIdentifierTypes {

    public static PatientIdentifierTypeDescriptor AIHD_PATIENT_NUMBER = new PatientIdentifierTypeDescriptor() {
        @Override
        public String name() {
            return "AIHD Patient Number";
        }

        @Override
        public String description() {
            return "This is the patient's identifier used at the treating facility";
        }

        public String uuid() {
            return "b9ba3418-7108-450c-bcff-7bc1ed5c42d1";
        }

        public String formatDescription() {
            return "Description will be needed later";
        }
    };

    public static PatientIdentifierTypeDescriptor MOBILE_NUMBER = new PatientIdentifierTypeDescriptor() {
        @Override
        public String name() {
            return "AIHD Patient mobile number";
        }

        @Override
        public String description() {
            return "This is the mobile number for te patient";
        }

        public String uuid() {
            return "d0929ad2-f87a-11e7-80ee-672bf941f754";
        }

        public String formatDescription() {
            return "At least 10 digits are required";
        }
    };

    public static PatientIdentifierTypeDescriptor DOPC_MOPC_NUMBER = new PatientIdentifierTypeDescriptor() {
        @Override
        public String name() {
            return "DOPC MOPC Number";
        }

        @Override
        public String description() {
            return "This is the DOPC MOPC number for te patient";
        }

        public String uuid() {
            return "82609a20-f87b-11e7-bf96-cbcf7771ff78";
        }

        public String formatDescription() {
            return "Will be determined";
        }
    };
}
