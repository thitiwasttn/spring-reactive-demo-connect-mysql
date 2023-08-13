package com.thitiwas.reactive.democonnectapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    private String id;
    private String email;
    private Date loginExpired;
    private String LoginExpiredStr;
    private String isConfirm;
    private String isDelete;
    private String telno;

}
