package com.longmenhuarun.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/12 21:43
 * 4
 */

@Entity
@Data
public class RetcdCode {
    @Id
    String codeVal;
    String DESCR;
}
