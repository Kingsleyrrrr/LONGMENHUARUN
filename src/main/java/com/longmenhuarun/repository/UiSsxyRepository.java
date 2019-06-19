package com.longmenhuarun.repository;

import com.longmenhuarun.entity.UiSsds;
import com.longmenhuarun.entity.UiSsxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UiSsxyRepository extends JpaRepository<UiSsxy,String> {
    @Modifying
    @Transactional
    @Query(value = "update ui_ssxy xy set xy.status = 'C' where xy.prot_no=?1",nativeQuery=true)
    void cancelSsxyByprotNo(String protNo);

}
