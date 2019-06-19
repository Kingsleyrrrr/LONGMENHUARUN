package com.longmenhuarun.repository;

import com.longmenhuarun.entity.RetcdCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RetcdRepository extends JpaRepository<RetcdCode,String> {
    @Query(value="select DESCR from retcd_code u where u.code_type='RETCD' and u.code_val=?1",nativeQuery=true)
    String findDescr(String codeVal);
}
