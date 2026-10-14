package com.example.wonit.dto;

import com.example.wonit.model.Account;

public record AccountCreateRequest(Integer customerId, String accountType){
}
