package center.xargus.ClientHttpRequester.parser;

import center.xargus.ClientHttpRequester.annotation.JsonModel;
import center.xargus.ClientHttpRequester.annotation.XmlModel;

public class ParserFactory {
	public static <T> Parable<T> create(Class classType) {
		return getParserType(classType).getParser();
	}
	
	private enum ParserType {
		JSON(JsonModel.class) {
			@Override
			public <T> Parable<T> getParser() {
				return new JsonParser<>();
			}
		},XML(XmlModel.class) {
			@Override
			public <T> Parable<T> getParser() {
				return new XmlParser<>();
			}
		},NONE(null) {
			@Override
			public <T> Parable<T> getParser() {
				return null;
			}
		};
		
		private Class classType;
		private ParserType(Class classType) {
			this.classType = classType;
		}
		
		public abstract <T> Parable<T> getParser();
	}
	
	private static ParserType getParserType(Class classType) {
		ParserType parserType = ParserType.NONE;
		for (ParserType type : ParserType.values()) {
			if (type.classType == classType) {
				parserType = type;
				break;
			}
		}
		
		return parserType;
	}
}
