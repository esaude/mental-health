package org.openmrs.module.aihdconfigs.metadata;

import org.openmrs.module.metadatadeploy.descriptor.PersonAttributeTypeDescriptor;

public class PersonAttributeTypes {

    public static PersonAttributeTypeDescriptor SERVICE_DELIVERY_POINT = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "0a93cbc6-5d65-4886-8091-47a25d3df944";
        }

        @Override
        public String name() {
            return "Service Delivery Point";
        }

        @Override
        public String description() {
            return "USed for collecting at which point a patient was seen";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor TREATMENT_SUPPORTER_NAME = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "14d07597-d618-4f58-baab-d921e43f0a4c";
        }

        @Override
        public String name() {
            return "Treatment Supporter Name";
        }

        @Override
        public String description() {
            return "Treatment Supporter Name details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor TELEPHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "14d4f066-15f5-102d-96e4-000c29c2a5d7";
        }

        @Override
        public String name() {
            return "Telephone Number";
        }

        @Override
        public String description() {
            return "The telephone number for the person";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };
}
