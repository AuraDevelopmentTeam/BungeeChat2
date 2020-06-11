package dev.aura.bungeechat.packet;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import net.md_5.bungee.protocol.ProtocolConstants;
import net.md_5.bungee.protocol.ProtocolConstants.Direction;

/** A outbound package class. Represents this: https://wiki.vg/Protocol#Named_Sound_Effect */
@Value
@EqualsAndHashCode(callSuper = false)
public class NamedSoundEffectOutPacket extends OutPacket {
  public static final List<IdMapping> ID_MAPPINGS =
      Arrays.asList(
          new IdMapping("1.9", ProtocolConstants.MINECRAFT_1_9, 0x19),
          new IdMapping("1.13", ProtocolConstants.MINECRAFT_1_13, 0x1A),
          new IdMapping("1.14", ProtocolConstants.MINECRAFT_1_14, 0x19),
          new IdMapping("1.15", ProtocolConstants.MINECRAFT_1_15, 0x1A));

  private final String name;
  private final int soundCategory;
  private final int x;
  private final int y;
  private final int z;
  private final float volume;
  private final float pitch;

  public NamedSoundEffectOutPacket() {
    this("", 0, 0, 0, 1, 1);
  }

  public NamedSoundEffectOutPacket(
      String name, double x, double y, double z, float volume, float pitch) {
    this(name, SoundCategory.MASTER, x, y, z, volume, pitch);
  }

  public NamedSoundEffectOutPacket(
      String name,
      SoundCategory soundCategory,
      double x,
      double y,
      double z,
      float volume,
      float pitch) {
    this(name, soundCategory.ordinal(), x, y, z, volume, pitch);
  }

  public NamedSoundEffectOutPacket(
      String name, int soundCategory, double x, double y, double z, float volume, float pitch) {
    this.name = name.replace("minecraft:", "");
    this.soundCategory = soundCategory;
    this.x = (int) (x * 8);
    this.y = (int) (y * 8);
    this.z = (int) (z * 8);
    this.volume = volume;
    this.pitch = pitch;
  }

  public double getX() {
    return x / 8.0D;
  }

  public double getY() {
    return y / 8.0D;
  }

  public double getZ() {
    return z / 8.0D;
  }

  @Override
  public void write(ByteBuf buf, Direction direction, int protocolVersion) {
    writeString(this.name, buf);
    writeVarInt(this.soundCategory, buf);
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    buf.writeFloat(this.volume);
    buf.writeFloat(this.pitch);
  }

  public enum SoundCategory {
    MASTER,
    MUSIC,
    RECORD,
    WEATHER,
    BLOCK,
    HOSTILE,
    NEUTRAL,
    PLAYER,
    AMBIENT,
    VOICE;
  }
}
