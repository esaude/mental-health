<div>
    <div class="info-header">
    		<i class=" icon-calendar"></i>
    		<h3>${ ui.message("Appointments").toUpperCase() }</h3>

    </div>
    <div class="info-body">
    <div>
        <table>
            <% apointment.each { key, value -> %>
                <tr>
                    <td>${key}</td>
                    <td>${value}</td>
                </tr>
            <%}%>
        </table>
    </div>
    </div>
</div>