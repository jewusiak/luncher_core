Feature: CRUD - Place

  Background:
    Given User exists:
      | uuid                                 | email                 | password | name      | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp     | 1234     | Zbigniew  | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | ruser@luncher.corp    | 1234     | Zbigniew  | Json    | REST_USER    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | ruser2@luncher.corp   | 1234     | Zbigniew2 | Json2   | REST_USER    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp | 1234     | MOD       | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp    | 1234     | ADMIN     | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp      | 1234     | MOD       | Json2   | SYS_MOD      |


  Scenario: User creates place
    Given User logs in using credentials:
      | email             | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    When User creates a place as below:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    Then response code is 200

    And Retrieved place with last created UUID is as below:
      | name | description |
      | name | descr       |

  Scenario: Try to create place by end-user:
    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And User creates a place as below:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    And response code is 403


  Scenario Outline: Delete place by user and other users
    Given User logs in using credentials:
      | email             | password |
      | ruser@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as ruser@luncher.corp

    And User creates a place as below:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    And response code is 200

    And User logs in using credentials:
      | email       | password |
      | <otherUser> | 1234     |
    And response code is 200
    And User is logged in as <otherUser>

    And Place deletion with last created UUID results in <statusCode> code

    Examples:
      | otherUser             | statusCode |
      | user@luncher.corp     | 403        |
      | ruser@luncher.corp    | 204        |
      | ruser2@luncher.corp   | 403        |
      | rmanager@luncher.corp | 403        |
      #todo: addmanagers
      | mod@luncher.corp      | 204        |
      | admin@luncher.corp    | 204        |

