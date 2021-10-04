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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import edu.kit.turntable.doip.ExtendedOperations;
import edu.kit.turntable.doip.server.mockup.MockUpProcessor;
import edu.kit.turntable.mapping.MappingSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.dona.doip.DoipConstants;
import net.dona.doip.InDoipMessage;
import net.dona.doip.InDoipSegment;
import net.dona.doip.client.DigitalObject;
import net.dona.doip.client.DoipException;
import net.dona.doip.server.DoipProcessor;
import net.dona.doip.server.DoipServerRequest;
import net.dona.doip.server.DoipServerResponse;
import net.dona.doip.util.GsonUtility;
import net.dona.doip.util.InDoipMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Server implements the mapping to another metadata repository which will
 * not implement the DOIP interface.
 *
 */
public class TurntableDoipProcessor4Mapping implements DoipProcessor {

  /**
   * Logger for messages.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(TurntableDoipProcessor4Mapping.class);
  /**
   * Default folder for mappings.
   */
  private static String MAPPINGS_DIR_DEFAULT = "mappings";
  /**
   * Default suffix for mappings.
   */
  private static String MAPPINGS_SUFFIX_DEFAULT = "_mappings.json";
  private String serviceId;
  private String address;
  private int port;
  private String serviceName;
  private String serviceDescription;
  private String defaultToken;
  private boolean authenticationEnabled = false;
  private PublicKey publicKey;
  private String repoBaseUri;
  private Map<String, MappingSchema> allMappings;
  private String mappingsDir;
  private String mappingsSuffix;

  @Override
  public void init(JsonObject config) {
    LOGGER.debug("Initializing DOIP processor with configuration {}.", config);
    DoipProcessor.super.init(config);
    serviceId = config.get("serviceId").getAsString();
    serviceName = config.has("serviceName") ? config.get("serviceName").getAsString() : null;
    serviceDescription = config.has("serviceDescription") ? config.get("serviceDescription").getAsString() : null;
    address = config.has("address") ? config.get("address").getAsString() : null;
    port = config.has("port") ? config.get("port").getAsInt() : -1;
    publicKey = config.has("publicKey") ? GsonUtility.getGson().fromJson(config.get("publicKey"), PublicKey.class) : null;
    authenticationEnabled = config.has("authenticationEnabled") ? config.get("authenticationEnabled").getAsBoolean() : false;
    defaultToken = config.has("defaultToken") ? config.get("defaultToken").getAsString() : null;

    // config may overwrite default mappings dir
    mappingsDir = config.has("mappingsDir") ? config.get("mappingsDir").getAsString() : MAPPINGS_DIR_DEFAULT;

    // config may overwrite default suffix for mappings
    mappingsSuffix = config.has("mappingsSuffix") ? config.get("mappingsSuffix").getAsString() : MAPPINGS_SUFFIX_DEFAULT;

    parseAllMappings();
//    String repoBaseUri = config.get("repoBaseUri").getAsString();
    // String username = config.get("username").getAsString();
    //String password = config.get("password").getAsString();
    //initialize repo client
  }

  @Override
  public void process(DoipServerRequest req, DoipServerResponse resp) throws IOException {
    LOGGER.debug("Processing DOIP request.");
    printRequest(req);
    try {
      if (serviceId.equals(req.getTargetId())) {
        processServiceRequest(req, resp);
      } else {
        processObjectRequest(req, resp);
      }
    } catch (DoipException ex) {
      LOGGER.error("A DoipException occured. Forwarding status and message to client.", ex);
      resp.setStatus(ex.getStatusCode());
      resp.setAttribute(DoipConstants.MESSAGE_ATT, ex.getMessage());
//    } catch(IOException e){
//      LOGGER.error("Unexpected exception occured. Returning DOIP Status ERROR to client.", e);
//      resp.setStatus(DoipConstants.STATUS_ERROR);
//      resp.setAttribute(DoipConstants.MESSAGE_ATT, "An unexpected server error occurred");
    }
  }

