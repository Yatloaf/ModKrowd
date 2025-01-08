package dev.yatloaf.modkrowd.cubekrowd.common;

public enum LatencyLevel {
    UNKNOWN,
    L1,
    L2,
    L3,
    L4,
    L5;

    public static LatencyLevel fromLatency(int latency) {
        if (latency < 300) {
            if (latency < 0) {
                return UNKNOWN;
            } else if (latency < 150) {
                return L1;
            } else {
                return L2;
            }
        } else {
            if (latency < 600) {
                return L3;
            } else if (latency < 1000) {
                return L4;
            } else {
                return L5;
            }
        }
    }
}
