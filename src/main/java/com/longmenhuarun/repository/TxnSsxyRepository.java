package com.longmenhuarun.repository;

import com.longmenhuarun.entity.TxnSsds;
import com.longmenhuarun.entity.TxnSsxy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnSsxyRepository extends JpaRepository<TxnSsxy,String> {
    TxnSsxy findByReqMsgId(String reqMsgId);
}
