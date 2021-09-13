package ink.nest.inest.exception;

import ink.nest.inest.constant.MessagesConstant;

public class ExceptionMessages {
    public static String getNotFoundException(String elm) {
        return String.format(MessagesConstant.NOT_FOUND_FORMAT, elm);
    }

    public static String getInternalSeverException(String elm) {
        return String.format(MessagesConstant.INTERNAL_SERVER_FORMAT, elm);
    }

}
