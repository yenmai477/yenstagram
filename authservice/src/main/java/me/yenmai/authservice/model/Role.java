package me.yenmai.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO: considering to migrate to enum type
public class Role {
    public final static Role USER = new Role("USER");
    public final static Role SERVICE = new Role("SERVICE");

     private String name;


}
