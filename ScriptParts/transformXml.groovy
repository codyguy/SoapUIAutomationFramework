/* Author: Preet Kumar
 * emailId: preetkumar897@gmail.com
 * Mobile: 961699669
 * Linkedin: linkedin.com/in/preetkumar897
 * Description: Remove all empty child and parent nodes from the request xml, 
 * #KEEP is a keyword, if user wants to keep the node with empty value in request
 */

def groovyUtils = new com.eviware.soapui.support.GroovyUtils( context )
def req = testRunner.testCase.testSteps["requestSample"].testRequest.getRequestContent()
def holder = groovyUtils.getXmlHolder(req)
//log.info "$holder"
log.info("=======================================debug================================================")
use (groovy.xml.dom.DOMCategory) {
	for( node2 in holder.getDomNodes( "//*" )) {
		node2.depthFirst().each { child ->
			if(child.text().replaceAll("[ ]*[\r\n]*[?]*","") ==  ""){
				child.removeXobj()
			}
		}
	}
	for( node2 in holder.getDomNodes( "//*" )) {
		node2.depthFirst().each { child ->
			if(child.text() ==  "#KEEP"){
				child.value =""
			}
		}
	}
}
return holder.xml