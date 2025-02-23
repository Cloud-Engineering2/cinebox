//package cinebox.dto;
//
//import cinebox.entity.Booking;
//import cinebox.entity.BookingSeat;
//import cinebox.entity.Screen;
//import cinebox.entity.Seat;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//public class BookingSeatDTO {
//
//    private Long bookingSeatId;
//    private Long bookingId;
//    private Long screenId;
//    private Long seatId;
//
//    // 엔티티로 변환하는 메서드
//    public BookingSeat toEntity(Booking booking, Screen screen, Seat seat) {
//        // 기본 생성자를 사용하여 엔티티 생성
//        return new BookingSeat(booking, screen, seat);
//    }
//
//    
//    
//    // 엔티티에서 DTO로 변환하는 메서드
//    // 엔티티에서 DTO로 변환하는 메서드
//    public static BookingSeatDTO fromEntity(BookingSeat bookingSeat) {
//        return new BookingSeatDTO(
//                bookingSeat.getBookingSeatId(),
//                bookingSeat.getBooking().getBookingId(),
//                bookingSeat.getScreen().getScreenId(),
//                bookingSeat.getSeat().getSeatId()
//        );
//    }
//}
