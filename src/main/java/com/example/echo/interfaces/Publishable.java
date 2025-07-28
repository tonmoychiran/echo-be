package com.example.echo.interfaces;

import com.example.echo.enums.PublishableOpCodeEnum;
import com.example.echo.enums.PublishableTypeEnum;

public interface Publishable {
    PublishableOpCodeEnum getOpCode();
    PublishableTypeEnum getType();
}
