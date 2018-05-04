<style type="text/css">
    .green {
        background-color: #008000;
        color:#FFFFFF;
       }
      .red {
          background-color: #FF0000;
          color:#000000;
         }
     .yellow {
         background-color: #FFFF00;
        }
</style>
<div>
    <div class="info-header">
    		<i class="icon-user-md"></i>
    		<h3>${ ui.message("latest observations").toUpperCase() }</h3>

    </div>
    <div class="info-body">

        <div>
            <table>
                <% obs.each { key, value -> %>
                    <tr>
                        <td>${key}</td>
                        <td>${value}</td>
                    </tr>
                <%}%>
                <% if(greenBmi) {%>
                    <tr>
                        <td>BMI</td>
                        <td class="green">${greenBmi}</td>
                    </tr>
                <%}%>
                <% if(yellowBmi) {%>
                    <tr>
                        <td>BMI</td>
                        <td class="yellow">${yellowBmi}</td>
                    </tr>
                <%}%>
                <% if(redBmi) {%>
                    <tr>
                        <td>BMI</td>
                        <td class="red">${redBmi}</td>
                    </tr>
                <%}%>
                <% if(circRatio) {%>
                    <tr>
                        <td>Hip Waist ratio</td>
                        <td>${circRatio}</td>
                    </tr>
                <%}%>
            </table>
        </div>

    	</div>
</div>