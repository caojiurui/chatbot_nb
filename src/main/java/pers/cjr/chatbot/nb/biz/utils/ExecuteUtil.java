package pers.cjr.chatbot.nb.biz.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class ExecuteUtil {


    public <T> Map<String,T> execAll(Map<String,Callable<T>> callableMap) throws InterruptedException, ExecutionException {
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(callableMap.size());

        List<String> keys = new ArrayList<String>();
        List<Callable<T>> tasks = new ArrayList<Callable<T>>();
        // 提交10个任务
        for (String key: callableMap.keySet()) {
            keys.add(key);
            tasks.add(callableMap.get(key));
        }
        // 获取10个任务的返回结果
        List<Future<T>> results = executorService.invokeAll(tasks);

        Map<String,T> resultMap = new HashMap<String,T>();
        for (int i = 0; i < results.size(); i++){
            // 获取包含返回结果的future对象
            Future<T> future = results.get(i);
            // 从future中取出执行结果（若尚未返回结果，则get方法被阻塞，直到结果被返回为止）
            resultMap.put(keys.get(i),future.get());
        }
        executorService.shutdown();
        return resultMap;
    }

    public <T> Map<String,T> exec(Map<String,Callable<T>> callableMap) throws InterruptedException, ExecutionException {
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(callableMap.size());
        //阻塞队列
        final BlockingQueue<Future<T>> queue = new LinkedBlockingDeque<Future<T>>(callableMap.size());
        //实例化CompletionService
        CompletionService<T> completionService = new ExecutorCompletionService<T>(executorService, queue);

        // 提交10个任务
        for (String key: callableMap.keySet()) {
            completionService.submit(callableMap.get(key));
        }

        Map<String,T> resultMap = new HashMap<String,T>();
        for (String key: callableMap.keySet()) {
            // 获取包含返回结果的future对象（若整个阻塞队列中还没有一条线程返回结果，那么调用take将会被阻塞，当然你可以调用poll，不会被阻塞，若没有结果会返回null，poll和take返回正确的结果后会将该结果从队列中删除）
            Future<T> future = completionService.take();
            // 从future中取出执行结果，这里存储的future已经拥有执行结果，get不会被阻塞
            T result = future.get();
            resultMap.put(key,result);
        }
        return resultMap;
    }
//
//    public static Object exec(int size) throws InterruptedException, ExecutionException {
//        //线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(size);
//        //阻塞队列
//        final BlockingQueue<Future<Integer>> queue = new LinkedBlockingDeque<Future<Integer>>(size);
//        //实例化CompletionService
//        CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executorService, queue);
//
//        // 提交10个任务
//        for ( int i = 0; i < size; i++ ) {
//            final int index = i;
//            completionService.submit(new Callable<Integer>(){
//                public Integer call() throws InterruptedException {
//                    int ran = new Random().nextInt(1000);
//                    Thread.sleep(ran);
//                    System.out.println(Thread.currentThread().getName()+ " 休息了 " + ran);
//                    return ran;
//                }
//            } );
//        }
//
//        // 输出结果
//        for ( int i=0; i<10; i++ ) {
//            // 获取包含返回结果的future对象（若整个阻塞队列中还没有一条线程返回结果，那么调用take将会被阻塞，当然你可以调用poll，不会被阻塞，若没有结果会返回null，poll和take返回正确的结果后会将该结果从队列中删除）
//            Future<Integer> future = completionService.take();
//            // 从future中取出执行结果，这里存储的future已经拥有执行结果，get不会被阻塞
//            Integer result = future.get();
//            System.out.println(result);
//            return result;
//        }
//        return null;
//    }
//
//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        long ss = System.currentTimeMillis();
//        ThreadUtil.exec(10);
//        System.out.println("共耗时:"+(System.currentTimeMillis()-ss));
//    }
}
