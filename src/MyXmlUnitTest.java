import org.custommonkey.xmlunit.*;

public class MyXmlUnitTest extends XMLTestCase {
	public MyXmlUnitTest(String name) {
			super(name);
		}
			public void testForEquality() throws Exception {
			String myControlXML = "<msg><uuii>2376</uuii></msg>";
			String myTestXML = "<msg><uuid>2376</uuid></msg>";
			Diff myDiff = new Diff(myControlXML, myTestXML);

			assertTrue("xml is similar"+myDiff.toString(), myDiff.similar());
			assertTrue("xml is identical"+myDiff.toString(), myDiff.identical());

			//assertXMLIdentical("Comparing test xml to control xml",
			//myDiff, true);
		}
}

