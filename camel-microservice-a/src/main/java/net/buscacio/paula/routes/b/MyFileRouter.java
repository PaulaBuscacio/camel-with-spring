package net.buscacio.paula.routes.b;

import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFileRouter extends RouteBuilder{
	
	@Autowired
	private DeciderBean deciderBean;

	@Override
	public void configure() throws Exception {
		from("file:files/input")
		.routeId("Flies-Input-Route")
		.transform().body(String.class) //transforma em String para entender o contains
		.choice()
		.when(simple("${file:ext} ends with 'xml'"))
			.log("XML FILE")
			//.when(simple("${body} contains 'USD'"))
			.when(method(deciderBean))
			.log("NOT XML FILE BUT CONTAINS USD")
			.otherwise()
				.log("NOT XML FILE")
			.end()
		.to("direct://log-file-values")
		.to("file:files/output");
		
		//direct route com varios logs
		from("direct:log-file-values")
		.log("${messageHistory} ${headers.CamelFileAbsolutePath}")
		.log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
		.log("${file:onlyname.noext} ${file:parent} ${file:path} ${file:absolute}")
		.log("${file:size} ${file:modified}")
		.log("${routeId} ${camelId} ${body}");
		
		
	}

}

@Component
class DeciderBean {
	Logger logger = LoggerFactory.getLogger(DeciderBean.class);
	
	public Boolean isThisConditionMet(@Body String body,
			@Headers Map<String, String> headers,
			@ExchangeProperties Map<String, String> exchangeProperties) {
		logger.info("Decider Bean {} {} {}", body, headers, exchangeProperties);
		return true;
	}
}
