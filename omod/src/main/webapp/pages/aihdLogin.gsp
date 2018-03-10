<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeCss("referenceapplication", "login.css")

    def now = new Date()
    def year = now.getAt(Calendar.YEAR);
%>

<!DOCTYPE html>
<html>
<head>
    <title>${ ui.message("referenceapplication.login.title") }</title>
    <link rel="shortcut icon" type="image/ico" href="/${ ui.contextPath() }/images/openmrs-favicon.ico"/>
    <link rel="icon" type="image/png\" href="/${ ui.contextPath() }/images/openmrs-favicon.png"/>
    ${ ui.resourceLinks() }

    <style media="screen" type="text/css">
        body {
            font-family: "OpenSans", Arial, sans-serif;
            -webkit-font-smoothing: subpixel-antialiased;
            max-width: 1000px;
            margin: 10px auto;
            background-color: white;
        }
        #body-wrapper {
            margin-top: 10px;
            padding: 10px;
            background-color: white;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
            -khtml-border-radius: 5px;
            border-radius: 5px;
        }
        #body-wrapper #content {
            margin-top: 20px;
            padding: 10px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
            -khtml-border-radius: 5px;
            border-radius: 5px;
        }
        .logo {
            margin: 0px;
            text-align: center;
        }
        #error-message {
            color: #B53D3D;
            text-align: center;
        }
        .footer{
            float: left;
            margin: 0px 15px;
            width: 80%;
            display: inline-block;
            font-size: 0.7em;
            color: #808080;
        }
        .footer .left_al {
            float: left;
        }
        .footer .right_al{
            float: right;
        }
        .footer .center_al{
            float: center;
         }
        .footer a{
            color: #404040;
            font-size: 1em;
            padding: 5px;
            text-decoration: none;
        }
        .footer a:hover{
            color: #404040;
            font-size: 1em;
            padding: 5px;
            text-decoration: underline;
        }
        .footer a:active{
            color: #404040;
            font-size: 1em;
            padding: 5px;
            text-decoration: none;
        }
        .footer a:after{
            color: #404040;
            font-size: 1em;
            padding: 5px;
            text-decoration: none;
        }
        header {
            line-height: 1em;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
            -khtml-border-radius: 5px;
            border-radius: 5px;
            position: relative;
            background-color: white;
            color: #CCC;
            align: left;
        }
        header .logo img {
            width: 200px;
        }
        header .logo {
            float: none;
            margin: 4px;
        }
        #login-form ul.select {
            padding: 10px;
            background: beige;
        }
        ul.select li.selected {
            background-color: #94979A;
            color: white;
            border-color: transparent;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
            -khtml-border-radius: 5px;
            border-radius: 5px;
            padding: 5px;
            text-align: center;
        }
        ul.select li:hover {
            background-color: #AB3A15;
            color: white;
            cursor: pointer;
        }
        ul.select li {
            margin: 0 10px;
            text-align: left;
            display: inline-block;
            width: 20%;
            padding: 5px;
            color: #3B6692;
            background-color: #FFF;
            /* border-bottom: 1px solid #efefef; */
            border: dashed 1px #CEC6C6;
            text-align: center;
        }
        form fieldset, .form fieldset {
            border: solid 1px #CECECE;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
            -khtml-border-radius: 5px;
            border-radius: 5px;
            background: #FFFFFB;
        }
        #county-logo{
            margin-left: 30%;
            padding-top: 15px;
            height: 130px;
            width: auto;
        } 

        #bmz-logo img{
            width: 100%;
            height:auto;
        }
        #bmz-logo{
                width: 30%;
                float: left;
                padding: 10px;
        }
        
        #malteser-logo img{
               width: auto;
                height: 100px;
        }
        #malteser-logo{
            width: 30%;
            float: left;
            border-right-style: solid;
            border-right-width: 1px;
            border-right-color: #e5e5e5;
            padding: 3px;
        }

        #aihd-logo img{
            width: 100%;
            height: auto;
        }
        #aihd-logo{
            width: 50%;
            float: left;
            padding: 10px;
        }
        #logo-container{
        margin-left: 5%;
        margin-right: 10%;
        width: 100%;
        }
        </style>
</head>
<body>
<script type="text/javascript">
    var OPENMRS_CONTEXT_PATH = '${ ui.contextPath() }';
</script>


