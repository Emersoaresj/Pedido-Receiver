package com.fiap.postech.pedido_service.api.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String message;
    private Object data;
}
