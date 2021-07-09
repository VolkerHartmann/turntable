# DataCite Schema

## Forword 
The DataCite Metadata Schema defines a list of core metadata properties to
enable an accurate and consistent identification of a resource for citation 
and retrieval purposes.(schema.datacite.org)

## Specification
For describing the metadata documents and the metadata schema documents/application profiles the 
[DataCite JSON schema](https://github.com/datacite/schema/tree/master/source/json/kernel-4.3) is used.
See [examples](#minimal-datacite-examples) below for minimal DataCite JSON documents.

## Metadata of a Metadata Document
| Entry                    | Description                                           |
| :----------------------- | :---------------------------------------------------- |
| ***identifiers***        | Identifier of the DO                                  |
| ***creators***           | Creator of the DO                                     |
| ***titles***             | Title of the DO                                       |
| ***publisher***          | Publisher of the DO                                   |
| ***publicationYear***    | Publication year of the DO                            |
| ***formats***            | Mimetype of the content.                              |
| ***relatedIdentifiers*** | Related DOs.                                          |
|                          | Mandatory relation types:                             |
|                          | - IsMetadataFor: Identifier of the data               |
|                          | - IsDescribedBy: Identifier of the schema/AP          |
| ***schemaVersion***      | expected value: "http://datacite.org/schema/kernel-4" |

## Metadata of a Schema Document / Application Profile
| Entry                 | Description                                           |
| :-------------------- | :---------------------------------------------------- |
| ***identifiers***     | Identifier of the DO                                  |
| ***creators***        | Creator of the DO                                     |
| ***titles***          | Title of the DO                                       |
| ***publisher***       | Publisher of the DO                                   |
| ***publicationYear*** | Publication year of the DO                            |
| ***formats***         | Mimetype of the content.                              |
| ***schemaVersion***   | expected value: "http://datacite.org/schema/kernel-4" |


## Metadata for creating a Metadata Document
| Entry             | Provided | Mandatory | Description |
| :---------------- | :------: | :-------: | :---------- |
| ***identifiers*** | no       | no        | Will be intantiated by the server |
| ***creators***    | yes      | no        | If no creator is provided it will be filled with the entry found in authentication |
| ***titles***      | yes      | yes       | Title of the metadata document |
| ***publisher***   | yes      | yes       | Publisher of the metadata document  |
| ***publicationYear*** | yes  | no        | If not provided actual year will be set |
| ***formats***     | yes      | yes       | Mimetype of the content. |
|                   |          |           | Possible values:         |
|                   |          |           | - application/ld+json    |
|                   |          |           | - application/json       |
|                   |          |           | - application/xml        |
| ***relatedIdentifiers*** | yes | yes (both) | Related DOs.                              |
|                   |          |           | Supported relation types:                    |
|                   |          |           | - IsMetadataFor: Identifier of the data      |
|                   |          |           | - IsDescribedBy: Identifier of the schema/AP |
| ***schemaVersion*** | yes    | no        | If not provided actual kernel version will be set |

## Metadata for creating a Schema Document / Application Profile
| Entry             | Provided | Mandatory | Description |
| :---------------- | :------: | :-------: | :---------- |
| ***identifiers*** | no       | no        | Will be intantiated by the server |
| ***creators***    | yes      | no        | If no creator is provided it will be filled with the entry found in authentication |
| ***titles***      | yes      | yes       | Title of the schema/application profile |
| ***publisher***   | yes      | yes       | Publisher of the schema/application profile  |
| ***publicationYear*** | yes  | no        | If not provided actual year will be set |
| ***formats***     | yes      | yes       | Mimetype of the content. |
|                   |          |           | Possible values:         |
|                   |          |           | - application/ld+json    |
|                   |          |           | - application/json       |
|                   |          |           | - application/xml        |
| ***schemaVersion*** | yes    | no        | If not provided actual kernel version will be set |


### Minimal DataCite Examples
#### Example for a minimal DataCite JSON Schema for an XML document: 
```
{
  "identifiers": [
    {
      "identifierType": "URL",
      "identifier": "https://example.org/metastore/metadata/identifier"
    }
  ],
  "creators": [
    {
      "name": "Smith, Jane"
    }
  ],
  "titles": [
    {
      "title": "Minimal example for a(n) (XML) metadata document"
    }
  ],
  "publisher": "NFDI4Ing",
  "publicationYear": "2021",
  "formats": [
    "application/xml"
  ],
  "types": {
    "resourceTypeGeneral": "Other",
    "resourceType": "XML"
  },
  "relatedIdentifiers": [
    {
      "relatedIdentifier": "https://example.org/datarepository/identifier",
      "relatedIdentifierType": "URL",
      "relationType": "IsMetadataFor",
    }, 
    {
      "relatedIdentifier": "https://example.org/metastore/schema/identifier",
      "relatedIdentifierType": "URL",
      "relationType": "IsDescribedBy",
    }
  ],
  "schemaVersion": "http://datacite.org/schema/kernel-4"
}

```
#### Example for a minimal DataCite JSON Schema for an XSD document: 
```
{
  "identifiers": [
    {
      "identifierType": "URL",
      "identifier": "https://example.org/metastore/metadata/identifier"
    }
  ],
  "creators": [
    {
      "name": "Smith, Jane"
    }
  ],
  "titles": [
    {
      "title": "Minimal example for a(n) (XSD) schema document"
    }
  ],
  "publisher": "NFDI4Ing",
  "publicationYear": "2021",
  "formats": [
    "application/xml"
  ],
  "types": {
    "resourceTypeGeneral": "Other",
    "resourceType": "XML"
  },
  "schemaVersion": "http://datacite.org/schema/kernel-4"
}

```

