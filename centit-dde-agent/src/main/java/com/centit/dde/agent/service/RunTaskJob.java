package com.centit.dde.agent.service;

import com.centit.dde.po.DataPacket;
import com.centit.dde.services.impl.TaskRun;
import com.centit.support.quartz.AbstractQuartzJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author zhf
 */
public class RunTaskJob extends AbstractQuartzJob {
    private DataPacket dataPacket;

    public RunTaskJob() {
    }

    @Override
    protected void loadExecutionContext(JobExecutionContext context) {
        JobDataMap paramMap = context.getMergedJobDataMap();
        this.dataPacket = (DataPacket) paramMap.get("taskExchange");
    }

    @Override
    protected boolean runRealJob(JobExecutionContext context) {
        PathConfig pathConfig = ContextUtils.getBean(PathConfig.class);
        boolean userMoving="true".equalsIgnoreCase(pathConfig.getUseDataMoving());
        try {
            if(userMoving){
                Runtime.getRuntime().exec("java -jar " + pathConfig.getDataMovingPath() + " " + dataPacket.getPacketId());
            }else {
                TaskRun taskRun = ContextUtils.getBean(TaskRun.class);
                taskRun.runTask(dataPacket.getPacketId());
            }
//            //取得命令结果的输出流
//            InputStream fis=p.getInputStream();
//            //用一个读输出流类去读
//            InputStreamReader isr=new InputStreamReader(fis);
//            //用缓冲器读行
//            BufferedReader br=new BufferedReader(isr);
//            String line=null;
//            //直到读完为止
//            while((line=br.readLine())!=null)
//            {
//                System.out.println(line);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
