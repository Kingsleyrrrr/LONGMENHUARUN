package com.longmenhuarun.repository;

import com.longmenhuarun.entity.InfoSsxy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoSsxyRepository extends JpaRepository<InfoSsxy,String> {
    InfoSsxy findByReqMsgId(String reqMsgId);
}
