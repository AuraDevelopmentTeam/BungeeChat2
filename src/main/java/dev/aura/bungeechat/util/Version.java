package dev.aura.bungeechat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
    private static final String separator = "(?:\\.|_|-)";
    private static final Pattern versionPattern = Pattern.compile("[0-9]+(?:" + separator + "[0-9]+)*");

    private String version;

    public Version(String version) {
        if (version == null)
            throw new IllegalArgumentException("Version can not be null");
        
        Matcher match = versionPattern.matcher(version);
        
        if (!match.find())
            throw new IllegalArgumentException("Invalid version format");

        this.version = match.group();
    }

    public final String get() {
        return version;
    }

    @Override
    public int compareTo(Version that) {
        if (that == null)
            return 1;

        String[] thisParts = get().split(separator);
        String[] thatParts = that.get().split(separator);
        int length = Math.max(thisParts.length, thatParts.length);

        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ? parseInt(thatParts[i]) : 0;

            if (thisPart < thatPart)
                return -1;
            if (thisPart > thatPart)
                return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (that == null)
            return false;

        if (this.getClass() != that.getClass())
            return false;

        return compareTo((Version) that) == 0;
    }

    private static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
