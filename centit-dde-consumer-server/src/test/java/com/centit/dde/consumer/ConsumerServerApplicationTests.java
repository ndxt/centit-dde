package com.centit.dde.consumer;

import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ConsumerServerApplicationTests {
    @Resource
    DataPacketDao dataPacketDao;

    @Test
    void contextLoads() {
        DataPacket objectById = dataPacketDao.getObjectById("159cbd136539491a83b320e232340688");
    }

}
