package com.example.account;

import java.io.Serializable;

public record Account(String username, String email) implements Serializable {
}
