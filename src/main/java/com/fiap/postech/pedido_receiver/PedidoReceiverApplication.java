package com.fiap.postech.pedido_receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PedidoReceiverApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidoReceiverApplication.class, args);
	}

}
