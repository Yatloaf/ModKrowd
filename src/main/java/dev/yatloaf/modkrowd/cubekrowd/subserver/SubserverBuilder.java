package dev.yatloaf.modkrowd.cubekrowd.subserver;

public class SubserverBuilder {
    private String id = null;
    private String listName = null;
    private String[] tabNames = {};

    private FormatChat formatChat = FormatChat::empty;
    private Minigame minigame = Minigame.UNKNOWN;

    private boolean allowCheats = false;
    private boolean hasChattymotes = false;
    private boolean isMinigame = false;
    private boolean isCubeKrowd = true;
    private boolean isReal = true;

    public SubserverBuilder id(String id) {
        this.id = id;
        return this;
    }

    public SubserverBuilder listName(String listName) {
        this.listName = listName;
        return this;
    }

    public SubserverBuilder tabNames(String... tabNames) {
        this.tabNames = tabNames;
        return this;
    }

    public SubserverBuilder name(String name) {
        this.listName = name;
        this.tabNames = new String[]{name};
        return this;
    }

    public SubserverBuilder formatChat(FormatChat formatChat) {
        this.formatChat = formatChat;
        return this;
    }

    public SubserverBuilder mainChat() {
        this.hasChattymotes = true;
        return this.formatChat(FormatChat::rank);
    }

    public SubserverBuilder minigameChat() {
        return this.formatChat(FormatChat::team);
    }

    public SubserverBuilder mixedChat() {
        return this.formatChat(FormatChat::mixed);
    }

    public SubserverBuilder allowCheats() {
        this.allowCheats = true;
        return this;
    }

    public SubserverBuilder minigame(Minigame minigame) {
        this.minigame = minigame;
        this.isMinigame = true;
        return this;
    }

    public SubserverBuilder notCubeKrowd() {
        this.isCubeKrowd = false;
        return this;
    }

    public SubserverBuilder fake() {
        this.isReal = false;
        return this;
    }

    public Subserver build() {
        Subserver result = new Subserver(
                this.id,
                this.listName,
                this.tabNames,
                this.minigame,
                this.formatChat,
                this.allowCheats,
                this.hasChattymotes,
                this.isMinigame,
                this.isCubeKrowd,
                this.isReal
        );
        Subservers.register(result);
        return result;
    }
}
