Feature: Assets controller

  Background:
    Given User exists:
      | uuid                                 | email                 | password | name    | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp | 1234     | MANAGER | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp      | 1234     | MOD     | Json2   | SYS_MOD      |

    And place types exist:
      | identifier | iconName   | name       |
      | RESTAURANT | restaurant | Restaurant |

    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below ID -1:
      | name | description | placeTypeIdentifier | enabled |
      | name | descr       | RESTAURANT          | true    |

    And response code is 200

  Scenario: User creates and deletes a gcp image asset
    When User requests creation of a new asset for Place ID -1 at ID -1:
      | description      |
      | Restaurants hall |

    Then response code is 200

    And Place ID -1 has 1 asset:
      | description      |
      | Restaurants hall |

    When User deletes Asset ID -1

    Then response code is 204

    And Place ID -1 has 0 assets:
      |  |

    Given User logs in using credentials:
      | email            | password |
      | mod@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as mod@luncher.corp

    When Send GET request to /asset/[ID:ASSET:-1] without body

    Then response code is 404
