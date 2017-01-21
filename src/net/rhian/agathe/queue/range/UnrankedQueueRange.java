package net.rhian.agathe.queue.range;

public class UnrankedQueueRange implements QueueRange {

    @Override
    public void incrementRange() {

    }

    @Override
    public boolean inRange(QueueRange range) {
        return true;
    }

    @Override
    public String rangeToString() {
        return "[Unranked]";
    }
}
