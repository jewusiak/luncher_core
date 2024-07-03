Feature: CRUD - Place

  Background:
    Given User exists:
      | uuid                                 | email              | password | name      | surname | role |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp  | 1234     | Zbigniew  | Json    | USER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | user2@luncher.corp | 1234     | Zbigniew2 | Json2   | USER |


  Scenario: User creates place
    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    When User creates a place as below:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    Then response code is 200

    And Retrieved place with last created UUID is as below:
      | name | description |
      | name | descr       |

  Scenario: Delete place by other user on the same level
    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp

    And User creates a place as below:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    And response code is 200

    And User logs in using credentials:
      | email              | password |
      | user2@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user2@luncher.corp

    And Place deletion with last created UUID results in 403 code

