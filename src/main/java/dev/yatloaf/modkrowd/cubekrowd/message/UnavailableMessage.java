package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;

public interface UnavailableMessage extends Message {
    Subserver subserver();
}
