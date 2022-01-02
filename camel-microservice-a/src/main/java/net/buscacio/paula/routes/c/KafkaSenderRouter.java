package net.buscacio.paula.routes.c;

import org.apache.camel.builder.RouteBuilder;

import org.springframework.stereotype.Component;
//subir o kafka com docker compose para ler o yaml da raiz do projeto
//@Component
public class KafkaSenderRouter extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		
		from("file:files/json")
		.log("${body}")
		.to("kafka:myKafkaTopic");
	}

}