${ ui.includeFragment("referenceapplication", "infoAndErrorMessages") }

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery('#username').focus();
    });

    updateSelectedOption = function() {
        jQuery('#sessionLocation option').removeClass('selected');
        jQuery('#sessionLocation option[value|=' + jQuery('#sessionLocationInput').val() + ']').addClass('selected');

        var sessionLocationVal = jQuery('#sessionLocationInput').val();
        if(parseInt(sessionLocationVal, 10) > 0){
            jQuery('#login-button').removeClass('disabled');
            jQuery('#login-button').removeAttr('disabled');
        }else{
            jQuery('#login-button').addClass('disabled');
            jQuery('#login-button').attr('disabled','disabled');
        }
    };

    jQuery(function() {
        updateSelectedOption();

        jQuery('#sessionLocation option').click( function() {
            jQuery('#sessionLocationInput').val(jQuery(this).attr("value"));
            updateSelectedOption();
        });

        var cannotLoginController = emr.setupConfirmationDialog({
            selector: '#cannot-login-popup',
            actions: {
                confirm: function() {
                    cannotLoginController.close();
                }
            }
        });
        jQuery('a#cant-login').click(function() {
            cannotLoginController.show();
        })
    });
</script>

<div style="align:center;">
    <a href="${ui.pageLink("referenceapplication", "home")}">
        <img src="${ui.resourceLink("aihdconfigs", "images/banners/city_county_logo.jpg")}" height="200px" width="300px" id="county-logo"/>
    </a>
</div>
<div id="body-wrapper" style="width:100%; margin:0 auto; align:center;">
    <div id="content">
        <form id="login-form" method="post" autocomplete="off">
            <fieldset>

                <legend>
                    <i class="icon-lock small"></i>
                    ${ ui.message("referenceapplication.login.loginHeading") }
                </legend>
                <table>
                    <tr>
                        <td>

                            <label for="username">
                                ${ ui.message("referenceapplication.login.username") }:
                            </label>
                        </td>
                        <td>
                            <input id="username" type="text" name="username" placeholder="${ ui.message("referenceapplication.login.username.placeholder") }"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <label for="password">
                                ${ ui.message("referenceapplication.login.password") }:
                            </label>
                        </td>
                        <td>
                            <input id="password" type="password" name="password" placeholder="${ ui.message("referenceapplication.login.password.placeholder") }"/>
                        </td>
                    </tr>


                    <tr>
                        <td>
                            <label for="sessionLocation">
                                ${ ui.message("referenceapplication.login.sessionLocation") }:
                            </label>
                        </td>
                        <td>
                            <select id="sessionLocation" class="select">
                                <% locations.sort { ui.format(it) }.each { %>
                                <option id="${it.name}" value="${it.id}">${ui.format(it)}</option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                </table>

                <input type="hidden" id="sessionLocationInput" name="sessionLocation" value="7" />

                <p></p>
                <div style="padding-top: 20px">
                    <div>
                        <p>
                            <input id="login-button" class="confirm" type="submit" value="${ ui.message("referenceapplication.login.button") }"/>
                        </p>
                        <p>
                            <a id="cant-login" href="javascript:void(0)">
                                <i class="icon-question-sign small"></i>
                                ${ ui.message("referenceapplication.login.cannotLogin") }
                            </a>
                        </p>
                    </div>
                </div>

            </fieldset>
            <input type="hidden" name="redirectUrl" value="${redirectUrl}" />
        </form>
    </div>
</div>
<div id ="logo-container">

    <div id="malteser-logo">
            <img src="${ui.resourceLink("aihdconfigs", "svg/malteser_logo.svg")}" height="95px" />
    </div>
    <div id="bmz-logo">
           <img src="${ui.resourceLink("aihdconfigs", "images/bmz_logo2.png")}" height="125px" />
    </div>
    <div id="aihd-logo">
           <img src="${ui.resourceLink("aihdconfigs", "images/AIHD_logo.jpg")}" width="450px" height="auto" />
    </div>

</div>

<div id="cannot-login-popup" class="dialog" style="display: none">
    <div class="dialog-header">
        <i class="icon-info-sign"></i>
        <h3>${ ui.message("referenceapplication.login.cannotLogin") }</h3>
    </div>
    <div class="dialog-content">
        <p class="dialog-instructions">${ ui.message("referenceapplication.login.cannotLoginInstructions") }</p>

        <button class="confirm">${ ui.message("referenceapplication.okay") }</button>
    </div>
</div>

</body>
</html>