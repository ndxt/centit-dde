package com.centit.dde.consumer;

import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.po.DataPacket;
import com.centit.fileserver.common.FileStore;
import com.centit.fileserver.common.FileStoreContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

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
