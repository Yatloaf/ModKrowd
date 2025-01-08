package dev.yatloaf.modkrowd.config.exception;

public class ConfigException extends Exception {
    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
