package com.centit.dde.datamoving;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.centit.dde.dao.TaskExchangeDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.TaskExchange;
import com.centit.dde.po.TaskLog;
import com.centit.framework.core.controller.WrapUpResponseBodyReturnValueHandler;
import com.centit.framework.jdbc.dao.BaseDao;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.product.datapacket.po.DataPacket;
import com.centit.product.datapacket.service.DBPacketBizSupplier;
import com.centit.product.datapacket.service.DataPacketService;
import com.centit.product.datapacket.service.impl.DataPacketServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
//@ComponentScan(basePackages = "com.centit.dde")
public class DataMovingApplication  {

    /**
     * 这个只做一个任务的一次交换
     * 由 agent 调度
     * @param args args[0] 为任务ID
     */

    public static void main(String[] args) {
        if(args==null || args.length<1){
            return;
        }
        String taskLogId = args[0];
        System.out.println(taskLogId);
        //通过 TaskLogId 获取到 TaskId
        // 通过TaskId 获取 DataPacketId
        // 通过 DataPacketId创建 DBPacketBizSupplier
        // 通过TaskInfo 创建 DatabaseBizOperation
        // 这行 DatabaseBizOperation 就完成工作了
        // 每一步需要编写日志

        ConfigurableApplicationContext context = SpringApplication.run(DataMovingApplication.class, args);
        DataSource dataSource = context.getBean(DataSource.class);
        TaskLogDao taskLogDao = new TaskLogDao();
        taskLogDao.setDataSource(dataSource);
        TaskLog taskLog = taskLogDao.getObjectById("1");
        TaskExchangeDao taskExchangeDao = new TaskExchangeDao();
        taskExchangeDao.setDataSource(dataSource);
        TaskExchange taskExchange = taskExchangeDao.getObjectById("1");
        DataPacketService dataPacketService = new DataPacketServiceImpl();
        DataPacket dataPacket = dataPacketService.getDataPacket("1");
        DBPacketBizSupplier dbPacketBizSupplier = new DBPacketBizSupplier(dataPacket);
        dbPacketBizSupplier.get();

    }
}
