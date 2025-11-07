package com.StayEasedev.StayEase.service.interfac;



import com.StayEasedev.StayEase.dto.Response;
import com.StayEasedev.StayEase.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String confirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
