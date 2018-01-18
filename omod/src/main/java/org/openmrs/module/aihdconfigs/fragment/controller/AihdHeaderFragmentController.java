package org.openmrs.module.aihdconfigs.fragment.controller;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.AppUiExtensions;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.List;
import java.util.Map;

public class AihdHeaderFragmentController {

    private static final String GET_LOCATIONS = "Get Locations";

    private static final String VIEW_LOCATIONS = "View Locations";

    public void controller(@SpringBean AppFrameworkService appFrameworkService, FragmentModel fragmentModel) {
        try {
            Context.addProxyPrivilege(GET_LOCATIONS);
            Context.addProxyPrivilege(VIEW_LOCATIONS);
            fragmentModel.addAttribute("loginLocations", appFrameworkService.getLoginLocations());

            List<Extension> exts = appFrameworkService.getExtensionsForCurrentUser(AppUiExtensions.HEADER_CONFIG_EXTENSION);
            Map<String, Object> configSettings = exts.size() > 0 ? exts.get(0).getExtensionParams() : null;
            fragmentModel.addAttribute("configSettings", configSettings);
            List<Extension> userAccountMenuItems = appFrameworkService.getExtensionsForCurrentUser(
                    AppUiExtensions.HEADER_USER_ACCOUNT_MENU_ITEMS_EXTENSION);
            fragmentModel.addAttribute("userAccountMenuItems", userAccountMenuItems);
            Location location = Context.getLocationService().getLocationByUuid(Context.getAdministrationService().getGlobalProperty("aihdconfigs.facilityName"));
            fragmentModel.addAttribute("facility", location);
        }
        finally {
            Context.removeProxyPrivilege(GET_LOCATIONS);
            Context.removeProxyPrivilege(VIEW_LOCATIONS);
        }
    }
}
