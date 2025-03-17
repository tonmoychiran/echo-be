package com.example.goppho.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public class CreateNewConversationRequest {
    @NotEmpty(message = "Users list is empty")
    @Size(min = 2, message = "At least 2 members are required for a conversation")
    @Size(max = 50, message = "Only 50 members are allowed for a conversation")
    @UniqueElements(message = "Users list items should be unique")
    List<@NotBlank(message = "Users list items should be strings")
            String> users;

    public List<String> getUsers() {
        return users;
    }


    @AssertTrue(message = "Private conversations can have only two users")
    public Boolean isUsersListContainsOnlyTwoUser() {
        return users.size() != 2;
    }
}
