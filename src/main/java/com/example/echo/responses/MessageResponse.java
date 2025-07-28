package com.example.echo.responses;

import com.example.echo.entities.MessageEntity;
import com.example.echo.enums.PublishableOpCodeEnum;
import com.example.echo.enums.PublishableTypeEnum;
import com.example.echo.interfaces.Publishable;

public class MessageResponse implements Publishable {

    MessageEntity data;
    PublishableTypeEnum type;

    public MessageResponse(
            MessageEntity data,
            PublishableTypeEnum type
    ) {
        this.data = data;
        this.type = type;
    }

    @Override
    public PublishableOpCodeEnum getOpCode() {
        return PublishableOpCodeEnum.DISPATCH;
    }

    @Override
    public PublishableTypeEnum getType() {
        return this.type;
    }

    public MessageEntity getData() {
        return data;
    }

}
