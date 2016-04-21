package cheetah.predator.server.support;

import cheetah.commons.logger.Loggers;
import cheetah.predator.core.Message;
import cheetah.predator.core.PreyPacket;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by Max on 2016/4/21.
 */
public class MessageEncoder extends MessageToMessageDecoder<Message> {
    private ObjectMapper objectMapper;

    public MessageEncoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Message msg, List<Object> list) throws Exception {
        byte[] body = objectMapper.writeValueAsBytes(msg);

        PreyPacket packet = PreyPacket.empty().type(PreyPacket.POST_TYPE).body(body);
        list.add(packet);
        Loggers.me().info(
                getClass(),
                "encode message success. from:{}, to:{}, type:{}, digestSize:{}, bodySize:{}.",
                msg.from(), msg.to(), msg.type(), packet.digestSize(), packet.bodySize()
        );
    }
}
