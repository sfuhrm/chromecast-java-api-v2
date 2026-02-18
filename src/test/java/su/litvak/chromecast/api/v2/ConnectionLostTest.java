/*
 * Copyright 2014 Vitaly Litvak (vitavaque@gmail.com)
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ConnectionLostTest {
    MockedChromeCast chromeCastStub;
    ChromeCast cast = new ChromeCast("localhost");

    @BeforeEach
    public void initMockedCast() throws Exception {
        chromeCastStub = new MockedChromeCast();
        cast.connect();
        chromeCastStub.close();
        // ensure that chrome cast disconnected
        int retry = 0;
        while (cast.isConnected() && retry++ < 25) {
            Thread.sleep(50);
        }
        assertTrue(retry < 25, "ChromeCast wasn't properly disconnected");
    }

    @Test
    public void testDisconnect() throws Exception {
        assertThrows(ConnectException.class, () -> {
            assertNull(cast.getStatus());
        });
    }

    @Test
    public void testReconnect() throws Exception {
        chromeCastStub = new MockedChromeCast();
        assertNotNull(cast.getStatus());
    }

    @AfterEach
    public void shutdown() throws IOException {
        if (cast.isConnected()) {
            cast.disconnect();
        }
        chromeCastStub.close();
    }
}