  /**
   * Process a service request, e.g. a request where the targetId is equal the
   * serviceId.
   */
  private void processServiceRequest(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    String operationId = req.getOperationId();
    if (null == operationId) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "Missing operationId.");
    } else {
      switch (operationId) {
        case DoipConstants.OP_HELLO:
          serviceHello(req, resp);
          break;
        case DoipConstants.OP_LIST_OPERATIONS:
          listOperationsForService(req, resp);
          break;
        case DoipConstants.OP_CREATE:
          create(req, resp);
          break;
        case DoipConstants.OP_SEARCH:
          search(req, resp);
          break;
        case ExtendedOperations.OP_VALIDATE:
          validate(req, resp);
          break;
        default:
          resp.setStatus(DoipConstants.STATUS_DECLINED);
          resp.setAttribute(DoipConstants.MESSAGE_ATT, "Operation not supported");
          break;
      }
    }
  }

  /**
   * Process an object request, e.g. a request where the targetId is NOT equal
   * the serviceId.
   */
  private void processObjectRequest(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    String operationId = req.getOperationId();
    String targetId = req.getTargetId();
    LOGGER.debug("Processing object request for operation {} and target {}.", operationId, targetId);
    if (null == operationId) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "Missing operationId.");
    } else {
      switch (operationId) {
        case DoipConstants.OP_RETRIEVE:
          retrieve(req, resp);
          break;
        case DoipConstants.OP_UPDATE:
          update(req, resp);
          break;
        case DoipConstants.OP_DELETE:
          delete(req, resp);
          break;
        case DoipConstants.OP_LIST_OPERATIONS:
          listOperationsForObject(targetId, req, resp);
          break;
        default:
          //call(req, resp);
          throw new DoipException(DoipConstants.STATUS_DECLINED, "Operation " + operationId + " is not supported for target " + targetId + ".");
      }
    }
  }

  /**
   * Obtain service information, e.g. serviceId, type, address, port, protocol
   * version and public key.
   */
  private void serviceHello(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }

    testAuthentication(req, resp);

    JsonObject res = new JsonObject();
    res.addProperty("id", serviceId);
    res.addProperty("type", "0.TYPE/DOIPServiceInfo");
    JsonObject atts = new JsonObject();
    if (serviceName != null) {
      atts.addProperty("serviceName", serviceName);
    }
    if (serviceDescription != null) {
      atts.addProperty("serviceDescription", serviceDescription);
    }
    atts.addProperty("ipAddress", address);
    atts.addProperty("port", port);
    atts.addProperty("protocol", "TCP");
    atts.addProperty("protocolVersion", "2.0");
    if (publicKey != null) {
      atts.add("publicKey", GsonUtility.getGson().toJsonTree(publicKey));
    }
    res.add("attributes", atts);
    resp.writeCompactOutput(res);
  }

  /**
   * List all service operations. By default, operations OP_HELLO,
   * OP_LIST_OPERATIONS, OP_CREATE and OP_SEARCH should be supported.
   */
  private void listOperationsForService(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling listOperationsForService().");
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }

    testAuthentication(req, resp);

    LOGGER.debug("Building list of operations.");
    JsonArray res = new JsonArray();
    res.add(DoipConstants.OP_HELLO);
    res.add(DoipConstants.OP_LIST_OPERATIONS);
    res.add(DoipConstants.OP_CREATE);
    res.add(DoipConstants.OP_SEARCH);
    res.add(ExtendedOperations.OP_VALIDATE);
    LOGGER.debug("Writing list of operations to output.");
    resp.writeCompactOutput(res);
    LOGGER.debug("Returning from listOperationsForService().");
  }

  /**
   * Create a new DigitalObject.
   */
  private void create(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling create()...");
    // Get Datacite metadata
    printRequest(req);
    MockUpProcessor.create(req, resp);
    printResponse(resp);
    LOGGER.debug("Returning from create().");
  }

  /**
   * Search for resources using a provided search query and pagination
   * information. The search query should be a serialized data resource in JSON
   * format. If deserialization fails, the service will query for all data
   * resources and returns the selected page.
   */
  private void search(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling search().");
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }
    printRequest(req);

    JsonObject attributes = req.getAttributes();
    String query = req.getAttributeAsString("query");
    if (query == null) {
      LOGGER.error("No query found in request.");
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "Missing query");
    }

    LOGGER.debug("Searching resource using query {}.", query);
    Gson gson = GsonUtility.getGson();
