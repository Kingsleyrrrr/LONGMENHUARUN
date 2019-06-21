package com.longmenhuarun.Service;


import com.longmenhuarun.Vo.PldsVo;
import com.longmenhuarun.Vo.SsdsVo;
import com.longmenhuarun.model.PldsMsg;
import com.longmenhuarun.model.SsdsMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface PldsService {
   PldsMsg anaCustomFile(String filepath,String filename);

    Map<String,String> genReqPldsFile(PldsMsg pldsMsg);

    boolean sendFile(String encFileName);

    PldsMsg anaRespPldsFile(String name);

    void insertDB(PldsMsg pldsMsg,String batchId);

    void updateDB(PldsMsg pldsMsg);

    Page<PldsVo> findPldsList(Pageable pageable);

    void genCustomFile(PldsMsg pldsMsg,String refFileName);
}
