package cheetah.distributor.engine.support;

import cheetah.distributor.engine.EngineDirector;

/**
 * Created by Max on 2016/2/23.
 */
public enum EnginePolicy {
    DEFAULT {
        @Override
        public EngineDirector getEngineDirector() {
            return new DefaultEngineDirector(new DefualtEngineBuilder());
        }
    },
    DISRUPTOR {
        @Override
        public EngineDirector getEngineDirector() {
            return null;
        }
    };

    public abstract EngineDirector getEngineDirector();

    public static EnginePolicy formatFrom(String name) {
        for (EnginePolicy policy : EnginePolicy.values()) {
            if (policy.name().equals(name.toUpperCase())) {
                return policy;
            }
        }
        return DEFAULT;
    }
}