//    DataResource example;
//    try{
//      LOGGER.debug("Trying to deserialize query {} to a data resource.", query);
//      example = gson.fromJson(query, DataResource.class);
//      LOGGER.debug("Query is a data resource. Querying by example.");
//    } catch(JsonSyntaxException ex){
//      LOGGER.debug("Provided query string {} is not a DataResource. Querying for all resources.", query);
//      example = new DataResource();
//    }
//    String type = req.getAttributeAsString("type");
//    if(type == null){
//      type = "full";
//    }
//
//    LOGGER.debug("Searching with type 'full'");
//    int pageNum = 0;
//    if(attributes.has("pageNum")){
//      pageNum = attributes.get("pageNum").getAsInt();
//    }
//    int pageSize = 20;
//    if(attributes.has("pageSize")){
//      pageSize = attributes.get("pageSize").getAsInt();
//    }
//    List<SortField> sortFields = Collections.emptyList();
//    if(attributes.has("sortFields")){
//      String sortFieldsString = attributes.get("sortFields").getAsString();
//      sortFields = DOIPUtils.getSortFieldsFromString(sortFieldsString);
//    }
//
//    LOGGER.debug("Performing search with pagination options: page #{}, {} elements per page, sorted by {}.", pageNum, pageSize, sortFields);
//    ResultPage<DataResource> resultPage = SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getResources(example, pageNum, pageSize, sortFields.toArray(new SortField[]{}));
//
//    if(resultPage == null){
//      LOGGER.debug("No results returned from repository. Setting empty result.");
//      resultPage = new ResultPage(new DataResource[0], new ControllerUtils.ContentRange());
//    }
//
//    LOGGER.debug("Building output message.");
//    try(JsonWriter writer = new JsonWriter(resp.getOutput().getJsonWriter())){
//      writer.setIndent("  ");
//      //start response object
//      writer.beginObject();
//      writer.name("size").value(resultPage.getContentRange().getTotalElements());
//      //start results array
//      writer.name("results").beginArray();
//      for(DataResource resource : resultPage.getResources()){
//        if("id".equals(type)){
//          writer.value(resource.getId());
//        } else{
//          LOGGER.debug("Obtaining content information for targetId {}.", resource.getId());
//          ContentInformation[] contentInformation = SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getContentInformation(resource.getId(), "/");
//          DigitalObject obj = DOIPUtils.ofDataResource(resource, contentInformation, false);
//
//          JsonElement dobjJson = gson.toJsonTree(obj);
//          gson.toJson(dobjJson, writer);
//        }
//      }
//      //finish results array
//      writer.endArray();
//      //finish response object
//      writer.endObject();
//      LOGGER.debug("Returning from search().");
//    }
  }

  /**
   * Validate provided document with referenced schema.
   */
  private void validate(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling validate().");
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }
    printRequest(req);
    MockUpProcessor.validate(req, resp);
    printResponse(resp);
    LOGGER.debug("Returning from validate().");
  }

  /**
   * Retrieve a single resource and/or element(s). The requests identifies the
   * resource by the 'targetId'. A single element is addressed by providing an
   * attribute 'element' having a value denoting the relative path of an
   * element. The path might map to a virtual folder matching multiple elements.
   * If a specific element is addressed, only the element data is returned.
   * Otherwise, the first segment contains the serialized data resource and
   * following segments may contain associated element's data if the attribute
   * 'includeElementData' is provided and has the value {@code true}.
   */
  private void retrieve(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling retrieve().");

    printRequest(req);
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }
    MockUpProcessor.retrieve(req, resp);
