/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.aeron.util;

import java.util.concurrent.CountDownLatch;

/**
 * Base agent that is responsible for an ongoing activity which runs in its own thread.
 */
public abstract class Agent implements Runnable, AutoCloseable
{
    private volatile boolean running;

    private final long sleepPeriod;

    private CountDownLatch latch = new CountDownLatch(1);

    public Agent(final long sleepPeriod)
    {
        this.sleepPeriod = sleepPeriod;
        running = true;
    }

    public void run()
    {
        while (running)
        {
            process();

            try
            {
                Thread.sleep(sleepPeriod);
            }
            catch (final InterruptedException ex)
            {
                // TODO: logging
                ex.printStackTrace();
            }
        }

        latch.countDown();
    }

    public void close() throws Exception
    {
        running = false;
        //await();
    }

    public void stop()
    {
        running = false;
        //await();
    }

    public void await()
    {
        try
        {
            latch.await();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public abstract void process();
}
