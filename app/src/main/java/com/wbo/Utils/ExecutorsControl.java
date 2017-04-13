package com.wbo.Utils;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 丶 on 2017/4/12.
 */

public class ExecutorsControl {

    private static ExecutorsControl executorsControl;
    private ExecutorService executorService;


    private ExecutorsControl(){
        executorService=Executors.newFixedThreadPool(10);
    }


    public static ExecutorsControl getInstance(){

        if(executorsControl==null){
            executorsControl=new ExecutorsControl();
        }
        return executorsControl;
    }

    public ExecutorService getExecutorService(){

        return  executorService;
    }

}