//    String targetId = req.getTargetId();
//    String element = req.getAttributeAsString("element");
//    boolean includeElementData = element == null && DOIPUtils.getBooleanAttributeFromRequest(req, "includeElementData");
//    LOGGER.debug("Retrieving targetId {}, element {}, including element data: {}.", targetId, (element != null) ? element : "<not provided>", includeElementData);
//
//    if(element != null){
//      LOGGER.debug("Obtaining content information for provided element {}.", element);
//      ContentInformation[] contentInformation = SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getContentInformation(targetId, element);
//
//      if(contentInformation == null || contentInformation.length == 0){
//              throw new DoipException(DoipConstants.STATUS_NOT_FOUND, "Element with id " + element + " was not found for resource " + targetId + ".");
//      }
//
//      LOGGER.debug("Returning {} content information element(s) to client.", contentInformation.length);
//      for(ContentInformation info : contentInformation){
//        LOGGER.debug("Writing element metadata for content information {}.", info.getRelativePath());
//        JsonObject header = new JsonObject();
//        header.addProperty("id", info.getRelativePath());
//        header.addProperty("type", info.getMediaType());
//        header.addProperty("filename", info.getFilename());
//        resp.getOutput().writeJson(header);
//        URI contentUri = URI.create(info.getContentUri());
//        LOGGER.debug("Writing byte stream from content URI {} to response.", contentUri);
//        //check URI protocol for file
//        resp.getOutput().writeBytes(contentUri.toURL().openStream());
//      }
//    } else{
//      LOGGER.debug("Obtaining data resource with targetId {}.", targetId);
//      DataResource resource = SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getResource(targetId);
//      if(resource == null){
//              throw new DoipException(DoipConstants.STATUS_NOT_FOUND, "Resource with id " + targetId + " was not found.");
//      }
//      LOGGER.debug("Obtaining content information for targetId {}.", targetId);
//      ContentInformation[] contentInformation = SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getContentInformation(targetId, "/");
//      LOGGER.debug("Transforming data resource to DOIP DigitalObject.");
//      DigitalObject obj = DOIPUtils.ofDataResource(resource, contentInformation, false);
//
//      JsonElement dobjJson = GsonUtility.getGson().toJsonTree(obj);
//      if(!includeElementData){
//        LOGGER.debug("No element data requested. Returning serialized data resource.");
//        resp.writeCompactOutput(dobjJson);
//      } else{
//        LOGGER.debug("Element data requested. Writing serialized data resource as first segment.");
//        resp.getOutput().writeJson(dobjJson);
//
//        LOGGER.debug("Returning {} content information element(s) to client.", contentInformation.length);
//        for(ContentInformation info : contentInformation){
//          LOGGER.debug("Writing element metadata for content information {}.", info.getRelativePath());
//          JsonObject header = new JsonObject();
//          header.addProperty("id", info.getRelativePath());
//          resp.getOutput().writeJson(header);
//          URI contentUri = URI.create(info.getContentUri());
//          LOGGER.debug("Writing byte stream from content URI {} to response.", contentUri);
//          //check URI protocol for file
//          resp.getOutput().writeBytes(contentUri.toURL().openStream());
//        }
//      }
//    }
    LOGGER.debug("Returning from retrieve().");
  }

  /**
   * Update a single resource and/or element(s). The requests identifies the
   * resource by the 'targetId'. The input message contains the serialized
   * resource and payload, if desired. The update succeeds if the resource and
   * all provided payloads have been sent to the repository. As resource
   * metadata and payload are updated sequentially, it may happen, that the
   * resource is updated whereas one or more payloads are not due to an error.
   * In this case, NO rollback is performed but the resource remains in the
   * partly updated state.
   */
  private void update(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling update().");
    printRequest(req);
    //check authentication if required
    String targetId = req.getTargetId();
    LOGGER.debug("Updating targetId {}. Obtaining DataResource from input message.", targetId);
    MockUpProcessor.update(req, resp);
    printResponse(resp);
    LOGGER.debug("Returning from update().");
  }

  /**
   * Delete a single resource identified by the targetId in the request.
   */
  private void delete(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling delete().");
    printRequest(req);
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }
    String targetId = req.getTargetId();
    LOGGER.debug("Deleting resource with targetId {}.", targetId);
    //check authentication if required
