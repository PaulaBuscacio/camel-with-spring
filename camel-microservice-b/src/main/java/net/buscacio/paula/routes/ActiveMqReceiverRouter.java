package net.buscacio.paula.routes;

import java.math.BigDecimal;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.buscacio.paula.CurrencyExchange;

@Component
public class ActiveMqReceiverRouter extends RouteBuilder{

	@Autowired
	MyCurrencyExchangeTransformer myCurrencyExchangeTransformer;
	
	@Override
	public void configure() throws Exception {
//		from("activemq:my-activemq-queue")
//		.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
//		.bean(myCurrencyExchangeTransformer)
//		.to("log:received-message-from-microserviceA");
		
		from("activemq:my-activemq-xml-queue")
		.unmarshal().jacksonxml( CurrencyExchange.class)
//		.bean(myCurrencyExchangeTransformer)
		.to("log:received-message-from-microserviceA");
		
	}

}

//@Component
//class MyCurrencyExchangeProcessor {
//	
//	
//	Logger logger = LoggerFactory.getLogger(ActiveMqReceiverRouter.class);
//	
//	public void processMessage(CurrencyExchange currencyExchange) {
//		
//		logger.info("Do some processing with currencyExchange.getConversionMultple() "
//				+ "value which is {}", currencyExchange.getConversionMultiple());
//		
//	}
//
//}

@Component
class MyCurrencyExchangeTransformer {
	
	public CurrencyExchange processMessage(CurrencyExchange currencyExchange) {
		
		currencyExchange.setConversionMultiple(
				currencyExchange.getConversionMultiple().multiply(BigDecimal.TEN));
	
		return currencyExchange;

		
	}

}



