/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Fetch the data from testdata sheet where column of first row are like Data
 * Test data sheet is in testData folder having same name as SoapUi project
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

	def testCaseName = testRunner.testCase.getPropertyValue("TESTCASE_NAME")
	def projectName = testRunner.testCase.testSuite.project.name
	def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
	def projectPath = groovyUtils.projectPath
	def testDataFilePath = projectPath+"\\..\\testData\\"+projectName+".xlsx"
     
	fis = new FileInputStream(new File(testDataFilePath));
	workbook = new XSSFWorkbook(fis);
	sheet = workbook.getSheet(testCaseName)

	try{
		int rowCount = sheet.getLastRowNum().toInteger()
		def rowNum = testRunner.testCase.getPropertyValue("ROW_NUM")
		int row = rowNum.toInteger()
		log.info "System is picking test case at row number: $row"

		if(row<=rowCount){
			def cellCount = sheet.getRow(row).getLastCellNum()
			for(int j=0; j<cellCount; j++){
				gRow = sheet.getRow(0)
				gCell = gRow.getCell(j)
				String colName = gCell.getStringCellValue()
				if(colName.contains("Data")){
					gRow = sheet.getRow(row)
					gCell = gRow.getCell(j)
					cellData = gCell.getStringCellValue()
					def (reqTagName, reqTagValue) = cellData.tokenize(';')
					if(reqTagName != null){
						log.info "ppp"
						//tc.setPropertyValue(reqTagValue, reqTagValue)
						testRunner.testCase.setPropertyValue('TAG_NAME', reqTagName)
						testRunner.testCase.setPropertyValue('TAG_VALUE', reqTagValue)
						holder = groovyUtils.getXmlHolder("requestSample#Request")
						if(reqTagName.contains("//") && !holder.containsKey(reqTagName)){
							def itemName = reqTagName.split("//")
							String strItem = "//"
							for(int i=2; i<itemName.size();i++){
								strItem = strItem+itemName[i]
								if(!holder.containsKey(strItem)){
									use (groovy.xml.dom.DOMCategory){
										String strChild = ""
										if(strItem.subString(strItem.length()-1, strItem.length()=="]")){strChild=strItem.subString(0,strItem.length()-3)}
											for(child in holder.getDomNodes(strChild)){
												def node = child.parent()
												def copyItem = node.getOwnerDocument().importNode(child,true)
												node.insertBefore(copyItem,child)
												holder.updateProperty()
												context.requestContent = holder.xml
												return
											}
									}
								}
								if(i<itemName.size()-1){strItem=strItem+"//"}			
							}
						}		
					}		
				}		
			}		
			for(int j=0; j<cellCount; j++){
				gRow = sheet.getRow(0)
				gCell = gRow.getCell(j)
				String colName = gCell.getStringCellValue()
				if(colName.contains("Data")){
					gRow = sheet.getRow(row)
					gCell = gRow.getCell(j)
					cellData = gCell.getStringCellValue()
					def (reqTagName, reqTagValue) = cellData.tokenize(';')
					//tc.setPropertyValue(reqTagValue, reqTagValue)
					testRunner.testCase.setPropertyValue('TAG_NAME', reqTagName)
					testRunner.testCase.setPropertyValue('TAG_VALUE', reqTagValue)
					if(reqTagName != null){
						holder = groovyUtils.getXmlHolder("requestSample#Request")
						if(!reqTagName.contains("//")){reqTagName ="//*:"+reqTagName}
						if(holder.containsKey(reqTagName)){
							holder [reqTagName] = reqTagValue
						}
						else
						{
							log.info "Do Nothing"	
						}
						holder.updateProperty()
						context.requestContent = holder.xml
					}
				}		
			}		
		}
		else
		{
			log.info "There is no test case to execute"
			testRunner.gotoStepByName('End')
		}
	}
	catch(Exception e)
	{
		log.info "$e"
	}
	finally
	{
		workbook.close()
	}