//    SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).deleteResource(targetId);
    LOGGER.debug("Returning from delete().");
  }

  /**
   * List all operations supported for a particular object. Depending on the
   * addressed object, the list of supported operations may change. By default,
   * each object should support at least OP_LIST_OPERATIONS, OP_RETRIEVE,
   * OP_UPDATE and OP_DELETE.
   */
  private void listOperationsForObject(String targetId, DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    LOGGER.debug("Calling listOperationsForObject().");
    if (!InDoipMessageUtil.isEmpty(req.getInput())) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }

//    //include authentication
//    LOGGER.debug("Checking for resource with targetId {}.", targetId);
//    DataResource resource = SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getResource(targetId);
//    if(resource == null){
//      LOGGER.error("No resource with targetId {} found. Listing operations not allowed.", targetId);
//      throw new DoipException(DoipConstants.STATUS_NOT_FOUND, "Resource with id " + targetId + " was not found.");
//    }
    LOGGER.debug("Resource found. Building list of operations.");
    JsonArray res = new JsonArray();
    res.add(DoipConstants.OP_LIST_OPERATIONS);
    res.add(DoipConstants.OP_RETRIEVE);
    res.add(DoipConstants.OP_UPDATE);
    //DELETE is currently forbidden anyways
    res.add(DoipConstants.OP_DELETE);

    //may add additional ops depending on resource type?
    LOGGER.debug("Writing list of operations to output.");
    resp.writeCompactOutput(res);
    LOGGER.debug("Returning from listOperationsForObject().");
  }

  /**
   * Restore a data resource from a provided DoipMessage. The message has to
   * contain at least one segment containing the digital object metadata. If the
   * digital object metadata contains payload elements, the message also has to
   * contain payload segments matching the number of expected elements. If the
   * number of payload elements does not match or if no payload is expected but
   * provided, a DoipException will be thrown.
   */
  private DigitalObject dataResourceFromSegments(InDoipMessage input) throws DoipException, IOException {
    LOGGER.debug("Obtaining data resource from DOIP message. Searching for first segment.");
    InDoipSegment firstSegment = InDoipMessageUtil.getFirstSegment(input);
    if (firstSegment == null) {
      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No input found in request.");
    }
//    String idAsString = firstSegment.getJson().getAsJsonObject().get("id").getAsString();
//    LOGGER.debug("ID of first segment: '{}'", idAsString);
    LOGGER.debug("Deserializing digital object from first segment.");
    DigitalObject digitalObject = GsonUtility.getGson().fromJson(firstSegment.getJson(), DigitalObject.class);
    return digitalObject;

//    LOGGER.debug("Deserializing data resource from digital object.");
//    DataResource resource = DOIPUtils.toDataResource(digitalObject, false);
//    LOGGER.debug("Checking for contained content information.");
//    int openPayloadCount = 0;
//    if(resource.getAssociatedContentInformation() != null){
//      Map<String, ContentInformation> payloads = new HashMap<>();
//      resource.getAssociatedContentInformation().forEach((contentInfo) -> {
//        payloads.put(contentInfo.getRelativePath(), contentInfo);
//      });
//      openPayloadCount = payloads.size();
//      LOGGER.debug("Found {} contained content information elements. Reading data from segments.", openPayloadCount);
//      Iterator<InDoipSegment> segments = input.iterator();
//      while(segments.hasNext()){
//        LOGGER.debug("Reading next segment.");
//        InDoipSegment headerSegment = segments.next();
//        String payloadName;
//        try{
//          payloadName = headerSegment.getJson().getAsJsonObject().get("id").getAsString();
//          LOGGER.debug("Reading payload for name {} from current segment.", payloadName);
//        } catch(Exception e){
//          LOGGER.error("Unable to detect 'id' header element in segment.");
//      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "Payload header element 'id' is missing.");
//        }
//        ContentInformation contentInfo = payloads.get(payloadName);
//        if(contentInfo == null){
//      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "Payload element " + payloadName + " not expected.");
//        }
//
//        if(!segments.hasNext()){
//          LOGGER.error("Unable to detect payload segment for payload with name {}.", payloadName);
//      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No payload data found for payload " + payloadName + ".");
//        }
//
//        LOGGER.debug("Obtaining payload segment.");
//        InDoipSegment elementBytesSegment = segments.next();
//
//        LOGGER.debug("Reading payload data from current segment.");
//        contentInfo.setContentStream(persistInputStream(elementBytesSegment.getInputStream()));
//        openPayloadCount--;
//      }
//    } else{
//      LOGGER.debug("No contained content information found. Checking for payload.");
//      if(!InDoipMessageUtil.isEmpty(input)){
//      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "No payload expected.");
//      }
//    }
//    if(openPayloadCount > 0){
//      throw new DoipException(DoipConstants.STATUS_BAD_REQUEST, "Expecting " + openPayloadCount + " more payload(s).");
//    }
//
//    LOGGER.debug("All segments read. Returning resource.");
//    return resource;
  }

  private static ByteArrayInputStream persistInputStream(InputStream in) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buf = new byte[8192];
    int r;
    while ((r = in.read(buf)) > 0) {
      bout.write(buf, 0, r);
    }
    return new ByteArrayInputStream(bout.toByteArray());
  }

  /**
   * Call a custom operation specific for a certain object.
   */
  private void call(DoipServerRequest req, DoipServerResponse resp) throws DoipException, IOException {
    //generic call for misc operations

//        Options options = authenticate(req);
//        String operationId = req.getOperationId();
//        String targetId = req.getTargetId();
//
//        InDoipSegment initialSegment = InDoipMessageUtil.getFirstSegment(req.getInput());
//        JsonElement params = null;
//        if (initialSegment != null) {
//            if (initialSegment.isJson()) {
//                params = initialSegment.getJson();
//                if (!InDoipMessageUtil.isEmpty(req.getInput())) {
//                    resp.setStatus(DoipConstants.STATUS_BAD_REQUEST);
//                    resp.setAttribute(DoipConstants.MESSAGE_ATT, "Cordra operation expects at most single JSON segment");
//                    return;
//                }
//            } else {
//                resp.setStatus(DoipConstants.STATUS_BAD_REQUEST);
//                resp.setAttribute(DoipConstants.MESSAGE_ATT, "Cordra operation expects at most single JSON segment");
//                return;
//            }
//        }
//        JsonElement result;
//        if ("true".equals(req.getAttributeAsString("isCallForType"))) {
//            String type = targetId;
//            result = cordraClient.callForType(type, operationId, params, options);
//        } else {
//            CordraObject co = cordraClient.get(targetId);
//            if (co == null) {
//                resp.setStatus(DoipConstants.STATUS_NOT_FOUND);
//                resp.setAttribute(DoipConstants.MESSAGE_ATT, "No such object " + targetId);
//                return;
//            }
//            if ("Schema".equals(co.type)) {
//                result = cordraClient.callForType(this.getTypeNameForSchemaObject(co), operationId, params, options);
//            } else {
//                result = cordraClient.call(targetId, operationId, params, options);
//            }
//        }
//        if (result != null) {
//            if (result.isJsonNull()) {
//                // workaround for difference between explicit null result and undefined/missing result
//                resp.getOutput().writeJson(result);
//            } else {
//                resp.writeCompactOutput(result);
//            }
//        }
  }

  @Override
  public void shutdown() {
    DoipProcessor.super.shutdown();
  }

  /**
   * Check if the request contains any authentication information.
   */
  private boolean containsAuthInfo(DoipServerRequest req) {
    return !(req.getAuthentication() == null
            || !req.getAuthentication().isJsonObject()
            || req.getAuthentication().getAsJsonObject().keySet().isEmpty());
    //&& req.getConnectionClientId() == null));
  }

  /**
   * Obtain authentication information (aka. JWT token) and return it as single
   * string. Optionally, a default token can be configured and returned in case
   * of anonymous access (
   */
  private String authenticate(DoipServerRequest req) throws DoipException {
    if (!authenticationEnabled) {
      LOGGER.debug("Authentication disabled. Returning empty token.");
      return null;
    }

    if (!containsAuthInfo(req)) {
      LOGGER.debug("No authentication information found in request. Returning default token {}.", defaultToken);
      return defaultToken;
    }

    JsonObject authentication = req.getAuthentication().getAsJsonObject();
    if (authentication.has("token")) {
      return authentication.get("token").getAsString();
    }
//    if(authentication.has("password")){
//      //return authenticateViaPassword(req, authentication);
//      throw new DoipException(DoipConstants.STATUS_UNAUTHENTICATED, "Authentication via password not yet supported.");
//    }
    throw new DoipException(DoipConstants.STATUS_UNAUTHENTICATED, "Unable to parse authentication. Currently, only JWT-based authentication via 'token' attribute is supported.");
  }

