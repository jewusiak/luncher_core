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


  Scenario Outline: Bad requests
    When User registers as below:
      | email   | password   | firstName   | surname   |
      | <email> | <password> | <firstName> | <surname> |
    Then response code is <expectedCode>


    Examples:
      | email  | password | firstName | surname | expectedCode |
      | email  | password | firstName | surname | 400          |
      |        | 1234     | Zbigniew  | Json    | 400          |
      | e@b.pl |          | Zbigniew  | Json    | 400          |
      | e@b.pl | 1234     |           | Json    | 400          |
      | e@b.pl | 1234     | Zbigniew  |         | 400          |
      | e@b.pl | 1234     |           |         | 400          |
      | e@     | 1234     | Zbigniew  | Json    | 400          |
      | @b.pl  | 1234     | Zbigniew  | Json    | 400          |
      | ""     | 1234     | Zbigniew  | Json    | 400          |
      | e@b.pl | ""       | Zbigniew  | Json    | 400          |
      | e@b.pl | 1234     | ""        | Json    | 400          |
      | e@b.pl | 1234     | Zbigniew  | ""      | 400          |
      | e@b.pl | 1234     | Zbigniew  | Json    | 204          |

  Scenario: Duplicate email
    Given User registers as below:
      | email              | password | firstName | surname |
      | user1@luncher.corp | 1234     | Zbigniew  | Json    |
    And response code is 204

    Given User registers as below:
      | email              | password | firstName | surname |
      | user1@luncher.corp | 12345    | Zbigniewa | Jsona   |
    And response code is 409
     
