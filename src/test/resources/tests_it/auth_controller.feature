Feature: Authentication controller features

  Scenario: User registers

    When User registers as below:
      | email          | password | firstName | surname |
      | e@luncher.corp | 1234     | Zbigniew  | Json    |
    Then response code is 204

    When User logs in using credentials:
      | email          | password |
      | e@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as e@luncher.corp

    And GET profile of authenticated user is:
      | email          | firstName | surname | role |
      | e@luncher.corp | Zbigniew  | Json    | USER |


  Scenario: Bad requests
    When User registers as below:
      | email | password | firstName | surname |
      |       | 1234     | Zbigniew  | Json    |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl |          | Zbigniew  | Json    |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | 1234     |           | Json    |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | 1234     | Zbigniew  |         |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | 1234     |           |         |
    Then response code is 400

    When User registers as below:
      | email | password | firstName | surname |
      | e@    | 1234     | Zbigniew  | Json    |
    Then response code is 400

    When User registers as below:
      | email | password | firstName | surname |
      | @b.pl | 1234     | Zbigniew  | Json    |
    Then response code is 400

    When User registers as below:
      | email | password | firstName | surname |
      | ""    | 1234     | Zbigniew  | Json    |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | ""       | Zbigniew  | Json    |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | 1234     | ""        | Json    |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | 1234     | Zbigniew  | ""      |
    Then response code is 400

    When User registers as below:
      | email  | password | firstName | surname |
      | e@b.pl | 1234     | Zbigniew  | Json    |
    Then response code is 204


  Scenario: Duplicate email
    Given User registers as below:
      | email              | password | firstName | surname |
      | user1@luncher.corp | 1234     | Zbigniew  | Json    |
    And response code is 204

    Given User registers as below:
      | email              | password | firstName | surname |
      | user1@luncher.corp | 12345    | Zbigniewa | Jsona   |
    And response code is 409
     
    