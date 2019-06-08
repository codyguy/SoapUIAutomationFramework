/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Create folder structure which is required to run the test case. Initiate the test case with row 1.
 */

import groovy.io.FileType

//Get project name
def groovyUtils=new  com.eviware.soapui.support.GroovyUtils(context)
def projectPath = groovyUtils.projectPath

//Get testcase name
def testCase = testRunner.testCase.name
testRunner.testCase.setPropertyValue('TESTCASE_NAME', testCase)

//Get timestamp
def timeStamp = new Date().format("yyyy-MM-dd_HH-mm-ss")
testRunner.testCase.testSuite.project.setPropertyValue('TIMESTAMP', timeStamp)


//Set output path
def outputFolderPath = projectPath+"\\..\\output\\"+testCase+timeStamp
testRunner.testCase.setPropertyValue('OUTPUT_FOLDER_PATH', outputFolderPath)

//Create folder inside Output folder if does not exist
def folder = new File(outputFolderPath)
if (!folder.exists()){
	folder.mkdirs()
}

//Setting ROW_NUM to 1 to read the test data from row number one, 
//before every run it will be set to 1 to start the test from row number 1
String rowNum = 1
testRunner.testCase.setPropertyValue('ROW_NUM', rowNum)