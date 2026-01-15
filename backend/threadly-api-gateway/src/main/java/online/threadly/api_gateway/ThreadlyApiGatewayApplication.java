package online.threadly.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ThreadlyApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreadlyApiGatewayApplication.class, args);
	}

}
