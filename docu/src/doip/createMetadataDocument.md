#### Create a Metadata Document

While creating a metadata document the DO has to contain
two elements:
1. Serialization of metadata ([DataCite Schema(JSON)](../datacite/datacite.md#metadata-of-a-metadata-document))
   - id = metadata
   - type = application/json
2. Serialization of metadata document
   - id = document
   - type = application/ld+json | application/json | application/xml

##### (DataCite) Metadata
The dataCite metadata for creating a DO of a metadata document must contain the following fields:
- titles
- publisher
- formats
- relatedIdentifiers
  - IsDescribedBy
  - IsMetadataFor

Some fields are filled by server. See also [here](../datacite/datacite.md#metadata-for-creating-a-metadata-document) 
for further information.
 
###### formats
Defines the format of the metadata document
  - application/ld+json -> JSON LD 
  - application/json    -> JSON
  - application/xml     -> XML

###### relatedIdentifiers
Identifier for 
- Schema Document -> type: IsDescribedBy
- Digital Object(DO) -> type: IsMetadataFor

Many identifiers are supported. Most relevant are (in alphabetical order):
- DOI
- Handle
- PURL
- URL

