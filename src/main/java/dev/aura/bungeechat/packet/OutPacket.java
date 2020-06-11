package dev.aura.bungeechat.packet;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.ProtocolConstants;

public abstract class OutPacket extends DefinedPacket {
  protected static final String errorMessage = "This is an outbound only packet!";

  @Override
  public final void handle(AbstractPacketHandler handler) throws Exception {
    throw new UnsupportedOperationException(errorMessage);
  }

  @Override
  public final void read(ByteBuf buf) {
    throw new UnsupportedOperationException(errorMessage);
  }

  @Override
  public final void read(ByteBuf buf, ProtocolConstants.Direction direction, int protocolVersion) {
    throw new UnsupportedOperationException(errorMessage);
  }
}
