package com.longmenhuarun.Service;


import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.model.SsdsMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SsdsService {
    String createSsdsMsg(SsdsMsg ssdsMsg);
    boolean sendSsdsMsg(String ssReqMsg);
    void insertDB(String ssReqMsg);
    void updateDB(SsdsMsg ssdsMsg);
    Page<SsdsVo> findSsdsList(SsdsMsg ssdsMsg,Pageable pageable);
    void  genCustomFile(String decFileName);
    String dz(String filename);

    String checkProtocol(SsdsMsg ssdsMsg);

    boolean checkBankId(String bankId);
}
