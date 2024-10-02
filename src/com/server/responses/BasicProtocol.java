package com.server.responses;
import java.util.Optional;

public class BasicProtocol implements IProtocol {
    private int count = 0;
    private final int maxCount = Response.values().length;
    
    @Override
    public synchronized String Message(String msg) {

        // string sanitisation
        msg = msg.replace("\n", "");
        msg = msg.replace("\r", "");
        msg = msg.strip();

        Optional<Response> triggerMessage = Response.GetByState(this.count, msg);
        if (triggerMessage.isPresent()) {
            String responseString = triggerMessage.get().Value();

            this.count++;
            if (this.count > maxCount) {
                this.count = 0;
            }
            return responseString;

        } else if (true) {
            
        }

        return "";
    }

    enum Response {
        HELLO(0, "HELLO LECTURER", "HELLO STUDENT"),
        WHERE_LECTURE(1, "WHERE IS TODAY’S CLASS?", "IT DEPENDS"),
        THANK(2, "THANK YOU", "YOU’RE WELCOME");

        private final int order;
        private final String msg;
        private final String val;

        Response(int order, String msg, String val) {
            this.order = order;
            this.msg = msg;
            this.val = val;
        }

        public String Message() {
            return this.msg;
        }

        public int Order() {
            return this.order;
        }

        public String Value() {
            return this.val;
        }

        public static Optional<Response> GetByState(int order, String msg) {
            Optional<Response> r = GetByOrder(order);
            if (r.isPresent() && r.get().Message().equals(msg)) {
                return r;
            }
            return Optional.empty();
        }

        public static Optional<Response> GetByOrder(int order) {
            for (Response r : Response.values()) {
                if (r.order == order) {
                    return Optional.of(r);
                }
            }
            return Optional.empty();
        }
    }
}
