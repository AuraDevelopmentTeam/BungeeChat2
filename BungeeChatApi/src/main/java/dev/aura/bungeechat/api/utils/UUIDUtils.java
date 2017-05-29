package dev.aura.bungeechat.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class UUIDUtils {

    public static List<UUID> convertStringList(List<String> stringList) {
        List<UUID> uuids = new ArrayList<>();
        for (String s : stringList) {
            uuids.add(UUID.fromString(s));
        }
        return uuids;
    }

    public static CopyOnWriteArrayList<UUID> convertStringCopyOnWriteArrayList(List<String> stringList) {
        CopyOnWriteArrayList<UUID> uuids = new CopyOnWriteArrayList<>();
        for (String s : stringList) {
            uuids.add(UUID.fromString(s));
        }
        return uuids;
    }

}
