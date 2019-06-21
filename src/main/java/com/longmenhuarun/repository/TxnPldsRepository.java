package com.longmenhuarun.repository;

import com.longmenhuarun.entity.BatchLog;
import com.longmenhuarun.entity.TxnPlds;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 2 * @Author: Gosin
 * 3 * @Date: 2019/6/20 17:26
 * 4
 */
public interface TxnPldsRepository extends JpaRepository<TxnPlds,String> {
    TxnPlds findByReqMsgId(String msgId);
}
