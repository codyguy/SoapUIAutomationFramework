/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Validate Expected fields from testdata sheet with response in WebServiceTest soap step
 * Write PASSED or FAILED status in testdata sheet
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
import com.eviware.soapui.support.XmlHolder

	
	def xml = new XmlHolder(context.response)
	def testCaseName = testRunner.testCase.getPropertyValue("TESTCASE_NAME")
	def projectName = testRunner.testCase.testSuite.project.name
	def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
	def projectPath = groovyUtils.projectPath
	def testDataFilePath = projectPath+"\\..\\testData\\"+projectName+".xlsx"
	def actual = null
	def status = "PASSED"
	context.testCase.setPropertyValue("FAILED_STATUS",status)

     fis = new FileInputStream(new File(testDataFilePath));
	workbook = new XSSFWorkbook(fis);
	sheet = workbook.getSheet(testCaseName);
	int rowCount = sheet.getLastRowNum().toInteger()
	def rowNum = testRunner.testCase.getPropertyValue("ROW_NUM")
	int row = rowNum.toInteger()
	
	if(row<=rowCount){
		def cellCount = sheet.getRow(row).getLastCellNum()
			for(int j=0; j<cellCount; j++){
				gRow = sheet.getRow(0)
				gCell = gRow.getCell(j)
				String colName = gCell.getStringCellValue()
				if(colName.contains("Expected")){
					gRow = sheet.getRow(row)
					gCell = gRow.getCell(j)
					cellData = gCell.getStringCellValue()
					if (cellData!='')
					{
						def (expectedXpath, expectedValue) = cellData.tokenize(';')
						log.info "$expectedXpath"
						log.info "$expectedValue"
						if(expectedXpath.contains("//"))
						{
							actual = xml.getNodeValue(expectedXpath).toString()
						}
						else
						{
							actual = xml.getNodeValue("//*:"+expectedXpath).toString()
						}
					if(expectedValue.contains("#EXISTS") && xml.getNodeValue("//*:"+expectedXpath)!=null)
					{
						if(xml.getNodeValue("//*:"+expectedXpath).isEmpty()==false)
						{
							status = "PASSED"
						}
						else
						{
							status = "FAILED"
						}
					}
					else
					{
						if(expectedValue == actual && status !='FAILED')
						{
							status = "PASSED"
							context.testCase.setPropertyValue('FAILED_STATUS',status)
						}
						else
						{
							status = "FAILED"
							context.testCase.setPropertyValue('FAILED_STATUS',status)
						}
					}  
				}
			}
		}  
		for(int j=0; j<cellCount; j++){
			gRow = sheet.getRow(0)
			gCell = gRow.getCell(j)
			String colName = gCell.getStringCellValue()
			if(colName.contains("Status")){
				gRow = sheet.getRow(row)
				gCell = gRow.getCell(j)
				gCell.setCellType(gCell.CELL_TYPE_STRING)
				gCell.setCellValue(status)
			}
		} 
	}
	 
	FileOutputStream fos = new FileOutputStream(testDataFilePath)
	workbook.write(fos)
	fos.close()

	row = row+1
	context.testCase.setPropertyValue('ROW_NUM',row.toString())

	if(row>rowCount)
	{
		testRunner.gotoStepByName("End")
	}
	else
	{
		testRunner.gotoStepByName("bootstrap")
	}
