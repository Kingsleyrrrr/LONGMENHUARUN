package com.longmenhuarun.Service;


import com.longmenhuarun.Vo.SsxyVo;
import com.longmenhuarun.model.SsxyMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface SsxyService {
    String createSsxyMsg(SsxyMsg ssxyMsg);
    boolean sendSsxyMsg(String ssReqMsg);
    void insertDB(String ssReqMsg);
    Page<SsxyVo> findSsxyList(Pageable pageable);
    void updateDB(SsxyMsg ssxyMsg);
    String cancelSsxyMsg(String ReqMsgNo,String msgId);

    Page<SsxyVo> findSsxyRecord(Pageable pageable);
}
