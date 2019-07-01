package com.longmenhuarun.repository;

import com.longmenhuarun.entity.TxnSsds;
import com.longmenhuarun.entity.UiSsds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UiSsdsRepository extends JpaRepository<UiSsds,String> , JpaSpecificationExecutor<UiSsds> {
}
