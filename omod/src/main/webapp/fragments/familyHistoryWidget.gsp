<div>
    <div class="info-header">
    		<i class="icon-group"></i>
    		<h3>${ ui.message("Family History").toUpperCase() }</h3>

    </div>
    <div class="info-body">

        <div>
            <table>
                <% history.each { key, value -> %>
                    <tr>
                        <td>${key}</td>
                        <td>${value}</td>
                    </tr>
                <%}%>
            </table>
        </div>

    	</div>
</div>