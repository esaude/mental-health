<div>
    <div class="info-header">
    		<i class="icon-copy"></i>
    		<h3>${ ui.message("patient summary").toUpperCase() }</h3>

    </div>
    <div class="info-body">

        <div id="summary-fragment-dashboard-widget">
        <table>
            <% summary.each { key, value -> %>
                <tr>
                    <td>${key}</td>
                    <td>${value}</td>
                </tr>
            <%}%>
        </table>
        </div>

    	</div>
</div>