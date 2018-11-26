package socket.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Here be dragons
 * Created by haotian on 2018/11/24 4:09 PM
 */
@Builder
@Getter
public class IUserBuilder {

    @NonNull
    private String name;

    private int age;




}
