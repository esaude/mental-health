//namespace datastore by page path, internally localStorage uses only host (dns:port)
var dataStoreNS = window.location.pathname;

const initializeDefaultSrc = {
    //emulate a HTMLInputElement, type "checkbox", "radio"
    "checked": false,
    //emulate a HTMLInputElement type "date", "number", "tel", "text", "textarea"
    "value": "",
    //emulate a HTMLSelectElement
    0: { "id": "not-selected"},
    selectedIndex: 0,
};

function fnForFieldsetRadios(control, fn=function(radio){}){
    var childRadios = $(`fieldset[id=${control.id}] input[type=radio]`);

    for(radio of childRadios){
        fn(radio);
    }
}

function clearDisabledInputByType(control, alwaysClear=false){

    if(control.disabled || alwaysClear){
        setMemberByType(initializeDefaultSrc, control, control);
    }

    var inputEvent = new Event("input");

    control.dispatchEvent(inputEvent);

}

function setDependentDisabledState(clickedElement, specificRadio, specificTarget, stateOverride=false, disabledOverrideValue){

    if(specificRadio) {
        //console.log("setting clickedElement to ", specificRadio);
        clickedElement = document.querySelector(specificRadio);
    }

    let el = clickedElement.parentElement;

    let targetState = !clickedElement.checked || clickedElement.selected;

    if(stateOverride){
        targetState = disabledOverrideValue;
    }

    
    if(!specificTarget){
        //console.log('toggling state', clickedElement);
        while(el.nextElementSibling) {

            el = el.nextElementSibling;
            var targetEl = el.children[0];
            //console.log(el, el.children[0]);
            targetEl.disabled = targetState;

            clearDisabledInputByType(targetEl);
            //console.log('toggled disavbled on element with id', el.children[0].id);

        }
    } else {
        
        if(Array.isArray(specificTarget)) {
            
            for(target of specificTarget) {
                var targetEl = document.querySelector(target);
                targetEl.disabled = targetState;
                
                if($(`fieldset[id=${targetEl.id}] input[type=radio]`).length){
                    fnForFieldsetRadios(targetEl, function(radio){clearDisabledInputByType(radio, true)});
                }
                else{
                    clearDisabledInputByType(targetEl);
                }
            }

        } else {
            var targetEl = document.querySelector(specificTarget)
            targetEl.disabled = targetState;
            
            clearDisabledInputByType(targetEl);
        }
    }
};

function getIndexFromOptionID(control, optionID){

    var option = control.namedItem(optionID);

    var optionIndex = -1;
    
    var iteratedOption = 0;

    for(var iteratedOption=0; optionIndex==-1 && control[iteratedOption]; iteratedOption++){
        
        var curOption = control[iteratedOption];
        //console.log(iteratedOption, curOption);

        if(curOption == option){
            //console.log(curOption);
            optionIndex = iteratedOption;
            
            //When the form is reset, the select will return to its default selection for this select input
            //console.log("setting as default selection");
            curOption.defaultSelected=true;
        }
            
    }

    return optionIndex;
}

function loadLocalSiteInfo(attach=false){

    //console.log("loading header selections from browser local storage");

    var storedSelectOptions = ["province", "district", "health-facility"];

    for(selectID of storedSelectOptions) {

        //console.log("looking for", selectID);

        var jQuerySelect = $("select[id="+selectID+"-select]");
        var DOMSelect = undefined;

        if(jQuerySelect) {
            DOMSelect = jQuerySelect[0];
        }

        if(DOMSelect && localStorage[dataStoreNS+"mhf-"+selectID]){
            
            var optionIndex = getIndexFromOptionID(DOMSelect, localStorage[dataStoreNS+"mhf-"+selectID]);
            
            if(optionIndex) {
                DOMSelect.selectedIndex = optionIndex;
            }

        }

        if(attach && jQuerySelect) {
            //attach onChange handler to store province selection in local browser storage
            jQuerySelect.on("change", function(event){
                //console.log(this[this.selectedIndex].value);

                let storageName=dataStoreNS+"mhf-"+this.id.slice(0, this.id.lastIndexOf("-select"));
                //console.log("storage name", storageName);

                var prevOptionID = localStorage[storageName];
                if(prevOptionID != undefined) {
                    var prevOptionSelected = this.options[prevOptionID];

                    //console.log(prevOptionID, prevOptionSelected);

                    //Remove default on previously stored option
                    prevOptionSelected.defaultSelected = false;
                }
                
                var newOptionSelected = this[this.selectedIndex];
                                        
                localStorage[storageName] = newOptionSelected.id;
                //Set this as the new default to return to when resetting the form
                newOptionSelected.defaultSelected = true;
            });
        }
    }
}

