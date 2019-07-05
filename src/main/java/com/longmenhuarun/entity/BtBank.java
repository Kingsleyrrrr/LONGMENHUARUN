package com.longmenhuarun.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/7/2 9:50
 * 4
 */
@Entity
@Data
public class BtBank {
    @Id
    private String bankId;
}
