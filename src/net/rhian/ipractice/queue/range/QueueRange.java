package net.rhian.ipractice.queue.range;

public interface QueueRange {
    void incrementRange();
    boolean inRange(QueueRange range);
    String rangeToString();

}
