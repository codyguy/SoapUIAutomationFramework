/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Creates sample request in sampleXML folder based on given Web service name and Web service operation name 
 * in each row in testdata sheet,
 */
	
	def projectName = testRunner.testCase.testSuite.project.name
	def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
	def projectPath = groovyUtils.projectPath
	def rowNum = testRunner.testCase.getPropertyValue("ROW_NUM").toInteger()
	
	def webService = testRunner.testCase.getPropertyValue("WEBSERVICE")
	def operation = testRunner.testCase.getPropertyValue("OPERATION")

	map = context.testCase.testSuite.project.interfaces[webService].operations

	for (entry in map)
	{
	opName = entry.getKey()
	if(opName==operation){
	inputFileRequest = new File(projectPath+"\\..\\sampleXML\\" + opName + ".xml")
	log.info "$inputFileRequest"
	def sampleXml = testRunner.testCase.setPropertyValue('SAMPLE_XML', inputFileRequest.toString())
	inputFileRequest.write(entry.getValue().createRequest(true))
	break
	}
	}
	def pickXml= testRunner.testCase.getPropertyValue("SAMPLE_XML")
	def xml = new File(pickXml).getText()
	xml.replaceAll("(?s)\\s*/\\*.*\\*/","")
	//log.info "$xml"
	