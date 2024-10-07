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
      | identifier | iconName   | name                |
      | RESTAURANT | restaurant | Restaurant category |
      | BAR        | bar        | Bar category name   |


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
      | name | description | owner.email           | placeType.identifier |
      | name | descr       | rmanager@luncher.corp | RESTAURANT           |

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
      | name | description | placeTypeIdentifier | facebookPageId | location.latitude | location.longitude |
      | name | descr       | RESTAURANT          | fbid           | 52.21507395584024 | 21.02108986309555  |

    Then response code is 200

    And GET place with ID -1 is as below:
      | name | description | phoneNumber | facebookPageId | location.latitude | location.longitude | placeType.identifier |
      | name | descr       |             | fbid           | 52.21507395584024 | 21.02108986309555  | RESTAURANT           |

    And Place ID -1 is as below:
      | owner.email           |
      | rmanager@luncher.corp |

    # update (BEGIN)

    # description is null so not updated
    When Updates Place with ID -1 with data below:
      | location.latitude | location.longitude | placeTypeIdentifier |
      | -10               | -15.666            | BAR                 |

    Then response code is 200

    And GET place with ID -1 is as below:
      | name | description | phoneNumber | facebookPageId | location.latitude | location.longitude | placeType.identifier |
      | name | descr       |             | fbid           | -10               | -15.666            | BAR                  |

    # description is null so not updated
    When Updates Place with ID -1 with data below:
      | name     | longName | phoneNumber     | description | facebookPageId |
      | New name | long2    | +48 000 111 222 |             | ""             |

    Then response code is 200

    And GET place with ID -1 is as below:
      | name     | description | longName | phoneNumber     | facebookPageId |
      | New name | descr       | long2    | +48 000 111 222 | ""             |

    # change owner (BEGIN)

    When Updates Place with ID -1 with data below:
      | owner.email            |
      | rmanager2@luncher.corp |

    Then response code is 200

    # after transferring ownership

    When Updates Place with ID -1 with data below:
      | owner.email            |
      | rmanager1@luncher.corp |

    Then response code is 403

    # try to update (BEGIN)

    When Updates Place with ID -1 with data below:
      | name      |
      | New name2 |

    Then response code is 403

    And Place ID -1 is as below:
      | owner.email            |
      | rmanager2@luncher.corp |

  Scenario: Place opening times - on creation, update and closing

    And User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID 1:
      | name            | description                                      | placeTypeIdentifier | location.latitude | location.longitude |
      | The Cool Cat TR | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  |
    And response code is 200

    #todo: create/delete/edit opening times
