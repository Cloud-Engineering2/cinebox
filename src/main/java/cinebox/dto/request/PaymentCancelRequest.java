package cinebox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequest {
	private String paymentId;   // 결제 ID
    private String bookingId;   // 예매 ID

}
