package net.rhian.agathe.queue.range;

public interface QueueRange {
    void incrementRange();
    boolean inRange(QueueRange range);
    String rangeToString();

}
