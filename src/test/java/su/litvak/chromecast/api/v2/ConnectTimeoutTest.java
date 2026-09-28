/*
 * Copyright 2018 Vitaly Litvak (vitavaque@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package su.litvak.chromecast.api.v2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ConnectTimeoutTest {

    @Test
    public void testSilentDeviceTimesOut() throws Exception {
        try (ServerSocket server = new ServerSocket(0)) {
            Thread accepting = new Thread(() -> {
                try (Socket ignored = server.accept()) {
                    Thread.sleep(10 * 1000);
                } catch (Exception e) {
                    // closed by the test
                }
            });
            accepting.setDaemon(true);
            accepting.start();

            ChromeCast cast = new ChromeCast("127.0.0.1", server.getLocalPort());
            cast.setConnectTimeout(500);

            long start = System.nanoTime();
            IOException e = assertThrows(IOException.class, cast::connect);
            assertTrue(System.nanoTime() - start < TimeUnit.SECONDS.toNanos(5));
            // JDK 8 wraps the handshake read timeout in an SSLException, later JDKs rethrow it as is
            boolean timedOut = e instanceof SocketTimeoutException || e.getCause() instanceof SocketTimeoutException;
            assertTrue(timedOut, e.toString());

            // Must not throw although the channel never opened
            cast.disconnect();
        }
    }
}
