package com.example.echo.requests;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public class CreateNewGroupConversationRequest extends CreateNewConversationRequest {
    @NotBlank(message = "Group conversations should have a name")
    String conversationName;


    public String getConversationName() {
        return conversationName;
    }


    @Override
    @AssertTrue(message = "Group conversations should have at least three users")
    public Boolean isUsersListContainsOnlyTwoUser() {
        return users.size() < 2;
    }
}