function setAllDisabledStates(){
    //onclick*='setDependentDisabledState'
    /*for(inputSetsDisabled of )){
        //console.log(inputSetsDisabled, inputSetsDisabled.checked, inputSetsDisabled.onclick);
        
        var onclick=String(inputSetsDisabled.onclick);
        
        var param = [];
        var argsStr = onclick.slice(onclick.indexOf("setDependentDisabledState(")+"setDependentDisabledState(".length, onclick.lastIndexOf(");"));

        param = argsStr.split(",");
        
        setDependentDisabledState(param[0]=="this"?clickedElement=inputSetsDisabled:param[0],specificRadio=param[1], specificTarget=param[2], stateOverride=true, disabledOverrideValue=true);
    }*/

    var disableDependencies = [
            {
                set: $("input[onclick*='setDependentDisabledState']"),
                handler: "onclick"
            },
            {
                set: $("fieldset[onchange*='setDependentDisabledState']"),
                handler: "onchange"
            }
    ];

    for(ontype of disableDependencies) {
        //onchange*='setDependentDisabledState'
        for(inputSetsDisabled of ontype.set){
            //console.log(inputSetsDisabled, inputSetsDisabled.checked, inputSetsDisabled.onchange);
            
            var onchange=String(inputSetsDisabled[ontype.handler]);
            
            var allParams = [];
            var argsStr = onchange.slice(onchange.indexOf("setDependentDisabledState(")+"setDependentDisabledState(".length, onchange.lastIndexOf(");"));

            //also splits arrays of targets which need to be rebuilt too
            allParams = argsStr.split(",");

            
            //console.log("allParams", allParams)

            if(allParams.length>2){
                var beginArrayIndex=-1;
                var foundArray=false;
                var endArrayIndex=-1;

                for(i=0; i<allParams.length; i++){
                    var param = allParams[i];
                    //selector like '[id=example]' also begins with '[', make sure there is more than one '[' in this param
                    //var segmentsAfterOpenBracket = param.split("[").length;

                    if(param.search(/\[ *('|"|`)/) != -1 /*&& param.split("[").length > 2*/){
                        beginArrayIndex = i;
                        foundArray = true;
                        allParams[i] = param.replace(/\[ */, "");
                    }
                    //don't count a closing square bracket as an array if we havent found an open bracket
                    //a selector like '[id=example]' also ends with ']', there should be more than ']', (n+1 if n)
                    //in case someone decides to write (this, input[independent], [ input[id=dependent] ])
                    //var segmentsAfterCloseBracket = param.split("]").length;
                    if(foundArray && param.search(/('|"|`) *\]/) != -1 /*&& param.split("]").length > 2*/){
                        endArrayIndex = i;

                        allParams[i] = param.replace(/\] *('|"|`) *\]/, "]'");
                    }
                }

                allParams = allParams.map(param => param.replace(/'/g, " ").trim());

                if( foundArray ) {
                    //console.log("target array found");
                    allParams[2] = allParams.slice(beginArrayIndex, endArrayIndex);

                    allParams = allParams.slice(0, 3);
                }
            }

            //console.log(argsStr, allParams, allParams[0]=="this");


            setDependentDisabledState(allParams[0]=="this"?clickedElement=inputSetsDisabled:allParams[0],allParams[1]=="undefined"?undefined:specificRadio=allParams[1], specificTarget=allParams[2], stateOverride=true, disabledOverrideValue=true);
        }
    }
}

function confirmReset(evt){
    var result = confirm('Are you sure you want to reset *ALL* patient inputs to defaults?');
    
    if(result==false)
    {
        //the user cancelled the reset, prevent the default form reset behavior
        evt.preventDefault();

    }else{
        //go through clearing and re-initilization of localdatastore without re-attaching inputs' event listeners
        initializeInputValuePersistence(reset=true);

        //the default behavior of form input type="reset" will be applied after this handler returns
    }

}

function warnMaxDateToday(dateInput, alertSelector){
    var todayDate = new Date();
    var todayStr = todayDate.toISOString().split('T')[0];
    console.log(todayStr, dateInput.value);
    
    var specifiedDay = new Date(dateInput.value);
    
    var alertjQuery = $(alertSelector);
    var alert = alertjQuery[0];

    //alert.clientWidth = dateInput.clientWidth;

    var alertText = "";

    if(specifiedDay > todayDate){
        //dateInput.value = todayStr;
        alertText = "Specified date is after today."
        alertjQuery.addClass("bg-warning");
    } else{
        alertjQuery.removeClass("bg-warning");
    }
    
    console.log(alertText);

    alert.textContent = alertText;

}

function calculateAge(dobDateInput, targetSelector){
    
    var specifiedDay = new Date(dobDateInput.value);
    
    if(isNaN(specifiedDay)) {
        return;
    }
    
    var todayDate = new Date();
    

    var ageDate = new Date(todayDate - specifiedDay);
    var age = ageDate.getFullYear() - new Date("1/1/1970").getFullYear()
    
    $(targetSelector)[0].value=age;
}

function setHiddenCheckbox(checkboxSelector, value){
    $(checkboxSelector)[0].checked = value;
}

//
function setMaxDateToToday(){
    var today =new Date().toISOString().split('T')[0];
    console.log(today);
    
    for(dateInput of $("input[type=date]")){
        dateInput.max=today;
    }
}

const dataStoreProto = JSON.stringify({"storageVersion": "0.1"})

function initializeLocalStore(clear){

    if(clear){
        delete localStorage[dataStoreNS];
    }

    if(!localStorage[dataStoreNS]){
        localStorage[dataStoreNS] = dataStoreProto;
    }
}

function setMemberByType(src, dest, control)
{
    if(control instanceof HTMLInputElement || control instanceof HTMLTextAreaElement) {
        switch(control.type){
            case "checkbox":
            case "radio":
                dest.checked= src.checked;
                break;

            case "date":
            case "text":
            case "number":
            case "tel":
            case "textarea":
                dest.value= src.value;
                break;
        }
    } else if (control instanceof HTMLSelectElement) {
        
        if(dest instanceof HTMLSelectElement) {
            var optionIndex = getIndexFromOptionID(control, src.id);

            dest.selectedIndex= optionIndex;
        }
        else {
            dest.id= src[src.selectedIndex].id;
        }
    }
}

function versionedDataStore(datastore, control){

    var result = {};

    //if(datastore.storageVersion > "0.2") { }
    //if(datastore.storageVersion > "0.1") { }
    if(datastore.storageVersion < 0.2 && datastore.storageVersion >= "0.1") {
        result = datastore[control.id];
    }

    return result;
}

function localDataStore(control, load){
    
    var src = control;
    var dest = {};
    var dataStore = {};

    try{
        
        dataStore = JSON.parse(localStorage[dataStoreNS]);
        
        dest = versionedDataStore(dataStore, control);

    } catch (e){

        initializeLocalStore(clear=true);

        dataStore = JSON.parse(localStorage[dataStoreNS]);
        dest = versionedDataStore(dataStore, control);
    }

    if(load){
        src = dest;
        dest = control;
    }

    if(!src){

        //if the local data store is uninitialized for this input during load, initialize it
        if(load){

            dest = dataStore[control.id] = {"class": control.constructor.name};

            src = initializeDefaultSrc;

        } else {
            return;
        }
    }

    setMemberByType(src, dest, control);
    
    //store entire JSON object back into localStorage
    localStorage[dataStoreNS]=JSON.stringify(dataStore);
}

function applyScrollPositionPersistence(){

    document.body.scrollTop = document.documentElement.scrollTop = localStorage[dataStoreNS+"currentScroll"];

    window.addEventListener('scroll', function(event){
        //"pick up where you left off"
        //store current page scroll (though it is set to 0 when selecting a new tab)
        //when reloading the page to view changes, this value will be used to scroll back to scroll position before page refresh
        
        //console.log(document.documentElement.scrollTop);
        //document.body.scrollTop remains 0 with sticky position on common header
        localStorage[dataStoreNS+"currentScroll"]=document.documentElement.scrollTop;
    });
}

function initializeInputValuePersistence(reset=false){
    
    var inputs = [];
    inputs = jQuery.makeArray($('form#mh-form input'));
    inputs = inputs.concat(jQuery.makeArray($('form#mh-form select')));
    
    var textAreas = jQuery.makeArray($('form#mh-form textarea'));
    inputs = inputs.concat(textAreas);


    var fieldsets = jQuery.makeArray($('form#mh-form fieldset'));

    var fieldsetsWithRadios = fieldsets.filter( (fieldset) => {
        if(fieldset.id=="") {
            //console.log("Skipping", fieldset);
            return false;
        }

        //see if there are any child radios for the given fieldset
        var fieldsetHasRadios = $(`fieldset[id=${fieldset.id}] input[type=radio]`).length>0;
        
        //check if this fieldset has any children fieldsets (which may account for the radios we see)
        var isImmediateParent = $(`fieldset[id=${fieldset.id}] fieldset`).length==0;

        //if this is not a parent of fieldset and it has radio children, keep it
        return fieldsetHasRadios && isImmediateParent; 

        });


    //if just re-initializing, clear the local datastore
    initializeLocalStore(clear=(false||reset));

    //attach listener to locally store all data to all inputs
    for(input of inputs){
        
        if(input.type!="radio"){

            //if not resetting, attach event listeners (this only need to be done on document.ready())
            //if resetting local data store, don't re-attach input event listeners
            if(!reset){
                input.addEventListener("input", function oninputInputStore(event){localDataStore(this, load=false)});
            }
    
            //load locally stored data, if it exists, otherwise initialize data storage object
            localDataStore(input, load=true);
        }
    }

    for(fieldset of fieldsetsWithRadios){

        //console.log(fieldset)
        
        //if not resetting, attach event listeners (this only need to be done on document.ready())
        //if resetting local data store, don't re-attach input event listeners
        if(!reset){
            //attach event listener to the radios parent fieldset
            fieldset.addEventListener("input", function oninputFieldsetStore(event){
                /*var childRadios = $(`fieldset[id=${this.id}] input[type=radio]`);

                for(radio of childRadios){
                    localDataStore(radio, load=false);
                }*/
                fnForFieldsetRadios(this, function(radio){ localDataStore(radio, load=false);})
            });
        }
        //load stored data to child radios
/*        var childRadios = $(`fieldset[id=${fieldset.id}] input[type=radio]`);

        for(radio of childRadios){
            localDataStore(radio, load=true);
        }*/

        fnForFieldsetRadios(fieldset, function(radio){localDataStore(radio, load=true);})
    }

}

$(document).ready( 
    function () {

            //setMaxDateToToday();

            var settings = {};

            window.location.search.substr(1).split("&").map( (param) => {
                var settingValue = param.split("=");
                settings[settingValue[0]] = settingValue[1];

            } );

            //if in an HFE form, pathname will be the same, but GET param formId will differ when creating a form for a patient, id will differ when accessing the form from the HFE module pages
            if(settings.formId){
                dataStoreNS+="-id-"+settings.formId;
            } else if(settings.id) {
                dataStoreNS+="-id-"+settings.id;
            }

            //console.log(settings);

            if(settings.tabbed=="true"){
                //console.log("tabbed version");
                
                var tabs = $("#section-tabs");

                //remove hidden attribute from tabs
                tabs.removeAttr("hidden");

                $("#"+sessionStorage[dataStoreNS+"visibleTab"]).tab('show');

                $('#section-tabs a').on('click', 
                    
                    function (event) {

                        event.preventDefault();

                        $(this).tab('show');

                        sessionStorage[dataStoreNS+"visibleTab"]=this.id;

                        document.body.scrollTop = document.documentElement.scrollTop = 0;

                    });

                //this is supposed to be handled for us already... but doesnt seem to be (previously selected tabs continue to show selected state without manually removing active)
                $('a[data-toggle="tab"]').on('shown.bs.tab', 
                    function (e) {
                        //e.target // newly activated tab
                        $(e.relatedTarget).removeClass("active");
                    });

            }else{
                //console.log("monolithic");
                
                //todo: make this default in html and apply these in tabbed, rather than remove, so browser w/o js will still see a usable form

                var tabContent = $("#tab-content-sections");

                //console.log(tabContent);
                tabContent.removeClass("tab-content");

                var panes = $(".tab-pane");

                //console.log(panes);

                for(pane of panes) { 
                    //console.log(pane);
                    $(pane).addClass("show");
                }
            }


            applyScrollPositionPersistence();

            initializeInputValuePersistence(reset=false);
/*
            for(textarea of textAreas){
                textarea.addEventListener("input", function(event){localDataStore(this, load=false)});
            }
*/

            
            loadLocalSiteInfo(true);
        }

);