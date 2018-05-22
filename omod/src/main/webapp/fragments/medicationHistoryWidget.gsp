<div class="info-section">
	<div class="info-header">
		<i class="icon-leaf"></i>
		<h3>${ ui.message("Medication History").toUpperCase() }</h3>
	</div>

	<div class="info-body">

    <div id="flag-fragment-dashboard-widget">
        <table>
            <tr>
                <td colspan="2">Date: ${date}</td>
            </tr>
            <tr>
                <th>Drug</th>
                <th>Formulation</th>
            </tr>
            <% medication.each { key, value -> %>
            <tr>
                    <td>${key}</td>
                    <td>${value}</td>
                </tr>
            <%}%>
        </table>
    </div>

	</div>

</div>