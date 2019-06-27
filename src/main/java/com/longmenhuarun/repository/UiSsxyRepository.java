package com.longmenhuarun.repository;

import com.longmenhuarun.entity.UiSsds;
import com.longmenhuarun.entity.UiSsxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UiSsxyRepository extends JpaRepository<UiSsxy,String> {

}
