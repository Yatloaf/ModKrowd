package dev.yatloaf.modkrowd.config.exception;

public class ReadConfigException extends ConfigException {
    public ReadConfigException(String message) {
        super(message);
    }

    public ReadConfigException(Throwable cause) {
        super(cause);
    }
}
