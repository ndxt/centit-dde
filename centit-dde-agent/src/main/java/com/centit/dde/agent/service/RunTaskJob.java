package com.centit.dde.agent.service;

import com.centit.dde.po.DataPacket;
import com.centit.dde.services.impl.TaskRun;
import com.centit.support.quartz.AbstractQuartzJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhf
 */
//@DisallowConcurrentExecution
public class RunTaskJob extends AbstractQuartzJob {
    private static ConcurrentHashMap<String, Boolean> runningTask = new ConcurrentHashMap<>(128);

    public RunTaskJob() {
    }

    @Override
    protected void loadExecutionContext(JobExecutionContext context) {

    }

    @Override
    protected boolean runRealJob(JobExecutionContext context) {
        JobDataMap paramMap = context.getMergedJobDataMap();
        DataPacket dataPacket = (DataPacket) paramMap.get("taskExchange");

        PathConfig pathConfig = ContextUtils.getBean(PathConfig.class);
        boolean userMoving = "true".equalsIgnoreCase(pathConfig.getUseDataMoving());

        if (userMoving) {
            try {
                Runtime.getRuntime().exec("java -jar " + pathConfig.getDataMovingPath() + " " + dataPacket.getPacketId());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else {
            Boolean isRunning = runningTask.get(dataPacket.getPacketId());
            if(isRunning!=null && isRunning){
                logger.info("任务："+dataPacket.getPacketId()+ "仍在执行中或略这个执行周期");
                return true;
            }
            try {
                runningTask.put(dataPacket.getPacketId(), true);
                TaskRun taskRun = ContextUtils.getBean(TaskRun.class);
                taskRun.runTask(dataPacket.getPacketId(), null); // paramMap
            } finally {
                runningTask.put(dataPacket.getPacketId(), false);
            }
        }
//      //取得命令结果的输出流
//      InputStream fis=p.getInputStream();
//      //用一个读输出流类去读
//      InputStreamReader isr=new InputStreamReader(fis);
//      //用缓冲器读行
//      BufferedReader br=new BufferedReader(isr);
//      String line=null;
//      //直到读完为止
//      while((line=br.readLine())!=null)
//      {
//          System.out.println(line);
//      }
        return true;
    }
}
