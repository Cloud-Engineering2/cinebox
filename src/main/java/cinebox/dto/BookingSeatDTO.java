package cinebox.dto;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.repository.BookingRepository;
import cinebox.repository.ScreenRepositoryTest;
import cinebox.repository.SeatRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class BookingSeatDTO {
    private Long bookingSeatId;
    private Long bookingId;
    private Long screenId;
    private Long seatId;
    private String seatNumber; // 좌석 번호 추가
    private BookingStatus status; // 예매 상태 추가

    // 생성자
    public BookingSeatDTO(Long bookingSeatId, Long bookingId, Long screenId, Long seatId, String seatNumber, BookingStatus status) {
        this.bookingSeatId = bookingSeatId;
        this.bookingId = bookingId;
        this.screenId = screenId;
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.status = status;
    }
    
    // DTO -> 엔티티 변환
    public BookingSeat toEntity(BookingRepository bookingRepository, ScreenRepositoryTest screenRepositoryTest, SeatRepository seatRepository) {
        Booking booking = bookingRepository.findById(this.bookingId)
                                          .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Screen screen = screenRepositoryTest.findById(this.screenId)
                                        .orElseThrow(() -> new IllegalArgumentException("Screen not found"));
        Seat seat = seatRepository.findById(this.seatId)
                                  .orElseThrow(() -> new IllegalArgumentException("Seat not found"));

        return BookingSeat.builder()
                          .booking(booking)
                          .screen(screen)
                          .seat(seat)
                          .status(this.status) // 상태 값 반영
                          .build();
    }
    
    
  
    // 엔티티 -> DTO 변환 메서드
    public static BookingSeatDTO fromEntity(BookingSeat bookingSeat) {
        return BookingSeatDTO.builder()
                .bookingSeatId(bookingSeat.getBookingSeatId())
                .bookingId(bookingSeat.getBooking().getBookingId())
                .screenId(bookingSeat.getScreen().getScreenId())
                .seatId(bookingSeat.getSeat().getSeatId())
                .seatNumber(bookingSeat.getSeat().getSeatNumber())
                .status(bookingSeat.getStatus()) // 예매 상태 추가
                .build();
    }
}
