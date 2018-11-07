package common.domain;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.ToString;
import org.omg.CORBA.portable.ApplicationException;

/**
 * Here be dragons
 * Created by haotian on 2018/11/7 6:29 PM
 */

@ToString
public class Resp<T> implements Serializable {
    private static final int SUCCESS = 200;
    private static final int ERROR = 500;

    int code;
    String errorHint;
    T data;

    private Resp() {
    }

    public static Resp<Null> success() {
        Resp<Null> resp = new Resp<Null>();
        resp.code = SUCCESS;
        resp.data = Null.NULL;
        return resp;
    }

    public static Resp<Null> convertTo(Resp<?> resp) {
        if (resp.isSuccess()) {
            return success();
        } else {
            return fail(resp.errorHint);
        }
    }

    public <F> Resp<F> map(Function<T, Resp<F>> f) {
        if (!this.isSuccess()) {
            return Resp.fail(this.errorHint);
        }

        return f.apply(this.data);
    }

    public static <T> Resp<T> convertTo(Resp<?> resp, T data) {
        if (resp.isSuccess()) {
            return success(data);
        } else {
            return fail(resp.errorHint);
        }
    }

    public static <T> Resp<T> success(T data) {
        Resp<T> resp = new Resp<T>();
        resp.code = SUCCESS;
        resp.data = data;
        return resp;
    }

    public static <T> Resp<T> fail(String hint) {
        Resp<T> resp = new Resp<T>();
        resp.code = ERROR;
        resp.errorHint = hint;
        return resp;
    }

    public boolean isSuccess() {
        return code == SUCCESS;
    }

    public String getErrorHint() {
        return errorHint;
    }

    public T getData() {
        return data;
    }


    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "code=" + code + ",errorHint=" + errorHint + ",data=" + data;
    }

    public <F> Resp<F> next(Function<T, Resp<F>> f) {
        if (!this.isSuccess()) {
            return Resp.fail(this.errorHint);
        }

        return f.apply(this.data);
    }

    public <F> Resp<F> nextIf(Function<T, Resp<F>> f, F defaultValue) {
        if (this.isSuccess()) {
            return f.apply(this.data);
        }
        return Resp.success(defaultValue);
    }

    public <F> Resp<F> nextIf(Function<T, Resp<F>> f, Supplier<F> defaultValueSupplier) {
        if (this.isSuccess()) {
            return f.apply(this.data);
        }
        return Resp.success(defaultValueSupplier.get());
    }

    public <F> Resp<F> nextIf(boolean condition, Resp<F> defaultResp, String hint) {
        if (condition && defaultResp.isSuccess()) {
            return defaultResp;
        }
        return Resp.fail(hint);
    }

    public <F> Resp<F> nextIf(boolean condition, Supplier<Resp<F>> defaultRespSupplier, String hint) {
        if (condition && defaultRespSupplier.get().isSuccess()) {
            return defaultRespSupplier.get();
        }
        return Resp.fail(hint);
    }

    public <F> Resp<F> nextIfElse(boolean condition, Resp<F> resp1, Resp<F> resp2) {
        if (condition) {
            return resp1;
        }
        return resp2;
    }

    public <F> Resp<F> nextIfElse(boolean condition, Supplier<Resp<F>> supplier1, Supplier<Resp<F>> supplier2) {
        if (condition) {
            return supplier1.get();
        }
        return supplier2.get();
    }

    public <F> Resp<F> nextIfElse(boolean condition, Supplier<Resp<F>> supplier1, Resp<F> resp2) {
        if (condition) {
            return supplier1.get();
        }
        return resp2;
    }

    public <F> Resp<F> nextIfElse(boolean condition, Resp<F> resp1, Supplier<Resp<F>> supplier2) {
        if (condition) {
            return resp1;
        }
        return supplier2.get();
    }

    public <F> Resp<F> nextIfElse(Optional optional, Resp<F> resp1, Resp<F> resp2) {
        if (optional.isPresent()) {
            return resp1;
        }
        return resp2;
    }

    public <F> Resp<F> nextIfElse(Optional optional, Supplier<Resp<F>> supplier1, Supplier<Resp<F>> supplier2) {
        if (optional.isPresent()) {
            return supplier1.get();
        }
        return supplier2.get();
    }

    public <F> Resp<F> nextIfElse(Optional optional, Supplier<Resp<F>> supplier1, Resp<F> resp2) {
        if (optional.isPresent()) {
            return supplier1.get();
        }
        return resp2;
    }

    public <F> Resp<F> nextIfElse(Optional optional, Resp<F> resp1, Supplier<Resp<F>> supplier2) {
        if (optional.isPresent()) {
            return resp1;
        }
        return supplier2.get();
    }

    public <F> Resp<F> nextOptional(Function<T, Optional<F>> f, String errorHint) {
        if (!this.isSuccess()) {
            return Resp.fail(errorHint);
        }
        Optional<F> apply = f.apply(this.data);
        return assertion(apply.isPresent(), apply::get, errorHint);
    }

    public <F> Resp<F> nextOptional(Function<T, Optional<F>> f, Supplier<String> errorHintSupplier) {
        if (!this.isSuccess()) {
            return Resp.fail(errorHintSupplier.get());
        }
        Optional<F> apply = f.apply(this.data);
        return assertion(apply.isPresent(), apply::get, errorHintSupplier);
    }

    public static <T> Resp<T> assertion(Optional<T> optional, String errorHint) {

        return optional.isPresent() ? Resp.success(optional.get()) : Resp.fail(errorHint);
    }

    public static <T> Resp<T> assertion(Optional<T> optional, Supplier<String> errorHintSupplier) {

        return optional.isPresent() ? Resp.success(optional.get()) : Resp.fail(errorHintSupplier.get());
    }

    public static <T> Resp<T> assertion(boolean success, Supplier<T> dataSupplier, String errorHint) {

        return success ? Resp.success(dataSupplier.get()) : Resp.fail(errorHint);
    }

    public static <T> Resp<T> assertion(boolean success, Supplier<T> dataSupplier, Supplier<String> errorHintSupplier) {

        return success ? Resp.success(dataSupplier.get()) : Resp.fail(errorHintSupplier.get());
    }

    public static <T> Resp<T> assertion(boolean success, T data, String errorHint) {

        return success ? Resp.success(data) : Resp.fail(errorHint);
    }

    public static <T> Resp<T> assertion(boolean success, T data, Supplier<String> errorHintSupplier) {

        return success ? Resp.success(data) : Resp.fail(errorHintSupplier.get());
    }
}
