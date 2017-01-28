package center.xargus.ClientHttpRequester.test.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import center.xargus.ClientHttpRequester.annotation.XmlModel;

@XmlModel
@Root
public class TestXmlDummyModel {

	@Element
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
