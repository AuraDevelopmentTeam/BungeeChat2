package dev.aura.bungeechat.packet;

/**
 * Taken from
 * https://github.com/Phoenix616/ResourcepacksPlugins/blob/d765867b9c8d758995907873c2af43d6ec75e1c0/bungee/src/main/java/de/themoep/resourcepacksplugin/bungee/packets/IdMapping.java
 */
public class IdMapping {
  private final String name;
  private final int protocolVersion;
  private final int packetId;

  public IdMapping(String name, int protocolVersion, int packetId) {
    this.name = name;
    this.protocolVersion = protocolVersion;
    this.packetId = packetId;
  }

  public String getName() {
    return name;
  }

  public int getProtocolVersion() {
    return protocolVersion;
  }

  public int getPacketId() {
    return packetId;
  }
}
