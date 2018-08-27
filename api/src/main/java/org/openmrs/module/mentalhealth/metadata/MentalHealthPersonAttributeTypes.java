package org.openmrs.module.mentalhealth.metadata;

import org.openmrs.module.metadatadeploy.descriptor.PersonAttributeTypeDescriptor;

public class MentalHealthPersonAttributeTypes {

    public static PersonAttributeTypeDescriptor TELEPHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public double sortWeight() {
            return 1.0;
        }

        @Override
        public String name() {
            return "Telephone Number";
        }

        @Override
        public String description() {
            return "The patient phone number";
        }

        @Override
        public String uuid() {
            return "11192de8-aa03-11e8-ac0e-7b2acb24e59c";
        }
    };

}
