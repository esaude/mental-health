<div>
    <div class="info-header">
    		<i class="icon-heart"></i>
    		<h3>${ ui.message("Vitals").toUpperCase() }</h3>

    </div>
    <div class="info-body">

        <div>
            <table>
                <% vitals.each { key, value -> %>
                    <tr>
                        <td>${key}</td>
                        <td>${value}</td>
                    </tr>
                <%}%>
            </table>
        </div>

    	</div>
</div>