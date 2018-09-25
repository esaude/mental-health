package org.openmrs.module.mentalhealth.metadata;

import org.openmrs.module.metadatadeploy.descriptor.PatientIdentifierTypeDescriptor;

public class MentalHealthPatientIdentifierTypes {

    public static PatientIdentifierTypeDescriptor MH_NID= new PatientIdentifierTypeDescriptor(){

        @Override
        public String uuid() {
            return "689a4f84-aa03-11e8-adff-6fec648a8d89";
        }

        @Override
        public String name() {
            return "MENTAL HEALTH NID";
        }

        @Override
        public String description() {
            return "Patient Identifier to represent a patient in mental health program";
        }
    };
}
