package cheetah.fighter.async.disruptor;

import cheetah.fighter.async.AsynchronousFactory;
import cheetah.fighter.core.Interceptor;
import cheetah.fighter.handler.Handler;
import cheetah.fighter.worker.support.DisruptorWorker;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Max on 2016/2/29.
 */
public class DisruptorFactory implements AsynchronousFactory<Disruptor<DisruptorEvent>> {
    private int ringbufferSize = 64;

    public Disruptor<DisruptorEvent> createMultiDisruptor() {
        return new Disruptor<>(new DisruptorEventFactory(), ringbufferSize, Executors.newCachedThreadPool());
    }

    public Disruptor<DisruptorEvent> createSingleDisruptor() {
        WaitStrategy waitStrategy = new BlockingWaitStrategy();
        return new Disruptor<>(new DisruptorEventFactory(), ringbufferSize, Executors.newCachedThreadPool(), ProducerType.SINGLE, waitStrategy);
    }

    public void setRingbufferSize(int ringbufferSize) {
        this.ringbufferSize = ringbufferSize;
    }

    @Override
    public Disruptor<DisruptorEvent> createAsynchronous(String name, Map<Class<? extends EventListener>, Handler> handlerMap,
                                                        List<Interceptor> interceptors) {
        Disruptor<DisruptorEvent> disruptor;
        if (name.equals(ProducerType.SINGLE.name()))
            disruptor = createSingleDisruptor();
        else disruptor = createMultiDisruptor();
        DisruptorWorker worker = new DisruptorWorker();
        worker.setHandlerMap(handlerMap);
        worker.setInterceptors(interceptors);
        disruptor.handleEventsWith(worker);
        disruptor.start();
        return disruptor;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

}