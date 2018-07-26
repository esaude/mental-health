document.getElementById('age').onchange = function selectProvider() {
    var age = document.getElementById('age').value;
    var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var day = d.getDate();
    var birthYear = year - age;
    var birthDateStr = day + '/'+ month +'/' + birthYear ;
    document.getElementById('birthdate').value = birthDateStr;
}

// Identifier generation process
document.getElementById('family_name').onchange = function generatePatientIdentifier() {  
 var registration = isRegisteration();
 if(registration ){
    var location = getCurrentUserLocation();
    console.log('locationlocationlocationlocation  ==',location);  
  }  
}

function padPatientNumber(noRegistered,idLen){
    noRegistered = Number(noRegistered) + 1;
   var identifierLen = noRegistered.toString().length;
   var requiredPadding = idLen-identifierLen;
   var pad = '';
   if(requiredPadding > 0){
       for(var i = 0; i <requiredPadding; i++){
           pad = pad + '0';
       }

   }
   return pad + noRegistered;
}

function getCurrentUserLocation(){
    
    var userLoc;
    var server = getServerPath();   
    if(server){
        url_content('/'+server + "/ws/rest/v1/appui/session").success(function(data){ 
        userLoc = data?data.sessionLocation:null;  
        getRegisteredPatients(userLoc.uuid);
        document.getElementById('subcounty').value = userLoc ?userLoc.countyDistrict:null  ;   
        document.getElementById('county').value = userLoc ?userLoc.stateProvince:null  ;   
        document.getElementById('village').value = userLoc ?userLoc.cityVillage:null  ;   

        });
        return userLoc;
    }
    
}

function url_content(url){
    return jq.get(url);
}

function getServerPath(){
  var pathname = window.location.pathname;
  return pathname?pathname.split('/')[1]:null; 
}

function getRegisteredPatients(location){
 var encoded = encodeURIComponent("select count(distinct patient_id) registered, la.value_reference name from location l left join patient_identifier pi on l.location_id=pi.location_id left join location_attribute la on la.location_id =l.location_id where la.attribute_type_id=1 and l.uuid ='"+location+"'  group by  la.value_reference");
        var server = getServerPath();   
        if(server){
         url_content("/"+ server+"/moduleServlet/xforms/widgetValueDownload?ExternalSource=" + encoded + "&amp;DisplayField=registered&amp;ValueField=name").success(function(data){
          document.getElementById('identifier').value=data.split('|')[1]+ '-'+ padPatientNumber(data.split('|')[0],5); 
          });
        }

}

function isRegisteration(){
var url = new URL(window.location.href);
var patientId = url.searchParams.get("patientId");
return patientId>0?false:true;

} 
