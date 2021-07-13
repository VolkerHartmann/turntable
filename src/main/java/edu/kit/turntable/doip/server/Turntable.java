/*
 * Copyright 2021 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.turntable.doip.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import net.dona.doip.InDoipSegment;
import net.dona.doip.server.DoipProcessor;
import net.dona.doip.server.DoipServer;
import net.dona.doip.server.DoipServerConfig;
import net.dona.doip.server.DoipServerRequest;
import net.dona.doip.server.DoipServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generic DOIP Server. It's a facade for (multiple) implementations of metadata
 * repositories.
 */
public class Turntable implements DoipProcessor {

  /**
   * Logger for this class.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(Turntable.class);
  /**
   * Port to listen.
   */
  private static String port;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) throws Exception {
    DoipServerConfig config = new DoipServerConfig();
    config.listenAddress = args[1];
    config.port = Integer.parseInt(args[2]);
    config.processorClass = Turntable.class.getName();
    config.processorClass="edu.kit.turntable.doip.server.TurntableDoipProcessor";
    config.processorConfig = convertFileToJSON("src/main/resources/configDOIP.json");
    DoipServerConfig.TlsConfig tlsConfig = new DoipServerConfig.TlsConfig();
    tlsConfig.id = args[0];
    config.tlsConfig = tlsConfig;
    TurntableDoipProcessor tdp = new TurntableDoipProcessor();
    tdp.init(config.processorConfig);
    DoipServer server = new DoipServer(config);
    server.init();
    Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    port = args[2];
    LOGGER.info("Server is up and running!");
    LOGGER.info("ID: '{}'", tlsConfig.id);
    LOGGER.info("Address: '{}'", config.listenAddress);
    LOGGER.info("Port: '{}'", config.port);
    LOGGER.debug("TRACE is on");
    LOGGER.debug("DEBUG is on");
    LOGGER.info("INFO is on");
    LOGGER.warn("WARN is on");
    LOGGER.error("Error is on");
  }

  @Override
  public void process(DoipServerRequest req, DoipServerResponse resp) throws IOException {
    LOGGER.debug("Get any request on port '{}'", port);
    try {
      resp.commit();
      for (InDoipSegment segment : req.getInput()) {
        if (segment.isJson()) {
          resp.getOutput().writeJson(segment.getJson());
        } else {
          try (InputStream in = segment.getInputStream(); OutputStream out = resp.getOutput().getBytesOutputStream();) {
            byte[] buf = new byte[8192];
            int r;
            String response = "Get response from port '" + port + "'\n";
            out.write(response.getBytes());
            while ((r = in.read(buf)) > 0) {
              out.write(buf, 0, r);
            }
          }
        }
      }
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }
  
  public static JsonObject convertFileToJSON (String fileName){

        // Read from File to String
        JsonObject jsonObject = new JsonObject();
        
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(fileName));
            jsonObject = jsonElement.getAsJsonObject();
            LOGGER.debug("Hurra!");
            LOGGER.debug(jsonElement.toString());
            LOGGER.debug("-------------------------------");
            LOGGER.debug(jsonObject.toString());
        } catch (FileNotFoundException e) {
           LOGGER.error("Reading error", e);
//        } catch (IOException ioe){
//          LOGGER.error("Parsing error", ioe);
        
        }
        
        
        return jsonObject;
    }

}
