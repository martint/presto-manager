/*
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
package com.teradata.prestomanager.controller;

import io.airlift.bootstrap.Bootstrap;
import io.airlift.discovery.client.DiscoveryModule;
import io.airlift.event.client.HttpEventModule;
import io.airlift.http.server.HttpServerModule;
import io.airlift.jaxrs.JaxrsModule;
import io.airlift.json.JsonModule;
import io.airlift.log.Logger;
import io.airlift.node.NodeModule;

public class ControllerServer
{
    private static final Logger LOG = Logger.get(ControllerServer.class);

    private ControllerServer() {}

    public static void main(String[] args)
            throws Exception
    {
        // TODO: Replace placeholder properties with proper configuration
        System.setProperty("node.environment", "test");
        System.setProperty("http-server.http.port", "8081");
        System.setProperty("log.levels-file", "etc/log.properties");

        Bootstrap bootstrap = new Bootstrap(
                new NodeModule(),
                new DiscoveryModule(),
                new HttpServerModule(),
                new JsonModule(),
                new JaxrsModule(true), // requireExplicitBindings = true
                new HttpEventModule(),
                new ControllerServerModule()
        );

        try {
            bootstrap.strictConfig().initialize();
        }
        catch (Exception e) {
            LOG.error(e, "Error starting server");
        }
    }
}