<div class="info-section">
	<div class="info-header">
		<i class="icon-leaf"></i>
		<h3>${ ui.message("Medication History").toUpperCase() }</h3>
	</div>

	<div class="info-body">

    <div id="flag-fragment-dashboard-widget">
        <%if(date){%>
            <table>
                <tr>
                    <td colspan="2">Date: ${date}</td>
                </tr>
                <tr>
                    <th>Group</th>
                    <th>Formulation</th>
                </tr>
                <% medication.each { key, value -> %>
                    <% if(value.size > 0) {%>
                        <tr>
                            <td>${key}</td>
                            <td>
                                <ul>
                                    <% value.each {%>
                                        <li>${it}</li>
                                    <%}%>
                                </ul>
                            </td>
                        </tr>
                    <%}%>
                <%}%>
            </table>
        <%} else {%>
        <p>None</p>
        <%}%>
    </div>

	</div>

</div>