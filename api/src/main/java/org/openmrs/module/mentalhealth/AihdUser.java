/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.mentalhealth;

import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.mentalhealth.metadata.PersonAttributeTypes;

import java.util.Set;

public class AihdUser {
    private Person person;
    private Location location;

    private String mflCode;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Location getLocation() {
        PersonAttributeType personAttribute = Context.getPersonService().getPersonAttributeTypeByUuid(PersonAttributeTypes.USER_LOCATION.uuid());
        PersonAttribute attribute = getPerson().getAttribute(personAttribute);
        if(attribute != null){
            location = Context.getLocationService().getLocation(Integer.parseInt(attribute.getValue()));
        }
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public String getMflCode() {
        Location location = getLocation();
        if(location != null){
            LocationAttributeType locationAttributeType = Context.getLocationService().getLocationAttributeTypeByName("MflCode");
            Set<LocationAttribute> locationAttribute = location.getAttributes();
            if(locationAttribute != null) {
                for (LocationAttribute attribute: locationAttribute){
                    if(attribute != null && attribute.getAttributeType().equals(locationAttributeType)){
                        mflCode = String.valueOf(attribute.getValue());
                    }
                }
            }
        }
        return mflCode;
    }

    public void setMflCode(String mflCode) {
        this.mflCode = mflCode;
    }
}
