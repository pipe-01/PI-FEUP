package cosn.group2.realsystem.model;

import java.io.Serializable;
import java.util.Objects;

public class Log implements Serializable {

    public enum LogType {INFORMATION, WARNING, FATAL, DEBUG, OTHER}
    public enum OperationType {CREATE, UPDATE, DELETE, READ, OTHER}

    public LogType messageType;
    public OperationType operationType;
    public String message;
    public String serviceName;

    public static Log instance(String message) {
        return instance(LogType.INFORMATION, message);
    }

    public static Log instance(LogType messageType, String message) {
        return instance(messageType, OperationType.OTHER, message);
    }

    public static Log instance(LogType messageType, OperationType operationType, String message) {
        return new Log(messageType, operationType, message);
    }

    public Log() {}

    public Log(LogType messageType, OperationType operationType, String message) {
        this.messageType = messageType;
        this.operationType = operationType;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return messageType == log.messageType && operationType == log.operationType && Objects.equals(message, log.message) && Objects.equals(serviceName, log.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, operationType, message, serviceName);
    }

    @Override
    public String toString() {
        return "Log{" +
                "messageType=" + messageType +
                ", operationType=" + operationType +
                ", message='" + message + '\'' +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
