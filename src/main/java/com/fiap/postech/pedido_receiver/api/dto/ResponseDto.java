package com.fiap.postech.pedido_receiver.api.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;
    private Object data;
}
