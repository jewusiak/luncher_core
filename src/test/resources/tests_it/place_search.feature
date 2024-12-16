Feature: Place search engine

  Background:
    Given User exists:
      | uuid                                 | email                  | password | name     | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp      | 1234     | Zbigniew | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp  | 1234     | MAN      | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | rmanager2@luncher.corp | 1234     | MAN2     | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp     | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp       | 1234     | MOD      | Json2   | SYS_MOD      |

    And place types exist:
      | identifier | iconName   | name       |
      | RESTAURANT | restaurant | Restaurant |
      | BAR        | bar        | Bar        |


  Scenario: Location-wise places search

    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID 1:
      | name            | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | The Cool Cat TR | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200

    And User creates a place as below ID 2:
      | name              | description                                                                                                                        | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | Restauracja Ceska | Restauracja o swobodnej atmosferze oferująca czeskie klasyki, takie jak smażony ser, dania mięsne i gulasz, a także piwo z beczki. | RESTAURANT          | 52.23165442404853 | 21.011960242452425 | true    |
    And response code is 200

    And User creates a place as below ID 3:
      | name          | description                                                                                         | placeTypeIdentifier | location.latitude  | location.longitude | enabled |
      | BARdzo bardzo | Klub Muzyczny Bardzo Bardzo: Serce Warszawskiej Sceny Muzycznej. Jam Sessions, NY Funky Nights etc. | BAR                 | 52.229357307480846 | 21.017813616696706 | true    |
    And response code is 200

#    And Send PUT request to /place/[ID:PLACE:1] with body as below:
#    """
#    {
#   "openingWindows":[
#      {
#         "startTime":{
#            "day":"MONDAY",
#            "time":"10:00"
#         },
#         "endTime":{
#            "day":"MONDAY",
#            "time":"22:00"
#         }
#      }
#   ]
#}
#    """

#    And response code is 200
    And User logs out (by removing saved auth token)

    And User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And Refresh Hibernate Search indexes

#    distances:
#    ~1270m to The Cool Cat TR
#    ~777m to Restauracja Ceska
#    ~342m to Bardzo Bardzo
    When Place Search request is sent as below:
      | location.latitude | location.longitude | location.radius | size | page |
      | 52.22640473227021 | 21.019359661284454 | 340             | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 0 in path .

    When Place Search request is sent as below:
      | location.latitude | location.longitude | location.radius | size | page |
      | 52.22640473227021 | 21.019359661284454 | 345             | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response contains:
      | [ID:PLACE:3] |

    When Place Search request is sent as below:
      | location.latitude | location.longitude | location.radius | size | page |
      | 52.22640473227021 | 21.019359661284454 | 800             | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path .
    And HTTP Response contains:
      | [ID:PLACE:2] |
      | [ID:PLACE:3] |

    When Place Search request is sent as below:
      | location.latitude | location.longitude | location.radius | size | page |
      | 52.22640473227021 | 21.019359661284454 | 1300            | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 3 in path .
    And HTTP Response contains:
      | [ID:PLACE:1] |
      | [ID:PLACE:2] |
      | [ID:PLACE:3] |

    When Place Search request is sent as below:
      | location.latitude | location.longitude | location.radius | placeTypeIdentifier | size | page |
      | 52.22640473227021 | 21.019359661284454 | 1300            | RESTAURANT          | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path .
    And HTTP Response contains:
      | [ID:PLACE:1] |
      | [ID:PLACE:2] |


  Scenario: Places search by text/type

    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID 1:
      | name            | longName                       | description                                                          | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | The Cool Cat TR | The Cool Cat Teatr Rozmaitości | Restauracja typu asian fusion w centrum Warszawy. Azjatyckie potrawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200

    And User creates a place as below ID 2:
      | name  | longName          | description                                                                                                                        | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | Ceska | Restauracja Ceska | Restauracja o swobodnej atmosferze oferująca czeskie klasyki, takie jak smażony ser, dania mięsne i gulasz, a także piwo z beczki. | RESTAURANT          | 52.23165442404853 | 21.011960242452425 | true    |
    And response code is 200

    And User creates a place as below ID 3:
      | name          | longName                    | description                                                                                       | placeTypeIdentifier | location.latitude  | location.longitude | enabled |
      | BARdzo bardzo | Klub muzyczny BARdzo bardzo | Bardzo Bardzo: Bar będący Sercem Warszawskiej Sceny Muzycznej. Jam Sessions, NY Funky Nights etc. | BAR                 | 52.229357307480846 | 21.017813616696706 | true    |
    And response code is 200

    And User creates a place as below ID 4:
      | name          | longName          | description                                                                                                                                                            | placeTypeIdentifier | location.latitude  | location.longitude | enabled | address.firstLine       | address.district | address.city |
      | Czeska Baszta | Bar Czeska Baszta | Jedno z nielicznych miejsc w Warszawie gdzie można napić się dobrego czeskiego piwa prosto z kranu np. Litovel’a czy Holby. Lokalne rodzinne browary z Czech w Polsce. | BAR                 | 52.234388948039566 | 21.034098784160808 | true    | Aleje Jerozolimskie 191 | Śródmieście      | Warszawa     |
    And response code is 200

    And User logs out (by removing saved auth token)

    And User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And Refresh Hibernate Search indexes



    When Place Search request is sent as below:
      | textQuery | size | page |
      | bar       | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path .
    And HTTP Response contains:
      | [ID:PLACE:3] |
      | [ID:PLACE:4] |


    When Place Search request is sent as below:
      | textQuery   | size | page |
      | Restauracja | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path .
    And HTTP Response contains:
      | [ID:PLACE:2] |
      | [ID:PLACE:1] |


    When Place Search request is sent as below:
      | textQuery | size | page |
      | Czeska    | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path .
    And HTTP Response contains:
      | [ID:PLACE:2] |
      | [ID:PLACE:4] |


    When Place Search request is sent as below:
      | textQuery | placeTypeIdentifier | size | page |
      | Czeska    | BAR                 | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response contains:
      | [ID:PLACE:4] |


    When Place Search request is sent as below:
      | textQuery | placeTypeIdentifier | size | page |
      | Czeska    | RESTAURANT          | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response contains:
      | [ID:PLACE:2] |


    When Place Search request is sent as below:
      | textQuery         | size | page |
      | kuchnia azjatycka | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response is:
      | [0].id       |
      | [ID:PLACE:1] |

    When Place Search request is sent as below:
      | textQuery     | size | page |
      | jerozolimskie | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response is:
      | [0].id       |
      | [ID:PLACE:4] |

    When Place Search request is sent as below:
      | textQuery   | size | page |
      | śródmieście | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response is:
      | [0].id       |
      | [ID:PLACE:4] |

    
