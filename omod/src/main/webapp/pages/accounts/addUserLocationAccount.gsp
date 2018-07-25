<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeCss("adminui", "systemadmin/account.css")

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.systemAdministration.label')}" , link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}'},
        { label: "${ ui.message("adminui.manageAccounts.label")}", link: '${ui.pageLink("adminui", "systemadmin/accounts/manageAccounts")}' },
    ];
    contextPath = '${ui.contextPath()}';
</script>

<form name="accountForm" class="simple-form-ui" novalidate method="post" action="">

        <select name="location">
            <% locations.each{%>
                <option value="${it.locationId}">${it.name}</option>
            <%}%>
        </select>
    <br />
    <input type="submit" class="confirm" id="save-button" value="${ ui.message("Save") }" />
</form>