package cheetah.machine;

import cheetah.common.logger.Debug;
import cheetah.common.logger.Info;
import cheetah.event.Event;

import java.util.EventListener;

/**
 * 每个lisnter都会配一Machine负责监控和处理
 * Created by Max on 2016/2/1.
 */
public interface Machine extends Cloneable {

    /**
     * 给机器发送一个指令，让其工作
     * @param directive
     * @return
     */
    default Feedback send(Directive directive) {
        Feedback feedback = Feedback.FAILURE;
        if (directive.needResult()) {
            feedback = completeExecute(directive.event());
            if (feedback.isFail())
                onFailure(directive.event());
            else
                onSuccess(directive.event());
        } else execute(directive.event());

        return feedback;
    }

    /**
     * 机器工作故障后的回调函数
     * @param event
     */
    default void onFailure(Event event) {
        Debug.log(this.getClass(), "Machine execute failure event is [" + event + "]");
    }

    /**
     * 机器工作故障后的回调函数
     * @param event
     */
    default void onSuccess(Event event) {
        Info.log(this.getClass(), "Machine execute success event is [" + event + "]");
    }

    /**
     * 无工作反馈的执行方式
     * @param event
     */
    void execute(Event event);

    /**
     * 有反馈的执行方式
     * @param event
     * @return
     */
    Feedback completeExecute(Event event);

    void setEventListener(EventListener eventListener);

    EventListener getEventListener();

    Machine kagebunsin() throws CloneNotSupportedException;

    Machine kagebunsin(EventListener listener) throws CloneNotSupportedException;

}