<div>
    <div class="info-header">
    		<i class="icon-book"></i>
    		<h3>${ ui.message("patient summary").toUpperCase() }</h3>

    </div>
    <div class="info-body">

        <div>
            <table>
                <% diabetes.each { key, value -> %>
                    <tr>
                        <td>${key}</td>
                        <td>${value}</td>
                    </tr>
                <%}%>
                <% hypertension.each { key, value -> %>
                    <tr>
                        <td>${key}</td>
                        <td>${value}</td>
                    </tr>
                <%}%>
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