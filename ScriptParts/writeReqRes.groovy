/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Write request and response of each test case in Output/runtime folder for the evidences
 */

import com.eviware.soapui.support.*; 

def testCase = testRunner.testCase.getPropertyValue("TEST_CASE_NAME")
log.info "$testCase"
def outputFolderPath = testRunner.testCase.getPropertyValue("OUTPUT_FOLDER_PATH")

//Get Current date time stamp to append the same in the file name. 
def date = new Date() 
def dts = date.format("yyyy-MM-dd-HH-mm-ss") 

//Write Request to a XML File and save it with Date Time stamp appended to your filename. 
def myXmlRequest = outputFolderPath+"\\"+testCase+"_Request_"+dts+".xml" 
context.testCase.setPropertyValue("OP_REQUEST",myXmlRequest)
def request = context.expand('${WebServiceTest#Request}')
def req = new File(myXmlRequest) 
req.write(request, "UTF-8") 

// Write Response to a XMl File and save it with Date Time stamp appended to your filename. 
def myXmlResponse = outputFolderPath+"\\"+testCase+"_Response_"+dts+".xml" 
context.testCase.setPropertyValue("OP_RESPONSE",myXmlResponse)
def response = context.expand( '${WebServiceTest#Response}' ) 
def res = new File(myXmlResponse) 
res.write(response, "UTF-8")


