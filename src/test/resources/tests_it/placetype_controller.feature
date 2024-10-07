Feature: Place types management

  Background:
    Given User exists:
      | uuid                                 | email                 | password | name     | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp     | 1234     | Zbigniew | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp | 1234     | MAN      | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp    | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp      | 1234     | MOD      | Json2   | SYS_MOD      |

  Scenario: CRUD actions
    Given User logs in using credentials:
      | email            | password |
      | mod@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as mod@luncher.corp

    When Send GET request to /placetype without body

    Then response code is 200
    And HTTP Response has a list of size 0 in path .

    When Send POST request to /placetype with body as below:
    """
    {
    "identifier":"BAR",
    "name":"Bar place",
    "iconName":"bar"
    }
    """

    Then response code is 200
    And HTTP Response is:
      | identifier | name      | iconName |
      | BAR        | Bar place | bar      |


    When Send GET request to /placetype without body

    Then response code is 200
    And HTTP Response has a list of size 1 in path .
    And HTTP Response is:
      | [0].identifier | [0].name  | [0].iconName |
      | BAR            | Bar place | bar          |

    When Send PUT request to /placetype/BAR with body as below:
    """
    {
    "iconName": "bar-2nd-icon"
    }
    """

    Then response code is 200

    When Send GET request to /placetype/BAR without body

    Then response code is 200
    And HTTP Response is:
      | identifier | name      | iconName     |
      | BAR        | Bar place | bar-2nd-icon |

    When Send DELETE request to /placetype/BAR without body
    Then response code is 204

    When Send GET request to /placetype without body

    Then response code is 200
    And HTTP Response has a list of size 0 in path .

  Scenario: On delete - places with this placetype exist
    Given User logs in using credentials:
      | email            | password |
      | mod@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as mod@luncher.corp

    And Send POST request to /placetype with body as below:
    """
    {
    "identifier":"BAR",
    "name":"Bar place",
    "iconName":"bar"
    }
    """

    And response code is 200

    And User creates a place as below ID -1:
      | name | description | placeTypeIdentifier |
      | name | descr       | BAR                 |

    And response code is 200


    When Send DELETE request to /placetype/BAR without body
    #FIXME: not 500 but 400
    Then response code is 500

    When Send GET request to /placetype without body

    Then response code is 200
    And HTTP Response has a list of size 1 in path .


  Scenario: Restricted actions to administrators
    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    When Send POST request to /placetype with body as below:
    """
    {
    "identifier":"RESTAURANT"
    }
    """

    Then response code is 403

    And Send GET request to /placetype without body

    And response code is 200
    And HTTP Response has a list of size 0 in path .
 

    