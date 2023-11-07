/***********************************************************************************************************************
*  Copyright 2023 craigde
*
*  Source -https://github.com/craigde/SonosTile
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*
*  Sonos Album Art Tile
*
*  Author: craigde
*  Date: 2023-11-6
*
* This is an application that allows you to select a sonos device and then creates a tile for use in dashboards with the
* album art for the playing track for use with HUBITAT
*
*
***********************************************************************************************************************/

public static String version()      {  return "v1.0"  }


/***********************************************************************************************************************
*
* Version 1
*   11/6/2023: 1.0 - initial version
*/

definition(
    name: "Sonos Album Art Tile",
    namespace: "craigde",
    author: "Craig Dewar",
    description: "App to create sonos album art dashboard tile",
    category: "My Apps",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences {
   
    page(name: "mainPage", title: "Sonos Album Art", install: true, uninstall: true) {
      
    section("Sonos Selection") {
        input "sonosDevice", "capability.musicPlayer", title: "Sonos Device", required:true
    }
    
   // section("File Storage Driver") {
   //     input "fileDevice", "capability.actuator", title: "File Storage", required:true
   // }
    
   section("File Storage Driver") {
        input "fileDevice", "device.FileManagerDevice", title: "File Storage", required:true
    }
        
        
        
    section("Collect Additional Debug information") {
        input "isDebug", "bool", title:"Debug mode", required:true, defaultValue:false
        }    
    }
         
}

def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {

    if (isDebug) {log.debug "sonosDevice.getName: " + sonosDevice.getName()}
    if (isDebug) {log.debug "sonosDevice.getLabel: " + sonosDevice.getLabel()}
      
    if (sonosDevice.getName()=="Sonos Player") {
		subscribe(settings.sonosDevice, "trackData", musicHandler)
    }
    
}

def musicHandler(evt) {
    
    if (isDebug) log.debug "musicHandler() called: name: ${evt.name} value: ${evt.value}"
      
    def json = evt.data
    def jsonResp = new groovy.json.JsonSlurper().parseText(evt.value)
   
    if (jsonResp.audioSource == "Aux Input") {
    }
    else {
       def pos1 = jsonResp.trackMetaData.indexOf("https:")
       def pos2 = jsonResp.trackMetaData.indexOf("upnp")
    
       def trackMetaData = jsonResp.trackMetaData.toString()
       trackMetaData = trackMetaData.substring (pos1, pos1+pos2-1) 
    
    if (isDebug) log.debug "trackData.trackMetaData Image url: " + trackMetaData 
    trackMetaData = cleanUrl(trackMetaData)    
    if (isDebug) log.debug "trackData.trackMetaData Cleaned Image url: " + trackMetaData 
        
    fileDevice.uploadImage(trackMetaData, "album.jpg")
       
    }
}

def cleanUrl (url) {

    def cleanUrl = ""
    
    for (int i = 0; i < url.length(); i++) {
        if (url[i] == "\\") {
        }
        else {
           cleanUrl = cleanUrl + url[i]     
        }
    }            
    if (isDebug) log.debug "cleanUrl: " + cleanUrl 
    return cleanUrl           
}
