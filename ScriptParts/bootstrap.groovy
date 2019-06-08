/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Fetch the following fields from testdata sheet; Execute, Operation, WebService, EndPoint, UserName, Password, KeyStore, KeyPass, Env, TestCase
 * Set the values at different level for test execution
 */
 
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import com.eviware.soapui.settings.SSLSettings
import com.eviware.soapui.SoapUI

	def rowNum = testRunner.testCase.getPropertyValue("ROW_NUM").toInteger()
	def testCaseName = testRunner.testCase.getPropertyValue("TESTCASE_NAME")
	def projectName = testRunner.testCase.testSuite.project.name
	def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
	def projectPath = groovyUtils.projectPath
	def testDataFilePath = projectPath+"\\..\\testData\\"+projectName+".xlsx"
	def XSSFWorkbook workbook = null;
     def XSSFSheet sheet = null;
     def XSSFRow row   =null;
     def XSSFCell cell = null
     def String filename = null
     def String path;
     def FileInputStream fis = null;
     def FileOutputStream fileOut =null;

     fis = new FileInputStream(new File(testDataFilePath));
	workbook = new XSSFWorkbook(fis);
	sheet = workbook.getSheet(testCaseName);
	int rowCount = sheet.getLastRowNum().toInteger()

	if(rowNum<=rowCount){
		gRow = sheet.getRow(rowNum)
		gCell = gRow.getCell(0)
		String exeValue = gCell.getStringCellValue()
		log.info "FLAG $exeValue"
		
		if(exeValue=="Y"){
			colCount = sheet.getRow(rowNum).getLastCellNum()

			for(j=1;j<colCount;j++){
				gRow = sheet.getRow(0)
				gCell = gRow.getCell(j)
				String headerValue = gCell.getStringCellValue()
			//Fetching web service name from the excel sheet
				if(headerValue.contains("WebService")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String webService = gCell.getStringCellValue()
					testRunner.testCase.setPropertyValue('WEBSERVICE', webService)
					log.info "WebService =  $webService"
				}
			
			//Fetching operation name from the excel sheet
				if(headerValue.contains("Operation")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String Operation = gCell.getStringCellValue()
					testRunner.testCase.setPropertyValue('OPERATION', Operation)
					log.info "Operation = $Operation"
				}

			/*fetching end point from the excel sheet and
			Setting endpoint to web service request which is with the test name as requestSample */
				if(headerValue.contains("EndPoint")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String endPoint = gCell.getStringCellValue()
					def end= testRunner.testCase.getTestStepByName("requestSample").getHttpRequest().setEndpoint(endPoint)
					log.info "EndPoint = $endPoint"
				}
			
			/*fetching username from the excel sheet and
			Setting username for authentication to web service request which is with the test name as requestSample*/
				if(headerValue.contains("UserName")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String userName = gCell.getStringCellValue()
					testRunner.testCase.testSteps["requestSample"].httpRequest.setUsername(userName)
					log.info "UserName = $userName"
				}

			/*fetching password from the excel sheet and
			Setting password for authentication to web service request which is with the test name as requestSample */
				if(headerValue.contains("Password")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String passWord = gCell.getStringCellValue()
					testRunner.testCase.testSteps["requestSample"].httpRequest.setPassword(passWord)
					log.info "Password = $passWord"
				}
			
			/*fetching keystore certificate name from the excel sheet and
			Setting keystore certificate (SSL certificate) */
				if(headerValue.contains("KeyStore")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String keyStore = gCell.getStringCellValue()
					def pathToKeystore = projectPath+"/../keyStore/"+keyStore
					SoapUI.settings.setString( SSLSettings.KEYSTORE, pathToKeystore)
					log.info "KeyStore = $keyStore"
				}
			
			/* Fetching Keystore password from excel sheet and
			Setting keystore password (SSL password)*/
				if(headerValue.contains("KeyPass")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String keyPass = gCell.getStringCellValue()
					SoapUI.settings.setString( SSLSettings.KEYSTORE_PASSWORD, keyPass)
					log.info "KeyPass = $keyPass"
				}
			
			/*fetching environment name from excel sheet*/
				if(headerValue.contains("Env")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String environMent = gCell.getStringCellValue()
					testRunner.testCase.setPropertyValue('ENVIRONMENT', environMent)
					log.info "Env = $environMent"
				}

			/* Ftech testcase name from testdata sheet*/
				if(headerValue.contains("TestCase")){
					gRow = sheet.getRow(rowNum)
					gCell = gRow.getCell(j)
					String testName = gCell.getStringCellValue()
					
					testRunner.testCase.setPropertyValue('TEST_CASE_NAME', testName)
					log.info "TEST_CASE_NAME = $testName"
				}
			}
		}
		else{
			int Row = rowNum+1
			testRunner.testCase.setPropertyValue('ROW_NUM', Row.toString())
			testRunner.gotoStepByName("bootstrap")
		}
	}