//  private Options authenticateViaTls(DoipServerRequest req) throws UnauthorizedException{
//    if(req.getClientId() != null && !req.getClientId().equals(req.getConnectionClientId())){
//      throw new DoipException(DoipConstants.STATUS_UNAUTHENTICATED, "No authentication provided for " + req.getClientId());
//    }
//    String clientId = req.getConnectionClientId();
//    PublicKey clientPublicKey = req.getConnectionPublicKey();
//    if(clientPublicKeyChecks(clientId, clientPublicKey)){
//      return new Options().setAsUserId(clientId).setUseDefaultCredentials(true);
//    } else{
//      throw new UnauthorizedException("Client TLS certificate key does not match handle record of " + clientId);
//    }
//  }
//  private Options authenticateViaPassword(DoipServerRequest req, JsonObject authentication) throws UnauthorizedException{
//    String password = authentication.get("password").getAsString();
//    String username = null;
//    if(authentication.has("username")){
//      username = authentication.get("username").getAsString();
//    }
//    if(req.getClientId() != null){
//      if(username != null){
//        throw new UnauthorizedException("No support for authenticating with both username and clientId");
//      }
//      return new Options().setUserId(req.getClientId()).setPassword(password);
//    }
//    if(username == null){
//      throw new UnauthorizedException("Unable to parse authentication (neither username nor clientId)");
//    }
//    Options options = new Options();
//    options.setUsername(username);
//    options.setPassword(password);
//    if(authentication.has("asUserId")){
//      options.setAsUserId(authentication.get("asUserId").getAsString());
//    }
//    return options;
//  }
//  private boolean clientPublicKeyChecks(String clientId, PublicKey clientPublicKey){
//    List<PublicKey> publicKeys = getPublicKeysFor(clientId);
//    for(PublicKey foundPublicKey : publicKeys){
//      if(foundPublicKey.equals(clientPublicKey)){
//        return true;
//      }
//    }
//    return false;
//  }
//  private List<PublicKey> getPublicKeysFor(String iss){
//    List<PublicKey> result = new ArrayList<>();
//    if("admin".equals(iss)){
//      try{
//        JsonElement adminPublicKeyElement = cordraClient.get("design").content.getAsJsonObject().get("adminPublicKey");
//        if(adminPublicKeyElement != null){
//          result.add(GsonUtility.getGson().fromJson(adminPublicKeyElement, PublicKey.class));
//        }
//      } catch(Exception e){
//        logger.warn("Error checking admin public key", e);
//      }
//      return result;
//    }
//    try{
//      HandleValue[] values = resolver.resolveHandle(Util.encodeString(iss), Common.PUBLIC_KEY_TYPES, null);
//      List<PublicKey> pubkeyValues = Util.getPublicKeysFromValues(values);
//      result.addAll(pubkeyValues);
//    } catch(HandleException e){
//      // error resolving handle
//    }
//    return result;
//  }
  /**
   * Do a simple authentication test.
   *
   * @param doipReq Request
   * @param doipResp Response
   * @throws IOException Error writing to stream.
   */
  private void testAuthentication(DoipServerRequest doipReq, DoipServerResponse doipResp) throws IOException {
    if (authenticationEnabled) {
      //simply test authentication
//      SimpleRepositoryClient.create(repoBaseUri, authenticate(req)).getResources(0, 0);
//      doipResp.getOutput().writeBytes("Attention: Service called without authentication check!".getBytes());
      LOGGER.debug("Do nothing while testing authentication");
    }
  }

  private void printRequest(DoipServerRequest doipReq) throws IOException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("*************************************************************");
      LOGGER.debug("Request:");
      LOGGER.debug("Client ID: '{}'", doipReq.getClientId());
      LOGGER.debug("ConnectionClient ID: '{}'", doipReq.getConnectionClientId());
      LOGGER.debug("Operation ID: '{}'", doipReq.getOperationId());
      LOGGER.debug("Target ID: '{}'", doipReq.getTargetId());
      if (doipReq.getAuthentication() != null) {
        LOGGER.debug("Authentication: '{}'", doipReq.getAuthentication().toString());
      }
      JsonObject attributes = doipReq.getAttributes();
      if (attributes != null) {
        LOGGER.debug("*************************************************************");

        LOGGER.debug("Attributes:");
        for (Entry<String, JsonElement> attribute : attributes.entrySet()) {
          LOGGER.debug("'{}' : '{}'", attribute.getKey(), attribute.getValue().toString());
        }
      }
    }
  }

  private void printResponse(DoipServerResponse doipResp) throws IOException {

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Cannot serialize DoipServerResponse!?");
    }
  }

  /**
   * Parse all mappings. Mappings should be located besides the jar file in a
   * subfolder called 'mapping'. Subfolders will not be supported. Mappings
   * should be placed in a file with suffix '_mapping.json'
   */
  private void parseAllMappings() {
      Path pathToMappings = Paths.get(mappingsDir).toAbsolutePath();
      LOGGER.debug("Parse all files with suffix '{}' in folder '{}'", mappingsSuffix, pathToMappings);
    try {
      Stream<Path> list = Files.list(pathToMappings);
      Gson gson = new Gson();
      list.filter(file -> file.toString().endsWith(mappingsSuffix)).forEach(path -> {
        LOGGER.debug("Read mapping from file: '{}'", path.getFileName());
        try {
          JsonReader reader;
          reader = new JsonReader(new FileReader(path.toFile()));
          MappingSchema mappingSchema = gson.fromJson(reader, MappingSchema.class);
          LOGGER.debug("Mapping: '{}'", mappingSchema.toString());
          if (mappingSchema.getTargetId() != null) { 
            // add mapping to map
//            allMappings.add(mappingSchema.getTargetId(), mappingSchema);
//            allMappings.add(mappingSchema.getBaseURL(), mappingSchema);
            
          }
        } catch (IOException ex) {
          LOGGER.error("Error reading mapping from file '{}'!", path.getFileName());
        }
      });
    } catch (IOException ex) {
          LOGGER.error("Error reading mapping dir '{}'!", pathToMappings);
    }
  }

}
