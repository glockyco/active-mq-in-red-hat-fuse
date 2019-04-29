package org.mycompany;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component // Remove "//" to enable the routes => make sure to disable the XML routes before
public class SampleRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:timer?repeatCount=10").to("activemq:queue:input");
		from("timer:timer?repeatCount=10").to("activemq:queue:input");
		from("timer:timer?repeatCount=10").to("activemq:queue:input");
		
		from("activemq:queue:input")
			.to("log:preprocessing?groupInterval=1000&groupDelay=1000")
			.to("activemq:queue:preprocessed");
		
		from("activemq:queue:preprocessed?destination.consumer.prefetchSize=1")
			.throttle(1).timePeriodMillis(1000)
			.to("activemq:queue:output");
		
		from("activemq:queue:preprocessed?destination.consumer.prefetchSize=1")
			.throttle(1).timePeriodMillis(2000)
			.to("activemq:queue:output");
		
		from("activemq:queue:output")
			.to("log:output?groupInterval=1000&groupDelay=1000");
	}
	
}
