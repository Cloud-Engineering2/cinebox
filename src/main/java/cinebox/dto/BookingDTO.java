package cinebox.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import cinebox.entity.Booking;
import lombok.Getter;

@Getter
public class BookingDTO {
    
    private Long bookingId;
    private Long screenId;
    private List<String> seatIds;
    private BigDecimal totalPrice;

    public BookingDTO(Long bookingId, List<String> seatIds, BigDecimal totalPrice) {
        this.bookingId = bookingId;
        this.seatIds = seatIds;
        this.totalPrice = totalPrice;
    }

    public BookingDTO(Long bookingId, List<String> seatIds) {
        this.bookingId = bookingId;
        this.seatIds = seatIds;
        this.screenId = null;  // 기본값으로 null 설정 (필요에 따라 수정)
        this.totalPrice = BigDecimal.ZERO;  // 기본값으로 0을 설정
    }

    // fromEntity 메소드 (totalPrice는 서비스에서 계산 후 전달)
    public static BookingDTO fromEntity(Booking booking) {
        // BookingSeat 리스트를 DTO로 변환
        List<String> seatIds = booking.getBookingSeats().stream()
            .map(bookingSeat -> bookingSeat.getSeat().getSeatNumber())  // 좌석 번호를 가져오기
            .collect(Collectors.toList());

        // DTO 반환 (totalPrice는 서비스에서 계산되어 저장된 값 사용)
        return new BookingDTO(
            booking.getBookingId(),
            seatIds,
            booking.getTotalPrice()  // 이미 계산된 totalPrice
        );
    }
}
