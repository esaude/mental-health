<%
    ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeJavascript("aihdconfigs", "bootstrap.min.js")
    ui.includeCss("referenceapplication", "login.css")
    ui.includeCss("aihdconfigs", "bootstrap.min.css")

    def now = new Date()
    def year = now.getAt(Calendar.YEAR);
%>

<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${ ui.message("referenceapplication.login.title") }</title>
    <link rel="shortcut icon" type="image/ico" href="/${ ui.contextPath() }/images/openmrs-favicon.ico"/>
    <link rel="icon" type="image/png\" href="/${ ui.contextPath() }/images/openmrs-favicon.png"/>
    ${ ui.resourceLinks() }

    <style media="screen" type="text/css">
        body {
            font-family: "OpenSans", Arial, sans-serif;
            -webkit-font-smoothing: subpixel-antialiased;
            margin: 5px auto;
            background-color: white;
            background-repeat: no-repeat;
            height: 100%;
        }
        form {border: 3px solid #f1f1f1;}
        input[type=text], input[type=password] {
            width: 100%;
            padding: 12px 20px;
            margin: 8px 0;
            display: inline-block;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        button {
            background-color: #4CAF50;
            color: white;
            padding: 14px 20px;
            margin: 8px 0;
            border: none;
            cursor: pointer;
            width: 100%;
        }

        button:hover {
            opacity: 0.8;
        }
        .imgcontainer {
            text-align: center;
            margin: 24px 0 12px 0;
        }

        .container {
            padding: 16px;
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
            margin-left: 8%;
        }
        #county-logo{
            margin-left: 0;
            padding-top: 0;
            height: 170px;
            width: auto;
            align:left;
        }

        #bmz-logo img{
            width: 100%;
            height:auto;
        }
        #bmz-logo{
                width: auto;
                float: left;
                padding: 5px;
                align:left;
        }

        #malteser-logo img{
            width: auto;
            float: left;
            border-right-style: solid;
            border-right-width: 0;
            border-right-color: #e5e5e5;
            padding: 0;
            align:left;
        }
        #aihd-logo img{
            width: 100%;
            float: left;
            padding: 0;
            align:left;
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

        jQuery('#sessionLocation').change( function() {
            jQuery('#sessionLocationInput').val(jQuery(this).val());
            //updateSelectedOption();
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
        });

        setCaptcha();
        
    });

    function setCaptcha(){
        generateRandomNumber();
        validateSecurityInput();
        jQuery('#login-button').addClass('disabled');
        jQuery('#login-button').attr('disabled','disabled');
        jQuery("#security_captcha_answer").keypress(function(){
            setTimeout(checkSecurityResponse, 200);
        });
    }

    function generateRandomNumber(){  
    var operators = [{
        sign: "+",
        method: function(a,b){ return a + b; }
    },{
        sign: "-",
        method: function(a,b){ return a - b; }
    }];
  
  
    var min = 12;
    var max = 100;
    firstNumber = Math.floor(Math.random() * (max - min + 1)) + min;

    var min_2 = 1;
    var max_2= 10;
    secondNumber = Math.floor(Math.random() * (max_2 - min_2 + 1)) + min_2; 
  
    var selectedOperator = Math.floor(Math.random()*operators.length);
    var currentOperator = operators[selectedOperator].sign;                   
    var computedResults = operators[selectedOperator].method(firstNumber, secondNumber);
    
   
      jQuery("#first_number").text(firstNumber);
      jQuery("#operator").text(currentOperator); 
      jQuery("#second_number").text(secondNumber); 
      jQuery("#computer_results_hidden").val(computedResults); 
}

function checkSecurityResponse(){ 
  var userInputAnswer = parseInt(jQuery('#security_captcha_answer').val());
  var securityAnswer = parseInt(jQuery('#computer_results_hidden').val());
  var sessionLocationVal = jQuery('#sessionLocationInput').val();

        if(parseInt(sessionLocationVal, 10) > 0 && securityAnswer == userInputAnswer){
            jQuery('#login-button').removeClass('disabled');
            jQuery('#login-button').removeAttr('disabled');
            jQuery("#captcha_notification").text('');
        }else{
            jQuery('#login-button').addClass('disabled');
            jQuery('#login-button').attr('disabled','disabled');
            if(securityAnswer != userInputAnswer){
               jQuery("#captcha_notification").text("You have entered incorrect answer" ); 
            }
        }


}

