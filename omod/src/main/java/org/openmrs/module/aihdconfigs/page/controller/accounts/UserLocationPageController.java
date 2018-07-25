/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.aihdconfigs.page.controller.accounts;

import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.module.aihdconfigs.AihdUser;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

import java.util.ArrayList;
import java.util.List;

public class UserLocationPageController {

    public void controller(PageModel model, @SpringBean("userService") UserService userService){
        List<User> users =  userService.getAllUsers();

        List<AihdUser> allUsers = new ArrayList<AihdUser>();
        for(User user:users){
            AihdUser aihdUser = new AihdUser();
            aihdUser.setPerson(user.getPerson());

            allUsers.add(aihdUser);
        }

        model.addAttribute("users", allUsers);
    }
}
