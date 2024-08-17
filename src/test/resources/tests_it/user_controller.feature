Feature: Users

  Background:
    Given User exists:
      | uuid                                 | email                  | password | name     | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp      | 1234     | Zbigniew | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp  | 1234     | MAN      | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | rmanager2@luncher.corp | 1234     | MAN2     | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp       | 1234     | MOD      | Json2   | SYS_MOD      |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod2@luncher.corp      | 1234     | MOD      | Json2   | SYS_MOD      |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp     | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin2@luncher.corp    | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e9 | root@luncher.corp      | 1234     | ROOT     | Json2   | SYS_ROOT     |


  Scenario Template: non-sys user is created by sys-user and edited, deleted
    Given User logs in using credentials:
      | email          | password |
      | <sysUserEmail> | 1234     |
    And response code is 200
    And User is logged in as <sysUserEmail>

    When User is created as below with ID -1:
      | email              | password | firstName | surname | role |
      | user1@luncher.corp | 1234     | Zbigniew  | Json    | USER |
    Then response code is 200

    Then GET User with ID -1 is as below:
      | email              | firstName | surname | role |
      | user1@luncher.corp | Zbigniew  | Json    | USER |

    When User with ID -1 is edited as below:
      | email              | firstName |
      | usera@luncher.corp | Andrzej   |
    Then response code is 200

    Then GET User with ID -1 is as below:
      | email              | firstName | surname | role |
      | usera@luncher.corp | Andrzej   | Json    | USER |

    When User with ID -1 is deleted
    Then response code is 204


    When GET User with ID -1 is as below:
      | message    |
      | Not found! |

    Then response code is 404

    Examples:
      | sysUserEmail       |
      | mod@luncher.corp   |
      | admin@luncher.corp |

  Scenario Template: sys-users cannot edit users >= themselves
    Given User logs in using credentials:
      | email             | password |
      | root@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as root@luncher.corp

    When User is created as below with ID -1:
      | email              | password | firstName | surname | role             |
      | user1@luncher.corp | 1234     | Zbigniew  | Json    | <editedUserRole> |

    And response code is 200

    Given User logs in using credentials:
      | email            | password |
      | <requestingUser> | 1234     |
    And response code is 200
    And User is logged in as <requestingUser>

    When User with ID -1 is edited as below:
      | firstName |
      | aaaaa     |

    Then response code is <statusCode>

    Examples:
      | editedUserRole | requestingUser        | statusCode |
      | SYS_ROOT       | mod@luncher.corp      | 403        |
      | SYS_ADMIN      | mod@luncher.corp      | 403        |
      | SYS_MOD        | mod@luncher.corp      | 403        |
      | REST_MANAGER   | mod@luncher.corp      | 200        |
      | USER           | mod@luncher.corp      | 200        |
      | SYS_ROOT       | admin@luncher.corp    | 403        |
      | SYS_ADMIN      | admin@luncher.corp    | 403        |
      | SYS_MOD        | admin@luncher.corp    | 200        |
      | REST_MANAGER   | admin@luncher.corp    | 200        |
      | USER           | admin@luncher.corp    | 200        |
      | SYS_ROOT       | root@luncher.corp     | 200        |
      | SYS_ADMIN      | root@luncher.corp     | 200        |
      | SYS_MOD        | root@luncher.corp     | 200        |
      | REST_MANAGER   | root@luncher.corp     | 200        |
      | USER           | root@luncher.corp     | 200        |
      | USER           | rmanager@luncher.corp | 403        |
      | USER           | user@luncher.corp     | 403        |


  Scenario: Password reset of user
    Given User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as user@luncher.corp
    And Removed saved authentication token

    When User requests password reset for user user-unknown@luncher.corp
    Then response code is 404

    When User requests password reset for user user@luncher.corp
    Then response code is 200


    # valid reset request
    When User changes password using last received url to 2222gggbbb
    Then response code is 204

    # invalid reset request
    When User changes password using last received url to 1111111
    Then response code is 400

    When User logs in using credentials:
      | email             | password |
      | user@luncher.corp | 1234     |
    Then response code is 401

    When User logs in using credentials:
      | email             | password   |
      | user@luncher.corp | 2222gggbbb |
    Then response code is 200
    And User is logged in as user@luncher.corp
