/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kit.turntable.doip.server;

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
    DoipServerConfig.TlsConfig tlsConfig = new DoipServerConfig.TlsConfig();
    tlsConfig.id = args[0];
    config.tlsConfig = tlsConfig;
    DoipServer server = new DoipServer(config);
    server.init();
    Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
    port = args[2];
    LOGGER.info("Server is up and running!");
    LOGGER.info("ID: '{}'", tlsConfig.id);
    LOGGER.info("Address: '{}'", config.listenAddress);
    LOGGER.info("Port: '{}'", config.port);
  }

  @Override
  public void process(DoipServerRequest req, DoipServerResponse resp) throws IOException {
    LOGGER.trace("Get any request on port '{}'", port);
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
}
