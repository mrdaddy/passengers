package com.rw.passengers.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
   private long id;
   private String login;
   private String email;
   private String language;
}
