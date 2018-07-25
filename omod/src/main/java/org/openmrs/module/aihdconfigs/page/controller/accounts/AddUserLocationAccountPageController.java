package org.openmrs.module.aihdconfigs.page.controller.accounts;

import org.openmrs.Location;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdconfigs.AihdUser;
import org.openmrs.module.aihdconfigs.metadata.PersonAttributeTypes;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class AddUserLocationAccountPageController {
    public void controller(PageModel model){
        model.addAttribute("locations", Context.getLocationService().getAllLocations());
    }

    public String post(PageModel model,
                       @RequestParam(value = "personId", required = false) Person person,
                       @RequestParam(value = "location", required = false) Location location,
                       @SpringBean("userService") UserService userService){

        List<User> users =  userService.getAllUsers();

        List<AihdUser> allUsers = new ArrayList<AihdUser>();
        for(User user:users){
            AihdUser aihdUser = new AihdUser();
            aihdUser.setPerson(user.getPerson());

            allUsers.add(aihdUser);
        }

        if(person != null && location != null){
            //save the person attribute with this location
            PersonAttributeType personAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(PersonAttributeTypes.USER_LOCATION.uuid());
            PersonAttribute updatePersonAttribute = new PersonAttribute();
            updatePersonAttribute.setAttributeType(personAttributeType);
            updatePersonAttribute.setValue(String.valueOf(location.getLocationId()));

            person.addAttribute(updatePersonAttribute);


            //save the new person into the database
            Context.getPersonService().savePerson(person);
        }

        model.addAttribute("users", allUsers);
        model.addAttribute("locations", Context.getLocationService().getAllLocations());

        return "accounts/userLocation";
    }
}
