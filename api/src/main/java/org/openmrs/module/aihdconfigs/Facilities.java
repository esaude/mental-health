package org.openmrs.module.aihdconfigs;

import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;

public class Facilities {

    public static void saveLocations(String mflcode, String name){
        System.out.println("getting into the location");
        LocationService locationService = Context.getLocationService();
        LocationAttributeType locationAttributeType = locationService.getLocationAttributeTypeByName("MflCode");
        //if missing create one here
        if(locationAttributeType == null){
            LocationAttributeType type = new LocationAttributeType();
            type.setName("MflCode");
            type.setCreator(Context.getAuthenticatedUser());
            type.setDatatypeClassname("org.openmrs.customdatatype.datatype.FreeTextDatatype");
            type.setDescription("Attribute that hold the unique code for the facility");
            locationService.saveLocationAttributeType(type);
        }
        //continue processing the attribute, the code and the name

    }
}