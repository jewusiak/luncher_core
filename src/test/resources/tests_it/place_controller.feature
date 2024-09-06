Feature: CRUD - Place

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


  Scenario: User creates place
    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    When User creates a place as below ID 3:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    Then response code is 200

    And GET place with ID 3 is as below:
      | name | description |
      | name | descr       |

    And Place ID 3 is as below:
      | owner.email           |
      | rmanager@luncher.corp |


  Scenario: Try to create place by end-user:
    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And User creates a place as below ID -1:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    And response code is 403


  Scenario Outline: Delete place by user and other users
    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    And User creates a place as below ID -1:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    And response code is 200

    And User logs in using credentials:
      | email       | password |
      | <otherUser> | 1234     |
    And response code is 200
    And User is logged in as <otherUser>

    And Place deletion with ID -1 results in <statusCode> code

    Examples:
      | otherUser              | statusCode |
      | user@luncher.corp      | 403        |
      | rmanager@luncher.corp  | 204        |
      | rmanager2@luncher.corp | 403        |
      | mod@luncher.corp       | 204        |
      | admin@luncher.corp     | 204        |

  Scenario: Place: create, update, change owner, try to update
    # create (BEGIN)
    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    When User creates a place as below ID -1:
      | name | description | placeTypeIdentifier | facebookPageId |
      | name | descr       | RESTAURANT          | fbid           |

    Then response code is 200

    And GET place with ID -1 is as below:
      | name | description | phoneNumber | facebookPageId |
      | name | descr       |             | fbid           |

    And Place ID -1 is as below:
      | owner.email           |
      | rmanager@luncher.corp |

    # update (BEGIN)

    # description is null so not updated
    When Updates Place with ID -1 with data below:
      | name     | longName | phoneNumber     | description | facebookPageId |
      | New name | long2    | +48 000 111 222 |             | ""             |

    Then response code is 200

    And GET place with ID -1 is as below:
      | name     | description | longName | phoneNumber     | facebookPageId |
      | New name | descr       | long2    | +48 000 111 222 | ""             |

    # change owner (BEGIN)

    When Request to change owner for Place ID -1 is sent:
      | email                  |
      | rmanager2@luncher.corp |

    Then response code is 200

    # try to update (BEGIN)

    When Updates Place with ID -1 with data below:
      | name      |
      | New name2 |

    Then response code is 403

    And Place ID -1 is as below:
      | owner.email            |
      | rmanager2@luncher.corp |


  Scenario: Place search
    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    And User creates a place as below ID -1:
      | name         | description                                      | placeTypeIdentifier | location.latitude | location.longitude |
      | The Cool Cat | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 1.223             | -20.34             |

    And response code is 200
    And User logs out (by removing saved auth token)

    And User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And Refresh Hibernate Search indexes

    When Place Search request is sent as below:
      | location.latitude | location.longitude | location.radius | size | page |
      | 1.223             | 1.226554           | 300             | 10   | 0    |

    Then response code is 200
    And HTTP Response has a list of size 1 in path results
    And HTTP Response is:
      | results[0].name |
      | The Cool Cat    |




    When Place Search request is sent as below:
      | textQuery | size | page |
      | asian     | 10   | 0    |

    Then response code is 200
    
    