function validateSecurityInput(){
    jQuery("#security_captcha_answer").keydown(function (e) {
        // Allow: backspace, delete, tab, escape, enter and .
        if (jQuery.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
             // Allow: Ctrl/cmd+A
            (e.keyCode == 65 && (e.ctrlKey === true || e.metaKey === true)) ||
             // Allow: Ctrl/cmd+C
            (e.keyCode == 67 && (e.ctrlKey === true || e.metaKey === true)) ||
             // Allow: Ctrl/cmd+X
            (e.keyCode == 88 && (e.ctrlKey === true || e.metaKey === true)) ||
             // Allow: home, end, left, right
            (e.keyCode >= 35 && e.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        // Ensure that it is a number and stop the keypress
        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
            e.preventDefault();
        }
    });
}

</script>

<div class="container">
    <section>
        <div class="page-header">
            <a href="${ui.pageLink("referenceapplication", "home")}" class="pull-left">
                <img src="${ui.resourceLink("aihdconfigs", "images/banners/city_county_logo.jpg")}" id="county-logo"/>
            </a>
            <div class="pull-right">
                <h1>Sample tag line message to go here</h1>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="row">
                    <button type="button" class="btn btn-success">Our Patiners</button>
                </div>
                <div class="row" id="malteser-logo">
                  <img src="${ui.resourceLink("aihdconfigs", "svg/malteser_logo.svg")}" height="95px" />
                </div>
                <div class="row" id="bmz-logo">
                  <img src="${ui.resourceLink("aihdconfigs", "images/bmz_logo2.png")}" height="125px" />
                </div>
                <div class="row" id="aihd-logo">
                  <img src="${ui.resourceLink("aihdconfigs", "images/AIHD_logo.jpg")}" />
                </div>
            </div>
            <div class="col-md-4">
                <form method="post" autocomplete="off">
                    <div class="imgcontainer">
                        <i class="icon-lock small"></i>
                        ${ ui.message("referenceapplication.login.loginHeading") }
                    </div>
                    <div class="form-group">
                        <label for="username">${ ui.message("referenceapplication.login.username") }:</label>
                        <input id="username" type="text" name="username" placeholder="${ ui.message("referenceapplication.login.username.placeholder") }"/>
                    </div>
                    <div class="form-group">
                     <label for="password">${ ui.message("referenceapplication.login.password") }:</label>
                     <input id="password" type="password" name="password" placeholder="${ ui.message("referenceapplication.login.password.placeholder") }"/>
                    </div>
                    <div class="form-group">
                        <label for="sessionLocation">Health facility:</label>
                         <select id="sessionLocation" class="form-control">
                            <% locations.sort { ui.format(it) }.each { %>
                            <option id="${it.name}" value="${it.id}">${ui.format(it)}</option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="security">Security Stamp:</label>
                        <div id="security-container">
                            <span id="first_number" style="color:red;"></span>
                            <span id="operator"></span>
                            <span id="second_number" style="color:red;"></span>
                            <input id="computer_results_hidden" type="hidden"/>
                            <input id="security_captcha_answer" type="text" name="security_check_answer" size="10" />
                            <span id="captcha_notification" style="color:red; font-size:18;"></span>
                         </div>
                    </div>
                    <div style="align:right;">
                        <a id="cant-login" href="javascript:void(0)">
                            <i class="icon-question-sign small"></i>
                            ${ ui.message("referenceapplication.login.cannotLogin") }
                        </a>
                        <input id="login-button" class="confirm" type="submit" value="${ ui.message("referenceapplication.login.button") }"/>
                    </div>
                        <input type="hidden" id="sessionLocationInput" name="sessionLocation"
                         <% if (lastSessionLocation != null) { %> value="${lastSessionLocation.id}" <% } %> />
                    <input type="hidden" name="redirectUrl" value="${redirectUrl}" />
                </form>
            </div>
        </div>
    </section>
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