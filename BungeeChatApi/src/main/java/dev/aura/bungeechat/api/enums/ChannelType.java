package dev.aura.bungeechat.api.enums;

/**
 * An Enum that contains all channel types.<br>
 * This is used to differentiate in which channel a person is talking, and the message needs to be
 * replicated.
 */
public enum ChannelType {
  GLOBAL,
  LOCAL,
  STAFF,
  HELP,
  MULTICAST
}
