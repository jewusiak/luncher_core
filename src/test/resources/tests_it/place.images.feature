Feature: CRUD of Assets based on Place's images

  Background:
    Given User exists:
      | uuid                                 | email                 | password | name    | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp | 1234     | MANAGER | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp      | 1234     | MOD     | Json2   | SYS_MOD      |

    And place types exist:
      | identifier | iconName   | name       |
      | RESTAURANT | restaurant | Restaurant |

    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID -1:
      | name | description | placeTypeIdentifier | enabled |
      | name | descr       | RESTAURANT          | true    |

    And response code is 200

  Scenario Outline: User creates and uploads images, then img is removed
    When User uploads place image as below:
      | placeUuid     | description   | filePath   |
      | [ID:PLACE:-1] | example descr | <filePath> |

    Then response code is 200
    And Put ID ASSET idx -1 to cache from HTTP response from path id
    And HTTP Response is:
      | id            | description   | mimeType           | uploadStatus |
      | [ID:ASSET:-1] | example descr | <expectedMimetype> | UPLOADED     |

    And File uploaded-assets-test/[ID:ASSET:-1].<mimetypeResultingExtension> exists

    When Send GET request to /place/[ID:PLACE:-1] without body
    Then response code is 200
    And HTTP Response has a list of size 1 in path images
    And HTTP Response is:
      | id            | images[0].id  | images[0].accessUrl                       | images[0].mimeType |
      | [ID:PLACE:-1] | [ID:ASSET:-1] | http://localhost:8080/asset/[ID:ASSET:-1] | <expectedMimetype> |

    And Put variable ACC_URL to cache from HTTP response from path images[0].accessUrl

    When Send GET request to [VAR:ACC_URL] without body
    Then response code is 200
    And Response binary contents are equal to file <filePath>

    When Send DELETE request to /asset/[ID:ASSET:-1] without body
    Then response code is 204
    And File uploaded-assets-test/[ID:ASSET:-1].<mimetypeResultingExtension> does not exist

    When Send GET request to /place/[ID:PLACE:-1] without body
    Then response code is 200
    And HTTP Response has a list of size 0 in path images

    Examples:
      | filePath             | expectedMimetype | mimetypeResultingExtension |
      | test-assets/img.png  | image/png        | png                        |
      | test-assets/img.jpg  | image/jpeg       | jpeg                       |
      | test-assets/img.jpeg | image/jpeg       | jpeg                       |

  Scenario Outline: Cascade place deletion on images
    When User uploads place image as below:
      | placeUuid     | description   | filePath   |
      | [ID:PLACE:-1] | example descr | <filePath> |

    Then response code is 200
    And Put ID ASSET idx -1 to cache from HTTP response from path id
    And HTTP Response is:
      | id            | description   | mimeType           | uploadStatus |
      | [ID:ASSET:-1] | example descr | <expectedMimetype> | UPLOADED     |

    And File uploaded-assets-test/[ID:ASSET:-1].<mimetypeResultingExtension> exists

    When Send DELETE request to /place/[ID:PLACE:-1] without body
    Then response code is 204

    And File uploaded-assets-test/[ID:ASSET:-1].<mimetypeResultingExtension> does not exist

    Examples:
      | filePath            | expectedMimetype | mimetypeResultingExtension |
      | test-assets/img.png | image/png        | png                        |
    
  Scenario Outline: Reordering images
    When User uploads place image as below:
      | placeUuid     | description   | filePath   |
      | [ID:PLACE:-1] | example descr | <filePath> |

    Then response code is 200
    And Put ID ASSET idx 1 to cache from HTTP response from path id
    And HTTP Response is:
      | id           | description   | mimeType           | uploadStatus |
      | [ID:ASSET:1] | example descr | <expectedMimetype> | UPLOADED     |

    And File uploaded-assets-test/[ID:ASSET:1].<mimetypeResultingExtension> exists

    When User uploads place image as below:
      | placeUuid     | description    | filePath   |
      | [ID:PLACE:-1] | example descr2 | <filePath> |

    Then response code is 200
    And Put ID ASSET idx 2 to cache from HTTP response from path id
    And HTTP Response is:
      | id           | description    | mimeType           | uploadStatus |
      | [ID:ASSET:2] | example descr2 | <expectedMimetype> | UPLOADED     |

    And File uploaded-assets-test/[ID:ASSET:2].<mimetypeResultingExtension> exists

    When Send GET request to /place/[ID:PLACE:-1] without body
    Then response code is 200
    And HTTP Response has a list of size 2 in path images
    And HTTP Response is:
      | id            | images[0].id | images[1].id |
      | [ID:PLACE:-1] | [ID:ASSET:1] | [ID:ASSET:2] |
    
    When Send PUT request to /place/[ID:PLACE:-1] with body as below:
    """
    {
      "imageIds": [
        [ID:ASSET:2],
        [ID:ASSET:1]
      ]
    }
    """
    
    When Send GET request to /place/[ID:PLACE:-1] without body
    Then response code is 200
    And HTTP Response has a list of size 2 in path images
    And HTTP Response is:
      | id            | images[0].id | images[1].id |
      | [ID:PLACE:-1] | [ID:ASSET:1] | [ID:ASSET:2] |

    
    When Send PUT request to /place/[ID:PLACE:-1] with body as below:
    """
    {
      "imageIds": [
        [ID:ASSET:1],
        [ID:ASSET:2]
      ]
    }
    """
    
    When Send GET request to /place/[ID:PLACE:-1] without body
    Then response code is 200
    And HTTP Response has a list of size 2 in path images
    And HTTP Response is:
      | id            | images[0].id | images[1].id |
      | [ID:PLACE:-1] | [ID:ASSET:1] | [ID:ASSET:2] |


    Examples:
      | filePath            | expectedMimetype | mimetypeResultingExtension |
      | test-assets/img.png | image/png        | png                        |
    