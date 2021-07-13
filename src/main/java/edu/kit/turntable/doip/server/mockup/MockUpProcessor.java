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
package edu.kit.turntable.doip.server.mockup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.kit.datacite.kernel_4.Creator;
import edu.kit.datacite.kernel_4.Datacite43Schema;
import edu.kit.datacite.kernel_4.Date;
import edu.kit.datacite.kernel_4.Identifier;
import edu.kit.turntable.doip.server.TurntableDoipProcessor;
import edu.kit.turntable.doip.util.DoipUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.dona.doip.DoipConstants;
import net.dona.doip.client.DigitalObject;
import net.dona.doip.client.DoipException;
import net.dona.doip.server.DoipServerRequest;
import net.dona.doip.server.DoipServerResponse;
import net.dona.doip.util.GsonUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class MockUpProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TurntableDoipProcessor.class);
  private static final String PREFIX_REPO = "mockup";
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
  private static SimpleDateFormat syf = new SimpleDateFormat("yyyy");

  private static Map<String, DigitalObject> localRepo = new HashMap<>();

  /**
   * Create a new DigitalObject.
   */
  public static void create(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Repo: Create...");

    // Get Datacite metadata
    JsonObject attributes = req.getAttributes();
    if (attributes != null) {
      JsonElement dataciteAttr = attributes.get(DoipUtils.ATTR_DATACITE);
      if (dataciteAttr != null) {
        Datacite43Schema datacite = GsonUtility.getGson().fromJson(dataciteAttr, Datacite43Schema.class);
        // Update DataCite
        Identifier identifier = new Identifier();
        identifier.setIdentifier(PREFIX_REPO + "/" + UUID.randomUUID());
        identifier.setIdentifierType("Handle");
        datacite.getIdentifiers().add(identifier);
        Date creationDate = new Date();
        creationDate.setDate(sdf.format(new java.util.Date()));
        creationDate.setDateInformation("Date of creation");
        creationDate.setDateType(Date.DateType.CREATED);
        datacite.getDates().add(creationDate);
        Date lastUpdate = new Date();
        lastUpdate.setDate(sdf.format(new java.util.Date()));
        lastUpdate.setDateInformation("Date of last update");
        lastUpdate.setDateType(Date.DateType.UPDATED);
        datacite.getDates().add(lastUpdate);
        datacite.setPublicationYear(syf.format(lastUpdate));
        Creator creator = new Creator();
        creator.setName("SELF");
        creator.setNameType(Creator.NameType.PERSONAL);
        datacite.getCreators().add(creator);
        DigitalObject dobj = new DigitalObject();
        dobj.id = datacite.getIdentifiers().iterator().next().getIdentifier();
        dobj.attributes.add(DoipUtils.ATTR_DATACITE, dataciteAttr);
        dobj.type = DoipUtils.TYPE_DO;
        JsonElement dobjJson = GsonUtility.getGson().toJsonTree(dobj);
        LOGGER.trace("Writing DigitalObject to output message.");
        resp.writeCompactOutput(dobjJson);
        resp.setStatus(DoipConstants.STATUS_OK);
        LOGGER.trace("Returning from create().");
      }

    }
  }

}
