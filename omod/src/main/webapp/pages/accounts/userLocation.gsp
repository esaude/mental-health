<%
    ui.decorateWith("appui", "standardEmrPage")
    ui.includeCss("adminui", "systemadmin/accounts.css")
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message('coreapps.app.systemAdministration.label')}" , link: '${ui.pageLink("coreapps", "systemadministration/systemAdministration")}'},
        { label: "${ ui.message("User Locations")}", link: '${ui.pageLink("adminui", "systemadmin/accounts/manageAccounts")}' }
    ];
</script>

<hr>
<table id="list-accounts" cellspacing="0" cellpadding="2">
    <thead>
        <tr>
            <th class="adminui-expand-column">${ ui.message("Name")}</th>
            <th class="adminui-shrink-column">${ ui.message("Gender")}</th>
            <th class="adminui-expand-column">${ ui.message("Facility")}</th>
            <th class="adminui-shrink-column">${ ui.message("Code")}</th>
            <th class="adminui-shrink-column">${ ui.message("Action")}</th>
        </tr>
    </thead>
    <tbody>
        <% users.each{  %>
            <tr>
                <td valign="top">${ui.encodeHtmlContent(ui.format(it.person))}</td>
                <td valign="top" class="adminui-center">${ ui.format(it.person.gender)}</td>
                <td valign="top">${ ui.encodeHtmlContent(ui.format(it.location))}</td>
                <td valign="top">${ ui.encodeHtmlContent(ui.format(it.mflCode))}</td>
                <td valign="top" class="adminui-center">
                    <i class="icon-pencil edit-action" title="${ ui.message("Edit")}"
                       onclick="location.href='${ui.pageLink("aihdconfigs", "accounts/addUserLocationAccount",[personId: it.person.id])}'"></i>
                </td>
            </tr>
        <% } %>
    </tbody>
</table>

<% if ( (users != null)) { %>
${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#list-accounts",
                                                         options: [
                                                                 bFilter: true,
                                                                 bJQueryUI: true,
                                                                 bLengthChange: false,
                                                                 iDisplayLength: 15,
                                                                 sPaginationType: '\"full_numbers\"',
                                                                 bSort: false,
                                                                 sDom: '\'ft<\"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg \"ip>\''
                                                         ]
]) }
<% } %>