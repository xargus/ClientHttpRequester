package center.xargus.ClientHttpRequester.test.response;

import center.xargus.ClientHttpRequester.annotation.JsonModel;

@JsonModel
public class TestJsonDummyModel {

	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
