package org.openmrs.module.mentalhealth.metadata;

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

    public static PersonAttributeTypeDescriptor TREATMENT_SUPPORTER_ADDRESS = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "0d54f47d-d633-4a81-b496-e5b66c0b38fc";
        }

        @Override
        public String name() {
            return "Treatment Supporter Address";
        }

        @Override
        public String description() {
            return "Treatment Supporter Physical Address details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor TREATMENT_SUPPORTER_PHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "9fe7f9c2-877c-4209-83f1-abeba41b80a7";
        }

        @Override
        public String name() {
            return "Treatment Supporter Phone Number";
        }

        @Override
        public String description() {
            return "Treatment Supporter Phone Number details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor PATIENT_TYPE = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "3d49cb0e-be35-4afe-9f30-b218d011d3a7";
        }

        @Override
        public String name() {
            return "Patient Type";
        }

        @Override
        public String description() {
            return "Specifies the patient type eg new or on transit details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor OTHER_TREATMENT_SUPPORTER_PHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "b082786c-43e8-11e8-889a-295d59a83e3b";
        }

        @Override
        public String name() {
            return "Other Treatment Supporter Phone Number";
        }

        @Override
        public String description() {
            return "Other Treatment Supporter Phone Number details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor ALTERNATIVE_TREATMENT_SUPPORTER_PHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "24092d4f-43e9-11e8-889a-295d59a83e3b";
        }

        @Override
        public String name() {
            return "Alternative Treatment Supporter Phone Number";
        }

        @Override
        public String description() {
            return "Alternative Treatment Supporter Phone Number details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor OTHER_PATIENT_PHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "a868656a-43e9-11e8-889a-295d59a83e3b";
        }

        @Override
        public String name() {
            return "Other Patient Phone Number";
        }

        @Override
        public String description() {
            return "Other Patient Phone Number details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor ALTERNATIVE_PATIENT_PHONE_NUMBER = new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "b12462f6-43e9-11e8-889a-295d59a83e3b";
        }

        @Override
        public String name() {
            return "Alternative Patient Phone Number";
        }

        @Override
        public String description() {
            return "Alternative Patient Phone Number details";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor PATIENT_LOCATION= new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "9d08e97c-88d1-11e8-84f4-7f46ca7cf7b5";
        }

        @Override
        public String name() {
            return "Patient Location";
        }

        @Override
        public String description() {
            return "Tie a patient to a location in the database";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };

    public static PersonAttributeTypeDescriptor USER_LOCATION= new PersonAttributeTypeDescriptor() {
        @Override
        public String uuid() {
            return "8930b69a-8e7c-11e8-9599-337483600ed7";
        }

        @Override
        public String name() {
            return "User Location";
        }

        @Override
        public String description() {
            return "Tie a a user to a location in the database";
        }

        @Override
        public double sortWeight() {
            return 0;
        }
    };
}
