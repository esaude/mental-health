package org.openmrs.module.aihdconfigs;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;

import java.io.*;
import java.util.Date;

public class Facilities {

    public static void saveLocations(InputStream csvFile){
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
        //get the contents of the csv file line by line and process them here

        //continue processing the attribute, the code and the name
        String line = "";
        String cvsSplitBy = ",";
        String headLine = "";
        String mflCode = "";
        String locationName = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(csvFile, "UTF-8"));
            headLine = br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] records = line.split(cvsSplitBy);
                    mflCode = records[0];
                    locationName = records[1];

                    if (StringUtils.isNotEmpty(locationName)) {
                        if (locationService.getLocation(locationName) == null) {
                            Location location = new Location();
                            location.setName(locationName);
                            location.setDateCreated(new Date());
                            location.setCreator(Context.getAuthenticatedUser());
                            //set the mfl code if availabble
                            if (StringUtils.isNotBlank(mflCode) && StringUtils.isNotEmpty(mflCode)) {
                                //set the mfl code here
                                LocationAttribute attribute = new LocationAttribute();
                                attribute.setAttributeType(locationAttributeType);
                                attribute.setCreator(Context.getAuthenticatedUser());
                                attribute.setDateCreated(new Date());
                                attribute.setValue(mflCode);
                                //save the attribute in the db
                                location.addAttribute(attribute);
                                locationService.saveLocation(location);
                            } else {
                                //we are missing the mfl code for the facility, we just create the location with the name
                                locationService.saveLocation(location);
                            }
                        }
                    }
                }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}