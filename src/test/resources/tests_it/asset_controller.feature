Feature: Assets controller

  Background:
    Given User exists:
      | uuid                                 | email                 | password | name | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp | 1234     | MOD  | Json2   | REST_MANAGER |

    And place types exist:
      | identifier | iconName   | name       |
      | RESTAURANT | restaurant | Restaurant |

    Given User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp


    And User creates a place as below:
      | name | description | placeTypeIdentifier |
      | name | descr       | RESTAURANT          |

    And response code is 200

  Scenario: User creates and deletes a gcp image asset
    When User requests creation of a new asset for the latest created place:
      | name          | description      | fileExtension |
      | Example image | Restaurants hall | jpeg          |

    Then response code is 200

    And Last created place has 1 asset:
      | name          | description      |
      | Example image | Restaurants hall |

    When User deletes last asset from last created place

    Then response code is 200

    And Last created place has 0 assets:
      |  |
