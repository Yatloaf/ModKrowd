package dev.yatloaf.modkrowd.cubekrowd.subserver;

public abstract class RealSubserver extends CubeKrowdSubserver {
    public RealSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public boolean isReal() {
        return true;
    }
}
