/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureContextListener;

/**
 * Listens to the result of a {@link Future}.
 * The result of the asynchronous {@link Channel} I/O operation is notified once this listener
 * is added by calling {@link Future#addListener(Object, FutureContextListener)} with the {@link Channel} as context.
 *
 * <h3>Return the control to the caller quickly</h3>
 *
 * The {@link FutureContextListener#operationComplete(Object, Future)} method is directly called by an I/O
 * thread.  Therefore, performing a time consuming task or a blocking operation
 * in the handler method can cause an unexpected pause during I/O.  If you need
 * to perform a blocking operation on I/O completion, try to execute the
 * operation in a different thread using a thread pool.
 */
public interface ChannelFutureListener {

    /**
     * A {@link FutureContextListener} that closes the {@link Channel} which is
     * associated with the specified {@link Future}.
     */
    FutureContextListener<Channel, Object> CLOSE = (channel, future) -> channel.close();

    /**
     * A {@link FutureContextListener} that closes the {@link Channel} when the
     * operation ended up with a failure or cancellation rather than a success.
     */
    FutureContextListener<Channel, Object> CLOSE_ON_FAILURE = (channel, future) -> {
        if (!future.isSuccess()) {
            channel.close();
        }
    };

    /**
     * A {@link FutureContextListener} that forwards the {@link Throwable} of the {@link Future} into the
     * {@link ChannelPipeline}. This mimics the old behavior of Netty 3.
     */
    FutureContextListener<Channel, Object> FIRE_EXCEPTION_ON_FAILURE = (channel, future) -> {
        if (!future.isSuccess()) {
            channel.pipeline().fireExceptionCaught(future.cause());
        }
    };

    // Just a type alias
}
