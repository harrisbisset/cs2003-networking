package com.server.responses;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AdvancedProtocol implements IProtocol {
    private int count = 0;
    private final int maxCount = Response.values().length;
    private final ScheduleData schedule = new ScheduleData();
    
    @Override
    public AdvancedProtocol generic() {
        return new AdvancedProtocol();
    }

    @Override
    public synchronized String message(String msg) {

        // string sanitisation
        msg = msg.replace("\n", "");
        msg = msg.replace("\r", "");
        msg = msg.strip();

        Optional<Response> triggerMessage = Response.getReponseByState(this.count, msg);
        if (triggerMessage.isPresent()) {
            String responseString;

            if (triggerMessage.get() == Response.WHERE_LECTURE) {
                responseString =  this.schedule.getLocation();
            } else {
                responseString = triggerMessage.get().getValue();
            }

            this.count++;
            if (this.count > maxCount) {
                this.count = 0;
            }
            return responseString;

        } else if (this.count == 2) {
            return Response.WHERE_LECTURE.getValue();
        }

        return "";
    }

    @Override
    public synchronized boolean endSession() {
        return this.count == maxCount;
    }

    enum Response {
        HELLO(0, "HELLO LECTURER", "HELLO STUDENT"),
        WHERE_LECTURE(1, "WHERE IS TODAY'S CLASS?", "IT DEPENDS"),
        THANK(2, "THANK YOU", "YOU'RE WELCOME");

        private final int order;
        private final String msg;
        private final String val;

        Response(int order, String msg, String val) {
            this.order = order;
            this.msg = msg;
            this.val = val;
        }

        public String getMessage() {
            return this.msg;
        }

        public int getOrder() {
            return this.order;
        }

        public String getValue() {
            return this.val;
        }

        public static Optional<Response> getReponseByState(int order, String msg) {
            Optional<Response> r = getReponseByOrder(order);
            if (r.isPresent() && r.get().getMessage().equals(msg)) {
                return r;
            }
            return Optional.empty();
        }

        public static Optional<Response> getReponseByOrder(int order) {
            for (Response r : Response.values()) {
                if (r.order == order) {
                    return Optional.of(r);
                }
            }
            return Optional.empty();
        }
    }

    class ScheduleData {
        private final HashMap<DayOfWeek, String> dayLocation = new HashMap<>();
        private final ArrayList<LocalDate> holidays = new ArrayList<>();

        public ScheduleData() {
            this.dayLocation.put(DayOfWeek.MONDAY, "Jack Cole Building");
            this.dayLocation.put(DayOfWeek.TUESDAY, "School II");
            this.dayLocation.put(DayOfWeek.THURSDAY, "Jack Cole Building");

            // independent learning week
            this.holidays.add(LocalDate.of(2024, Month.OCTOBER, 21));
            this.holidays.add(LocalDate.of(2024, Month.OCTOBER, 22));
            this.holidays.add(LocalDate.of(2024, Month.OCTOBER, 23));
            this.holidays.add(LocalDate.of(2024, Month.OCTOBER, 24));
            this.holidays.add(LocalDate.of(2024, Month.OCTOBER, 25));
        }

        public String getLocation() {
            LocalDateTime today = LocalDateTime.now();

            if (
                !this.dayLocation.containsKey(today.getDayOfWeek()) || 
                this.holidays.contains(LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth()))
            ) {
                return "there is no class today";
            }
            
            return this.dayLocation.get(today.getDayOfWeek());
        }
    }
}
