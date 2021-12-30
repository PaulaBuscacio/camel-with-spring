package net.buscacio.paula.routes.a;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyFirstTimerRouter extends RouteBuilder{
	
	@Autowired
	private GetCurrentTimeBean getCurrentTimeBean;
	@Autowired
	private SimpleLoggingProcessComponent loggingComponent;

	@Override
	public void configure() throws Exception {
		from("timer:first-timer")
		//transform muda o body da mensagem ao contratio do process
		//.transform().constant("Time is: " + LocalDateTime.now()) //sempre a mesma data e hora porque é uma constante
		.transform().constant("My constant message")
		.log("${body}")
		.bean(getCurrentTimeBean, "getCurrentTime") // hora dinâmica com a chamada do bean. O metodo esta especificado como uma boa pratica ja que a classe pode ter mais de um metodo
		.log("${body}")
		.bean(loggingComponent)
		.log("${body}")
		.process(new SimpleLoggingProcessor())
		.to("log:first-timer");
	
		
	}
	

}

@Component
class GetCurrentTimeBean {
	//retun transformation
	public String getCurrentTime() {
		return "Time is: " + LocalDateTime.now();
	}
}

@Component
class SimpleLoggingProcessComponent {
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessComponent.class);

	public void process(String message) {
		logger.info("SimpleLoggingProcessingComponent {}", message);
	}
}

@Component
class SimpleLoggingProcessor implements Processor {
	private Logger logger = LoggerFactory.getLogger(SimpleLoggingProcessComponent.class);

	@Override
	public void process(Exchange exchange) throws Exception {
		logger.info("SimpleLoggingProcessingComponent {}", exchange.getMessage().getBody());		

	}

}


