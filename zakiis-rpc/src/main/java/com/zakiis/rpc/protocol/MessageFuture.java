package com.zakiis.rpc.protocol;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.zakiis.rpc.exception.ShouldNeverHappenException;

public class MessageFuture {
    private RpcMessage requestMessage;
    private long timeout;
    private long start = System.currentTimeMillis();
    private transient CompletableFuture<Object> origin = new CompletableFuture<>();

    /**
     * Is timeout boolean.
     *
     * @return the boolean
     */
    public boolean isTimeout() {
        return System.currentTimeMillis() - start > timeout;
    }

    /**
     * Get object.
     *
     * @param timeout the timeout
     * @param unit    the unit
     * @return the object
     * @throws TimeoutException the timeout exception
     * @throws InterruptedException the interrupted exception
     */
    public Object get(long timeout, TimeUnit unit) throws TimeoutException,
        InterruptedException {
        Object result = null;
        try {
            result = origin.get(timeout, unit);
            if (result instanceof TimeoutException) {
                throw (TimeoutException)result;
            }
        } catch (ExecutionException e) {
            throw new ShouldNeverHappenException("Should not get results in a multi-threaded environment", e);
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("%s ,cost: %d ms", e.getMessage(), System.currentTimeMillis() - start));
        }

        if (result instanceof RuntimeException) {
            throw (RuntimeException)result;
        } else if (result instanceof Throwable) {
            throw new RuntimeException((Throwable)result);
        }

        return result;
    }

    /**
     * Sets result message.
     *
     * @param obj the obj
     */
    public void setResultMessage(Object obj) {
        origin.complete(obj);
    }

    /**
     * Gets request message.
     *
     * @return the request message
     */
    public RpcMessage getRequestMessage() {
        return requestMessage;
    }

    /**
     * Sets request message.
     *
     * @param requestMessage the request message
     */
    public void setRequestMessage(RpcMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
