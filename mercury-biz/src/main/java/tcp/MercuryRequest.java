package tcp;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;

/**
 * Here be dragons Created by @author Ezio on 2019-01-20 00:02
 */
@Builder
@Data
@AllArgsConstructor
public class MercuryRequest {

    private int command;

    private String body;

}
