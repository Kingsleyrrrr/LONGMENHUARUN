package com.longmenhuarun.Service;


import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.model.SsdsMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SsdsService {
    String createSsdsMsg(SsdsMsg ssdsMsg);
    boolean sendSsdsMsg(String ssReqMsg);
    void insertDB(String ssReqMsg);
    void updateDB(String ssResMsg);
    Page<SsdsVo> findSsdsList(Pageable pageable);

    void dz(String filename);

    boolean checkProtocol(String ssReqMsg);
}
