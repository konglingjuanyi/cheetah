package cheetah.governor;

import cheetah.common.utils.Assert;
import cheetah.event.Event;
import cheetah.core.plugin.PluginChain;
import cheetah.handler.Feedback;
import cheetah.handler.Handler;

import java.util.EventListener;
import java.util.Map;

/**
 * Created by Max on 2016/3/7.
 */
public class AbstractGovernorAdapter implements Governor {
    private Governor adaptee;

    public AbstractGovernorAdapter(Governor governor, PluginChain pluginChain) {
        this.adaptee = enhance(governor, pluginChain);
    }

    @Override
    public Governor reset() {
        return adaptee.reset();
    }

    @Override
    public Governor initialize() {
        return adaptee.initialize();
    }

    @Override
    public Feedback command() {
        return adaptee.command();
    }

    @Override
    public Governor registerEvent(Event $event) {
        return adaptee.registerEvent($event);
    }

    @Override
    public String getId() {
        return adaptee.getId();
    }

    @Override
    public Governor setFisrtSucceed(boolean $fisrtSucceed) {
        return adaptee.setFisrtSucceed($fisrtSucceed);
    }

    @Override
    public Governor registerHandlerSquad(Map<Class<? extends EventListener>, Handler> handlerMap) {
        return adaptee.registerHandlerSquad(handlerMap);
    }

    @Override
    public Governor setNeedResult(boolean $needResult) {
        return adaptee.setNeedResult($needResult);
    }

    @Override
    public void expelHandler(Handler handler) {
        adaptee.expelHandler(handler);
    }

    @Override
    public Governor kagebunsin() throws CloneNotSupportedException {
        AbstractGovernorAdapter adapter = (AbstractGovernorAdapter) super.clone();
        adapter.setAdaptee(adaptee.kagebunsin());
        return adapter;
    }

    public Governor enhance(Governor governor, PluginChain pluginChain) {
        Assert.notNull(governor, "$adaptee must not be null");
        return (Governor) pluginChain.pluginAll(governor);
    }

    public void setAdaptee(Governor adaptee) {
        this.adaptee = adaptee;
    }

    public Governor getAdaptee() {
        return adaptee;
    }
}