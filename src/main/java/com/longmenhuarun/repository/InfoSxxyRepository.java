package com.longmenhuarun.repository;

import com.longmenhuarun.entity.InfoSsxy;
import com.longmenhuarun.entity.InfoSxxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InfoSxxyRepository extends JpaRepository<InfoSxxy,String> {
    @Query(value="select * from info_sxxy u where u.org_id=?1 and u.tran_type=?2 and u.user_no=?3",nativeQuery=true)
    InfoSxxy checkProt(String orgId, String tranType, String userNo);
}
