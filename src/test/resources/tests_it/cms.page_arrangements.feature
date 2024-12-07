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
  "primaryPage": true,
  "sections": [
    {
      "sectionHeader": "Restaurant Week",
      "sectionSubheader": "Restauracje biorące udział w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:1]",
          "elementType": "PLACE",
          "heading": "Restauracja R1",
          "thumbnailId": "[ID:ASSET:1]"
        },
        {
          "sourceElementId": "[ID:PLACE:3]",
          "elementType": "PLACE",
          "heading": "Restauracja R3",
          "thumbnailId": "[ID:ASSET:3]"
        }
      ]
    },
    {
      "sectionHeader": "Restaurant NieWeek",
      "sectionSubheader": "Restauracje nie biorące udział w programie",
      "sectionElements": [
        {
          "sourceElementId": "[ID:PLACE:2]",
          "elementType": "PLACE",
          "heading": "Restauracja R2",
          "thumbnailId": "[ID:ASSET:2]"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Send GET request to /content-management/primary without body
    And response code is 200
    And HTTP Response is:
      |  |

