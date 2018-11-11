package exception;

import lombok.Getter;

/**
 * Here be dragons
 * Created by haotian on 2018/11/11 8:10 PM
 */
public class CacheException extends RuntimeException {
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 异常标题
     */
    @Getter
    private String title;

    /**
     * No-args constructor
     */
    public CacheException() {
        super();
    }

    /**
     * @param title
     * @param message
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * @param title
     * @param message
     */
    public CacheException(String title, String message) {
        super(message);
        this.title = title;
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause.
     */
    public CacheException(Throwable cause) {
        super(cause);
    }
}
