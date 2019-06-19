package com.longmenhuarun.repository;

import com.longmenhuarun.entity.TxnSsds;
import com.longmenhuarun.entity.UiSsds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnSsdsRepository extends JpaRepository<TxnSsds,String> {
    TxnSsds findByReqMsgId(String reqMsgId);
}
