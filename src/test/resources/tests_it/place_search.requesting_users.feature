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

    And User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID 1:
      | name | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | R1   | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200

    And User creates a place as below ID 2:
      | name | description                                                                                                                        | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | R2   | Restauracja o swobodnej atmosferze oferująca czeskie klasyki, takie jak smażony ser, dania mięsne i gulasz, a także piwo z beczki. | RESTAURANT          | 52.23165442404853 | 21.011960242452425 | false   |
    And response code is 200


    And User logs in using credentials:
      | email                  | password |
      | rmanager2@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager2@luncher.corp

    And User creates a place as below ID 3:
      | name | description                                                                                         | placeTypeIdentifier | location.latitude  | location.longitude | enabled |
      | R3   | Klub Muzyczny Bardzo Bardzo: Serce Warszawskiej Sceny Muzycznej. Jam Sessions, NY Funky Nights etc. | BAR                 | 52.229357307480846 | 21.017813616696706 | true    |
    And response code is 200

    And Refresh Hibernate Search indexes

    And User logs out (by removing saved auth token)

  Scenario: Manager 1 - sees own places (both enabled/disabled) and can filter by en/dis (and param owner is ignored)

    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    When Place Search request is sent as below:
      | size | page |
      | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path results
    And HTTP Response is:
      | results[0].id | results[1].id |
      | [ID:PLACE:1]  | [ID:PLACE:2]  |

    When Place Search request is sent as below:
      | enabled | size | page |
      | true    | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path results
    And HTTP Response is:
      | results[0].id |
      | [ID:PLACE:1]  |

    When Place Search request is sent as below:
      | enabled | size | page |
      | false   | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path results
    And HTTP Response is:
      | results[0].id |
      | [ID:PLACE:2]  |

    # owner param is ignored

    When Place Search request is sent as below:
      | enabled | owner                                | size | page |
      | false   | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path results
    And HTTP Response is:
      | results[0].id |
      | [ID:PLACE:2]  |


  Scenario: Manager 2 - sees own places (both enabled/disabled) and can filter by en/dis (and param owner is ignored)

    Given User logs in using credentials:
      | email                  | password |
      | rmanager2@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager2@luncher.corp


    When Place Search request is sent as below:
      | size | page |
      | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path results
    And HTTP Response is:
      | results[0].id |
      | [ID:PLACE:3]  |

    When Place Search request is sent as below:
      | enabled | size | page |
      | true    | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path results
    And HTTP Response is:
      | results[0].id |
      | [ID:PLACE:3]  |

    When Place Search request is sent as below:
      | enabled | size | page |
      | false   | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 0 in path results


  Scenario: USER - sees all enabled places (and param owner is ignored)

    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp


    When Place Search request is sent as below:
      | size | page |
      | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path results
    And HTTP Response is:
      | results[0].id | results[1].id |
      | [ID:PLACE:1]  | [ID:PLACE:3]  |

    When Place Search request is sent as below:
      | enabled | size | page |
      | true    | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path results
    And HTTP Response is:
      | results[0].id | results[1].id |
      | [ID:PLACE:1]  | [ID:PLACE:3]  |

    When Place Search request is sent as below:
      | enabled | size | page |
      | false   | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path results
    And HTTP Response is:
      | results[0].id | results[1].id |
      | [ID:PLACE:1]  | [ID:PLACE:3]  |
    
    # owner param is ignored

    When Place Search request is sent as below:
      | enabled | owner                                | size | page |
      | false   | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 2 in path results
    And HTTP Response is:
      | results[0].id | results[1].id |
      | [ID:PLACE:1]  | [ID:PLACE:3]  |

    
    
