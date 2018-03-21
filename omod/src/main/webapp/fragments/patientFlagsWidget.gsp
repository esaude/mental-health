<div class="info-section">
	<div class="info-header">
		<i class="icon-flag"></i>
		<h3>${ ui.message("Flags").toUpperCase() }</h3>
	</div>

	<div class="info-body">

    <div id="flag-fragment-dashboard-widget">
        <ul>
            <% flags.each { name -> %>
                <li>${name}</li>
            <%}%>
        </ul>
    </div>

	</div>

</div>
