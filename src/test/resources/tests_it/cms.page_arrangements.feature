Feature: CMS

  Background:
    Given User exists:
      | uuid                                 | email                 | password | name     | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp     | 1234     | Zbigniew | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp | 1234     | MAN      | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp    | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp      | 1234     | MOD      | Json2   | SYS_MOD      |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e8 | root@luncher.corp     | 1234     | ROOT     | Json2   | SYS_ROOT     |

    And place types exist:
      | identifier | iconName   | name        |
      | RESTAURANT | restaurant | Restaurants |
      | BAR        | bar        | Bars        |
      | CLUB       | club       | Clubs       |
      | CAFE       | coffee     | Cafes       |


  Scenario: Create and get primary offer by any user
    And User logs in using credentials:
      | email              | password |
      | admin@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as admin@luncher.corp

    And User creates a place as below ID 1:
      | name | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | R1   | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200

    And User creates a place as below ID 2:
      | name | description                                                                                                                        | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | R2   | Restauracja o swobodnej atmosferze oferująca czeskie klasyki, takie jak smażony ser, dania mięsne i gulasz, a także piwo z beczki. | RESTAURANT          | 52.23165442404853 | 21.011960242452425 | false   |
    And response code is 200

    And User creates a place as below ID 3:
      | name | description                                                                                         | placeTypeIdentifier | location.latitude  | location.longitude | enabled |
      | R3   | Klub Muzyczny Bardzo Bardzo: Serce Warszawskiej Sceny Muzycznej. Jam Sessions, NY Funky Nights etc. | BAR                 | 52.229357307480846 | 21.017813616696706 | true    |
    And response code is 200


    When User uploads image as below:
      | description      | filePath            |
      | example descr R1 | test-assets/img.png |

    Then response code is 200
    And Put ID ASSET idx 1 to cache from HTTP response from path id


    When User uploads image as below:
      | description      | filePath            |
      | example descr R2 | test-assets/img.png |

    Then response code is 200
    And Put ID ASSET idx 2 to cache from HTTP response from path id


    When User uploads image as below:
      | description      | filePath            |
      | example descr R3 | test-assets/img.png |

    Then response code is 200
    And Put ID ASSET idx 3 to cache from HTTP response from path id


    When Send POST request to /content-management/arrangements with body as below:
    """
    {
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "header": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        },
        {
          "sourceElementId": "[ID:PLACE:3]",
          "elementType": "PLACE",
          "header": "Restauracja R3",
          "thumbnailId": "[ID:ASSET:3]"
        }
      ]
    },
    {
      "sectionHeader": "Restaurant NieWeek",
      "sectionSubheader": "Restauracje nie biorące udziału w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:2]",
          "elementType": "PLACE",
          "header": "Restauracja R2",
          "thumbnailId": "[ID:ASSET:2]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Put ID ARRANGEMENT idx 1 to cache from HTTP response from path id
    And Send GET request to /content-management/arrangements/[ID:ARRANGEMENT:1] without body
    And response code is 200
    And HTTP Response is:
      | id                 | primaryPage | sections[0].sectionHeader | sections[0].sectionSubheader           | sections[0].sectionElements[0].header | sections[0].sectionElements[0].thumbnailId | sections[0].sectionElements[0].thumbnailAccessUrl | sections[0].sectionElements[1].header | sections[0].sectionElements[1].thumbnailId | sections[0].sectionElements[1].thumbnailAccessUrl | sections[1].sectionHeader | sections[1].sectionSubheader                | sections[1].sectionElements[0].header | sections[1].sectionElements[0].thumbnailId | sections[1].sectionElements[0].thumbnailAccessUrl |
      | [ID:ARRANGEMENT:1] | false       | Restaurant Week           | Restauracje biorące udział w programie | Restauracja R1                         | [ID:ASSET:1]                               | http://localhost:8080/asset/[ID:ASSET:1]          | Restauracja R3                         | [ID:ASSET:3]                               | http://localhost:8080/asset/[ID:ASSET:3]          | Restaurant NieWeek        | Restauracje nie biorące udziału w programie | Restauracja R2                         | [ID:ASSET:2]                               | http://localhost:8080/asset/[ID:ASSET:2]          |

    When Send GET request to /content-management/arrangements/primary without body
    Then response code is 404

    When Send PUT request to /content-management/arrangements/[ID:ARRANGEMENT:1]/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage | sections[0].sectionHeader | sections[0].sectionSubheader           | sections[0].sectionElements[0].header | sections[0].sectionElements[0].thumbnailId | sections[0].sectionElements[0].thumbnailAccessUrl | sections[0].sectionElements[1].header | sections[0].sectionElements[1].thumbnailId | sections[0].sectionElements[1].thumbnailAccessUrl | sections[1].sectionHeader | sections[1].sectionSubheader                | sections[1].sectionElements[0].header | sections[1].sectionElements[0].thumbnailId | sections[1].sectionElements[0].thumbnailAccessUrl |
      | [ID:ARRANGEMENT:1] | true        | Restaurant Week           | Restauracje biorące udział w programie | Restauracja R1                         | [ID:ASSET:1]                               | http://localhost:8080/asset/[ID:ASSET:1]          | Restauracja R3                         | [ID:ASSET:3]                               | http://localhost:8080/asset/[ID:ASSET:3]          | Restaurant NieWeek        | Restauracje nie biorące udziału w programie | Restauracja R2                         | [ID:ASSET:2]                               | http://localhost:8080/asset/[ID:ASSET:2]          |

    When Send GET request to /content-management/arrangements/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage | sections[0].sectionHeader | sections[0].sectionSubheader           | sections[0].sectionElements[0].header | sections[0].sectionElements[0].thumbnailId | sections[0].sectionElements[0].thumbnailAccessUrl | sections[0].sectionElements[1].header | sections[0].sectionElements[1].thumbnailId | sections[0].sectionElements[1].thumbnailAccessUrl | sections[1].sectionHeader | sections[1].sectionSubheader                | sections[1].sectionElements[0].header | sections[1].sectionElements[0].thumbnailId | sections[1].sectionElements[0].thumbnailAccessUrl |
      | [ID:ARRANGEMENT:1] | true        | Restaurant Week           | Restauracje biorące udział w programie | Restauracja R1                         | [ID:ASSET:1]                               | http://localhost:8080/asset/[ID:ASSET:1]          | Restauracja R3                         | [ID:ASSET:3]                               | http://localhost:8080/asset/[ID:ASSET:3]          | Restaurant NieWeek        | Restauracje nie biorące udziału w programie | Restauracja R2                         | [ID:ASSET:2]                               | http://localhost:8080/asset/[ID:ASSET:2]          |

    When Send PUT request to /content-management/arrangements/[ID:ARRANGEMENT:1] with body as below:
    """
    {
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
      {
          "sourceElementId": "[ID:PLACE:3]",
          "elementType": "PLACE",
          "header": "Restauracja R3",
          "thumbnailId": "[ID:ASSET:3]"
        },
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "header": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        }
        
      ]
    },
    {
      "sectionHeader": "Restaurant NieWeek",
      "sectionSubheader": "Restauracje nie biorące udziału w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:2]",
          "elementType": "PLACE",
          "header": "Restauracja R2",
          "thumbnailId": "[ID:ASSET:2]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage | sections[0].sectionHeader | sections[0].sectionSubheader           | sections[0].sectionElements[1].header | sections[0].sectionElements[1].thumbnailId | sections[0].sectionElements[1].thumbnailAccessUrl | sections[0].sectionElements[0].header | sections[0].sectionElements[0].thumbnailId | sections[0].sectionElements[0].thumbnailAccessUrl | sections[1].sectionHeader | sections[1].sectionSubheader                | sections[1].sectionElements[0].header | sections[1].sectionElements[0].thumbnailId | sections[1].sectionElements[0].thumbnailAccessUrl |
      | [ID:ARRANGEMENT:1] | true        | Restaurant Week           | Restauracje biorące udział w programie | Restauracja R1                         | [ID:ASSET:1]                               | http://localhost:8080/asset/[ID:ASSET:1]          | Restauracja R3                         | [ID:ASSET:3]                               | http://localhost:8080/asset/[ID:ASSET:3]          | Restaurant NieWeek        | Restauracje nie biorące udziału w programie | Restauracja R2                         | [ID:ASSET:2]                               | http://localhost:8080/asset/[ID:ASSET:2]          |



    When User uploads image as below:
      | description         | filePath            |
      | example descr R3 -1 | test-assets/img.png |

    Then response code is 200
    And Put ID ASSET idx 4 to cache from HTTP response from path id


    When Send PUT request to /content-management/arrangements/[ID:ARRANGEMENT:1] with body as below:
    """
    {
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
      {
          "sourceElementId": "[ID:PLACE:3]",
          "elementType": "PLACE",
          "header": "Restauracja R3",
          "thumbnailId": "[ID:ASSET:4]"
        },
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "header": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        }
        
      ]
    },
    {
      "sectionHeader": "Restaurant NieWeek",
      "sectionSubheader": "Restauracje nie biorące udziału w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:2]",
          "elementType": "PLACE",
          "header": "Restauracja R2",
          "thumbnailId": "[ID:ASSET:2]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage | sections[0].sectionHeader | sections[0].sectionSubheader           | sections[0].sectionElements[1].header | sections[0].sectionElements[1].thumbnailId | sections[0].sectionElements[1].thumbnailAccessUrl | sections[0].sectionElements[0].header | sections[0].sectionElements[0].thumbnailId | sections[0].sectionElements[0].thumbnailAccessUrl | sections[1].sectionHeader | sections[1].sectionSubheader                | sections[1].sectionElements[0].header | sections[1].sectionElements[0].thumbnailId | sections[1].sectionElements[0].thumbnailAccessUrl |
      | [ID:ARRANGEMENT:1] | true        | Restaurant Week           | Restauracje biorące udział w programie | Restauracja R1                         | [ID:ASSET:1]                               | http://localhost:8080/asset/[ID:ASSET:1]          | Restauracja R3                         | [ID:ASSET:4]                               | http://localhost:8080/asset/[ID:ASSET:4]          | Restaurant NieWeek        | Restauracje nie biorące udziału w programie | Restauracja R2                         | [ID:ASSET:2]                               | http://localhost:8080/asset/[ID:ASSET:2]          |





    When Send POST request to /content-management/arrangements with body as below:
    """
    {
  "sections": [
    {
      "sectionHeader": "Restaurant Week2",
      "sectionSubheader": "Restauracje biorące udział w programie2"
    }
  ]
}
    """
    Then response code is 200
    And Put ID ARRANGEMENT idx 2 to cache from HTTP response from path id


    When Send GET request to /content-management/arrangements/[ID:ARRANGEMENT:2] without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:2] | false       |

    When Send GET request to /content-management/arrangements/[ID:ARRANGEMENT:1] without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:1] | true        |

    When Send GET request to /content-management/arrangements/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:1] | true        |


    When Send PUT request to /content-management/arrangements/[ID:ARRANGEMENT:2]/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:2] | true        |

    When Send GET request to /content-management/arrangements/[ID:ARRANGEMENT:2] without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:2] | true        |

    When Send GET request to /content-management/arrangements/[ID:ARRANGEMENT:1] without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:1] | false       |

    When Send GET request to /content-management/arrangements/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:2] | true        |

    When User logs out (by removing saved auth token)
    Then Send GET request to /content-management/arrangements/primary without body
    And response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:2] | true        |
    And Send GET request to /content-management/arrangements/[ID:ARRANGEMENT:1] without body
    And response code is 401

  Scenario: Try to remove asset (image/thumbnail) associated with an arrangement
    And User logs in using credentials:
      | email              | password |
      | admin@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as admin@luncher.corp

    And User creates a place as below ID 1:
      | name | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | R1   | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200


    When User uploads image as below:
      | description      | filePath            |
      | example descr R1 | test-assets/img.png |

    Then response code is 200
    And Put ID ASSET idx 1 to cache from HTTP response from path id


    When Send POST request to /content-management/arrangements with body as below:
    """
   {
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "header": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Put ID ARRANGEMENT idx 1 to cache from HTTP response from path id

    When Send DELETE request to /asset/[ID:ASSET:1] without body
    Then response code is 400


  Scenario: Remove a primary arrangement
    And User logs in using credentials:
      | email              | password |
      | admin@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as admin@luncher.corp

    And User creates a place as below ID 1:
      | name | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | R1   | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200


    When User uploads image as below:
      | description      | filePath            |
      | example descr R1 | test-assets/img.png |

    Then response code is 200
    And Put ID ASSET idx 1 to cache from HTTP response from path id


    When Send POST request to /content-management/arrangements with body as below:
    """
   {
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "header": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Put ID ARRANGEMENT idx 1 to cache from HTTP response from path id


    When Send POST request to /content-management/arrangements with body as below:
    """
   {
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "header": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Put ID ARRANGEMENT idx 2 to cache from HTTP response from path id

    When Send PUT request to /content-management/arrangements/[ID:ARRANGEMENT:1]/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:1] | true        |

    When Send DELETE request to /content-management/arrangements/[ID:ARRANGEMENT:1] without body
    Then response code is 400


    When Send PUT request to /content-management/arrangements/[ID:ARRANGEMENT:2]/primary without body
    Then response code is 200
    And HTTP Response is:
      | id                 | primaryPage |
      | [ID:ARRANGEMENT:2] | true        |

    When Send DELETE request to /content-management/arrangements/[ID:ARRANGEMENT:1] without body
    Then response code is 204
    